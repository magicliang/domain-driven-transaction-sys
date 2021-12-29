package com.magicliang.transaction.sys.common.enums;

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
     * 结果状态
     */
    private final Integer code;

    /**
     * 通过状态获取枚举
     *
     * @param status 状态
     * @return 状态枚举
     */
    public static AliPayResultStatusEnum getByCode(Integer status) {
        if (status == null) {
            return null;
        }
        for (AliPayResultStatusEnum type : values()) {
            if (type.getCode().equals(status)) {
                return type;
            }
        }
        return null;
    }
}
