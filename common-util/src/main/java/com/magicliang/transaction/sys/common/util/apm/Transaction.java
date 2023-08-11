package com.magicliang.transaction.sys.common.util.apm;

import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 监控事务
 * <p>
 * 原型：https://github.com/dianping/cat/blob/master/lib/java/src/main/java/com/dianping/cat/message/Transaction.java
 * <p>
 * 事务的设计理念是：通过 composite 模式将操作自包含起来，然后统一输出
 *
 * @author magicliang
 *         <p>
 *         date: 2022-04-28 18:38
 */
public interface Transaction extends Message {

    /**
     * Add one nested child message to current transaction.
     *
     * @param message to be added
     * @return 当前事务
     */
    Transaction addChild(Message message);

    /**
     * Get all children message within current transaction.
     *
     * <p>
     * Typically, a <code>Transaction</code> can nest other <code>Transaction</code>s, <code>Event</code>s and
     * <code>Heartbeat</code>
     * s, while an <code>Event</code> or <code>Heartbeat</code> can't nest other messages.
     * </p>
     *
     * @return all children messages, empty if there is no nested children.
     */
    List<Message> getChildren();

    /**
     * get the value of finalLog
     *
     * @return the value of finalLog
     */
    String getFinalLog();

    /**
     * get the value of endTimestampInMillis
     *
     * @return the value of endTimestampInMillis
     */
    long getEndTimestampInMillis();

    /**
     * get the value of durationInMillis
     *
     * @return the value of durationInMillis
     */
    long getDurationInMillis();
}
