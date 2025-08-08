package algorithm.beautiful.backtracing;


import java.util.ArrayList;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: n皇后问题
 * n * n 的棋盘有n平方个空间状态
 * 这似乎是一个排列/组合问题，每一行的行肯定是错开的，问题是列在怎样的排列里是错开的
 * 如何帮回溯剪枝？使用 used 列数组往下传
 * 相比起中国象棋问题，多了一个不能在同一个对角线的判断
 * 那么怎么计算对角线是否相撞呢？
 * 把对角线也转化成一个 used 数组，所以需要两个 used 数组，我们姑且叫做 diag1 或者 diag2
 * 那么 diag1 和 diag2的数组多长，应该怎么匹配呢？
 * 我们知道n*n的矩阵，如果从0开始算，最大的row/column是n-1。
 * 那么如何定义某一根对角线？
 * <p>
 * 我们把从左到右的对角线观察一下：
 * <p>
 * 1. 在同一根主对角线上，从左到右、从上到下移动，我们发现横纵坐标是同样等差增长的。这也就意味着，在同一根对角线上，row-col是相等的，但是因为row的起点不一样，所以没跟对角线的 row - col
 * 又是不一样的，所以我们可以用 row-col来定义一根对角线，2个点的 row - col 等于同一个值，他们在同一根主对角线上。
 * 2. 类似地，在同一根反对角线上，row增加，col就等比减少，所以对于同一根对角线上的点，row + col 始终等于一个恒等值。
 * 3. 所以对于一个数组而言 row + col/row - col 可以作为特定数组的 index。
 * 4. 对角线的最大值和最小值是由最大的row-col的数值范围和row+col的数值范围决定的。也就是说对主对角线而言，row-col的最大值是 n-1（如果最大值是n的话，最小值就是1而不是0），最小值是 1
 * -n，多个对角线点的值去重后得到[1-n,n-1]这个数学区间，这个数学区间的大小就是 n-1-(1-n) + 1 = 2n-1;。我们可以用穷举的方法证明，这个区间上的点每一个值都是可能存在对角线的-想象正方形的
 * 两条边上每个点都代表一个独一无二的对角线，那么两条边上一共有2n-1个独一无二的点，就有那么多对角线。
 * 5. 所以两个diag数组都是 2n-1 的长度。
 * 6. row-col 有一个特点是可能有负数，为了让负数能够使用对角线的索引，我们要把最小的 row - 最大的 col 转成0，也就是把1-n转成0，就要给row-col + n - 1：这是类似补码的设计方案。
 * <p>
 * 再加上 col 引用列索引作为另一个数组。我们得到了3个used数组，进行剪枝。
 * 接下来我们就可以在一个 0-based 的棋盘上进行搜索了-这里如果选1base，很多used数组不适合从0开始 index，太麻烦了
 * <p>
 * 1. 首先我们可以定义一个 n * n 的二维数组，而且把它标记为 * - 这样我们可以日后确定是不是要修改-撤销。
 * 2. 用row作为初始化的层数，然后在每一层里面再遍历 col 的值，避开所有的剪枝，然后把组合延长。
 * 4. 延长到 row 的底部，判断是否满足所有的剪枝-如果符合就输出结果，然后退出
 * @author magicliang
 *
 *         date: 2025-08-06 21:52
 */
public class NQueen {

    public static void main(String[] args) {
        // n 对 2 和 3 无解，4有2个解，5有10个解
        final List<List<List<String>>> result = nQueen(5);
        // 每一个 combination 是一个棋盘组合
        System.out.println("有效的解数量：" + result.size());
        for (List<List<String>> combination : result) {
            System.out.println("combination: ");
            for (List<String> row : combination) {
                for (String pos : row) {
                    System.out.print(pos);
                }
                System.out.println(" ");
            }
        }
    }

    public static List<List<List<String>>> nQueen(int n) {
        // 先准备一个二维数组的结果集作为返回值
        List<List<List<String>>> result = new ArrayList<>();
        // 定义每一层自己的 combinations
        List<List<String>> combinations = new ArrayList<>();
        // 为了在行里直接打印皇后的数量，我们还是用标记的方法吧
        for (int i = 0; i < n; i++) {
            List<String> combination = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                combination.add("*");
            }
            combinations.add(combination);
        }

        // 第一个used数组，n 列
        boolean[] cols = new boolean[n];
        final int diagLength = 2 * n - 1;
        boolean[] diags1 = new boolean[diagLength];
        boolean[] diags2 = new boolean[diagLength];

        backtrack(n, 0, cols, diags1, diags2, combinations, result);

        return result;
    }

    // 用递归和回溯的方式写入皇后的解
    private static void backtrack(int n, int row, boolean[] cols, boolean[] diags1, boolean[] diags2,
            List<List<String>> combinations,
            List<List<List<String>>> result) {
        // row 和 col 是从1-n增长的

        // 第i层越过了，可以收束输出结果了
        if (row >= n) {

            // 易错的点，回溯法要求深拷贝要求逐层深拷贝
            List<List<String>> newCombinations = new ArrayList<>(combinations.size());
            for (List<String> combination : combinations) {
                List<String> newCombination = new ArrayList<>(combination);
                newCombinations.add(newCombination);
            }

            // 易错的点，在回溯法里要尽量使用新数据结构防止修改错
            result.add(newCombinations);
            return;
        }

        // 列从0到n，在本层里面累加
        for (int col = 0; col < n; col++) {
            if (cols[col] || diags1[row - col + n - 1] || diags2[row + col]) {
                continue;
            }
            diags1[row - col + n - 1] = diags2[row + col] = cols[col] = true;

            combinations.get(row).set(col, "Q");

            backtrack(n, row + 1, cols, diags1, diags2, combinations, result);

            combinations.get(row).set(col, "*");
            diags1[row - col + n - 1] = diags2[row + col] = cols[col] = false;
        }
    }
}
