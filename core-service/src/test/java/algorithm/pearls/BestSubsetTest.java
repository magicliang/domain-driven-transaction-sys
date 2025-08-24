package algorithm.pearls;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
