package com.magicliang.transaction.sys.biz.service.impl.handler;

import com.magicliang.transaction.sys.biz.service.impl.enums.OperationEnum;
import com.magicliang.transaction.sys.biz.service.impl.request.HandlerRequest;
import com.magicliang.transaction.sys.common.enums.TransPayOrderStatusEnum;
import com.magicliang.transaction.sys.common.type.IIdentifiableType;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.core.config.CommonConfig;
import com.magicliang.transaction.sys.core.domain.activity.acceptance.AcceptanceActivity;
import com.magicliang.transaction.sys.core.domain.activity.idgeneration.IdGenerationActivity;
import com.magicliang.transaction.sys.core.domain.activity.notification.NotificationActivity;
import com.magicliang.transaction.sys.core.domain.activity.payment.PaymentActivity;
import com.magicliang.transaction.sys.core.factory.ContextFactory;
import com.magicliang.transaction.sys.core.model.context.TransTransactionContext;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.service.IDistributedLock;
import com.magicliang.transaction.sys.core.service.IPayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.locks.Lock;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.*;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 基础处理器
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 15:17
 */
@Slf4j
public abstract class BaseHandler<R extends HandlerRequest, M extends TransactionModel, C extends TransTransactionContext<R, M>> implements IIdentifiableType<OperationEnum> {

    // ---------------------------------- 公共服务 ----------------------------------
    /**
     * 通用配置
     */
    @Autowired
    protected CommonConfig commonConfig;

    /**
     * 分布式锁
     */
    @Autowired
    protected IDistributedLock distributedLock;

    /**
     * 支付订单服务
     */
    @Autowired
    protected IPayOrderService payOrderService;

    // ---------------------------------- 领域活动 ----------------------------------

    /**
     * id 生成活动
     */
    @Autowired
    protected IdGenerationActivity idGenerationActivity;

    /**
     * 受理活动
     */
    @Autowired
    protected AcceptanceActivity acceptanceActivity;

    /**
     * 支付活动
     */
    @Autowired
    protected PaymentActivity paymentActivity;

    /**
     * 通知活动
     */
    @Autowired
    protected NotificationActivity notificationActivity;

    /**
     * 执行处理流程
     *
     * @param request 业务请求
     */
    public C execute(R request) {
        // 1. 加锁，所有请求都要在锁的保护下进行
        Lock lock = distributedLock.getLock(request.getIdempotentKey(), commonConfig.getLockExpiration());
        C context = null;
        lock.lock();
        try {
            // 2. 初始化上下文
            context = initContext(request);
            // 如果上下文提前结束，不执行本处理器的后续逻辑
            if (context.isComplete()) {
                return context;
            }
            // 3. 前置执行
            preExecution(context);
            // 4. 真处理操作
            realExecute(context);

            // 8. 返回上下文
            return context;
        } finally {
            // 5. 执行后置操作
            postExecution(context);
            // 6. 解锁
            lock.unlock();
            // 7. 清理上下文
            ContextFactory.clear();
        }
    }

    /**
     * 初始化上下文，上下文级别的幂等检查要在这里执行
     *
     * @param request 处理器上下文
     */
    public abstract C initContext(R request);

    /**
     * 执行前置操作
     *
     * @param context 处理器上下文
     */
    protected void preExecution(final C context) {
        log.info("pre execution ");
    }

    /**
     * 真处理操作
     *
     * @param context 处理器上下文
     */
    public abstract void realExecute(final C context);

    /**
     * 执行后置操作
     *
     * @param context 处理器上下文
     */
    protected void postExecution(final C context) {
        log.info("post execution ");
    }

    /**
     * 在分布式锁里执行锁回调
     *
     * @param idempotentKey 幂等键
     * @param runnable      可执行回调
     */
    protected void lockAround(final String idempotentKey, final Runnable runnable) {
        lockAround(idempotentKey, commonConfig.getLockExpiration(), runnable);
    }

    /**
     * 在带超时时间的分布式锁里执行锁回调
     *
     * @param idempotentKey  幂等键
     * @param lockExpiration 锁超时时间
     * @param runnable       可执行回调
     */
    protected void lockAround(final String idempotentKey, Integer lockExpiration, final Runnable runnable) {
        // 防御性编程，如果不带有幂等键则不执行
        AssertUtils.assertNotBlank(idempotentKey, INVALID_IDEMPOTENT_KEY_ERROR, idempotentKey);
        lockExpiration = lockExpiration == null ? commonConfig.getLockExpiration() : lockExpiration;
        distributedLock.lockAndRun(idempotentKey, lockExpiration, runnable);
    }

    /**
     * 填充全模型，不同的处理器的全模型定义不一样
     *
     * @param bizIdentifyNo 业务识别码
     * @param bizUniqueNo   业务唯一标识
     * @return 全领域模型
     */
    protected M populateNecessaryModel(final String bizIdentifyNo, final String bizUniqueNo) {
        return null;
    }

    /**
     * 校验已经进入不成功终态的数据
     * 只有需要写支付订单（如受理、支付，回调）才需要使用这个方法，基于支付订单进行通知不需要使用这个方法（如单纯通知）
     *
     * @param transactionModel 交易模型
     */
    protected void getErrorMessage(final TransactionModel transactionModel) {
        final TransPayOrderEntity payOrderEntity = transactionModel.getPayOrder();
        TransPayOrderStatusEnum payOrderStatus = TransPayOrderStatusEnum.getByCode(payOrderEntity.getStatus().intValue());
        // 执行失败
        if (TransPayOrderStatusEnum.FAILED == payOrderStatus) {
            setErrorCode(transactionModel, payOrderEntity);
            transactionModel.setErrorMsg(PAYMENT_FAILURE_ERROR.getSynthesizedErrorCode());
        } else if (TransPayOrderStatusEnum.CLOSED == payOrderStatus) {
            setErrorCode(transactionModel, payOrderEntity);
            transactionModel.setErrorMsg(PAYMENT_CLOSED_ERROR.getSynthesizedErrorCode());
        } else if (TransPayOrderStatusEnum.BOUNCED == payOrderStatus) {
            setErrorCode(transactionModel, payOrderEntity);
            transactionModel.setErrorMsg(PAYMENT_BOUNCED_ERROR.getSynthesizedErrorCode());
        }
    }

    /**
     * 校验是否已经被退票
     * 只有同步语义（如受理、支付，回调）才需要使用这个方法，异步语义不需要使用这个方法（如单纯通知）
     *
     * @param transactionModel 交易模型
     */
    protected boolean checkIsBounced(final TransactionModel transactionModel) {
        final TransPayOrderEntity payOrderEntity = transactionModel.getPayOrder();
        TransPayOrderStatusEnum payOrderStatus = TransPayOrderStatusEnum.getByCode(payOrderEntity.getStatus().intValue());
        if (TransPayOrderStatusEnum.BOUNCED == payOrderStatus) {
            setErrorCode(transactionModel, payOrderEntity);
            transactionModel.setErrorMsg(PAYMENT_BOUNCED_ERROR.getSynthesizedErrorCode());
            return true;
        }
        return false;
    }

    /**
     * 设置错误码
     *
     * @param transactionModel 交易模型
     * @param payOrderEntity   支付订单
     */
    private void setErrorCode(final TransactionModel transactionModel, final TransPayOrderEntity payOrderEntity) {
        transactionModel.setErrorMsg("channelErrorCode:" + payOrderEntity.getChannelErrorCode());
    }
}
