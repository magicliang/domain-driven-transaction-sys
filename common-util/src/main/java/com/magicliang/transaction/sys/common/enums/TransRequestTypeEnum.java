package com.magicliang.transaction.sys.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易请求类型
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 14:21
 */
@Getter
@RequiredArgsConstructor
public enum TransRequestTypeEnum {

    /**
     * 支付请求
     */
    PAYMENT(1, "payment"),

    /**
     * 支付订单终态回调基础通知
     */
    BASIC_NOTIFICATION(2, "basic_notification"),

    /**
     * 支付订单回调退票通知
     */
    BOUNCED_NOTIFICATION(3, "bounced_notification"),
    ;

    /**
     * 通知类型枚举列表
     */
    private static final Set<TransRequestTypeEnum> NOTIFICATION_TYPE = EnumSet.of(BASIC_NOTIFICATION, BOUNCED_NOTIFICATION);

    /**
     * 通知类型枚举值列表
     */
    public static final List<Integer> NOTIFICATION_TYPE_VALUE = NOTIFICATION_TYPE.stream().map(TransRequestTypeEnum::getCode).collect(Collectors.toList());

    /**
     * 枚举类型码
     * 注意，这个 code 的大小顺序决定了发送请求的优先级
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
    public static TransRequestTypeEnum getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (TransRequestTypeEnum value : TransRequestTypeEnum.values()) {
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
    public static TransRequestTypeEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (TransRequestTypeEnum value : TransRequestTypeEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }
}
