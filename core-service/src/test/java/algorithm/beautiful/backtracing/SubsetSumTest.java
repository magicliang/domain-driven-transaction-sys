package algorithm.beautiful.backtracing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * SubsetSum类的测试用例
 * 测试子集和问题的各种场景
 */
class SubsetSumTest {

    private SubsetSum subsetSum;

    @BeforeEach
    void setUp() {
        subsetSum = new SubsetSum();
    }

    /**
     * 测试基本功能：正常输入情况
     */
    @Test
    void testSubsetSumINaiveBasic() {
        int[] nums = {2, 3, 6, 7};
        int target = 7;
        List<List<Integer>> result = subsetSum.subsetSumINaive(nums, target);

        assertEquals(4, result.size());

        // 验证结果包含[7]
        assertTrue(result.contains(Collections.singletonList(7)));

        // 验证结果包含[2,2,3]
        assertTrue(result.contains(Arrays.asList(2, 2, 3)));
    }

    /**
     * 测试空数组输入
     */
    @Test
    void testSubsetSumINaiveEmptyArray() {
        int[] nums = {};
        int target = 5;
        List<List<Integer>> result = subsetSum.subsetSumINaive(nums, target);

        assertTrue(result.isEmpty());
    }

    /**
     * 测试null输入
     */
    @Test
    void testSubsetSumINaiveNullInput() {
        int[] nums = null;
        int target = 5;
        List<List<Integer>> result = subsetSum.subsetSumINaive(nums, target);

        assertTrue(result.isEmpty());
    }

    /**
     * 测试无法达到目标值的情况
     */
    @Test
    void testSubsetSumINaiveNoSolution() {
        int[] nums = {2, 4, 6};
        int target = 5;
        List<List<Integer>> result = subsetSum.subsetSumINaive(nums, target);

        assertTrue(result.isEmpty());
    }

    /**
     * 测试包含重复元素的情况
     */
    @Test
    void testSubsetSumINaiveWithDuplicates() {
        int[] nums = {2, 2, 3};
        int target = 7;
        List<List<Integer>> result = subsetSum.subsetSumINaive(nums, target);

        // 期望结果：[[2,2,3]]，但可能有重复
        assertTrue(result.size() >= 1);
        assertTrue(result.contains(Arrays.asList(2, 2, 3)));
    }

    /**
     * 测试单元素数组
     */
    @Test
    void testSubsetSumINaiveSingleElement() {
        int[] nums = {5};
        int target = 5;
        List<List<Integer>> result = subsetSum.subsetSumINaive(nums, target);

        assertEquals(1, result.size());
        assertTrue(result.contains(Collections.singletonList(5)));
    }

    /**
     * 测试需要多个相同元素的情况
     */
    @Test
    void testSubsetSumINaiveMultipleSameElements() {
        int[] nums = {3};
        int target = 9;
        List<List<Integer>> result = subsetSum.subsetSumINaive(nums, target);

        // 期望结果：[[3,3,3]]
        assertEquals(1, result.size());
        assertTrue(result.contains(Arrays.asList(3, 3, 3)));
    }

    /**
     * 测试大数值情况（使用正数避免无限递归）
     */
    @Test
    void testSubsetSumINaiveLargeNumbers() {
        int[] nums = {10, 20, 30};
        int target = 50;
        List<List<Integer>> result = subsetSum.subsetSumINaive(nums, target);

        // 期望结果：[[10,10,10,10,10], [10,10,10,20], [10,20,20], [20,30]]
        assertFalse(result.isEmpty());
    }

    /**
     * 测试目标值为0且数组不含0的情况（安全版本）
     */
    @Test
    void testSubsetSumINaiveTargetZeroSafe() {
        int[] nums = {1, 2, 3};
        int target = 0;
        List<List<Integer>> result = subsetSum.subsetSumINaive(nums, target);

        // 空子集的和为0
        assertEquals(1, result.size());
        assertTrue(result.contains(Collections.emptyList()));
    }
}