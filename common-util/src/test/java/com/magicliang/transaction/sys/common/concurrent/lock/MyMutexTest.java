package com.magicliang.transaction.sys.common.concurrent.lock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liangchuan
 */
class MyMutexTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testLockProcedure() throws InterruptedException {
        Lock lock = new ReentrantLock();
//        CountDownLatch countDownLatch1 = new CountDownLatch(1);
//        Thread t1 = new Thread(() -> {
//            lock.lock();
//            try {
//                Thread.sleep(10000L);
//                System.out.println("3");
//            } catch (Exception ignored) {
//
//            } finally {
//                System.out.println("4");
//                countDownLatch1.countDown();
//            }
//        });
//        t1.setName("test-thread1");
//        t1.start();
//        lock.lock();
//        try {
//            System.out.println("1");
//            Thread.sleep(10000L);
//        } finally {
//            System.out.println("2");
//            lock.unlock();
//
//            /**
//             * 理论上上一行跑完
//             * final boolean acquireQueued(final Node node, int arg) {
//             *         boolean failed = true;
//             *         try {
//             *             boolean interrupted = false;
//             *             for (;;) {
//             *                 final Node p = node.predecessor();
//             *                 if (p == head && tryAcquire(arg)) {
//             *                     setHead(node);
//             *                     p.next = null; // help GC
//             *                     failed = false;
//             *                     // 不管对或者错，从这里 return
//             *                     return interrupted;
//             *                 }
//             *                 if (shouldParkAfterFailedAcquire(p, node) &&
//             *                     parkAndCheckInterrupt())
//             *                      // 如果中断则跑到这，不然得到锁就保持 interrupted = false，然后从上面 return
//             *                     interrupted = true;
//             *             }
//             *         } finally {
//             *             if (failed)
//             *                 cancelAcquire(node);
//             *         }
//             *     }
//             *
//             *     public final void acquire(int arg) {
//             *         if (!tryAcquire(arg) &&
//             *             acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
//             *             // 如果 acquireQueued 方法返回true，断点到这一行
//             *             selfInterrupt();
//             *
//             *             // 否则断点到这一行即不做 selfInterrupt
//             *     }
//             *
//             *   然后断点到
//             *     System.out.println("3");
//             *
//             *
//             *    private final boolean parkAndCheckInterrupt() {
//             *         LockSupport.park(this);
//             *         // 这是一个清理线程状态的 testMethod()
//             *         return Thread.interrupted();
//             *     }
//             */
//        }
//        countDownLatch1.wait();
//        Thread.sleep(10000L);


        CountDownLatch countDownLatch2 = new CountDownLatch(1);
        CountDownLatch countDownLatch3 = new CountDownLatch(1);

        Thread t2 = new Thread(() -> {
            try {
                countDownLatch3.wait();
            } catch (Exception ignored) {
                System.out.println("isInterrupted: " + Thread.currentThread().isInterrupted());
            }

            lock.lock();
            try {
                System.out.println("7");
            } finally {
                System.out.println("8");
                countDownLatch2.countDown();
                lock.unlock();
            }
        });
        t2.setName("test-thread2");
        t2.start();
        lock.lock();
        try {
            System.out.println("5");
        } finally {
            System.out.println("6");
            countDownLatch3.countDown();
            lock.unlock();
            t2.interrupt();
//            t2.interrupt();

            /**
             * 理论上上一行跑完
             * final boolean acquireQueued(final Node node, int arg) {
             *         boolean failed = true;
             *         try {
             *             boolean interrupted = false;
             *             for (;;) {
             *                 final Node p = node.predecessor();
             *                 if (p == head && tryAcquire(arg)) {
             *                     setHead(node);
             *                     p.next = null; // help GC
             *                     failed = false;
             *                     // 理论上这里会返回 true
             *                     return interrupted;
             *                 }
             *                 if (shouldParkAfterFailedAcquire(p, node) &&
             *                     parkAndCheckInterrupt())
             *                      // 如果中断则跑到这，不然得到锁就保持 interrupted = false，然后从上面 return
             *                     interrupted = true;
             *             }
             *         } finally {
             *             if (failed)
             *                 cancelAcquire(node);
             *         }
             *     }
             *
             */
        }
        countDownLatch2.wait();
        Thread.sleep(10000L);

    }


    @Test
    void testMutexProcedure() throws InterruptedException {
        MyMutex mutex = new MyMutex();
        Thread t = new Thread(() -> {
            mutex.lock();
            try {
                // 理论上 aqs 内部的
                System.out.println("3");
                Thread.sleep(10000L);
            } catch (Exception ignored) {

            } finally {
                System.out.println("4");
            }
        });
        t.setName("test-thread");
        t.start();
        mutex.lock();
        try {
            System.out.println("1");
            Thread.sleep(10000L);
        } finally {
            System.out.println("2");
            mutex.unlock();
        }
        Thread.sleep(10000L);
    }
}
