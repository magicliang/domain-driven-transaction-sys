package algorithm.sort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * InsertionSort 测试类
 *
 * @author magicliang
 * @date 2025-08-21
 */
public class InsertionSortTest {

    private final InsertionSort insertionSort = new InsertionSort();

    @Test
    public void testInsertionSort_NormalCase() {
        // 测试正常情况：随机数组
        int[] nums = {5, 2, 8, 1, 9, 3};
        insertionSort.insertionSort(nums);
        assertArrayEquals(new int[]{1, 2, 3, 5, 8, 9}, nums);
    }

    @Test
    public void testInsertionSort_EmptyArray() {
        // 测试空数组
        int[] nums = {};
        insertionSort.insertionSort(nums);
        assertArrayEquals(new int[]{}, nums);
    }

    @Test
    public void testInsertionSort_SingleElement() {
        // 测试单元素数组
        int[] nums = {42};
        insertionSort.insertionSort(nums);
        assertArrayEquals(new int[]{42}, nums);
    }

    @Test
    public void testInsertionSort_AlreadySorted() {
        // 测试已排序数组（最好情况）
        int[] nums = {1, 2, 3, 4, 5};
        insertionSort.insertionSort(nums);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, nums);
    }

    @Test
    public void testInsertionSort_ReverseSorted() {
        // 测试逆序数组（最坏情况）
        int[] nums = {5, 4, 3, 2, 1};
        insertionSort.insertionSort(nums);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, nums);
    }

    @Test
    public void testInsertionSort_DuplicateElements() {
        // 测试包含重复元素的数组
        int[] nums = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        insertionSort.insertionSort(nums);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9}, nums);
    }

    @Test
    public void testInsertionSort_AllSameElements() {
        // 测试所有元素相同的数组
        int[] nums = {7, 7, 7, 7, 7};
        insertionSort.insertionSort(nums);
        assertArrayEquals(new int[]{7, 7, 7, 7, 7}, nums);
    }

    @Test
    public void testInsertionSort_NegativeNumbers() {
        // 测试包含负数的数组
        int[] nums = {-5, 3, -2, 0, 8, -1, 4};
        insertionSort.insertionSort(nums);
        assertArrayEquals(new int[]{-5, -2, -1, 0, 3, 4, 8}, nums);
    }

    @Test
    public void testInsertionSort_LargeArray() {
        // 测试较大数组
        int[] nums = {100, 50, 75, 25, 90, 10, 60, 80, 30, 40};
        insertionSort.insertionSort(nums);
        assertArrayEquals(new int[]{10, 25, 30, 40, 50, 60, 75, 80, 90, 100}, nums);
    }

    @Test
    public void testInsertionSort_NullArray() {
        // 测试null数组
        int[] nums = null;
        assertDoesNotThrow(() -> insertionSort.insertionSort(nums));
    }

    @Test
    public void testInsertionSort_Performance() {
        // 测试性能：较大随机数组
        int[] nums = new int[1000];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = (int) (Math.random() * 1000);
        }

        long startTime = System.nanoTime();
        insertionSort.insertionSort(nums);
        long endTime = System.nanoTime();

        // 验证排序正确性
        for (int i = 1; i < nums.length; i++) {
            assertTrue(nums[i - 1] <= nums[i], "数组未正确排序");
        }

        // 打印执行时间（可选）
        System.out.println("1000个元素的插入排序耗时: " + (endTime - startTime) / 1000000.0 + " ms");
    }
}