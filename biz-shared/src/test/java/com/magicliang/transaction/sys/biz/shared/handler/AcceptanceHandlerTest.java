package com.magicliang.transaction.sys.biz.shared.handler;

import com.magicliang.transaction.sys.biz.shared.request.acceptance.AcceptanceCommand;
import com.magicliang.transaction.sys.biz.shared.request.callback.CallbackCommand;
import com.magicliang.transaction.sys.common.util.ReflectionUtil;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 *         <p>
 *         date: 2022-12-30 16:07
 */
@Slf4j
public class AcceptanceHandlerTest {

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * 这个实验证明，我们可以验证多参数的继承关系
     */
    @Test
    public void testIsGenericSubClass() {
        Assertions.assertTrue(
                ReflectionUtil.isGenericSubClass(AcceptanceHandler.class, BaseHandler.class, AcceptanceCommand.class));
        Assertions.assertTrue(
                ReflectionUtil.isGenericSubClass(AcceptanceHandler.class, BaseHandler.class, TransactionModel.class));
        Assertions.assertFalse(
                ReflectionUtil.isGenericSubClass(AcceptanceHandler.class, BaseHandler.class, CallbackCommand.class));
    }

    @Test
    public void testIsParameterized() {
        Assertions.assertTrue(
                ReflectionUtil.isParameterized(AcceptanceHandler.class, BaseHandler.class, AcceptanceCommand.class));
        Assertions.assertFalse(
                ReflectionUtil.isParameterized(AcceptanceHandler.class, BaseHandler.class, CallbackCommand.class));
    }


    @Test
    public void testForkJoinPool() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        CompletableFuture<Thread> threadCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread thread = Thread.currentThread();
                // 这个log 是不能在标准输出流里打出来的，不知道为什么
//                log.info("isDaemon(): " + thread.isDaemon());
                System.out.println("isDaemon(): " + thread.isDaemon());
                return thread;
            } finally {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
        Assertions.assertTrue(threadCompletableFuture.isDone());
    }

    @Test
    public void testThreadFactoryProperties() throws InterruptedException {
        ForkJoinPool.ForkJoinWorkerThreadFactory factory = pool -> {
            ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            thread.setPriority(Thread.MAX_PRIORITY);           // 设置优先级
            // 我们不可修改这个，也不适宜修改这个
            thread.setDaemon(true);                            // 设置守护线程
            thread.setUncaughtExceptionHandler((t, e) -> {    // 设置异常处理器
                System.err.println("Thread " + t.getName() + " crashed: " + e);
            });
            return thread;
        };

        CountDownLatch countDownLatch = new CountDownLatch(1);
        CompletableFuture<Thread> threadCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread thread = Thread.currentThread();
                System.out.println("Thread priority: " + thread.getPriority());
                System.out.println("Is daemon: " + thread.isDaemon());
                return thread;
            } finally {
                countDownLatch.countDown();
            }
        }, new ForkJoinPool(1, factory, null, false));

        countDownLatch.await();
        Assertions.assertTrue(threadCompletableFuture.isDone());
    }
}
