package algorithm.pearls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class BestSubsetTest {

    @Test
    void testGreedyLessOrEqual_NullArray() {
        BestSubset bestSubset = new BestSubset();
        assertFalse(bestSubset.greedyLessOrEqual(10.0, 3, null));
    }

    @Test
    void testGreedyLessOrEqual_ArrayLengthLessThanK() {
        BestSubset bestSubset = new BestSubset();
        double[] arr = {1.0, 2.0};
        assertFalse(bestSubset.greedyLessOrEqual(10.0, 3, arr));
    }

    @Test
    void testGreedyLessOrEqual_NormalCase() {
        BestSubset bestSubset = new BestSubset();
        double[] arr = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertTrue(bestSubset.greedyLessOrEqual(6.0, 3, arr));
        assertFalse(bestSubset.greedyLessOrEqual(5.0, 3, arr));
    }

    @Test
    void testGreedyLessOrEqual_EdgeCase() {
        BestSubset bestSubset = new BestSubset();
        double[] arr = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertTrue(bestSubset.greedyLessOrEqual(15.0, 5, arr));
        assertTrue(bestSubset.greedyLessOrEqual(1.0, 1, arr));
    }

    @Test
    void testGreedyLessOrEqual_NegativeNumbers() {
        BestSubset bestSubset = new BestSubset();
        double[] arr = {-1.0, -2.0, -3.0, 4.0, 5.0};
        assertTrue(bestSubset.greedyLessOrEqual(-6.0, 3, arr));
        assertFalse(bestSubset.greedyLessOrEqual(-7.0, 3, arr));
    }

    @Test
    void testQuickSelectLessOrEqual_NullArray() {
        BestSubset bestSubset = new BestSubset();
        assertFalse(bestSubset.quickSelectLessOrEqual(10.0, 3, null));
    }

    @Test
    void testQuickSelectLessOrEqual_ArrayLengthLessThanK() {
        BestSubset bestSubset = new BestSubset();
        double[] arr = {1.0, 2.0};
        assertFalse(bestSubset.quickSelectLessOrEqual(10.0, 3, arr));
    }

    @Test
    void testQuickSelectLessOrEqual_NormalCase() {
        BestSubset bestSubset = new BestSubset();
        double[] arr = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertTrue(bestSubset.quickSelectLessOrEqual(6.0, 3, arr));
        assertFalse(bestSubset.quickSelectLessOrEqual(5.0, 3, arr));
    }

    @Test
    void testQuickSelectLessOrEqual_EdgeCase() {
        BestSubset bestSubset = new BestSubset();
        double[] arr = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertTrue(bestSubset.quickSelectLessOrEqual(15.0, 5, arr));
        assertTrue(bestSubset.quickSelectLessOrEqual(1.0, 1, arr));
    }

    @Test
    void testQuickSelectLessOrEqual_NegativeNumbers() {
        BestSubset bestSubset = new BestSubset();
        double[] arr = {-1.0, -2.0, -3.0, 4.0, 5.0};
        assertTrue(bestSubset.quickSelectLessOrEqual(-6.0, 3, arr));
        assertFalse(bestSubset.quickSelectLessOrEqual(-7.0, 3, arr));
    }

    @Test
    void testExistsKSubsetWithSumLE_NullArray() {
        assertFalse(BestSubset.existsKSubsetWithSumLE(null, 10.0, 3));
    }

    @Test
    void testExistsKSubsetWithSumLE_ArrayLengthLessThanK() {
        double[] arr = {1.0, 2.0};
        assertFalse(BestSubset.existsKSubsetWithSumLE(arr, 10.0, 3));
    }

    @Test
    void testExistsKSubsetWithSumLE_NormalCase() {
        double[] arr = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertTrue(BestSubset.existsKSubsetWithSumLE(arr, 6.0, 3));
        assertFalse(BestSubset.existsKSubsetWithSumLE(arr, 5.0, 3));
    }

    @Test
    void testExistsKSubsetWithSumLE_NegativeNumbers() {
        double[] arr = {-1.0, -2.0, -3.0, 4.0, 5.0};
        assertTrue(BestSubset.existsKSubsetWithSumLE(arr, -6.0, 3));
        assertFalse(BestSubset.existsKSubsetWithSumLE(arr, -7.0, 3));
    }

    @Test
    void testFindKSubsetWithSumLE_NullArray() {
        assertNull(BestSubset.findKSubsetWithSumLE(null, 10.0, 3));
    }

    @Test
    void testFindKSubsetWithSumLE_ArrayLengthLessThanK() {
        double[] arr = {1.0, 2.0};
        assertNull(BestSubset.findKSubsetWithSumLE(arr, 10.0, 3));
    }

    @Test
    void testFindKSubsetWithSumLE_NormalCase() {
        double[] arr = {1.0, 2.0, 3.0, 4.0, 5.0};
        List<Double> result = BestSubset.findKSubsetWithSumLE(arr, 6.0, 3);
        assertNotNull(result);
        assertEquals(3, result.size());
        double sum = result.stream().mapToDouble(Double::doubleValue).sum();
        assertTrue(sum <= 6.0);
    }

    @Test
    void testFindKSubsetWithSumLE_NoSolution() {
        double[] arr = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertNull(BestSubset.findKSubsetWithSumLE(arr, 5.0, 3));
    }

    @Test
    void testFindKSubsetWithSumLE_WithNegativeNumbers() {
        double[] arr = {-1.0, -2.0, -3.0, 4.0, 5.0};
        List<Double> result = BestSubset.findKSubsetWithSumLE(arr, -6.0, 3);
        assertNotNull(result);
        assertEquals(3, result.size());
        double sum = result.stream().mapToDouble(Double::doubleValue).sum();
        assertTrue(sum <= -6.0);

        // 验证找到的是三个负数
        assertTrue(result.stream().allMatch(x -> x < 0));
    }

    @Test
    void testBacktrackVsGreedy_Difference() {
        // 这个测试用例展示回溯和贪心算法的区别
        // 贪心算法会选择前3个最小的：[-3, -2, -1] = -6
        // 回溯算法能找到不同的解：[-3, -2, 4] = -1
        double[] arr = {-3.0, -2.0, -1.0, 4.0, 5.0};
        
        // 贪心算法会返回true，因为前3个最小的和是-6 <= -1
        BestSubset bestSubset = new BestSubset();
        assertTrue(bestSubset.greedyLessOrEqual(-1.0, 3, arr));

        // 回溯算法也能找到解[-3, -2, 4] = -1 <= -1
        assertTrue(BestSubset.existsKSubsetWithSumLE(arr, -1.0, 3));

        // 验证回溯算法找到的具体子集
        List<Double> result = BestSubset.findKSubsetWithSumLE(arr, -1.0, 3);
        assertNotNull(result);
        assertEquals(3, result.size());
        double sum = result.stream().mapToDouble(Double::doubleValue).sum();
        assertTrue(sum <= -1.0);
    }
}