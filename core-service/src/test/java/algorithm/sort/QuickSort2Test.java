package algorithm.sort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class QuickSort2Test {

    @Test
    void testQuickSort2() {
        int[] arr = {3, 2, 1, 5, 6, 4};
        int[] expected = {1, 2, 3, 4, 5, 6};
        QuickSort.quickSort2(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort2WithDuplicates() {
        int[] arr = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int[] expected = {1, 2, 2, 3, 3, 4, 5, 5, 6};
        QuickSort.quickSort2(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort2WithEmptyArray() {
        int[] arr = {};
        QuickSort.quickSort2(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void testQuickSort2WithSingleElement() {
        int[] arr = {42};
        QuickSort.quickSort2(arr);
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test
    void testQuickSort2WithAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        QuickSort.quickSort2(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort2WithReverseSorted() {
        int[] arr = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        QuickSort.quickSort2(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort2WithNullArray() {
        int[] arr = null;
        QuickSort.quickSort2(arr);
        assertNull(arr);
    }

    @Test
    void testQuickSort3() {
        int[] arr = {3, 2, 1, 5, 6, 4};
        int[] expected = {1, 2, 3, 4, 5, 6};
        QuickSort.quickSort3(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort3WithDuplicates() {
        int[] arr = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int[] expected = {1, 2, 2, 3, 3, 4, 5, 5, 6};
        QuickSort.quickSort3(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort3WithEmptyArray() {
        int[] arr = {};
        QuickSort.quickSort3(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void testQuickSort3WithSingleElement() {
        int[] arr = {42};
        QuickSort.quickSort3(arr);
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test
    void testQuickSort3WithAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        QuickSort.quickSort3(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort3WithReverseSorted() {
        int[] arr = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        QuickSort.quickSort3(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort3WithNullArray() {
        int[] arr = null;
        QuickSort.quickSort3(arr);
        assertNull(arr);
    }

    @Test
    void testQuickSort3WithAllEqual() {
        int[] arr = {5, 5, 5, 5, 5};
        int[] expected = {5, 5, 5, 5, 5};
        QuickSort.quickSort3(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort3LargeArray() {
        int[] arr = new int[1000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr.length - i; // 逆序排列
        }
        QuickSort.quickSort3(arr);

        // 验证排序结果
        for (int i = 1; i < arr.length; i++) {
            assertTrue(arr[i - 1] <= arr[i], "Array should be sorted in ascending order");
        }
    }

    @Test
    void testQuickSort3PerformanceComparison() {
        // 测试三数取中法在不同情况下的表现
        int[] arr1 = {1, 2, 3, 4, 5, 6, 7, 8, 9}; // 已排序
        int[] arr2 = arr1.clone();

        QuickSort.quickSort3(arr1);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, arr1);

        // 逆序数组
        int[] arr3 = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        QuickSort.quickSort3(arr3);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, arr3);
    }

    @Test
    void testPartitionMedianBasic() {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6};
        int pivotIndex = QuickSort.partitionMedian(arr, 0, arr.length - 1);
        
        // 验证pivot左边的元素都小于等于pivot
        int pivotValue = arr[pivotIndex];
        for (int i = 0; i < pivotIndex; i++) {
            assertTrue(arr[i] <= pivotValue, "Element at index " + i + " should be <= pivot");
        }

        // 验证pivot右边的元素都大于等于pivot
        for (int i = pivotIndex + 1; i < arr.length; i++) {
            assertTrue(arr[i] >= pivotValue, "Element at index " + i + " should be >= pivot");
        }
    }

    @Test
    void testPartitionMedianWithDuplicates() {
        int[] arr = {5, 2, 8, 2, 9, 1, 5, 5};
        int pivotIndex = QuickSort.partitionMedian(arr, 0, arr.length - 1);
        
        int pivotValue = arr[pivotIndex];
        for (int i = 0; i < pivotIndex; i++) {
            assertTrue(arr[i] <= pivotValue);
        }
        for (int i = pivotIndex + 1; i < arr.length; i++) {
            assertTrue(arr[i] >= pivotValue);
        }
    }

    @Test
    void testPartitionMedianSingleElement() {
        int[] arr = {42};
        int pivotIndex = QuickSort.partitionMedian(arr, 0, 0);
        assertEquals(0, pivotIndex);
        assertEquals(42, arr[0]);
    }

    @Test
    void testPartitionMedianTwoElements() {
        int[] arr = {2, 1};
        int pivotIndex = QuickSort.partitionMedian(arr, 0, 1);
        
        // 验证分区正确性
        assertTrue(arr[0] <= arr[1]);
    }

    @Test
    void testPartitionMedianAllEqual() {
        int[] arr = {5, 5, 5, 5, 5};
        int pivotIndex = QuickSort.partitionMedian(arr, 0, arr.length - 1);
        
        // 所有元素相等时，pivot可以是任意位置
        assertTrue(pivotIndex >= 0 && pivotIndex < arr.length);
    }

    @Test
    void testMedianThree() {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6};

        // 测试三数取中
        assertEquals(0, QuickSort.medianThree(arr, 0, 1, 2)); // 3,1,4 -> 3是中间值
        assertEquals(1, QuickSort.medianThree(arr, 1, 2, 3)); // 1,4,1 -> 1是中间值
        assertEquals(2, QuickSort.medianThree(arr, 2, 3, 4)); // 4,1,5 -> 4是中间值
    }

    // ===== 新增缺失的测试用例 =====

    @Test
    void testQuickSort2WithTwoElements() {
        int[] arr = {2, 1};
        int[] expected = {1, 2};
        QuickSort.quickSort2(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort3WithTwoElements() {
        int[] arr = {2, 1};
        int[] expected = {1, 2};
        QuickSort.quickSort3(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort2WithThreeElements() {
        int[] arr = {3, 1, 2};
        int[] expected = {1, 2, 3};
        QuickSort.quickSort2(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort3WithThreeElements() {
        int[] arr = {3, 1, 2};
        int[] expected = {1, 2, 3};
        QuickSort.quickSort3(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort2WithNegativeNumbers() {
        int[] arr = {-3, 2, -1, 5, -6, 4};
        int[] expected = {-6, -3, -1, 2, 4, 5};
        QuickSort.quickSort2(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort3WithNegativeNumbers() {
        int[] arr = {-3, 2, -1, 5, -6, 4};
        int[] expected = {-6, -3, -1, 2, 4, 5};
        QuickSort.quickSort3(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort2WithExtremeValues() {
        int[] arr = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE, 1, -1};
        int[] expected = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE};
        QuickSort.quickSort2(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort3WithExtremeValues() {
        int[] arr = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE, 1, -1};
        int[] expected = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE};
        QuickSort.quickSort3(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort2WithMixedDuplicates() {
        int[] arr = {1, 1, 1, 2, 2, 2, 3, 3, 3};
        int[] expected = {1, 1, 1, 2, 2, 2, 3, 3, 3};
        QuickSort.quickSort2(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort3WithMixedDuplicates() {
        int[] arr = {1, 1, 1, 2, 2, 2, 3, 3, 3};
        int[] expected = {1, 1, 1, 2, 2, 2, 3, 3, 3};
        QuickSort.quickSort3(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort2WithZeroValues() {
        int[] arr = {0, 0, 0, 1, -1, 0};
        int[] expected = {-1, 0, 0, 0, 0, 1};
        QuickSort.quickSort2(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSort3WithZeroValues() {
        int[] arr = {0, 0, 0, 1, -1, 0};
        int[] expected = {-1, 0, 0, 0, 0, 1};
        QuickSort.quickSort3(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testPartition2Basic() {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6};
        int pivotIndex = QuickSort.partition2(arr, 0, arr.length - 1);

        // 验证pivot左边的元素都小于等于pivot
        int pivotValue = arr[pivotIndex];
        for (int i = 0; i < pivotIndex; i++) {
            assertTrue(arr[i] <= pivotValue, "Element at index " + i + " should be <= pivot");
        }

        // 验证pivot右边的元素都大于等于pivot
        for (int i = pivotIndex + 1; i < arr.length; i++) {
            assertTrue(arr[i] >= pivotValue, "Element at index " + i + " should be >= pivot");
        }
    }

    @Test
    void testPartition2WithDuplicates() {
        int[] arr = {5, 2, 8, 2, 9, 1, 5, 5};
        int pivotIndex = QuickSort.partition2(arr, 0, arr.length - 1);

        int pivotValue = arr[pivotIndex];
        for (int i = 0; i < pivotIndex; i++) {
            assertTrue(arr[i] <= pivotValue);
        }
        for (int i = pivotIndex + 1; i < arr.length; i++) {
            assertTrue(arr[i] >= pivotValue);
        }
    }

    @Test
    void testPartitionBasic() {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6};
        int pivotIndex = QuickSort.partition(arr, 0, arr.length - 1);

        // 验证pivot左边的元素都小于pivot
        int pivotValue = arr[pivotIndex];
        for (int i = 0; i < pivotIndex; i++) {
            assertTrue(arr[i] < pivotValue, "Element at index " + i + " should be < pivot");
        }

        // 验证pivot右边的元素都大于等于pivot
        for (int i = pivotIndex + 1; i < arr.length; i++) {
            assertTrue(arr[i] >= pivotValue, "Element at index " + i + " should be >= pivot");
        }
    }

    @Test
    void testRandomArray() {
        // 测试随机数组
        int[] arr = new int[100];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 1000);
        }

        int[] arr2 = arr.clone();
        int[] arr3 = arr.clone();

        QuickSort.quickSort2(arr2);
        QuickSort.quickSort3(arr3);

        // 验证两个排序结果相同
        assertArrayEquals(arr2, arr3);

        // 验证排序正确性
        for (int i = 1; i < arr2.length; i++) {
            assertTrue(arr2[i - 1] <= arr2[i]);
            assertTrue(arr3[i - 1] <= arr3[i]);
        }
    }

    // ===== quickSortShortest 测试用例 =====

    @Test
    void testQuickSortShortestBasic() {
        int[] arr = {3, 2, 1, 5, 6, 4};
        int[] expected = {1, 2, 3, 4, 5, 6};
        QuickSort.quickSortShortest(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSortShortestWithDuplicates() {
        int[] arr = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int[] expected = {1, 2, 2, 3, 3, 4, 5, 5, 6};
        QuickSort.quickSortShortest(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSortShortestWithEmptyArray() {
        int[] arr = {};
        QuickSort.quickSortShortest(arr, 0, arr.length - 1);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void testQuickSortShortestWithSingleElement() {
        int[] arr = {42};
        QuickSort.quickSortShortest(arr, 0, 0);
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test
    void testQuickSortShortestWithTwoElements() {
        int[] arr = {2, 1};
        int[] expected = {1, 2};
        QuickSort.quickSortShortest(arr, 0, 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSortShortestWithThreeElements() {
        int[] arr = {3, 1, 2};
        int[] expected = {1, 2, 3};
        QuickSort.quickSortShortest(arr, 0, 2);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSortShortestWithAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        QuickSort.quickSortShortest(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSortShortestWithReverseSorted() {
        int[] arr = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        QuickSort.quickSortShortest(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSortShortestWithAllEqual() {
        int[] arr = {5, 5, 5, 5, 5};
        int[] expected = {5, 5, 5, 5, 5};
        QuickSort.quickSortShortest(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSortShortestWithNegativeNumbers() {
        int[] arr = {-3, 2, -1, 5, -6, 4};
        int[] expected = {-6, -3, -1, 2, 4, 5};
        QuickSort.quickSortShortest(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSortShortestWithExtremeValues() {
        int[] arr = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE, 1, -1};
        int[] expected = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE};
        QuickSort.quickSortShortest(arr, 0, arr.length - 1);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testQuickSortShortestLargeArray() {
        int[] arr = new int[1000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr.length - i; // 逆序排列
        }
        QuickSort.quickSortShortest(arr, 0, arr.length - 1);

        // 验证排序结果
        for (int i = 1; i < arr.length; i++) {
            assertTrue(arr[i - 1] <= arr[i], "Array should be sorted in ascending order");
        }
    }

    @Test
    void testQuickSortShortestWithNullArray() {
        int[] arr = null;
        QuickSort.quickSortShortest(arr, 0, 0);
        assertNull(arr);
    }

    @Test
    void testQuickSortShortestWithInvalidRange() {
        int[] arr = {1, 2, 3};
        // begin > end 应该直接返回
        QuickSort.quickSortShortest(arr, 2, 1);
        assertArrayEquals(new int[]{1, 2, 3}, arr);
    }

    @Test
    void testQuickSortShortestPerformanceComparison() {
        // 测试尾递归优化版本与其他版本的性能一致性
        int[] arr1 = {3, 2, 1, 5, 6, 4, 7, 8, 9, 0};
        int[] arr2 = arr1.clone();
        int[] arr3 = arr1.clone();

        QuickSort.quickSort2(arr1, 0, arr1.length - 1);
        QuickSort.quickSort3(arr2, 0, arr2.length - 1);
        QuickSort.quickSortShortest(arr3, 0, arr3.length - 1);

        // 验证三个版本结果一致
        assertArrayEquals(arr1, arr2);
        assertArrayEquals(arr2, arr3);
    }
}