package com.magicliang.transaction.sys.common.util.apm;

/**
 * project name: leads_web_ut
 * <p>
 * description:
 * 监控消息
 * 一个穷人版的监控结构体基接口
 * 原型：
 * https://github.com/dianping/cat/blob/master/lib/java/src/main/java/com/dianping/cat/message/Message.java
 *
 * @author magicliang
 * <p>
 * date: 2022-04-28 18:31
 */
public interface Message {

    /**
     * Message type.
     * <p>
     * <p>
     * Typical message types are:
     * <ul>
     * <li>URL: maps to one method of an action</li>
     * <li>Service: maps to one method of service call</li>
     * <li>Search: maps to one method of search call</li>
     * <li>SQL: maps to one SQL statement</li>
     * <li>Cache: maps to one cache access</li>
     * <li>Error: maps to java.lang.Throwable (java.lang.Exception and java.lang.Error)</li>
     * </ul>
     * </p>
     *
     * @return message type
     */
    String getType();

    /**
     * Message name.
     *
     * @return message name
     */
    String getName();

    /**
     * The time stamp the message was created.
     *
     * @return message creation time stamp in milliseconds
     */
    long getTimestamp();

    /**
     * 设置时间戳
     *
     * @param timestamp 待设置时间戳
     */
    void setTimestamp(long timestamp);

    /**
     * Complete the message construction.
     */
    void complete();

    /**
     * If the complete() method was called or not.
     *
     * @return true means the complete() method was called, false otherwise.
     */
    boolean isCompleted();

    /**
     * add one or multiple key-value pairs to the message.
     *
     * @param keyValuePairs key-value pairs like 'a=1&b=2&...'
     */
    void addData(String keyValuePairs);

    /**
     * add one key-value pair to the message.
     *
     * @param key   data key
     * @param value data value
     */
    void addData(String key, Object value);

    /**
     * 获取附加数据，可以用来存储参数
     *
     * @return key value pairs data
     */
    Object getData();

    /**
     * 产生给监控器的结构化日志
     *
     * @return 给监控器的结构化日志
     */
    default String toMonitorLog() {
        return toString();
    }

}
