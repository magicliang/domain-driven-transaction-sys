package com.magicliang.transaction.sys.biz.shared.request.payment.convertor;

import com.magicliang.transaction.sys.biz.shared.request.payment.PaymentCommand;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付命令转换器
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-05 14:52
 */
public class PaymentCommandConvertor {

    /**
     * 私有构造器
     */
    private PaymentCommandConvertor() {
        throw new UnsupportedOperationException();
    }

    /**
     * 从领域模型转换为支付命令
     *
     * @param payOrder 领域模型
     * @return 支付命令
     */
    public static PaymentCommand fromDomainEntity(final TransPayOrderEntity payOrder) {
        if (null == payOrder) {
            return null;
        }
        PaymentCommand paymentCommand = new PaymentCommand();
        return paymentCommand;
    }
}
