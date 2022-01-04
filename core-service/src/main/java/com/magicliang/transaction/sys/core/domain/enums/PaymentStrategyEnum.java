package com.magicliang.transaction.sys.core.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付策略枚举
 *
 * @author magicliang
 * <p>
 * date: 2022-01-04 13:41
 */
@Getter
@RequiredArgsConstructor
public enum PaymentStrategyEnum {

    /**
     * 支付宝支付到账户余额支付策略
     */
    AlipayBalance(1, "AlipayBalance"),

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
    public static PaymentStrategyEnum getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (PaymentStrategyEnum value : PaymentStrategyEnum.values()) {
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
    public static PaymentStrategyEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (PaymentStrategyEnum value : PaymentStrategyEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }
}
