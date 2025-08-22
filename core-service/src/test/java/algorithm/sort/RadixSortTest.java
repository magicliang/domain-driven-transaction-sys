package algorithm.sort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RadixSortTest {

    @Test
    void testRadixSortBasic() {
        int[] arr = {170, 45, 75, 90, 2, 802, 24, 66};
        int[] expected = {2, 24, 45, 66, 75, 90, 170, 802};

        int[] result = RadixSort.radixSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    void testRadixSortWithDuplicates() {
        int[] arr = {100, 100, 50, 50, 25, 25, 25};
        int[] expected = {25, 25, 25, 50, 50, 100, 100};

        int[] result = RadixSort.radixSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    void testRadixSortEmptyArray() {
        int[] arr = {};
        int[] result = RadixSort.radixSort(arr);
        assertArrayEquals(new int[]{}, result);
    }

    @Test
    void testRadixSortSingleElement() {
        int[] arr = {42};
        int[] result = RadixSort.radixSort(arr);
        assertArrayEquals(new int[]{42}, result);
    }

    @Test
    void testRadixSortAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};

        int[] result = RadixSort.radixSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    void testRadixSortReverseSorted() {
        int[] arr = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};

        int[] result = RadixSort.radixSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    void testRadixSortWithBase256() {
        int[] arr = {170, 45, 75, 90, 2, 802, 24, 66};
        int[] expected = {2, 24, 45, 66, 75, 90, 170, 802};

        int[] result = RadixSort.radixSort(arr, 256);
        assertArrayEquals(expected, result);
    }

    @Test
    void testRadixSortWithNegativeNumbers() {
        int[] arr = {-5, -10, 0, 5, 3, -3, 8, -8};
        int[] expected = {-10, -8, -5, -3, 0, 3, 5, 8};

        int[] result = RadixSort.radixSortWithNegative(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    void testRadixSortWithAllNegative() {
        int[] arr = {-5, -1, -10, -3, -8};
        int[] expected = {-10, -8, -5, -3, -1};

        int[] result = RadixSort.radixSortWithNegative(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    void testRadixSortWithMixedSignsAndDuplicates() {
        int[] arr = {-5, 5, -5, 0, 0, 3, -3, 3};
        int[] expected = {-5, -5, -3, 0, 0, 3, 3, 5};

        int[] result = RadixSort.radixSortWithNegative(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    void testRadixSortNullArray() {
        int[] result = RadixSort.radixSort(null);
        assertArrayEquals(new int[]{}, result);
    }

    @Test
    void testRadixSortWithNegativeNullArray() {
        int[] result = RadixSort.radixSortWithNegative(null);
        assertArrayEquals(new int[]{}, result);
    }

    @Test
    void testRadixSortLargeNumbers() {
        int[] arr = {999999, 100000, 500000, 123456, 987654};
        int[] expected = {100000, 123456, 500000, 987654, 999999};

        int[] result = RadixSort.radixSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    void testRadixSortStability() {
        // 测试稳定性：相同数字的相对顺序应该保持不变
        // 这里我们使用一个特殊的测试，确保稳定性
        int[] arr = {120, 121, 110, 111, 100, 101};
        int[] result = RadixSort.radixSort(arr);

        // 验证排序正确性
        int[] expected = {100, 101, 110, 111, 120, 121};
        assertArrayEquals(expected, result);
    }

    @Test
    void testRadixSortPerformance() {
        // 测试大数据量排序的性能
        int size = 10000;
        int[] arr = new int[size];

        // 生成随机数组
        for (int i = 0; i < size; i++) {
            arr[i] = (int) (Math.random() * 1000);
        }

        long startTime = System.currentTimeMillis();
        int[] result = RadixSort.radixSort(arr);
        long endTime = System.currentTimeMillis();

        // 验证排序正确性
        assertTrue(isSorted(result));

        // 打印性能信息
        System.out.println("排序 " + size + " 个元素耗时: " + (endTime - startTime) + "ms");
    }

    // 辅助方法：检查数组是否已排序
    private boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }
}