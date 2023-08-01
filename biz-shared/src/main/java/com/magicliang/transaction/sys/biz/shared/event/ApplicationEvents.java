package com.magicliang.transaction.sys.biz.shared.event;

import com.magicliang.transaction.sys.core.model.event.TransPayOrderAcceptedEvent;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 应用层事件
 * 声明这个接口在应用层，则
 *
 * @author magicliang
 *         <p>
 *         date: 2022-12-29 15:06
 */
public interface ApplicationEvents {

    /**
     * 支付订单被受理事件
     */
    void transPayOrderAccepted(TransPayOrderAcceptedEvent event);
}
