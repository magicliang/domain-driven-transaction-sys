package algorithm.dp;


/**
 * project name: domain-driven-transaction-sys
 *
 * description: 爬楼梯
 *
 * @author magicliang
 *
 *         date: 2025-08-26 16:10
 */
public class ClimbSteps {

    /**
     * 使用记忆化搜索（自顶向下动态规划）解决爬楼梯问题
     *
     * 问题描述：假设你正在爬楼梯。需要n阶你才能到达楼顶。
     * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
     *
     * 示例：
     * - n = 1: 只有一种方法 (1)
     * - n = 2: 两种方法 (1+1, 2)
     * - n = 3: 三种方法 (1+1+1, 1+2, 2+1)
     * - n = 4: 五种方法 (1+1+1+1, 1+1+2, 1+2+1, 2+1+1, 2+2)
     *
     * 时间复杂度：O(n) - 每个子问题只计算一次
     * 时间复杂度：O(n)
     * - 每个子问题只计算一次，通过记忆化避免重复计算
     * - 总共有n个子问题需要解决（从1到n）
     * - 相比朴素递归的O(2^n)有显著改进
     *
     * 空间复杂度：O(n)
     * - 记忆化数组需要O(n)空间存储中间结果
     * - 递归调用栈最大深度为O(n)
     * - 总空间复杂度为O(n)
     *
     * @param n 楼梯的总阶数，必须是正整数
     * @return 到达楼顶的不同方法数
     * @throws IllegalArgumentException 当n小于等于0时抛出
     * @apiNote 该方法使用记忆化技术优化递归，避免了指数级的时间复杂度
     * @implNote 内部使用深度优先搜索配合记忆化数组实现
     * @see #dfsMemoization(int, int[])
     * @see #dpClimb(int)
     */
    public int memoizationClimb(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(""); // 或者根据需求抛出IllegalArgumentException
        }
        // 使用这种 n + 1 数组会让系统从 1 开始遍历
        int[] mem = new int[n + 1];
        return dfsMemoization(n, mem);
    }

    /**
     * 深度优先搜索 + 记忆化的核心实现
     *
     * 状态转移方程：f(n) = f(n-1) + f(n-2)
     * 解释：到达第n阶楼梯的方法数 = 从n-1阶爬1步上来的方法数 + 从n-2阶爬2步上来的方法数
     *
     * 边界条件：
     * - f(1) = 1 (只有一种方法：爬1阶)
     * - f(2) = 2 (两种方法：1+1 或 直接爬2阶)
     *
     * 记忆化原理：使用数组mem缓存已计算的结果，避免重复计算
     *
     * 数学分析：每一层i的重复调用次数为fib(n-i+1)
     *
     * 证明：
     * 考虑计算f(n)时的递归调用树：
     *
     * 计算f(n)需要：
     * - 1次调用f(n-1)
     * - 1次调用f(n-2)
     *
     * 计算f(n-1)需要：
     * - 1次调用f(n-2)
     * - 1次调用f(n-3)
     *
     * 计算f(n-2)需要：
     * - 1次调用f(n-3)
     * - 1次调用f(n-4)
     *
     * 以此类推，我们可以观察到：
     * - f(n-1)被调用1次
     * - f(n-2)被调用2次（来自f(n)和f(n-1)）
     * - f(n-3)被调用3次（来自f(n-1)和f(n-2)）
     * - f(n-4)被调用5次
     *
     * 这个模式正是反向的斐波那契数列！
     *
     * 一般地，对于第i层（即计算f(i)），在没有记忆化的情况下，
     * 它会被重复调用的次数为fib(n-i+1)，其中fib(k)是第k个斐波那契数。
     *
     * 举例：计算f(5)
     * - f(4)被调用1次 = fib(5-4+1) = fib(2) = 1
     * - f(3)被调用2次 = fib(5-3+1) = fib(3) = 2
     * - f(2)被调用3次 = fib(5-2+1) = fib(4) = 3
     * - f(1)被调用5次 = fib(5-1+1) = fib(5) = 5
     *
     * 因此，记忆化技术将时间复杂度从指数级O(2^n)降低到线性级O(n)，
     * 因为每个f(i)只需要计算一次，后续fib(n-i+1)-1次重复调用都可以直接返回缓存结果。
     *
     * @param n 当前需要计算的楼梯阶数
     * @param mem 记忆化数组，存储已计算的结果
     * @return 到达第n阶楼梯的方法数
     * @implSpec 该方法采用懒加载策略，只在需要时才计算子问题
     * @implNote 数组索引从1开始，mem[0]未使用
     * @since 1.0
     */
    private int dfsMemoization(int n, int[] mem) {
        // 设计思想：每一层其实会被重复调用 fib(n-i+1)次，所以只要第一次调用，然后缓存，剩下的重复调用都不触发递归而直接返回就赢了

        if (n == 1 || n == 2) {
            mem[n] = n;
            return n;
        }

        // 记忆化搜索对于当前的元素可以采用一种懒加载的套路
        if (mem[n] != 0) {
            return mem[n];
        }

        mem[n] = dfsMemoization(n - 1, mem) + dfsMemoization(n - 2, mem);

        return mem[n];
    }

    /**
     * 迭代法实现（自底向上动态规划）
     *
     * 时间复杂度：O(n)
     * 空间复杂度：O(1) - 只使用常数个变量
     *
     * @param n 楼梯的总阶数
     * @return 到达楼顶的不同方法数
     * @throws IllegalArgumentException 当n小于等于0时返回0
     * @apiNote 这是最优化的实现，空间复杂度为O(1)
     * @implSpec 使用滚动数组技术，只保留前两个状态
     * @implNote 相比记忆化搜索，避免了递归栈的开销
     * @since 1.0
     * @see #memoizationClimb(int)
     * @see <a href="https://leetcode-cn.com/problems/climbing-stairs/">LeetCode 70. 爬楼梯</a>
     */
    public int dpClimb(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(""); // 或者根据需求抛出IllegalArgumentException
        }

        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }

        int prev2 = 1; // f(1)
        int prev1 = 2; // f(2)
        int current = 0;

        for (int i = 3; i <= n; i++) {
            current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }

        return current;
    }

    /**
     * 带约束爬楼梯
     *
     * <p>给定一个共有 {@code n} 阶的楼梯，你每步可以上 1 阶或者 2 阶，
     * 但不能连续两轮跳 1 阶，请问有多少种方案可以爬到楼顶？
     * 状态转移方程：
     * dp(n, 1) 意味着当前是n阶，再前一步是跳了1阶。
     * dp(n, 2) 意味着当前是n阶，再前一步是跳了2阶。
     * 不能连续跳1，则意味着1阶前面必然是2阶
     *
     * dp(n, 1) = dp(n-1, 2) // 子问题几乎可以直接传递给父问题
     * dp(n, 2) = dp(n-2, 1) + dp(n-2, 2) // 等式左边的2意味着上一轮的问题是 n-2 而不是 n-1
     * dp(n) = dp(n, 1) + dp(n, 2) // 这种带约束的，dp方程是可以下拆的
     * 状态方程
     *
     * @param n 楼梯的总阶数，必须为正整数
     * @return 在约束条件下的不同方法数
     * @throws IllegalArgumentException 当n小于等于0时返回0
     * @apiNote 这是一个典型的带约束的动态规划问题，需要维护两个状态
     * @implSpec 使用二维数组dp[i][j]，其中j=1表示最后一步跳1阶，j=2表示最后一步跳2阶
     * @implNote 数组大小为[n+1][3]，索引从1开始，dp[0]未使用
     * @see <a href="https://leetcode-cn.com/problems/climbing-stairs/">相关变种问题</a>
     *
     *         <p><b>示例：</b></p>
     *         <pre>{@code
     *         n = 1: 1种方法 (1)
     *         n = 2: 1种方法 (2) - 不能连续跳1，所以(1,1)不合法
     *         n = 3: 2种方法 (1,2) 和 (2,1)
     *         n = 4: 3种方法 (2,2), (1,2,1), (2,1,2)
     *         }</pre>
     *
     *         <p><b>状态定义：</b></p>
     *         <ul>
     *           <li>dp[i][1]：到达第i阶，且最后一步跳1阶的方法数</li>
     *           <li>dp[i][2]：到达第i阶，且最后一步跳2阶的方法数</li>
     *         </ul>
     *
     *         <p><b>边界条件：</b></p>
     *         <ul>
     *           <li>dp[1][1] = 1, dp[1][2] = 0</li>
     *           <li>dp[2][1] = 0, dp[2][2] = 1</li>
     *         </ul>
     *
     *         <p><b>时间复杂度：</b>O(n)</p>
     *         <p><b>空间复杂度：</b>O(n)</p>
     * @since 1.0
     */
    public int dpClimbWithAfterEffect(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(""); // 或者根据需求抛出IllegalArgumentException
        }

        if (n == 1) {
            return 1; // 只有(1)一种方法
        }
        if (n == 2) {
            return 1; // 只有(2)一种方法，不能是(1,1)
        }

        // dp 代表的是累加值，总共有n+1级台阶，从1开始必须有 n+1 个数。每个数有 2 种状态，从 1 开始就是 3 个元素的子数组。
        int[][] dp = new int[n + 1][3];
        dp[1][1] = 1;
        dp[1][2] = 0;
        dp[2][1] = 0; // 不能连续跳 1，所以这个初始值为 0
        dp[2][2] = 1;

        for (int i = 3; i <= n; i++) {
            // 易错的点，忘记给 i 赋值，直接给 n 赋值
            dp[i][1] = dp[i - 1][2];
            dp[i][2] = dp[i - 2][1] + dp[i - 2][2];
        }

        return dp[n][1] + dp[n][2];
    }

}