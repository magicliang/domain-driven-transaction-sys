package com.magicliang.transaction.sys.common.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: UUID 生成器
 *
 * @author magicliang
 * <p>
 * date: 2021-12-30 17:24
 */
public class UUIDGenerator {

    /**
     * 私有构造器
     */
    private UUIDGenerator() {
        throw new UnsupportedOperationException();
    }

    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    /**
     * randomBit的随机数
     *
     * @param randomBit
     * @return
     */
    public static String getRandomUuid(int randomBit) {
        String zero = String.format("%0" + (randomBit - 1) + "d", 0);
        String startValueStr = 1 + zero;
        String endValueStr = 9 + zero;
        int random = ThreadLocalRandom.current().nextInt(Integer.parseInt(startValueStr), Integer.parseInt(endValueStr));
        return String.valueOf(random);
    }
}
