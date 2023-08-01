package com.magicliang.transaction.sys.common.util.apm.internal;

import com.magicliang.transaction.sys.common.util.JsonUtils;
import com.magicliang.transaction.sys.common.util.apm.ApmMonitor;
import com.magicliang.transaction.sys.common.util.apm.Message;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 缺省事务类
 * 原型：https://github.com/dianping/cat/blob/master/lib/java/src/main/java/com/dianping/cat/message/internal
 * /DefaultTransaction.java
 * 这个 DefaultTransaction 目前还不支持跨线程传递消息，敬请期待
 * 缺一个缺省的 aop 拦截器：方法内部调用里出现异常在所难免，因此在 finally 里对事物进行完成至关重要，不正确完成的事务的测度数据是不准的
 *
 * @author magicliang
 *         <p>
 *         date: 2022-04-28 20:32
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
     * 最终日志，在事务完成时留档用
     */
    private String finalLog;

    /**
     * 全参构造器
     *
     * @param type 消息类型
     * @param name 消息名称
     * @param autoLogging 是否自动在日志里记录上下文
     */
    public DefaultTransaction(final String type, final String name, final boolean autoLogging) {
        super(type, name);
        this.autoLog = autoLogging;
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
     * Typically, a <code>Transaction</code> can nest other <code>Transaction</code>s, <code>Event</code>s and
     * <code>Heartbeat</code>
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
     * 1. complete 所有子事务
     * 2. complete 父类操作
     * 3. complete 本事务
     * 4. 如果本节点是跟，清理上下文-清理上下文必须在这个
     */
    @Override
    public void complete() {
        // 已完成的事务不允许再完成
        if (isCompleted()) {
            return;
        }
        try {
            final List<Message> children = getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                sortChildrenByTimestamp();
                for (Message child : children) {
                    // composite 模式必须要做的事情：对孩子进行逐层完结
                    child.complete();
                }
            }
            super.complete();
            setEndTimestampInMillis(System.currentTimeMillis());
            calculateDuration();
        } finally {
            // 无论上方发生了什么，如果当前节点是根节点，必定会清理上下文
            if (isRoot()) {
                if (autoLog) {
                    // 清理上下文并记录日志
                    ApmMonitor.flushMonitorAndLog();
                } else {
                    // 只是清理上下文，不记录日志
                    ApmMonitor.flushMonitor();
                }
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
     * 产生给监控器的结构化日志
     *
     * @return 给监控器的结构化日志
     */
    @Override
    public String toMonitorLog() {
        return finalLog = super.toMonitorLog();
    }

    /**
     * get the value of finalLog
     *
     * @return the value of finalLog
     */
    @Override
    public String getFinalLog() {
        return finalLog;
    }

    /**
     * 确认当前的事务是否根事务
     *
     * @return 当前的事务是否根事务
     */
    private boolean isRoot() {
        return Objects.equals(ApmMonitor.getRootTransaction(), this);
    }

    /**
     * 对孩子列表按照时间戳进行排序
     */
    private void sortChildrenByTimestamp() {
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        // 对于一般的底层数据结构而言，这个排序不会产生任何变化，因为它们是按照插入顺序来访问的，但以后如果引入多线程的 Message，则在输出孩子之前要先做排序
        children.sort(Comparator.comparing(Message::getTimestamp));
    }
}