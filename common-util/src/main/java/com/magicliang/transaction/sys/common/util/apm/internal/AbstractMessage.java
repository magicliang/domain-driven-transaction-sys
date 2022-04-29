package com.magicliang.transaction.sys.common.util.apm.internal;

import com.magicliang.transaction.sys.common.util.apm.Message;
import lombok.ToString;

/**
 * project name: leads_web_ut
 * <p>
 * description: 抽象消息
 * <p>
 * 原型：https://github.com/dianping/cat/blob/master/lib/java/src/main/java/com/dianping/cat/message/internal/AbstractMessage.java
 *
 * @author magicliang
 * <p>
 * date: 2022-04-28 20:23
 */
@ToString
public class AbstractMessage implements Message {

    /**
     * 未初始化时间戳
     */
    public static final int UNINITIALIZED_TIMESTAMP = -1;

    /**
     * 消息类型
     */
    protected final String type;

    /**
     * 消息名称
     */
    private final String name;
    /**
     * 本事务所处的线程名称
     */
    private final String threadName;
    /**
     * 生成消息的时间戳，单位为毫秒
     * -1 即未初始化
     */
    private long timestampInMillis;
    /**
     * 消息构造是否已完成
     */
    private boolean completed;
    /**
     * 追加数据
     */
    private CharSequence data;

    /**
     * 抽象构造器
     *
     * @param type 消息类型
     * @param name 消息名称
     */
    AbstractMessage(String type, String name) {
        this.type = String.valueOf(type);
        this.name = String.valueOf(name);
        this.timestampInMillis = System.currentTimeMillis();
        this.completed = false;
        this.threadName = Thread.currentThread().getName();
    }

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
    @Override
    public String getType() {
        return type;
    }

    /**
     * Message name.
     *
     * @return message name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * The time stamp the message was created.
     *
     * @return message creation time stamp in milliseconds
     */
    @Override
    public long getTimestamp() {
        return timestampInMillis;
    }

    /**
     * 设置时间戳
     *
     * @param timestamp 待设置时间戳
     */
    @Override
    public void setTimestamp(long timestamp) {
        this.timestampInMillis = timestamp;
    }

    /**
     * If the complete() method was called or not.
     *
     * @return true means the complete() method was called, false otherwise.
     */
    @Override
    public boolean isCompleted() {
        return completed;
    }

    /**
     * 设置完成态
     *
     * @param completed 设置完成态
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Complete the message construction.
     */
    @Override
    public void complete() {
        // 已完成的消息不允许再完成
        if (isCompleted()) {
            return;
        }
        setCompleted(true);
    }

    /**
     * add one or multiple key-value pairs to the message.
     *
     * @param keyValuePairs key-value pairs like 'a=1&b=2&...'
     */
    @Override
    public void addData(String keyValuePairs) {
        if (data == null) {
            data = keyValuePairs;
        } else if (data instanceof StringBuilder) {
            ((StringBuilder) data).append('&').append(keyValuePairs);
        } else {
            StringBuilder sb = new StringBuilder(data.length() + keyValuePairs.length() + 16);

            sb.append(data).append('&');
            sb.append(keyValuePairs);
            data = sb;
        }
    }

    /**
     * add one key-value pair to the message.
     *
     * @param key   data key
     * @param value data value
     */
    @Override
    public void addData(String key, Object value) {
        if (data instanceof StringBuilder) {
            ((StringBuilder) data).append('&').append(key).append('=').append(value);
        } else {
            String str = String.valueOf(value);
            int old = data == null ? 0 : data.length();
            StringBuilder sb = new StringBuilder(old + key.length() + str.length() + 16);

            if (data != null) {
                sb.append(data).append('&');
            }

            sb.append(key).append('=').append(str);
            data = sb;
        }
    }

    @Override
    public CharSequence getData() {
        if (data == null) {
            return "";
        } else {
            return data;
        }
    }

}