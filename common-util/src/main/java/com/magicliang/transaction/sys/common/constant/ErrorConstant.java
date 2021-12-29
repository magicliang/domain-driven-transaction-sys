package com.magicliang.transaction.sys.common.constant;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 错误常量
 *
 * @author magicliang
 * <p>
 * date: 2021-12-28 20:39
 */
public class ErrorConstant {

    /**
     * 私有构造器
     */
    private ErrorConstant() {
        throw new UnsupportedOperationException();
    }

    /**
     * 不正确的旧状态
     */
    public static final String INVALID_OLD_STATUS = "invalid oldStatus：";

    /**
     * 不正确的新状态
     */
    public static final String INVALID_NEW_STATUS = "invalid newStatus：";
}
