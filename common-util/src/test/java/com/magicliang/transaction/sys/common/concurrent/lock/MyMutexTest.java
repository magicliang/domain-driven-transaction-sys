package com.magicliang.transaction.sys.common.concurrent.lock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 基于互斥的测试
 *
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
    void testMutexProcedure() throws InterruptedException {
        MyMutex mutex = new MyMutex();
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
            }
            mutex.lock();
            try {
                // 理论上 aqs 内部的
                System.out.println("3");
            } catch (Exception ignored) {

            } finally {
                System.out.println("4");
                mutex.unlock();
            }
        });
        t1.setName("test-thread1");
        t1.start();
        mutex.lock();
        try {
            System.out.println("1");
            Thread.sleep(1000L);
        } finally {
            System.out.println("2");
            mutex.unlock();
        }
        Thread.sleep(10000L);
    }
}
