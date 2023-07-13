package com.magicliang.transaction.sys.common.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2023-07-13 15:45
 */
class MathUtilTest {

    @Test
    public void gcdOfNums() {
        final List<Long> l1 = Arrays.asList(5L, 3L, 1L);
        final List<Long> l2 = Arrays.asList(50L, 30L, 10L);
        final List<Long> l3 = Arrays.asList(6L, 4L);
        final List<Long> l4 = Arrays.asList(60L, 40L);
        assertEquals(1L, MathUtil.gcdOfNums(l1));
        assertEquals(10L, MathUtil.gcdOfNums(l2));
        assertEquals(2L, MathUtil.gcdOfNums(l3));
        assertEquals(20L, MathUtil.gcdOfNums(l4));
    }

}