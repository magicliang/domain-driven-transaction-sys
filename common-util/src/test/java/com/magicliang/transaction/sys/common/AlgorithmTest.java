package com.magicliang.transaction.sys.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

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

    @Test
    public void testInsertSort() {
        int[] arr = new int[]{5, 3, 1, 2, 99};
        insertSort1(arr);
        System.out.println(Arrays.toString(arr));

        arr = new int[]{5, 3, 1, 2, 99, 34, 22, 57, 48};
        insertSort2(arr);
        System.out.println(Arrays.toString(arr));

        arr = new int[]{5, 3, 1, 2, 99, 34, 22, 57, 48};
        standardInsertSort(arr);
        System.out.println(Arrays.toString(arr));

        arr = new int[]{105, -3, 11, 12, 199, 354, 1122, 5700, 4800};
        reverseInsertSort(arr);
        System.out.println(Arrays.toString(arr));

        int[] a = new int[]{1, 0, 1, 1, 1, 1};
        int[] b = new int[]{1, 0, 1, 1, 1, 1};
        // 答案为 1011110，恰好是上述的数组左移一位
        System.out.println(Arrays.toString(merge2BitArrs(a, b)));

        int[] c = new int[]{1, 1, 1, 1};
        int[] d = new int[]{1, 0, 1, 1, 1, 1};
        // 111110
        System.out.println(Arrays.toString(merge2BitArrs(c, d)));
    }

    /**
     * 不好的解法
     * 这个解法之所以不好，是因为没有办法只做一次赋值
     * 我们需要寻求的解应该是遍历得到一个独一无二的插入点，这个插入点的值已经被复制走了，而下一个点比它小或者等于它，不需要懂
     *
     * @param arr 待排序数组
     */
    private void insertSort1(int[] arr) {
        int length = arr.length;
        for (int i = 1; i < length; i++) {
            insert1(arr, i);
        }
    }

    private void insert1(int[] arr, int i) {
        int key = arr[i];
        for (int j = i - 1; j >= 0; j--) {
            if (arr[j] <= key) {
                arr[j + 1] = key;
                return;
            } else {
                arr[j + 1] = arr[j];
            }
            arr[j] = key;
        }
    }

    /**
     * 第二种算法：比较优雅，只要求出插入点就行了，如何求出呢？
     * <p>
     * 1. 先正序遍历待排序数组的元素 i
     * 2. 把它的前方当做已排序数组
     * 3. 从后方遍历已排序数组
     * 4. 如果后方遍历的元素 arr[j] 大于 arr[i]，不可插入，arr[j] 后退一格
     * 5. j--
     * 6. 如果此时的 arr[j] 小于等于arr[i]，则 arr[j+1] 适合插入，且 arr[j+1] 和 arr[j+2]在上一轮里做过了冗余
     * 7. 如果已经遍历到尽头，则 j 已然越界， arr[j+1] 为 0。
     * <p>
     * 插入算法要理解好 j、j + 1、j + 2 三区间移动的关系：
     * 在每轮迭代开始的时候，j 代表了未处理的区间的右边界，当我们做过判定后，j 被我们移到已处理的区间里，j因为有了冗余，
     * 成了灵活点，而j-1成了未处理的区间，我们通过j--，进入下一个未处理的区间，进入下一轮迭代
     * 等我们进入未处理的区间发现无须处理时，j- 1 + 1 = j 就是我们的处理点
     * 要从 check-do-check-do...check来理解平凡的 while 循环
     * 这也为我们的接下来引入快排做好了准备
     *
     * @param arr 待排序数组
     */
    private void insertSort2(int[] arr) {
        // 在每轮迭代以前，先抽取不变量
        int length = arr.length;
        for (int i = 1; i < length; i++) {
            int key = arr[i];
            int j = i - 1;
            // 逻辑归并以后的写法不如展开时容易理解，上一轮的状态在上一轮迭代已经完成了，每一轮的条件判定要单独做
            while (j >= 0 && arr[j] > key) {
                // 先退一位，这样，这样 a[j] 总是可插入的
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    /**
     * 根据 CLRS 的  psedocode 写出的算法
     *
     * @param arr 待排序数组
     */
    private void standardInsertSort(int[] arr) {
        // 在这个例子里，认为 i 应该是有序的，所以从 1 开始和从0开始都是一样的，但j要用j+1回正回这个区间，就比较麻烦
        for (int i = 0; i < arr.length; i++) {
            int key = arr[i];
            // 对 0 而言，此时数组已经越界
            int j = i - 1;
            // 要详细比较所有的元素，包括0，在CLRS的伪代码里，j的起点是1，就会写作 > 0
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            // 到这里要么 j 连 0 都大于 key，要么找到了一个不能进入的区间的终点
            arr[j + 1] = key;
        }
    }

    public void reverseInsertSort(int[] arr) {
        int length = arr.length;
        for (int i = 1; i < length; i++) {
            int key = arr[i];
            int j = i - 1;
            // 在这里不许等号意味着稳定，允许等号意味着交换
            while (j >= 0 && arr[j] < key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    private int[] merge2BitArrs(int[] a, int[] b) {
        int tmp = 0;
        int aLength = a.length;
        int bLength = b.length;
        int i = aLength - 1;
        int j = bLength - 1;
        int[] result = new int[(aLength > bLength ? a.length : b.length) + 1];
        int k = result.length - 1;
        while (i >= 0 || j >= 0) {
            int e1 = i >= 0 ? a[i] : 0;
            int e2 = j >= 0 ? b[j] : 0;
            int tmpResult = e1 + e2 + tmp;
            if (tmpResult > 1) {
                // 每进一位，本位减 2
                tmp = 1;
                tmpResult -= 2;
            } else {
                // 否则，进位为 0
                tmp = 0;
            }
            result[k] = tmpResult;
            i--;
            j--;
            k--;
        }
        if (tmp > 0) {
            result[k] = tmp;
        }
        if (result[0] == 0) {
            int newLength = result.length - 1;
            int[] newResult = new int[newLength];
            // 这个小拷贝的时间复杂度过高，放在这里实际上破坏了算法的理论复杂度，就当一个添头
            System.arraycopy(result, 1, newResult, 0, newLength);
            return newResult;
        }
        return result;
    }
}
