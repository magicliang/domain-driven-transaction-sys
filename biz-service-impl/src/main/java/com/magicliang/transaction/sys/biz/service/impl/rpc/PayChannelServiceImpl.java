package com.magicliang.transaction.sys.biz.service.impl.rpc;

import com.magicliang.transaction.sys.biz.service.impl.facade.IAcceptanceFacade;
import com.magicliang.transaction.sys.biz.service.impl.facade.IPaymentFacade;
import com.magicliang.transaction.sys.biz.shared.request.acceptance.AlipayAcceptanceCommand;
import com.magicliang.transaction.sys.biz.shared.request.payment.convertor.PaymentCommandConvertor;
import com.magicliang.transaction.sys.common.service.facade.PayChannelService;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付通道支付服务
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 19:25
 */
@Slf4j
@Service("payChannelService")
public class PayChannelServiceImpl implements PayChannelService {

    /**
     * 受理门面
     */
    @Autowired
    private IAcceptanceFacade acceptanceFacade;

    /**
     * 支付门面
     */
    @Autowired
    private IPaymentFacade paymentFacade;

    /**
     * 支付到支付宝余额
     *
     * @param payToAlipayRequest 原始 rpc 请求
     * @return rpc 结果
     */
    public Object payToAlipay(final Object payToAlipayRequest) {
        AlipayAcceptanceCommand command = convertToCommand(payToAlipayRequest);

        TransactionModel transactionModel = acceptanceFacade.acceptPayOrder(command);

        Object response = new Object();
//        response.setBizIdentifyNo(payToAlipayRequest.getBizIdentifyNo());
//        response.setBizUniqueNo(payToAlipayRequest.getBizUniqueNo());

        if (transactionModel.isSuccess()) {
//            response.setSuccess(true);
//            response.setIdempotent(transactionModel.isIdempotent());
            final TransPayOrderEntity payOrder = transactionModel.getPayOrder();
            Long payOrderNo = payOrder.getPayOrderNo();
//            response.setPayOrderNo(payOrderNo);

            // 受理即异步支付
            paymentFacade.asyncPay(PaymentCommandConvertor.fromDomainEntity(payOrder));
        } else {
//            response.setErrorCode(transactionModel.getErrorCode());
//            response.setErrorMsg(transactionModel.getErrorMsg());
        }
        return response;
    }

    /**
     * 转化 rpc 请求为门面命令
     *
     * @param payToAlipayRequest 原始 rpc 请求
     * @return 门面命令
     */
    private AlipayAcceptanceCommand convertToCommand(final Object payToAlipayRequest) {
        AlipayAcceptanceCommand alipayAcceptanceCommand = new AlipayAcceptanceCommand();
        return alipayAcceptanceCommand;
    }
}
