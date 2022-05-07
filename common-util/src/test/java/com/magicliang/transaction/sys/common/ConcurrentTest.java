package com.magicliang.transaction.sys.common;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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
        ExecutorService AD_SERVICE_POOL = new ThreadPoolExecutor(
                2,
                80,
                20,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName(Joiner.on("_").join("ad_service_pool", "thread", Thread.NORM_PRIORITY));
                    thread.setPriority(Thread.NORM_PRIORITY);
                    return thread;
                },
                (r, executor) -> log.error(
                        "{}_thread_full:queue_size={}, ActiveCount={}, CorePoolSize={}, CompletedTaskCount={}",
                        executor.getQueue().size(), executor.getActiveCount(), executor.getCorePoolSize(),
                        executor.getCompletedTaskCount()
                )
        );
        int count = 100;
        List<Callable<Integer>> tasks = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            final int j = i;
            tasks.add((Callable) () -> {
                System.out.println(j);
                countDownLatch.countDown();
                return j;
            });
        }
        AD_SERVICE_POOL.invokeAll(tasks);
        // 只要这个 awaiting完成，则程序必定正确退出了，没有异常发生，证明没有什么特别的 burst 导致线程池异常
        countDownLatch.await();
        // 如果不关闭这个线程池则主进程无法退出
        AD_SERVICE_POOL.shutdownNow();
        Assertions.assertTrue(true);
    }
}
