package com.magicliang.transaction.sys.common.enums;

/**
 * 健康状态枚举
 *
 * @author magicliang
 * date: 2026-03-23
 */
public enum HealthStatusEnum {

    /**
     * 健康状态正常
     */
    UP("UP"),

    /**
     * 健康状态异常
     */
    DOWN("DOWN"),

    /**
     * 健康状态警告
     */
    WARN("WARN");

    private final String value;

    HealthStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
