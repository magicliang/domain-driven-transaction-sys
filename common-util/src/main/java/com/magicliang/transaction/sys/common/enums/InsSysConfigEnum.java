package com.magicliang.transaction.sys.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 系统配置枚举
 *
 * @author magicliang
 * <p>
 * date: 2021-12-29 12:03
 */
@Getter
@RequiredArgsConstructor
public enum InsSysConfigEnum {

    /**
     * 本系统，交易核心
     */
    TRANS_CORE("00001", "trans_core"),

    /**
     * 支付宝
     */
    ALIPAY("00002", "alipay"),

    /**
     * 微信支付
     */
    WX_PAY("00003", "weixin_pay"),

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
    public static InsSysConfigEnum getByCode(String code) {
        if (null == code) {
            return null;
        }
        for (InsSysConfigEnum value : InsSysConfigEnum.values()) {
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
    public static InsSysConfigEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (InsSysConfigEnum value : InsSysConfigEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }
}
