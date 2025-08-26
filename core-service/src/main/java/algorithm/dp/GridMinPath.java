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

    /**
     * 使用深度优先搜索计算从左上角到(i,j)位置的最小路径和
     *
     * @param grid 二维网格数组
     * @param i 当前行索引
     * @param j 当前列索引
     * @return 从(0,0)到(i,j)的最小路径和
     */
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
     * 使用记忆化搜索计算从左上角到(i,j)位置的最小路径和
     *
     * 时间复杂度：O(m*n)，每个格子只计算一次
     * 空间复杂度：O(m*n)，用于存储dp数组
     *
     * @param grid 二维网格数组
     * @param i 目标行索引
     * @param j 目标列索引
     * @return 从(0,0)到(i,j)的最小路径和
     */
    int minPathSumMemoization(int[][] grid, int i, int j) {

        // 这里有个假设，虽然我们不知道网格矩阵实际上多大，但是我们只遍历 [0, 0] 到 [i, j]，所以这里可以假设网格矩阵大小为i*j
        Integer[][] dp = new Integer[i + 1][j + 1]; // 易错的点，我们不能初始化0,0大小的数组。
        return minPathSumMemoization(grid, i, j, dp);
    }

    /**
     * 记忆化搜索的私有实现方法
     *
     * 使用Integer数组而非int数组，利用null值判断缓存状态
     * 避免了int数组默认值0与真实路径和为0的冲突
     *
     * @param grid 二维网格数组
     * @param i 当前行索引
     * @param j 当前列索引
     * @param dp 记忆化缓存数组
     * @return 从(0,0)到(i,j)的最小路径和
     */
    private int minPathSumMemoization(int[][] grid, int i, int j, Integer[][] dp) {
        // 若为左上角单元格，则终止搜索
        if (i == 0 && j == 0) {
            return grid[0][0];
        }

        // 若行列索引越界，则返回 +∞ 代价
        if (i < 0 || j < 0) {
            return Integer.MAX_VALUE;
        }
        // 检查缓存，避免重复计算
        if (dp[i][j] != null) {
            return dp[i][j];
        }
        // 递归计算上方和左方的最小路径和
        int up = minPathSumMemoization(grid, i - 1, j, dp);
        int left = minPathSumMemoization(grid, i, j - 1, dp);
        // 缓存计算结果：取上方和左方的最小值加上当前格子的值
        dp[i][j] = Math.min(left, up) + grid[i][j];

        return dp[i][j];
    }

}
