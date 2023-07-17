package com.magicliang.transaction.sys.common.util;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 增强版集合工具
 *
 * @author magicliang
 * <p>
 * date: 2023-07-17 13:09
 */
public class CollectionUtilPlus {

    /**
     * 私有构造器
     */
    private CollectionUtilPlus() {
        throw new UnsupportedOperationException();
    }

    public static boolean isEqualCollection(final Collection<?> a, final Collection<?> b) {
        return ObjectUtilPlus.allNull(a, b)
                // CollectionUtils.isEqualCollection 不能接受空值，a 一个为空一个不为空，自然不相等
                && (!ObjectUtilPlus.allNotNull(a, b) && CollectionUtils.isEqualCollection(a, b));
    }

    public static boolean isNotEqualCollection(final Collection<?> a, final Collection<?> b) {
        return !isEqualCollection(a, b);
    }

}
