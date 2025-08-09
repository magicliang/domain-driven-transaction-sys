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
