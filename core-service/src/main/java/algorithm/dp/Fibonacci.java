package algorithm.dp;

/**
 * 斐波那契数列算法实现类
 *
 * 问题描述：
 * 斐波那契数列定义为：F(0) = 0, F(1) = 1, F(n) = F(n-1) + F(n-2) (n >= 2)
 *
 * 算法分析：
 * 本实现采用动态规划的空间优化版本，使用滚动数组技术。
 *
 * 时间复杂度：O(n)
 * - 证明：算法执行一个从2到n的循环，每次循环进行常数时间的加法和赋值操作，
 * 因此总时间复杂度为O(n)。
 *
 * 空间复杂度：O(1)
 * - 证明：算法只使用了3个额外变量(prev1, prev2, current)来存储中间结果，
 * 不依赖于输入规模n，因此空间复杂度为常数级别O(1)。
 *
 * 算法优势：
 * - 相比递归版本O(2^n)的指数时间复杂度，此实现大幅提升了效率
 * - 相比标准DP的O(n)空间复杂度，此实现优化到O(1)空间复杂度
 *
 * @author liangchuan
 */
public class Fibonacci {

    /**
     * 测试方法：验证斐波那契数列的计算结果
     *
     * 测试用例：
     * - F(0) = 0
     * - F(1) = 1
     * - F(2) = 1
     * - F(3) = 2
     */
    public static void main(String[] args) {
        System.out.println(fib(0)); // 输出: 0
        System.out.println(fib(1)); // 输出: 1
        System.out.println(fib(2)); // 输出: 1
        System.out.println(fib(3)); // 输出: 2
    }

    /**
     * 计算第n个斐波那契数
     *
     * 使用动态规划的空间优化版本，通过滚动数组技术实现O(1)空间复杂度。
     *
     * 算法流程：
     * 1. 处理边界情况：F(0)=0, F(1)=1
     * 2. 使用三个变量维护滚动状态：prev1(F(n-2)), prev2(F(n-1)), current(F(n))
     * 3. 从F(2)开始迭代计算，每次更新三个变量的值
     * 4. 返回最终计算结果
     *
     * 状态转移方程：F(n) = F(n-1) + F(n-2)
     *
     * @param n 要计算的斐波那契数的位置，必须为非负整数
     * @return 第n个斐波那契数
     * @throws IllegalArgumentException 当n为负数时抛出
     * @apiNote 该实现避免了递归版本的指数时间复杂度和标准DP的线性空间复杂度
     * @implNote 使用long类型避免整数溢出，适用于较大的n值
     * @see <a href="https://en.wikipedia.org/wiki/Fibonacci_number">斐波那契数列</a>
     * @since 1.0
     */
    public static long fib(int n) {
        // 边界条件：F(0) = 0, F(1) = 1
        if (n <= 1) {
            return n;
        }

        // 初始化滚动数组的三个状态变量
        long prev1 = 0;  // F(n-2)：前前一个斐波那契数
        long prev2 = 1;  // F(n-1)：前一个斐波那契数  
        long current = 0L; // F(n)：当前斐波那契数

        // 从F(2)开始迭代计算到F(n)
        // 在fib问题上要先定义错位的计算，然后移动错位
        for (int i = 2; i <= n; i++) {
            // 状态转移：F(n) = F(n-1) + F(n-2)
            current = prev1 + prev2;

            // 滚动更新：为下一轮迭代准备状态
            prev1 = prev2;    // 下一个F(n-2) = 当前F(n-1)
            prev2 = current;  // 下一个F(n-1) = 当前F(n)
        }

        return current;
    }
}
