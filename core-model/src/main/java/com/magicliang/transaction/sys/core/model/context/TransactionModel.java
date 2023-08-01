package com.magicliang.transaction.sys.core.model.context;

import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 基础交易模型
 * 一个领域的主交易模型到底应不应该因为平台产品的变动而变动，有待探讨
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-31 17:31
 */
@Data
public class TransactionModel {

    /**
     * 支付订单领域模型
     */
    private TransPayOrderEntity payOrder;

    /**
     * 交易是否成功，在受理场景下，支付失败的订单被幂等也会返回成功
     */
    private boolean success;

    /**
     * 是否被幂等
     */
    private boolean idempotent;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误消息
     */
    private String errorMsg;
}
