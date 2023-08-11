package com.magicliang.transaction.sys.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 *         <p>
 *         date: 2022-04-25 16:03
 */
public class DhtUtilTest {

    @Test
    public void testGetBucket() {
        Long anything = 380080L;
        DhtUtil.DhtNode[] nodes = DhtUtil.getNodes(6);
        DhtUtil.DhtNode bucket = DhtUtil.getBucket(anything, nodes);
        long hashCode = DhtUtil.hashAnything(anything);
        long modValue = DhtUtil.modDhtValue(hashCode);
        Assertions.assertNotNull(bucket);
    }
}
