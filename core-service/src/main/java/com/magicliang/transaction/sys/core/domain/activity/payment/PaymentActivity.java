package com.magicliang.transaction.sys.core.domain.activity.payment;

import com.magicliang.transaction.sys.common.enums.TransPayOrderStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestStatusEnum;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.common.util.JsonUtils;
import com.magicliang.transaction.sys.core.domain.activity.BaseActivity;
import com.magicliang.transaction.sys.core.domain.enums.PaymentStrategyEnum;
import com.magicliang.transaction.sys.core.domain.strategy.DomainStrategy;
import com.magicliang.transaction.sys.core.model.context.TransTransactionContext;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import com.magicliang.transaction.sys.core.model.request.payment.PaymentRequest;
import com.magicliang.transaction.sys.core.model.response.payment.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.*;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付活动
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 13:27
 */
@Slf4j
@Component
public class PaymentActivity extends BaseActivity<PaymentRequest, PaymentResponse, PaymentStrategyEnum> {

    /**
     * 领域策略
     */
    @Autowired
    private List<DomainStrategy<PaymentRequest, PaymentResponse, PaymentStrategyEnum>> strategies;

    /**
     * 活动执行以前，执行前置的平衡性检查，并填充领域模型，活动级的幂等在这里执行，模型的旧值校验在这里执行
     * 本活动不校验前置活动已经准备好的支付参数
     *
     * @param context 交易上下文
     */
    @Override
    protected boolean preExecution(final TransTransactionContext<?, ? extends TransactionModel> context) {
        // 1. 确定是否提前结束本活动
        context.setPaymentComplete(super.isComplete(context) || context.isPaymentComplete());
        if (context.isPaymentComplete()) {
            return true;
        }

        // 2. 执行父类检查
        super.preExecution(context);

        // 3. 检查模型
        TransactionModel transactionModel = context.getModel();
        final TransPayOrderEntity payOrder = transactionModel.getPayOrder();
        AssertUtils.assertNotNull(payOrder, INVALID_PAY_ORDER_ERROR, "invalid payOrder:" + payOrder);

        // 4. 局部幂等校验，如果目标模型已经进入终态，则不再执行活动
        if (TransPayOrderStatusEnum.isFinalStatus(payOrder.getStatus().intValue())) {
            context.setPaymentComplete(true);
            return true;
        }
        TransRequestEntity paymentRequest = payOrder.getPaymentRequest();
        AssertUtils.assertNotNull(paymentRequest, INVALID_PAYMENT_REQUEST_ERROR,
                "invalid paymentRequest:" + paymentRequest);
        // 如果支付订单是中间态，支付请求成功了，也提前返回
        if (TransRequestStatusEnum.isFinalStatus(paymentRequest.getStatus().intValue())) {
            context.setPaymentComplete(true);
            return true;
        }

        // 5. 校验决策出来的策略
        AssertUtils.assertNotNull(decideStrategy(context), INVALID_PAYMENT_STRATEGY_ERROR, JsonUtils.toJson(context));

        // 6. 完成当前钩子
        return context.isPaymentComplete();
    }

    /**
     * 生成领域能力请求，子类型必须实现这个方法，将模型旧值转换为新值在这里实现
     *
     * @param context 交易上下文
     * @return 领域能力请求
     */
    @Override
    protected PaymentRequest assembleDomainRequest(final TransTransactionContext<?, ? extends TransactionModel> context) {
        final TransactionModel model = context.getModel();
        final TransPayOrderEntity payOrder = model.getPayOrder();
        PaymentRequest paymentRequest = context.getPaymentRequest();
        // 更新支付订单
        assemblePayOrderBeforePay(payOrder);
        // 更新支付请求
        assemblePaymentRequestBeforePay(payOrder);
        paymentRequest.setInsPayOrder(payOrder);

        return paymentRequest;
    }

    /**
     * 生成领域能力响应，子类型必须实现这个方法
     *
     * @param context 交易上下文
     * @return 领域能力响应
     */
    @Override
    protected PaymentResponse assembleDomainResponse(final TransTransactionContext<?, ? extends TransactionModel> context) {
        return context.getPaymentResponse();
    }

    /**
     * 决策当前活动的策略点，子类型必须实现这个方法。
     * 未来可以在框架层接入流程引擎，靠流程引擎来获取策略点。
     *
     * @param context 交易上下文
     * @return 作为决策结果的策略点
     */
    @Override
    protected PaymentStrategyEnum decideStrategy(final TransTransactionContext<?, ? extends TransactionModel> context) {
        // TODO：以后用中间件来表达这个扩展点
        return PaymentStrategyEnum.AlipayBalance;
    }

    /**
     * get the value of strategies
     *
     * @return the value of strategies
     */
    @Override
    public List<DomainStrategy<PaymentRequest, PaymentResponse, PaymentStrategyEnum>> getStrategies() {
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
        final TransactionModel model = context.getModel();
        final TransPayOrderEntity payOrder = model.getPayOrder();

        // 2.检查活动响应
        final PaymentResponse paymentResponse = context.getPaymentResponse();
        final String channelPaymentTraceNo = paymentResponse.getChannelPaymentTraceNo();
        AssertUtils.assertNotBlank(channelPaymentTraceNo, INVALID_CHANNEL_PAYMENT_TRACE_NO_ERROR, "invalid channelPaymentTraceNo：" + channelPaymentTraceNo);

        // 3. 影响其他活动：支付未到终态，则不需要立即通知
        final Short payOrderStatus = payOrder.getStatus();
        context.setNotificationComplete(!TransPayOrderStatusEnum.isFinalStatus(payOrderStatus.intValue()));

        // 4. 完成当前钩子
        context.setPaymentComplete(true);
    }

    /**
     * 在请求前更新本活动需要的 payOrder 的状态、时间
     *
     * @param payOrder payOrder
     */
    private void assemblePayOrderBeforePay(final TransPayOrderEntity payOrder) {
        Date now = new Date();
        // 迁移状态
        payOrder.updateStatus(TransPayOrderStatusEnum.PENDING.getCode());
        // 变更时间
        payOrder.setGmtModified(now);
        payOrder.setGmtPaymentBeginTime(now);
    }


    /**
     * 在请求前更新本活动需要的 paymentRequest 的状态、时间
     *
     * @param payOrder payOrder
     */
    private void assemblePaymentRequestBeforePay(final TransPayOrderEntity payOrder) {
        // 支付请求
        TransRequestEntity paymentRequest = payOrder.getPaymentRequest();
        // 迁移状态
        paymentRequest.updateStatus(TransRequestStatusEnum.PENDING.getCode());
        // 变更时间
        paymentRequest.setGmtModified(payOrder.getGmtModified());
        paymentRequest.setGmtLastExecution(payOrder.getGmtModified());
        // 变更重试次数
        paymentRequest.setRetryCount(paymentRequest.getRetryCount() + 1);
    }
}