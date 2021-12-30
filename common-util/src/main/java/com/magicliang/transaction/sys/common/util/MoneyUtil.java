package com.magicliang.transaction.sys.common.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 金钱工具类
 *
 * @author magicliang
 * <p>
 * date: 2021-12-29 17:27
 */
public class MoneyUtil {

    private static final BigDecimal YUAN_FEN_RATE = BigDecimal.valueOf(100L);

    private static final int MIN_PRICE = 10;

    /**
     * 私有构造器
     */
    private MoneyUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * 分乘以百分比，取整。四舍五入
     *
     * @param fen     基数
     * @param percent 比例
     * @return 计算结果
     */
    public static int calcPercentValue(long fen, int percent) {
        BigDecimal fenCost = new BigDecimal(fen).multiply(new BigDecimal(percent)).divide(new BigDecimal(100), RoundingMode.HALF_UP);
        return fenCost.intValue();
    }

    /**
     * 分转元，四舍五入，保留2位小数
     *
     * @param fen 金额，单位分
     * @return 金额，单位元
     */
    public static String fenToYuan(int fen) {
        BigDecimal cost = new BigDecimal(fen);
        cost = cost.divide(new BigDecimal(100));
        cost.setScale(2, BigDecimal.ROUND_HALF_UP);
        return cost.toString();
    }

    /**
     * 元转分
     *
     * @param amount4Yuan 元金额
     * @return 分结果
     */
    public static long yuanToFen(double amount4Yuan) {
        String amount4YuanStr = String.valueOf(amount4Yuan);
        BigDecimal decimalYuan = new BigDecimal(amount4YuanStr);
        BigDecimal decimalFen = decimalYuan.multiply(new BigDecimal(100));
        return decimalFen.longValue();
    }


    /**
     * 分转元
     *
     * @param fen 分
     * @return 元
     */
    public static BigDecimal fen2Yuan(String fen) {
        return fen2Yuan(Long.valueOf(fen));
    }

    /**
     * 分转元
     *
     * @param fen 分
     * @return 元
     */
    public static BigDecimal fen2Yuan(long fen) {
        return fen2Yuan((Long) fen);
    }

    /**
     * 四舍五入进入得到金额的字符串
     *
     * @param money 金额，单位为元
     * @return 金额的字符串
     */
    public static String toString(final BigDecimal money) {
        if (Objects.isNull(money)) {
            return "";
        }
        return money.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }


    /**
     * 分转元
     *
     * @param fen 分
     * @return 元
     */
    public static BigDecimal fen2Yuan(Long fen) {
        return new BigDecimal(fen).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * 0的话 返回 1
     *
     * @param fen
     * @return
     */
    public static BigDecimal fen2YuanRoundDown(Integer fen) {
        BigDecimal price = new BigDecimal(fen).divide(new BigDecimal(100), 0, BigDecimal.ROUND_DOWN);
        return price.equals(new BigDecimal(0)) ? new BigDecimal(1) : price;
    }

    /**
     * 将整数价格转换为整十数
     * 规则：小于10 那么返回10 大于10 那么按照个位数字进行四舍五入 11->10 15->20 123->120 125->130
     *
     * @param price
     * @return
     */
    public static BigDecimal getRoundHalfUpPrice(BigDecimal price) {

        //如果价格小于十元 那么返回十元
        if (price.compareTo(new BigDecimal(MIN_PRICE)) < 0) {
            return new BigDecimal(MIN_PRICE);
        }
        BigDecimal priceToTenYuan = price.divide(new BigDecimal(10), 0, RoundingMode.HALF_UP);
        return priceToTenYuan.multiply(new BigDecimal(10));
    }

    public static Integer yuan2fen(String yuan) {
        if (StringUtils.isBlank(yuan)) {
            return 0;
        }
        return new BigDecimal(yuan).multiply(new BigDecimal(100)).intValue();
    }

    public static Long yuan2fen(BigDecimal yuan) {
        return yuan.multiply(YUAN_FEN_RATE).longValue();
    }

}
