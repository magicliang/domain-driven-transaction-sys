package algorithm.sort;

import java.util.Arrays;

/**
 * 基数排序实现类
 *
 * 基数排序是一种非比较型整数排序算法，通过逐位排序来实现整体排序。
 * 它利用了稳定排序的特性，通常结合计数排序作为子程序来实现。
 *
 * 算法特点：
 *
 * 时间复杂度为 O(d×(n+k))、非自适应排序：
 * - 涉及遍历 nums 和遍历 counter，都使用线性时间
 * - d 为数字的最大位数，k 为基数（通常是10或2^8）
 * - 一般情况下 k 为常数，时间复杂度趋于 O(d×n)
 * - 当 d 远小于 log n 时，性能优于基于比较的排序算法
 *
 * 空间复杂度为 O(n+k)、非原地排序：
 * - 借助了长度分别为 n 和 k 的数组 res 和 counter
 * - res 用于存储中间排序结果，长度为 n
 * - counter 用于计数排序的计数数组，长度为 k（基数）
 *
 * 稳定排序：
 * - 由于向 res 中填充元素的顺序是"从右向左"的，因此倒序遍历 nums 可以避免改变相等元素之间的相对位置
 * - 从而实现稳定排序
 * - 实际上，正序遍历 nums 也可以得到正确的排序结果，但结果是非稳定的
 * - 稳定性是基数排序正确工作的关键，确保高位排序不会破坏低位排序的结果
 *
 * 适用场景：
 * - 整数排序，特别是当数字范围不大但数字较长时
 * - 字符串排序（按字符逐位排序）
 * - 需要稳定排序的场景
 */
public class RadixSort {

    /**
     * 使用基数为10的基数排序
     *
     * @param arr 待排序的非负整数数组
     * @return 排序后的新数组
     */
    public static int[] radixSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr != null ? arr.clone() : new int[0];
        }

        // 找出最大值，确定最大位数
        int max = Arrays.stream(arr).max().orElse(0);

        // 从最低位开始，逐位排序
        int exp = 1; // 当前处理的位（个位开始）
        while (max / exp > 0) {
            countingSortByDigit(arr, exp);
            exp *= 10;
        }

        return arr.clone();
    }

    /**
     * 使用指定基数的基数排序
     *
     * @param arr 待排序的非负整数数组
     * @param base 基数（如10表示十进制，256表示字节）
     * @return 排序后的新数组
     */
    public static int[] radixSort(int[] arr, int base) {
        if (arr == null || arr.length <= 1) {
            return arr != null ? arr.clone() : new int[0];
        }

        int max = Arrays.stream(arr).max().orElse(0);

        int exp = 1;
        while (max / exp > 0) {
            countingSortByDigit(arr, exp, base);
            exp *= base;
        }

        return arr.clone();
    }

    /**
     * 按指定位进行计数排序（稳定排序）
     * 这是基数排序的核心子程序
     *
     * @param arr 待排序数组（会被修改）
     * @param exp 当前处理的位（1=个位，10=十位，100=百位...）
     */
    private static void countingSortByDigit(int[] arr, int exp) {
        countingSortByDigit(arr, exp, 10);
    }

    /**
     * 按指定位进行计数排序（使用指定基数）
     *
     * @param arr 待排序数组（会被修改）
     * @param exp 当前处理的位
     * @param base 基数
     */
    private static void countingSortByDigit(int[] arr, int exp, int base) {
        int n = arr.length;

        // 输出数组（长度n）
        int[] output = new int[n];

        // 计数数组（长度base）
        int[] count = new int[base];

        // 1. 统计当前位的数字出现次数
        for (int num : arr) {
            int digit = (num / exp) % base;
            count[digit]++;
        }

        // 2. 计算累积计数（实现稳定性）
        for (int i = 1; i < base; i++) {
            count[i] += count[i - 1];
        }

        // 3. 从右向左构建稳定排序结果
        // 这是实现稳定性的关键步骤
        for (int i = n - 1; i >= 0; i--) {
            int digit = (arr[i] / exp) % base;
            output[count[digit] - 1] = arr[i];
            count[digit]--;
        }

        // 4. 将排序结果复制回原数组
        System.arraycopy(output, 0, arr, 0, n);
    }

    /**
     * 支持负数的基数排序
     * 通过偏移处理负数
     *
     * @param arr 待排序整数数组（可包含负数）
     * @return 排序后的新数组
     */
    public static int[] radixSortWithNegative(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr != null ? arr.clone() : new int[0];
        }

        // 找到最小值和最大值
        int min = Arrays.stream(arr).min().orElse(0);
        int max = Arrays.stream(arr).max().orElse(0);

        // 如果都是非负数，使用标准基数排序
        if (min >= 0) {
            return radixSort(arr);
        }

        // 处理负数：将所有数加上|min|，使其变为非负数
        int offset = Math.abs(min);
        int[] shifted = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            shifted[i] = arr[i] + offset;
        }

        // 对偏移后的数组进行排序
        radixSort(shifted);

        // 恢复原始值
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = shifted[i] - offset;
        }

        return result;
    }

    /**
     * 示例用法
     */
    public static void main(String[] args) {
        // 示例1：标准基数排序
        int[] arr1 = {170, 45, 75, 90, 2, 802, 24, 66};
        System.out.println("原始数组: " + Arrays.toString(arr1));
        int[] sorted1 = radixSort(arr1);
        System.out.println("基数排序结果: " + Arrays.toString(sorted1));

        // 示例2：使用不同基数
        int[] arr2 = {170, 45, 75, 90, 2, 802, 24, 66};
        int[] sorted2 = radixSort(arr2, 256); // 使用256作为基数（字节排序）
        System.out.println("字节基数排序: " + Arrays.toString(sorted2));

        // 示例3：包含负数的排序
        int[] arr3 = {-5, -10, 0, 5, 3, -3, 8, -8};
        int[] sorted3 = radixSortWithNegative(arr3);
        System.out.println("含负数基数排序: " + Arrays.toString(sorted3));
    }
}