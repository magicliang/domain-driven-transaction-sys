package com.magicliang.transaction.sys.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 底层支付通道枚举
 *
 * @author magicliang
 * <p>
 * date: 2022-01-25 22:27
 */
@Getter
@RequiredArgsConstructor
public enum TransUnderlyingPayChannelTypeEnum {

    /**
     * 支付宝
     */
    ALI_PAY(1, "ali_pay", "addr1:portNum1"),

    /**
     * 微信支付
     */
    WEIXIN_PAY(2, "weixin_pay", "addr1:portNum2"),
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
     * 调用地址
     */
    private final String addr;

    /**
     * 通过枚举类型码获取枚举
     *
     * @param code 枚举类型码
     * @return 枚举
     */
    public static TransUnderlyingPayChannelTypeEnum getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (TransUnderlyingPayChannelTypeEnum value : TransUnderlyingPayChannelTypeEnum.values()) {
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
    public static TransUnderlyingPayChannelTypeEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (TransUnderlyingPayChannelTypeEnum value : TransUnderlyingPayChannelTypeEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }
}
