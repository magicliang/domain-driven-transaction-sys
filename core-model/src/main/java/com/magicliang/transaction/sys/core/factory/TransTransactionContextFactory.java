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
     * StandardTransactionContext key
     */
    private static final String STANDARD_TRANSACTION_CONTEXT_KEY = "StandardTransactionContext";

    /**
     * 锁映射表
     */
    private static final Map<String, Object> LOCK_MAP = new ConcurrentHashMap<>();

    static {
        LOCK_MAP.put(STANDARD_TRANSACTION_CONTEXT_KEY, new Object());
    }

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
     * <p>
     * 暂时每产生一个新的子类可以产生一个新的工厂方法
     * 也可以产生一个新的子类
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
            // 此处其实不用并发隔离，因为 realContextHolder 是线程封闭的局部变量，而本线程的 set 只会 set 在本线程的 ThreadLocal 里
            getContextHolder().set(realContextHolder);
        }
        // 此时再取值，无论如何都取得出来
        realContextHolder = getContextHolder().get();
        // 如果 context 为空，则初始化 context
        TransTransactionContext<R, T> realContext;
        Object mapValue = realContextHolder.get(STANDARD_TRANSACTION_CONTEXT_KEY);
        // instanceof 包含 != null 的检查
        if (mapValue instanceof TransTransactionContext) {
            // 警告点：所有容器转成特定泛型类型的容器必然产生这种警告点，这种警告逻辑的正确性只能由put get 的逻辑设计保证
            realContext = (TransTransactionContext<R, T>) mapValue;
        } else {
            // 在这里要慎重地使用懒人模式，在多线程里只 new 和 put 一个新的全局上下文，所以要引入 double-check 的下半部分
            // 用 key 来表达锁相关性也是一个方法，最小限度地确定了锁的范围-专门为了本场景设计的锁对象是锁定范围最小的
            synchronized (LOCK_MAP.get(STANDARD_TRANSACTION_CONTEXT_KEY)) {
                mapValue = realContextHolder.get(STANDARD_TRANSACTION_CONTEXT_KEY);
                if (mapValue instanceof TransTransactionContext) {
                    // 如果服务已经在其他线程里启动过了，此处直接返回
                    // 警告点：所有容器转成特定泛型类型的容器必然产生这种警告点，这种警告逻辑的正确性只能由put get 的逻辑设计保证
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
