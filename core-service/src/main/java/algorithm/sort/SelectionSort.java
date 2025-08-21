package algorithm.sort;


/**
 * project name: domain-driven-transaction-sys
 *
 * description: 选择排序。开启一个循环，每轮从未排序区间选择最小的元素，将其放到已排序区间的末尾。
 * 逐步收窄排序区间
 *
 * @author magicliang
 *
 *         date: 2025-08-21 15:10
 */
public class SelectionSort {

    /**
     * 选择排序算法实现（降序排列）
     * 算法思路：从数组末尾开始，每次在[0,i]范围内找到最大值，与位置i的元素交换
     * 时间复杂度：O(n²) - 需要进行n*(n-1)/2次比较
     * 空间复杂度：O(1) - 只需要常数级别的额外空间
     * 稳定性：不稳定排序 - 相同元素的相对位置可能改变
     *
     * @param nums 待排序的整数数组
     */
    public void selectionSort(int[] nums) {
        if (nums == null || nums.length == 0) {
            return;
        }
        int n = nums.length;
        // 外层循环：从数组末尾开始，逐步向前处理
        // i表示当前要确定最终值的位置
        for (int i = n - 1; i >= 0; i--) {
            // 假设当前未排序区间的第一个元素是最大值
            int max = 0;
            // 内层循环：在[0,i]范围内寻找真正的最大值
            for (int j = max + 1; j <= i; j++) {
                if (nums[j] > nums[max]) {
                    max = j;
                }
            }
            // 将找到的最大值交换到位置i
            swap(nums, max, i);
        }
    }

    /**
     * 交换数组中两个位置的元素
     *
     * @param nums 整数数组
     * @param i 第一个位置索引
     * @param j 第二个位置索引
     */
    private void swap(int[] nums, int i, int j) {
        if (i == j) {
            return;
        }
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
