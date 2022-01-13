package com.magicliang.transaction.sys.biz.service.impl.facade.impl;

import com.magicliang.transaction.sys.common.constant.TimeConstant;
import com.magicliang.transaction.sys.common.exception.BaseTransException;
import com.magicliang.transaction.sys.core.model.context.TransactionModel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.BATCH_PAYMENT_JOB_ERROR;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 带有并发功能的门面
 *
 * @author magicliang
 * <p>
 * date: 2022-01-05 16:52
 */
@Slf4j
public abstract class AbstractConcurrentFacade extends AbstractFacade {

    /**
     * 补充时间，先设置为 5 分钟
     */
    private static final int PADDING_SECONDS = TimeConstant.ONE_MINUTE_IN_SECOND * 5;

    /**
     * 执行所有任务
     *
     * @param tasks 所有任务
     */
    protected void invokeAll(final List<Callable<TransactionModel>> tasks) {
        try {
            // 同步转异步
            // TODO：在线程池的容量不够的时候要能调高线程池的线程大小
            List<Future<TransactionModel>> results = getExecutorService().invokeAll(tasks);
            int successCount = 0;
            int idempotentCount = 0;
            int failureCount = 0;

            // 异步转同步
            try {
                for (Future<TransactionModel> result : results) {
                    // 等所有任务跑完以后逐一取出任务结果，此刻阻塞主线程
                    TransactionModel transactionModel = result.get();
                    if (transactionModel.isSuccess()) {
                        successCount++;
                    } else {
                        failureCount++;
                    }
                    if (transactionModel.isIdempotent()) {
                        idempotentCount++;
                    }
                }
                log.info("batch pay，total：{}，success：{}，failed：{}，idempotentCount：{}", results.size(), successCount, failureCount, idempotentCount);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                // 任一线程执行失败，抛出异常
                throw ex;
            } catch (ExecutionException e) {
                throw new BaseTransException(e, BATCH_PAYMENT_JOB_ERROR);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("Thread interrupted：{}", Thread.currentThread().getName());
        }
    }

    /**
     * 获取线程池
     *
     * @return 线程池
     */
    protected abstract ExecutorService getExecutorService();

    /**
     * 估算任务执行时间，忽略除法的零头，单位为秒
     *
     * @param threadCount      线程数
     * @param taskCount        任务数量
     * @param threadThroughPut 单线程吞吐量
     * @return 任务近似执行时间
     */
    protected long estimateElapsedSeconds(final int threadCount, final long taskCount, int threadThroughPut) {
        // 计算吞吐量，加上一部分的补充时间来防止窗口不准
        return (long) Math.ceil((double) taskCount / (threadCount * threadThroughPut)) + PADDING_SECONDS;
    }
}
