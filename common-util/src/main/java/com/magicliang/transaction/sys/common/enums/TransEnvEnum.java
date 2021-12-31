package com.magicliang.transaction.sys.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易环境参数
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 11:48
 */
@Getter
@RequiredArgsConstructor
public enum TransEnvEnum {

    /**
     * dev
     */
    DEV(1, "dev"),

    /**
     * test
     */
    TEST(2, "test"),

    /**
     * staging
     */
    STAGING(3, "staging"),

    /**
     * prod
     */
    PROD(4, "prod"),
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
    public static TransEnvEnum getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (TransEnvEnum value : TransEnvEnum.values()) {
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
    public static TransEnvEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (TransEnvEnum value : TransEnvEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }
}
