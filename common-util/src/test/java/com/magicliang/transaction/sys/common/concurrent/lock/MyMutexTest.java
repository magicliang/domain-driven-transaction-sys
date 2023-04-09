package com.magicliang.transaction.sys.common.concurrent.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于互斥的测试
 *
 * @author liangchuan
 */
@Slf4j
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

    /**
     * 找出全部满足 n < 8 * log(n) 的整数
     */
    @Test
    public void find122() {
        List<Integer> results = new ArrayList<>();

        Boolean begin = null;
        boolean end = false;
        int i = 0;
        while (begin == null || !end) {
            boolean satisfy = checkSatisfy1(i);
            if (satisfy) {
                results.add(i);
                begin = true;
            } else if (begin != null) {
                end = true;
            }
            ++i;
        }
        System.out.println(results.toString());
        log.info(results.toString());
    }

    private static boolean checkSatisfy1(int i) {
        return i < 8 * (Math.log(i) / Math.log(2));
    }

    /**
     * 找出所有满足100n平方小于 2 的 n次幂的数的最小值，运用反向思维，从另一个区间的最大值开始找起，这也运用了整数的间隔性
     * 跨过15（包含15）以后，100n平方大于 2 的 n次幂的运行效率
     */
    @Test
    public void find123() {
        List<Integer> results = new ArrayList<>();

        Boolean begin = null;
        boolean end = false;
        int i = 0;
        while (begin == null || !end) {
            boolean satisfy = checkSatisfy2(i);
            if (satisfy) {
                results.add(i);
                begin = true;
            } else if (begin != null) {
                end = true;
            }
            ++i;
        }
        System.out.println(results.toString());
        log.info(results.toString());
    }

    private static boolean checkSatisfy2(int i) {
        return 100 * Math.pow(i, 2) > Math.pow(2, i);
    }
}
