package com.magicliang.transaction.sys.core.domain.strategy.payment;

import com.magicliang.transaction.sys.common.enums.TransPayOrderStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransRequestStatusEnum;
import com.magicliang.transaction.sys.common.exception.BaseTransException;
import com.magicliang.transaction.sys.core.config.CommonConfig;
import com.magicliang.transaction.sys.core.config.KmsConfig;
import com.magicliang.transaction.sys.core.domain.enums.PaymentStrategyEnum;
import com.magicliang.transaction.sys.core.domain.strategy.BaseStrategy;
import com.magicliang.transaction.sys.core.domain.strategy.DomainStrategy;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import com.magicliang.transaction.sys.core.model.entity.helper.PayOrderHelper;
import com.magicliang.transaction.sys.core.model.request.payment.PaymentRequest;
import com.magicliang.transaction.sys.core.model.response.payment.PaymentResponse;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付策略基类
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-04 20:08
 */
public abstract class BasePaymentStrategy extends BaseStrategy implements
        DomainStrategy<PaymentRequest, PaymentResponse, PaymentStrategyEnum> {

    /**
     * 通用配置
     */
    @Autowired
    protected CommonConfig commonConfig;

    /**
     * 支付配置
     */
    @Autowired
    protected KmsConfig kmsConfig;

    /**
     * 记录请求异常
     *
     * @param ex 请求异常
     * @param payOrder 支付订单
     * @param payRequest 支付领域请求
     */
    protected void recordException(final BaseTransException ex,
            final TransPayOrderEntity payOrder,
            final TransRequestEntity payRequest) {
        Throwable cause = ex.getCause();
        if (null != cause) {
            payRequest.setRequestException(cause.toString());
        }

        // 可重试的错误直接让订单让订单继续处于中间态
        payOrder.updateStatus(TransPayOrderStatusEnum.PENDING.getCode());
        payRequest.updateStatus(TransRequestStatusEnum.FAILED.getCode());

        // 记录异常后还要再抛出异常
        throw ex;
    }

    /**
     * 所有支付完策略执行完都要更新支付订单
     *
     * @param payOrder 支付订单
     */
    protected void updateDomainModelAfterPayment(final TransPayOrderEntity payOrder) {
        //  如果支付完立即通知，则在此处生成一个通知请求。只有支付宝策略会生成需要立即通知的请求。
        payOrderService.updateDomainModels(payOrder);
    }

    /**
     * 更新请求后的领域模型
     *
     * @param payOrder 支付订单
     * @param payRequest 支付请求
     */
    protected void updateDomainModelsBeforePayment(final TransPayOrderEntity payOrder,
            final TransRequestEntity payRequest) {
        final Date now = new Date();
        payRequest.setGmtModified(now);
        payOrder.setGmtModified(now);
        // 注意，这里暂时不使用 TransactionSynchronizationManager.registerSynchronization，免得出现顺序错乱问题
        payOrderService.updatePayOrderAndRequest(PayOrderHelper.updatePayOrder(payOrder),
                payRequest);
    }

}
