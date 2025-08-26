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

    // ===== 新增的记忆化搜索方法测试 =====

    @Test
    public void testMemoizationBasic3x3Grid() {
        int[][] grid = {
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}
        };
        assertEquals(7, solution.minPathSumMemoization(grid, 2, 2));
    }

    @Test
    public void testMemoizationSingleCell() {
        int[][] grid = {{5}};
        assertEquals(5, solution.minPathSumMemoization(grid, 0, 0));
    }

    @Test
    public void testMemoizationTopLeftCorner() {
        int[][] grid = {
                {1, 2, 3},
                {4, 5, 6}
        };
        assertEquals(1, solution.minPathSumMemoization(grid, 0, 0));
    }

    @Test
    public void testMemoizationEdgeCases() {
        int[][] grid = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        // 测试边界位置
        assertEquals(6, solution.minPathSumMemoization(grid, 0, 2)); // 第一行最后一个
        assertEquals(12, solution.minPathSumMemoization(grid, 2, 0)); // 第一列最后一个
    }

    @Test
    public void testMemoizationWithZeros() {
        int[][] grid = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        assertEquals(0, solution.minPathSumMemoization(grid, 2, 2));
    }

    @Test
    public void testMemoizationLargeGrid() {
        int[][] grid = {
                {1, 3, 1, 2, 1},
                {1, 5, 1, 3, 2},
                {4, 2, 1, 1, 3},
                {3, 1, 2, 1, 4},
                {1, 2, 3, 2, 1}
        };
        assertEquals(12, solution.minPathSumMemoization(grid, 4, 4));
    }

    // ===== 验证两种方法结果一致性 =====

    @Test
    public void testConsistencyBetweenMethods() {
        int[][] grid = {
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}
        };
        int m = grid.length;
        int n = grid[0].length;

        // 验证两种方法结果一致
        assertEquals(solution.minPathSum(grid), solution.minPathSumMemoization(grid, m - 1, n - 1));
    }
}