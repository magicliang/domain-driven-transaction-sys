package com.magicliang.transaction.sys.common;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2022-05-07 16:47
 */
@Slf4j
public class ConcurrentTest extends UnitTest {

    @Test
    public void testBurstSubmit() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        ExecutorService AD_SERVICE_POOL = new ThreadPoolExecutor(
                2,
                80,
                20,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName(Joiner.on("_").join("ad_service_pool", "thread", counter.getAndIncrement(), "priority", Thread.NORM_PRIORITY));
                    thread.setPriority(Thread.NORM_PRIORITY);
                    return thread;
                },
                (r, executor) -> {
                    log.error("{}_thread_full:queue_size={}, ActiveCount={}, CorePoolSize={}, CompletedTaskCount={}",
                            executor.getQueue().size(), executor.getActiveCount(), executor.getCorePoolSize(), executor.getCompletedTaskCount());
                    // 如果没有这一行，这个线程池就是有bug的，一单线程池满了，future task 会无限等待，见：https://stackoverflow.com/questions/31761012/how-to-handle-rejection-so-that-future-get-does-not-forever
                    if (r instanceof FutureTask) {
                        ((FutureTask) r).cancel(false);
                    }
                }
        );
        int count = 100;
        Semaphore semaphore = new Semaphore(5);
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final int j = i;
            tasks.add((Callable) () -> {
                try {
                    semaphore.acquire();
                } catch (InterruptedException ex) {
                    log.error("LeadsDistributeSettingController.setLeadsPoolName，semaphore.acquire() interrupted, just return, ", ex);
                    return false;
                }
                try {
                    log.info(Thread.currentThread().getName() + "：" + j);
                } finally {
                    semaphore.release();
                    return j;
                }
            });
        }
        // invokeAll 可以代替 countDownLatch 用
        AD_SERVICE_POOL.invokeAll(tasks);
        // 如果不关闭这个线程池则主进程无法退出
        AD_SERVICE_POOL.shutdownNow();
        Assertions.assertTrue(true);
    }
}
