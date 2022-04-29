package com.magicliang.transaction.sys.common.util.apm.internal;

import com.magicliang.transaction.sys.common.util.JsonUtils;
import com.magicliang.transaction.sys.common.util.apm.ApmMonitor;
import com.magicliang.transaction.sys.common.util.apm.Message;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * project name: leads_web_ut
 * <p>
 * description: 缺省事务类
 * 原型：https://github.com/dianping/cat/blob/master/lib/java/src/main/java/com/dianping/cat/message/internal/DefaultTransaction.java
 * 这个 DefaultTransaction 目前还不支持跨线程传递消息，敬请期待
 *
 * @author magicliang
 * <p>
 * date: 2022-04-28 20:32
 */
@EqualsAndHashCode(callSuper = true)
public class DefaultTransaction extends AbstractTransaction {

    /**
     * 是否自动 flush 上下文
     */
    private final boolean autoLog;

    /**
     * 孩子节点
     */
    private volatile List<Message> children;

    /**
     * 全参构造器
     *
     * @param type 消息类型
     * @param name 消息名称
     */
    public DefaultTransaction(String type, String name) {
        this(type, name, true);
    }

    /**
     * 全参构造器
     *
     * @param type    消息类型
     * @param name    消息名称
     * @param autoLog 是否自动在日志里记录上下文
     */
    public DefaultTransaction(final String type, final String name, final boolean autoLog) {
        super(type, name);
        this.autoLog = autoLog;
    }

    /**
     * Add one nested child message to current transaction.
     *
     * @param message to be added
     * @return DefaultTransaction
     */
    @Override
    public DefaultTransaction addChild(Message message) {
        if (null == children) {
            synchronized (this) {
                if (null == children) {
                    // 使用任一线性表，可以保证按插入顺序遍历即可
                    children = new ArrayList<>();
                }
            }
        }
        children.add(message);
        return this;
    }

    /**
     * Get all children message within current transaction.
     *
     * <p>
     * Typically, a <code>Transaction</code> can nest other <code>Transaction</code>s, <code>Event</code>s and <code>Heartbeat</code>
     * s, while an <code>Event</code> or <code>Heartbeat</code> can't nest other messages.
     * </p>
     *
     * @return all children messages, empty if there is no nested children.
     */
    @Override
    public List<Message> getChildren() {
        return children;
    }

    /**
     * Complete the message construction.
     * 未来引入状态机，则引入 complete 对状态的修改
     */
    @Override
    public void complete() {
        // 已完成的事务不允许再完成
        if (isCompleted()) {
            return;
        }
        final List<Message> children = getChildren();
        if (CollectionUtils.isNotEmpty(children)) {
            for (Message child : children) {
                // composite 模式必须要做的事情：对孩子进行逐层完结
                child.complete();
            }
        }
        super.complete();
        setEndTimestampInMillis(System.currentTimeMillis());
        calculateDuration();

        // 如果当前是根节点，必定会刷新日志
        if (isRoot()) {
            if (autoLog) {
                ApmMonitor.flushMonitorAndLog();
            } else {
                ApmMonitor.flushMonitor();
            }
        }
    }

    /**
     * toString 的另一种做法
     * <p>
     * return "DefaultTransaction{" +
     * "type=" + getType() +
     * ", name=" + getName() +
     * ", beginTime=" + getTimestamp() +
     * ", endTime=" + getEndTimestampInMillis() +
     * ", duration=" + getDurationInMillis() +
     * ", data=" + getData() +
     * ", children=" + children +
     * '}';
     *
     * @return 字符串形式
     */
    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }

    /**
     * 确认当前的事务是否根事务
     *
     * @return 当前的事务是否根事务
     */
    private boolean isRoot() {
        return Objects.equals(ApmMonitor.getContext().get(), this);
    }
}
