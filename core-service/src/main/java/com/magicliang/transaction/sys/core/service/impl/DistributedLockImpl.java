package com.magicliang.transaction.sys.core.service.impl;

import com.magicliang.transaction.sys.common.exception.DistributedLockException;
import com.magicliang.transaction.sys.core.manager.IDistributedLockManager;
import com.magicliang.transaction.sys.core.service.IDistributedLock;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-05 15:24
 */
@Slf4j
@Service
public class DistributedLockImpl implements IDistributedLock {

    /**
     * cerberus 分布式锁服务
     */
    @Autowired
    private IDistributedLockManager distributedLockManager;

    /**
     * 只加锁
     *
     * @param lockName 锁名称
     * @param expiredTimeInSeconds 超时时间，单位为秒
     * @return 锁实例
     */
    @Override
    public Lock getLock(String lockName, int expiredTimeInSeconds) {
        if (StringUtils.isAllBlank(lockName)) {
            throw new DistributedLockException("invalid lockName：" + lockName);
        }
        if (expiredTimeInSeconds <= 0) {
            throw new DistributedLockException("invalid expiredTimeInSeconds：" + expiredTimeInSeconds);
        }
        return distributedLockManager.getReentrantLock(lockName, expiredTimeInSeconds);
    }

    /**
     * 加锁后调用求值
     *
     * @param lockName 锁名称
     * @param expiredTimeInSeconds 超时时间，单位为秒
     * @param callable 可调用值
     * @param <T> 返回值类型变量
     * @return 调用返回值
     */
    @Override
    public <T> T lockAndCall(String lockName, int expiredTimeInSeconds, Callable<T> callable) {
        if (Objects.isNull(callable)) {
            return null;
        }
        Lock lock = getLock(lockName, expiredTimeInSeconds);
        String threadName = Thread.currentThread().getName();
        // 获取锁
        beforeLock(threadName);
        lock.lock();
        // 处理任务
        duringLock(threadName);
        try {
            return callable.call();
        } catch (Exception e) {
            log.error("Thread={} call exception，exception：{}", threadName, e);
            throw new DistributedLockException(e);
        } finally {
            // 释放锁
            lock.unlock();
            afterUnlock(threadName);
        }
    }

    /**
     * 求阻塞锁并执行回调。
     * 无超时，慎用。锁的淘汰依赖于底层集群的实现和策略。
     *
     * @param lockName 锁名称
     * @param expiredTimeInSeconds 超时时间，单位为秒
     * @param runnable 回调闭包
     */
    @Override
    public void lockAndRun(String lockName, int expiredTimeInSeconds, Runnable runnable) {
        if (Objects.isNull(runnable)) {
            return;
        }
        Lock lock = getLock(lockName, expiredTimeInSeconds);
        String threadName = Thread.currentThread().getName();
        // 获取锁
        beforeLock(threadName);
        lock.lock();
        try {
            // 处理任务
            duringLock(threadName);
            runnable.run();
        } finally {
            // 释放锁
            lock.unlock();
            afterUnlock(threadName);
        }
    }

    /**
     * 试锁并执行回调，试锁不成功什么都不做。
     * 不需要考虑超时问题
     *
     * @param lockName 锁名称
     * @param expiredTimeInSeconds 超时时间，单位为秒
     * @param runnable 回调闭包
     */
    @Override
    public void tryLock(String lockName, int expiredTimeInSeconds, Runnable runnable) {
        tryLock(lockName, expiredTimeInSeconds, runnable, () -> {
        });
    }

    /**
     * 试锁并执行回调，试锁不成功执行回退回调。
     * 不需要考虑超时问题
     *
     * @param lockName 锁名称
     * @param expiredTimeInSeconds 超时时间，单位为秒
     * @param runnable 回调闭包
     * @param elseRunnable 回退回调闭包
     */
    @Override
    public void tryLock(String lockName, int expiredTimeInSeconds, Runnable runnable, Runnable elseRunnable) {
        if (Objects.isNull(runnable)) {
            return;
        }
        Lock lock = getLock(lockName, expiredTimeInSeconds);
        String threadName = Thread.currentThread().getName();
        // 获取锁
        beforeLock(threadName);
        if (lock.tryLock()) {
            try {
                // 处理任务
                duringLock(threadName);
                runnable.run();

            } finally {
                lock.unlock();// 释放锁
                afterUnlock(threadName);
            }
        } else {
            // 如果不能获取锁，则直接做其他事情
            elseDo(threadName);
            if (Objects.nonNull(elseRunnable)) {
                elseRunnable.run();
            }
        }
    }

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
    @Override
    public void tryLock(String lockName,
            long time,
            TimeUnit timeUnit,
            Runnable runnable,
            Runnable elseRunnable,
            int expiredTimeInSeconds) throws InterruptedException {
        if (Objects.isNull(runnable)) {
            return;
        }
        Lock lock = getLock(lockName, expiredTimeInSeconds);
        String threadName = Thread.currentThread().getName();
        // 获取锁
        beforeLock(threadName);
        if (lock.tryLock(time, timeUnit)) {
            try {
                // 处理任务
                duringLock(threadName);
                runnable.run();
            } finally {
                lock.unlock();// 释放锁
                afterUnlock(threadName);
            }
        } else {
            // 如果不能获取锁，则直接做其他事情
            elseDo(threadName);
            if (Objects.nonNull(elseRunnable)) {
                elseRunnable.run();
            }
        }
    }

    /**
     * 求可中断阻塞锁并执行回调。
     *
     * @param lockName 锁名称
     * @param expiredTimeInSeconds 超时时间，单位为秒
     * @param runnable 回调闭包
     * @throws InterruptedException 中断异常
     */
    @Override
    public void lockInterruptibly(String lockName, int expiredTimeInSeconds, Runnable runnable)
            throws InterruptedException {
        if (Objects.isNull(runnable)) {
            return;
        }
        Lock lock = getLock(lockName, expiredTimeInSeconds);
        String threadName = Thread.currentThread().getName();
        // 获取锁
        beforeLock(threadName);
        lock.lockInterruptibly();
        try {
            // 处理任务
            duringLock(threadName);
            runnable.run();
        } finally {
            // 释放锁
            lock.unlock();
            afterUnlock(threadName);
        }
    }

    /**
     * 求锁前
     *
     * @param threadName 线程名称
     */
    private void beforeLock(final String threadName) {
        log.info("Thread={} try to acquire lock", threadName);
    }

    /**
     * 在锁中
     *
     * @param threadName 线程名称
     */
    private void duringLock(final String threadName) {
        log.info("Thread={} inside-lock processing...", threadName);
    }

    /**
     * 解锁后
     *
     * @param threadName 线程名称
     */
    private void afterUnlock(final String threadName) {
        log.info("Thread={} unlocked", threadName);
    }

    /**
     * 否则执行
     *
     * @param threadName 线程名称
     */
    private void elseDo(final String threadName) {
        log.info("Thread={} do other things...", threadName);
    }
}
