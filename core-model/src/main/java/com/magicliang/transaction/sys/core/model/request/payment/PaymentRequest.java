package com.magicliang.transaction.sys.core.model.request.payment;

import com.magicliang.transaction.sys.core.model.request.acceptance.AcceptanceRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付请求
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-31 17:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentRequest extends AcceptanceRequest {

}