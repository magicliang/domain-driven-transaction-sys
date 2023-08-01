package com.magicliang.transaction.sys.common.enums;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_PAY_ORDER_STATUS_ERROR;

import com.magicliang.transaction.sys.common.constant.ErrorConstant;
import com.magicliang.transaction.sys.common.util.AssertUtils;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 支付订单状态枚举
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-31 16:16
 */
@Getter
@RequiredArgsConstructor
public enum TransPayOrderStatusEnum {

    /**
     * 初始化
     */
    INIT(1, "init"),

    /**
     * 支付中
     */
    PENDING(2, "pending"),

    /**
     * 支付成功，终态
     * 凡是有中间态和终态的设计有个好处：可以支持受理即成功的纯同步模式，也可以支持回调才成功的异步模式，
     */
    SUCCESS(3, "success"),

    /**
     * 支付失败，终态。意味着支付订单已然失败，无法再重试，和通道请求的失败不一样。
     * 注意多种失败终态设计
     */
    FAILED(4, "failed"),

    /**
     * 被关闭，终态。意味着支付订单被干预而关闭，和支付失败不一样。
     * 注意多种失败终态设计
     */
    CLOSED(5, "closed"),

    /**
     * 支付退票，终态
     * 注意多种失败终态设计
     */
    BOUNCED(6, "bounced"),

    ;

    /**
     * 未支付状态枚举列表
     */
    private static final Set<TransPayOrderStatusEnum> UNPAID_STATUS = EnumSet.of(INIT, PENDING);

    /**
     * 未支付状态枚举值列表
     */
    public static final List<Integer> UNPAID_STATUS_VALUE = UNPAID_STATUS.stream().map(TransPayOrderStatusEnum::getCode)
            .collect(Collectors.toList());

    /**
     * 坏的终态
     */
    private static final Set<TransPayOrderStatusEnum> BAD_FINAL_STATUSES = EnumSet.of(FAILED, CLOSED, BOUNCED);

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
    public static TransPayOrderStatusEnum getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (TransPayOrderStatusEnum value : TransPayOrderStatusEnum.values()) {
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
    public static TransPayOrderStatusEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
            return null;
        }
        for (TransPayOrderStatusEnum value : TransPayOrderStatusEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 是否是终态
     *
     * @param code 状态码
     * @return 是否是终态
     */
    public static boolean isFinalStatus(Integer code) {
        return isSuccessFinalStatus(code) || isBadFinalStatus(code);
    }

    /**
     * 是否是成功终态
     *
     * @param code 状态码
     * @return 是否是成功终态
     */
    public static boolean isSuccessFinalStatus(Integer code) {
        TransPayOrderStatusEnum status = getByCode(code);
        return TransPayOrderStatusEnum.SUCCESS == status;
    }

    /**
     * 是否是非成功终态
     *
     * @param code 状态码
     * @return 是否是非成功终态
     */
    public static boolean isBadFinalStatus(Integer code) {
        TransPayOrderStatusEnum status = getByCode(code);
        return BAD_FINAL_STATUSES.contains(status);
    }

    /**
     * 是否被退票
     *
     * @param code 状态码
     * @return 是否被退票
     */
    public static boolean isBounced(Integer code) {
        TransPayOrderStatusEnum status = getByCode(code);
        return TransPayOrderStatusEnum.BOUNCED == status;
    }

    /**
     * 校验是否可以从旧状态迁移到新状态
     *
     * @param oldStatus 旧状态
     * @param newStatus 新状态
     */
    public static void validateStatusBeforeUpdate(final Integer oldStatus, final Integer newStatus) {
        TransPayOrderStatusEnum oldStatusEnum = TransPayOrderStatusEnum.getByCode(oldStatus);
        TransPayOrderStatusEnum newStatusEnum = TransPayOrderStatusEnum.getByCode(newStatus);
        AssertUtils.assertNotNull(oldStatusEnum, INVALID_PAY_ORDER_STATUS_ERROR,
                ErrorConstant.INVALID_OLD_STATUS + oldStatus);
        AssertUtils.assertNotNull(newStatusEnum, INVALID_PAY_ORDER_STATUS_ERROR,
                ErrorConstant.INVALID_NEW_STATUS + newStatus);

        // 旧态非终态
        final boolean oldIsNotFinalStatus = !TransPayOrderStatusEnum.isFinalStatus(oldStatus);

        if (TransPayOrderStatusEnum.INIT == newStatusEnum) {
            // 只能由初始态跃迁到初始态
            AssertUtils.assertEquals(TransPayOrderStatusEnum.INIT, oldStatusEnum, INVALID_PAY_ORDER_STATUS_ERROR,
                    ErrorConstant.INVALID_NEW_STATUS + newStatus);
        } else if (TransPayOrderStatusEnum.PENDING == newStatusEnum) {
            // 只能由非终态迁到在途态
            AssertUtils.isTrue(oldIsNotFinalStatus, INVALID_PAY_ORDER_STATUS_ERROR,
                    ErrorConstant.INVALID_NEW_STATUS + newStatus);
        } else if (TransPayOrderStatusEnum.BOUNCED == newStatusEnum) {
            // 只能由成功态跃迁到退票态
            AssertUtils.assertEquals(TransPayOrderStatusEnum.SUCCESS, oldStatusEnum, INVALID_PAY_ORDER_STATUS_ERROR,
                    ErrorConstant.INVALID_NEW_STATUS + newStatus);
        } else if (TransPayOrderStatusEnum.isFinalStatus(newStatus)) {
            // 只能由非终态跃迁到其他终态
            AssertUtils.isTrue(oldIsNotFinalStatus, INVALID_PAY_ORDER_STATUS_ERROR,
                    ErrorConstant.INVALID_NEW_STATUS + newStatus);
        }
    }
}
