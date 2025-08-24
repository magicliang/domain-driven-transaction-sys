package algorithm.pearls;

import java.util.Arrays;

/**
 * 解决实数和的子集问题：判断是否存在大小为k的子集，其和不超过目标值t。
 * 实数和的子集问题有以下特点：
 * 1. 状态爆炸：实数是连续的，无法用有限的离散状态表示，导致动态规划（DP）的状态空间无限大。
 * 2. 精度问题：浮点数比较存在误差，需要引入容差（如Math.abs(a + b - c) < epsilon）来比较。
 * 3. 存储困难：无法直接用数组索引表示连续的实数区间。
 *
 * 因此，实数和的子集问题不适合用动态规划解决。
 * 当前实现采用贪心算法和快速选择算法，避免了DP的局限性：
 * - 贪心算法：通过排序和选择最小的k个元素，快速判断是否存在满足条件的子集。
 * - 快速选择算法：通过部分排序（QuickSelect）优化性能，避免完全排序的开销。
 *
 * @author liangchuan
 */
public class BestSubset {

    /**
     * 判断是否存在一个 k 元子集，其元素之和不超过 t。
     * 使用贪心算法，先对数组排序，然后取前 k 个最小的元素求和，判断是否满足条件。
     * @param t   目标值，子集和的上限
     * @param k   子集的大小
     * @param arr 输入的整数数组
     * @return 如果存在满足条件的子集，返回 true；否则返回 false
     */
    public boolean greedyLessOrEqual(double t, int k, double[] arr) {
        // 检查输入有效性
        if (arr == null || arr.length < k) {
            return false;
        }

        // 对数组进行排序，以便取前 k 个最小的元素
        Arrays.sort(arr);
        double sum = 0.0;
        // 计算前 k 个最小元素的和
        for (int i = 0; i < k; i++) {
            sum += arr[i];
        }
        // 最终检查是否满足条件
        return sum <= t;
    }

    /**
     * 判断是否存在一个 k 元子集，其元素之和不超过 t。
     * 使用快速选择算法（QuickSelect）找到前 k 个最小的元素，然后计算它们的和是否不超过 t。
     * 快速选择算法的时间复杂度为 O(n)，比排序更高效。
     *
     * @param t   目标值，子集和的上限
     * @param k   子集的大小
     * @param arr 输入的整数数组
     * @return 如果存在满足条件的子集，返回 true；否则返回 false
     */
    public boolean quickSelectLessOrEqual(double t, int k, double[] arr) {
        // 检查输入有效性
        if (arr == null || arr.length < k) {
            return false;
        }

        // 使用快速选择算法，将前 k 个最小的元素移动到数组的前 k 个位置
        quickSelect(arr, k, 0, arr.length - 1);

        double sum = 0.0;
        // 计算前 k 个最小元素的和
        for (int i = 0; i < k; i++) {
            sum += arr[i];
        }
        // 最终检查是否满足条件
        return sum <= t;
    }

    /**
     * 快速选择算法：将数组中前 k 个最小的元素移动到数组的前 k 个位置。
     * 通过递归调用 partition 方法，找到第 k 小的元素，并确保其左侧的元素都小于它。
     * <p>
     * 关键点：
     * 1. 如果 pivotal < k - 1，说明前 k 个最小元素在 pivotal 的右侧，因此新区间为 [pivotal + 1, end]。
     * - pivotal 本身已经是第 (pivotal + 1) 小的元素，因此无需再处理 pivotal 的位置。
     * - 新区间从 pivotal + 1 开始，避免重复处理 pivotal，防止死循环。
     * 2. 如果 pivotal > k - 1，说明前 k 个最小元素在 pivotal 的左侧，因此新区间为 [begin, pivotal - 1]。
     * - pivotal 本身已经大于第 k 小的元素，因此无需包含 pivotal 在内。
     * - 新区间到 pivotal - 1 结束，确保只搜索左侧更小的部分。
     *
     * @param arr   输入的整数数组
     * @param k     需要选择的最小元素的数量
     * @param begin 当前处理的子数组起始索引
     * @param end   当前处理的子数组结束索引
     */
    private void quickSelect(double[] arr, int k, int begin, int end) {
        // 获取当前 pivotal 的位置
        int pivotal = partition(arr, begin, end);
        // 如果 pivotal 的位置不等于 k-1，继续调整区间
        while (pivotal != k - 1) {
            if (pivotal < k - 1) {
                // 如果 pivotal 的位置小于 k-1，说明前 k 个最小元素在 pivotal 的右侧
                pivotal = partition(arr, pivotal + 1, end);
            } else {
                // 如果 pivotal 的位置大于 k-1，说明前 k 个最小元素在 pivotal 的左侧
                pivotal = partition(arr, begin, pivotal - 1);
            }
        }
    }

    /**
     * 分区函数：将数组分为两部分，左侧元素小于 pivotal，右侧元素大于等于 pivotal。
     * 返回 pivotal 的最终位置。
     *
     * @param arr   输入的整数数组
     * @param begin 当前处理的子数组起始索引
     * @param end   当前处理的子数组结束索引
     * @return pivotal 的最终位置
     */
    private int partition(double[] arr, int begin, int end) {
        // 选择最后一个元素作为 pivotal
        double target = arr[end];
        int i = begin - 1; // i 是小于 pivotal 的元素的边界
        int j = begin; // j 是当前遍历的索引

        while (j < end) {
            if (arr[j] < target) {
                // 如果当前元素小于 pivotal，将其交换到左侧
                i++;
                swap(arr, i, j);
            }
            j++;
        }

        // 将 pivotal 放到正确的位置（i+1）
        i++;
        swap(arr, i, end);
        return i;
    }

    void swap(double[] arr, int i, int j) {
        double tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}
