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
 *         date: 2025-08-21 16:00
 */
public class MergeSortTest {

    @Test
    public void testBasicSort() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, MergeSort.mergeSort(new int[]{5, 4, 3, 2, 1}));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, MergeSort.mergeSort(new int[]{1, 2, 3, 4, 5}));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, MergeSort.mergeSort(new int[]{3, 1, 4, 2, 5}));
    }

    @Test
    public void testEmptyArray() {
        assertArrayEquals(new int[]{}, MergeSort.mergeSort(new int[]{}));
    }

    @Test
    public void testSingleElement() {
        assertArrayEquals(new int[]{42}, MergeSort.mergeSort(new int[]{42}));
    }

    @Test
    public void testTwoElements() {
        assertArrayEquals(new int[]{1, 2}, MergeSort.mergeSort(new int[]{2, 1}));
        assertArrayEquals(new int[]{1, 2}, MergeSort.mergeSort(new int[]{1, 2}));
    }

    @Test
    public void testDuplicateElements() {
        assertArrayEquals(new int[]{1, 2, 2, 3, 3, 3}, MergeSort.mergeSort(new int[]{3, 2, 1, 3, 2, 3}));
        assertArrayEquals(new int[]{5, 5, 5, 5}, MergeSort.mergeSort(new int[]{5, 5, 5, 5}));
    }

    @Test
    public void testNegativeNumbers() {
        assertArrayEquals(new int[]{-5, -3, -1, 0, 2, 4}, MergeSort.mergeSort(new int[]{0, -1, 2, -3, 4, -5}));
        assertArrayEquals(new int[]{-10, -5, 0, 5, 10}, MergeSort.mergeSort(new int[]{-5, 10, 0, -10, 5}));
    }

    @Test
    public void testAllSameElements() {
        assertArrayEquals(new int[]{7, 7, 7, 7, 7}, MergeSort.mergeSort(new int[]{7, 7, 7, 7, 7}));
    }

    @Test
    public void testLargeNumbers() {
        assertArrayEquals(new int[]{1000000, 2000000, 3000000},
                MergeSort.mergeSort(new int[]{3000000, 1000000, 2000000}));
    }

    @Test
    public void testAlreadySorted() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                MergeSort.mergeSort(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
    }

    @Test
    public void testReverseSorted() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                MergeSort.mergeSort(new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1}));
    }

    @Test
    public void testOddLengthArray() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, MergeSort.mergeSort(new int[]{3, 1, 5, 2, 4}));
    }

    @Test
    public void testEvenLengthArray() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, MergeSort.mergeSort(new int[]{6, 3, 1, 5, 2, 4}));
    }

    @Test
    public void testZeroAndNegative() {
        assertArrayEquals(new int[]{-10, -5, 0, 0, 5, 10},
                MergeSort.mergeSort(new int[]{0, -5, 10, -10, 0, 5}));
    }

    @Test
    public void testMaxIntValues() {
        assertArrayEquals(new int[]{Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE},
                MergeSort.mergeSort(new int[]{0, 1, -1, Integer.MAX_VALUE, Integer.MIN_VALUE}));
    }

    @Test
    public void testVeryLargeArray() {
        int[] input = new int[1000];
        int[] expected = new int[1000];

        for (int i = 0; i < 1000; i++) {
            input[i] = 999 - i;
            expected[i] = i;
        }

        assertArrayEquals(expected, MergeSort.mergeSort(input));
    }

    @Test
    public void testSingleElementTypes() {
        assertArrayEquals(new int[]{0}, MergeSort.mergeSort(new int[]{0}));
        assertArrayEquals(new int[]{-1}, MergeSort.mergeSort(new int[]{-1}));
        assertArrayEquals(new int[]{Integer.MAX_VALUE}, MergeSort.mergeSort(new int[]{Integer.MAX_VALUE}));
        assertArrayEquals(new int[]{Integer.MIN_VALUE}, MergeSort.mergeSort(new int[]{Integer.MIN_VALUE}));
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
        int[] result = MergeSort.mergeSort(input);
        long endTime = System.currentTimeMillis();

        assertArrayEquals(expected, result);
        // 确保在合理时间内完成（10000个元素应该<100ms）
        assert (endTime - startTime) < 1000 : "Performance test failed - took too long";
    }

    @Test
    public void testStability() {
        // 由于我们处理的是基本类型int，稳定性测试不适用
        // 但如果是对象排序，这里可以测试稳定性
        assertArrayEquals(new int[]{1, 1, 2, 2, 3, 3}, MergeSort.mergeSort(new int[]{1, 2, 1, 3, 2, 3}));
    }

    // ========== merge2Arrays 测试用例 ==========

    @Test
    public void testMerge2ArraysBasic() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, MergeSort.merge2Arrays(new int[]{1, 3, 5}, new int[]{2, 4, 6}));
        assertArrayEquals(new int[]{1, 2, 3, 4}, MergeSort.merge2Arrays(new int[]{1, 2}, new int[]{3, 4}));
    }

    @Test
    public void testMerge2ArraysEmpty() {
        assertArrayEquals(new int[]{1, 2, 3}, MergeSort.merge2Arrays(new int[]{}, new int[]{1, 2, 3}));
        assertArrayEquals(new int[]{1, 2, 3}, MergeSort.merge2Arrays(new int[]{1, 2, 3}, new int[]{}));
        assertArrayEquals(new int[]{}, MergeSort.merge2Arrays(new int[]{}, new int[]{}));
    }

    @Test
    public void testMerge2ArraysSingleElement() {
        assertArrayEquals(new int[]{1, 2}, MergeSort.merge2Arrays(new int[]{1}, new int[]{2}));
        assertArrayEquals(new int[]{1}, MergeSort.merge2Arrays(new int[]{1}, new int[]{}));
        assertArrayEquals(new int[]{1}, MergeSort.merge2Arrays(new int[]{}, new int[]{1}));
    }

    @Test
    public void testMerge2ArraysDuplicates() {
        assertArrayEquals(new int[]{1, 1, 2, 2, 3, 3}, MergeSort.merge2Arrays(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertArrayEquals(new int[]{1, 1, 1, 2, 2, 2}, MergeSort.merge2Arrays(new int[]{1, 1, 1}, new int[]{2, 2, 2}));
    }

    @Test
    public void testMerge2ArraysNegative() {
        assertArrayEquals(new int[]{-5, -3, -1, 0, 2, 4},
                MergeSort.merge2Arrays(new int[]{-5, -3, -1}, new int[]{0, 2, 4}));
        assertArrayEquals(new int[]{-10, -5, 0, 5, 10},
                MergeSort.merge2Arrays(new int[]{-10, -5, 0}, new int[]{5, 10}));
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

        assertArrayEquals(expected, MergeSort.merge2Arrays(a, b));
    }
}