package com.magicliang.transaction.sys.biz.shared.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 操作类型枚举
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-05 14:08
 */
@Getter
@RequiredArgsConstructor
public enum OperationEnum {

    /**
     * 受理
     */
    ACCEPTANCE(1, "acceptance"),

    /**
     * 查询未完成支付订单
     */
    QUERY_UNPAID_ORDERS(2, "query_unpaid_orders"),

    /**
     * 支付未完成的支付订单
     */
    PAYMENT(3, "payment"),

    /**
     * 回调更新支付订单
     */
    CALLBACK(4, "callback"),

    /**
     * 查询未完成通知请求
     */
    QUERY_UNSENT_NOTIFICATION(5, "query_unsent_notification"),

    /**
     * 通知已完成的支付订单
     */
    NOTIFY(6, "notify"),
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
    public static OperationEnum getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (OperationEnum value : OperationEnum.values()) {
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
    public static OperationEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (OperationEnum value : OperationEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }
}
