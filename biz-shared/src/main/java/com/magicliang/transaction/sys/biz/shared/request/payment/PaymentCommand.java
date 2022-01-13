package com.magicliang.transaction.sys.biz.shared.request.payment;

import com.magicliang.transaction.sys.biz.shared.enums.OperationEnum;
import com.magicliang.transaction.sys.biz.shared.request.HandlerRequest;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 14:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentCommand extends HandlerRequest {

    /**
     * 外部提供的支付订单，如果外部提供了支付订单，则直接使用这个支付订单填充交易模型
     * 否则，使用支付订单号/业务标识码进行支付
     */
    private TransPayOrderEntity payOrder;

    /**
     * 支付订单号
     */
    private Long payOrderNo;

    /**
     * 标识自己的类型。
     *
     * @return 类型
     */
    @Override
    public OperationEnum identify() {
        return OperationEnum.PAYMENT;
    }
}

