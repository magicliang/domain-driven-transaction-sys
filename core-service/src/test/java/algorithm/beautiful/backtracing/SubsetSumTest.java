package algorithm.beautiful.backtracing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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

        // 期望结果：[[7], [2,2,3]]
        assertEquals(2, result.size());

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
     * 测试目标值为0的情况
     */
    @Test
    void testSubsetSumINaiveTargetZero() {
        int[] nums = {1, 2, 3};
        int target = 0;
        List<List<Integer>> result = subsetSum.subsetSumINaive(nums, target);

        // 空子集的和为0
        assertEquals(1, result.size());
        assertTrue(result.contains(Collections.emptyList()));
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
     * 测试负数元素
     */
    @Test
    void testSubsetSumINaiveWithNegativeNumbers() {
        int[] nums = {-1, 2, 3};
        int target = 4;
        List<List<Integer>> result = subsetSum.subsetSumINaive(nums, target);

        // 期望结果：[[2,2], [3,-1,2], ...]
        assertFalse(result.isEmpty());
    }

    /**
     * 测试大数值情况
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
     * 测试sum方法的边界情况
     */
    @Test
    void testSumMethod() {
        // 使用反射测试私有方法
        try {
            java.lang.reflect.Method sumMethod = SubsetSum.class.getDeclaredMethod("sum", List.class);
            sumMethod.setAccessible(true);

            // 测试空列表
            assertEquals(0, sumMethod.invoke(subsetSum, Collections.emptyList()));

            // 测试null输入
            assertEquals(0, sumMethod.invoke(subsetSum, (List<Integer>) null));

            // 测试正常列表
            assertEquals(6, sumMethod.invoke(subsetSum, Arrays.asList(1, 2, 3)));

        } catch (Exception e) {
            fail("测试sum方法失败: " + e.getMessage());
        }
    }

    /**
     * 测试结果的唯一性（可选，用于验证重复结果）
     */
    @Test
    void testResultUniqueness() {
        int[] nums = {2, 2, 3};
        int target = 5;
        List<List<Integer>> result = subsetSum.subsetSumINaive(nums, target);

        // 验证结果中是否有重复
        for (int i = 0; i < result.size(); i++) {
            for (int j = i + 1; j < result.size(); j++) {
                assertNotEquals(result.get(i), result.get(j),
                        "发现重复结果: " + result.get(i));
            }
        }
    }
}