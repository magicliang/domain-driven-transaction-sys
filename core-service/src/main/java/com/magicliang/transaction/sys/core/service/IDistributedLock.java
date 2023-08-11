package com.magicliang.transaction.sys.core.service;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 分布式锁服务接口
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-05 15:23
 */
public interface IDistributedLock {

    /**
     * 只加锁
     *
     * @param lockName 锁名称
     * @param expiredTimeInSeconds 超时时间，单位为秒
     * @return 锁实例
     */
    Lock getLock(String lockName, int expiredTimeInSeconds);

    /**
     * 加锁后调用求值
     *
     * @param lockName 锁名称
     * @param expiredTimeInSeconds 超时时间，单位为秒
     * @param callable 可调用值
     * @param <T> 返回值类型变量
     * @return 调用返回值
     */
    <T> T lockAndCall(String lockName, int expiredTimeInSeconds, Callable<T> callable);

    /**
     * 求阻塞锁并执行回调。
     * 无超时，慎用。锁的淘汰依赖于底层集群的实现和策略。
     *
     * @param lockName 锁名称
     * @param expiredTimeInSeconds 超时时间，单位为秒
     * @param runnable 回调闭包
     */
    void lockAndRun(String lockName, int expiredTimeInSeconds, Runnable runnable);

    /**
     * 试锁并执行回调，试锁不成功什么都不做。
     * 不需要考虑超时问题
     *
     * @param lockName 锁名称
     * @param expiredTimeInSeconds 超时时间，单位为秒
     * @param runnable 回调闭包
     */
    void tryLock(String lockName, int expiredTimeInSeconds, Runnable runnable);

    /**
     * 试锁并执行回调，试锁不成功执行回退回调。
     * 不需要考虑超时问题
     *
     * @param lockName 锁名称
     * @param expiredTimeInSeconds 超时时间，单位为秒
     * @param runnable 回调闭包
     * @param elseRunnable 回退回调闭包
     */
    void tryLock(String lockName, int expiredTimeInSeconds, Runnable runnable, Runnable elseRunnable);

    /**
     * 计时试锁并执行回调，试锁不成功执行回退回调。
     * 不需要考虑超时问题。
     *
     * @param lockName 锁名称
     * @param time 计时时间
     * @param timeUnit 时间单位
     * @param runnable 回调闭包
     * @param elseRunnable 回退回调闭包
     * @param expiredTimeInSeconds 超时时间，单位为秒
     * @throws InterruptedException 中断异常
     */
    void tryLock(String lockName,
            long time,
            TimeUnit timeUnit,
            Runnable runnable,
            Runnable elseRunnable,
            int expiredTimeInSeconds) throws InterruptedException;

    /**
     * 求可中断阻塞锁并执行回调。
     *
     * @param lockName 锁名称
     * @param expiredTimeInSeconds 超时时间，单位为秒
     * @param runnable 回调闭包
     * @throws InterruptedException 中断异常
     */
    void lockInterruptibly(String lockName, int expiredTimeInSeconds, Runnable runnable) throws InterruptedException;
}
