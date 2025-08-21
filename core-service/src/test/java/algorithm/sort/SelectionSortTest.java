package algorithm.sort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * SelectionSort 测试类
 * 测试选择排序算法的各种场景
 */
class SelectionSortTest {

    private SelectionSort selectionSort;

    @BeforeEach
    void setUp() {
        selectionSort = new SelectionSort();
    }

    /**
     * 测试正常情况：随机无序数组
     */
    @Test
    void testSelectionSort_NormalCase() {
        int[] nums = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90}; // 升序排列

        selectionSort.selectionSort(nums);

        assertArrayEquals(expected, nums);
    }

    /**
     * 测试空数组
     */
    @Test
    void testSelectionSort_EmptyArray() {
        int[] nums = {};

        selectionSort.selectionSort(nums);

        assertArrayEquals(new int[]{}, nums);
    }

    /**
     * 测试null数组
     */
    @Test
    void testSelectionSort_NullArray() {
        int[] nums = null;

        assertDoesNotThrow(() -> selectionSort.selectionSort(nums));
    }

    /**
     * 测试单元素数组
     */
    @Test
    void testSelectionSort_SingleElement() {
        int[] nums = {42};

        selectionSort.selectionSort(nums);

        assertArrayEquals(new int[]{42}, nums);
    }

    /**
     * 测试已经有序的数组（升序）
     */
    @Test
    void testSelectionSort_AlreadySorted() {
        int[] nums = {10, 20, 30, 50, 100};

        selectionSort.selectionSort(nums);

        assertArrayEquals(new int[]{10, 20, 30, 50, 100}, nums);
    }

    /**
     * 测试完全逆序的数组（降序）
     */
    @Test
    void testSelectionSort_ReverseSorted() {
        int[] nums = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};

        selectionSort.selectionSort(nums);

        assertArrayEquals(expected, nums);
    }

    /**
     * 测试包含重复元素的数组
     */
    @Test
    void testSelectionSort_DuplicateElements() {
        int[] nums = {5, 2, 8, 2, 9, 1, 5, 5};
        int[] expected = {1, 2, 2, 5, 5, 5, 8, 9};

        selectionSort.selectionSort(nums);

        assertArrayEquals(expected, nums);
    }

    /**
     * 测试包含负数的数组
     */
    @Test
    void testSelectionSort_NegativeNumbers() {
        int[] nums = {-5, 3, -1, 0, 8, -10, 2};
        int[] expected = {-10, -5, -1, 0, 2, 3, 8};  // 升序排列

        selectionSort.selectionSort(nums);

        assertArrayEquals(expected, nums);
    }

    /**
     * 测试所有元素相同的数组
     */
    @Test
    void testSelectionSort_AllSameElements() {
        int[] nums = {7, 7, 7, 7, 7};

        selectionSort.selectionSort(nums);

        assertArrayEquals(new int[]{7, 7, 7, 7, 7}, nums);
    }

    /**
     * 测试大数组的性能
     */
    @Test
    void testSelectionSort_LargeArray() {
        int[] nums = new int[1000];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = nums.length - i; // 逆序填充
        }

        selectionSort.selectionSort(nums);

        // 验证是否按升序排列
        for (int i = 0; i < nums.length - 1; i++) {
            assertTrue(nums[i] <= nums[i + 1],
                    "数组未正确排序，位置 " + i + " 的值 " + nums[i] +
                            " 应该小于等于位置 " + (i + 1) + " 的值 " + nums[i + 1]);
        }
    }
}