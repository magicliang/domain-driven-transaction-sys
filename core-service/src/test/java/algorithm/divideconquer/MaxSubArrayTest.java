package algorithm.divideconquer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * MaxSubArray 类的 JUnit 5 测试用例。
 * 测试分治法实现。
 */
public class MaxSubArrayTest {

    // --- 测试用例数据 ---
    // Test 1: 基本正数和负数混合
    private static final int[] ARR1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
    private static final int EXPECTED1 = 6; // 子数组 [4, -1, 2, 1]

    // Test 2: 全为负数
    private static final int[] ARR2 = {-5, -2, -8, -1};
    private static final int EXPECTED2 = -1; // 最大的单个元素

    // Test 3: 全为正数
    private static final int[] ARR3 = {1, 2, 3, 4, 5};
    private static final int EXPECTED3 = 15; // 整个数组

    // Test 4: 单个元素 (正数)
    private static final int[] ARR4 = {5};
    private static final int EXPECTED4 = 5;

    // Test 5: 单个元素 (负数)
    private static final int[] ARR5 = {-3};
    private static final int EXPECTED5 = -3;

    // Test 6: 包含零
    private static final int[] ARR6 = {-1, 0, -2, 3, 0, -1, 2, -1};
    private static final int EXPECTED6 = 4; // 子数组 [3, 0, -1, 2]

    // Test 7: 跨越中点的情况 (验证 findMidMaxSubArray)
    private static final int[] ARR7 = {1, -3, 2, 1, -1}; // 最大子数组 [2, 1] 跨越中点 (index 2)
    private static final int EXPECTED7 = 3; // 和为 2 + 1

    // --- JUnit 测试方法 ---
    @Test
    public void testMaxSubArrayDC_Mixed() {
        assertEquals(EXPECTED1, MaxSubArray.maxSubArrayDC(ARR1), "Test 1 - Mixed failed");
    }

    @Test
    public void testMaxSubArrayDC_AllNegative() {
        assertEquals(EXPECTED2, MaxSubArray.maxSubArrayDC(ARR2), "Test 2 - All Negative failed");
    }

    @Test
    public void testMaxSubArrayDC_AllPositive() {
        assertEquals(EXPECTED3, MaxSubArray.maxSubArrayDC(ARR3), "Test 3 - All Positive failed");
    }

    @Test
    public void testMaxSubArrayDC_SinglePositive() {
        assertEquals(EXPECTED4, MaxSubArray.maxSubArrayDC(ARR4), "Test 4 - Single Positive failed");
    }

    @Test
    public void testMaxSubArrayDC_SingleNegative() {
        assertEquals(EXPECTED5, MaxSubArray.maxSubArrayDC(ARR5), "Test 5 - Single Negative failed");
    }

    @Test
    public void testMaxSubArrayDC_WithZeros() {
        assertEquals(EXPECTED6, MaxSubArray.maxSubArrayDC(ARR6), "Test 6 - With Zeros failed");
    }

    @Test
    public void testMaxSubArrayDC_CrossingMidpoint() {
        assertEquals(EXPECTED7, MaxSubArray.maxSubArrayDC(ARR7), "Test 7 - Crossing Midpoint failed");
    }

    @Test
    public void testMaxSubArrayDC_NullAndEmptyArray() {
        // 测试 null 输入
        assertThrows(IllegalArgumentException.class, () -> {
            MaxSubArray.maxSubArrayDC(null);
        }, "Should throw IllegalArgumentException for null array");

        // 测试空数组输入
        assertThrows(IllegalArgumentException.class, () -> {
            MaxSubArray.maxSubArrayDC(new int[]{});
        }, "Should throw IllegalArgumentException for empty array");
    }
}
