package com.magicliang.transaction.sys.common.enums;

import com.magicliang.transaction.sys.common.constant.ErrorConstant;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;
import java.util.Set;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_CHANNEL_REQUEST_STATUS_ERROR;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易请求状态枚举
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 14:23
 */
@Getter
@RequiredArgsConstructor
public enum TransRequestStatusEnum {

    /**
     * 初始态
     */
    INIT(1, "init"),

    /**
     * 请求中
     */
    PENDING(2, "pending"),

    /**
     * 请求成功。
     * 支付请求一被受理即请求成功，不再重试。即使再有回调，也不会更新这个请求的状态。
     */
    SUCCESS(3, "success"),

    /**
     * 请求失败，可以被重试，和支付订单的失败不一样
     */
    FAILED(4, "failed"),

    /**
     * 被关闭
     */
    CLOSED(5, "closed"),

    ;

    /**
     * 终态集合
     */
    private static Set<TransRequestStatusEnum> FINAL_STATUS = EnumSet.of(SUCCESS, CLOSED);

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态描述
     */
    private final String desc;

    /**
     * 通过 code 获取枚举
     *
     * @param code 状态码
     * @return 枚举
     */
    public static TransRequestStatusEnum getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (TransRequestStatusEnum value : TransRequestStatusEnum.values()) {
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
    public static TransRequestStatusEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (TransRequestStatusEnum value : TransRequestStatusEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 是否是非成功终态
     *
     * @param code 状态码
     * @return 是否是非成功终态
     */
    public static boolean isFinalStatus(Integer code) {
        TransRequestStatusEnum status = getByCode(code);
        return FINAL_STATUS.contains(status);
    }

    /**
     * 校验是否可以从旧状态迁移到新状态
     *
     * @param oldStatus 旧状态
     * @param newStatus 新状态
     */
    public static void validateStatusBeforeUpdate(final Integer oldStatus, final Integer newStatus) {
        TransRequestStatusEnum oldStatusEnum = TransRequestStatusEnum.getByCode(oldStatus);
        TransRequestStatusEnum newStatusEnum = TransRequestStatusEnum.getByCode(newStatus);
        AssertUtils.assertNotNull(oldStatusEnum, INVALID_CHANNEL_REQUEST_STATUS_ERROR, ErrorConstant.INVALID_OLD_STATUS + oldStatus);
        AssertUtils.assertNotNull(newStatusEnum, INVALID_CHANNEL_REQUEST_STATUS_ERROR, ErrorConstant.INVALID_NEW_STATUS + newStatus);

        // 旧态非终态
        final boolean oldIsNotFinalStatus = !TransRequestStatusEnum.isFinalStatus(oldStatus);

        if (TransRequestStatusEnum.INIT == newStatusEnum) {
            // 只能由初始态跃迁到初始态
            AssertUtils.assertEquals(TransRequestStatusEnum.INIT, oldStatusEnum, INVALID_CHANNEL_REQUEST_STATUS_ERROR, ErrorConstant.INVALID_NEW_STATUS + newStatus);
        } else if (TransRequestStatusEnum.PENDING == newStatusEnum || TransRequestStatusEnum.FAILED == newStatusEnum) {
            // 只能由非终态迁到在途态和失败态
            AssertUtils.isTrue(oldIsNotFinalStatus, INVALID_CHANNEL_REQUEST_STATUS_ERROR, ErrorConstant.INVALID_NEW_STATUS + newStatus);
        } else if (TransRequestStatusEnum.SUCCESS == newStatusEnum || TransRequestStatusEnum.CLOSED == newStatusEnum) {
            // 只能由非终态迁到成功或关闭态
            AssertUtils.isTrue(oldIsNotFinalStatus, INVALID_CHANNEL_REQUEST_STATUS_ERROR, ErrorConstant.INVALID_NEW_STATUS + newStatus);
        }
    }
}
