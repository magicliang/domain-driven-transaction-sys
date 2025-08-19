package algorithm.math;

/**
 * 快速幂算法实现类，支持负数指数。
 * <p>
 * 核心思想是“幂指数二进制分解”或“平方求幂”：
 * 将指数 n 看作二进制数，通过不断平方底数并根据指数的二进制位决定是否累乘，
 * 可以在 O(log n) 时间内计算出结果。
 * 对于负指数，利用 x^(-n) = 1 / x^n 的性质进行计算。
 * </p>
 *
 * @author liangchuan
 * @version 1.1 // 假设这是更新后的版本
 */
public class FastPower {

    /**
     * 使用自底向上的快速幂算法计算 base 的 exponent 次方 (base^exponent)。
     * <p>
     * 算法步骤：
     * 1. 处理特殊情况：指数为 0 时，返回 1.0。
     * 2. 记录指数的符号，并将指数转为正数进行计算。
     * 3. 初始化结果 result 为 1.0，当前底数 currentBase 为 base (转为 double)。
     * 4. 当指数 exponent 大于 0 时，循环执行：
     * a. 如果 exponent 是奇数 (exponent % 2 == 1)，
     * 则将当前的 currentBase 乘入 result。
     * b. 将 exponent 除以 2 (相当于二进制右移一位)。
     * c. 将 currentBase 平方 (currentBase = currentBase * currentBase)。
     * 5. 循环结束，如果原指数为负，则返回 1.0 / result；否则返回 result。
     * </p>
     * <p>
     * 时间复杂度: O(log |exponent|) - 因为每次循环都将指数的绝对值减半。
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间。
     * </p>
     *
     * @param base 底数 (整数)
     * @param exponent 指数 (整数，可正可负可为零)
     * @return base 的 exponent 次方的结果 (double 类型)
     * @see <a href="https://en.wikipedia.org/wiki/Exponentiation_by_squaring">Exponentiation by squaring</a>
     */
    public static double powerBottomUpWithNegative(int base, int exponent) {
        if (exponent == 0) {
            return 1;
        }

        boolean isNegative = false;
        if (exponent < 0) {
            exponent = -exponent;
            isNegative = true;
        }

        long result = 1L;
        long currentBase = base; // 使用 long 避免整数溢出，仅在最后一步转换为 double

        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                result *= currentBase;
            }
            currentBase *= currentBase;
            exponent >>>= 1;
        }

        return isNegative ? 1.0 / result : (double) result;
    }

    /**
     * 使用自底向上的快速幂算法计算 base 的 exponent 次方 (base^exponent)。
     * <p>
     * 算法步骤：
     * 1. 处理特殊情况：指数为 0 时，返回 1。
     * 2. 初始化结果 result 为 1。
     * 3. 当指数 exponent 大于 0 时，循环执行：
     * a. 如果 exponent 是奇数 (exponent & 1 == 1)，
     * 则将当前的 base 乘入 result，并将 exponent 减 1- 类似于 2^5 = 2 * 2 ^4。左边单拉出一个2出来乘，到最后一步，指数从4变1，base变成16。此时相当于 result * 4 ^
     * 1 但拉出来就乘好了。然后消耗掉幂，1变0，循环结束。
     * b. 如果 exponent 是偶数，则将 base 平方，并将 exponent 右移一位（相当于除以 2）。
     * 4. 循环结束，返回 result。
     * </p>
     * <p>
     * 时间复杂度: O(log exponent) - 因为每次循环都将指数减半。
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间。
     * </p>
     * <p>
     * 注意：此方法只支持非负整数指数。如果需要支持负指数，请使用 {@link #powerBottomUpWithNegative(int, int)}。
     * </p>
     *
     * @param base 底数 (整数)
     * @param exponent 指数 (非负整数)
     * @return base 的 exponent 次方的结果 (整数)
     * @throws ArithmeticException 如果结果超出int范围
     * @see <a href="https://en.wikipedia.org/wiki/Exponentiation_by_squaring">Exponentiation by squaring</a>
     */
    public static int powerBottomUp(int base, int exponent) {
        // base ^ exponent: exponent 如果是奇数，可以转成 base * base ^ exponent - 1
        // 然后每次迭代给 base 升级，exponent/2

        if (exponent == 0) {
            return 1;
        }

        int result = 1;
        // 把 2^5 拆解成 2 * 2^4 4 下折成1要循环2次，所以 base 2 要平方2次，4 变成 1 的时候，需要乘以结果了
        while (exponent > 0) {
            if ((exponent & 1) == 1) { // 另一种求奇偶的方法，最初的奇数1和最后1个1会触发一次乘法
                result *= base;
                exponent--; // 这一步减等于0就需要直接退出了
            } else {
                base *= base;
                exponent = exponent >>> 1;
            }
        }

        return result;
    }

    /**
     * 快速幂算法的主函数，用于演示和测试基本的快速幂计算。
     * <p>
     * 测试用例包括：
     * - 2^1 = 2
     * - 2^4 = 16
     * - 2^5 = 32
     * </p>
     * <p>
     * 注意：此方法仅用于演示{@link #powerBottomUp(int, int)}方法的基本功能。
     * 如需测试负数指数，请使用{@link #powerBottomUpWithNegative(int, int)}方法。
     * </p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        System.out.println(powerBottomUp(2, 1));
        System.out.println(powerBottomUp(2, 4));
        System.out.println(powerBottomUp(2, 5));
    }

}
