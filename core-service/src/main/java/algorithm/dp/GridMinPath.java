package algorithm.dp;


/**
 * 网格最小路径和问题的多种解决方案
 * <p>
 * 这是一个经典的动态规划问题，也是路径规划问题的基础应用。
 * 问题描述：给定一个包含非负整数的 m x n 网格 grid，请找出一条从左上角到右下角的路径，
 * 使得路径上的数字总和为最小。每次只能向下或者向右移动一步。
 * </p>
 *
 * <p>
 * <strong>问题分析：</strong><br>
 * 这是一个典型的"最优子结构"问题：
 * <ul>
 *   <li>到达位置(i,j)的最小路径和 = min(到达(i-1,j)的最小路径和, 到达(i,j-1)的最小路径和) + grid[i][j]</li>
 *   <li>状态转移方程：dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]</li>
 *   <li>边界条件：dp[0][0] = grid[0][0]，第一行和第一列只有一种走法</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>解决方案对比：</strong>
 * <table border="1">
 *   <tr>
 *     <th>方法</th>
 *     <th>时间复杂度</th>
 *     <th>空间复杂度</th>
 *     <th>特点</th>
 *   </tr>
 *   <tr>
 *     <td>朴素递归(DFS)</td>
 *     <td>O(2^(m+n))</td>
 *     <td>O(m+n)</td>
 *     <td>存在大量重复计算，效率极低</td>
 *   </tr>
 *   <tr>
 *     <td>记忆化搜索</td>
 *     <td>O(m×n)</td>
 *     <td>O(m×n)</td>
 *     <td>自顶向下，避免重复计算</td>
 *   </tr>
 *   <tr>
 *     <td>动态规划</td>
 *     <td>O(m×n)</td>
 *     <td>O(m×n)</td>
 *     <td>自底向上，逻辑清晰</td>
 *   </tr>
 *   <tr>
 *     <td>空间优化DP</td>
 *     <td>O(m×n)</td>
 *     <td>O(min(m,n))</td>
 *     <td>滚动数组优化空间</td>
 *   </tr>
 * </table>
 * </p>
 *
 * <p>
 * <strong>算法思想：</strong><br>
 * 本问题体现了动态规划的核心思想：
 * <ul>
 *   <li><strong>最优子结构：</strong>问题的最优解包含子问题的最优解</li>
 *   <li><strong>重叠子问题：</strong>递归过程中会重复计算相同的子问题</li>
 *   <li><strong>无后效性：</strong>当前状态只依赖于前面的状态，与后面的状态无关</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>实际应用场景：</strong>
 * <ul>
 *   <li>机器人路径规划：在有障碍物的网格中寻找最短路径</li>
 *   <li>游戏开发：RPG游戏中的最优移动策略</li>
 *   <li>物流优化：仓库货物搬运的最小成本路径</li>
 *   <li>图像处理：像素级别的最优路径寻找</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>扩展问题：</strong>
 * <ul>
 *   <li>带障碍物的网格路径：某些格子不能通过</li>
 *   <li>多起点多终点：从多个起点到多个终点的最优路径</li>
 *   <li>三维网格：扩展到三维空间的路径规划</li>
 *   <li>路径计数：统计从起点到终点的所有可能路径数量</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>坐标系约定：</strong><br>
 * 在本实现中，统一使用以下坐标系约定：
 * <ul>
 *   <li>i 表示行索引（row index），从上到下递增</li>
 *   <li>j 表示列索引（column index），从左到右递增</li>
 *   <li>grid[i][j] 表示第i行第j列的元素</li>
 *   <li>起点为 (0,0)，终点为 (m-1,n-1)</li>
 * </ul>
 * </p>
 *
 * @author magicliang
 * @version 1.0
 * @since 2025-08-26
 * @see <a href="https://leetcode-cn.com/problems/minimum-path-sum/">LeetCode 64. 最小路径和</a>
 * @see <a href="https://en.wikipedia.org/wiki/Dynamic_programming">Dynamic Programming - Wikipedia</a>
 */
public class GridMinPath {

    /**
     * 计算从左上角到右下角的最小路径和（使用深度优先搜索）
     * <p>
     * 这是问题的入口方法，使用朴素的深度优先搜索算法求解。
     * 该方法会调用递归的DFS实现，适合用于理解问题的本质，但效率较低。
     * </p>
     *
     * <p>
     * <strong>算法特点：</strong>
     * <ul>
     *   <li>直观易懂：完全按照问题的递归定义实现</li>
     *   <li>效率较低：存在大量重复计算，时间复杂度为指数级</li>
     *   <li>适合小规模：仅适用于小规模网格（建议不超过10x10）</li>
     * </ul>
     * </p>
     *
     * <p>
     * <strong>使用场景：</strong>
     * <ul>
     *   <li>算法学习和理解：帮助理解问题的递归结构</li>
     *   <li>小规模测试：验证算法正确性</li>
     *   <li>对比基准：与优化算法进行性能对比</li>
     * </ul>
     * </p>
     *
     * <p>
     * <strong>复杂度分析：</strong>
     * <ul>
     *   <li><strong>时间复杂度：O(2^(m+n))</strong> - 每个位置有两个递归分支，递归深度为m+n</li>
     *   <li><strong>空间复杂度：O(m+n)</strong> - 递归调用栈的最大深度</li>
     * </ul>
     * </p>
     *
     * @param grid 二维网格数组，包含非负整数，不能为null或空数组
     * @return 从左上角(0,0)到右下角(m-1,n-1)的最小路径和
     * @throws IllegalArgumentException 如果grid为null、空数组或包含负数
     *
     * @example
     * <pre>
     * GridMinPath solver = new GridMinPath();
     * int[][] grid = {
     *     {1, 3, 1},
     *     {1, 5, 1},
     *     {4, 2, 1}
     * };
     * int result = solver.minPathSum(grid); // 返回 7
     * // 最优路径：1→3→1→1→1，总和为7
     * </pre>
     *
     * @apiNote 对于大规模网格，建议使用 {@link #minPathSumMemoization(int[][], int, int)}
     *          或 {@link #minPathSumDp(int[][], int, int)} 方法
     * @see #minPathSumDFS(int[][], int, int)
     * @see #minPathSumMemoization(int[][], int, int)
     * @see #minPathSumDp(int[][], int, int)
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
     * 时间复杂度：O(2^(m+n))
     * - 每个位置有向上和向左两个递归分支
     * - 递归树的深度为 m+n-2（从(m-1,n-1)到(0,0)需要走m+n-2步）
     * - 每层最多有2个分支，总的递归调用次数约为 2^(m+n-2)
     * - 没有记忆化，存在大量重复计算
     *
     * 空间复杂度：O(m+n)
     * - 递归调用栈的最大深度为 m+n-2
     * - 每次递归调用需要常数空间存储参数
     * - 总空间复杂度为递归深度，即 O(m+n)
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
     * 使用记忆化搜索计算从左上角到指定位置的最小路径和（推荐方法）
     * <p>
     * 这是对朴素递归算法的重要优化，通过缓存已计算的结果避免重复计算。
     * 该方法结合了递归的直观性和动态规划的高效性，是解决此类问题的经典方法。
     * </p>
     *
     * <p>
     * <strong>算法原理：</strong><br>
     * 记忆化搜索（Memoization）是一种自顶向下的动态规划方法：
     * <ol>
     *   <li>使用递归分解问题：minPath(i,j) = min(minPath(i-1,j), minPath(i,j-1)) + grid[i][j]</li>
     *   <li>使用缓存数组存储已计算的结果</li>
     *   <li>每次递归前先检查缓存，避免重复计算</li>
     *   <li>将计算结果存入缓存供后续使用</li>
     * </ol>
     * </p>
     *
     * <p>
     * <strong>优势分析：</strong>
     * <ul>
     *   <li><strong>效率提升：</strong>从O(2^(m+n))降低到O(m×n)</li>
     *   <li><strong>逻辑清晰：</strong>保持了递归的直观性</li>
     *   <li><strong>易于理解：</strong>符合人类思维的自顶向下分析</li>
     *   <li><strong>通用性强：</strong>适用于各种规模的网格</li>
     * </ul>
     * </p>
     *
     * <p>
     * <strong>实现细节：</strong><br>
     * 使用Integer[][]而非int[][]作为缓存数组的原因：
     * <ul>
     *   <li>Integer默认值为null，可以明确区分"未计算"和"计算结果为0"</li>
     *   <li>int默认值为0，无法区分是否已计算过</li>
     *   <li>避免了路径和恰好为0时的判断错误</li>
     * </ul>
     * </p>
     *
     * <p>
     * <strong>复杂度分析：</strong>
     * <ul>
     *   <li><strong>时间复杂度：O(m×n)</strong> - 每个格子最多计算一次，总共m×n个格子</li>
     *   <li><strong>空间复杂度：O(m×n)</strong> - 缓存数组需要O(m×n)空间，递归栈需要O(m+n)空间</li>
     * </ul>
     * </p>
     *
     * <p>
     * <strong>适用场景：</strong>
     * <ul>
     *   <li>中大规模网格：适合处理100×100以内的网格</li>
     *   <li>一次性计算：不需要多次查询不同位置的最小路径和</li>
     *   <li>学习目的：理解记忆化搜索的经典应用</li>
     * </ul>
     * </p>
     *
     * @param grid 二维网格数组，包含非负整数，不能为null或空数组
     * @param i 目标行索引，必须在有效范围内 [0, grid.length-1]
     * @param j 目标列索引，必须在有效范围内 [0, grid[0].length-1]
     * @return 从左上角(0,0)到目标位置(i,j)的最小路径和
     * @throws IllegalArgumentException 如果参数无效
     * @throws ArrayIndexOutOfBoundsException 如果索引越界
     *
     * @example
     * <pre>
     * GridMinPath solver = new GridMinPath();
     * int[][] grid = {
     *     {1, 2, 3},
     *     {4, 5, 6}
     * };
     *
     * // 计算到右下角的最小路径和
     * int result1 = solver.minPathSumMemoization(grid, 1, 2); // 返回 12
     * // 最优路径：1→2→3→6 或 1→4→5→6，都是12
     *
     * // 计算到中间位置的最小路径和
     * int result2 = solver.minPathSumMemoization(grid, 0, 1); // 返回 3
     * // 最优路径：1→2，总和为3
     * </pre>
     *
     * @implNote 该方法创建新的缓存数组，适合单次计算。如需多次查询，
     *           考虑预先计算整个DP表
     * @see #minPathSumMemoization(int[][], int, int, Integer[][])
     * @see #minPathSumDp(int[][], int, int)
     * @since 1.0
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

    /**
     * 使用动态规划计算从左上角到(i,j)位置的最小路径和
     *
     * 算法原理：
     * 1. 创建dp数组存储从(0,0)到每个位置的最小路径和
     * 2. 初始化边界：第一行只能向右走，第一列只能向下走
     * 3. 状态转移：dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
     *
     * 时间复杂度：O(m*n)，需要遍历整个网格
     * 空间复杂度：O(m*n)，用于存储dp数组
     *
     * @param grid 二维网格数组
     * @param i 目标行索引
     * @param j 目标列索引
     * @return 从(0,0)到(i,j)的最小路径和
     */
    int minPathSumDp(int[][] grid, int i, int j) {
        // 若为左上角单元格，则终止搜索
        if (i == 0 && j == 0) {
            return grid[0][0];
        }
        // 若行列索引越界，则返回 +∞ 代价
        if (i < 0 || j < 0) {
            return Integer.MAX_VALUE;
        }
        int[][] dp = new int[i + 1][j + 1];
        // 初始化边界条件。

        // 易错的点：只能绕开 grid[0][0]，其他全不能绕开
        for (int a = 1; a <= j; a++) {
            dp[0][a] = dp[0][a - 1] + grid[0][a];
        }
        for (int b = 1; b <= i; b++) {
            dp[b][0] = dp[b - 1][0] + grid[b][0];
        }

        // 这里仍然是一个组合搜索问题：双层循环的点都从1开始，恰好能绕开00
        for (int m = 1; m <= i; m++) {
            for (int n = 1; n <= j; n++) {
                dp[m][n] = Math.min(dp[m - 1][n], dp[m][n - 1]) + grid[m][n];
            }
        }

        return dp[i][j];
    }

}
