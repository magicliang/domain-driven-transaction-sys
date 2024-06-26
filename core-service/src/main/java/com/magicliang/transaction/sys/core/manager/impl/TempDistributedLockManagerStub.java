package com.magicliang.transaction.sys.core.manager.impl;

import com.magicliang.transaction.sys.core.manager.IDistributedLockManager;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import org.springframework.stereotype.Component;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 以后要单独实现一个专有的分布式锁包
 *
 * @author magicliang
 *         <p>
 *         date: 2022-01-25 17:42
 */
@Component
public class TempDistributedLockManagerStub implements IDistributedLockManager {

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public Lock getReentrantLock(String lockName) {
        return new DefaultLockImpl();
    }

    @Override
    public ReadWriteLock getReentrantReadWriteLock(String lockName) {
        return new DefaultLockImpl();
    }

    @Override
    public Lock getReentrantLock(String lockName, int expireTime) {
        return new DefaultLockImpl();
    }

    @Override
    public Lock getReentrantLock(String lockName, int expireTime, int retry) {
        return new DefaultLockImpl();
    }

    @Override
    public ReadWriteLock getReentrantReadWriteLock(String lockName, int expireTime) {
        return new DefaultLockImpl();
    }

    @Override
    public ReadWriteLock getReentrantReadWriteLock(String lockName, int expireTime, int retry) {
        return new DefaultLockImpl();
    }

    @Override
    public Lock getNewMultiLock(Lock[] locks) {
        return new DefaultLockImpl();
    }

    @Override
    public Lock getNewMultiLock(String[] locks) {
        return new DefaultLockImpl();
    }

    @Override
    public Lock getNewMultiLock(String[] locks, int expireTime) {
        return new DefaultLockImpl();
    }

    @Override
    public String switchEngine() {
        return "newEngine";
    }

    @Override
    public String switchEngine(String engineName) {
        return engineName;
    }

    /**
     * 临时的锁实现
     */
    private static class DefaultLockImpl implements Lock, ReadWriteLock {

        /**
         * Acquires the lock.
         *
         * <p>If the lock is not available then the current thread becomes
         * disabled for thread scheduling purposes and lies dormant until the
         * lock has been acquired.
         *
         * <p><b>Implementation Considerations</b>
         *
         * <p>A {@code Lock} implementation may be able to detect erroneous use
         * of the lock, such as an invocation that would cause deadlock, and
         * may throw an (unchecked) exception in such circumstances.  The
         * circumstances and the exception type must be documented by that
         * {@code Lock} implementation.
         */
        @Override
        public void lock() {

        }

        /**
         * Acquires the lock unless the current thread is
         * {@linkplain Thread#interrupt interrupted}.
         *
         * <p>Acquires the lock if it is available and returns immediately.
         *
         * <p>If the lock is not available then the current thread becomes
         * disabled for thread scheduling purposes and lies dormant until
         * one of two things happens:
         *
         * <ul>
         * <li>The lock is acquired by the current thread; or
         * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
         * current thread, and interruption of lock acquisition is supported.
         * </ul>
         *
         * <p>If the current thread:
         * <ul>
         * <li>has its interrupted status set on entry to this method; or
         * <li>is {@linkplain Thread#interrupt interrupted} while acquiring the
         * lock, and interruption of lock acquisition is supported,
         * </ul>
         * then {@link InterruptedException} is thrown and the current thread's
         * interrupted status is cleared.
         *
         * <p><b>Implementation Considerations</b>
         *
         * <p>The ability to interrupt a lock acquisition in some
         * implementations may not be possible, and if possible may be an
         * expensive operation.  The programmer should be aware that this
         * may be the case. An implementation should document when this is
         * the case.
         *
         * <p>An implementation can favor responding to an interrupt over
         * normal method return.
         *
         * <p>A {@code Lock} implementation may be able to detect
         * erroneous use of the lock, such as an invocation that would
         * cause deadlock, and may throw an (unchecked) exception in such
         * circumstances.  The circumstances and the exception type must
         * be documented by that {@code Lock} implementation.
         *
         * @throws InterruptedException if the current thread is
         *         interrupted while acquiring the lock (and interruption
         *         of lock acquisition is supported)
         */
        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        /**
         * Acquires the lock only if it is free at the time of invocation.
         *
         * <p>Acquires the lock if it is available and returns immediately
         * with the value {@code true}.
         * If the lock is not available then this method will return
         * immediately with the value {@code false}.
         *
         * <p>A typical usage idiom for this method would be:
         * <pre> {@code
         * Lock lock = ...;
         * if (lock.tryLock()) {
         *   try {
         *     // manipulate protected state
         *   } finally {
         *     lock.unlock();
         *   }
         * } else {
         *   // perform alternative actions
         * }}</pre>
         * <p>
         * This usage ensures that the lock is unlocked if it was acquired, and
         * doesn't try to unlock if the lock was not acquired.
         *
         * @return {@code true} if the lock was acquired and
         *         {@code false} otherwise
         */
        @Override
        public boolean tryLock() {
            return false;
        }

        /**
         * Acquires the lock if it is free within the given waiting time and the
         * current thread has not been {@linkplain Thread#interrupt interrupted}.
         *
         * <p>If the lock is available this method returns immediately
         * with the value {@code true}.
         * If the lock is not available then
         * the current thread becomes disabled for thread scheduling
         * purposes and lies dormant until one of three things happens:
         * <ul>
         * <li>The lock is acquired by the current thread; or
         * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
         * current thread, and interruption of lock acquisition is supported; or
         * <li>The specified waiting time elapses
         * </ul>
         *
         * <p>If the lock is acquired then the value {@code true} is returned.
         *
         * <p>If the current thread:
         * <ul>
         * <li>has its interrupted status set on entry to this method; or
         * <li>is {@linkplain Thread#interrupt interrupted} while acquiring
         * the lock, and interruption of lock acquisition is supported,
         * </ul>
         * then {@link InterruptedException} is thrown and the current thread's
         * interrupted status is cleared.
         *
         * <p>If the specified waiting time elapses then the value {@code false}
         * is returned.
         * If the time is
         * less than or equal to zero, the method will not wait at all.
         *
         * <p><b>Implementation Considerations</b>
         *
         * <p>The ability to interrupt a lock acquisition in some implementations
         * may not be possible, and if possible may
         * be an expensive operation.
         * The programmer should be aware that this may be the case. An
         * implementation should document when this is the case.
         *
         * <p>An implementation can favor responding to an interrupt over normal
         * method return, or reporting a timeout.
         *
         * <p>A {@code Lock} implementation may be able to detect
         * erroneous use of the lock, such as an invocation that would cause
         * deadlock, and may throw an (unchecked) exception in such circumstances.
         * The circumstances and the exception type must be documented by that
         * {@code Lock} implementation.
         *
         * @param time the maximum time to wait for the lock
         * @param unit the time unit of the {@code time} argument
         * @return {@code true} if the lock was acquired and {@code false}
         *         if the waiting time elapsed before the lock was acquired
         * @throws InterruptedException if the current thread is interrupted
         *         while acquiring the lock (and interruption of lock
         *         acquisition is supported)
         */
        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        /**
         * Releases the lock.
         *
         * <p><b>Implementation Considerations</b>
         *
         * <p>A {@code Lock} implementation will usually impose
         * restrictions on which thread can release a lock (typically only the
         * holder of the lock can release it) and may throw
         * an (unchecked) exception if the restriction is violated.
         * Any restrictions and the exception
         * type must be documented by that {@code Lock} implementation.
         */
        @Override
        public void unlock() {

        }

        /**
         * Returns a new {@link Condition} instance that is bound to this
         * {@code Lock} instance.
         *
         * <p>Before waiting on the condition the lock must be held by the
         * current thread.
         * A call to {@link Condition#await()} will atomically release the lock
         * before waiting and re-acquire the lock before the wait returns.
         *
         * <p><b>Implementation Considerations</b>
         *
         * <p>The exact operation of the {@link Condition} instance depends on
         * the {@code Lock} implementation and must be documented by that
         * implementation.
         *
         * @return A new {@link Condition} instance for this {@code Lock} instance
         * @throws UnsupportedOperationException if this {@code Lock}
         *         implementation does not support conditions
         */
        @Override
        public Condition newCondition() {
            return null;
        }

        /**
         * Returns the lock used for reading.
         *
         * @return the lock used for reading
         */
        @Override
        public Lock readLock() {
            return null;
        }

        /**
         * Returns the lock used for writing.
         *
         * @return the lock used for writing
         */
        @Override
        public Lock writeLock() {
            return null;
        }
    }
}
