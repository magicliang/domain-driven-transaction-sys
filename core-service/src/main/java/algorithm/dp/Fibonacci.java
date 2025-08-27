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
package algorithm.dp;

/**
 * @author liangchuan
 */
public class Fibonacci {

    public static void main(String[] args) {
        System.out.println(fib(0));
        System.out.println(fib(1));
        System.out.println(fib(2));
        System.out.println(fib(3));
    }

    public static long fib(int n) {
        if (n <= 1) {
            return n;
        }

        long prev1 = 0; // f(n-2)
        long prev2 = 1; // f(n-1)
        long current = 0L; // f(n)

        // 在 fib 问题上要先定义错位的计算，然后移动错位
        for (int i = 2; i <= n; i++) {
            current = prev1 + prev2; //  f(n) = f(n-1) +  f(n-2)

            // 为了下一轮
            prev1 = prev2; // 下一个 f(n-2) = 当前 f(n-1)
            prev2 = current; // 下一个  f(n-1) = 当前
        }
        return current;
    }
}
