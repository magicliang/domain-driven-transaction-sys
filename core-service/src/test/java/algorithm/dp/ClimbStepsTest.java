package algorithm.dp;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * ClimbSteps类的全量测试用例
 * 测试覆盖范围：
 * - 正常功能测试
 * - 边界条件测试
 * - 异常处理测试
 * - 性能测试（大输入）
 */
@DisplayName("爬楼梯算法测试")
class ClimbStepsTest {

    private ClimbSteps climbSteps;

    @BeforeEach
    void setUp() {
        climbSteps = new ClimbSteps();
    }

    // ==================== memoizationClimb 方法测试 ====================

    @Test
    @DisplayName("memoizationClimb - 正常情况 n=1")
    void testMemoizationClimb_N1() {
        assertEquals(1, climbSteps.memoizationClimb(1));
    }

    @Test
    @DisplayName("memoizationClimb - 正常情况 n=2")
    void testMemoizationClimb_N2() {
        assertEquals(2, climbSteps.memoizationClimb(2));
    }

    @Test
    @DisplayName("memoizationClimb - 正常情况 n=3")
    void testMemoizationClimb_N3() {
        assertEquals(3, climbSteps.memoizationClimb(3));
    }

    @Test
    @DisplayName("memoizationClimb - 正常情况 n=4")
    void testMemoizationClimb_N4() {
        assertEquals(5, climbSteps.memoizationClimb(4));
    }

    @Test
    @DisplayName("memoizationClimb - 正常情况 n=5")
    void testMemoizationClimb_N5() {
        assertEquals(8, climbSteps.memoizationClimb(5));
    }

    @ParameterizedTest
    @DisplayName("memoizationClimb - 多个正常值测试")
    @CsvSource({
            "1, 1",
            "2, 2",
            "3, 3",
            "4, 5",
            "5, 8",
            "6, 13",
            "7, 21",
            "8, 34",
            "9, 55",
            "10, 89"
    })
    void testMemoizationClimb_MultipleValues(int n, int expected) {
        assertEquals(expected, climbSteps.memoizationClimb(n));
    }

    @Test
    @DisplayName("memoizationClimb - 大数值测试")
    void testMemoizationClimb_LargeValue() {
        assertEquals(1836311903, climbSteps.memoizationClimb(45));
    }

    @Test
    @DisplayName("memoizationClimb - 边界值 n=0 应抛出异常")
    void testMemoizationClimb_Zero() {
        assertThrows(IllegalArgumentException.class, () -> climbSteps.memoizationClimb(0));
    }

    @Test
    @DisplayName("memoizationClimb - 边界值 n=-1 应抛出异常")
    void testMemoizationClimb_Negative() {
        assertThrows(IllegalArgumentException.class, () -> climbSteps.memoizationClimb(-1));
    }

    @ParameterizedTest
    @DisplayName("memoizationClimb - 多个负值测试")
    @ValueSource(ints = {-100, -10, -1, 0})
    void testMemoizationClimb_InvalidValues(int n) {
        assertThrows(IllegalArgumentException.class, () -> climbSteps.memoizationClimb(n));
    }

    // ==================== dpClimb 方法测试 ====================

    @Test
    @DisplayName("dpClimb - 正常情况 n=1")
    void testDpClimb_N1() {
        assertEquals(1, climbSteps.dpClimb(1));
    }

    @Test
    @DisplayName("dpClimb - 正常情况 n=2")
    void testDpClimb_N2() {
        assertEquals(2, climbSteps.dpClimb(2));
    }

    @Test
    @DisplayName("dpClimb - 正常情况 n=3")
    void testDpClimb_N3() {
        assertEquals(3, climbSteps.dpClimb(3));
    }

    @Test
    @DisplayName("dpClimb - 正常情况 n=4")
    void testDpClimb_N4() {
        assertEquals(5, climbSteps.dpClimb(4));
    }

    @ParameterizedTest
    @DisplayName("dpClimb - 多个正常值测试")
    @CsvSource({
            "1, 1",
            "2, 2",
            "3, 3",
            "4, 5",
            "5, 8",
            "6, 13",
            "7, 21",
            "8, 34",
            "9, 55",
            "10, 89",
            "20, 10946",
            "30, 1346269"
    })
    void testDpClimb_MultipleValues(int n, int expected) {
        assertEquals(expected, climbSteps.dpClimb(n));
    }

    @Test
    @DisplayName("dpClimb - 大数值测试")
    void testDpClimb_LargeValue() {
        assertEquals(1836311903, climbSteps.dpClimb(45));
    }

    @Test
    @DisplayName("dpClimb - 边界值 n=0 应抛出异常")
    void testDpClimb_Zero() {
        assertThrows(IllegalArgumentException.class, () -> climbSteps.dpClimb(0));
    }

    @Test
    @DisplayName("dpClimb - 边界值 n=-1 应抛出异常")
    void testDpClimb_Negative() {
        assertThrows(IllegalArgumentException.class, () -> climbSteps.dpClimb(-1));
    }

    // ==================== dpClimbWithAfterEffect 方法测试 ====================

    @Test
    @DisplayName("dpClimbWithAfterEffect - 正常情况 n=1")
    void testDpClimbWithAfterEffect_N1() {
        assertEquals(1, climbSteps.dpClimbWithAfterEffect(1));
    }

    @Test
    @DisplayName("dpClimbWithAfterEffect - 正常情况 n=2")
    void testDpClimbWithAfterEffect_N2() {
        assertEquals(1, climbSteps.dpClimbWithAfterEffect(2));
    }

    @Test
    @DisplayName("dpClimbWithAfterEffect - 正常情况 n=3")
    void testDpClimbWithAfterEffect_N3() {
        assertEquals(2, climbSteps.dpClimbWithAfterEffect(3));
    }

    @Test
    @DisplayName("dpClimbWithAfterEffect - 正常情况 n=4")
    void testDpClimbWithAfterEffect_N4() {
        assertEquals(2, climbSteps.dpClimbWithAfterEffect(4));
    }

    @Test
    @DisplayName("dpClimbWithAfterEffect - 正常情况 n=5")
    void testDpClimbWithAfterEffect_N5() {
        assertEquals(3, climbSteps.dpClimbWithAfterEffect(5));
    }

    @Test
    @DisplayName("dpClimbWithAfterEffect - 正常情况 n=6")
    void testDpClimbWithAfterEffect_N6() {
        assertEquals(4, climbSteps.dpClimbWithAfterEffect(6));
    }

    @ParameterizedTest
    @DisplayName("dpClimbWithAfterEffect - 多个正常值测试")
    @CsvSource({
            "1, 1",
            "2, 1",
            "3, 2",
            "4, 2",
            "5, 3",
            "6, 4",
            "7, 5",
            "8, 7",
            "9, 9",
            "10, 12"
    })
    void testDpClimbWithAfterEffect_MultipleValues(int n, int expected) {
        assertEquals(expected, climbSteps.dpClimbWithAfterEffect(n));
    }

    @Test
    @DisplayName("dpClimbWithAfterEffect - 边界值 n=0 应抛出异常")
    void testDpClimbWithAfterEffect_Zero() {
        assertThrows(IllegalArgumentException.class, () -> climbSteps.dpClimbWithAfterEffect(0));
    }

    @Test
    @DisplayName("dpClimbWithAfterEffect - 边界值 n=-1 应抛出异常")
    void testDpClimbWithAfterEffect_Negative() {
        assertThrows(IllegalArgumentException.class, () -> climbSteps.dpClimbWithAfterEffect(-1));
    }

    // ==================== 方法一致性测试 ====================

    @ParameterizedTest
    @DisplayName("所有方法结果一致性测试")
    @CsvSource({
            "1, 1",
            "2, 2",
            "3, 3",
            "4, 5",
            "5, 8",
            "6, 13",
            "7, 21",
            "8, 34",
            "9, 55",
            "10, 89",
            "15, 987",
            "20, 10946"
    })
    void testAllMethodsConsistency(int n, int expected) {
        assertAll("所有方法结果应该一致",
                () -> assertEquals(expected, climbSteps.memoizationClimb(n), "memoizationClimb 方法"),
                () -> assertEquals(expected, climbSteps.dpClimb(n), "dpClimb 方法")
        );
    }

    @ParameterizedTest
    @DisplayName("带约束方法的特殊值测试")
    @CsvSource({
            "1, 1",
            "2, 1",
            "3, 2",
            "4, 2",
            "5, 3",
            "6, 4",
            "7, 5",
            "8, 7",
            "9, 9",
            "10, 12",
            "11, 16",
            "12, 21",
            "13, 28",
            "14, 37",
            "15, 49",
            "16, 65",
            "17, 86",
            "18, 114",
            "19, 151",
            "20, 200"
    })
    void testConstrainedMethod(int n, int expected) {
        assertEquals(expected, climbSteps.dpClimbWithAfterEffect(n));
    }

    // ==================== 性能测试 ====================

    @Test
    @DisplayName("性能测试 - 大数值计算")
    void testPerformance_LargeValues() {
        long startTime = System.nanoTime();
        int result1 = climbSteps.memoizationClimb(40);
        long memoTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        int result2 = climbSteps.dpClimb(40);
        long dpTime = System.nanoTime() - startTime;

        assertEquals(result1, result2, "两种方法结果应该相同");

        // 输出性能信息（可选）
        System.out.printf("memoizationClimb(40) took %d ns%n", memoTime);
        System.out.printf("dpClimb(40) took %d ns%n", dpTime);
    }

    // ==================== 边界测试 ====================

    @Test
    @DisplayName("边界测试 - 最大值测试")
    void testBoundary_MaxValue() {
        // 测试接近整数溢出的值
        assertEquals(1134903170, climbSteps.dpClimb(44));
        assertEquals(1134903170, climbSteps.memoizationClimb(44));
    }

    @Test
    @DisplayName("边界测试 - 整数溢出检查")
    void testBoundary_IntegerOverflow() {
        // 测试第46个斐波那契数会溢出
        assertTrue(climbSteps.dpClimb(46) < 0, "第46个斐波那契数应该溢出为负数");
    }
}