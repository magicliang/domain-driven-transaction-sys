package com.magicliang.transaction.sys.common.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易的基本枚举
 *
 * @author magicliang
 * <p>
 * date: 2021-12-28 20:42
 */
@Getter
@RequiredArgsConstructor
public enum TransBasicEnum {

    /**
     * 苹果
     */
    APPLE(1, "apple"),
    ;

    /**
     * 枚举映射
     */
    private static final ImmutableMap<Integer, TransBasicEnum> ENUM_MAP;

    /**
     * 初始化枚举映射
     */
    static {
        ImmutableMap.Builder<Integer, TransBasicEnum> builder = ImmutableMap.builder();
        for (TransBasicEnum item : TransBasicEnum.values()) {
            builder.put(item.getCode(), item);
        }
        ENUM_MAP = builder.build();
    }

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
    public static TransBasicEnum Key2Enum(Integer code) {
        return ENUM_MAP.get(code);
    }

    /**
     * 通过枚举类型描述获取枚举
     *
     * @param desc 枚举类型描述
     * @return 枚举
     */
    public static TransBasicEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (TransBasicEnum value : TransBasicEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }
}
