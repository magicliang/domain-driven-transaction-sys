package com.magicliang.transaction.sys.core.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 受理策略点枚举
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-04 13:30
 */
@Getter
@RequiredArgsConstructor
public enum AcceptanceStrategyEnum {

    /**
     * 同步受理
     */
    SYNC(1, "Sync"),
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
    public static AcceptanceStrategyEnum getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (AcceptanceStrategyEnum value : AcceptanceStrategyEnum.values()) {
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
    public static AcceptanceStrategyEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (AcceptanceStrategyEnum value : AcceptanceStrategyEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }
}
