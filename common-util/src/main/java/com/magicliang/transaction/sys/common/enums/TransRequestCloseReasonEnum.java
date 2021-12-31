package com.magicliang.transaction.sys.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 关闭原因枚举
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 15:45
 */
@Getter
@RequiredArgsConstructor
public enum TransRequestCloseReasonEnum {

    /**
     * 不可重试的请求
     */
    NOT_RETRYABLE_REQUEST(1, "not_retryable_request"),

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
    public static TransRequestCloseReasonEnum getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (TransRequestCloseReasonEnum value : TransRequestCloseReasonEnum.values()) {
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
    public static TransRequestCloseReasonEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (TransRequestCloseReasonEnum value : TransRequestCloseReasonEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }
}
