package com.magicliang.transaction.sys.common.util;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 对象工具类
 *
 * @author magicliang
 * <p>
 * date: 2023-07-17 11:32
 */
public class ObjectUtilPlus {

    /**
     * 私有构造器
     */
    private ObjectUtilPlus() {
        throw new UnsupportedOperationException();
    }

    public static boolean allNull(Object... objs) {
        for (Object obj : objs) {
            if (obj != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean notAllNull(Object... objs) {
        return !allNull(objs);
    }

    public static boolean allNotNull(Object... objs) {
        for (Object obj : objs) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }

}
