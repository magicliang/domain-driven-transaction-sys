package algorithm.sort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 快速排序测试类
 *
 * @author magicliang
 *
 *         date: 2025-08-21 16:00
 */
public class QuickSortTest {

    // ========== quickSort 测试用例 ==========
    // 以下测试用例是为 QuickSort.quickSort(...) 方法添加的

    @Test
    public void testQuickSortBasic() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, QuickSort.quickSort(new int[]{5, 4, 3, 2, 1}));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, QuickSort.quickSort(new int[]{1, 2, 3, 4, 5}));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, QuickSort.quickSort(new int[]{3, 1, 4, 2, 5}));
    }

    @Test
    public void testQuickSortEmptyArray() {
        assertArrayEquals(new int[]{}, QuickSort.quickSort(new int[]{}));
    }

    @Test
    public void testQuickSortSingleElement() {
        assertArrayEquals(new int[]{42}, QuickSort.quickSort(new int[]{42}));
    }

    @Test
    public void testQuickSortTwoElements() {
        assertArrayEquals(new int[]{1, 2}, QuickSort.quickSort(new int[]{2, 1}));
        assertArrayEquals(new int[]{1, 2}, QuickSort.quickSort(new int[]{1, 2}));
    }

    @Test
    public void testQuickSortDuplicateElements() {
        assertArrayEquals(new int[]{1, 2, 2, 3, 3, 3}, QuickSort.quickSort(new int[]{3, 2, 1, 3, 2, 3}));
        assertArrayEquals(new int[]{5, 5, 5, 5}, QuickSort.quickSort(new int[]{5, 5, 5, 5}));
    }

    @Test
    public void testQuickSortNegativeNumbers() {
        assertArrayEquals(new int[]{-5, -3, -1, 0, 2, 4}, QuickSort.quickSort(new int[]{0, -1, 2, -3, 4, -5}));
        assertArrayEquals(new int[]{-10, -5, 0, 5, 10}, QuickSort.quickSort(new int[]{-5, 10, 0, -10, 5}));
    }

    @Test
    public void testQuickSortAllSameElements() {
        assertArrayEquals(new int[]{7, 7, 7, 7, 7}, QuickSort.quickSort(new int[]{7, 7, 7, 7, 7}));
    }

    @Test
    public void testQuickSortLargeNumbers() {
        assertArrayEquals(new int[]{1000000, 2000000, 3000000},
                QuickSort.quickSort(new int[]{3000000, 1000000, 2000000}));
    }

    @Test
    public void testQuickSortAlreadySorted() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                QuickSort.quickSort(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
    }

    @Test
    public void testQuickSortReverseSorted() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                QuickSort.quickSort(new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1}));
    }

    @Test
    public void testQuickSortOddLengthArray() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, QuickSort.quickSort(new int[]{3, 1, 5, 2, 4}));
    }

    @Test
    public void testQuickSortEvenLengthArray() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, QuickSort.quickSort(new int[]{6, 3, 1, 5, 2, 4}));
    }

    @Test
    public void testQuickSortZeroAndNegative() {
        assertArrayEquals(new int[]{-10, -5, 0, 0, 5, 10},
                QuickSort.quickSort(new int[]{0, -5, 10, -10, 0, 5}));
    }

    @Test
    public void testQuickSortMaxIntValues() {
        assertArrayEquals(new int[]{Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE},
                QuickSort.quickSort(new int[]{0, 1, -1, Integer.MAX_VALUE, Integer.MIN_VALUE}));
    }

    @Test
    public void testQuickSortVeryLargeArray() {
        int[] input = new int[1000];
        int[] expected = new int[1000];
        for (int i = 0; i < 1000; i++) {
            input[i] = 999 - i;
            expected[i] = i;
        }
        assertArrayEquals(expected, QuickSort.quickSort(input));
    }

    @Test
    public void testQuickSortSingleElementTypes() {
        assertArrayEquals(new int[]{0}, QuickSort.quickSort(new int[]{0}));
        assertArrayEquals(new int[]{-1}, QuickSort.quickSort(new int[]{-1}));
        assertArrayEquals(new int[]{Integer.MAX_VALUE}, QuickSort.quickSort(new int[]{Integer.MAX_VALUE}));
        assertArrayEquals(new int[]{Integer.MIN_VALUE}, QuickSort.quickSort(new int[]{Integer.MIN_VALUE}));
    }

    @Test
    public void testQuickSortPerformanceWithLargeArray() {
        int[] input = new int[10000];
        int[] expected = new int[10000];
        for (int i = 0; i < 10000; i++) {
            input[i] = 9999 - i;
            expected[i] = i;
        }
        long startTime = System.currentTimeMillis();
        int[] result = QuickSort.quickSort(input);
        long endTime = System.currentTimeMillis();
        assertArrayEquals(expected, result);
        // 确保在合理时间内完成（10000个元素应该<1000ms）
        assert (endTime - startTime) < 1000 : "Performance test failed - took too long";
    }

    @Test
    public void testQuickSortStability() {
        // 由于我们处理的是基本类型int，稳定性测试不适用
        // 但如果是对象排序，这里可以测试稳定性
        // 对于基础类型，我们只验证排序结果
        assertArrayEquals(new int[]{1, 1, 2, 2, 3, 3}, QuickSort.quickSort(new int[]{1, 2, 1, 3, 2, 3}));
    }
}