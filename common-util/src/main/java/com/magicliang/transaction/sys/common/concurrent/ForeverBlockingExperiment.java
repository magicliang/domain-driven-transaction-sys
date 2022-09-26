package com.magicliang.transaction.sys.common.concurrent;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 永久阻塞的实验
 *
 * @author magicliang
 * <p>
 * date: 2022-05-09 16:12
 */
@Slf4j
public class ForeverBlockingExperiment {

    /**
     * 一个有缺陷的线程池构造器
     *
     * @param corePoolSize      the number of threads to keep in the pool, even
     *                          if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize   the maximum number of threads to allow in the
     *                          pool
     * @param keepAliveTime     when the number of threads is greater than
     *                          the core, this is the maximum time that excess idle threads
     *                          will wait for new tasks before terminating.
     * @param unit              the time unit for the {@code keepAliveTime} argument
     * @param blockingQueueSize 拥塞队列尺寸
     * @param tag               线程标签
     * @return 有缺陷的线程池
     */
    public static ThreadPoolExecutor arrayBlockingExecutor(int corePoolSize, int maximumPoolSize
            , long keepAliveTime, TimeUnit unit, int blockingQueueSize, final String tag) {
        log.info("create thread pool with blocking array queue, "
                        + "tag : {}, corePoolSize : {}, maximumPoolSize : {}, "
                        + "keepAliveTime : {}, unit : {}, blockingQueueSize : {}",
                tag, corePoolSize, maximumPoolSize, keepAliveTime, unit, blockingQueueSize
        );
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                new ArrayBlockingQueue<>(blockingQueueSize),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName(Joiner.on("_").join(tag, "thread", Thread.NORM_PRIORITY));
                    thread.setPriority(Thread.NORM_PRIORITY);
                    return thread;
                },
                /*
                 * 这个线程池就是有bug的，一旦线程池满了，future task 会无限等待，见：https://stackoverflow.com/questions/31761012/how-to-handle-rejection-so-that-future-get-does-not-forever
                 * 要不要修这个 bug 还得仔细想一想
                 */
//                (r, executor) -> log.error(
//                        "{}_thread_full:queue_size={}, ActiveCount={}, CorePoolSize={}, CompletedTaskCount={}",
//                        executor.getQueue().size(), executor.getActiveCount(), executor.getCorePoolSize(),
//                        executor.getCompletedTaskCount()
//                new ThreadPoolExecutor.CallerRunsPolicy()
                new ThreadPoolExecutor.DiscardPolicy()
        );
    }

//    public static void main(String[] args) {
//        // 设计一个特别狭小的线程池，只有一个线程，拥塞队列极易满掉
//        ExecutorService testPool = arrayBlockingExecutor(
//                1,
//                1,
//                10,
//                TimeUnit.SECONDS,
//                1,
//                "test-pool");
//
//        // 生成一系列批量任务
//        final List<Callable<Integer>> tasks = IntStream.range(1, 10).mapToObj((i) -> {
//            return new Callable<Integer>() {
//                /**
//                 * Computes a result, or throws an exception if unable to do so.
//                 *
//                 * @return computed result
//                 * @throws Exception if unable to compute a result
//                 */
//                @Override
//                public Integer call() throws Exception {
//                    // 模仿长时间操作，防止线程一提交即立即执行完
//                    Thread.sleep(1000L);
//                    return i;
//                }
//            };
//        }).collect(Collectors.toList());
//        try {
//            testPool.invokeAll(tasks);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        // 永远打不出这个语句出来
//        log.info("this executor can run through all tasks");
//        testPool.shutdownNow();
//        // 永远打不出这个语句出来
//        log.info("good bye");
//    }
}
