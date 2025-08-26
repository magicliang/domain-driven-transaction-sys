package algorithm.dp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ClimbSteps类的测试用例
 *
 * 测试策略：
 * 1. 边界值测试：n=0,1,2
 * 2. 正常值测试：n=3,4,5,10
 * 3. 大值测试：n=20,45（验证性能和正确性）
 * 4. 两种实现的一致性测试
 */
public class ClimbStepsTest {

    private ClimbSteps climbSteps;

    @BeforeEach
    public void setUp() {
        climbSteps = new ClimbSteps();
    }

    /**
     * 测试边界条件：n=0
     * 预期结果：返回0
     */
    @Test
    public void testMemoizationClimb_N0() {
        assertEquals(0, climbSteps.memoizationClimb(0));
        assertEquals(0, climbSteps.dpClimb(0));
    }

    /**
     * 测试边界条件：n=1
     * 预期结果：返回1（只有一种方法：爬1阶）
     */
    @Test
    public void testMemoizationClimb_N1() {
        assertEquals(1, climbSteps.memoizationClimb(1));
        assertEquals(1, climbSteps.dpClimb(1));
    }

    /**
     * 测试边界条件：n=2
     * 预期结果：返回2（两种方法：1+1 或 直接爬2阶）
     */
    @Test
    public void testMemoizationClimb_N2() {
        assertEquals(2, climbSteps.memoizationClimb(2));
        assertEquals(2, climbSteps.dpClimb(2));
    }

    /**
     * 测试正常值：n=3
     * 预期结果：返回3（三种方法：1+1+1, 1+2, 2+1）
     */
    @Test
    public void testMemoizationClimb_N3() {
        assertEquals(3, climbSteps.memoizationClimb(3));
        assertEquals(3, climbSteps.dpClimb(3));
    }

    /**
     * 测试正常值：n=4
     * 预期结果：返回5
     */
    @Test
    public void testMemoizationClimb_N4() {
        assertEquals(5, climbSteps.memoizationClimb(4));
        assertEquals(5, climbSteps.dpClimb(4));
    }

    /**
     * 测试正常值：n=5
     * 预期结果：返回8
     */
    @Test
    public void testMemoizationClimb_N5() {
        assertEquals(8, climbSteps.memoizationClimb(5));
        assertEquals(8, climbSteps.dpClimb(5));
    }

    /**
     * 测试较大值：n=10
     * 验证算法的正确性
     */
    @Test
    public void testMemoizationClimb_N10() {
        assertEquals(89, climbSteps.memoizationClimb(10));
        assertEquals(89, climbSteps.dpClimb(10));
    }

    /**
     * 测试大值：n=20
     * 验证算法的性能和正确性
     */
    @Test
    public void testMemoizationClimb_N20() {
        assertEquals(10946, climbSteps.memoizationClimb(20));
        assertEquals(10946, climbSteps.dpClimb(20));
    }

    /**
     * 测试更大值：n=45
     * 验证算法在接近int上限时的表现
     */
    @Test
    public void testMemoizationClimb_N45() {
        assertEquals(1836311903, climbSteps.memoizationClimb(45));
        assertEquals(1836311903, climbSteps.dpClimb(45));
    }

    /**
     * 测试两种实现的一致性
     * 验证记忆化搜索和动态规划的结果一致
     */
    @Test
    public void testConsistencyBetweenImplementations() {
        for (int i = 0; i <= 30; i++) {
            assertEquals(
                    climbSteps.memoizationClimb(i),
                    climbSteps.dpClimb(i),
                    "两种实现在n=" + i + "时不一致"
            );
        }
    }

    /**
     * 测试负值输入
     * 预期结果：返回0
     */
    @Test
    public void testNegativeInput() {
        assertEquals(0, climbSteps.memoizationClimb(-1));
        assertEquals(0, climbSteps.dpClimb(-5));
    }

    /**
     * 性能测试：比较两种实现的执行时间
     * 注意：这是一个简单的性能比较，实际结果可能因系统而异
     */
    @Test
    public void testPerformanceComparison() {
        int testValue = 40;

        // 测试记忆化搜索
        long startTime1 = System.nanoTime();
        int result1 = climbSteps.memoizationClimb(testValue);
        long endTime1 = System.nanoTime();
        long duration1 = endTime1 - startTime1;

        // 测试动态规划
        long startTime2 = System.nanoTime();
        int result2 = climbSteps.dpClimb(testValue);
        long endTime2 = System.nanoTime();
        long duration2 = endTime2 - startTime2;

        // 验证结果一致性
        assertEquals(result1, result2);

        // 打印性能信息（可选）
        System.out.println("记忆化搜索耗时: " + duration1 + " ns");
        System.out.println("动态规划耗时: " + duration2 + " ns");
    }
}