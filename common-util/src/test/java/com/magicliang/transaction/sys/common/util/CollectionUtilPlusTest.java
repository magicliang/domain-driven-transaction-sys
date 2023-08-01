package com.magicliang.transaction.sys.common.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 *
 * description:
 *
 * @author magicliang
 *
 *         date: 2023-07-19 21:37
 */
class CollectionUtilPlusTest {

    @Test
    public void isEqualCollection() {
        Map<Long, Long> map = new HashMap<>();
        map.put(0L, 3L);
        map.put(10020045L, 5L);
        map.put(10019039L, 1L);
        List<Long> list = Lists.newArrayList(10019039L, 10020045L, 0L);
        assertTrue(CollectionUtilPlus.isEqualCollection(map.keySet(), null));
    }
}