package algorithm.sort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CountingSortTest {

    @Test
    @DisplayName("标准计数排序 - 正常场景")
    void testCountingSortNormal() {
        int[] arr = {4, 2, 2, 8, 3, 3, 1};
        int[] expected = {1, 2, 2, 3, 3, 4, 8};
        int[] result = CountingSort.countingSort(arr, 1, 8);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("标准计数排序 - 空数组")
    void testCountingSortEmpty() {
        int[] arr = {};
        int[] result = CountingSort.countingSort(arr, 0, 10);
        assertArrayEquals(new int[]{}, result);
    }

    @Test
    @DisplayName("标准计数排序 - 单元素")
    void testCountingSortSingleElement() {
        int[] arr = {5};
        int[] result = CountingSort.countingSort(arr, 1, 10);
        assertArrayEquals(new int[]{5}, result);
    }

    @Test
    @DisplayName("标准计数排序 - 已排序")
    void testCountingSortAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        int[] result = CountingSort.countingSort(arr, 1, 5);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("标准计数排序 - 逆序")
    void testCountingSortReverse() {
        int[] arr = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        int[] result = CountingSort.countingSort(arr, 1, 5);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("标准计数排序 - 重复元素")
    void testCountingSortDuplicates() {
        int[] arr = {3, 3, 3, 1, 1, 2, 2, 2};
        int[] expected = {1, 1, 2, 2, 2, 3, 3, 3};
        int[] result = CountingSort.countingSort(arr, 1, 3);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("标准计数排序 - 范围错误")
    void testCountingSortOutOfRange() {
        int[] arr = {1, 2, 10}; // 10 超出范围
        assertThrows(IllegalArgumentException.class, () -> {
            CountingSort.countingSort(arr, 1, 5);
        });
    }

    @Test
    @DisplayName("稳定计数排序 - 保持相对顺序")
    void testStableCountingSort() {
        // 测试稳定性：相同元素保持原始相对顺序
        int[] arr = {4, 2, 2, 8, 3, 3, 1};
        int[] expected = {1, 2, 2, 3, 3, 4, 8};
        int[] result = CountingSort.stableCountingSort(arr, 1, 8);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("稳定计数排序 - 复杂场景")
    void testStableCountingSortComplex() {
        int[] arr = {5, 2, 9, 5, 2, 3, 5};
        int[] expected = {2, 2, 3, 5, 5, 5, 9};
        int[] result = CountingSort.stableCountingSort(arr, 2, 9);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("原地计数排序 - 正常场景")
    void testInPlaceCountingSort() {
        int[] arr = {4, 2, 2, 8, 3, 3, 1};
        int[] expected = {1, 2, 2, 3, 3, 4, 8};
        CountingSort.inPlaceCountingSort(arr, 1, 8);
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("原地计数排序 - 空数组")
    void testInPlaceCountingSortEmpty() {
        int[] arr = {};
        CountingSort.inPlaceCountingSort(arr, 0, 10);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    @DisplayName("自动范围计数排序 - 正常场景")
    void testAutoRangeCountingSort() {
        int[] arr = {100, 95, 87, 92, 100, 95};
        int[] expected = {87, 92, 95, 95, 100, 100};
        int[] result = CountingSort.countingSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("自动范围计数排序 - 负数")
    void testAutoRangeCountingSortNegative() {
        int[] arr = {-5, -1, -3, 0, 2, -2};
        int[] expected = {-5, -3, -2, -1, 0, 2};
        int[] result = CountingSort.countingSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("自动范围计数排序 - 大数")
    void testAutoRangeCountingSortLargeNumbers() {
        int[] arr = {1000000, 999999, 1000001};
        int[] expected = {999999, 1000000, 1000001};
        int[] result = CountingSort.countingSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("分块计数排序 - 正常场景")
    void testSegmentedCountingSort() {
        int[] arr = {1000, 500, 2000, 1500, 800, 1200};
        int[] expected = {500, 800, 1000, 1200, 1500, 2000};
        int[] result = CountingSort.segmentedCountingSort(arr, 500);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("分块计数排序 - 小数组")
    void testSegmentedCountingSortSmall() {
        int[] arr = {3, 1, 2};
        int[] expected = {1, 2, 3};
        int[] result = CountingSort.segmentedCountingSort(arr, 2);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("分块计数排序 - 单元素")
    void testSegmentedCountingSortSingle() {
        int[] arr = {42};
        int[] expected = {42};
        int[] result = CountingSort.segmentedCountingSort(arr, 10);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("边界测试 - null数组")
    void testNullArray() {
        assertArrayEquals(new int[]{}, CountingSort.countingSort(null, 0, 10));
        assertArrayEquals(new int[]{}, CountingSort.stableCountingSort(null, 0, 10));
        // 原地排序对null数组应该直接返回
        CountingSort.inPlaceCountingSort(null, 0, 10);
    }

    @Test
    @DisplayName("边界测试 - 单元素数组")
    void testSingleElementArray() {
        int[] arr = {42};
        int[] result = CountingSort.countingSort(arr, 42, 42);
        assertArrayEquals(new int[]{42}, result);
    }

    @Test
    @DisplayName("性能测试 - 大数据量")
    void testLargeDataSet() {
        int size = 10000;
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (int) (Math.random() * 1000);
        }

        int[] result = CountingSort.countingSort(arr, 0, 1000);

        // 验证排序结果
        for (int i = 1; i < result.length; i++) {
            assertTrue(result[i - 1] <= result[i], "数组未正确排序");
        }
    }

    @Test
    @DisplayName("稳定性验证 - 对象排序")
    void testStabilityWithObjects() {
        // 模拟需要稳定排序的场景
        int[] arr = {5, 2, 8, 2, 9, 1, 5, 5};
        int[] stableResult = CountingSort.stableCountingSort(arr, 1, 9);
        int[] nonStableResult = CountingSort.countingSort(arr, 1, 9);

        // 两种结果都应该正确排序
        assertArrayEquals(new int[]{1, 2, 2, 5, 5, 5, 8, 9}, stableResult);
        assertArrayEquals(new int[]{1, 2, 2, 5, 5, 5, 8, 9}, nonStableResult);
    }
}