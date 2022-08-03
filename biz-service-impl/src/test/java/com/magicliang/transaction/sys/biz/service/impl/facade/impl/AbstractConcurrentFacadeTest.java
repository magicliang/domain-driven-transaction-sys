package com.magicliang.transaction.sys.biz.service.impl.facade.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2022-08-03 16:17
 */
@Slf4j
class AbstractConcurrentFacadeTest {


    /**
     * 测试并发时 ForkJoin 线程池的核心线程是不是会被关闭
     *
     * @param args 命令行参数
     */
    @Test
    public void testParallelStream() {
        IntStream.range(0, 100).parallel().forEach((i) -> {
            System.out.println("hello: " + i);
            try {
                // 模拟长操作
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                log.error("testParallelStream error", e);
            }
            log.info("Byebye: " + i);
        });
        try {
            // 让子线程跑一阵，模拟两类线程共存的场景
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            log.error("testParallelStream error", e);
        }
        log.info("Byebye main thread");
    }
}