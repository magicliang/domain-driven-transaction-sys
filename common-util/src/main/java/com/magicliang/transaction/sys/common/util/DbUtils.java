package com.magicliang.transaction.sys.common.util;

import com.magicliang.transaction.sys.common.enums.TransErrorEnum;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.*;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: db 工具类
 *
 * @author magicliang
 * <p>
 * date: 2021-12-29 12:13
 */
public class DbUtils {
    /**
     * 私有构造器
     */
    private DbUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 确认查询一条
     *
     * @param collection 查询结果
     * @param params     参数
     */
    public static void checkDbSelectExpectedOne(Collection collection, Object... params) {
        checkDbSelectExpected(1, collection, params);
    }

    /**
     * 确认查询数量
     *
     * @param expected   期望值
     * @param collection 查询结果
     * @param params     参数
     */
    public static void checkDbSelectExpected(int expected, Collection collection, Object... params) {
        int actual = CollectionUtils.isEmpty(collection) ? 0 : collection.size();
        check(expected, actual, DB_SELECT_ERROR, params);
    }

    /**
     * 确认插入一条
     *
     * @param actual 实际值
     * @param params 参数
     */
    public static void checkDbInsertExpectedOne(int actual, Object... params) {
        checkDbInsertExpected(1, actual, params);
    }

    /**
     * 确认更新一条
     *
     * @param actual 实际值
     * @param params 参数
     */
    public static void checkDbUpdateExpectedOne(int actual, Object... params) {
        checkDbUpdateExpected(1, actual, params);
    }

    /**
     * 确认插入数量
     *
     * @param expected 期望值
     * @param actual   实际值
     * @param params   参数
     */
    public static void checkDbInsertExpected(int expected, int actual, Object... params) {
        check(expected, actual, DB_INSERT_ERROR, params);
    }

    /**
     * 确认更新数量
     *
     * @param expected 期望值
     * @param actual   实际值
     * @param params   参数
     */
    public static void checkDbUpdateExpected(int expected, int actual, Object... params) {
        check(expected, actual, DB_UPDATE_ERROR, params);
    }

    /**
     * 确认操作数量
     *
     * @param expected  期望值
     * @param actual    实际值
     * @param errorEnum 错误枚举
     * @param params    参数
     */
    public static void check(int expected, int actual, TransErrorEnum errorEnum, Object... params) {
        StringBuilder builder = new StringBuilder();
        builder.append("期望=").append(expected).append(",实际=").append(actual).append(",params:");
        for (Object param : params) {
            builder.append(param).append(",");
        }
        AssertUtils.assertEquals(expected, actual, errorEnum, builder.toString());
    }
}
