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
     * 空间复杂度：O(n) - 记忆化数组和递归栈空间
     *
     * @param n 楼梯的总阶数，必须是正整数
     * @return 到达楼顶的不同方法数
     */
    public int memoizationClimb(int n) {
        if (n <= 0) {
            return 0; // 或者根据需求抛出IllegalArgumentException
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
     * @param n 当前需要计算的楼梯阶数
     * @param mem 记忆化数组，存储已计算的结果
     * @return 到达第n阶楼梯的方法数
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
     */
    public int dpClimb(int n) {
        if (n <= 0) {
            return 0;
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
}
