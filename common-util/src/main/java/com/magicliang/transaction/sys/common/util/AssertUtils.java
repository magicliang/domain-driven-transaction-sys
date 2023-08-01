package com.magicliang.transaction.sys.common.util;

import com.magicliang.transaction.sys.common.enums.TransErrorEnum;
import com.magicliang.transaction.sys.common.exception.BaseTransException;
import java.util.Collection;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 断言工具类
 *
 * @author magicliang
 *         <p>
 *         date: 2021-12-29 12:08
 */
public class AssertUtils {

    /**
     * 私有构造器
     */
    private AssertUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 断言目标对象必为空，否则抛出异常
     *
     * @param object 目标对象
     * @param errorEnum 错误枚举
     * @param errorMsg 错误信息
     */
    public static void assertNull(Object object, TransErrorEnum errorEnum, String errorMsg) {
        isTrue(null == object, errorEnum, errorMsg);
    }

    /**
     * 断言目标对象必不为空，否则抛出异常
     *
     * @param object 目标对象
     * @param errorEnum 错误枚举
     * @param errorMsg 错误信息
     */
    public static void assertNotNull(Object object, TransErrorEnum errorEnum, String errorMsg) {
        isTrue(null != object, errorEnum, errorMsg);
    }

    /**
     * 断言目标字符串必不为空，否则抛出异常
     *
     * @param str 目标字符串
     * @param errorEnum 错误枚举
     * @param errorMsg 错误信息
     */
    public static void assertNotBlank(String str, TransErrorEnum errorEnum, String errorMsg) {
        isTrue(StringUtils.isNotBlank(str), errorEnum, errorMsg);
    }

    /**
     * 断言目标集合必不为空，否则抛出异常
     *
     * @param collection 目标集合
     * @param errorEnum 错误枚举
     * @param errorMsg 错误信息
     */
    public static void assertNotEmpty(Collection collection, TransErrorEnum errorEnum, String errorMsg) {
        isTrue(CollectionUtils.isNotEmpty(collection), errorEnum, errorMsg);
    }

    /**
     * 断言目标集合必为单例集合，否则抛出异常
     *
     * @param collection 目标集合
     * @param errorEnum 错误枚举
     * @param errorMsg 错误信息
     */
    public static void assertSingletonCollection(Collection collection, TransErrorEnum errorEnum, String errorMsg) {
        assertNotEmpty(collection, errorEnum, errorMsg);
        assertEquals(1, collection.size(), errorEnum, errorMsg);
    }

    /**
     * 断言目标对象必定相等，否则抛出异常
     *
     * @param obj1 obj1
     * @param obj2 obj2
     * @param errorEnum 错误枚举
     * @param errorMsg 错误信息
     */
    public static void assertEquals(Object obj1, Object obj2, TransErrorEnum errorEnum, String errorMsg) {
        isTrue(Objects.equals(obj1, obj2), errorEnum, errorMsg);
    }

    /**
     * 断言必为真，否则抛出异常
     *
     * @param flag 标志位
     * @param errorEnum 错误枚举
     * @param errorMsg 错误信息
     */
    public static void isTrue(boolean flag, TransErrorEnum errorEnum, String errorMsg) {
        if (!flag) {
            throw new BaseTransException(errorEnum, errorMsg);
        }
    }
}
