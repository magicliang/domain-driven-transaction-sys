package algorithm.sort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 归并排序测试类
 *
 * @author magicliang
 *
 *         date: 2025-07-31 14:38
 */
public class SorterTest {

    @Test
    public void testBasicSort() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, Sorter.mergeSort(new int[]{5, 4, 3, 2, 1}));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, Sorter.mergeSort(new int[]{1, 2, 3, 4, 5}));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, Sorter.mergeSort(new int[]{3, 1, 4, 2, 5}));
    }

    @Test
    public void testEmptyArray() {
        assertArrayEquals(new int[]{}, Sorter.mergeSort(new int[]{}));
    }

    @Test
    public void testSingleElement() {
        assertArrayEquals(new int[]{42}, Sorter.mergeSort(new int[]{42}));
    }

    @Test
    public void testTwoElements() {
        assertArrayEquals(new int[]{1, 2}, Sorter.mergeSort(new int[]{2, 1}));
        assertArrayEquals(new int[]{1, 2}, Sorter.mergeSort(new int[]{1, 2}));
    }

    @Test
    public void testDuplicateElements() {
        assertArrayEquals(new int[]{1, 2, 2, 3, 3, 3}, Sorter.mergeSort(new int[]{3, 2, 1, 3, 2, 3}));
        assertArrayEquals(new int[]{5, 5, 5, 5}, Sorter.mergeSort(new int[]{5, 5, 5, 5}));
    }

    @Test
    public void testNegativeNumbers() {
        assertArrayEquals(new int[]{-5, -3, -1, 0, 2, 4}, Sorter.mergeSort(new int[]{0, -1, 2, -3, 4, -5}));
        assertArrayEquals(new int[]{-10, -5, 0, 5, 10}, Sorter.mergeSort(new int[]{-5, 10, 0, -10, 5}));
    }

    @Test
    public void testAllSameElements() {
        assertArrayEquals(new int[]{7, 7, 7, 7, 7}, Sorter.mergeSort(new int[]{7, 7, 7, 7, 7}));
    }

    @Test
    public void testLargeNumbers() {
        assertArrayEquals(new int[]{1000000, 2000000, 3000000}, Sorter.mergeSort(new int[]{3000000, 1000000, 2000000}));
    }

    @Test
    public void testAlreadySorted() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                Sorter.mergeSort(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
    }

    @Test
    public void testReverseSorted() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                Sorter.mergeSort(new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1}));
    }

    @Test
    public void testOddLengthArray() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, Sorter.mergeSort(new int[]{3, 1, 5, 2, 4}));
    }

    @Test
    public void testEvenLengthArray() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, Sorter.mergeSort(new int[]{6, 3, 1, 5, 2, 4}));
    }

    @Test
    public void testZeroAndNegative() {
        assertArrayEquals(new int[]{-10, -5, 0, 0, 5, 10},
                Sorter.mergeSort(new int[]{0, -5, 10, -10, 0, 5}));
    }

    @Test
    public void testMaxIntValues() {
        assertArrayEquals(new int[]{Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE},
                Sorter.mergeSort(new int[]{0, 1, -1, Integer.MAX_VALUE, Integer.MIN_VALUE}));
    }

    @Test
    public void testVeryLargeArray() {
        int[] input = new int[1000];
        int[] expected = new int[1000];

        for (int i = 0; i < 1000; i++) {
            input[i] = 999 - i;
            expected[i] = i;
        }

        assertArrayEquals(expected, Sorter.mergeSort(input));
    }

    @Test
    public void testSingleElementTypes() {
        assertArrayEquals(new int[]{0}, Sorter.mergeSort(new int[]{0}));
        assertArrayEquals(new int[]{-1}, Sorter.mergeSort(new int[]{-1}));
        assertArrayEquals(new int[]{Integer.MAX_VALUE}, Sorter.mergeSort(new int[]{Integer.MAX_VALUE}));
        assertArrayEquals(new int[]{Integer.MIN_VALUE}, Sorter.mergeSort(new int[]{Integer.MIN_VALUE}));
    }

    @Test
    public void testMerge2ArraysBasic() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, Sorter.merge2Arrays(new int[]{1, 3, 5}, new int[]{2, 4, 6}));
        assertArrayEquals(new int[]{1, 2, 3, 4}, Sorter.merge2Arrays(new int[]{1, 2}, new int[]{3, 4}));
    }

    @Test
    public void testMerge2ArraysEmpty() {
        assertArrayEquals(new int[]{1, 2, 3}, Sorter.merge2Arrays(new int[]{}, new int[]{1, 2, 3}));
        assertArrayEquals(new int[]{1, 2, 3}, Sorter.merge2Arrays(new int[]{1, 2, 3}, new int[]{}));
        assertArrayEquals(new int[]{}, Sorter.merge2Arrays(new int[]{}, new int[]{}));
    }

    @Test
    public void testMerge2ArraysSingleElement() {
        assertArrayEquals(new int[]{1, 2}, Sorter.merge2Arrays(new int[]{1}, new int[]{2}));
        assertArrayEquals(new int[]{1}, Sorter.merge2Arrays(new int[]{1}, new int[]{}));
        assertArrayEquals(new int[]{1}, Sorter.merge2Arrays(new int[]{}, new int[]{1}));
    }

    @Test
    public void testMerge2ArraysDuplicates() {
        assertArrayEquals(new int[]{1, 1, 2, 2, 3, 3}, Sorter.merge2Arrays(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertArrayEquals(new int[]{1, 1, 1, 2, 2, 2}, Sorter.merge2Arrays(new int[]{1, 1, 1}, new int[]{2, 2, 2}));
    }

    @Test
    public void testMerge2ArraysNegative() {
        assertArrayEquals(new int[]{-5, -3, -1, 0, 2, 4},
                Sorter.merge2Arrays(new int[]{-5, -3, -1}, new int[]{0, 2, 4}));
        assertArrayEquals(new int[]{-10, -5, 0, 5, 10}, Sorter.merge2Arrays(new int[]{-10, -5, 0}, new int[]{5, 10}));
    }

    @Test
    public void testMerge2ArraysLarge() {
        int[] a = new int[500];
        int[] b = new int[500];
        int[] expected = new int[1000];

        for (int i = 0; i < 500; i++) {
            a[i] = i * 2;
            b[i] = i * 2 + 1;
            expected[i * 2] = i * 2;
            expected[i * 2 + 1] = i * 2 + 1;
        }

        assertArrayEquals(expected, Sorter.merge2Arrays(a, b));
    }

    @Test
    public void testPerformanceWithLargeArray() {
        int[] input = new int[10000];
        int[] expected = new int[10000];

        for (int i = 0; i < 10000; i++) {
            input[i] = 9999 - i;
            expected[i] = i;
        }

        long startTime = System.currentTimeMillis();
        int[] result = Sorter.mergeSort(input);
        long endTime = System.currentTimeMillis();

        assertArrayEquals(expected, result);
        // 确保在合理时间内完成（10000个元素应该<100ms）
        assert (endTime - startTime) < 1000 : "Performance test failed - took too long";
    }

    @Test
    public void testStability() {
        // 由于我们处理的是基本类型int，稳定性测试不适用
        // 但如果是对象排序，这里可以测试稳定性
        assertArrayEquals(new int[]{1, 1, 2, 2, 3, 3}, Sorter.mergeSort(new int[]{1, 2, 1, 3, 2, 3}));
    }

    // ========== inPlaceMergeSort 测试用例 ==========

    @Test
    public void testInPlaceMergeSortBasic() {
        int[] arr = {6, 3, 8, 5, 1, 7, 9, 2};
        int[] expected = {1, 2, 3, 5, 6, 7, 8, 9};
        Sorter.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortEmptyArray() {
        int[] arr = {};
        Sorter.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    public void testInPlaceMergeSortSingleElement() {
        int[] arr = {42};
        Sorter.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test
    public void testInPlaceMergeSortTwoElements() {
        int[] arr1 = {2, 1};
        int[] expected1 = {1, 2};
        Sorter.inPlaceMergeSort(arr1, 0, arr1.length - 1);
        assertArrayEquals(expected1, arr1);

        int[] arr2 = {1, 2};
        int[] expected2 = {1, 2};
        Sorter.inPlaceMergeSort(arr2, 0, arr2.length - 1);
        assertArrayEquals(expected2, arr2);
    }

    @Test
    public void testInPlaceMergeSortDuplicates() {
        int[] arr = {3, 2, 1, 3, 2, 3};
        int[] expected = {1, 2, 2, 3, 3, 3};
        Sorter.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortNegativeNumbers() {
        int[] arr = {0, -1, 2, -3, 4, -5};
        int[] expected = {-5, -3, -1, 0, 2, 4};
        Sorter.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Sorter.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortReverseSorted() {
        int[] arr = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Sorter.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortOddLength() {
        int[] arr = {3, 1, 5, 2, 4};
        int[] expected = {1, 2, 3, 4, 5};
        Sorter.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortEvenLength() {
        int[] arr = {6, 3, 1, 5, 2, 4};
        int[] expected = {1, 2, 3, 4, 5, 6};
        Sorter.inPlaceMergeSort(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testInPlaceMergeSortMaxIntValues() {
        int[] arr = {0, 1, -1, Integer.MAX_VALUE, Integer.MIN_VALUE};
        int[] expected = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE};
        Sorter.inPlaceMergeSort(arr, 0, arr.length - 1);
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

        Sorter.inPlaceMergeSort(arr, 0, arr.length - 1);
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
        Sorter.inPlaceMergeSort(arr, 0, arr.length - 1);
        long endTime = System.currentTimeMillis();

        assertArrayEquals(expected, arr);
        // 确保在合理时间内完成（10000个元素应该<1000ms）
        assert (endTime - startTime) < 1000 : "Performance test failed - took too long";
    }
}