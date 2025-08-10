package algorithm.dp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * MaxSubArray 类的测试用例。
 * 用于测试 Kadane's Algorithm 实现的最大子数组和计算 [[1]].
 *
 * @author magicliang
 */
public class MaxSubArrayTest {

    private final MaxSubArray maxSubArray = new MaxSubArray();

    /**
     * 测试用例1: 基本正数数组
     * 输入: [1, 2, 3, 4, 5]
     * 期望输出: 15 (整个数组的和)
     */
    @Test
    public void testAllPositiveNumbers() {
        int[] arr = {1, 2, 3, 4, 5};
        int expected = 15;
        int actual = maxSubArray.getMaxSubArraySum(arr);
        assertEquals(expected, actual, "Failed for all positive numbers");
    }

    /**
     * 测试用例2: 基本负数数组
     * 输入: [-5, -2, -8, -1]
     * 期望输出: -1 (最大的单个元素)
     */
    @Test
    public void testAllNegativeNumbers() {
        int[] arr = {-5, -2, -8, -1};
        int expected = -1;
        int actual = maxSubArray.getMaxSubArraySum(arr);
        assertEquals(expected, actual, "Failed for all negative numbers");
    }

    /**
     * 测试用例3: 混合正负数 (经典例子)
     * 输入: [-2, 1, -3, 4, -1, 2, 1, -5, 4]
     * 期望输出: 6 (子数组 [4, -1, 2, 1] 的和)
     */
    @Test
    public void testMixedNumbers() {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int expected = 6;
        int actual = maxSubArray.getMaxSubArraySum(arr);
        assertEquals(expected, actual, "Failed for mixed numbers");
    }

    /**
     * 测试用例4: 包含零
     * 输入: [-1, 0, -2]
     * 期望输出: 0
     */
    @Test
    public void testWithZeros() {
        int[] arr = {-1, 0, -2};
        int expected = 0;
        int actual = maxSubArray.getMaxSubArraySum(arr);
        assertEquals(expected, actual, "Failed for array with zeros");
    }

    /**
     * 测试用例5: 单个元素 (正数)
     * 输入: [5]
     * 期望输出: 5
     */
    @Test
    public void testSinglePositiveElement() {
        int[] arr = {5};
        int expected = 5;
        int actual = maxSubArray.getMaxSubArraySum(arr);
        assertEquals(expected, actual, "Failed for single positive element");
    }

    /**
     * 测试用例6: 单个元素 (负数)
     * 输入: [-3]
     * 期望输出: -3
     */
    @Test
    public void testSingleNegativeElement() {
        int[] arr = {-3};
        int expected = -3;
        int actual = maxSubArray.getMaxSubArraySum(arr);
        assertEquals(expected, actual, "Failed for single negative element");
    }

    /**
     * 测试用例7: 全为零
     * 输入: [0, 0, 0]
     * 期望输出: 0
     */
    @Test
    public void testAllZeros() {
        int[] arr = {0, 0, 0};
        int expected = 0;
        int actual = maxSubArray.getMaxSubArraySum(arr);
        assertEquals(expected, actual, "Failed for all zeros");
    }

    /**
     * 测试用例8: 最大和在数组开始
     * 输入: [5, -1, -2, 1, 1]
     * 期望输出: 5
     */
    @Test
    public void testMaxSumAtBeginning() {
        int[] arr = {5, -1, -2, 1, 1};
        int expected = 5;
        int actual = maxSubArray.getMaxSubArraySum(arr);
        assertEquals(expected, actual, "Failed for max sum at beginning");
    }

    /**
     * 测试用例9: 最大和在数组末尾
     * 输入: [-1, -2, 1, 1, 5]
     * 期望输出: 7 (子数组 [1, 1, 5] 的和)
     */
    @Test
    public void testMaxSumAtEnd() {
        int[] arr = {-1, -2, 1, 1, 5};
        int expected = 7;
        int actual = maxSubArray.getMaxSubArraySum(arr);
        assertEquals(expected, actual, "Failed for max sum at end");
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
        }, "Should throw IllegalArgumentException for empty array");
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
        }, "Should throw IllegalArgumentException for null array");
    }
}
