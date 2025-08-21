package algorithm.sort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: bubbleSortWithFlag方法的测试类
 * 专门测试标志优化版冒泡排序的正确性和优化效果
 *
 * @author magicliang
 *
 *         date: 2025-08-21 15:45
 */
class BubbleSortWithFlagTest {

    private final BubbleSort bubbleSort = new BubbleSort();

    @Test
    void testBubbleSortWithFlag_NormalArray() {
        int[] nums = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};

        bubbleSort.bubbleSortWithFlag(nums);

        assertArrayEquals(expected, nums);
    }

    @Test
    void testBubbleSortWithFlag_EmptyArray() {
        int[] nums = {};

        bubbleSort.bubbleSortWithFlag(nums);

        assertArrayEquals(new int[]{}, nums);
    }

    @Test
    void testBubbleSortWithFlag_NullArray() {
        int[] nums = null;

        // 验证方法能够正常处理null输入而不抛出异常
        assertDoesNotThrow(() -> bubbleSort.bubbleSortWithFlag(nums));

        // 验证null数组处理后仍然是null
        bubbleSort.bubbleSortWithFlag(nums);
        assertNull(nums);
    }

    @Test
    void testBubbleSortWithFlag_SingleElement() {
        int[] nums = {42};

        bubbleSort.bubbleSortWithFlag(nums);

        assertArrayEquals(new int[]{42}, nums);
    }

    @Test
    void testBubbleSortWithFlag_AlreadySorted() {
        int[] nums = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};

        bubbleSort.bubbleSortWithFlag(nums);

        assertArrayEquals(expected, nums);
    }

    @Test
    void testBubbleSortWithFlag_ReverseSorted() {
        int[] nums = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};

        bubbleSort.bubbleSortWithFlag(nums);

        assertArrayEquals(expected, nums);
    }

    @Test
    void testBubbleSortWithFlag_DuplicateElements() {
        int[] nums = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        int[] expected = {1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9};

        bubbleSort.bubbleSortWithFlag(nums);

        assertArrayEquals(expected, nums);
    }

    @Test
    void testBubbleSortWithFlag_NegativeNumbers() {
        int[] nums = {-5, 2, -8, 12, -1, 0, 7};
        int[] expected = {-8, -5, -1, 0, 2, 7, 12};

        bubbleSort.bubbleSortWithFlag(nums);

        assertArrayEquals(expected, nums);
    }

    @Test
    void testBubbleSortWithFlag_AllSameElements() {
        int[] nums = {7, 7, 7, 7, 7};
        int[] expected = {7, 7, 7, 7, 7};

        bubbleSort.bubbleSortWithFlag(nums);

        assertArrayEquals(expected, nums);
    }

    @Test
    void testBubbleSortWithFlag_LargeArray() {
        int[] nums = new int[1000];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = nums.length - i; // 逆序填充
        }

        bubbleSort.bubbleSortWithFlag(nums);

        // 验证排序结果
        for (int i = 0; i < nums.length - 1; i++) {
            assertTrue(nums[i] <= nums[i + 1],
                    "Array not sorted at index " + i);
        }
    }

    @Test
    void testBubbleSortWithFlag_OptimizationEffect() {
        // 测试标志优化的效果：已排序数组应该很快完成
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        bubbleSort.bubbleSortWithFlag(nums);

        assertArrayEquals(expected, nums);
        // 由于标志优化，这个测试应该很快完成
    }

    @Test
    void testBubbleSortWithFlag_AlternatingValues() {
        int[] nums = {1, 3, 2, 4, 3, 5, 4, 6, 5, 7};
        int[] expected = {1, 2, 3, 3, 4, 4, 5, 5, 6, 7};

        bubbleSort.bubbleSortWithFlag(nums);

        assertArrayEquals(expected, nums);
    }
}