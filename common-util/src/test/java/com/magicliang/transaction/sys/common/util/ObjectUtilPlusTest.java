package com.magicliang.transaction.sys.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2023-07-17 11:35
 */
class ObjectUtilPlusTest {

    @Test
    public void testAllNull() {
        assertTrue(ObjectUtilPlus.allNull(null, null, null));
    }

}