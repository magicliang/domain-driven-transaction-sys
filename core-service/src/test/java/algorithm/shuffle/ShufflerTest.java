package algorithm.shuffle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Shuffler类的单元测试
 *
 * 使用JUnit 5进行测试，包含功能测试、边界测试、异常测试等
 */
public class ShufflerTest {

    private final Shuffler shuffler = new Shuffler();

    @Test
    @DisplayName("基本功能测试 - 正常洗牌")
    void testBasicFunctionality() {
        // 测试1：正常情况
        int[] result = shuffler.shuffle(10, 5);
        assertEquals(5, result.length, "结果长度应为5");

        // 验证所有元素在正确范围内
        for (int num : result) {
            assertTrue(num >= 0 && num < 10, "元素应在[0,9]范围内");
        }

        // 测试2：k=0的情况
        int[] emptyResult = shuffler.shuffle(10, 0);
        assertEquals(0, emptyResult.length, "空结果长度应为0");

        // 测试3：k=n的情况
        int[] fullResult = shuffler.shuffle(5, 5);
        assertEquals(5, fullResult.length, "全选结果长度应为5");
    }

    @Test
    @DisplayName("边界情况测试")
    void testEdgeCases() {
        // 测试1：n=1, k=1
        int[] singleResult = shuffler.shuffle(1, 1);
        assertEquals(1, singleResult.length);
        assertEquals(0, singleResult[0], "唯一元素应为0");

        // 测试2：大数测试
        int[] largeResult = shuffler.shuffle(1000, 100);
        assertEquals(100, largeResult.length, "大数测试结果长度应为100");
    }

    @Test
    @DisplayName("参数验证测试 - 异常处理")
    void testParameterValidation() {
        // 测试1：n为负数
        assertThrows(IllegalArgumentException.class,
                () -> shuffler.shuffle(-1, 5),
                "应该抛出异常：n为负数");

        // 测试2：k为负数
        assertThrows(IllegalArgumentException.class,
                () -> shuffler.shuffle(10, -1),
                "应该抛出异常：k为负数");

        // 测试3：k大于n
        assertThrows(IllegalArgumentException.class,
                () -> shuffler.shuffle(5, 10),
                "应该抛出异常：k大于n");
    }

    @Test
    @DisplayName("唯一性测试 - 确保无重复元素")
    void testUniqueness() {
        int[] result = shuffler.shuffle(100, 50);
        Set<Integer> uniqueSet = new HashSet<>();

        for (int num : result) {
            assertFalse(uniqueSet.contains(num), "发现重复元素: " + num);
            uniqueSet.add(num);
        }

        assertEquals(50, uniqueSet.size(), "应该有50个唯一元素");
    }

    @Test
    @DisplayName("均匀分布测试 - 统计验证")
    void testUniformDistribution() {
        int n = 10;
        int k = 5;
        int trials = 10000;
        int[] frequency = new int[n];

        // 运行多次洗牌，统计每个数字出现的频率
        for (int i = 0; i < trials; i++) {
            int[] result = shuffler.shuffle(n, k);
            for (int num : result) {
                frequency[num]++;
            }
        }

        // 计算期望频率
        double expected = (double) (k * trials) / n;

        // 检查每个数字的频率是否接近期望值（允许20%的偏差）
        for (int i = 0; i < n; i++) {
            double ratio = frequency[i] / expected;
            assertTrue(ratio > 0.8 && ratio < 1.2,
                    "数字" + i + "的分布不均匀，比例: " + ratio);
        }
    }

    @Test
    @DisplayName("性能测试 - 大数组处理")
    void testPerformance() {
        long startTime = System.currentTimeMillis();

        // 测试大数组的性能
        int[] result = shuffler.shuffle(1000000, 1000);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertEquals(1000, result.length);
        assertTrue(duration < 1000, "性能测试应在1秒内完成，实际耗时: " + duration + "ms");
    }
}