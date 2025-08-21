package algorithm.sort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 原地归并排序测试类
 *
 * @author magicliang
 *
 *         date: 2025-08-21 16:00
 */
public class InPlaceMergeSortTest {

    // ========== inPlaceMergeSort 测试用例 ==========

    @Test
    public void testInPlaceMergeSortBasic() {
        int[] arr = {6, 3, 8, 5, 1, 7, 9, 2};
        int[] expected = {1, 2, 3, 5, 6, 7, 8, 9};
        InPlaceMergeSort.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortEmptyArray() {
        int[] arr = {};
        InPlaceMergeSort.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    public void testInPlaceMergeSortSingleElement() {
        int[] arr = {42};
        InPlaceMergeSort.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test
    public void testInPlaceMergeSortTwoElements() {
        int[] arr1 = {2, 1};
        int[] expected1 = {1, 2};
        InPlaceMergeSort.inPlaceMergeSort(arr1, 0, arr1.length - 1);
        assertArrayEquals(expected1, arr1);

        int[] arr2 = {1, 2};
        int[] expected2 = {1, 2};
        InPlaceMergeSort.inPlaceMergeSort(arr2, 0, arr2.length - 1);
        assertArrayEquals(expected2, arr2);
    }

    @Test
    public void testInPlaceMergeSortDuplicates() {
        int[] arr = {3, 2, 1, 3, 2, 3};
        int[] expected = {1, 2, 2, 3, 3, 3};
        InPlaceMergeSort.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortNegativeNumbers() {
        int[] arr = {0, -1, 2, -3, 4, -5};
        int[] expected = {-5, -3, -1, 0, 2, 4};
        InPlaceMergeSort.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        InPlaceMergeSort.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortReverseSorted() {
        int[] arr = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        InPlaceMergeSort.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortOddLength() {
        int[] arr = {3, 1, 5, 2, 4};
        int[] expected = {1, 2, 3, 4, 5};
        InPlaceMergeSort.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortEvenLength() {
        int[] arr = {6, 3, 1, 5, 2, 4};
        int[] expected = {1, 2, 3, 4, 5, 6};
        InPlaceMergeSort.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortMaxIntValues() {
        int[] arr = {0, 1, -1, Integer.MAX_VALUE, Integer.MIN_VALUE};
        int[] expected = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE};
        InPlaceMergeSort.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortLargeArray() {
        int[] arr = new int[1000];
        int[] expected = new int[1000];

        for (int i = 0; i < 1000; i++) {
            arr[i] = 999 - i;
            expected[i] = i;
        }

        InPlaceMergeSort.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortPerformance() {
        int[] arr = new int[10000];
        int[] expected = new int[10000];

        for (int i = 0; i < 10000; i++) {
            arr[i] = 9999 - i;
            expected[i] = i;
        }

        long startTime = System.currentTimeMillis();
        InPlaceMergeSort.inPlaceMergeSort(arr, 0, arr.length - 1);
        long endTime = System.currentTimeMillis();

        assertArrayEquals(expected, arr);
        // 确保在合理时间内完成（10000个元素应该<1000ms）
        assert (endTime - startTime) < 1000 : "Performance test failed - took too long";
    }
}