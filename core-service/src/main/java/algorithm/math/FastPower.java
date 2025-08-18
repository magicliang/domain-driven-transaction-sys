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
     * @param base     底数 (整数)
     * @param exponent 指数 (整数，可正可负可为零)
     * @return base 的 exponent 次方的结果 (double 类型)
     * @see <a href="https://en.wikipedia.org/wiki/Exponentiation_by_squaring">Exponentiation by squaring</a>
     */
    public static double powerBottomUpWithNegative(int base, int exponent) {
        // 处理 0 的负数次幂，这是未定义的
        if (base == 0 && exponent < 0) {
            throw new ArithmeticException("Cannot raise zero to a negative power.");
        }

        // 根据数学定义，任何非零数的 0 次幂都是 1，0^0 在此也定义为 1.0
        if (exponent == 0) {
            return 1.0;
        }

        // 记录指数是否为负数
        boolean isNegativeExponent = exponent < 0;
        // 将指数转换为正数进行计算
        int positiveExponent = Math.abs(exponent);

        // 使用 double 类型存储结果和底数，以支持小数结果和防止中间溢出
        double result = 1.0;
        // 将底数转换为 double 进行计算
        double currentBase = (double) base;
        int currentExponent = positiveExponent;

        // 迭代过程：计算 base^|exponent|
        while (currentExponent > 0) {
            // 检查指数的最低二进制位是否为 1 (即指数是否为奇数)
            if (currentExponent % 2 == 1) {
                result *= currentBase;
            }
            // 将指数右移一位
            currentExponent /= 2;
            // 将当前底数平方
            currentBase *= currentBase;
        }

        // 如果原指数是负数，则返回倒数
        if (isNegativeExponent) {
            return 1.0 / result;
        } else {
            return result;
        }
    }

    public static int powerBottomUp(int base, int exponent) {
        // base ^ exponent: exponent 如果是奇数，可以转成 base * base ^ exponent - 1
        // 然后每次迭代给 base 升级，exponent/2
        if (exponent == 0) {
            return 1;
        }

        int result = 1;
        if (exponent % 2 != 0) {
            result *= base;
            exponent -= 1;
        }

        while (exponent > 1) { // 易错的点： exp 降到 1 的时候就是乘的时候，降到0base会再得到一个平方
            base = base * base;
            exponent = exponent >>> 1;
        }
        if (exponent == 1) {
            result *= base;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(powerBottomUp(2, 1));
    }

}
