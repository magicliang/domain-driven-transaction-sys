package algorithm.divideconquer;

/**
 * 求解最大子数组问题
 * “一分为二看全局，三种情况须记牢，跨越边界莫忘记，合并结果定乾坤。”
 * 这个问题在 leetcode 是通过不过的。
 * 因为 leetcode 的数组还有如下特点：
 * 1. 可能长度为1-也就是允许不交易
 * 2. 整个总的子数组和也可能是负数，所以也应该允许不交易
 *
 * @author liangchuan
 * @version 1.1 // 标记版本更新
 */
public class MaxSubArray {

    /**
     * 公共入口方法，使用分治法找到数组中具有最大和的连续子数组的和。
     *
     * @param arr 输入的整数数组
     * @return 最大子数组的和
     * @throws IllegalArgumentException 如果数组为 null 或空
     */
    public static int maxSubArrayDC(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Input array must not be null or empty");
        }
        // 调用递归辅助函数处理整个数组范围 [0, arr.length - 1]
        return maxSubArrayDCHelper(arr, 0, arr.length - 1);
    }

    /**
     * 递归辅助函数，计算 arr[low...high] 范围内的最大子数组和。
     * <p>
     * 核心分治逻辑 ("分、治、合")：
     * 1. 分 (Divide): 将当前区间 [low, high] 从中点 mid 分为 [low, mid] 和 [mid+1, high]。
     * 2. 治 (Conquer): 递归地计算左半部分和右半部分的最大子数组和。
     * - lowBest = maxSubArrayDCHelper(arr, low, mid) -> 情况1：最大子数组完全在左半部
     * - highBest = maxSubArrayDCHelper(arr, mid + 1, high) -> 情况2：最大子数组完全在右半部
     * 3. 合 (Combine): 计算跨越中点的最大子数组和 (midBest)。
     * - midBest = findMidMaxSubArray(arr, low, mid, high) -> 情况3：最大子数组跨越中点
     * 4. 合 (Combine): 返回三种情况下的最大值，作为当前区间 [low, high] 的解。
     * - "合并结果定乾坤"
     * </p>
     *
     * @param arr  输入数组
     * @param low  区间左边界 (inclusive)
     * @param high 区间右边界 (inclusive)
     * @return 区间 [low, high] 内的最大子数组和
     */
    private static int maxSubArrayDCHelper(int[] arr, int low, int high) {
        // 基本情况：区间只有一个元素，最大和就是该元素本身
        // 这是递归的终止条件 ("看全局" 的基础)
        if (low == high) {
            return arr[low];
        }

        // 分 (Divide): 找到中点，避免 (low + high) 溢出
        int mid = (high - low) / 2 + low;

        // 治 (Conquer): 递归求解左半部分 [low, mid] 的最大子数组和 (情况 1: 完全在左)
        int lowBest = maxSubArrayDCHelper(arr, low, mid);

        // 治 (Conquer): 递归求解右半部分 [mid+1, high] 的最大子数组和 (情况 2: 完全在右)
        int highBest = maxSubArrayDCHelper(arr, mid + 1, high);

        // 合 (Combine): 计算跨越中点的最大子数组和 (情况 3: 跨越边界)
        // "跨越边界莫忘记"
        int midBest = findMidMaxSubArray(arr, low, mid, high);

        // 合 (Combine): 返回三种情况下的最大值，得到当前区间 [low, high] 的全局最大和
        // "合并结果定乾坤"
        int max = Math.max(lowBest, highBest);
        max = Math.max(max, midBest);
        return max;
    }

    /**
     * 计算跨越中点的最大子数组和。
     * <p>
     * 这是分治法中处理 "跨越边界" 情况的关键步骤 ("跨越边界莫忘记")。
     * 跨越中点的子数组可以看作是：
     * 1. 左半部分以 mid 结尾的后缀 (suffix) 的最大和。
     * 2. 右半部分以 mid+1 开头的前缀 (prefix) 的最大和。
     * 将这两部分的最大和相加即得跨越中点的最大子数组和。
     * </p>
     *
     * @param arr  输入数组
     * @param low  区间左边界
     * @param mid  中点
     * @param high 区间右边界
     * @return 跨越中点的最大子数组和
     */
    private static int findMidMaxSubArray(int[] arr, int low, int mid, int high) {
        // --- 计算左半部分包含 arr[mid] 的最大和 (向左扩展) ---
        // 初始化为 arr[mid]，确保至少包含中点元素
        int lowBest = arr[mid];
        int sum = lowBest;
        // 从 mid-1 开始，向左遍历到 low
        // "这个查找有个子数组不得变变边界的问题，所以就来两个指针，找出单方向的最大子数组"
        for (int i = mid - 1; i >= low; i--) {
            sum += arr[i];
            if (sum > lowBest) {
                lowBest = sum;
            }
        }

        // --- 计算右半部分包含 arr[mid+1] 的最大和 (向右扩展) ---
        // 初始化为 arr[mid+1]，确保至少包含中点右边的元素
        int highBest;
        if (mid + 1 <= high) { // 易错的点：这里面右区间是可能在 +1的地方越界的
            // 右半部分存在
            highBest = arr[mid + 1];
            sum = highBest;
            // 从 mid+2 开始，向右遍历到 high
            for (int i = mid + 2; i <= high; i++) {
                sum += arr[i];
                if (sum > highBest) {
                    highBest = sum;
                }
            }
        } else {
            // 右半部分不存在（理论上不应该发生，但为了安全）
            highBest = 0;
        }

        // --- 合并结果 ---
        // 跨越中点的最大和是左半部分最大和加上右半部分最大和
        return lowBest + highBest;
    }
}
