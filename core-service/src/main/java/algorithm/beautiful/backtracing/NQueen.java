package algorithm.beautiful.backtracing;

import java.util.ArrayList;
import java.util.List;

/**
 * N皇后问题求解器。
 *
 * 问题描述：
 * 在一个 N×N 的国际象棋棋盘上放置 N 个皇后，使得任意两个皇后都不能处于同一行、同一列或同一条对角线上。
 *
 * 解题思路：
 * 1.  约束分析：
 *     *   行：通过逐行放置皇后（第 row 行放置一个）来天然满足行不冲突的约束。
 *     *   列：使用一个布尔数组 cols[col] 来记录第 col 列是否已被占用。
 *     *   对角线：这是关键的约束。
 *         *   主对角线（\）：对于棋盘上的点 (row, col)，在同一条主对角线上的所有点，其 row - col 的值是恒定的。
 *             由于 row - col 的取值范围是 [-(n-1), n-1]，共 2n-1 个值。为了用作数组索引（非负），
 *             我们将索引映射为 row - col + (n - 1)，存储在 diags1 数组中。
 *         *   次对角线（/）：对于棋盘上的点 (row, col)，在同一条次对角线上的所有点，其 row + col 的值是恒定的。
 *             row + col 的取值范围是 [0, 2(n-1)]，共 2n-1 个值。可以直接用 row + col 作为索引，
 *             存储在 diags2 数组中。
 * 2.  算法选择：使用回溯法（Backtracking）进行深度优先搜索。
 * 3.  剪枝策略：在每一行尝试放置皇后时，检查列、主对角线、次对角线是否冲突。如果冲突，则跳过该列，实现剪枝。
 * 4.  结果表示：找到一个解时，将当前棋盘状态（combinations）进行深拷贝后存入结果集。
 *
 * @author magicliang
 * @date 2025-08-06 21:52
 */
public class NQueen {

    /**
     * 程序入口，测试 N 皇后问题求解。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // n 对 2 和 3 无解，4有2个解，5有10个解
        final int n = 5; // 测试用例，求解 5 皇后问题
        final List<List<List<String>>> result = nQueen(n);
        // 每一个 combination 是一个棋盘组合
        System.out.println("有效的解数量：" + result.size());
        for (List<List<String>> combination : result) {
            System.out.println("combination: ");
            // 遍历棋盘的每一行
            for (List<String> row : combination) {
                // 遍历行内的每个位置
                for (String pos : row) {
                    System.out.print(pos);
                }
                System.out.println(" ");
            }
            System.out.println("---"); // 分隔不同的解
        }
    }

    /**
     * 求解 N 皇后问题的主方法。
     * 步骤：
     * 1. 初始化结果集 result。
     * 2. 初始化棋盘状态 combinations（一个 N*N 的二维列表，初始用 "*" 填充）。
     * 3. 初始化剪枝用的布尔数组：cols（列），diags1（主对角线），diags2（次对角线）。
     * 4. 调用回溯方法 backtrack 开始搜索。
     * 5. 返回所有找到的解。
     *
     * @param n 皇后的数量，也是棋盘的大小 (N x N)
     * @return 所有有效的解，每个解是一个 N x N 的棋盘表示
     */
    public static List<List<List<String>>> nQueen(int n) {
        // 1. 准备存储所有解的结果集
        List<List<List<String>>> result = new ArrayList<>();

        // 2. 初始化当前棋盘状态，用 "*" 表示空位
        List<List<String>> combinations = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add("*");
            }
            combinations.add(row);
        }

        // 3. 初始化剪枝所需的布尔数组
        // cols[col] 表示第 col 列是否被占用
        boolean[] cols = new boolean[n];
        // 对角线数量为 2*n - 1
        final int diagLength = 2 * n - 1;
        // diags1[index] 表示主对角线 (row - col + n - 1 = index) 是否被占用
        boolean[] diags1 = new boolean[diagLength];
        // diags2[index] 表示次对角线 (row + col = index) 是否被占用
        boolean[] diags2 = new boolean[diagLength];

        // 4. 调用回溯函数开始搜索，从第 0 行开始
        backtrack(n, 0, cols, diags1, diags2, combinations, result);

        // 5. 返回所有找到的解
        return result;
    }

    /**
     * 使用回溯法递归地在棋盘上放置皇后。
     * 步骤：
     * 1. 递归基（Base Case）：如果当前行 row 等于 n，说明所有行都已成功放置皇后，找到一个解。
     * a. 对当前棋盘状态 combinations 进行深拷贝。
     * b. 将深拷贝的解添加到结果集 result 中。
     * c. 返回上一层调用。
     * 2. 递归步骤（Recursive Step）：在当前行 row 尝试放置皇后。
     * a. 遍历当前行的所有列 (col 从 0 到 n-1)。
     * b. 剪枝：检查当前位置 (row, col) 是否与已放置的皇后冲突（列冲突、主对角线冲突、次对角线冲突）。
     * *   如果 cols[col] 为 true，表示列冲突。
     * *   如果 diags1[row - col + n - 1] 为 true，表示主对角线冲突。
     * *   如果 diags2[row + col] 为 true，表示次对角线冲突。
     * *   如果任一冲突条件满足，则跳过当前列 (continue)。
     * c. 如果不冲突：
     * i.   更新状态：标记列 cols[col]、主对角线 diags1[row-col+n-1]、次对角线 diags2[row+col] 为已占用。
     * ii.  在棋盘 combinations 的 (row, col) 位置放置皇后 'Q'。
     * iii. 递归调用 backtrack，处理下一行 (row + 1)。
     * iv.  回溯（Backtrack）：
     * *   将棋盘 combinations 的 (row, col) 位置恢复为 '*'。
     * *   取消标记列 cols[col]、主对角线 diags1[row-col+n-1]、次对角线 diags2[row+col] 的占用状态。
     *
     * @param n 棋盘大小和皇后数量
     * @param row 当前正在尝试放置皇后的行号 (从 0 开始)
     * @param cols 记录各列是否被占用的布尔数组
     * @param diags1 记录各主对角线是否被占用的布尔数组 (索引: row - col + n - 1)
     * @param diags2 记录各次对角线是否被占用的布尔数组 (索引: row + col)
     * @param combinations 当前正在构建的棋盘状态
     * @param result 存储所有找到的有效解的列表
     */
    private static void backtrack(int n, int row, boolean[] cols, boolean[] diags1, boolean[] diags2,
            List<List<String>> combinations,
            List<List<List<String>>> result) {
        // 1. 递归基：所有行都已处理完毕，找到一个有效解
        if (row >= n) {
            // 易错的点，回溯法要求深拷贝要求逐层深拷贝
            // 创建当前棋盘状态的深拷贝，防止后续回溯修改影响已找到的解
            List<List<String>> newCombinations = new ArrayList<>(combinations.size());
            for (List<String> combination : combinations) {
                List<String> newCombination = new ArrayList<>(combination);
                newCombinations.add(newCombination);
            }
            // 易错的点，在回溯法里要尽量使用新数据结构防止修改错
            result.add(newCombinations);
            return; // 返回，继续探索其他可能性
        }

        // 2. 递归步骤：在当前行 row 尝试放置皇后
        // 遍历当前行的所有列
        for (int col = 0; col < n; col++) {
            // 2b. 剪枝：检查是否与已放置的皇后冲突
            if (cols[col] || diags1[row - col + n - 1] || diags2[row + col]) {
                continue; // 冲突，跳过此列，尝试下一列
            }

            // 2c. 如果不冲突，执行放置皇后操作
            // i. 更新占用标记
            diags1[row - col + n - 1] = diags2[row + col] = cols[col] = true;
            // ii. 在棋盘上放置皇后
            combinations.get(row).set(col, "Q");

            // iii. 递归处理下一行
            backtrack(n, row + 1, cols, diags1, diags2, combinations, result);

            // iv. 回溯：撤销当前行的操作，以便尝试其他列
            // 将棋盘恢复
            combinations.get(row).set(col, "*");
            // 取消占用标记
            diags1[row - col + n - 1] = diags2[row + col] = cols[col] = false;
        }
        // 当前行所有列都尝试完毕，返回上一层
    }
}