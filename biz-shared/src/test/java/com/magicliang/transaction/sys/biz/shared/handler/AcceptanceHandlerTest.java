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

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2022-12-30 16:07
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
        Assertions.assertTrue(ReflectionUtil.isGenericSubClass(AcceptanceHandler.class, BaseHandler.class, AcceptanceCommand.class));
        Assertions.assertTrue(ReflectionUtil.isGenericSubClass(AcceptanceHandler.class, BaseHandler.class, TransactionModel.class));
        Assertions.assertFalse(ReflectionUtil.isGenericSubClass(AcceptanceHandler.class, BaseHandler.class, CallbackCommand.class));
    }

    @Test
    public void testIsParameterized() {
        Assertions.assertTrue(ReflectionUtil.isParameterized(AcceptanceHandler.class, BaseHandler.class, AcceptanceCommand.class));
        Assertions.assertFalse(ReflectionUtil.isParameterized(AcceptanceHandler.class, BaseHandler.class, CallbackCommand.class));
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
}
