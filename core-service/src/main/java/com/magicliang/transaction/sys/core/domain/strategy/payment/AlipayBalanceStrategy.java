package com.magicliang.transaction.sys.core.domain.strategy.payment;

import com.magicliang.transaction.sys.common.dal.po.TransAlipaySubOrderPo;
import com.magicliang.transaction.sys.common.enums.AliPayResultStatusEnum;
import com.magicliang.transaction.sys.common.enums.TransPayOrderStatusEnum;
import com.magicliang.transaction.sys.common.exception.BaseTransException;
import com.magicliang.transaction.sys.common.service.integration.delegate.alipay.IAlipayDelegate;
import com.magicliang.transaction.sys.common.service.integration.param.AlipayReqParam;
import com.magicliang.transaction.sys.common.service.integration.param.AlipayResDto;
import com.magicliang.transaction.sys.common.util.JsonUtils;
import com.magicliang.transaction.sys.core.domain.enums.PaymentStrategyEnum;
import com.magicliang.transaction.sys.core.model.entity.TransAlipaySubOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.entity.TransRequestEntity;
import com.magicliang.transaction.sys.core.model.entity.TransSubOrderEntity;
import com.magicliang.transaction.sys.core.model.request.payment.PaymentRequest;
import com.magicliang.transaction.sys.core.model.response.payment.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付到支付宝余额支付策略
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 20:22
 */
public class AlipayBalanceStrategy extends AbstractAlipayStrategy {

    /**
     * 付款平台委托接口
     */
    @Autowired
    private IAlipayDelegate alipayDelegate;

    /**
     * 标识自己的类型
     *
     * @return 类型
     */
    @Override
    public PaymentStrategyEnum identify() {
        return PaymentStrategyEnum.AlipayBalance;
    }

    /**
     * 执行领域请求，生成领域响应
     *
     * @param paymentRequest  领域请求
     * @param paymentResponse 领域响应
     */
    @Override
    public void execute(final PaymentRequest paymentRequest, final PaymentResponse paymentResponse) {
        TransPayOrderEntity payOrder = paymentRequest.getInsPayOrder();
        TransAlipaySubOrderEntity subOrder = (TransAlipaySubOrderEntity) payOrder.getSubOrder();
        final AlipayReqParam param = buildTransferToQdbBalanceReqParam(payOrder, subOrder);

        // 记录请求参数
        final TransRequestEntity payRequest = payOrder.getPaymentRequest();
        payRequest.setRequestParams(JsonUtils.toJson(param));

        // 事务1：发送前事务
        updateDomainModelsBeforePayment(payOrder, payRequest);

        try {
            // 使用私钥为请求签名并支付
            final AlipayResDto res = alipayDelegate.standardPay(param , kmsConfig.getPrivateKey());
            // 记录响应
            recordRes(paymentResponse, payOrder, payRequest, res);
        } catch (BaseTransException ex) {
            // 记录异常
            recordException(ex, payOrder, payRequest);
        } finally {
            // 事务2：发送后事务
            updateDomainModelAfterPayment(payOrder);
        }
    }

    /**
     * 从支付订单和子订单开始构建支付宝余额请求参数
     *
     * @param payOrder 支付订单
     * @param subOrder 支付子订单
     * @return 支付宝余额请求参数
     */
    private AlipayReqParam buildTransferToQdbBalanceReqParam(final TransPayOrderEntity payOrder, final TransAlipaySubOrderEntity subOrder) {
        AlipayReqParam param = new AlipayReqParam();
        return param;
    }

    /**
     * 记录响应
     *
     * @param paymentResponse 支付领域响应
     * @param payOrder        支付订单
     * @param payRequest      支付请求
     * @param res             原始响应
     */
    private void recordRes(final PaymentResponse paymentResponse, final TransPayOrderEntity payOrder, final TransRequestEntity payRequest, final AlipayResDto res) {
        payRequest.setRequestResponse(JsonUtils.toJson(res));
        Date now = new Date();
        if (AliPayResultStatusEnum.SUCCESS == AliPayResultStatusEnum.getByCode(res.getStatus())) {
            // 钱包系统资金转账流水号
            Long fundSerialNo = 55596L;
            // 更新领域响应
            paymentResponse.setChannelPaymentTraceNo("" + fundSerialNo);

            // 更新领域模型
            // 支付宝余额转账到余额受理成功即支付成功，支付订单进入终态
            payOrder.updateStatus(TransPayOrderStatusEnum.SUCCESS.getCode());
            // 支付宝余额付款到余额成功即终态，更新支付成功时间
            payOrder.setGmtPaymentSuccessTime(now);

            // 更新，支付渠道流水号
            payOrder.setChannelPaymentTraceNo(paymentResponse.getChannelPaymentTraceNo());
            // 付款平台受理成功，支付请求执行成功，即使再发生回调也不影响走入终态的支付请求。
            payRequest.updateStatus(TransPayOrderStatusEnum.SUCCESS.getCode());
        } else {
            // 更新领域响应
            final String errorCode = res.getErrorCode();
            paymentResponse.setChannelErrorCode(errorCode);

            // 一般情况下订单支付失败，维持在中间态
            payOrder.updateStatus(TransPayOrderStatusEnum.PENDING.getCode());
            payOrder.setChannelErrorCode(errorCode);
            // 支付请求也进入失败状态
            payRequest.updateStatus(TransPayOrderStatusEnum.FAILED.getCode());
        }
    }

}
