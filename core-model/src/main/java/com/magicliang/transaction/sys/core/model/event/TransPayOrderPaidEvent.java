package com.magicliang.transaction.sys.core.model.event;

import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易订单已支付事件
 *
 * @author magicliang
 * <p>
 * date: 2022-12-30 16:14
 */
public class TransPayOrderPaidEvent extends TransPayOrderAcceptedEvent {

    private TransPayOrderPaidEvent(Object source, final TransPayOrderEntity payOrderEntitySnapShot) {
        super(source, payOrderEntitySnapShot);
    }
}
