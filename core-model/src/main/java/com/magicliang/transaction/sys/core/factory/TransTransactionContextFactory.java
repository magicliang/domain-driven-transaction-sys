package com.magicliang.transaction.sys.core.factory;

import com.magicliang.transaction.sys.core.model.context.TransTransactionContext;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易上下文工厂
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 19:55
 */
public class TransTransactionContextFactory extends ContextFactory {

    /**
     * InsTransactionContext key
     */
    private static final String STANDARD_TRANSACTION_CONTEXT_KEY = "StandardTransactionContext";

    /**
     * 私有构造器
     */
    private TransTransactionContextFactory() {
        throw new UnsupportedOperationException();
    }

    /**
     * 静态工厂方法，获取交易上下文实例。
     * 正确的使用实践，是在第一次 getInstance 的时候就 initContext - 免得接下来的使用出问题
     * 因为线程封闭和线程隔离，这里无需加锁和 double check
     * 层级关系：
     * 1. ContextHolder 持有一个并发安全的 concurrentHashMap
     * 2. concurrentHashMap 按 key 承载上下文：不同的全领域、服务边界持有不同的上下文
     * 3. 上下文里才持有领域请求和响应 + 交易模型
     * 4. 领域请求和响应只关注本领域的输入输出，交易模型关注当前的聚合根关联的各个领域实体的状态变动
     * 这里这个工厂方法里允许多态变化的只有 Request 和 model
     * 不同的平台能力/商业产品可能产生不同的 Request 和 model
     *
     * @return 交易上下文实例
     */
    @SuppressWarnings("unchecked")
    public static <R, T extends TransactionModel> TransTransactionContext<R, T> getStandardTransactionContext() {
        // 1. 获取当前上下文的映射表
        Map<String, Object> realContextHolder = getContextHolder().get();
        // 如果映射表为空，则初始化映射表
        if (null == realContextHolder) {
            realContextHolder = new ConcurrentHashMap<>(CONTEXT_CACHE_SIZE);
            getContextHolder().set(realContextHolder);
        }
        // 如果 context 为空，则初始化 context
        TransTransactionContext<R, T> realContext;
        Object mapValue = realContextHolder.get(STANDARD_TRANSACTION_CONTEXT_KEY);
        // instanceof 包含 != null 的检查
        if (mapValue instanceof TransTransactionContext) {
            // 1. 如果持有这个上下文的类型是InsTransactionContext，直接 type casting，一般情况下这里不会发生类型的错误-由 key 和类型的绑定决定了此处的行为必然如此
            realContext = (TransTransactionContext<R, T>) mapValue;
        } else {
            // 在这里要慎重地使用懒人模式，在多线程里只 new 和 put 一个新的全局上下文，所以要引入 double-check 的下半部分
            synchronized (realContextHolder) {
                mapValue = realContextHolder.get(STANDARD_TRANSACTION_CONTEXT_KEY);
                if (mapValue instanceof TransTransactionContext) {
                    // 如果服务已经在其他线程里启动过了，此处直接返回
                    return (TransTransactionContext<R, T>) mapValue;
                }
                // 2. 否则使用标准类型覆盖原有 value
                realContext = new TransTransactionContext<>();
                // 这里不能用 putIfAbsent，必须使用新创建出来的 realContext
                realContextHolder.put(STANDARD_TRANSACTION_CONTEXT_KEY, realContext);
            }
        }
        return realContext;
    }
}
