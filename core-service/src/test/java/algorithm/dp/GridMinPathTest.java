package algorithm.dp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * GridMinPath的单元测试类
 */
public class GridMinPathTest {

    private final GridMinPath solution = new GridMinPath();

    @Test
    public void testBasic3x3Grid() {
        int[][] grid = {
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}
        };
        assertEquals(7, solution.minPathSum(grid));
    }

    @Test
    public void testSingleRow() {
        int[][] grid = {
                {1, 2, 3}
        };
        assertEquals(6, solution.minPathSum(grid));
    }

    @Test
    public void testSingleColumn() {
        int[][] grid = {
                {1},
                {2},
                {3}
        };
        assertEquals(6, solution.minPathSum(grid));
    }

    @Test
    public void testSingleCell() {
        int[][] grid = {
                {5}
        };
        assertEquals(5, solution.minPathSum(grid));
    }

    @Test
    public void testGridWithZeros() {
        int[][] grid = {
                {0, 1, 0},
                {1, 1, 1},
                {0, 1, 0}
        };
        assertEquals(2, solution.minPathSum(grid));
    }

    @Test
    public void testLargerGrid() {
        int[][] grid = {
                {1, 4, 7, 11},
                {2, 5, 8, 12},
                {3, 6, 9, 16}
        };
        assertEquals(37, solution.minPathSum(grid));
    }

    @Test
    public void testEmptyGrid() {
        int[][] grid = {};
        assertEquals(0, solution.minPathSum(grid));
    }

    @Test
    public void testNullGrid() {
        assertEquals(0, solution.minPathSum(null));
    }

    @Test
    public void testComplexPath() {
        int[][] grid = {
                {1, 3, 1, 2},
                {1, 5, 1, 3},
                {4, 2, 1, 1},
                {3, 1, 2, 1}
        };
        assertEquals(9, solution.minPathSum(grid));
    }
}