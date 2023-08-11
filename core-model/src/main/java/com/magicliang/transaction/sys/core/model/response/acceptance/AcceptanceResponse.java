package com.magicliang.transaction.sys.core.model.response.acceptance;

import com.magicliang.transaction.sys.core.model.response.IResponse;
import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 受理响应
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-31 17:20
 */
@Data
public class AcceptanceResponse implements IResponse {

    /**
     * 受理的支付订单号
     */
    private Long acceptedPayOrderNo;
}
