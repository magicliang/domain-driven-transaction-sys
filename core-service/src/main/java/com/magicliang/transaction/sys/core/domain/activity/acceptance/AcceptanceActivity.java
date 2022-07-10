package com.magicliang.transaction.sys.core.domain.activity.acceptance;

import com.magicliang.transaction.sys.common.constant.TransConstant;
import com.magicliang.transaction.sys.common.enums.TransEnvEnum;
import com.magicliang.transaction.sys.common.enums.TransPayOrderStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestStatusEnum;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.common.util.JsonUtils;
import com.magicliang.transaction.sys.core.domain.activity.BaseActivity;
import com.magicliang.transaction.sys.core.domain.enums.AcceptanceStrategyEnum;
import com.magicliang.transaction.sys.core.domain.strategy.DomainStrategy;
import com.magicliang.transaction.sys.core.model.context.TransTransactionContext;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import com.magicliang.transaction.sys.core.model.entity.TransSubOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.validator.TransPayOrderValidator;
import com.magicliang.transaction.sys.core.model.entity.validator.TransRequestValidator;
import com.magicliang.transaction.sys.core.model.entity.validator.TransSubOrderValidator;
import com.magicliang.transaction.sys.core.model.request.acceptance.AcceptanceRequest;
import com.magicliang.transaction.sys.core.model.response.acceptance.AcceptanceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.*;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 受理活动
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 11:21
 */
@Slf4j
@Component
public class AcceptanceActivity extends BaseActivity<AcceptanceRequest, AcceptanceResponse, AcceptanceStrategyEnum> {

    /**
     * 领域策略
     */
    @Autowired
    private List<DomainStrategy<AcceptanceRequest, AcceptanceResponse, AcceptanceStrategyEnum>> strategies;

    /**
     * 活动执行以前，执行前置的平衡性检查，并填充领域模型，活动级的幂等在这里执行，模型的旧值校验在这里执行
     *
     * @param context 交易上下文
     */
    @Override
    protected boolean preExecution(final TransTransactionContext<?, ? extends TransactionModel> context) {
        // merge 父类的 isComplete 和本上下文的 isComplete 则可以保证中间件可以通过提前设置 complete 来编排和调度流程
        context.setAcceptanceComplete(super.isComplete(context) || context.isAcceptanceComplete());
        if (context.isAcceptanceComplete()) {
            return true;
        }

        // 1. 执行父类检查，父类检查的 isComplete 不予采用
        super.preExecution(context);

        // 2. 检查模型
        TransactionModel model = context.getModel();

        // ------------------------------ 所有支付信息的实体完整性都在这里校验，而不在支付时再校验 ------------------------------

        // 3. 检查支付订单
        final TransPayOrderEntity payOrder = model.getPayOrder();
        TransPayOrderValidator.validateBeforeInsert(payOrder);

        // 4. 检查子订单
        TransSubOrderEntity subOrder = payOrder.getSubOrder();
        TransSubOrderValidator.validateBeforeInsert(subOrder);

        // 5. 检查支付请求
        final TransRequestEntity paymentRequest = payOrder.getPaymentRequest();
        AssertUtils.assertNotNull(paymentRequest, INVALID_PAYMENT_REQUEST_ERROR, "invalid paymentRequest:" + paymentRequest);
        TransRequestValidator.validateBeforeInsert(paymentRequest);

        // 6. 校验决策出来的策略
        AssertUtils.assertNotNull(decideStrategy(context), INVALID_ACCEPTANCE_STRATEGY_ERROR, JsonUtils.toJson(context));

        // 7. 完成当前钩子
        return context.isAcceptanceComplete();
    }

    /**
     * 生成领域能力请求，子类型必须实现这个方法，将模型旧值转换为新值在这里实现
     *
     * @param context 交易上下文
     * @return 领域能力请求
     */
    @Override
    protected AcceptanceRequest assembleDomainRequest(final TransTransactionContext<?, ? extends TransactionModel> context) {
        AcceptanceRequest request = context.getAcceptanceRequest();
        TransactionModel model = context.getModel();
        final TransPayOrderEntity payOrder = model.getPayOrder();
        assemblePayOrder(payOrder);
        assemblePaymentRequest(payOrder);
        request.setTransPayOrder(payOrder);
        return request;
    }

    /**
     * 生成领域能力响应，子类型必须实现这个方法
     *
     * @param context 交易上下文
     * @return 领域能力响应
     */
    @Override
    protected AcceptanceResponse assembleDomainResponse(final TransTransactionContext<?, ? extends TransactionModel> context) {
        return context.getAcceptanceResponse();
    }

    /**
     * 决策当前活动的策略点，子类型必须实现这个方法
     *
     * @param context 交易上下文
     * @return 作为决策结果的策略点
     */
    @Override
    protected AcceptanceStrategyEnum decideStrategy(final TransTransactionContext<?, ? extends TransactionModel> context) {
        return AcceptanceStrategyEnum.SYNC;
    }

    /**
     * get the value of strategies
     *
     * @return the value of strategies
     */
    @Override
    public List<DomainStrategy<AcceptanceRequest, AcceptanceResponse, AcceptanceStrategyEnum>> getStrategies() {
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
        // 2. 断言处理结果
        AcceptanceResponse acceptanceResponse = context.getAcceptanceResponse();
        final Long acceptedPayOrderNo = acceptanceResponse.getAcceptedPayOrderNo();
        AssertUtils.assertNotNull(acceptedPayOrderNo, ACCEPTANCE_FAILURE_ERROR, "invalid acceptedPayOrderNo:" + acceptedPayOrderNo);
        // 3. 完成当前钩子，一般情况下，只有在当前 activity 执行完之后，才可以调用 setAcceptanceComplete
        context.setAcceptanceComplete(true);
    }

    /**
     * 在请求前更新本活动需要的 payOrder 的状态、时间
     *
     * @param payOrder payOrder
     */
    private void assemblePayOrder(final TransPayOrderEntity payOrder) {
        Date now = new Date();
        payOrder.setGmtCreated(now);
        payOrder.setGmtModified(now);
        payOrder.setGmtAcceptedTime(now);
        payOrder.updateStatus(TransPayOrderStatusEnum.INIT.getCode());
        payOrder.setVersion(TransConstant.INIT_VERSION);
        TransEnvEnum env = TransEnvEnum.getByDesc(commonConfig.getEnv());
        if (null != env) {
            payOrder.setEnv(env.getCode().shortValue());
        }
    }

    /**
     * 在请求前更新本活动需要的 paymentRequest 的状态、时间
     *
     * @param payOrder payOrder
     */
    private void assemblePaymentRequest(final TransPayOrderEntity payOrder) {
        TransRequestEntity paymentRequest = payOrder.getPaymentRequest();
        paymentRequest.setRetryCount(0L);
        paymentRequest.updateStatus(TransRequestStatusEnum.INIT.getCode());
        paymentRequest.setGmtCreated(payOrder.getGmtCreated());
        paymentRequest.setGmtModified(payOrder.getGmtModified());
        // 默认当前插入的支付请求就是可执行的
        paymentRequest.setGmtNextExecution(paymentRequest.getGmtCreated());
        paymentRequest.setEnv(payOrder.getEnv());
    }
}
