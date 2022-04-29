package com.magicliang.transaction.sys.common.util.apm.internal;

import com.magicliang.transaction.sys.common.util.apm.Transaction;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * project name: leads_web_ut
 * <p>
 * description: 抽象事务
 *
 * @author magicliang
 * <p>
 * date: 2022-04-29 12:03
 */
@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class AbstractTransaction extends AbstractMessage implements Transaction {

    /**
     * 消息终结的时间戳，单位为毫秒
     * -1 即未初始化
     */
    private long endTimestampInMillis = UNINITIALIZED_TIMESTAMP;

    /**
     * 消息经历的时间，单位为毫秒
     * -1 即未初始化
     */
    private long durationInMillis = UNINITIALIZED_TIMESTAMP;

    /**
     * 全参构造器
     *
     * @param type 消息类型
     * @param name 消息名称
     */
    public AbstractTransaction(String type, String name) {
        super(type, name);
    }

    /**
     * get the value of endTimestampInMillis
     *
     * @return the value of endTimestampInMillis
     */
    public long getEndTimestampInMillis() {
        return endTimestampInMillis;
    }

    /**
     * set the value of the endTimestampInMillis
     *
     * @param endTimestampInMillis the value of endTimestampInMillis
     */
    public void setEndTimestampInMillis(long endTimestampInMillis) {
        this.endTimestampInMillis = endTimestampInMillis;
    }

    /**
     * get the value of durationInMillis
     *
     * @return the value of durationInMillis
     */
    public long getDurationInMillis() {
        return durationInMillis;
    }

    /**
     * 计算耗时
     */
    public void calculateDuration() {
        // 有任意的时间戳未初始化，或者时间戳不合法（因为墙上时钟精度问题，timestampInMillis 应该小于等于 endTimestampInMillis 才合法），则不可计算
        if (UNINITIALIZED_TIMESTAMP == getTimestamp() || UNINITIALIZED_TIMESTAMP == endTimestampInMillis || getTimestamp() > endTimestampInMillis) {
            return;
        }
        this.durationInMillis = endTimestampInMillis - getTimestamp();
    }

}
