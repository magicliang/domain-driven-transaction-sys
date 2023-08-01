package com.magicliang.transaction.sys.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 目标账户类型枚举
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-25 22:31
 */
@Getter
@RequiredArgsConstructor
public enum TransTargetAccountTypeEnum {

    /**
     * 银行卡
     */
    BANK_CARD(1, "bank_card"),

    /**
     * 账户余额
     */
    ACCOUNT_BALANCE(2, "account_balance"),

    ;

    /**
     * 枚举类型码
     */
    private final Integer code;

    /**
     * 枚举类型描述
     */
    private final String desc;

    /**
     * 通过枚举类型码获取枚举
     *
     * @param code 枚举类型码
     * @return 枚举
     */
    public static TransTargetAccountTypeEnum getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (TransTargetAccountTypeEnum value : TransTargetAccountTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 通过枚举类型描述获取枚举
     *
     * @param desc 枚举类型描述
     * @return 枚举
     */
    public static TransTargetAccountTypeEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (TransTargetAccountTypeEnum value : TransTargetAccountTypeEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }
}
