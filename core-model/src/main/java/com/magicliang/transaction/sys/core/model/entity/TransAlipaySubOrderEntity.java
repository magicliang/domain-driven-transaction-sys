package com.magicliang.transaction.sys.core.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付宝子订单实体
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-31 16:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TransAlipaySubOrderEntity extends TransSubOrderEntity {

    /**
     * 目标支付宝账户
     */
    private String toAliPayAccount;
}
