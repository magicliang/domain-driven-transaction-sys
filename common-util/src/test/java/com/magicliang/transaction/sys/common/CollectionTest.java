package com.magicliang.transaction.sys.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 这个类验证 JVM 的行为
 *
 * @author liangchuan
 */
public class CollectionTest {

    private static final int LOOP = 30000000;
    private static final int CAPACITY = 64;

    /**
     * -Xmx5500m -Xms4096m -Xmn2048m -server -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m  -XX:+CMSClassUnloadingEnabled -XX:+PrintGCDetails -Xloggc:./gc-%p-%t.log -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./log/hprof/ -XX:ErrorFile=./log/jvm_error.log -XX:+UseConcMarkSweepGC -XX:+PrintSafepointStatistics -XX:PrintSafepointStatisticsCount=1 -XX:-OmitStackTraceInFastThrow -XX:+UseCMSCompactAtFullCollection -XX:-UseBiasedLocking -XX:-UseCounterDecay -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+ParallelRefProcEnabled -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+CMSParallelRemarkEnabled -XX:+ScavengeBeforeFullGC -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSScavengeBeforeRemark
     */
    public static void main(String[] args) {
//        testWithCapacity();
        testWithoutCapacity();
    }

    /**
     * -Xmx5500m -Xms4096m -Xmn2048m -server -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m  -XX:+CMSClassUnloadingEnabled -XX:+PrintGCDetails -Xloggc:./gc-%p-%t.log -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./log/hprof/ -XX:ErrorFile=./log/jvm_error.log -XX:+UseConcMarkSweepGC -XX:+PrintSafepointStatistics -XX:PrintSafepointStatisticsCount=1 -XX:-OmitStackTraceInFastThrow -XX:+UseCMSCompactAtFullCollection -XX:-UseBiasedLocking -XX:-UseCounterDecay -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+ParallelRefProcEnabled -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+CMSParallelRemarkEnabled -XX:+ScavengeBeforeFullGC -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSScavengeBeforeRemark
     */
    public static void testWithCapacity() {
        for (int i = 0; i < LOOP; i++) {
            List<String> list = new ArrayList<>(CAPACITY);
            for (int j = 0; j < CAPACITY; j++) {
                String e = ThreadLocalRandom.current().nextInt(0, CAPACITY) + "";
                list.add(e);
            }
            System.out.println(list);
        }
    }

    public static void testWithoutCapacity() {
        for (int i = 0; i < LOOP; i++) {
            List<String> list = new ArrayList<>();
            for (int j = 0; j < CAPACITY; j++) {
                String e = ThreadLocalRandom.current().nextInt(0, CAPACITY) + "";
                list.add(e);
            }
            System.out.println(list);
        }
    }
}
