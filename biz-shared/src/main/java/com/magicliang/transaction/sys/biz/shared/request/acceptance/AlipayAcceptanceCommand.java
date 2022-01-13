package com.magicliang.transaction.sys.biz.shared.request.acceptance;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付宝宝余额支付订单受理命令
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 14:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlipayAcceptanceCommand extends AcceptanceCommand {

    /**
     * 目标支付宝账户
     */
    private String toAliPayAccount;

}
