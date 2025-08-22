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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Shuffler类的测试类
 *
 * 测试覆盖范围：
 * 1. 正常场景测试：验证洗牌逻辑正确性
 * 2. 边界条件测试：k=0、k=1、k=数组长度等边界情况
 * 3. 异常测试：参数验证、非法输入处理
 * 4. 随机性测试：验证洗牌的随机性
 * 5. 兼容性测试：确保向后兼容
 *
 * @author liangchuan
 */
class ShufflerTest {

    private Shuffler shuffler;

    @BeforeEach
    void setUp() {
        shuffler = new Shuffler();
    }

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

    @Test
    void testKnuthShuffle() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] original = arr.clone();

        shuffler.knuthShuffle(arr);

        // 验证洗牌后数组长度不变
        assertEquals(original.length, arr.length);

        // 验证元素集合不变（只是顺序改变）
        Set<Integer> originalSet = new HashSet<>();
        Set<Integer> shuffledSet = new HashSet<>();
        for (int value : original) {
            originalSet.add(value);
        }
        for (int value : arr) {
            shuffledSet.add(value);
        }
        assertEquals(originalSet, shuffledSet);

        // 验证洗牌确实发生了（虽然有小概率失败，但概率极低）
        boolean changed = false;
        for (int i = 0; i < original.length; i++) {
            if (original[i] != arr[i]) {
                changed = true;
                break;
            }
        }
        assertTrue(changed, "洗牌应该改变数组顺序");
    }

    @Test
    void testInPlaceShuffle() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] original = arr.clone();

        shuffler.inPlaceShuffle(arr);

        // 验证洗牌后数组长度不变
        assertEquals(original.length, arr.length);

        // 验证元素集合不变
        Set<Integer> originalSet = new HashSet<>();
        Set<Integer> shuffledSet = new HashSet<>();
        for (int value : original) {
            originalSet.add(value);
        }
        for (int value : arr) {
            shuffledSet.add(value);
        }
        assertEquals(originalSet, shuffledSet);

        // 验证洗牌确实发生了
        boolean changed = false;
        for (int i = 0; i < original.length; i++) {
            if (original[i] != arr[i]) {
                changed = true;
                break;
            }
        }
        assertTrue(changed, "洗牌应该改变数组顺序");
    }

    @Test
    void testReservoirSampling() {
        int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        // 测试正常情况
        int[] result = shuffler.reservoirSampling(data, 5);
        assertEquals(5, result.length);

        // 验证所有结果元素都来自原始数据
        Set<Integer> dataSet = new HashSet<>();
        for (int value : data) {
            dataSet.add(value);
        }
        for (int value : result) {
            assertTrue(dataSet.contains(value), "结果元素必须来自原始数据");
        }

        // 测试边界情况
        int[] allResult = shuffler.reservoirSampling(data, 10);
        assertEquals(10, allResult.length);

        int[] emptyResult = shuffler.reservoirSampling(data, 0);
        assertEquals(0, emptyResult.length);

        // 测试单元素选择
        int[] singleResult = shuffler.reservoirSampling(data, 1);
        assertEquals(1, singleResult.length);
        assertTrue(dataSet.contains(singleResult[0]));
    }

    @Test
    void testReservoirSamplingInvalidParameters() {
        int[] data = {1, 2, 3, 4, 5};

        // 测试null数组
        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.reservoirSampling(null, 3);
        });

        // 测试负数k
        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.reservoirSampling(data, -1);
        });

        // 测试k大于数组长度
        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.reservoirSampling(data, 10);
        });
    }

    @Test
    void testWeightedShuffle() {
        int[] arr = {1, 2, 3, 4, 5};
        double[] weights = {1.0, 2.0, 3.0, 4.0, 5.0}; // 权重递增

        int[] original = arr.clone();

        shuffler.weightedShuffle(arr, weights);

        // 验证洗牌后数组长度不变
        assertEquals(original.length, arr.length);

        // 验证元素集合不变
        Set<Integer> originalSet = new HashSet<>();
        Set<Integer> shuffledSet = new HashSet<>();
        for (int value : original) {
            originalSet.add(value);
        }
        for (int value : arr) {
            shuffledSet.add(value);
        }
        assertEquals(originalSet, shuffledSet);

        // 验证洗牌确实发生了
        boolean changed = false;
        for (int i = 0; i < original.length; i++) {
            if (original[i] != arr[i]) {
                changed = true;
                break;
            }
        }
        assertTrue(changed, "加权洗牌应该改变数组顺序");
    }

    @Test
    void testWeightedShuffleInvalidParameters() {
        int[] arr = {1, 2, 3};
        double[] weights = {1.0, 2.0, 3.0};

        // 测试null数组
        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.weightedShuffle(null, weights);
        });

        // 测试null权重
        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.weightedShuffle(arr, null);
        });

        // 测试长度不匹配
        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.weightedShuffle(arr, new double[]{1.0, 2.0});
        });

        // 测试空数组
        int[] emptyArr = {};
        double[] emptyWeights = {};
        assertDoesNotThrow(() -> {
            shuffler.weightedShuffle(emptyArr, emptyWeights);
        });
    }

    @Test
    void testPartialShuffle() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] original = arr.clone();

        // 测试部分洗牌（索引2到7）
        shuffler.partialShuffle(arr, 2, 7);

        // 验证洗牌后数组长度不变
        assertEquals(original.length, arr.length);

        // 验证区间外的元素位置不变
        assertEquals(original[0], arr[0]);
        assertEquals(original[1], arr[1]);
        assertEquals(original[7], arr[7]);
        assertEquals(original[8], arr[8]);
        assertEquals(original[9], arr[9]);

        // 验证区间内的元素集合不变
        Set<Integer> originalSubset = new HashSet<>();
        Set<Integer> shuffledSubset = new HashSet<>();
        for (int i = 2; i < 7; i++) {
            originalSubset.add(original[i]);
            shuffledSubset.add(arr[i]);
        }
        assertEquals(originalSubset, shuffledSubset);
    }

    @Test
    void testPartialShuffleInvalidParameters() {
        int[] arr = {1, 2, 3, 4, 5};

        // 测试null数组
        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.partialShuffle(null, 1, 3);
        });

        // 测试无效区间
        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.partialShuffle(arr, -1, 3);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.partialShuffle(arr, 3, 1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            shuffler.partialShuffle(arr, 0, 10);
        });

        // 测试有效边界
        assertDoesNotThrow(() -> {
            shuffler.partialShuffle(arr, 0, 5);
        });

        assertDoesNotThrow(() -> {
            shuffler.partialShuffle(arr, 2, 3);
        });
    }

    @Test
    void testShuffleAlgorithmsConsistency() {
        // 测试不同洗牌算法的一致性
        int[] arr1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] arr2 = arr1.clone();
        int[] arr3 = arr1.clone();

        // 使用不同算法洗牌
        shuffler.knuthShuffle(arr1);
        shuffler.inPlaceShuffle(arr2);
        shuffler.shuffleBackward(arr3);

        // 验证所有算法都保持了元素集合
        Set<Integer> originalSet = new HashSet<>();
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();
        Set<Integer> set3 = new HashSet<>();

        for (int i = 1; i <= 10; i++) {
            originalSet.add(i);
        }

        for (int value : arr1) {
            set1.add(value);
        }
        for (int value : arr2) {
            set2.add(value);
        }
        for (int value : arr3) {
            set3.add(value);
        }

        assertEquals(originalSet, set1);
        assertEquals(originalSet, set2);
        assertEquals(originalSet, set3);
    }

    @Test
    void testReservoirSamplingDistribution() {
        // 测试蓄水池抽样的分布均匀性
        int[] data = {1, 2, 3, 4, 5};
        int k = 3;
        int trials = 10000;

        // 统计每个元素被选中的次数
        int[] counts = new int[data.length];

        for (int t = 0; t < trials; t++) {
            int[] result = shuffler.reservoirSampling(data, k);
            for (int value : result) {
                counts[value - 1]++;
            }
        }

        // 验证每个元素都有被选中
        for (int count : counts) {
            assertTrue(count > 0, "每个元素都应该有机会被选中");
        }

        // 验证总选择次数
        int totalSelections = 0;
        for (int count : counts) {
            totalSelections += count;
        }
        assertEquals(trials * k, totalSelections);
    }

    @Test
    void testWeightedShuffleBias() {
        // 测试加权洗牌的偏向性
        int[] arr = {1, 2, 3};
        double[] weights = {1.0, 10.0, 1.0}; // 中间元素权重最高

        int trials = 10000;
        int[] counts = new int[arr.length];

        for (int t = 0; t < trials; t++) {
            int[] testArr = arr.clone();
            shuffler.weightedShuffle(testArr, weights);
            // 统计第一个位置的元素
            counts[testArr[0] - 1]++;
        }

        // 验证权重高的元素出现在前面的概率更高
        // 元素2（权重10.0）应该比元素1和3（权重1.0）更频繁地出现在前面
        assertTrue(counts[1] > counts[0], "权重高的元素应该更频繁地出现在前面");
        assertTrue(counts[1] > counts[2], "权重高的元素应该更频繁地出现在前面");
    }
}