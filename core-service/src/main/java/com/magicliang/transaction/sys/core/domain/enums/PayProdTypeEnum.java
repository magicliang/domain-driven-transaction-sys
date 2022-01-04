package com.magicliang.transaction.sys.core.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 银行付款时效枚举
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 13:40
 */
@Getter
@RequiredArgsConstructor
public enum PayProdTypeEnum {

    /**
     * 实时
     */
    REAL_TIME("REAL_TIME", "实时"),

    /**
     * 两小时到账
     */
    T_2_H("T_2_H", "两小时到账"),

    /**
     * 当天到账
     */
    T_0_D("T_0_D", "当天到账"),

    ;


    /**
     * 枚举码
     */
    private final String code;

    /**
     * 枚举描述
     */
    private final String desc;
}
