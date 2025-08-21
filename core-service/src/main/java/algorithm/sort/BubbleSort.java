package algorithm.sort;


/**
 * project name: domain-driven-transaction-sys
 *
 * description: 冒泡排序是左右两两交换，选择排序则是目标值和尾部交换。最后和收窄目标区间。
 *
 * @author magicliang
 *
 *         date: 2025-08-21 15:34
 */
public class BubbleSort {

    /**
     * 冒泡排序算法实现
     *
     * 算法原理：
     * 1. 比较相邻的两个元素，如果前一个比后一个大，则交换它们
     * 2. 对每一对相邻元素做同样的工作，从开始第一对到结尾的最后一对
     * 3. 针对所有的元素重复以上的步骤，除了最后一个
     * 4. 重复步骤1~3，直到排序完成
     *
     * 时间复杂度：O(n²) - 最坏情况和平均情况
     * 空间复杂度：O(1) - 原地排序
     * 稳定性：稳定排序算法
     *
     * @param nums 待排序的整数数组
     */
    public void bubbleSort(int[] nums) {
        if (nums == null || nums.length == 0) {
            return;
        }
        int n = nums.length;

        // 外层循环控制排序轮数，每轮确定一个最大值的位置
        // i表示当前未排序部分的右边界，从n-1递减到0
        for (int i = n - 1; i >= 0; i--) {
            // 内层循环进行相邻元素的比较和交换
            // j从0遍历到i-1，比较nums[j]和nums[j+1]
            for (int j = 0; j < i; j++) {
                // 如果前一个元素大于后一个元素，则交换
                // 这样每轮循环都能将当前未排序部分的最大值"冒泡"到右侧
                if (nums[j] > nums[j + 1]) {
                    swap(nums, j, j + 1);
                }
            }
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
