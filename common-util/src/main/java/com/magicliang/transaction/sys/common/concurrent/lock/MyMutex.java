package com.magicliang.transaction.sys.common.concurrent.lock;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import lombok.extern.slf4j.Slf4j;

/**
 * 一个自定义互斥器的实例
 * FROM：https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/locks/AbstractQueuedSynchronizer
 * .html#tryAcquire-int-
 * 通常 sync 都是保内声明包内可见的，这样才能把锁语义和同步器语义分离出来。我们应该明确理解这一点：
 * lock 依托于 sync，但 sync 并不只是简单、狭义的 lock，否则也不足以拿来实现 semaphore 这类东西。
 *
 * @author liangchuan
 */
@Slf4j
class MyMutex implements Lock {

    /**
     * 如果这个类型不被 Spring 管理，则不需要面向父类型编程
     * 此时还需要专门考虑反序列化时，找到 concrete empty constructor 的问题
     */
    private final MySynchronizer sync = new MySynchronizer();

    /**
     * 理想的锁实现可以校验出死锁来，即 erroneous use，并抛出 unchecked exception
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
        // lock 和 acquire 的主要差距之一就是，lock 无参数，而 acquire 有参数。
        sync.acquire(1);
        log.info("Thread acquired lock: " + Thread.currentThread().getName());
        System.out.println("Thread acquired lock: " + Thread.currentThread().getName());
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
        sync.acquireInterruptibly(1);
        log.info("Thread acquired lock interruptibly: " + Thread.currentThread().getName());
        System.out.println("Thread acquired lock interruptibly: " + Thread.currentThread().getName());
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
        boolean b = sync.tryAcquire(1);
        log.info("Thread tried acquire lock: " + Thread.currentThread().getName());
        System.out.println("Thread tried acquire lock: " + Thread.currentThread().getName());
        return b;
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
        boolean b = sync.tryAcquireNanos(1, unit.toNanos(time));
        log.info("Thread tried acquire lock nanos: " + Thread.currentThread().getName());
        System.out.println("Thread tried acquire lock nanos: " + Thread.currentThread().getName());
        return b;
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
        sync.tryRelease(1);
        log.info("Thread tried release lock nanos: " + Thread.currentThread().getName());
        System.out.println("Thread tried release lock nanos: " + Thread.currentThread().getName());

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
        return sync.newCondition();
    }

    /**
     * 一个示范的自定义同步器类
     * 本同步器类认为，0是 released，1是 acquired。
     * 只要状态为 1，则这个同步器是被互斥持有的。即这个 mutex 本身只能进入一次，本身不是 reentrant（不然就要往信号量发展了），所以在 acquire 比较 state 的时候，只要比较为 1 即可。
     * 这个设计模式告诉我们，每个 mutex 类型的真正语义的实现，最好使用一个 utility helper class 来实现，然后让这个 concrete lock implementation delegate
     * 自己的行为到这个 helper 的内部实现上。
     * 如 ReentrantLock、Semaphore 都使用一个标准的内部自定义 Sync 来表达自己的并发控制语义。
     * <p>
     * 本类只实现了 try 系列方法，即 tryAcquire 和 tryRelease。因为它们也是 acquire 和 release 的技术底座，因为基础的 AQS 在 acquire 里面实现了 CLH queue
     * 的方法，这就可以得到一个最基本的 sync 了
     */
    private static class MySynchronizer extends AbstractQueuedSynchronizer {

        /**
         * Returns {@code true} if synchronization is held exclusively with
         * respect to the current (calling) thread.  This method is invoked
         * upon each call to a non-waiting {@link ConditionObject} method.
         * (Waiting methods instead invoke {@link #release}.)
         *
         * <p>The default implementation throws {@link
         * UnsupportedOperationException}. This method is invoked
         * internally only within {@link ConditionObject} methods, so need
         * not be defined if conditions are not used.
         *
         * @return {@code true} if synchronization is held exclusively;
         *         {@code false} otherwise
         * @throws UnsupportedOperationException if conditions are not supported
         */
        @Override
        protected boolean isHeldExclusively() {
            // 有一个隐式的假设，getExclusiveQueuedThreads不为空，则 state 必定大于0。
            return Objects.equals(getExclusiveQueuedThreads(), Thread.currentThread());
        }

        /**
         * Attempts to acquire in exclusive mode. This method should query
         * if the state of the object permits it to be acquired in the
         * exclusive mode, and if so to acquire it.
         *
         * <p>This method is always invoked by the thread performing
         * acquire.  If this method reports failure, the acquire method
         * may queue the thread, if it is not already queued, until it is
         * signalled by a release from some other thread. This can be used
         * to implement method {@link Lock#tryLock()}.
         *
         * <p>The default
         * implementation throws {@link UnsupportedOperationException}.
         *
         * @param arg the acquire argument. This value is always the one
         *         passed to an acquire method, or is the value saved on entry
         *         to a condition wait.  The value is otherwise uninterpreted
         *         and can represent anything you like.
         * @return {@code true} if successful. Upon success, this object has
         *         been acquired.
         * @throws IllegalMonitorStateException if acquiring would place this
         *         synchronizer in an illegal state. This exception must be
         *         thrown in a consistent fashion for synchronization to work
         *         correctly.
         * @throws UnsupportedOperationException if exclusive mode is not supported
         */
        @Override
        protected boolean tryAcquire(int arg) {
            assert 1 == arg;
            int state = getState();
            Thread thread = Thread.currentThread();
            if (state == 0) {
                // 如果是不允许重入的mutex，其实只有这三行就够了
                if (compareAndSetState(0, arg)) {
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
            } else if (getExclusiveOwnerThread() == thread) {
                // 其实如果是不允许重入的锁，则此处不管等于不等于都需要返回false，如果允许重入，则此处要差别对待，实现对 state 的累加
                return false;
            }

            return false;
        }

        /**
         * Attempts to set the state to reflect a release in exclusive
         * mode.
         *
         * <p>This method is always invoked by the thread performing release.
         *
         * <p>The default implementation throws
         * {@link UnsupportedOperationException}.
         *
         * @param arg the release argument. This value is always the one
         *         passed to a release method, or the current state value upon
         *         entry to a condition wait.  The value is otherwise
         *         uninterpreted and can represent anything you like.
         * @return {@code true} if this object is now in a fully released
         *         state, so that any waiting threads may attempt to acquire;
         *         and {@code false} otherwise.
         * @throws IllegalMonitorStateException if releasing would place this
         *         synchronizer in an illegal state. This exception must be
         *         thrown in a consistent fashion for synchronization to work
         *         correctly.
         * @throws UnsupportedOperationException if exclusive mode is not supported
         */
        @Override
        protected boolean tryRelease(int arg) {
            // 因为是互斥锁，所以断言 state 参数只能用 1
            assert 1 == arg;
            // 因为此处的比对只有单线程才会比对成功，所以下面就不用cas 设置来 release
            if (getExclusiveOwnerThread() != Thread.currentThread()) {
                return false;
            }
            // 如果支持空归还，则此处不应该抛出异常，要返回 true 才对
            if (getState() == 0) {
                // 解状态可以抛出 IllegalMonitorStateException
                throw new IllegalMonitorStateException();
            }
            // 覆写线程就正好反过来
            setExclusiveOwnerThread(null);
            setState(getState() - arg);
            return true;
        }

        /**
         * 给出 aqs 自己的内部类的一个实例
         * 这个实例拥有以下信息：
         * 1. 有 mode marker
         * 2. 有 status
         * 3. 有 waiter list。
         * <p>
         * 这个方法内部不再需要做一些按步骤的比对了。
         *
         * @return 条件对象接口的一个内部实现
         */
        Condition newCondition() {
            return new ConditionObject();
        }
    }
}
