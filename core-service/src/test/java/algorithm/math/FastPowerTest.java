package algorithm.math; // 确保包名正确

// 导入基础的 JUnit 5 注解和断言

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


/**
 * FastPower 类的 JUnit 5 测试用例。
 * 测试支持负数指数的快速幂算法。
 * 使用兼容性更好的 JUnit 5 API 特性。
 *
 * @author liangchuan
 * @version 1.1
 */
public class FastPowerTest {

    // --- 基本功能和正指数测试 (使用 double) ---
    @Test
    public void testPowerBottomUpWithNonNegativeExponents() {
        assertEquals(1024.0, FastPower.powerBottomUpWithNegative(2, 10), 0.0);
        assertEquals(1.0, FastPower.powerBottomUpWithNegative(3, 0), 0.0);
        assertEquals(5.0, FastPower.powerBottomUpWithNegative(5, 1), 0.0);
        assertEquals(1.0, FastPower.powerBottomUpWithNegative(1, 100), 0.0);
        assertEquals(0.0, FastPower.powerBottomUpWithNegative(0, 5), 0.0);
        assertEquals(243.0, FastPower.powerBottomUpWithNegative(3, 5), 0.0);
        assertEquals(1073741824.0, FastPower.powerBottomUpWithNegative(2, 30), 0.0);
        assertEquals(-8.0, FastPower.powerBottomUpWithNegative(-2, 3), 0.0);
        assertEquals(16.0, FastPower.powerBottomUpWithNegative(-2, 4), 0.0);
    }

    // --- 负指数测试 ---
    @Test
    public void testPowerBottomUpWithNegativeExponents() {
        double delta = 1e-12;
        assertEquals(0.125, FastPower.powerBottomUpWithNegative(2, -3), delta);
        assertEquals(0.1, FastPower.powerBottomUpWithNegative(10, -1), delta);
        assertEquals(0.04, FastPower.powerBottomUpWithNegative(5, -2), delta);
        assertEquals(0.1111111111111111, FastPower.powerBottomUpWithNegative(-3, -2), delta);
    }

    // --- 边界和特殊情况测试 ---
    @Test
    public void testPowerZeroToThePowerOfZero() {
        assertEquals(1.0, FastPower.powerBottomUpWithNegative(0, 0), 0.0);
    }

    @Test
    public void testPowerZeroToNegativeExponent() {
        double result = FastPower.powerBottomUpWithNegative(0, -2);
        assertTrue(Double.isInfinite(result), "0^-n should return Infinity");
    }

    // --- 大数值测试 ---
    @Test
    public void testPowerLargerExponent() {
        assertEquals(1152921504606846976.0, FastPower.powerBottomUpWithNegative(2, 60), 0.0);
    }

    @Test
    public void testPowerLargerNegativeExponent() {
        assertEquals(1.0 / 1024.0, FastPower.powerBottomUpWithNegative(2, -10), 0.0);
    }

    // --- 重复主函数中的测试逻辑 ---
    @Test
    public void testMainMethodCaseNegativeBaseEvenExponent() {
        assertEquals(1024.0, FastPower.powerBottomUpWithNegative(-2, 10), 0.0);
    }

    @Test
    public void testMainMethodCaseNegativeBaseOddExponent() {
        assertEquals(-243.0, FastPower.powerBottomUpWithNegative(-3, 5), 0.0);
    }

    // --- 新增：powerBottomUp 方法专门测试 ---

    /**
     * 测试 powerBottomUp 方法的基本功能（非负指数）
     */
    @Test
    public void testPowerBottomUpBasicCases() {
        assertEquals(1, FastPower.powerBottomUp(2, 0));
        assertEquals(2, FastPower.powerBottomUp(2, 1));
        assertEquals(1024, FastPower.powerBottomUp(2, 10));
        assertEquals(125, FastPower.powerBottomUp(5, 3));
        assertEquals(10000, FastPower.powerBottomUp(10, 4));
        assertEquals(1, FastPower.powerBottomUp(1, 100));
        assertEquals(1, FastPower.powerBottomUp(0, 0));
        assertEquals(0, FastPower.powerBottomUp(0, 5));
        assertEquals(-8, FastPower.powerBottomUp(-2, 3));
        assertEquals(16, FastPower.powerBottomUp(-2, 4));
        assertEquals(-243, FastPower.powerBottomUp(-3, 5));
    }

    /**
     * 测试 powerBottomUp 方法的大指数计算
     */
    @Test
    public void testPowerBottomUpLargeExponent() {
        assertEquals(1073741824, FastPower.powerBottomUp(2, 30));
        assertEquals(2147483647, FastPower.powerBottomUp(2147483647, 1));
    }

    /**
     * 测试 powerBottomUp 方法的边界值
     */
    @Test
    public void testPowerBottomUpEdgeCases() {
        // 测试最大int值的1次方
        assertEquals(Integer.MAX_VALUE, FastPower.powerBottomUp(Integer.MAX_VALUE, 1));

        // 测试最小int值的1次方
        assertEquals(Integer.MIN_VALUE, FastPower.powerBottomUp(Integer.MIN_VALUE, 1));

        // 测试-1的偶数次方
        assertEquals(1, FastPower.powerBottomUp(-1, 100));

        // 测试-1的奇数次方
        assertEquals(-1, FastPower.powerBottomUp(-1, 99));
    }

    /**
     * 测试 powerBottomUp 方法的性能（大指数）
     */
    @Test
    public void testPowerBottomUpPerformance() {
        long startTime = System.nanoTime();
        int result = FastPower.powerBottomUp(3, 19); // 3^19 = 1,162,261,467
        long endTime = System.nanoTime();

        assertEquals(1162261467, result);
        assertTrue((endTime - startTime) < 1_000_000, "Performance test took too long");
    }

    /**
     * 验证两个方法在正指数情况下结果一致性
     */
    @Test
    public void testConsistencyBetweenMethods() {
        assertEquals(FastPower.powerBottomUp(2, 5), (int) FastPower.powerBottomUpWithNegative(2, 5));
        assertEquals(FastPower.powerBottomUp(3, 10), (int) FastPower.powerBottomUpWithNegative(3, 10));
        assertEquals(FastPower.powerBottomUp(-2, 4), (int) FastPower.powerBottomUpWithNegative(-2, 4));
        assertEquals(FastPower.powerBottomUp(5, 0), (int) FastPower.powerBottomUpWithNegative(5, 0));
        assertEquals(FastPower.powerBottomUp(10, 3), (int) FastPower.powerBottomUpWithNegative(10, 3));
    }
}
