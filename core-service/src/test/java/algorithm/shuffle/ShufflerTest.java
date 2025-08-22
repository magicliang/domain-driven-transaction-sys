package algorithm.shuffle;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Shuffler类的单元测试
 *
 * 使用JUnit 5进行测试，包含功能测试、边界测试、异常测试等
 */
public class ShufflerTest {

    private final Shuffler shuffler = new Shuffler();

    @Test
    @DisplayName("基本功能测试 - 正常洗牌")
    void testBasicFunctionality() {
        // 测试1：正常情况
        int[] result = shuffler.shuffle(10, 5);
        assertEquals(5, result.length, "结果长度应为5");

        // 验证所有元素在正确范围内
        for (int num : result) {
            assertTrue(num >= 0 && num < 10, "元素应在[0,9]范围内");
        }

        // 测试2：k=0的情况
        int[] emptyResult = shuffler.shuffle(10, 0);
        assertEquals(0, emptyResult.length, "空结果长度应为0");

        // 测试3：k=n的情况
        int[] fullResult = shuffler.shuffle(5, 5);
        assertEquals(5, fullResult.length, "全选结果长度应为5");
    }

    @Test
    @DisplayName("边界情况测试")
    void testEdgeCases() {
        // 测试1：n=1, k=1
        int[] singleResult = shuffler.shuffle(1, 1);
        assertEquals(1, singleResult.length);
        assertEquals(0, singleResult[0], "唯一元素应为0");

        // 测试2：大数测试
        int[] largeResult = shuffler.shuffle(1000, 100);
        assertEquals(100, largeResult.length, "大数测试结果长度应为100");
    }

    @Test
    @DisplayName("参数验证测试 - 异常处理")
    void testParameterValidation() {
        // 测试1：n为负数
        assertThrows(IllegalArgumentException.class,
                () -> shuffler.shuffle(-1, 5),
                "应该抛出异常：n为负数");

        // 测试2：k为负数
        assertThrows(IllegalArgumentException.class,
                () -> shuffler.shuffle(10, -1),
                "应该抛出异常：k为负数");

        // 测试3：k大于n
        assertThrows(IllegalArgumentException.class,
                () -> shuffler.shuffle(5, 10),
                "应该抛出异常：k大于n");
    }

    @Test
    @DisplayName("唯一性测试 - 确保无重复元素")
    void testUniqueness() {
        int[] result = shuffler.shuffle(100, 50);
        Set<Integer> uniqueSet = new HashSet<>();

        for (int num : result) {
            assertFalse(uniqueSet.contains(num), "发现重复元素: " + num);
            uniqueSet.add(num);
        }

        assertEquals(50, uniqueSet.size(), "应该有50个唯一元素");
    }

    @Test
    @DisplayName("均匀分布测试 - 统计验证")
    void testUniformDistribution() {
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

        // 检查每个数字的频率是否接近期望值（允许20%的偏差）
        for (int i = 0; i < n; i++) {
            double ratio = frequency[i] / expected;
            assertTrue(ratio > 0.8 && ratio < 1.2,
                    "数字" + i + "的分布不均匀，比例: " + ratio);
        }
    }

    @Test
    @DisplayName("性能测试 - 大数组处理")
    void testPerformance() {
        long startTime = System.currentTimeMillis();

        // 测试大数组的性能
        int[] result = shuffler.shuffle(1000000, 1000);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertEquals(1000, result.length);
        assertTrue(duration < 1000, "性能测试应在1秒内完成，实际耗时: " + duration + "ms");
    }

    @Test
    @DisplayName("reverseShuffle - 基本功能测试")
    void testReverseShuffleBasicFunctionality() {
        // 测试正常情况
        int[] result = shuffler.reverseShuffle(10, 5);
        assertEquals(5, result.length, "结果长度应为5");

        // 验证所有元素在正确范围内
        for (int num : result) {
            assertTrue(num >= 0 && num < 10, "元素应在[0,9]范围内");
        }

        // 测试边界情况
        assertEquals(0, shuffler.reverseShuffle(10, 0).length);
        assertEquals(5, shuffler.reverseShuffle(5, 5).length);
    }

    @Test
    @DisplayName("reverseShuffle - 唯一性测试")
    void testReverseShuffleUniqueness() {
        int[] result = shuffler.reverseShuffle(100, 50);
        Set<Integer> uniqueSet = new HashSet<>();

        for (int num : result) {
            assertFalse(uniqueSet.contains(num), "发现重复元素: " + num);
            uniqueSet.add(num);
        }

        assertEquals(50, uniqueSet.size(), "应该有50个唯一元素");
    }

    @Test
    @DisplayName("reverseShuffle - 参数验证")
    void testReverseShuffleParameterValidation() {
        assertThrows(IllegalArgumentException.class, () -> shuffler.reverseShuffle(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> shuffler.reverseShuffle(10, -1));
        assertThrows(IllegalArgumentException.class, () -> shuffler.reverseShuffle(5, 10));
    }

    @Test
    @DisplayName("shuffleOptimized - 基本功能测试")
    void testShuffleOptimizedBasicFunctionality() {
        int[] result = shuffler.shuffleOptimized(10, 5);
        assertEquals(5, result.length);

        for (int num : result) {
            assertTrue(num >= 0 && num < 10);
        }

        assertEquals(0, shuffler.shuffleOptimized(10, 0).length);
        assertEquals(5, shuffler.shuffleOptimized(5, 5).length);
    }

    @Test
    @DisplayName("shuffleOptimized - 唯一性测试")
    void testShuffleOptimizedUniqueness() {
        int[] result = shuffler.shuffleOptimized(100, 50);
        Set<Integer> uniqueSet = new HashSet<>();

        for (int num : result) {
            assertFalse(uniqueSet.contains(num));
            uniqueSet.add(num);
        }

        assertEquals(50, uniqueSet.size());
    }

    @Test
    @DisplayName("shuffleOptimized - 参数验证")
    void testShuffleOptimizedParameterValidation() {
        assertThrows(IllegalArgumentException.class, () -> shuffler.shuffleOptimized(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> shuffler.shuffleOptimized(10, -1));
        assertThrows(IllegalArgumentException.class, () -> shuffler.shuffleOptimized(5, 10));
    }

    @Test
    @DisplayName("reverseShuffleOptimized - 基本功能测试")
    void testReverseShuffleOptimizedBasicFunctionality() {
        int[] result = shuffler.reverseShuffleOptimized(10, 5);
        assertEquals(5, result.length);

        for (int num : result) {
            assertTrue(num >= 0 && num < 10);
        }

        assertEquals(0, shuffler.reverseShuffleOptimized(10, 0).length);
        assertEquals(5, shuffler.reverseShuffleOptimized(5, 5).length);
    }

    @Test
    @DisplayName("reverseShuffleOptimized - 唯一性测试")
    void testReverseShuffleOptimizedUniqueness() {
        int[] result = shuffler.reverseShuffleOptimized(100, 50);
        Set<Integer> uniqueSet = new HashSet<>();

        for (int num : result) {
            assertFalse(uniqueSet.contains(num));
            uniqueSet.add(num);
        }

        assertEquals(50, uniqueSet.size());
    }

    @Test
    @DisplayName("reverseShuffleOptimized - 参数验证")
    void testReverseShuffleOptimizedParameterValidation() {
        assertThrows(IllegalArgumentException.class, () -> shuffler.reverseShuffleOptimized(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> shuffler.reverseShuffleOptimized(10, -1));
        assertThrows(IllegalArgumentException.class, () -> shuffler.reverseShuffleOptimized(5, 10));
    }

    @Test
    @DisplayName("shuffleFloyd - 基本功能测试")
    void testShuffleFloydBasicFunctionality() {
        int[] result = shuffler.shuffleFloyd(10, 5);
        assertEquals(5, result.length);

        for (int num : result) {
            assertTrue(num >= 0 && num < 10);
        }

        assertEquals(0, shuffler.shuffleFloyd(10, 0).length);
        assertEquals(5, shuffler.shuffleFloyd(5, 5).length);
    }

    @Test
    @DisplayName("shuffleFloyd - 唯一性测试")
    void testShuffleFloydUniqueness() {
        int[] result = shuffler.shuffleFloyd(100, 50);
        Set<Integer> uniqueSet = new HashSet<>();

        for (int num : result) {
            assertFalse(uniqueSet.contains(num));
            uniqueSet.add(num);
        }

        assertEquals(50, uniqueSet.size());
    }

    @Test
    @DisplayName("shuffleFloyd - 参数验证")
    void testShuffleFloydParameterValidation() {
        assertThrows(IllegalArgumentException.class, () -> shuffler.shuffleFloyd(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> shuffler.shuffleFloyd(10, -1));
        assertThrows(IllegalArgumentException.class, () -> shuffler.shuffleFloyd(5, 10));
    }

    @Test
    @DisplayName("所有洗牌算法结果一致性验证")
    void testAllAlgorithmsConsistency() {
        int n = 20;
        int k = 10;

        // 测试所有算法都能产生有效结果
        int[] result1 = shuffler.shuffle(n, k);
        int[] result2 = shuffler.reverseShuffle(n, k);
        int[] result3 = shuffler.shuffleOptimized(n, k);
        int[] result4 = shuffler.reverseShuffleOptimized(n, k);
        int[] result5 = shuffler.shuffleFloyd(n, k);

        // 验证所有结果长度正确
        assertEquals(k, result1.length);
        assertEquals(k, result2.length);
        assertEquals(k, result3.length);
        assertEquals(k, result4.length);
        assertEquals(k, result5.length);

        // 验证所有元素都在正确范围内
        for (int[] result : new int[][]{result1, result2, result3, result4, result5}) {
            Set<Integer> uniqueSet = new HashSet<>();
            for (int num : result) {
                assertTrue(num >= 0 && num < n);
                assertFalse(uniqueSet.contains(num));
                uniqueSet.add(num);
            }
            assertEquals(k, uniqueSet.size());
        }
    }

    @Test
    @DisplayName("性能对比测试 - 所有算法")
    void testAllAlgorithmsPerformance() {
        int n = 100000;
        int k = 1000;

        // 测试shuffle
        long start1 = System.currentTimeMillis();
        shuffler.shuffle(n, k);
        long time1 = System.currentTimeMillis() - start1;

        // 测试reverseShuffle
        long start2 = System.currentTimeMillis();
        shuffler.reverseShuffle(n, k);
        long time2 = System.currentTimeMillis() - start2;

        // 测试shuffleOptimized
        long start3 = System.currentTimeMillis();
        shuffler.shuffleOptimized(n, k);
        long time3 = System.currentTimeMillis() - start3;

        // 测试reverseShuffleOptimized
        long start4 = System.currentTimeMillis();
        shuffler.reverseShuffleOptimized(n, k);
        long time4 = System.currentTimeMillis() - start4;

        // 测试shuffleFloyd
        long start5 = System.currentTimeMillis();
        shuffler.shuffleFloyd(n, k);
        long time5 = System.currentTimeMillis() - start5;

        // 所有算法应在合理时间内完成
        assertTrue(time1 < 2000, "shuffle耗时过长: " + time1 + "ms");
        assertTrue(time2 < 2000, "reverseShuffle耗时过长: " + time2 + "ms");
        assertTrue(time3 < 2000, "shuffleOptimized耗时过长: " + time3 + "ms");
        assertTrue(time4 < 2000, "reverseShuffleOptimized耗时过长: " + time4 + "ms");
        assertTrue(time5 < 2000, "shuffleFloyd耗时过长: " + time5 + "ms");
    }

    @Test
    void testShuffleBackwardWithK() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int k = 5;

        int[] original = arr.clone();
        shuffler.shuffleBackward(arr, k);

        // 验证前k个元素被洗牌，后5个元素保持不变
        assertArrayEquals(Arrays.copyOfRange(original, k, original.length),
                Arrays.copyOfRange(arr, k, arr.length));

        // 验证前k个元素仍然是原来的元素，只是顺序不同
        Set<Integer> originalFirstK = new HashSet<>();
        Set<Integer> shuffledFirstK = new HashSet<>();
        for (int i = 0; i < k; i++) {
            originalFirstK.add(original[i]);
            shuffledFirstK.add(arr[i]);
        }
        assertEquals(originalFirstK, shuffledFirstK);
    }

    @Test
    void testShuffleBackwardWithK_EqualsArrayLength() {
        int[] arr = {1, 2, 3, 4, 5};
        int k = 5;

        int[] original = arr.clone();
        shuffler.shuffleBackward(arr, k);

        // 验证所有元素都被洗牌
        Set<Integer> originalSet = new HashSet<>();
        Set<Integer> shuffledSet = new HashSet<>();
        for (int i = 0; i < arr.length; i++) {
            originalSet.add(original[i]);
            shuffledSet.add(arr[i]);
        }
        assertEquals(originalSet, shuffledSet);
    }

    @Test
    void testShuffleBackwardWithK_Zero() {
        int[] arr = {1, 2, 3, 4, 5};
        int k = 0;

        int[] original = arr.clone();
        shuffler.shuffleBackward(arr, k);

        // 验证数组保持不变
        assertArrayEquals(original, arr);
    }

    @Test
    void testShuffleBackwardWithK_One() {
        int[] arr = {1, 2, 3, 4, 5};
        int k = 1;

        int[] original = arr.clone();
        shuffler.shuffleBackward(arr, k);

        // 验证只有第一个元素可能改变位置
        assertEquals(original[0], arr[0]); // 只有一个元素，洗牌后位置不变

        // 验证其余元素保持不变
        assertArrayEquals(Arrays.copyOfRange(original, 1, original.length),
                Arrays.copyOfRange(arr, 1, arr.length));
    }

    @Test
    void testShuffleBackwardWithK_InvalidK_Negative() {
        int[] arr = {1, 2, 3, 4, 5};

        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.shuffleBackward(arr, -1);
        });
    }

    @Test
    void testShuffleBackwardWithK_InvalidK_TooLarge() {
        int[] arr = {1, 2, 3, 4, 5};

        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.shuffleBackward(arr, 6);
        });
    }

    @Test
    void testShuffleBackwardWithK_NullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.shuffleBackward(null, 3);
        });
    }

    @Test
    void testShuffleBackwardFromEnd() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int k = 4;

        int[] original = arr.clone();
        shuffler.shuffleBackwardFromEnd(arr, k);

        // 验证前6个元素保持不变
        assertArrayEquals(Arrays.copyOfRange(original, 0, original.length - k),
                Arrays.copyOfRange(arr, 0, arr.length - k));

        // 验证后4个元素是原来的元素，只是顺序不同
        Set<Integer> originalLastK = new HashSet<>();
        Set<Integer> shuffledLastK = new HashSet<>();
        for (int i = arr.length - k; i < arr.length; i++) {
            originalLastK.add(original[i]);
            shuffledLastK.add(arr[i]);
        }
        assertEquals(originalLastK, shuffledLastK);
    }

    @Test
    void testShuffleBackwardFromEnd_Zero() {
        int[] arr = {1, 2, 3, 4, 5};
        int k = 0;

        int[] original = arr.clone();
        shuffler.shuffleBackwardFromEnd(arr, k);

        // 验证数组保持不变
        assertArrayEquals(original, arr);
    }

    @Test
    void testShuffleBackwardFromEnd_EqualsArrayLength() {
        int[] arr = {1, 2, 3, 4, 5};
        int k = 5;

        int[] original = arr.clone();
        shuffler.shuffleBackwardFromEnd(arr, k);

        // 验证所有元素都被洗牌
        Set<Integer> originalSet = new HashSet<>();
        Set<Integer> shuffledSet = new HashSet<>();
        for (int i = 0; i < arr.length; i++) {
            originalSet.add(original[i]);
            shuffledSet.add(arr[i]);
        }
        assertEquals(originalSet, shuffledSet);
    }

    @Test
    void testShuffleBackwardFromEnd_InvalidParameters() {
        int[] arr = {1, 2, 3, 4, 5};

        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.shuffleBackwardFromEnd(arr, -1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.shuffleBackwardFromEnd(arr, 6);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.shuffleBackwardFromEnd(null, 3);
        });
    }

    @Test
    void testShuffleBackwardCompatibility() {
        int[] arr = {1, 2, 3, 4, 5};

        // 测试向后兼容的方法
        assertDoesNotThrow(() -> {
            shuffler.shuffleBackward(arr);
        });

        // 验证数组长度不变
        assertEquals(5, arr.length);
    }

    @Test
    void testShuffleBackwardRandomness() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int k = 5;

        // 运行多次洗牌，验证随机性
        Set<String> results = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            int[] testArr = arr.clone();
            shuffler.shuffleBackward(testArr, k);
            results.add(Arrays.toString(testArr));
        }

        // 验证产生了不同的结果（概率上应该产生多种排列）
        assertTrue(results.size() > 1, "洗牌应该产生不同的排列");
    }
}