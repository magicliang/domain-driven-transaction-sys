package com.magicliang.transaction.sys.core.model.response.payment;

import com.magicliang.transaction.sys.core.model.response.IResponse;
import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付响应
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 17:22
 */
@Data
public class PaymentResponse implements IResponse {

    /**
     * 支付通道的支付流水号，单一通道必须全局唯一
     */
    private String channelPaymentTraceNo;

    /**
     * 支付错误码
     */
    private String channelErrorCode;
}
