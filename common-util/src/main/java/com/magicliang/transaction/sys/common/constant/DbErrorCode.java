package com.magicliang.transaction.sys.common.constant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: DB 错误码
 *
 * @author magicliang
 * <p>
 * date: 2021-12-30 17:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DbErrorCode extends ErrorCode {

    /**
     * 数据库操作异常
     */
    public final static DbErrorCode DB_UNKNOWN = new DbErrorCode(2201, "E2201", "数据库操作异常");

    /**
     * 更新计划操作，期望1条实际0条
     */
    public final static DbErrorCode DB_EXPECTED_ONE_ACTUAL_ZERO = new DbErrorCode(2210, "E2210", "更新计划操作，期望1条实际0条");

    /**
     * 更新计划操作，期望1条实际多条
     */
    public final static DbErrorCode DB_EXPECTED_ONE_ACTUAL_MORE = new DbErrorCode(2211, "E2211", "更新计划操作，期望1条实际多条");

    /**
     * 更新计划操作错误
     */
    public final static DbErrorCode DB_EXPECTED_X_ACTUAL_Y = new DbErrorCode(2212, "E2212", "更新计划操作错误");

    protected DbErrorCode(int code, String type, String message) {
        super(code, type, message);
    }
}
