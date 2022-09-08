package com.magicliang.transaction.sys.common.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付宝支付结果状态枚举
 *
 * @author magicliang
 * <p>
 * date: 2021-12-29 12:24
 */
@Getter
@RequiredArgsConstructor
public enum AliPayResultStatusEnum {

    /**
     * 成功
     */
    SUCCESS(0),

    /**
     * 不成功
     */
    FAILURE(-1),
    ;

    /**
     * 枚举映射
     */
    private static final ImmutableMap<Integer, AliPayResultStatusEnum> ENUM_MAP;

    /*
     * 初始化枚举映射
     */
    static {
        ImmutableMap.Builder<Integer, AliPayResultStatusEnum> builder = ImmutableMap.builder();
        for (AliPayResultStatusEnum item : AliPayResultStatusEnum.values()) {
            builder.put(item.getCode(), item);
        }
        ENUM_MAP = builder.build();
    }

    /**
     * 结果状态
     */
    private final Integer code;

    /**
     * 通过状态获取枚举
     *
     * @param code 状态
     * @return 状态枚举
     */
    public static AliPayResultStatusEnum getByCode(Integer code) {
        return ENUM_MAP.get(code);
    }
}
