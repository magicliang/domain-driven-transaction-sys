package com.magicliang.transaction.sys.biz.service.impl.facade.impl;

import com.magicliang.transaction.sys.biz.service.impl.facade.INotificationFacade;
import com.magicliang.transaction.sys.biz.shared.request.notification.NotificationCommand;
import com.magicliang.transaction.sys.biz.shared.request.notification.UnSentNotificationQuery;
import com.magicliang.transaction.sys.biz.shared.request.notification.convertor.NotificationCommandConvertor;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 通知门面实现
 * fixme：这里的任务处理模型要仔细重写过
 *
 * @author magicliang
 * <p>
 * date: 2022-01-25 17:49
 */
@Slf4j
@Service
public class NotificationFacadeImpl extends AbstractConcurrentFacade implements INotificationFacade {

    /**
     * 单线程每秒支付吞吐量，假定每个线程每秒可以通知 10 次
     */
    private static final int SINGLE_THREAD_NOTIFICATION_THROUGHPUT_PER_SECOND = 10;

    /**
     * 线程数
     */
    private static final int THREAD_COUNT = 100;
    /**
     * 批量支付线程池
     */
    @Getter
//    @CustomExecutorService(id = "batchNotifyPool", corePoolSize = THREAD_COUNT, maximumPoolSize = THREAD_COUNT, workQueueCapacity = 1500)
    private ExecutorService executorService;

    /**
     * 查询并发送全部未完成通知
     * IMPORTANT：如果这个翻页的功能是对外暴露的接口，接口在使用参数以前一定要做好页数的校验
     *
     * @param unSentNotificationQuery 未完成通知
     * @return 操作结果
     */
    @Override
    public boolean batchNotify(UnSentNotificationQuery unSentNotificationQuery) {
        int batchSize = unSentNotificationQuery.getBatchSize();
        int env = unSentNotificationQuery.getEnv();

        long approximateUnSentCount = payOrderService.countUnSentNotifications();

        // 弹性计算锁的超时时间
        int lockExpirationInSeconds = (int) estimateElapsedSeconds(THREAD_COUNT,
                approximateUnSentCount, SINGLE_THREAD_NOTIFICATION_THROUGHPUT_PER_SECOND);
        int loop = (int) Math.ceil((double) approximateUnSentCount / batchSize);

        distributedLock.tryLock("IPaymentFacade.batchPay", lockExpirationInSeconds,
                () -> {
                    for (int i = 0; i < loop; i++) {
                        List<TransRequestEntity> notifications = payOrderService.populateUnSentNotifications(batchSize, env);
                        if (CollectionUtils.isEmpty(notifications)) {
                            return;
                        }

                        List<TransPayOrderEntity> payOrders = payOrderService.populateWholePayOrders(notifications);

                        batchNotify(payOrders);
                    }
                },
                () -> log.info("批量通知求锁失败，等待下次调度"));
        return true;
    }

    /**
     * 批量发送全部未完成通知，使用线程池来执行发送任务
     *
     * @param payOrders 待发送通知的支付订单列表
     */
    @Override
    public void batchNotify(List<TransPayOrderEntity> payOrders) {
        // 转化为任务
        List<Callable<TransactionModel>> tasks = mapToTasks(payOrders);

        // 执行所有任务
        invokeAll(tasks);
    }

    /**
     * 执行通知
     *
     * @param notificationCommand 通知命令
     * @return 交易模型
     */
    @Override
    public TransactionModel notify(NotificationCommand notificationCommand) {
        return commandQueryBus.send(notificationCommand);
    }

    /**
     * 生成通知任务
     *
     * @param payOrders 待发送通知的支付订单列表
     * @return 通知任务列表
     */
    private List<Callable<TransactionModel>> mapToTasks(final List<TransPayOrderEntity> payOrders) {

        // 按照顺序生成任务
        return payOrders.stream().map((payOrder) -> (Callable<TransactionModel>) () -> {
            NotificationCommand notificationCommand = NotificationCommandConvertor.fromDomainEntity(payOrder);
            return notify(notificationCommand);
        }).collect(Collectors.toList());
    }
}
