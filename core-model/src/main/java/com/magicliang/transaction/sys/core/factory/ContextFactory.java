package com.magicliang.transaction.sys.core.factory;

import org.springframework.core.NamedInheritableThreadLocal;

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
     * <p>
     * 类似的容器还有 Spring 的 RequestContextHolder、TransactionSynchronizationManager、LocaleContextHolder
     * <p>
     * ThreadLocal 本质上是 ThreadLocalVar
     */
    private static final ThreadLocal<Map<String, Object>> CONTEXT_HOLDER = NamedInheritableThreadLocal.withInitial(() -> new ConcurrentHashMap<>(CONTEXT_CACHE_SIZE));

    /**
     * 清理本线程上下文的内容
     * 重要：这类方法必须支持空清扫
     */
    public static void clear() {
        final Map<String, Object> context = CONTEXT_HOLDER.get();

        /*
         * 在某些场景下，子线程可能越过本上下文获取 context 里的信息，会最终导致上下文越来越臃肿，引发最终的 fgc，所以如果有必要：
         * 1. 每个子线程使用context里的对象的时候必须显式地透过 context。
         * 2. 不能在本对象内持有句柄。
         * 3. 如果有必要，本 context 的清理需要逐层深入-这取决于 context 是否有明确的显式结构。
         * 4. 子线程在使用另一个 InheritableThreadLocal，却不知道自己该主动 clear 什么别的数据，也会造成内存泄漏。
         */

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
