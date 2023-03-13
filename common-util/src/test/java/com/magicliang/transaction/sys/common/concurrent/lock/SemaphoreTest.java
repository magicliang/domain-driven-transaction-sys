package com.magicliang.transaction.sys.common.concurrent.lock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Semaphore;

/**
 * @author liangchuan
 */
public class SemaphoreTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAsymmetricSemaphore() throws InterruptedException {
        // 和countdown latch 和 cyclic barrier 不一样，这个值是不是固定的
        final Semaphore available = new Semaphore(3, true);

        Thread t1 = new Thread(() -> {
            try {
                available.acquire(3);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                available.release(5);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();

        // 3 或者 0
        System.out.println("begin: " + available.availablePermits());
        while (available.availablePermits() <= 3) {
            // 有时候无法打出来，打出来就在0或者3之间
            System.out.println(available.availablePermits());
        }
        // 5 可以超过 permit 的值
        System.out.println("end: " + available.availablePermits());

        System.out.println("bye");
    }
}
