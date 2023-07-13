package com.magicliang.transaction.sys.common.util;

import java.math.BigInteger;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 数学工具类
 *
 * @author magicliang
 * <p>
 * date: 2023-07-13 15:44
 */
public class MathUtil {

    /**
     * 私有构造器
     */
    private MathUtil() {
        throw new UnsupportedOperationException();
    }


    public static long gcdOfNums(List<Long> nums) {
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
}
