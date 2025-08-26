package algorithm.dp;


/**
 * project name: domain-driven-transaction-sys
 *
 * description: 网格最小路径和的问题
 *
 * @author magicliang
 *
 *         date: 2025-08-26 20:58
 */
public class GridMinPath {

    int minPathSumDFS(int[][] grid, int i, int j) {
        // 若为左上角单元格，则终止搜索
        if (i == 0 && j == 0) {
            return grid[0][0];
        }

        // 若行列索引越界，则返回 +∞ 代价
        if (i < 0 || j < 0) {
            return Integer.MAX_VALUE;
        }

        // 计算从左上角到 (i-1, j) 和 (i, j-1) 的最小路径代价，这个地方可以做成类似左右子树的问题很漂亮
        int up = minPathSumDFS(grid, i - 1, j);
        int left = minPathSumDFS(grid, i, j - 1);

        // 返回从左上角到 (i, j) 的最小路径代价，每一层递归增加的值在这里：grid[i][j]
        // 这里其实可以做成一个类似左右子树的问题
        return Math.min(left, up) + grid[i][j];
    }

    /**
     * 计算从左上角到右下角的最小路径和
     *
     * @param grid 二维网格，每个格子包含非负整数
     * @return 最小路径和
     */
    public int minPathSum(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int m = grid.length;
        int n = grid[0].length;
        return minPathSumDFS(grid, m - 1, n - 1);
    }

}
