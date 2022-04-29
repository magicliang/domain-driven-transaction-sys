package com.magicliang.transaction.sys.common.util.apm;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;


/**
 * project name: leads_web_ut
 * <p>
 * description: 事务测试
 *
 * @author magicliang
 * <p>
 * date: 2022-04-29 14:17
 */
@Slf4j
public class TransactionTest {

    /**
     * 测试单一事务操作
     * example：
     * {
     * "type": "TransactionTest",
     * "name": "singleTransaction",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218713436,
     * "durationInMillis": 750,
     * "timestamp": 1651218712686
     * }
     */
    @Test
    public void testSingleTransactionPrint() {
        final Transaction transaction = ApmMonitor.beginTransaction("TransactionTest", "singleTransaction");
        singleTransaction();
        transaction.complete();
        Assertions.assertTrue(true);
    }

    /**
     * 测试嵌套事务操作：
     * example：
     * <p>
     * {
     * "type": "TransactionTest",
     * "name": "testSingleNestedTransactionPrint",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218748905,
     * "durationInMillis": 4291,
     * "children": [
     * {
     * "type": "TransactionTest",
     * "name": "T2",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218747214,
     * "durationInMillis": 550,
     * "timestamp": 1651218746664
     * },
     * {
     * "type": "TransactionTest",
     * "name": "T3",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218748905,
     * "durationInMillis": 1691,
     * "timestamp": 1651218747214
     * }
     * ],
     * "timestamp": 1651218744614
     * }
     */
    @Test
    public void testSingleNestedTransactionPrint() {
        final Transaction transaction = ApmMonitor.beginTransaction("TransactionTest", "testSingleNestedTransactionPrint");
        singleTransaction();
        singleNestedTransaction("TransactionTest", "T2");
        singleNestedTransaction("TransactionTest", "T3");
        transaction.complete();
        Assertions.assertTrue(true);
    }

    /**
     * 测试嵌套事务运行：
     * example：
     * <p>
     * {
     * "type": "TransactionTest",
     * "name": "testComplicatedNestedTransactionPrint",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218808222,
     * "durationInMillis": 16496,
     * "children": [
     * {
     * "type": "TransactionTest",
     * "name": "T2",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218808094,
     * "durationInMillis": 12014,
     * "children": [
     * {
     * "type": "TransactionTest",
     * "name": "T3",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218803410,
     * "durationInMillis": 7325,
     * "children": [
     * {
     * "type": "TransactionTest",
     * "name": "T4",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218799691,
     * "durationInMillis": 3606,
     * "timestamp": 1651218796085
     * }
     * ],
     * "timestamp": 1651218796085
     * }
     * ],
     * "timestamp": 1651218796080
     * },
     * {
     * "type": "TransactionTest",
     * "name": "T5",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218808222,
     * "durationInMillis": 128,
     * "timestamp": 1651218808094
     * }
     * ],
     * "timestamp": 1651218791726
     * }
     */
    @Test
    public void testComplicatedNestedTransactionPrint() {
        final Transaction transaction = ApmMonitor.beginTransaction("TransactionTest", "testComplicatedNestedTransactionPrint");
        singleTransaction();
        complicatedNestedTransaction("TransactionTest", "T2", () -> {
            complicatedNestedTransaction("TransactionTest", "T3", () -> {
                singleNestedTransaction("TransactionTest", "T4");
            });
        });
        singleNestedTransaction("TransactionTest", "T5");
        transaction.complete();
        Assertions.assertTrue(true);
    }

    /**
     * 测试嵌套循环事务：
     * <p>
     * {
     * "type": "TransactionTest",
     * "name": "testComplicatedForLoopTransactionPrint",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218949253,
     * "durationInMillis": 22407,
     * "children": [
     * {
     * "type": "TransactionTest",
     * "name": "T2",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218946821,
     * "durationInMillis": 16649,
     * "children": [
     * {
     * "type": "TransactionTest",
     * "name": "T3",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218945465,
     * "durationInMillis": 15288,
     * "children": [
     * {
     * "type": "TransactionTest",
     * "name": "T4",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218931649,
     * "durationInMillis": 1472,
     * "timestamp": 1651218930177
     * },
     * {
     * "type": "TransactionTest",
     * "name": "T-Loop-0",
     * "completed": true,
     * "data": "loop=0",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218934158,
     * "durationInMillis": 2509,
     * "timestamp": 1651218931649
     * },
     * {
     * "type": "TransactionTest",
     * "name": "T-Loop-1",
     * "completed": true,
     * "data": "loop=1",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218937454,
     * "durationInMillis": 3296,
     * "timestamp": 1651218934158
     * },
     * {
     * "type": "TransactionTest",
     * "name": "T-Loop-2",
     * "completed": true,
     * "data": "loop=2",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218937879,
     * "durationInMillis": 425,
     * "timestamp": 1651218937454
     * },
     * {
     * "type": "TransactionTest",
     * "name": "T-Loop-3",
     * "completed": true,
     * "data": "loop=3",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218941765,
     * "durationInMillis": 3886,
     * "timestamp": 1651218937879
     * },
     * {
     * "type": "TransactionTest",
     * "name": "T-Loop-4",
     * "completed": true,
     * "data": "loop=4",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218942520,
     * "durationInMillis": 755,
     * "timestamp": 1651218941765
     * }
     * ],
     * "timestamp": 1651218930177
     * }
     * ],
     * "timestamp": 1651218930172
     * },
     * {
     * "type": "TransactionTest",
     * "name": "T5",
     * "completed": true,
     * "data": "",
     * "threadName": "main",
     * "endTimestampInMillis": 1651218949253,
     * "durationInMillis": 2432,
     * "timestamp": 1651218946821
     * }
     * ],
     * "timestamp": 1651218926846
     * }
     */
    @Test
    public void testComplicatedForLoopTransactionPrint() {
        final Transaction transaction = ApmMonitor.beginTransaction("TransactionTest", "testComplicatedForLoopTransactionPrint");
        singleTransaction();
        int i = 0;
        complicatedNestedTransaction("TransactionTest", "T2", () -> {
            complicatedNestedTransaction("TransactionTest", "T3", () -> {
                singleNestedTransaction("TransactionTest", "T4");
                for (int j = 0; j < 5; j++) {
                    final Transaction nestedTx = ApmMonitor.beginTransaction("TransactionTest", "T-Loop-" + j);
                    slowOp(getRandomSleepTime());
                    nestedTx.addData("loop=" + j);
                    nestedTx.complete();
                }
            });
        });
        singleNestedTransaction("TransactionTest", "T5");
        transaction.complete();
        Assertions.assertTrue(true);
    }

    /**
     * 测试多线程下的多个事务：
     * <p>
     * {
     * "type": "TransactionTest",
     * "name": "T1",
     * "completed": true,
     * "data": "",
     * "threadName": "fooThread",
     * "endTimestampInMillis": 1651219022867,
     * "durationInMillis": 7709,
     * "children": [
     * {
     * "type": "TransactionTest",
     * "name": "T2",
     * "completed": true,
     * "data": "",
     * "threadName": "fooThread",
     * "endTimestampInMillis": 1651219019191,
     * "durationInMillis": 4032,
     * "timestamp": 1651219015159
     * }
     * ],
     * "timestamp": 1651219015158
     * }
     * <p>
     * {
     * "type": "TransactionTest",
     * "name": "T3",
     * "completed": true,
     * "data": "",
     * "threadName": "barThread",
     * "endTimestampInMillis": 1651219030628,
     * "durationInMillis": 15470,
     * "children": [
     * {
     * "type": "TransactionTest",
     * "name": "T4",
     * "completed": true,
     * "data": "",
     * "threadName": "barThread",
     * "endTimestampInMillis": 1651219026729,
     * "durationInMillis": 11570,
     * "children": [
     * {
     * "type": "TransactionTest",
     * "name": "T5",
     * "completed": true,
     * "data": "",
     * "threadName": "barThread",
     * "endTimestampInMillis": 1651219024018,
     * "durationInMillis": 8854,
     * "children": [
     * {
     * "type": "TransactionTest",
     * "name": "T6",
     * "completed": true,
     * "data": "",
     * "threadName": "barThread",
     * "endTimestampInMillis": 1651219019208,
     * "durationInMillis": 4044,
     * "timestamp": 1651219015164
     * }
     * ],
     * "timestamp": 1651219015164
     * }
     * ],
     * "timestamp": 1651219015159
     * }
     * ],
     * "timestamp": 1651219015158
     * }
     */
    @Test
    public void testMultiThreadTransaction() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread t1 = new Thread(() -> {
            complicatedNestedTransaction("TransactionTest", "T1", () -> {
                singleNestedTransaction("TransactionTest", "T2");
            });
            countDownLatch.countDown();
        });

        Thread t2 = new Thread(() -> {
            complicatedNestedTransaction("TransactionTest", "T3", () -> {
                complicatedNestedTransaction("TransactionTest", "T4", () -> {
                    complicatedNestedTransaction("TransactionTest", "T5", () -> {
                        singleNestedTransaction("TransactionTest", "T6");
                    });
                });
            });
            countDownLatch.countDown();

        });
        t1.setName("fooThread");
        t2.setName("barThread");
        t1.start();
        t2.start();
        countDownLatch.await();
        Assertions.assertTrue(true);
    }

    /**
     * 单一事务操作
     */
    private void singleTransaction() {
        slowOp(getRandomSleepTime());
    }

    /**
     * 单一嵌套事务操作
     */
    private void singleNestedTransaction(final String type, final String name) {
        final Transaction transaction = ApmMonitor.beginTransaction(type, name);
        slowOp(getRandomSleepTime());
        transaction.complete();
    }

    /**
     * 复杂嵌套事务操作
     */
    private void complicatedNestedTransaction(final String type, final String name, Runnable runnable) {
        final Transaction transaction = ApmMonitor.beginTransaction(type, name);
        runnable.run();
        slowOp(getRandomSleepTime());
        transaction.complete();
    }

    /**
     * 一个模拟慢操作
     *
     * @param millis 满操作时间
     */
    private void slowOp(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("慢操作失败：", e);
        }
    }

    /**
     * 获取随机睡眠时间
     */
    private long getRandomSleepTime() {
        return ThreadLocalRandom.current().nextLong(0, 5000L);
    }
}

