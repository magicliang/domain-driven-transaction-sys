package com.magicliang.transaction.sys.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 *         <p>
 *         date: 2023-07-13 15:45
 */
class MathUtilTest {

    @Test
    public void testGcdOfNums() {
        final List<Long> l1 = Arrays.asList(5L, 3L, 1L);
        final List<Long> l2 = Arrays.asList(50L, 30L, 10L);
        final List<Long> l3 = Arrays.asList(6L, 4L);
        final List<Long> l4 = Arrays.asList(60L, 40L);
        assertEquals(1L, MathUtil.greatestCommonDivisorOfNums(l1));
        assertEquals(10L, MathUtil.greatestCommonDivisorOfNums(l2));
        assertEquals(2L, MathUtil.greatestCommonDivisorOfNums(l3));
        assertEquals(20L, MathUtil.greatestCommonDivisorOfNums(l4));
    }

    @Test
    public void testConvertToOneDigit() {
        assertEquals(6, MathUtil.convertToOneDigit(60));
        assertEquals(5, MathUtil.convertToOneDigit(540));
        assertEquals(7, MathUtil.convertToOneDigit(700));

    }

}