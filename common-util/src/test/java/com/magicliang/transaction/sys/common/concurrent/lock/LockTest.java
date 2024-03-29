package com.magicliang.transaction.sys.common.concurrent.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 基于锁的测试
 *
 * @author liangchuan
 */
public class LockTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    /**
     * 这个实验的问题是，无法复现求锁的子线程从被中断状态下退出的场景
     * 是不是只有从 os 层面干预线程，才能触发这一点？
     *
     * @throws InterruptedException 中断异常
     */
    @Test
    void testLockProcedure() throws InterruptedException {
        Lock lock = new ReentrantLock();

        CountDownLatch countDownLatch1 = new CountDownLatch(1);

        Thread t1 = new Thread(() -> {
            try {
                countDownLatch1.await();
            } catch (Exception ignored) {
                System.out.println("isInterrupted: " + Thread.currentThread().isInterrupted());
            }

            // 用 while 而不是 sleep，这样就不怕 interrupt 被阻塞 api 抛出异常来响应了
            int j = 0;
            for (int i = 0; i < 100000; i++) {
                j += i;
            }
            System.out.println(j);
            // 在lock 内部可能检测不到这个中断，需要外部频繁中断为好
            Thread.currentThread().interrupt();
            lock.lock();
            try {
                System.out.println("3");
            } finally {
                System.out.println("4");
                lock.unlock();
            }
        });
        t1.setName("test-thread1");
        t1.start();
        lock.lock();
        try {
            System.out.println("1");
        } finally {
            System.out.println("2");
            countDownLatch1.countDown();
            try {
                // 休眠一段时间，让t2开始求锁，然后再解锁
                Thread.sleep(1000L);
            } catch (Exception e) {

            }
            // 中断一千次
            for (int i = 0; i < 1000; i++) {
                t1.interrupt();
            }
            Thread.sleep(10000L);
            lock.unlock();
//            t2.interrupt();

            /**
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
             *                     // 不管是否中断，都从这里 return
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
             *     public final void acquire(int arg) {
             *         if (!tryAcquire(arg) &&
             *             acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
             *             // 如果 acquireQueued 方法返回true，断点到这一行
             *             // 如果 while interrupt 循环足够多，是可以从这一步跑出去的
             *             selfInterrupt();
             *
             *             // 否则断点到这一行即不做 selfInterrupt
             *     }
             *
             *    private final boolean parkAndCheckInterrupt() {
             *         LockSupport.park(this);
             *         // 这是一个清理线程状态的 testMethod()
             *         return Thread.interrupted();
             *     }
             *
             *
             */
        }
        t1.join();
        Thread.sleep(10000L);

    }
}
