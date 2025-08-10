package algorithm.math; // 确保包名正确

// 导入基础的 JUnit 5 注解和断言

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * FastPower 类的 JUnit 5 测试用例。
 * 测试支持负数指数的快速幂算法。
 * 使用兼容性更好的 JUnit 5 API 特性。
 *
 * @author liangchuan
 * @version 1.1
 */
// @DisplayName("FastPower Class Tests") // 移除顶层 DisplayName
public class FastPowerTest { // 使用 public 类访问修饰符

    // --- 基本功能和正指数测试 (使用 double) ---
    // @ParameterizedTest
    // @DisplayName("Test basic power calculations with non-negative exponents") // 移除 DisplayName
    @ParameterizedTest(name = "Base: {0}, Exponent: {1}, Expected: {2}") // 使用 name 属性提供描述，更基础
    @CsvSource({
            "2, 10, 1024.0",
            "3, 0, 1.0",
            "5, 1, 5.0",
            "1, 100, 1.0",
            "0, 5, 0.0",
            "3, 5, 243.0",
            "2, 30, 1073741824.0",
            "-2, 3, -8.0",
            "-2, 4, 16.0"
    })
    public void testPowerBottomUpWithNonNegativeExponents(int base, int exponent, double expected) {
        double result = FastPower.powerBottomUpWithNegative(base, exponent);
        // assertEquals(expected, result, 0.0); // 简化断言，不使用 lambda 消息
        assertEquals(expected, result, 0.0, "Result of " + base + "^" + exponent + " is incorrect.");
    }

    // --- 负指数测试 ---
    // @ParameterizedTest
    // @DisplayName("Test power calculations with negative exponents")
    @ParameterizedTest(name = "Base: {0}, Exponent: {1}, Expected: {2}")
    @CsvSource({
            "2, -3, 0.125",
            "10, -1, 0.1",
            "5, -2, 0.04",
            "-3, -2, 0.1111111111111111"
    })
    public void testPowerBottomUpWithNegativeExponents(int base, int exponent, double expected) {
        double result = FastPower.powerBottomUpWithNegative(base, exponent);
        double delta = 1e-12;
        // assertEquals(expected, result, delta);
        assertEquals(expected, result, delta, "Result of " + base + "^" + exponent + " is incorrect (within delta).");
    }

    // --- 边界和特殊情况测试 ---
    // @Test
    // @DisplayName("Test power with base 0 and exponent 0 (0^0)")
    @Test
    public void testPowerZeroToThePowerOfZero() {
        double result = FastPower.powerBottomUpWithNegative(0, 0);
        // assertEquals(1.0, result, 0.0);
        assertEquals(1.0, result, 0.0, "0^0 should be 1.0");
    }

    // @Test
    // @DisplayName("Test power with base 0 and negative exponent (should throw exception)")
    @Test
    public void testPowerZeroToNegativeExponent() {
        int base = 0;
        int negativeExponent = -2;

        // 不使用 assertThrows，直接在 try-catch 中断言
        try {
            FastPower.powerBottomUpWithNegative(base, negativeExponent);
            // 如果没有抛出异常，则测试失败
            fail("Expected ArithmeticException to be thrown for 0^-n");
        } catch (ArithmeticException e) {
            // 预期捕获到 ArithmeticException，测试通过
            // 可以进一步断言异常消息，如果需要的话
            // assertEquals("Expected error message", e.getMessage());
            // 这里只要捕获到异常就算通过
        } catch (Exception e) {
            // 如果抛出了其他类型的异常，则测试失败
            fail("Expected ArithmeticException, but " + e.getClass().getSimpleName() + " was thrown.");
        }
    }

    // --- 大数值测试 ---
    // @Test
    // @DisplayName("Test power with larger exponent")
    @Test
    public void testPowerLargerExponent() {
        int base = 2;
        int exponent = 60;
        double expected = 1152921504606846976.0;
        double result = FastPower.powerBottomUpWithNegative(base, exponent);
        // assertEquals(expected, result, 0.0);
        assertEquals(expected, result, 0.0, "2^60 calculation failed.");
    }

    // @Test
    // @DisplayName("Test power with larger negative exponent")
    @Test
    public void testPowerLargerNegativeExponent() {
        int base = 2;
        int exponent = -10;
        double expected = 1.0 / 1024.0;
        double result = FastPower.powerBottomUpWithNegative(base, exponent);
        // assertEquals(expected, result, 0.0);
        assertEquals(expected, result, 0.0, "2^-10 calculation failed.");
    }

    // --- 重复主函数中的测试逻辑 ---
    // @Test
    // @DisplayName("Replicate main method test case: (-2)^10")
    @Test
    public void testMainMethodCaseNegativeBaseEvenExponent() {
        int base = -2;
        int exp = 10;
        double expected = 1024.0;
        double result = FastPower.powerBottomUpWithNegative(base, exp);
        // assertEquals(expected, result, 0.0);
        assertEquals(expected, result, 0.0, "(-2)^10 calculation failed.");
    }

    // @Test
    // @DisplayName("Replicate main method test case: (-3)^5")
    @Test
    public void testMainMethodCaseNegativeBaseOddExponent() {
        int base = -3;
        int exp = 5;
        double expected = -243.0;
        double result = FastPower.powerBottomUpWithNegative(base, exp);
        // assertEquals(expected, result, 0.0);
        assertEquals(expected, result, 0.0, "(-3)^5 calculation failed.");
    }
}
