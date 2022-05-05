package com.magicliang.transaction.sys.common.util.apm;

import com.magicliang.transaction.sys.common.util.apm.internal.DefaultTransaction;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * project name: leads_web_ut
 * <p>
 * description:
 * APM 监视器
 * 标准用法：
 * <code>
 * // 在所有的方法调用的时候都做这样的调用，在最外层的 transaction.complete() 的时候，会生成一棵树，树会告诉我们整个调用的热点在哪里
 * // 可以在公有方法，私有方法，for循环里使用这个方法，树总是能够自动生成的！
 * final Transaction transaction = ApmMonitor.beginTransaction("TransactionTest", "singleTransaction");
 * // 执行基本操作
 * transaction.addData(JacksonUtils.toJson(parameters));
 * transaction.complete();
 * </ code>。
 * <p>
 * 进阶用法：
 * 制造一个 aop：
 * 所有的方法类是 type
 * 所有的方法名是 name
 * 所有的方法参数是 data
 * <p>
 * 做嵌套调用的时候就可以看到整个调用树的性能数据：
 *
 * @author magicliang
 * <p>
 * date: 2022-04-28 20:42
 */
public class ApmMonitor {

    /**
     * 缺省日志器
     */
    private static final Logger log = LoggerFactory.getLogger(ApmMonitor.class);

    /**
     * 上下文持有器，持有消息树
     * 这个消息树的清理非常重要，不处理好会出现污染问题
     */
    @Getter
    private static final ThreadLocal<Transaction> context = new ThreadLocal<>();

    /**
     * 私有构造器
     */
    private ApmMonitor() {
        throw new UnsupportedOperationException();
    }

    /**
     * 生成一个事务
     *
     * @param type 事务类型
     * @param name 事务名称
     * @return 生成的事务
     */
    public static Transaction beginTransaction(final String type, final String name) {
        // 1. 生成本次需要的事务消息
        Transaction newTransaction = new DefaultTransaction(type, name);

        // 2. 在一个 ThreadLocal 里检查，当前是否有 Transaction，如果没有则当前的 Transaction 是根。
        Transaction rootTransaction = context.get();
        if (null == rootTransaction) {
            rootTransaction = newTransaction;
            // 注意，这个方法操作的是 ThreadLocal 变量，操作之间天然是线程隔离的，不用做 double check
            context.set(rootTransaction);
            // 如果此 Transaction 为 root，则直接返回
            return newTransaction;
        }
        return appendToTree(rootTransaction, newTransaction);
    }

    /**
     * 生成一个手动事务
     *
     * @param type 事务类型
     * @param name 事务名称
     * @return 生成的手动事务
     */
    public static Transaction beginManualTransaction(final String type, final String name) {
        // 1. 生成本次需要的事务消息
        Transaction newTransaction = new DefaultTransaction(type, name, false);

        // 2. 在一个 ThreadLocal 里检查，当前是否有 Transaction，如果没有则当前的 Transaction 是根。
        Transaction rootTransaction = context.get();
        if (null == rootTransaction) {
            rootTransaction = newTransaction;
            // 注意，这个方法操作的是 ThreadLocal 变量，操作之间天然是线程隔离的，不用做 double check
            context.set(rootTransaction);
            // 如果此 Transaction 为 root，则直接返回
            return newTransaction;
        }
        return appendToTree(rootTransaction, newTransaction);
    }

    /**
     * 从根节点开始，找到一个父节点，把此节点追加为它的一个新孩子（如果此节点没有 children，则作为首个孩子，否则追加为最新的兄弟）
     * 原则：
     * 1. 从根节点的孩子出发，：
     * 1.1. 如果当前根节点没有孩子，直接追加新孩子作为它的第一个孩子。
     * 1.2. 否则，遍历每个孩子：
     * 1.2.1. 如果此孩子的状态是为未完成，则将此孩子作为新的根节点，继续遍历
     * 1.2.2. 如果此孩子的状态为已完成，则遍历下一个孩子。
     * 1.2.3. 如果所有孩子都已完成，则把新孩子作为本根节点的新孩子追加为 children 列表里的一个新 sibling
     * 重要原则：一个已完成的事务不允许追加新事务（未完成）作为孩子。
     * <p>
     * Step1：
     * T1 begin -> T2 Begin
     * <p>
     * T2 的 parent 应该是 T1
     * <p>
     * Step2：
     * <p>
     * T1 begin -> T2 Begin End
     * -> T3 Begin
     * T3 的兄弟是 T2，T1 是它的 parent
     * <p>
     * Step3：
     * <p>
     * T1 begin -> T2 Begin End
     * -> T3 Begin
     * -> T4 Begin
     * T4 的 parent 应该是 T3
     * <p>
     * Step4：
     * <p>
     * T1 begin -> T2 Begin End
     * -> T3 Begin
     * -> T4 Begin End
     * End
     * End
     * 这样树形结构的闭环就形成了，这要求所有的 Transaction 在兄弟 Transaction 开始之前必须合理关闭，在返回给自己的 parent 之前必须合理关闭
     *
     * @param rootTransaction 当前的根消息
     * @param newTransaction  待追加的消息
     * @return 适合做父事务
     */
    private static Transaction appendToTree(final Transaction rootTransaction, final Transaction newTransaction) {
        if (null == rootTransaction || null == newTransaction) {
            // 未来可能在此处抛出异常更好，待定
            return newTransaction;
        }
        if (rootTransaction.isCompleted()) {
            /*
             * 如果当前根节点已完成，则不允许再追加孩子，得到一个没有 parent 的节点，对它进行 complete 不会触发对消息树的修改，所以这是一个无用的 dummy 对象
             * 未来可能在此处抛出异常更好，待定
             */
            return newTransaction;
        }

        List<Message> children = rootTransaction.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            // 根据原则 1.1：追加孩子后返回
            rootTransaction.addChild(newTransaction);
            return newTransaction;
        }

        for (Message child : children) {
            // 根据原则 1.2.1，在未完成的孩子里继续完成追加动作
            if (!child.isCompleted() && child instanceof Transaction) {
                return appendToTree((Transaction) child, newTransaction);
            }
        }

        // 根据原则 1.2.3，将孩子作为本根节点的一个新sibling
        rootTransaction.addChild(newTransaction);
        return newTransaction;
    }

    /**
     * 清理监视器里的内容并打印监控日志
     */
    public static void flushMonitorAndLog() {
        flushMonitorAndLog(null);
    }

    /**
     * 清理监视器里的内容并打印监控日志，如果外部提供日志器则使用外部提供的日志器，否则使用内置的日志器
     *
     * @param externalLogger 外部日志器
     */
    public static void flushMonitorAndLog(Logger externalLogger) {
        Logger logger = Objects.nonNull(externalLogger) ? externalLogger : log;
        final String s = flushMonitor();
        logger.info(s);
    }

    /**
     * 输出监控日志并清理监视器里的内容
     *
     * @return 监控日志
     */
    public static String flushMonitor() {
        // 生成可视化的日志输出
        Transaction rootTransaction = context.get();
        final String monitorLog = rootTransaction.toMonitorLog();
        // 清理上下文
        context.remove();
        return monitorLog;
    }

}
