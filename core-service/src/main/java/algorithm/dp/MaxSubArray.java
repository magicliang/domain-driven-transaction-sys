package algorithm.dp;

/**
 * 使用动态规划 (Kadane's Algorithm) 解决最大子数组和问题。
 *
 * @author liangchuan
 */
public class MaxSubArray {


    /**
     * 使用 Kadane 的动态规划方式计算数组中连续子数组的最大和。
     * <p>
     * 算法思路 (Kadane's Algorithm):
     * 1. 状态定义:
     * - 定义 dp[i] 表示所有以 arr[i] 结尾的子数组中和的最大值。
     * 2. 状态转移方程:
     * - 对于每个元素 arr[i]，以它结尾的最大子数组和要么是它本身 (arr[i])，
     * 要么是它加上以 arr[i-1] 结尾的最大子数组和 (dp[i-1] + arr[i]).
     * - 因此，dp[i] = max(arr[i], dp[i-1] + arr[i]).
     * 3. 完备性论证:
     * - 任何一个子数组 arr[i...j]（其中 0 <= i <= j <= n-1）都必须以某个元素 arr[j] 结尾。
     * - 完备性 (Completeness): 每一个可能的子数组 arr[i...j] 都恰好属于一个集合 S_j
     * （所有以 arr[j] 结尾的子数组）。因此，所有可能的子数组的和 sum(arr[i...j]) 都被
     * 分配到了某个集合 S_j 中。
     * - 通过计算每个 dp[j]（即每个集合 S_j 的最大值），然后取这些最大值中的最大值
     * (max(dp[0], dp[1], ..., dp[n-1]))，我们保证能找到全局的最大子数组和。
     * 4. 优化:
     * - 状态转移 dp[i] = max(dp[i-1] + arr[i], arr[i]) 只依赖于前一个状态 dp[i-1].
     * - 因此，可以使用两个变量代替整个 dp 数组，实现 O(1) 空间复杂度。
     * </p>
     * <p>
     * 算法步骤:
     * 1. 初始化 dpCurrent (当前以 arr[i] 结尾的最大和) 和 maxCurrent (到目前为止找到的全局最大和) 为 arr[0].
     * 2. 从数组的第二个元素 (i = 1) 开始遍历.
     * 3. 对于每个元素 arr[i]:
     * a. 更新 dpCurrent: dpCurrent = max(dpCurrent + arr[i], arr[i])
     * (这实现了状态转移方程 dp[i] = max(dp[i-1] + arr[i], arr[i])).
     * b. 更新全局最大和 maxCurrent: maxCurrent = max(maxCurrent, dpCurrent).
     * 4. 遍历结束后，maxCurrent 即为所求的最大子数组和.
     * </p>
     * <p>
     * 时间复杂度: O(n) - 只需遍历数组一次.
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间.
     * </p>
     *
     * @param arr 输入的整数数组.
     * @return 数组中连续子数组的最大和.
     * @throws IllegalArgumentException 如果输入数组为 null 或长度为 0.
     */
    public int getMaxSubArraySum(int[] arr) { // 修正方法名拼写
        /* 检查输入数组的有效性 */
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Input array must not be null or empty");
        }

        /* 基本情况：初始化第一个元素的值 dp[0] 就是 arr[0] 本身 */
        int dpCurrent = arr[0]; /* 当前以 arr[i] 结尾的最大子数组和 (dp[i]) */
        int maxCurrent = dpCurrent; /* 到目前为止找到的最大和 (max(dp[0], ..., dp[i])) */

        /* 从第二个元素开始迭代计算，对应于计算 dp[1] 到 dp[n-1] */
        for (int i = 1; i < arr.length; i++) {
            /* 状态转移方程: dp[i] = max(dp[i-1] + arr[i], arr[i])，这里 dp[i-1] 对应循环中的 dpCurrent */
            dpCurrent = Math.max(dpCurrent + arr[i], arr[i]);

            /* 更新全局最大值: maxCurrent = max(maxCurrent, dp[i])，这里 maxCurrent 对应循环中到 i 为止的 max(dp[0], ..., dp[i]) */
            maxCurrent = Math.max(maxCurrent, dpCurrent);
        }

        /* 循环结束时，maxCurrent 存储了 max(dp[0], dp[1], ..., dp[n-1]) 的计算结果，即整个数组的最大子数组和 */
        return maxCurrent;
    }

}
