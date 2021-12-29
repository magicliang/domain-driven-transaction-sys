package com.magicliang.transaction.sys.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易系统错误中间类型枚举
 *
 * @author magicliang
 * <p>
 * date: 2021-12-29 12:00
 */
@Getter
@RequiredArgsConstructor
public enum TransErrorMiddleTypeEnum {

    /**
     * 本系统业务错误
     */
    SELF_BIZ("01", "本系统业务错误"),

    /**
     * 本系统系统错误
     */
    SELF_SYS("02", "本系统系统错误"),

    /**
     * 第二方业务错误
     */
    SECOND_BIZ("03", "第二方业务错误"),

    /**
     * 第二方系统错误
     */
    SECOND_SYS("04", "第二方系统错误"),

    /**
     * 第三方系统错误
     */
    THIRD_BIZ("05", "第三方业务错误"),

    /**
     * 第三方系统错误
     */
    THIRD_SYS("06", "第三方系统错误"),
    ;

    /**
     * 枚举类型码
     */
    private final String code;

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
    public static TransErrorMiddleTypeEnum getByCode(String code) {
        if (null == code) {
            return null;
        }
        for (TransErrorMiddleTypeEnum value : TransErrorMiddleTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
