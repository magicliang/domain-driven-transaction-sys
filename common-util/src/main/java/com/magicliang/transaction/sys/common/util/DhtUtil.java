package com.magicliang.transaction.sys.common.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 分布式散列表工具
 *
 * @author magicliang
 * <p>
 * date: 2022-04-25 15:11
 */
@Slf4j
public class DhtUtil {

    /**
     * 42 亿多，必须使用 long 才能承载得下，int会直接编译报错，大致上等于 Integer.MAX_VALUE * 2L + 2
     */
    private static final long TWO_POWER_32 = 4294967296L;

    /**
     * 私有构造器
     */
    private DhtUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据节点数量生成一个节点数组
     *
     * @param nodeCnt 节点数量
     * @return 节点数组
     */
    public static DhtNode[] getNodes(final int nodeCnt) {
        if (nodeCnt <= 0) {
            throw new IllegalArgumentException("nodeCnt must be greater than zero!");
        }
        long[] tokens = new long[nodeCnt];
        for (int i = 0; i < nodeCnt; i++) {
            tokens[i] = ThreadLocalRandom.current().nextLong(0, TWO_POWER_32);
        }
        // 用 tokens 升序保证 nodes 升序
        Arrays.sort(tokens);

        DhtNode[] nodes = new DhtNode[nodeCnt];
        for (int i = 0; i < nodeCnt; i++) {
            DhtNode node = new DhtNode();
            node.setToken(tokens[i]);
            node.setUuid(UUID.randomUUID().toString());
            nodes[i] = node;
        }

        return nodes;
    }

    /**
     * 获取任意对象在 DHT 里的桶位置
     *
     * @param anything 任意对象
     * @return 在 DHT 里的桶位置
     */
    public static DhtNode getBucket(Object anything, DhtNode[] nodes) {
        if (null == anything) {
            throw new IllegalArgumentException("anything must be non-null!");
        }
        if (ArrayUtils.isEmpty(nodes)) {
            throw new IllegalArgumentException("nodes must be non-null!");
        }
        long modValue = modDhtValue(hashAnything(anything));
        // 遍历所有节点，找到第一个大于等于 anything 的节点的值，注意越界循环问题
        for (DhtNode node : nodes) {
            if (node.getToken() >= modValue) {
                return node;
            }
        }
        // 否则返回第一个 node，因为可能 anything 的 modValue 大于任何 node 的token，这时候就需要使用循环的值
        return nodes[0];
    }

    /**
     * 获取分布式散列的值
     *
     * @param hashCode 原始散列码
     * @return 分布式散列的值
     */
    public static long modDhtValue(long hashCode) {
        // 小心，所有的 bytes to long 有一半概率得到负值结果
        return Math.abs(hashCode % TWO_POWER_32);
    }

    /**
     * 给任何东西做散列
     *
     * @param anything 任何东西
     * @return 散列结果
     */
    public static long hashAnything(Object anything) {
        if (null == anything) {
            throw new IllegalArgumentException("anything must be non-null!");
        }
        // 散列必备步骤：

        // 1. 转化出待散列的字节数组
        byte[] bytesOfMessage = new byte[0];
        try {
            bytesOfMessage = anything.toString().getBytes("UTF-8");
            // 2. 获取 md5 的 md 实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 3. 对原始的字节数组进行消息摘要，生成结果字节数组
            byte[] theMD5digest = md.digest(bytesOfMessage);
            // 转为 long 的附加步骤1：截断前8位。转化为 int 的附加步骤：截断前8位或者直接 hashCode。
            byte[] truncatedDigest = Arrays.copyOf(theMD5digest, 8);
            // 转为 long 的附加步骤2：位移加法
            return bytes2Long(truncatedDigest);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            log.error("hashAnything exception: ", e);
        }
        throw new IllegalStateException("unable to digest anything");
    }

    /**
     * 转化字节数组到长整型数
     *
     * @param bytes 字节数组
     * @return 长整型数
     */
    private static long bytes2Long(byte[] bytes) {
        long value = 0;
        for (byte b : bytes) {
            // 位移加法，经典中的经典
            value = (value << 8) + (b & 0xFF);
        }
        return value;
    }

    /**
     * 转化字节数组到整型数
     *
     * @param bytes 字节数组
     * @return 整型数
     */
    private static int bytes2Int(byte[] bytes) {
        int value = 0;
        for (byte b : bytes) {
            // 位移加法，经典中的经典
            value = (value << 8) + (b & 0xFF);
        }
        return value;
    }

    @Data
    public static class DhtNode {
        /**
         * 物理 id
         */
        private long id;

        /**
         * 分布式的 uuid
         */
        private String uuid;

        /**
         * 分布式节点的令牌
         */
        private long token;
    }
}
