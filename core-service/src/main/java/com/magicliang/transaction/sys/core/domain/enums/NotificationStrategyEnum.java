package com.magicliang.transaction.sys.core.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 通知策略点枚举
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-04 13:39
 */
@Getter
@RequiredArgsConstructor
public enum NotificationStrategyEnum {

    /**
     * Kafka
     */
    KAFKA(1, "Kafka"),

    /**
     * Rpc
     */
    RPC(2, "RPC"),
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
    public static NotificationStrategyEnum getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (NotificationStrategyEnum value : NotificationStrategyEnum.values()) {
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
    public static NotificationStrategyEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (NotificationStrategyEnum value : NotificationStrategyEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }
}
