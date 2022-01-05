package com.magicliang.transaction.sys.core.domain.activity.notification;

import com.magicliang.transaction.sys.common.enums.TransPayOrderStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestStatusEnum;
import com.magicliang.transaction.sys.common.exception.BaseTransException;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.common.util.JsonUtils;
import com.magicliang.transaction.sys.core.domain.activity.BaseActivity;
import com.magicliang.transaction.sys.core.domain.enums.NotificationStrategyEnum;
import com.magicliang.transaction.sys.core.domain.strategy.DomainStrategy;
import com.magicliang.transaction.sys.core.model.context.TransTransactionContext;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import com.magicliang.transaction.sys.core.model.entity.helper.PayOrderHelper;
import com.magicliang.transaction.sys.core.model.request.notification.NotificationRequest;
import com.magicliang.transaction.sys.core.model.response.notification.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.*;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 通知活动
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 12:06
 */
@Slf4j
@Component
public class NotificationActivity extends BaseActivity<NotificationRequest, NotificationResponse, NotificationStrategyEnum> {

    /**
     * 领域策略
     */
    @Autowired
    private List<DomainStrategy<NotificationRequest, NotificationResponse, NotificationStrategyEnum>> strategies;

    /**
     * 活动执行以前，执行前置的平衡性检查，并填充领域模型，活动级的幂等在这里执行，模型的旧值校验在这里执行
     *
     * @param context 交易上下文
     */
    @Override
    protected boolean preExecution(final TransTransactionContext<?, ? extends TransactionModel> context) {
        context.setNotificationComplete(super.isComplete(context) || context.isNotificationComplete());
        if (context.isNotificationComplete()) {
            return true;
        }

        // 1. 执行父类检查
        super.preExecution(context);

        // 3. 检查模型
        final TransactionModel transactionModel = context.getModel();
        final TransPayOrderEntity payOrderEntity = transactionModel.getPayOrder();
        AssertUtils.assertNotNull(payOrderEntity, INVALID_PAY_ORDER_ERROR, "invalid payOrder: " + payOrderEntity);

        // 非终态的支付订单不允许通知
        if (!TransPayOrderStatusEnum.isFinalStatus(payOrderEntity.getStatus().intValue())) {
            throw new BaseTransException(INVALID_PAY_ORDER_ERROR, "invalid pay order status: " + payOrderEntity);
        }

        List<TransRequestEntity> notificationRequests = payOrderEntity.getNotificationRequests();
        AssertUtils.assertNotNull(notificationRequests, INVALID_NOTIFICATION_REQUEST_ERROR, "invalid notificationRequest" + notificationRequests);

        // 4. 局部幂等校验，如果目标模型已经进入终态，则不再执行活动
        if (CollectionUtils.isEmpty(PayOrderHelper.getUnsentNotificationRequests(payOrderEntity))) {
            context.setNotificationComplete(true);
            return true;
        }

        // 5. 校验决策出来的策略
        AssertUtils.assertNotNull(decideStrategy(context), INVALID_PAYMENT_STRATEGY_ERROR, JsonUtils.toJson(context));
        return context.isNotificationComplete();
    }

    /**
     * 生成领域能力请求，子类型必须实现这个方法，将模型旧值转换为新值在这里实现
     *
     * @param context 交易上下文
     * @return 领域能力请求
     */
    @Override
    protected NotificationRequest assembleDomainRequest(final TransTransactionContext<?, ? extends TransactionModel> context) {
        NotificationRequest notificationRequest = context.getNotificationRequest();
        TransactionModel model = context.getModel();
        final TransPayOrderEntity payOrder = model.getPayOrder();
        notificationRequest.setTransPayOrder(payOrder);

        // 本活动只涉及通知请求，不涉及支付订单，只更新通知请求
        assembleRequestBeforeNotification(payOrder);

        return notificationRequest;
    }

    /**
     * 在请求前更新本活动需要的 notificationRequest 的状态、时间
     *
     * @param payOrder 支付订单
     */
    private void assembleRequestBeforeNotification(final TransPayOrderEntity payOrder) {
        List<TransRequestEntity> notifications = PayOrderHelper.getUnsentNotificationRequests(payOrder);

        notifications.forEach((notification) -> {
            // 迁移状态
            notification.updateStatus(TransRequestStatusEnum.PENDING.getCode());
            // 变更时间
            Date now = new Date();
            notification.setGmtModified(now);
            notification.setGmtLastExecution(now);
            // 变更重试次数
            notification.setRetryCount(notification.getRetryCount() + 1);
        });

    }

    /**
     * 生成领域能力响应，子类型必须实现这个方法
     *
     * @param context 交易上下文
     * @return 领域能力响应
     */
    @Override
    protected NotificationResponse assembleDomainResponse(final TransTransactionContext<?, ? extends TransactionModel> context) {
        return context.getNotificationResponse();
    }

    /**
     * 决策当前活动的策略点，子类型必须实现这个方法。
     * 未来可以在框架层接入流程引擎，靠流程引擎来获取策略点。
     *
     * @param context 交易上下文
     * @return 作为决策结果的策略点
     */
    @Override
    protected NotificationStrategyEnum decideStrategy(final TransTransactionContext<?, ? extends TransactionModel> context) {
        return NotificationStrategyEnum.RPC;
    }

    /**
     * get the value of strategies
     *
     * @return the value of strategies
     */
    @Override
    public List<DomainStrategy<NotificationRequest, NotificationResponse, NotificationStrategyEnum>> getStrategies() {
        return strategies;
    }

    /**
     * 活动执行以后，执行后置的平衡性检查和结果对领域模型的装填
     *
     * @param context 交易上下文
     */
    @Override
    protected void postExecution(final TransTransactionContext<?, ? extends TransactionModel> context) {
        // 1. 父类后处理
        super.postExecution(context);
        // 2.检查活动响应
        final NotificationResponse paymentResponse = context.getNotificationResponse();
        AssertUtils.isTrue(paymentResponse.isNotificationSuccess(), NOTIFICATION_FAILURE_ERROR, NOTIFICATION_FAILURE_ERROR.getErrorMsg());
        // 3. 完成当前钩子
        context.setNotificationComplete(true);
    }
}
