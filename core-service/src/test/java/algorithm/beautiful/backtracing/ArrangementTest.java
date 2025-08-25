package algorithm.beautiful.backtracing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: Arrangement类的JUnit测试
 *
 * @author magicliang
 *
 *         date: 2025-08-25 20:01
 */
class ArrangementTest {

    @Test
    @DisplayName("测试基本全排列 - 3个不同元素")
    void testArrangeBasic() {
        int[] nums = {1, 2, 3};
        List<List<Integer>> result = Arrangement.arrange(nums);

        assertEquals(6, result.size());
        assertTrue(result.contains(Arrays.asList(1, 2, 3)));
        assertTrue(result.contains(Arrays.asList(1, 3, 2)));
        assertTrue(result.contains(Arrays.asList(2, 1, 3)));
        assertTrue(result.contains(Arrays.asList(2, 3, 1)));
        assertTrue(result.contains(Arrays.asList(3, 1, 2)));
        assertTrue(result.contains(Arrays.asList(3, 2, 1)));
    }

    @Test
    @DisplayName("测试单个元素")
    void testArrangeSingleElement() {
        int[] nums = {5};
        List<List<Integer>> result = Arrangement.arrange(nums);

        assertEquals(1, result.size());
        assertEquals(Arrays.asList(5), result.get(0));
    }

    @Test
    @DisplayName("测试两个元素")
    void testArrangeTwoElements() {
        int[] nums = {1, 2};
        List<List<Integer>> result = Arrangement.arrange(nums);

        assertEquals(2, result.size());
        assertTrue(result.contains(Arrays.asList(1, 2)));
        assertTrue(result.contains(Arrays.asList(2, 1)));
    }

    @Test
    @DisplayName("测试空数组")
    void testArrangeEmptyArray() {
        int[] nums = {};
        List<List<Integer>> result = Arrangement.arrange(nums);

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("测试null输入")
    void testArrangeNullInput() {
        List<List<Integer>> result = Arrangement.arrange(null);

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("测试包含重复元素的去重排列")
    void testArrangeWithDuplicates() {
        int[] nums = {1, 1, 2};
        List<List<Integer>> result = Arrangement.arrange(nums);

        assertEquals(3, result.size());
        assertTrue(result.contains(Arrays.asList(1, 1, 2)));
        assertTrue(result.contains(Arrays.asList(1, 2, 1)));
        assertTrue(result.contains(Arrays.asList(2, 1, 1)));
    }

    @Test
    @DisplayName("测试所有元素相同")
    void testArrangeAllSameElements() {
        int[] nums = {2, 2, 2};
        List<List<Integer>> result = Arrangement.arrange(nums);

        assertEquals(1, result.size());
        assertEquals(Arrays.asList(2, 2, 2), result.get(0));
    }

    @Test
    @DisplayName("测试permutationsII - 基本测试")
    void testPermutationsIIBasic() {
        int[] nums = {1, 2, 3};
        List<List<Integer>> result = Arrangement.permutationsII(nums);

        assertEquals(6, result.size());
        assertTrue(result.contains(Arrays.asList(1, 2, 3)));
        assertTrue(result.contains(Arrays.asList(1, 3, 2)));
        assertTrue(result.contains(Arrays.asList(2, 1, 3)));
        assertTrue(result.contains(Arrays.asList(2, 3, 1)));
        assertTrue(result.contains(Arrays.asList(3, 1, 2)));
        assertTrue(result.contains(Arrays.asList(3, 2, 1)));
    }

    @Test
    @DisplayName("测试permutationsII - 包含重复元素")
    void testPermutationsIIWithDuplicates() {
        int[] nums = {1, 1, 2};
        List<List<Integer>> result = Arrangement.permutationsII(nums);

        assertEquals(3, result.size());
        assertTrue(result.contains(Arrays.asList(1, 1, 2)));
        assertTrue(result.contains(Arrays.asList(1, 2, 1)));
        assertTrue(result.contains(Arrays.asList(2, 1, 1)));
    }

    @Test
    @DisplayName("测试permutationsII - 四个不同元素")
    void testPermutationsIIFourElements() {
        int[] nums = {1, 2, 3, 4};
        List<List<Integer>> result = Arrangement.permutationsII(nums);

        assertEquals(24, result.size());
    }

    @Test
    @DisplayName("测试permutationsII - 空数组")
    void testPermutationsIIEmptyArray() {
        int[] nums = {};
        List<List<Integer>> result = Arrangement.permutationsII(nums);

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("测试arrange和permutationsII结果一致性")
    void testConsistencyBetweenMethods() {
        int[] nums = {1, 2, 3};
        List<List<Integer>> arrangeResult = Arrangement.arrange(nums);
        List<List<Integer>> permutationsIIResult = Arrangement.permutationsII(nums);

        assertEquals(arrangeResult.size(), permutationsIIResult.size());

        // 检查两个结果是否包含相同的排列
        for (List<Integer> arrangement : arrangeResult) {
            assertTrue(permutationsIIResult.contains(arrangement));
        }
    }

    @Test
    @DisplayName("测试复杂重复元素情况")
    void testComplexDuplicates() {
        int[] nums = {1, 1, 1, 2};
        List<List<Integer>> result = Arrangement.arrange(nums);

        // 4! / 3! = 4 种不同的排列
        assertEquals(4, result.size());

        // 验证所有可能的排列
        assertTrue(result.contains(Arrays.asList(1, 1, 1, 2)));
        assertTrue(result.contains(Arrays.asList(1, 1, 2, 1)));
        assertTrue(result.contains(Arrays.asList(1, 2, 1, 1)));
        assertTrue(result.contains(Arrays.asList(2, 1, 1, 1)));
    }

    @Test
    @DisplayName("测试两个重复元素")
    void testTwoPairsOfDuplicates() {
        int[] nums = {1, 1, 2, 2};
        List<List<Integer>> result = Arrangement.arrange(nums);

        // 4! / (2! * 2!) = 6 种不同的排列
        assertEquals(6, result.size());

        // 验证所有可能的排列
        assertTrue(result.contains(Arrays.asList(1, 1, 2, 2)));
        assertTrue(result.contains(Arrays.asList(1, 2, 1, 2)));
        assertTrue(result.contains(Arrays.asList(1, 2, 2, 1)));
        assertTrue(result.contains(Arrays.asList(2, 1, 1, 2)));
        assertTrue(result.contains(Arrays.asList(2, 1, 2, 1)));
        assertTrue(result.contains(Arrays.asList(2, 2, 1, 1)));
    }
}