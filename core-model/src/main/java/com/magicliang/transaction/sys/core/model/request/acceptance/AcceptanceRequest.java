package com.magicliang.transaction.sys.core.model.request.acceptance;

import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.request.IRequest;
import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 受理请求
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-31 17:25
 */
@Data
public class AcceptanceRequest implements IRequest {

    /**
     * 支付订单
     */
    private TransPayOrderEntity transPayOrder;
}
