package com.magicliang.transaction.sys.common.util;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @author magicliang
 */
public class FJPDaemonTest {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Main thread (should be non-daemon) ===");
        printThreadInfo("Main");

        // 启动一个普通非守护线程作为对照
        Thread normalThread = new Thread(() -> {
            printThreadInfo("Normal Worker Thread");
        }, "Normal-Worker");
        normalThread.setDaemon(false); // 显式设为非守护（默认也是）
        normalThread.start();
        normalThread.join(); // 等它跑完

        System.out.println("\n=== Testing ForkJoinPool.commonPool() ===");
        ForkJoinPool.commonPool().submit(() -> {
            printThreadInfo("CommonPool Task");
        }).get(); // 等待完成

        System.out.println("\n=== Testing Custom ForkJoinPool (created from main) ===");
        ForkJoinPool custom = new ForkJoinPool(2);
        custom.submit(() -> {
            printThreadInfo("Custom Pool Task");
        }).get();
        custom.shutdown();
        custom.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\n=== Done ===");
    }

    static void printThreadInfo(String label) {
        Thread t = Thread.currentThread();
        System.out.println(label + " -> Thread: " + t.getName() +
                " | isDaemon: " + t.isDaemon() +
                " | priority: " + t.getPriority() +
                " | group: " + (t.getThreadGroup() != null ? t.getThreadGroup().getName() : "null"));
    }
}
