package com.magicliang.transaction.sys.core.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 交易上下文工厂
 *
 * @author magicliang
 * <p>
 * date: 2021-12-31 19:46
 */
public abstract class ContextFactory {

    /**
     * 上下文缓存的尺寸
     * 内部使用的变量用继承也可以限定可见作用域
     * 如果需要广泛使用，则需要考虑一个包内常量了
     * 模块级常量要考虑模块级的复用
     */
    protected static final int CONTEXT_CACHE_SIZE = 256;

    /**
     * 上下文持有器
     * 全局静态变量，保存真正的交易上下文。因为是静态成员，所以无法引用每个实例特有的类型实参。
     * withInitial 没有被重载过，必须赋值给 ThreadLocal 类型，面向抽象类型编程更好。
     * <p>
     * 上下文是一个线程隔离的 ConcurrentHashMap，应对各种线程池的交接
     * holder 的存在，起到了线程安全地持有和隔离 map 的作用
     */
    private static final ThreadLocal<Map<String, Object>> CONTEXT_HOLDER = InheritableThreadLocal.withInitial(() -> new ConcurrentHashMap<>(CONTEXT_CACHE_SIZE));

    /**
     * 清理本线程上下文的内容
     */
    public static void clear() {
        final Map<String, Object> context = CONTEXT_HOLDER.get();
        // 彻底清理对 map 的引用所能引用到的句柄
        if (null != context) {
            context.clear();
        }
        // 然后剥离本 holder 的引用
        CONTEXT_HOLDER.remove();
    }

    /**
     * 获取上下文持有器
     *
     * @return 上下文持有器
     */
    public static ThreadLocal<Map<String, Object>> getContextHolder() {
        return CONTEXT_HOLDER;
    }
}
