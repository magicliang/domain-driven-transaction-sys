package com.magicliang.transaction.sys.common.service.facade.impl;

import com.magicliang.transaction.sys.biz.service.impl.request.notification.NotificationCommand;
import com.magicliang.transaction.sys.biz.service.impl.request.notification.convertor.NotificationCommandConvertor;
import com.magicliang.transaction.sys.biz.service.impl.request.payment.PaymentCommand;
import com.magicliang.transaction.sys.biz.service.impl.request.payment.UnPaidOrderQuery;
import com.magicliang.transaction.sys.biz.service.impl.request.payment.convertor.PaymentCommandConvertor;
import com.magicliang.transaction.sys.common.service.facade.INotificationFacade;
import com.magicliang.transaction.sys.common.service.facade.IPaymentFacade;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付门面实现
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 16:57
 */
@Slf4j
@Service
public class PaymentFacadeImpl extends AbstractConcurrentFacade implements IPaymentFacade {

    /**
     * 单线程每秒支付吞吐量，支付的tp99是 100ms，再加上大约 40ms 的写数据库时间，每秒大约可以发起七笔支付
     */
    private static final int SINGLE_THREAD_PAYMENT_THROUGHPUT_PER_SECOND = 7;

    /**
     * 线程数
     */
    private static final int THREAD_COUNT = 300;

    /**
     * 批量支付线程池
     */
    @Getter
//    @CustomExecutorService(id = "batchPayPool", corePoolSize = THREAD_COUNT, maximumPoolSize = THREAD_COUNT, workQueueCapacity = 3000)
    private ExecutorService executorService;

    /**
     * 通知门面
     */
    @Autowired
    private INotificationFacade notificationFacade;

    /**
     * 查询并支付全部未支付订单
     *
     * @param unPaidOrderQuery 未支付订单查询
     * @return 操作结果
     */
    @Override
    public boolean batchPay(final UnPaidOrderQuery unPaidOrderQuery) {
        int batchSize = unPaidOrderQuery.getBatchSize();
        int env = unPaidOrderQuery.getEnv();
        // 在无锁的情况下，查询未支付订单的近似值
        long approximateUnpaidCount = payOrderService.countUnPaidRequests();
        // 弹性计算锁的超时时间
        int lockExpirationInSeconds = (int) estimateElapsedSeconds(THREAD_COUNT,
                approximateUnpaidCount, SINGLE_THREAD_PAYMENT_THROUGHPUT_PER_SECOND);
        int loop = (int) Math.ceil((double) approximateUnpaidCount / batchSize);
        // FIXME：注意，嵌套锁可能出问题，要密切注意以后才处理！
        distributedLock.tryLock("IPaymentFacade.batchPay", lockExpirationInSeconds,
                () -> {
                    for (int i = 0; i < loop; i++) {
                        List<TransRequestEntity> requests = payOrderService.populateUnpaidRequest(batchSize, env);

                        // 如果已经取不到未支付请求，则不再继续执行下去，跳出死循环
                        if (CollectionUtils.isEmpty(requests)) {
                            return;
                        }
                        List<TransPayOrderEntity> payOrders = payOrderService.populateWholePayOrders(requests);

                        batchPay(payOrders);
                    }
                },
                () -> log.info("批量支付求锁失败，等待下次调度"));
        return true;
    }

    /**
     * 批量支付订单，使用线程池来执行支付任务
     *
     * @param payOrders 待支付订单列表
     */
    @Override
    public void batchPay(final List<TransPayOrderEntity> payOrders) {
        // 转化为任务
        List<Callable<TransactionModel>> tasks = mapToTasks(payOrders);

        // 执行所有任务
        invokeAll(tasks);
    }

    /**
     * 支付单一订单
     *
     * @param paymentCommand 支付命令
     * @return 交易模型
     */
    @Override
    public TransactionModel pay(PaymentCommand paymentCommand) {
        return commandQueryBus.send(paymentCommand);
    }

    /**
     * 异步支付单一订单
     *
     * @param paymentCommand 支付命令
     */
    @Override
    public void asyncPay(PaymentCommand paymentCommand) {
        getExecutorService().submit(() -> payAndNotify(paymentCommand));
    }

    /**
     * 支付单一订单
     *
     * @param paymentCommand 支付命令
     * @return 交易模型
     */
    @Override
    public TransactionModel payAndNotify(PaymentCommand paymentCommand) {
        TransactionModel transactionModel = pay(paymentCommand);
        if (transactionModel.isSuccess()) {
            TransPayOrderEntity payOrder = transactionModel.getPayOrder();
            getExecutorService().submit(() -> {
                final NotificationCommand notificationCommand = NotificationCommandConvertor.fromDomainEntity(payOrder);
                notificationFacade.notify(notificationCommand);
            });
        }
        return transactionModel;
    }

    /**
     * 用支付订单生成支付任务
     *
     * @param payOrders 支付订单列表
     * @return 支付任务列表
     */
    private List<Callable<TransactionModel>> mapToTasks(final List<TransPayOrderEntity> payOrders) {

        // 按照顺序生成任务
        return payOrders.stream().map((payOrder) -> (Callable<TransactionModel>) () -> {
            final Thread thread = Thread.currentThread();
            PaymentCommand paymentCommand = PaymentCommandConvertor.fromDomainEntity(payOrder);
            return pay(paymentCommand);
        }).collect(Collectors.toList());
    }

}
