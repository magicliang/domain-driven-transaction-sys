package com.magicliang.transaction.sys.common.constant;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 时间常量
 *
 * @author magicliang
 * <p>
 * date: 2021-12-28 20:41
 */
public class TimeConstant {

    /**
     * 一分钟六十秒
     */
    public static final int ONE_MINUTE_IN_SECOND = 60;
    /**
     * 缺省执行间隔
     * 如果引入优先级调度策略，则需要用规则来调节这个间隔
     */
    public static final int DEFAULT_EXECUTION_INTERVAL_IN_MINUTES = 10;

    /**
     * 私有构造器
     */
    private TimeConstant() {
        throw new UnsupportedOperationException();
    }

}
