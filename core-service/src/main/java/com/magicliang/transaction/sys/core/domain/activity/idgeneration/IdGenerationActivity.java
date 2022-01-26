package com.magicliang.transaction.sys.core.domain.activity.idgeneration;

import com.magicliang.transaction.sys.common.service.integration.constant.IntegrationConstant;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import com.magicliang.transaction.sys.common.util.JsonUtils;
import com.magicliang.transaction.sys.core.domain.activity.BaseActivity;
import com.magicliang.transaction.sys.core.domain.enums.IdGenerationStrategyEnum;
import com.magicliang.transaction.sys.core.domain.strategy.DomainStrategy;
import com.magicliang.transaction.sys.core.model.context.TransTransactionContext;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import com.magicliang.transaction.sys.core.model.entity.TransSubOrderEntity;
import com.magicliang.transaction.sys.core.model.request.idgeneration.IdGenerationRequest;
import com.magicliang.transaction.sys.core.model.response.idgeneration.IdGenerationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.*;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: id 生成活动
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 12:01
 */
@Slf4j
@Component
public class IdGenerationActivity extends BaseActivity<IdGenerationRequest, IdGenerationResponse, IdGenerationStrategyEnum> {

    /**
     * 领域策略
     */
    @Autowired
    private List<DomainStrategy<IdGenerationRequest, IdGenerationResponse, IdGenerationStrategyEnum>> strategies;

    /**
     * 活动执行以前，执行前置的平衡性检查，并填充领域模型，活动级的幂等在这里执行，模型的旧值校验在这里执行
     *
     * @param context 交易上下文
     */
    @Override
    protected boolean preExecution(final TransTransactionContext<?, ? extends TransactionModel> context) {
        context.setIdGenerationComplete(super.isComplete(context) || context.isIdGenerationComplete());
        if (context.isIdGenerationComplete()) {
            return true;
        }

        // 1. 执行父类检查
        super.preExecution(context);
        // 2. 检查模型
        TransactionModel model = context.getModel();

        // 3. 检查支付订单
        final TransPayOrderEntity payOrder = model.getPayOrder();
        AssertUtils.assertNotNull(payOrder, INVALID_PAY_ORDER_ERROR, "invalid payOrder:" + payOrder);

        // 4. 校验决策出来的策略
        AssertUtils.assertNotNull(decideStrategy(context), INVALID_GENERATION_STRATEGY_ERROR, JsonUtils.toJson(context));

        // 5. 完成当前钩子
        return context.isIdGenerationComplete();
    }

    /**
     * 生成领域能力请求，子类型必须实现这个方法，将模型旧值转换为新值在这里实现
     *
     * @param context 交易上下文
     * @return 领域能力请求
     */
    @Override
    protected IdGenerationRequest assembleDomainRequest(final TransTransactionContext<?, ? extends TransactionModel> context) {
        IdGenerationRequest request = context.getIdGenerationRequest();
        request.setSequenceKey(IntegrationConstant.LEAF_KEY);
        request.setBatchSize(1);
        return request;
    }

    /**
     * 生成领域能力响应，子类型必须实现这个方法
     *
     * @param context 交易上下文
     * @return 领域能力响应
     */
    @Override
    protected IdGenerationResponse assembleDomainResponse(final TransTransactionContext<?, ? extends TransactionModel> context) {
        return context.getIdGenerationResponse();
    }

    /**
     * 决策当前活动的策略点，子类型必须实现这个方法。
     * 未来可以在框架层接入流程引擎，靠流程引擎来获取策略点。
     *
     * @param context 交易上下文
     * @return 作为决策结果的策略点
     */
    @Override
    protected IdGenerationStrategyEnum decideStrategy(final TransTransactionContext<?, ? extends TransactionModel> context) {
        return IdGenerationStrategyEnum.LEAF;
    }

    /**
     * get the value of strategies
     *
     * @return the value of strategies
     */
    @Override
    public List<DomainStrategy<IdGenerationRequest, IdGenerationResponse, IdGenerationStrategyEnum>> getStrategies() {
        return strategies;
    }

    /**
     * 活动执行以后，执行后置的平衡性检查和结果对领域模型的装填和结果对领域模型的装填
     *
     * @param context 交易上下文
     */
    @Override
    protected void postExecution(final TransTransactionContext<?, ? extends TransactionModel> context) {
        // 1. 执行父类检查
        super.postExecution(context);

        // 检查活动响应

        // 2. 检查 id
        IdGenerationResponse response = context.getIdGenerationResponse();
        List<Long> ids = response.getIds();

        AssertUtils.assertSingletonCollection(ids, INVALID_PAY_ORDER_NO_ERROR, "invalid pay order no:" + ids);

        Long payOrderNo = ids.get(0);
        AssertUtils.isTrue(payOrderNo > 0, INVALID_PAY_ORDER_NO_ERROR, "invalid pay order no:" + ids);

        // 装填领域模型
        TransactionModel acceptanceModel = context.getModel();

        // 3. 装填支付订单
        TransPayOrderEntity payOrder = acceptanceModel.getPayOrder();
        payOrder.setPayOrderNo(payOrderNo);
        // 4. 装填子订单
        TransSubOrderEntity subOrder = payOrder.getSubOrder();
        subOrder.setPayOrderNo(payOrderNo);
        // 5. 装填支付请求
        TransRequestEntity paymentRequest = payOrder.getPaymentRequest();
        paymentRequest.setPayOrderNo(payOrderNo);

        // 6. 完成当前钩子
        context.setIdGenerationComplete(true);
    }
}

