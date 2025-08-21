package algorithm.shuffle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 洗牌算法实现类
 *
 * 功能：从0到n-1的整数中随机选择k个不重复的数
 * 算法：基于Fisher-Yates洗牌算法的部分洗牌实现
 *
 * 时间复杂度：O(n) - 需要初始化数组和k次交换
 * 空间复杂度：O(n) - 需要存储整个数组
 * 
 * @author liangchuan
 */
public class Shuffler {

    /**
     * 从0到n-1的整数中随机选择k个不重复的数
     *
     * @param n 整数范围的上限（不包含n）
     * @param k 需要选择的整数个数
     * @return 包含k个随机整数的数组，范围在[0, n-1]之间
     * @throws IllegalArgumentException 当参数不合法时
     */
    public int[] shuffle(int n, int k) {
        if (n <= 0) {
            throw new IllegalArgumentException("n必须大于0");
        }
        if (k < 0) {
            throw new IllegalArgumentException("k必须大于等于0");
        }
        if (k > n) {
            throw new IllegalArgumentException("k不能大于n");
        }

        int[] total = new int[n];

        // 初始化待排序数组：[0, 1, 2, ..., n-1]
        for (int i = 0; i < n; i++) {
            total[i] = i;
        }

        // 执行k次交换，每次将当前位置j与[j, n-1]范围内的随机位置交换
        // 这样前k个位置就是随机选择的k个数
        for (int j = 0; j < k; j++) {
            // 在[j, n-1]范围内随机选择一个位置
            // ThreadLocalRandom.current().nextInt(0, n-j) 生成[0, n-j-1]的随机数
            // 加上j后得到[j, n-1]范围内的随机位置
            swap(total, j, j + ThreadLocalRandom.current().nextInt(0, n - j));
        }

        // 提取前k个元素作为结果
        int[] result = new int[k];
        for (int m = 0; m < k; m++) {
            result[m] = total[m];
        }

        return result;
    }

    /**
     * 交换数组中两个位置的元素
     *
     * @param array 目标数组
     * @param i 第一个位置
     * @param j 第二个位置
     */
    private void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    /**
     * 测试用例类
     */
    public static class ShufflerTest {

        public static void main(String[] args) {
            Shuffler shuffler = new Shuffler();

            // 运行所有测试
            testBasicFunctionality(shuffler);
            testEdgeCases(shuffler);
            testUniformDistribution(shuffler);
            testUniqueness(shuffler);
            testParameterValidation(shuffler);

            System.out.println("所有测试通过！");
        }

        /**
         * 测试基本功能
         */
        private static void testBasicFunctionality(Shuffler shuffler) {
            System.out.println("=== 基本功能测试 ===");

            // 测试1：正常情况
            int[] result = shuffler.shuffle(10, 5);
            System.out.println("shuffle(10, 5) = " + Arrays.toString(result));
            assert result.length == 5 : "结果长度应为5";

            // 测试2：k=0的情况
            int[] emptyResult = shuffler.shuffle(10, 0);
            System.out.println("shuffle(10, 0) = " + Arrays.toString(emptyResult));
            assert emptyResult.length == 0 : "空结果长度应为0";

            // 测试3：k=n的情况
            int[] fullResult = shuffler.shuffle(5, 5);
            System.out.println("shuffle(5, 5) = " + Arrays.toString(fullResult));
            assert fullResult.length == 5 : "全选结果长度应为5";

            // 验证所有元素在正确范围内
            for (int num : result) {
                assert num >= 0 && num < 10 : "元素应在[0,9]范围内";
            }
        }

        /**
         * 测试边界情况
         */
        private static void testEdgeCases(Shuffler shuffler) {
            System.out.println("\n=== 边界情况测试 ===");

            // 测试1：n=1, k=1
            int[] singleResult = shuffler.shuffle(1, 1);
            System.out.println("shuffle(1, 1) = " + Arrays.toString(singleResult));
            assert singleResult[0] == 0 : "唯一元素应为0";

            // 测试2：大数测试
            int[] largeResult = shuffler.shuffle(1000, 100);
            System.out.println("shuffle(1000, 100) 长度 = " + largeResult.length);
            assert largeResult.length == 100 : "大数测试结果长度应为100";
        }

        /**
         * 测试参数验证
         */
        private static void testParameterValidation(Shuffler shuffler) {
            System.out.println("\n=== 参数验证测试 ===");

            try {
                shuffler.shuffle(-1, 5);
                assert false : "应该抛出异常：n为负数";
            } catch (IllegalArgumentException e) {
                System.out.println("✓ 正确捕获n为负数的异常: " + e.getMessage());
            }

            try {
                shuffler.shuffle(10, -1);
                assert false : "应该抛出异常：k为负数";
            } catch (IllegalArgumentException e) {
                System.out.println("✓ 正确捕获k为负数的异常: " + e.getMessage());
            }

            try {
                shuffler.shuffle(5, 10);
                assert false : "应该抛出异常：k大于n";
            } catch (IllegalArgumentException e) {
                System.out.println("✓ 正确捕获k大于n的异常: " + e.getMessage());
            }
        }

        /**
         * 测试唯一性：确保结果中没有重复元素
         */
        private static void testUniqueness(Shuffler shuffler) {
            System.out.println("\n=== 唯一性测试 ===");

            int[] result = shuffler.shuffle(100, 50);
            Set<Integer> uniqueSet = new HashSet<>();

            for (int num : result) {
                assert !uniqueSet.contains(num) : "发现重复元素: " + num;
                uniqueSet.add(num);
            }

            System.out.println("✓ 所有元素都是唯一的，共" + uniqueSet.size() + "个");
        }

        /**
         * 测试均匀分布（统计测试）
         */
        private static void testUniformDistribution(Shuffler shuffler) {
            System.out.println("\n=== 均匀分布测试 ===");

            int n = 10;
            int k = 5;
            int trials = 10000;
            int[] frequency = new int[n];

            // 运行多次洗牌，统计每个数字出现的频率
            for (int i = 0; i < trials; i++) {
                int[] result = shuffler.shuffle(n, k);
                for (int num : result) {
                    frequency[num]++;
                }
            }

            // 计算期望频率
            double expected = (double) (k * trials) / n;
            System.out.println("期望频率: " + expected);

            // 检查每个数字的频率是否接近期望值
            for (int i = 0; i < n; i++) {
                double ratio = frequency[i] / expected;
                System.out.printf("数字%d出现%d次，比例: %.2f\n", i, frequency[i], ratio);
                assert ratio > 0.8 && ratio < 1.2 : "数字" + i + "的分布可能不均匀";
            }

            System.out.println("✓ 分布测试通过");
        }
    }
}
