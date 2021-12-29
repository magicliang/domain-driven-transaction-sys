package com.magicliang.transaction.sys.common.service.integration.dto;

import lombok.Builder;
import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付宝请求参数
 *
 * @author magicliang
 * <p>
 * date: 2021-12-29 14:18
 */
@Builder
@Data
public class AlipayReqParam {

    /**
     * 出资账户
     * 为了防止IEEE 754 标准相关的Number 浮点数溢出，先使用 String 替代
     * 在内层转化为 BO 的时候要仔细注意转化的过程里的 NumberFormatException
     */
    private String fromAccount;

    /**
     * 进款账户
     */
    private String toAccount;

    /**
     * 支付备注
     */
    private String comment;

    /**
     * 付款金额，单位：分，非空且为正整数，范围1 - 4,000,000,000
     * 还有另一种设计思路：moneyDigit + moneyUnit
     */
    private Long moneyCent;

}
