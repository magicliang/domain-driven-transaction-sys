package algorithm.dp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * MaxSubArray 类的测试用例。
 * 用于测试 Kadane's Algorithm 实现的最大子数组和计算及坐标返回.
 *
 * @author magicliang
 */
public class MaxSubArrayTest {

    private final MaxSubArray maxSubArray = new MaxSubArray();

    /**
     * 测试用例1: 基本正数数组
     * 输入: [1, 2, 3, 4, 5]
     * 期望输出: 15 (整个数组的和)
     * 坐标: [0, 4]
     */
    @Test
    public void testAllPositiveNumbers() {
        int[] arr = {1, 2, 3, 4, 5};
        int expectedSum = 15;
        List<Integer> expectedCoords = Arrays.asList(0, 4);

        int actualSum = maxSubArray.getMaxSubArraySum(arr);
        List<Integer> actualCoords = MaxSubArray.getMaxSubArraySumCoOrdination(arr);

        assertEquals(expectedSum, actualSum, "Failed for all positive numbers sum");
        assertEquals(expectedCoords, actualCoords, "Failed for all positive numbers coordinates");
    }

    /**
     * 测试用例2: 基本负数数组
     * 输入: [-5, -2, -8, -1]
     * 期望输出: -1 (最大的单个元素)
     * 坐标: [3, 3]
     */
    @Test
    public void testAllNegativeNumbers() {
        int[] arr = {-5, -2, -8, -1};
        int expectedSum = -1;
        List<Integer> expectedCoords = Arrays.asList(3, 3);

        int actualSum = maxSubArray.getMaxSubArraySum(arr);
        List<Integer> actualCoords = maxSubArray.getMaxSubArraySumCoOrdination(arr);

        assertEquals(expectedSum, actualSum, "Failed for all negative numbers sum");
        assertEquals(expectedCoords, actualCoords, "Failed for all negative numbers coordinates");
    }

    /**
     * 测试用例3: 混合正负数 (经典例子)
     * 输入: [-2, 1, -3, 4, -1, 2, 1, -5, 4]
     * 期望输出: 6 (子数组 [4, -1, 2, 1] 的和)
     * 坐标: [3, 6]
     */
    @Test
    public void testMixedNumbers() {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int expectedSum = 6;
        List<Integer> expectedCoords = Arrays.asList(3, 6);

        int actualSum = maxSubArray.getMaxSubArraySum(arr);
        List<Integer> actualCoords = maxSubArray.getMaxSubArraySumCoOrdination(arr);

        assertEquals(expectedSum, actualSum, "Failed for mixed numbers sum");
        assertEquals(expectedCoords, actualCoords, "Failed for mixed numbers coordinates");

        // 验证坐标对应的子数组和
        int calculatedSum = 0;
        for (int i = 3; i <= 6; i++) {
            calculatedSum += arr[i];
        }
        assertEquals(expectedSum, calculatedSum, "Coordinates should point to correct subarray");
    }

    /**
     * 测试用例4: 包含零
     * 输入: [-1, 0, -2]
     * 期望输出: 0
     * 坐标: [1, 1]
     */
    @Test
    public void testWithZeros() {
        int[] arr = {-1, 0, -2};
        int expectedSum = 0;
        List<Integer> expectedCoords = Arrays.asList(1, 1);

        int actualSum = maxSubArray.getMaxSubArraySum(arr);
        List<Integer> actualCoords = maxSubArray.getMaxSubArraySumCoOrdination(arr);

        assertEquals(expectedSum, actualSum, "Failed for array with zeros sum");
        assertEquals(expectedCoords, actualCoords, "Failed for array with zeros coordinates");
    }

    /**
     * 测试用例5: 单个元素 (正数)
     * 输入: [5]
     * 期望输出: 5
     * 坐标: [0, 0]
     */
    @Test
    public void testSinglePositiveElement() {
        int[] arr = {5};
        int expectedSum = 5;
        List<Integer> expectedCoords = Arrays.asList(0, 0);

        int actualSum = maxSubArray.getMaxSubArraySum(arr);
        List<Integer> actualCoords = maxSubArray.getMaxSubArraySumCoOrdination(arr);

        assertEquals(expectedSum, actualSum, "Failed for single positive element sum");
        assertEquals(expectedCoords, actualCoords, "Failed for single positive element coordinates");
    }

    /**
     * 测试用例6: 单个元素 (负数)
     * 输入: [-3]
     * 期望输出: -3
     * 坐标: [0, 0]
     */
    @Test
    public void testSingleNegativeElement() {
        int[] arr = {-3};
        int expectedSum = -3;
        List<Integer> expectedCoords = Arrays.asList(0, 0);

        int actualSum = maxSubArray.getMaxSubArraySum(arr);
        List<Integer> actualCoords = maxSubArray.getMaxSubArraySumCoOrdination(arr);

        assertEquals(expectedSum, actualSum, "Failed for single negative element sum");
        assertEquals(expectedCoords, actualCoords, "Failed for single negative element coordinates");
    }

    /**
     * 测试用例7: 全为零
     * 输入: [0, 0, 0]
     * 期望输出: 0
     * 坐标: [0, 0]
     */
    @Test
    public void testAllZeros() {
        int[] arr = {0, 0, 0};
        int expectedSum = 0;
        List<Integer> expectedCoords = Arrays.asList(0, 0);

        int actualSum = maxSubArray.getMaxSubArraySum(arr);
        List<Integer> actualCoords = maxSubArray.getMaxSubArraySumCoOrdination(arr);

        assertEquals(expectedSum, actualSum, "Failed for all zeros sum");
        assertEquals(expectedCoords, actualCoords, "Failed for all zeros coordinates");
    }

    /**
     * 测试用例8: 最大和在数组开始
     * 输入: [5, -1, -2, 1, 1]
     * 期望输出: 5
     * 坐标: [0, 0]
     */
    @Test
    public void testMaxSumAtBeginning() {
        int[] arr = {5, -1, -2, 1, 1};
        int expectedSum = 5;
        List<Integer> expectedCoords = Arrays.asList(0, 0);

        int actualSum = maxSubArray.getMaxSubArraySum(arr);
        List<Integer> actualCoords = maxSubArray.getMaxSubArraySumCoOrdination(arr);

        assertEquals(expectedSum, actualSum, "Failed for max sum at beginning sum");
        assertEquals(expectedCoords, actualCoords, "Failed for max sum at beginning coordinates");
    }

    /**
     * 测试用例9: 最大和在数组末尾
     * 输入: [-1, -2, 1, 1, 5]
     * 期望输出: 7 (子数组 [1, 1, 5] 的和)
     * 坐标: [2, 4]
     */
    @Test
    public void testMaxSumAtEnd() {
        int[] arr = {-1, -2, 1, 1, 5};
        int expectedSum = 7;
        List<Integer> expectedCoords = Arrays.asList(2, 4);

        int actualSum = maxSubArray.getMaxSubArraySum(arr);
        List<Integer> actualCoords = maxSubArray.getMaxSubArraySumCoOrdination(arr);

        assertEquals(expectedSum, actualSum, "Failed for max sum at end sum");
        assertEquals(expectedCoords, actualCoords, "Failed for max sum at end coordinates");

        // 验证坐标对应的子数组和
        int calculatedSum = 0;
        for (int i = 2; i <= 4; i++) {
            calculatedSum += arr[i];
        }
        assertEquals(expectedSum, calculatedSum, "Coordinates should point to correct subarray");
    }

    /**
     * 测试用例10: 空数组 (应抛出异常)
     * 输入: []
     * 期望: IllegalArgumentException
     */
    @Test
    public void testEmptyArray() {
        int[] arr = {};
        assertThrows(IllegalArgumentException.class, () -> {
            maxSubArray.getMaxSubArraySum(arr);
        }, "Should throw IllegalArgumentException for empty array - sum method");

        assertThrows(IllegalArgumentException.class, () -> {
            maxSubArray.getMaxSubArraySumCoOrdination(arr);
        }, "Should throw IllegalArgumentException for empty array - coordinates method");
    }

    /**
     * 测试用例11: null 数组 (应抛出异常)
     * 输入: null
     * 期望: IllegalArgumentException
     */
    @Test
    public void testNullArray() {
        int[] arr = null;
        assertThrows(IllegalArgumentException.class, () -> {
            maxSubArray.getMaxSubArraySum(arr);
        }, "Should throw IllegalArgumentException for null array - sum method");

        assertThrows(IllegalArgumentException.class, () -> {
            maxSubArray.getMaxSubArraySumCoOrdination(arr);
        }, "Should throw IllegalArgumentException for null array - coordinates method");
    }

    /**
     * 测试用例12: 重新开始后更好的情况
     * 输入: [10, -15, 5, 6, -1]
     * 期望输出: 10 (子数组 [5, 6] 的和)
     * 坐标: [2, 3]
     */
    @Test
    public void testRestartBetter() {
        int[] arr = {10, -15, 5, 6, -1};
        int expectedSum = 11;
        List<Integer> expectedCoords = Arrays.asList(2, 3);

        int actualSum = maxSubArray.getMaxSubArraySum(arr);
        List<Integer> actualCoords = maxSubArray.getMaxSubArraySumCoOrdination(arr);

        assertEquals(expectedSum, actualSum, "Failed for restart better case sum");
        assertEquals(expectedCoords, actualCoords, "Failed for restart better case coordinates");
    }

    /**
     * 测试用例13: 相等的最大值（返回第一个）
     * 输入: [1, -1, 1, -1, 1]
     * 期望输出: 1
     * 坐标: [0, 0]
     */
    @Test
    public void testEqualMaxValues() {
        int[] arr = {1, -1, 1, -1, 1};
        int expectedSum = 1;
        List<Integer> expectedCoords = Arrays.asList(0, 0);

        int actualSum = maxSubArray.getMaxSubArraySum(arr);
        List<Integer> actualCoords = maxSubArray.getMaxSubArraySumCoOrdination(arr);

        assertEquals(expectedSum, actualSum, "Failed for equal max values sum");
        assertEquals(expectedCoords, actualCoords, "Failed for equal max values coordinates");
    }

    /**
     * 测试用例14: 验证坐标的有效性
     */
    @Test
    public void testCoordinateValidity() {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        List<Integer> coords = maxSubArray.getMaxSubArraySumCoOrdination(arr);

        int begin = coords.get(0);
        int end = coords.get(1);

        // 验证坐标范围有效性
        assertTrue(begin >= 0, "Begin index should be non-negative");
        assertTrue(end >= 0, "End index should be non-negative");
        assertTrue(begin < arr.length, "Begin index should be within array bounds");
        assertTrue(end < arr.length, "End index should be within array bounds");
        assertTrue(begin <= end, "Begin index should not exceed end index");

        // 验证坐标对应的和等于返回的最大和
        int sumFromCoords = 0;
        for (int i = begin; i <= end; i++) {
            sumFromCoords += arr[i];
        }
        int maxSum = maxSubArray.getMaxSubArraySum(arr);
        assertEquals(maxSum, sumFromCoords, "Sum calculated from coordinates should match max sum");
    }
}
