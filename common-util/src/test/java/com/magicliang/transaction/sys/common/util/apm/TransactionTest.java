package com.magicliang.transaction.sys.common.util.apm;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;


/**
 * project name: domain-driven-transaction-sys
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
     * <pre>
     * {
     *   "type": "TransactionTest",
     *   "name": "singleTransaction",
     *   "threadName": "main",
     *   "completed": true,
     *   "data": "",
     *   "endTimestampInMillis": 1651742155133,
     *   "durationInMillis": 807,
     *   "timestamp": 1651742154326
     * }
     * </pre>
     */
    @Test
    public void testSingleTransactionPrint() {
        final Transaction transaction = ApmMonitor.beginAutoTransaction("TransactionTest", "singleTransaction");
        singleTransaction();
        transaction.complete();
        Assertions.assertTrue(true);
    }

    /**
     * 测试嵌套事务操作：
     * example：
     * <pre>
     * {
     *   "type": "TransactionTest",
     *   "name": "testSingleNestedTransactionPrint",
     *   "threadName": "main",
     *   "completed": true,
     *   "data": "",
     *   "endTimestampInMillis": 1651742256646,
     *   "durationInMillis": 9616,
     *   "children": [
     *     {
     *       "type": "TransactionTest",
     *       "name": "T2",
     *       "threadName": "main",
     *       "completed": true,
     *       "data": "",
     *       "endTimestampInMillis": 1651742252051,
     *       "durationInMillis": 4061,
     *       "timestamp": 1651742247990
     *     },
     *     {
     *       "type": "TransactionTest",
     *       "name": "T3",
     *       "threadName": "main",
     *       "completed": true,
     *       "data": "",
     *       "endTimestampInMillis": 1651742256604,
     *       "durationInMillis": 4553,
     *       "timestamp": 1651742252051
     *     }
     *   ],
     *   "timestamp": 1651742247030
     * }
     * </pre>
     */
    @Test
    public void testSingleNestedTransactionPrint() {
        final Transaction transaction = ApmMonitor.beginAutoTransaction("TransactionTest", "testSingleNestedTransactionPrint");
        singleTransaction();
        singleNestedTransaction("TransactionTest", "T2");
        singleNestedTransaction("TransactionTest", "T3");
        transaction.complete();
        Assertions.assertTrue(true);
    }

    /**
     * 测试嵌套事务运行：
     * example：
     * <pre>
     * {
     *   "type": "TransactionTest",
     *   "name": "testComplicatedNestedTransactionPrint",
     *   "threadName": "main",
     *   "completed": true,
     *   "data": "",
     *   "endTimestampInMillis": 1651742339875,
     *   "durationInMillis": 18020,
     *   "children": [
     *     {
     *       "type": "TransactionTest",
     *       "name": "T2",
     *       "threadName": "main",
     *       "completed": true,
     *       "data": "",
     *       "endTimestampInMillis": 1651742335433,
     *       "durationInMillis": 11005,
     *       "children": [
     *         {
     *           "type": "TransactionTest",
     *           "name": "T3",
     *           "threadName": "main",
     *           "completed": true,
     *           "data": "",
     *           "endTimestampInMillis": 1651742331875,
     *           "durationInMillis": 7442,
     *           "children": [
     *             {
     *               "type": "TransactionTest",
     *               "name": "T4",
     *               "threadName": "main",
     *               "completed": true,
     *               "data": "",
     *               "endTimestampInMillis": 1651742328163,
     *               "durationInMillis": 3730,
     *               "timestamp": 1651742324433
     *             }
     *           ],
     *           "timestamp": 1651742324433
     *         }
     *       ],
     *       "timestamp": 1651742324428
     *     },
     *     {
     *       "type": "TransactionTest",
     *       "name": "T5",
     *       "threadName": "main",
     *       "completed": true,
     *       "data": "",
     *       "endTimestampInMillis": 1651742339875,
     *       "durationInMillis": 4442,
     *       "timestamp": 1651742335433
     *     }
     *   ],
     *   "timestamp": 1651742321855
     * }
     * </pre>
     */
    @Test
    public void testComplicatedNestedTransactionPrint() {
        final Transaction transaction = ApmMonitor.beginAutoTransaction("TransactionTest", "testComplicatedNestedTransactionPrint");
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
     * <pre>
     * {
     *   "type": "TransactionTest",
     *   "name": "testComplicatedForLoopTransactionPrint",
     *   "threadName": "main",
     *   "completed": true,
     *   "data": "",
     *   "endTimestampInMillis": 1651742800461,
     *   "durationInMillis": 22643,
     *   "children": [
     *     {
     *       "type": "TransactionTest",
     *       "name": "T2",
     *       "threadName": "main",
     *       "completed": true,
     *       "data": "",
     *       "endTimestampInMillis": 1651742797774,
     *       "durationInMillis": 14967,
     *       "children": [
     *         {
     *           "type": "TransactionTest",
     *           "name": "T3",
     *           "threadName": "main",
     *           "completed": true,
     *           "data": "",
     *           "endTimestampInMillis": 1651742795136,
     *           "durationInMillis": 12325,
     *           "children": [
     *             {
     *               "type": "TransactionTest",
     *               "name": "T4",
     *               "threadName": "main",
     *               "completed": true,
     *               "data": "",
     *               "endTimestampInMillis": 1651742783295,
     *               "durationInMillis": 484,
     *               "timestamp": 1651742782811
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-0",
     *               "threadName": "main",
     *               "completed": true,
     *               "data": "loop=0",
     *               "endTimestampInMillis": 1651742785512,
     *               "durationInMillis": 2216,
     *               "timestamp": 1651742783296
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-1",
     *               "threadName": "main",
     *               "completed": true,
     *               "data": "loop=1",
     *               "endTimestampInMillis": 1651742790103,
     *               "durationInMillis": 4591,
     *               "timestamp": 1651742785512
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-2",
     *               "threadName": "main",
     *               "completed": true,
     *               "data": "loop=2",
     *               "endTimestampInMillis": 1651742790841,
     *               "durationInMillis": 738,
     *               "timestamp": 1651742790103
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-3",
     *               "threadName": "main",
     *               "completed": true,
     *               "data": "loop=3",
     *               "endTimestampInMillis": 1651742791882,
     *               "durationInMillis": 1041,
     *               "timestamp": 1651742790841
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-4",
     *               "threadName": "main",
     *               "completed": true,
     *               "data": "loop=4",
     *               "endTimestampInMillis": 1651742794216,
     *               "durationInMillis": 2334,
     *               "timestamp": 1651742791882
     *             }
     *           ],
     *           "timestamp": 1651742782811
     *         }
     *       ],
     *       "timestamp": 1651742782807
     *     },
     *     {
     *       "type": "TransactionTest",
     *       "name": "T5",
     *       "threadName": "main",
     *       "completed": true,
     *       "data": "",
     *       "endTimestampInMillis": 1651742800461,
     *       "durationInMillis": 2687,
     *       "timestamp": 1651742797774
     *     }
     *   ],
     *   "timestamp": 1651742777818
     * }
     * </pre>
     */
    @Test
    public void testComplicatedForLoopTransactionPrint() {
        final Transaction transaction = ApmMonitor.beginAutoTransaction("TransactionTest", "testComplicatedForLoopTransactionPrint");
        singleTransaction();
        int i = 0;
        complicatedNestedTransaction("TransactionTest", "T2", () -> {
            complicatedNestedTransaction("TransactionTest", "T3", () -> {
                singleNestedTransaction("TransactionTest", "T4");
                for (int j = 0; j < 5; j++) {
                    final Transaction nestedTx = ApmMonitor.beginAutoTransaction("TransactionTest", "T-Loop-" + j);
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
     * <pre>
     * {
     *   "type": "TransactionTest",
     *   "name": "testComplicatedForLoopTransactionPrint",
     *   "threadName": "main",
     *   "completed": true,
     *   "data": "",
     *   "endTimestampInMillis": 1651742536384,
     *   "durationInMillis": 21706,
     *   "children": [
     *     {
     *       "type": "TransactionTest",
     *       "name": "T2",
     *       "threadName": "main",
     *       "completed": true,
     *       "data": "",
     *       "endTimestampInMillis": 1651742535225,
     *       "durationInMillis": 17716,
     *       "children": [
     *         {
     *           "type": "TransactionTest",
     *           "name": "T3",
     *           "threadName": "main",
     *           "completed": true,
     *           "data": "",
     *           "endTimestampInMillis": 1651742533644,
     *           "durationInMillis": 16130,
     *           "children": [
     *             {
     *               "type": "TransactionTest",
     *               "name": "T4",
     *               "threadName": "main",
     *               "completed": true,
     *               "data": "",
     *               "endTimestampInMillis": 1651742518067,
     *               "durationInMillis": 553,
     *               "timestamp": 1651742517514
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-0",
     *               "threadName": "main",
     *               "completed": true,
     *               "data": "loop=0",
     *               "endTimestampInMillis": 1651742518069,
     *               "durationInMillis": 2,
     *               "timestamp": 1651742518067
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-1",
     *               "threadName": "main",
     *               "completed": true,
     *               "data": "loop=1",
     *               "endTimestampInMillis": 1651742519539,
     *               "durationInMillis": 1470,
     *               "timestamp": 1651742518069
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-2",
     *               "threadName": "main",
     *               "completed": true,
     *               "data": "loop=2",
     *               "endTimestampInMillis": 1651742520835,
     *               "durationInMillis": 1296,
     *               "timestamp": 1651742519539
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-3",
     *               "threadName": "main",
     *               "completed": true,
     *               "data": "loop=3",
     *               "endTimestampInMillis": 1651742524837,
     *               "durationInMillis": 4002,
     *               "timestamp": 1651742520835
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-4",
     *               "threadName": "main",
     *               "completed": true,
     *               "data": "loop=4",
     *               "endTimestampInMillis": 1651742529811,
     *               "durationInMillis": 4974,
     *               "timestamp": 1651742524837
     *             }
     *           ],
     *           "timestamp": 1651742517514
     *         }
     *       ],
     *       "timestamp": 1651742517509
     *     },
     *     {
     *       "type": "TransactionTest",
     *       "name": "T5",
     *       "threadName": "main",
     *       "completed": true,
     *       "data": "",
     *       "endTimestampInMillis": 1651742536384,
     *       "durationInMillis": 1159,
     *       "timestamp": 1651742535225
     *     }
     *   ],
     *   "timestamp": 1651742514678
     * }
     * </pre>
     */
    @Test
    public void testMultiThreadTransactionPrint() throws InterruptedException {
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
     * 测试在线程池里使用一个线程交替执行任务
     * <pre>
     * {
     *   "type": "TransactionTest",
     *   "name": "T1",
     *   "threadName": "pool-1-thread-1",
     *   "completed": true,
     *   "data": "",
     *   "endTimestampInMillis": 1651742657321,
     *   "durationInMillis": 6505,
     *   "children": [
     *     {
     *       "type": "TransactionTest",
     *       "name": "T2",
     *       "threadName": "pool-1-thread-1",
     *       "completed": true,
     *       "data": "",
     *       "endTimestampInMillis": 1651742653066,
     *       "durationInMillis": 2250,
     *       "timestamp": 1651742650816
     *     }
     *   ],
     *   "timestamp": 1651742650816
     * }
     * </pre>
     * <pre>
     * {
     *   "type": "TransactionTest",
     *   "name": "T3",
     *   "threadName": "pool-1-thread-1",
     *   "completed": true,
     *   "data": "",
     *   "endTimestampInMillis": 1651742666675,
     *   "durationInMillis": 9149,
     *   "children": [
     *     {
     *       "type": "TransactionTest",
     *       "name": "T4",
     *       "threadName": "pool-1-thread-1",
     *       "completed": true,
     *       "data": "",
     *       "endTimestampInMillis": 1651742666602,
     *       "durationInMillis": 9076,
     *       "children": [
     *         {
     *           "type": "TransactionTest",
     *           "name": "T5",
     *           "threadName": "pool-1-thread-1",
     *           "completed": true,
     *           "data": "",
     *           "endTimestampInMillis": 1651742665613,
     *           "durationInMillis": 8086,
     *           "children": [
     *             {
     *               "type": "TransactionTest",
     *               "name": "T6",
     *               "threadName": "pool-1-thread-1",
     *               "completed": true,
     *               "data": "",
     *               "endTimestampInMillis": 1651742661661,
     *               "durationInMillis": 4134,
     *               "timestamp": 1651742657527
     *             }
     *           ],
     *           "timestamp": 1651742657527
     *         }
     *       ],
     *       "timestamp": 1651742657526
     *     }
     *   ],
     *   "timestamp": 1651742657526
     * }
     * </pre>
     */
    @Test
    public void testTransactionsInSameExecutor() throws InterruptedException {
        // pool-1-thread-1
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        CountDownLatch countDownLatch = new CountDownLatch(2);
        executor.submit(() -> {
            try {
                complicatedNestedTransaction("TransactionTest", "T1", () -> {
                    singleNestedTransaction("TransactionTest", "T2");
                });
            } finally {
                countDownLatch.countDown();
            }
        });

        executor.submit(() -> {
            complicatedNestedTransaction("TransactionTest", "T3", () -> {
                complicatedNestedTransaction("TransactionTest", "T4", () -> {
                    complicatedNestedTransaction("TransactionTest", "T5", () -> {
                        singleNestedTransaction("TransactionTest", "T6");
                    });
                });
            });
            try {
                countDownLatch.countDown();
            } finally {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
        Assertions.assertTrue(true);
    }

    /**
     * 验证每个 Transaction 的时间合法性
     * <pre>
     * </pre>
     * <pre>
     * </pre>
     */
    @Test
    public void verifyEveryTransactionTime() throws InterruptedException {
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        CountDownLatch countDownLatch = new CountDownLatch(2);
        executor.submit(() -> {
            // 使用非自动提交模式，生成一段嵌套事务
            final Transaction transaction = complicatedNestedManualTransaction("TransactionTest", "1", () -> {
                complicatedNestedManualTransaction("TransactionTest", "2", () -> {
                    singleManualTransaction("TransactionTest", "3");
                    singleManualTransaction("TransactionTest", "4");
                });
            });
            // 取出清空上下文后的 log
            final String monitorLog = transaction.getFinalLog();
            log.info("deserialized transaction: {}", monitorLog);
            verifyTransaction(transaction);
            countDownLatch.countDown();
            log.info("countDown end");
        });
        executor.submit(() -> {
            final Transaction transaction = complicatedNestedManualTransaction("TransactionTest", "5", () -> singleManualTransaction("TransactionTest", "6"));
            // 取出清空上下文后的 log
            final String monitorLog = transaction.getFinalLog();
            log.info("deserialized transaction: {}", monitorLog);
            verifyTransaction(transaction);
            countDownLatch.countDown();
            log.info("countDown end");
        });
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
     *
     * @param type 事务类型
     * @param name 食物名称
     */
    private void singleNestedTransaction(final String type, final String name) {
        final Transaction transaction = ApmMonitor.beginAutoTransaction(type, name);
        slowOp(getRandomSleepTime());
        transaction.complete();
    }

    /**
     * 单一手动事务操作
     *
     * @param type 事务类型
     * @param name 食物名称
     * @return 生成的事务
     */
    private Transaction singleManualTransaction(final String type, final String name) {
        final Transaction transaction = ApmMonitor.beginManualTransaction(type, name);
        slowOp(getRandomSleepTime());
        transaction.complete();
        return transaction;
    }

    /**
     * 复杂嵌套事务操作
     *
     * @param type     事务类型
     * @param name     食物名称
     * @param runnable 待运行的事务
     */
    private void complicatedNestedTransaction(final String type, final String name, Runnable runnable) {
        final Transaction transaction = ApmMonitor.beginAutoTransaction(type, name);
        // 先执行子事务
        runnable.run();
        // 再执行本事务
        slowOp(getRandomSleepTime());
        transaction.complete();
    }


    /**
     * 复杂嵌套手动事务操作
     *
     * @param type     事务类型
     * @param name     食物名称
     * @param runnable 待运行的事务
     * @return 生成的事务
     */
    private Transaction complicatedNestedManualTransaction(final String type, final String name, Runnable runnable) {
        final Transaction transaction = ApmMonitor.beginManualTransaction(type, name);
        // 先执行子事务
        runnable.run();
        // 再执行本事务
        slowOp(getRandomSleepTime());
        transaction.complete();
        return transaction;
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

    /**
     * 校验单个事务内部是否完整
     * 不要考虑把这个 util 放到非测试环节里去，因为那样做 assert 就不精准了
     *
     * @param transaction 单个事务
     */
    private void verifyTransaction(final Transaction transaction) {
        // 1. 做本事务级校验
        Assertions.assertNotNull(transaction);

        // 本事务必须已完成
        Assertions.assertTrue(transaction.isCompleted());

        final long timestamp = transaction.getTimestamp();
        final long endTimestampInMillis = transaction.getEndTimestampInMillis();
        final long durationInMillis = transaction.getDurationInMillis();

        // 本事务的起始时间必须小于等于中止时间
        Assertions.assertTrue(timestamp <= endTimestampInMillis);
        // 本事务的经过时间必须等于时间戳的差值
        Assertions.assertEquals(durationInMillis, endTimestampInMillis - timestamp);

        // 2. 对孩子列表做列表级校验
        final List<Message> children = transaction.getChildren();
        if (CollectionUtils.isNotEmpty(children)) {
            // 2.1 做父子级约束校验

            // 本事务的时间戳必须小于等于第一个的孩子的时间戳
            Message firstChild = getFirstChild(children);
            if (null != firstChild) {
                Assertions.assertTrue(timestamp <= firstChild.getTimestamp());
            }

            // 获取最后一个事务
            Transaction lastChild = getLastTransaction(children);
            if (null != lastChild) {
                // 本事务的结束时间戳必须大于等于最后一个事务的终结时间戳
                Assertions.assertTrue(endTimestampInMillis >= lastChild.getEndTimestampInMillis());
            }

            // 做逐孩子校验
            children.stream()
                    .filter(Transaction.class::isInstance)
                    .map(Transaction.class::cast)
                    .forEach(this::verifyTransaction);
            // 目前不用做兄弟级约束校验，因为兄弟内部会被排序一遍
        }
    }

    /**
     * 获取第一个孩子：起始时间最小的孩子是第一个孩子
     *
     * @param children 孩子列表
     * @return 第一个孩子
     */
    private Message getFirstChild(final List<Message> children) {
        return children.stream()
                .min(Comparator.comparing(Message::getTimestamp))
                .orElse(null);
    }

    /**
     * 获取最后一个孩子：终结时间最大的孩子是第一个孩子
     *
     * @param children 孩子列表
     * @return 第一个孩子
     */
    private Transaction getLastTransaction(final List<Message> children) {
        return children.stream()
                .filter(Transaction.class::isInstance)
                .map(Transaction.class::cast)
                .max(Comparator.comparing(Transaction::getEndTimestampInMillis))
                .orElse(null);
    }
}
