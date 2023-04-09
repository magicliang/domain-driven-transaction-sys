package com.magicliang.transaction.sys.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author liangchuan
 */
@Slf4j
public class AlgorithmTest {

    /**
     * 找出全部满足 n < 8 * log(n) 的整数
     */
    @Test
    public void find122() {
        List<Integer> results = new ArrayList<>();

        boolean begin = false;
        boolean end = false;
        int i = 0;
        while (!begin || (begin = !end)) {
            boolean satisfy = checkSatisfy1(i);
            if (satisfy) {
                results.add(i);
                begin = true;
            } else if (begin) {
                end = true;
            }
            ++i;
        }
        System.out.println(results.toString());
        log.info(results.toString());
    }

    private static boolean checkSatisfy1(int i) {
        return i < 8 * (Math.log(i) / Math.log(2));
    }

    /**
     * 找出所有满足100n平方小于 2 的 n次幂的数的最小值，运用反向思维，从另一个区间的最大值开始找起，这也运用了整数的间隔性
     * 跨过15（包含15）以后，100n平方大于 2 的 n次幂的运行效率
     */
    @Test
    public void find123() {
        List<Integer> results = new ArrayList<>();

        boolean begin = false;
        boolean end = false;
        int i = 0;
        while (!begin || (begin = !end)) {
            boolean satisfy = checkSatisfy2(i);
            if (satisfy) {
                results.add(i);
                begin = true;
            } else if (begin) {
                end = true;
            }
            ++i;
        }
        System.out.println(results.toString());
        log.info(results.toString());
    }

    private static boolean checkSatisfy2(int i) {
        return 100 * Math.pow(i, 2) > Math.pow(2, i);
    }

    @Test
    public void findThinkProblem11() {
        @Data
        @AllArgsConstructor
        class TimeUnit {
            private String name;
            private long length;
        }

        long oneSecond = 1000L;
        long oneMinute = 60 * oneSecond;
        long oneHour = 60 * oneMinute;
        long oneDay = 24 * oneHour;
        long oneMonth = 30 * oneDay;
        long oneYear = 365 * oneMonth;

        List<TimeUnit> units = Lists.newArrayList(
                new TimeUnit("oneSecond", oneSecond),
                new TimeUnit("oneMinute", oneMinute),
                new TimeUnit("oneHour", oneHour),
                new TimeUnit("oneDay", oneDay),
                new TimeUnit("oneMonth", oneMonth),
                new TimeUnit("oneYear", oneYear)
        );
        List<Function<Integer, Long>> timeComplexities = Lists.newArrayList(
//                (n) -> toLong(Math.log(n)/ Math.log(2))
//                (n) -> toLong(Math.sqrt(n))
//                (n) -> (long) n,
//                (n) -> toLong(n * (Math.log(n)/ Math.log(2)))
//                        (n) -> toLong(Math.pow(n, 2)),
//                (n) -> toLong(Math.pow(n, 3)),
//                (n) -> toLong(Math.pow(2, n)),
                AlgorithmTest::factorial
        );

        @Data
        @AllArgsConstructor
        class Applier {
            private String name;
            private Function<Integer, Long> applier;
        }

        for (TimeUnit unit : units) {
            for (Function<Integer, Long> timeComplexity : timeComplexities) {
                int max = 0;
                log.info("for this unit: {}, function: {}", unit.getName(), timeComplexity.toString());
                System.out.printf("for this unit: %s, function: %s%n", unit.getName(), timeComplexity.toString());

                boolean begin = false;
                boolean end = false;
                int i = 1;
                while (i < Integer.MAX_VALUE - 1 && (!begin || (begin = !end))) {
                    boolean satisfy = checkSatisfy3(i, timeComplexity, unit.getLength());
                    if (satisfy) {
                        if (i > max) {
                            max = i;
                        }
                        begin = true;
                    } else if (begin) {
                        end = true;
                    }
                    ++i;
                }
                System.out.println(max);
                log.info(max + "");
            }

        }
    }

    private static boolean checkSatisfy3(int i, Function<Integer, Long> applier, long maxMilliSeconds) {
        return applier.apply(i) < maxMilliSeconds;
    }

    private static long toLong(double d) {
        return Double.valueOf(d).longValue();
    }

    public static long factorial(long number) {
        long ret = 1;
        int i = 0;
        for (i = 1; i <= number; i++) {
            ret = ret * i;
        }
        return ret;
    }
}
