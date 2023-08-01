package com.magicliang.transaction.sys.common.util.apm;

import com.magicliang.transaction.sys.common.UnitTest;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 事务测试
 *
 * @author magicliang
 *         <p>
 *         date: 2022-04-29 14:17
 */
@Slf4j
public class TransactionTest extends UnitTest {

    /**
     * 测试单一事务操作
     * example：
     * <pre>
     * {
     *   "type": "TransactionTest",
     *   "name": "singleTransaction",
     *   "completed": true,
     *   "msgId": "29e2fac6-f069-4d00-b34e-0f136da32d00",
     *   "endTimestampInMillis": 1652363535031,
     *   "durationInMillis": 1891,
     *   "timestamp": 1652363533140
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
     *   "completed": true,
     *   "msgId": "a349ac5c-ac4f-4295-b149-54decc3ebf40",
     *   "endTimestampInMillis": 1652363591474,
     *   "durationInMillis": 7565,
     *   "children": [
     *     {
     *       "type": "TransactionTest",
     *       "name": "T2",
     *       "completed": true,
     *       "msgId": "a349ac5c-ac4f-4295-b149-54decc3ebf40",
     *       "endTimestampInMillis": 1652363588160,
     *       "durationInMillis": 2425,
     *       "timestamp": 1652363585735
     *     },
     *     {
     *       "type": "TransactionTest",
     *       "name": "T2",
     *       "completed": true,
     *       "msgId": "a349ac5c-ac4f-4295-b149-54decc3ebf40",
     *       "endTimestampInMillis": 1652363591472,
     *       "durationInMillis": 3312,
     *       "timestamp": 1652363588160
     *     }
     *   ],
     *   "timestamp": 1652363583909
     * }
     * </pre>
     */
    @Test
    public void testSingleNestedTransactionPrint() {
        final Transaction transaction = ApmMonitor.beginAutoTransaction("TransactionTest",
                "testSingleNestedTransactionPrint");
        singleTransaction();
        singleNestedTransaction("TransactionTest", "T2");
        singleNestedTransaction("TransactionTest", "T2");
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
     *   "completed": true,
     *   "msgId": "9117ac98-1202-45cc-a502-70a73f16d9fa",
     *   "endTimestampInMillis": 1652363641359,
     *   "durationInMillis": 5723,
     *   "children": [
     *     {
     *       "type": "TransactionTest",
     *       "name": "T2",
     *       "completed": true,
     *       "msgId": "9117ac98-1202-45cc-a502-70a73f16d9fa",
     *       "endTimestampInMillis": 1652363640981,
     *       "durationInMillis": 4840,
     *       "children": [
     *         {
     *           "type": "TransactionTest",
     *           "name": "T3",
     *           "completed": true,
     *           "msgId": "9117ac98-1202-45cc-a502-70a73f16d9fa",
     *           "endTimestampInMillis": 1652363640166,
     *           "durationInMillis": 4019,
     *           "children": [
     *             {
     *               "type": "TransactionTest",
     *               "name": "T4",
     *               "completed": true,
     *               "msgId": "9117ac98-1202-45cc-a502-70a73f16d9fa",
     *               "endTimestampInMillis": 1652363639439,
     *               "durationInMillis": 3292,
     *               "timestamp": 1652363636147
     *             }
     *           ],
     *           "timestamp": 1652363636147
     *         }
     *       ],
     *       "timestamp": 1652363636141
     *     },
     *     {
     *       "type": "TransactionTest",
     *       "name": "T5",
     *       "completed": true,
     *       "msgId": "9117ac98-1202-45cc-a502-70a73f16d9fa",
     *       "endTimestampInMillis": 1652363641359,
     *       "durationInMillis": 378,
     *       "timestamp": 1652363640981
     *     }
     *   ],
     *   "timestamp": 1652363635636
     * }
     * </pre>
     */
    @Test
    public void testComplicatedNestedTransactionPrint() {
        final Transaction transaction = ApmMonitor.beginAutoTransaction("TransactionTest",
                "testComplicatedNestedTransactionPrint");
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
     *   "completed": true,
     *   "msgId": "68f70d47-1610-4e57-8be1-95018a5a6c0e",
     *   "endTimestampInMillis": 1652363724336,
     *   "durationInMillis": 29811,
     *   "children": [
     *     {
     *       "type": "TransactionTest",
     *       "name": "T2",
     *       "completed": true,
     *       "msgId": "68f70d47-1610-4e57-8be1-95018a5a6c0e",
     *       "endTimestampInMillis": 1652363720436,
     *       "durationInMillis": 21031,
     *       "children": [
     *         {
     *           "type": "TransactionTest",
     *           "name": "T3",
     *           "completed": true,
     *           "msgId": "68f70d47-1610-4e57-8be1-95018a5a6c0e",
     *           "endTimestampInMillis": 1652363715556,
     *           "durationInMillis": 16146,
     *           "children": [
     *             {
     *               "type": "TransactionTest",
     *               "name": "T4",
     *               "completed": true,
     *               "msgId": "68f70d47-1610-4e57-8be1-95018a5a6c0e",
     *               "endTimestampInMillis": 1652363700470,
     *               "durationInMillis": 1060,
     *               "timestamp": 1652363699410
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-0",
     *               "completed": true,
     *               "data": "loop=0",
     *               "msgId": "68f70d47-1610-4e57-8be1-95018a5a6c0e",
     *               "endTimestampInMillis": 1652363703016,
     *               "durationInMillis": 2546,
     *               "timestamp": 1652363700470
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-1",
     *               "completed": true,
     *               "data": "loop=1",
     *               "msgId": "68f70d47-1610-4e57-8be1-95018a5a6c0e",
     *               "endTimestampInMillis": 1652363705342,
     *               "durationInMillis": 2326,
     *               "timestamp": 1652363703016
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-2",
     *               "completed": true,
     *               "data": "loop=2",
     *               "msgId": "68f70d47-1610-4e57-8be1-95018a5a6c0e",
     *               "endTimestampInMillis": 1652363707997,
     *               "durationInMillis": 2655,
     *               "timestamp": 1652363705342
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-3",
     *               "completed": true,
     *               "data": "loop=3",
     *               "msgId": "68f70d47-1610-4e57-8be1-95018a5a6c0e",
     *               "endTimestampInMillis": 1652363709547,
     *               "durationInMillis": 1550,
     *               "timestamp": 1652363707997
     *             },
     *             {
     *               "type": "TransactionTest",
     *               "name": "T-Loop-4",
     *               "completed": true,
     *               "data": "loop=4",
     *               "msgId": "68f70d47-1610-4e57-8be1-95018a5a6c0e",
     *               "endTimestampInMillis": 1652363713452,
     *               "durationInMillis": 3905,
     *               "timestamp": 1652363709547
     *             }
     *           ],
     *           "timestamp": 1652363699410
     *         }
     *       ],
     *       "timestamp": 1652363699405
     *     },
     *     {
     *       "type": "TransactionTest",
     *       "name": "T5",
     *       "completed": true,
     *       "msgId": "68f70d47-1610-4e57-8be1-95018a5a6c0e",
     *       "endTimestampInMillis": 1652363724336,
     *       "durationInMillis": 3900,
     *       "timestamp": 1652363720436
     *     }
     *   ],
     *   "timestamp": 1652363694525
     * }
     * </pre>
     */
    @Test
    public void testComplicatedForLoopTransactionPrint() {
        final Transaction transaction = ApmMonitor.beginAutoTransaction("TransactionTest",
                "testComplicatedForLoopTransactionPrint");
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
     *   "name": "T1",
     *   "completed": true,
     *   "msgId": "ebb3cfc7-5d4b-4043-8648-7bcd381056ae",
     *   "endTimestampInMillis": 1652363777364,
     *   "durationInMillis": 6990,
     *   "children": [
     *     {
     *       "type": "TransactionTest",
     *       "name": "T2",
     *       "completed": true,
     *       "msgId": "ebb3cfc7-5d4b-4043-8648-7bcd381056ae",
     *       "endTimestampInMillis": 1652363772746,
     *       "durationInMillis": 2364,
     *       "timestamp": 1652363770382
     *     }
     *   ],
     *   "timestamp": 1652363770374
     * }
     * </pre>
     * <pre>
     * {
     *   "type": "TransactionTest",
     *   "name": "T3",
     *   "completed": true,
     *   "msgId": "e2c1b50d-022d-4a84-8b05-48fe11415ed7",
     *   "endTimestampInMillis": 1652363779126,
     *   "durationInMillis": 8752,
     *   "children": [
     *     {
     *       "type": "TransactionTest",
     *       "name": "T4",
     *       "completed": true,
     *       "msgId": "e2c1b50d-022d-4a84-8b05-48fe11415ed7",
     *       "endTimestampInMillis": 1652363777175,
     *       "durationInMillis": 6792,
     *       "children": [
     *         {
     *           "type": "TransactionTest",
     *           "name": "T5",
     *           "completed": true,
     *           "msgId": "e2c1b50d-022d-4a84-8b05-48fe11415ed7",
     *           "endTimestampInMillis": 1652363773632,
     *           "durationInMillis": 3246,
     *           "children": [
     *             {
     *               "type": "TransactionTest",
     *               "name": "T6",
     *               "completed": true,
     *               "msgId": "e2c1b50d-022d-4a84-8b05-48fe11415ed7",
     *               "endTimestampInMillis": 1652363772978,
     *               "durationInMillis": 2592,
     *               "timestamp": 1652363770386
     *             }
     *           ],
     *           "timestamp": 1652363770386
     *         }
     *       ],
     *       "timestamp": 1652363770383
     *     }
     *   ],
     *   "timestamp": 1652363770374
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
     *   "completed": true,
     *   "msgId": "23e2e9d9-5fa3-4069-becf-6febb4b5603c",
     *   "endTimestampInMillis": 1652363936959,
     *   "durationInMillis": 6211,
     *   "children": [
     *     {
     *       "type": "TransactionTest",
     *       "name": "T2",
     *       "completed": true,
     *       "msgId": "23e2e9d9-5fa3-4069-becf-6febb4b5603c",
     *       "endTimestampInMillis": 1652363932543,
     *       "durationInMillis": 1785,
     *       "timestamp": 1652363930758
     *     }
     *   ],
     *   "timestamp": 1652363930748
     * }
     * </pre>
     * <pre>
     * {
     *   "type": "TransactionTest",
     *   "name": "T3",
     *   "completed": true,
     *   "msgId": "121719bf-654d-4e40-be2d-8225c72bc033",
     *   "endTimestampInMillis": 1652363948403,
     *   "durationInMillis": 11267,
     *   "children": [
     *     {
     *       "type": "TransactionTest",
     *       "name": "T4",
     *       "completed": true,
     *       "msgId": "121719bf-654d-4e40-be2d-8225c72bc033",
     *       "endTimestampInMillis": 1652363946183,
     *       "durationInMillis": 9047,
     *       "children": [
     *         {
     *           "type": "TransactionTest",
     *           "name": "T5",
     *           "completed": true,
     *           "msgId": "121719bf-654d-4e40-be2d-8225c72bc033",
     *           "endTimestampInMillis": 1652363943405,
     *           "durationInMillis": 6269,
     *           "children": [
     *             {
     *               "type": "TransactionTest",
     *               "name": "T6",
     *               "completed": true,
     *               "msgId": "121719bf-654d-4e40-be2d-8225c72bc033",
     *               "endTimestampInMillis": 1652363940097,
     *               "durationInMillis": 2961,
     *               "timestamp": 1652363937136
     *             }
     *           ],
     *           "timestamp": 1652363937136
     *         }
     *       ],
     *       "timestamp": 1652363937136
     *     }
     *   ],
     *   "timestamp": 1652363937136
     * }
     * </pre>
     */
    @Test
    public void testTransactionsInSameExecutor() throws InterruptedException {
        // pool-1-thread-1
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10));
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
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10));
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
            final Transaction transaction = complicatedNestedManualTransaction("TransactionTest", "5",
                    () -> singleManualTransaction("TransactionTest", "6"));
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
     * @param type 事务类型
     * @param name 食物名称
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
     * @param type 事务类型
     * @param name 食物名称
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
