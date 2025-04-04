package com.magicliang.transaction.sys.common.util;

import java.math.BigInteger;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 数学工具类
 *
 * @author magicliang
 *         <p>
 *         date: 2023-07-13 15:44
 */
public class MathUtil {

    /**
     * 私有构造器
     */
    private MathUtil() {
        throw new UnsupportedOperationException();
    }


    public static long greatestCommonDivisorOfNums(List<Long> nums) {
        final int size = nums.size();
        BigInteger[] bigNums = new BigInteger[size];
        for (int i = 0; i < size; i++) {
            bigNums[i] = BigInteger.valueOf(nums.get(i));
        }
        BigInteger result = bigNums[0];
        for (int i = 1; i < bigNums.length; i++) {
            result = result.gcd(bigNums[i]);
        }
        return result.longValue();
    }

    public static int convertToOneDigit(long number) {
        // 将数值转换为字符串
        String str = String.valueOf(number);
        // 取字符串的第一位字符
        char firstChar = str.charAt(0);
        // 将字符转换为整数
        int digit = Character.getNumericValue(firstChar);
        return digit;
    }

    public static <T> T getMiddle(T... a) {
        /*
         * 不可编译的原因是：编译器收到的参数以后先找 common super type，找到 Number 和 Comparable，这两者都无法转成 double
         * double middle = MathUtil.getMiddle(3.17, 1729, 0); // 不兼容的类型: Number & Comparable<? extends Number & Comparable<?>> 无法转换为 double
         * double middle = MathUtil.getMiddle(3, 1729, 0); // 可编译
         * Number middle = MathUtil.<Number>getMiddle(3.18, 1729, 0); // 使用类型见证
         * Number middle = MathUtil.getMiddle(3.18, 1729, 0); // 不使用类型见证
         *
         * 好的解法是：
         * 1. 禁止转成 double，确实不合法
         * 2. 尝试使用 bound 或者带有 bound 的 wildcard，这仍然会导致禁止赋值到 double 身上，虽然看起来几个数里找到中间数给 double 是合法的。
         *
         * 所以尝试给 primitive 类型写泛型计算方法会产生各式各样的苦手问题。
         */

        return a[a.length / 2];
    }


}
