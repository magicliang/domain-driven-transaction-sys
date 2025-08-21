package algorithm.sort;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 原地归并排序实现
 * 在原始数组上进行归并排序，减少内存使用
 *
 * @author magicliang
 *
 *         date: 2025-08-21 16:00
 */
public class InPlaceMergeSort {

    /**
     * 原地归并排序入口方法
     * 在原始数组上进行归并排序，减少内存使用
     *
     * @param arr 待排序的整数数组
     * @param left 排序区间的起始索引
     * @param right 排序区间的结束索引
     */
    public static void inPlaceMergeSort(int[] arr, int left, int right) {

        // 某些情况下不该排序
        if (arr == null || arr.length == 1 || left >= right) {
            return;
        }

        // 生成一个临时数据段，最好和数组完全相等，对整个数组反复使用；如果和 right - left + 1 相等，则需要使用一些技巧
        int[] temp = new int[right - left + 1];

        // 再排右部分：因为偏左，所以加一是安全的
        inPlaceMergeSort(arr, temp, left, right);
    }

    /**
     * 原地归并排序的递归实现
     * 使用临时数组辅助合并操作
     *
     * @param arr 待排序的数组
     * @param temp 临时数组，用于合并操作
     * @param left 当前排序区间的起始索引
     * @param right 当前排序区间的结束索引
     */
    public static void inPlaceMergeSort(int[] arr, int[] temp, int left, int right) {
        // 某些情况下不该排序
        if (arr == null || arr.length == 1 || left >= right) {
            return;
        }
        // 取中点：对奇数处于正中央，对于偶数处于偏左的位置。对于单值意味着本值本身
        int mid = left + (right - left) / 2;

        // 先排左部分
        inPlaceMergeSort(arr, temp, left, mid);

        // 再排右部分：因为偏左，所以加一是安全的
        inPlaceMergeSort(arr, temp, mid + 1, right);

        // 易错的点：分治以后应该在每一层都做好 merge
        inPlaceMerge(arr, temp, left, mid, right);
    }

    /**
     * 原地合并算法
     * 将两个已排序的子数组合并成一个有序数组
     *
     * @param arr 原始数组
     * @param temp 临时数组，用于合并操作
     * @param left 左子数组的起始索引
     * @param mid 左子数组的结束索引
     * @param right 右子数组的结束索引
     */
    // 原地合并算法
    private static void inPlaceMerge(int[] arr, int[] temp, int left, int mid, int right) {
        // 已排序
        if (arr[mid] <= arr[mid + 1]) {
            return;
        }

        // 原地内存操作的核心思想是：准备一个和现空间一样大的空间，先把数据拷贝过去，然后再拷贝回这个共享数据段

        // 如果数组完全和原始 arr 总长相等，则可以一对一映射，否则，这里面的 temp 要从0开始，而 arr要从 left 开始
        if (right + 1 - left >= 0) {
            System.arraycopy(arr, left, temp, left - left, right + 1 - left);
        }

        // 设置两个游标，分别在区间的起点

        // 先对距离在 temp 里是从0开始的，映射到 arr 的 left 区间
        int i = 0;
        int j = mid - left + 1;
        int tempMid = j - 1;
        int end = right - left;
        int k = left;
        while (i <= tempMid && j <= end) {
            if (temp[i] <= temp[j]) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
            }
        }

        while (i <= tempMid) {
            arr[k++] = temp[i++];
        }

        while (j <= end) {
            arr[k++] = temp[j++];
        }
    }
}