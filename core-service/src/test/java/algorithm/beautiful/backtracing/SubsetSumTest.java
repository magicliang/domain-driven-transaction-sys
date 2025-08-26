package algorithm.beautiful.backtracing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * SubsetSum类的测试类
 * 测试subsetSumNoDuplicate方法的各种场景
 */
public class SubsetSumTest {

    private SubsetSum subsetSum;

    @BeforeEach
    void setUp() {
        subsetSum = new SubsetSum();
    }

    /**
     * 测试subsetSumNoDuplicate基本功能：不重复使用元素
     */
    @Test
    void testSubsetSumNoDuplicateCombinationBasic() {
        int[] nums = {2, 3, 6, 7};
        int target = 7;
        List<List<Integer>> result = subsetSum.subsetSumNoDuplicateCombination(nums, target);

        // 期望结果：[[7]]
        assertEquals(1, result.size());
        assertTrue(result.contains(Collections.singletonList(7)));
    }

    /**
     * 测试subsetSumNoDuplicate处理重复元素：避免重复组合
     */
    @Test
    void testSubsetSumNoDuplicateCombinationWithDuplicates() {
        int[] nums = {2, 2, 3, 3, 6, 7};
        int target = 7;
        List<List<Integer>> result = subsetSum.subsetSumNoDuplicateCombination(nums, target);

        // 期望结果：[[7], [2,2,3]] 每个组合只出现一次
        assertEquals(2, result.size());
        assertTrue(result.contains(Collections.singletonList(7)));
        assertTrue(result.contains(Arrays.asList(2, 2, 3)));
    }

    /**
     * 测试subsetSumNoDuplicate：多个有效解
     */
    @Test
    void testSubsetSumNoDuplicateCombinationMultipleSolutions() {
        int[] nums = {1, 2, 3, 4, 5};
        int target = 5;
        List<List<Integer>> result = subsetSum.subsetSumNoDuplicateCombination(nums, target);

        // 期望结果：[[5], [1,4], [2,3]]
        assertEquals(3, result.size());
        assertTrue(result.contains(Collections.singletonList(5)));
        assertTrue(result.contains(Arrays.asList(1, 4)));
        assertTrue(result.contains(Arrays.asList(2, 3)));
    }

    /**
     * 测试subsetSumNoDuplicate：空数组
     */
    @Test
    void testSubsetSumNoDuplicateCombinationEmptyArray() {
        int[] nums = {};
        int target = 5;
        List<List<Integer>> result = subsetSum.subsetSumNoDuplicateCombination(nums, target);

        assertTrue(result.isEmpty());
    }

    /**
     * 测试subsetSumNoDuplicate：null输入
     */
    @Test
    void testSubsetSumNoDuplicateCombinationNullInput() {
        int[] nums = null;
        int target = 5;
        List<List<Integer>> result = subsetSum.subsetSumNoDuplicateCombination(nums, target);

        assertTrue(result.isEmpty());
    }

    /**
     * 测试subsetSumNoDuplicate：无解情况
     */
    @Test
    void testSubsetSumNoDuplicateNoSolutionCombination() {
        int[] nums = {2, 4, 6};
        int target = 5;
        List<List<Integer>> result = subsetSum.subsetSumNoDuplicateCombination(nums, target);

        assertTrue(result.isEmpty());
    }

    /**
     * 测试subsetSumNoDuplicate：单元素匹配
     */
    @Test
    void testSubsetSumNoDuplicateCombinationSingleElement() {
        int[] nums = {5};
        int target = 5;
        List<List<Integer>> result = subsetSum.subsetSumNoDuplicateCombination(nums, target);

        assertEquals(1, result.size());
        assertTrue(result.contains(Collections.singletonList(5)));
    }

    /**
     * 测试subsetSumNoDuplicate：包含负数（应抛出异常）
     */
    @Test
    void testSubsetSumNoDuplicateCombinationNegativeNumbers() {
        int[] nums = {-1, 2, 3};
        int target = 5;

        assertThrows(IllegalArgumentException.class, () -> {
            subsetSum.subsetSumNoDuplicateCombination(nums, target);
        });
    }

    /**
     * 测试subsetSumNoDuplicate：包含零（应抛出异常）
     */
    @Test
    void testSubsetSumNoDuplicateCombinationContainsZero() {
        int[] nums = {0, 2, 3};
        int target = 5;

        assertThrows(IllegalArgumentException.class, () -> {
            subsetSum.subsetSumNoDuplicateCombination(nums, target);
        });
    }

    /**
     * 测试subsetSumNoDuplicate：复杂重复元素场景
     */
    @Test
    void testSubsetSumNoDuplicateCombinationComplexDuplicates() {
        int[] nums = {1, 1, 1, 2, 2, 3};
        int target = 4;
        List<List<Integer>> result = subsetSum.subsetSumNoDuplicateCombination(nums, target);

        // 期望结果：[[1,3], [1,1,2], [2,2]] 每个组合只出现一次
        assertEquals(3, result.size());
        assertTrue(result.contains(Arrays.asList(1, 3)));
        assertTrue(result.contains(Arrays.asList(1, 1, 2)));
        assertTrue(result.contains(Arrays.asList(2, 2)));
    }

    /**
     * 测试subsetSumNoDuplicate：目标值为0
     */
    @Test
    void testSubsetSumNoDuplicateCombinationTargetZero() {
        int[] nums = {1, 2, 3};
        int target = 0;
        List<List<Integer>> result = subsetSum.subsetSumNoDuplicateCombination(nums, target);

        // 空子集的和为0
        assertEquals(1, result.size());
        assertTrue(result.contains(Collections.emptyList()));
    }
}