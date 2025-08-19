package algorithm.beautiful.pancake;

import java.util.Arrays;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 翻烙饼
 * 煎饼排序 (Pancake Sorting) 问题求解器。
 *
 * 问题描述：
 * 有一摞大小各异的烙饼，用一个整数数组表示（数字代表大小）。
 * 你只能用一只手操作：每次可以拿起顶端的若干个饼，将它们整体翻转（颠倒顺序）。
 * 目标：通过一系列这样的翻转操作，将烙饼按大小排序。
 * 注意：原始问题描述是“由大到小翻转（最大在底）”，但代码实现和示例输入{4,2,3,1...}的目标是
 * 排成递增序列（最小在顶），这与标准 Pancake Sorting 问题（排成递增）一致。
 * 本实现以此为准，目标是将数组从上到下排成递增顺序。
 *
 * 算法思路：
 * 1.  使用带剪枝的回溯法（穷举搜索）来寻找最优解（最少翻转次数）。
 * 2.  初始上界：设为 2 * n (n为饼数)，这是一个已知的可行解上界（虽然可能不是最紧的）。
 * 3.  剪枝策略：
 * a. 如果当前已翻转次数 >= 当前记录的最优次数，则停止该分支搜索。
 * b. 启发式函数 lowerBound 估算从当前状态到目标状态的最少翻转次数。
 * 如果 (已翻转次数 + 估算剩余次数) >= 当前记录的最优次数，则停止该分支搜索。
 * 4.  回溯：在递归探索完一个分支后，撤销该分支的最后一次操作（通过再次翻转），恢复状态，
 * 以便探索其他分支。
 *
 * @author magicliang
 *
 *         date: 2025-08-06 18:38
 */
public class PancakeSorting {

    // 原始烙饼信息数组 (只读，初始化后不改变，代表问题输入)
    private int[] cakeArray;
    // 烙饼个数
    private int cakeCount;
    // 记录当前找到的最少翻转次数 (在搜索中也用作上界进行剪枝)
    // 初始值设为一个上界 (upperBound)，随着找到更优解而减小。
    private int maxSwap;
    // 存储最终找到的最优翻转序列
    // swapArray[i] 表示最优序列中的第 (i+1) 步操作：翻转从顶部数起的前 swapArray[i] 个烙饼。
    // 例如，swapArray[0] = 3，表示第一步是翻转最上面的 3 个烙饼。
    // 大小初始化为上界，确保能容纳任何可能的最优解。
    private int[] swapArray;

    // 当前搜索过程中，正在处理的烙饼排列状态 (工作区)
    // 这是算法执行的核心数据结构，所有翻转操作 (reverse) 都直接修改它。
    // 它的状态随着搜索的深入而变化，并在回溯时恢复。
    private int[] reverseCakeArray;

    // 当前搜索路径中，临时记录已执行的翻转操作位置 (临时日志)
    // reverseCakeArraySwap[step] 记录了在 search(step) 这一步执行的翻转位置。
    // 它与 reverseCakeArray 的状态紧密相关，共同描述了当前搜索路径的状态。
    // 大小初始化为上界，与 swapArray 对应。
    private int[] reverseCakeArraySwap;

    // 统计搜索过程调用次数 (性能分析)
    private int searchCount;

    /**
     * 构造函数
     */
    public PancakeSorting() {
        // 成员变量将在 init 方法中初始化
        cakeCount = 0;
        maxSwap = 0; // 将在 init 中被设置为上界
    }

    /**
     * 主函数，用于测试煎饼排序求解器
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 示例输入: 烙饼大小从上到下为 {4,2,3,1,5,6,7,9,8}
        // 目标是通过从顶部翻转，使其变为 {1,2,3,4,5,6,7,8,9} (顶端最小，底端最大)
        int[] cakeArrayInput = {3, 2, 1, 6, 5, 4, 9, 8, 7, 0};
        int size = cakeArrayInput.length;

        PancakeSorting sorter = new PancakeSorting();
        sorter.run(cakeArrayInput, size);
        sorter.output();
    }

    /**
     * 初始化求解器状态
     *
     * @param pCakeArray 初始烙饼大小数组 (从上到下)
     * @param cakeCount 烙饼数量
     */
    public void init(int[] pCakeArray, int cakeCount) {
        this.cakeCount = cakeCount;
        // 复制初始数组到只读的 cakeArray
        // 为什么拷贝？答：防御性编程，确保内部状态不被外部意外修改。
        // 虽然在此简单示例中直接赋值 this.cakeArray = pCakeArray 也能工作，
        // 但拷贝提供了更好的封装性和安全性。
        this.cakeArray = Arrays.copyOf(pCakeArray, cakeCount);

        // 设置初始上界 (启发式值，保证足够大以容纳最优解)
        // 原理：最坏情况下，每个元素最多需要两次翻转归位 (2*(n-1))，这里用 2*n 作为宽松上界
        this.maxSwap = upperBound(this.cakeCount);

        // 初始化存储最优解的数组
        // 大小设为初始上界，确保空间足够。
        // 为什么是 maxSwap 而不是 maxSwap + 1？
        // 答：maxSwap 本身就是上界，代表最大可能的翻转次数。数组大小等于上界即可保证安全。
        this.swapArray = new int[this.maxSwap];

        // 初始化当前搜索状态数组 (工作区)
        // 初始状态与输入数组相同
        this.reverseCakeArray = Arrays.copyOf(this.cakeArray, this.cakeCount);

        // 初始化临时记录翻转序列的数组 (临时日志)
        this.reverseCakeArraySwap = new int[this.maxSwap];
    }

    /**
     * 启动排序求解过程
     *
     * @param pCakeArray 初始烙饼大小数组 (从上到下)
     * @param cakeCount 烙饼数量
     */
    public void run(int[] pCakeArray, int cakeCount) {
        init(pCakeArray, cakeCount);
        searchCount = 0;
        // 从第 0 步开始搜索
        search(0);
    }

    /**
     * 输出最终找到的最优翻转序列及相关信息
     */
    public void output() {
        System.out.print("Flip sequence (positions): ");
        // 输出最优翻转序列
        // 序列中的每个数字 k 表示一次翻转操作：翻转从顶部（数组索引 0 的位置）开始的 k 个烙饼。
        for (int i = 0; i < maxSwap; ++i) {
            System.out.print(swapArray[i] + " ");
        }
        System.out.println();

        System.out.println("|Search Times| : " + searchCount);
        // maxSwap 记录的是找到的最优翻转序列的步数（即最少翻转次数）
        System.out.println("Total Swap times (minimum flips) = " + maxSwap);
    }

    /**
     * 计算搜索过程中的上界（最大允许翻转次数）
     *
     * @param cakeCount 烙饼数量
     * @return 上界值
     */
    public int upperBound(int cakeCount) {
        // 使用 2 * n 作为初始上界 (比最坏情况 2*(n-1) 稍宽松)
        return (cakeCount - 1) * 2;
    }

    /**
     * 启发式函数：估算从当前烙饼状态到目标状态（升序）所需的最少翻转次数下界
     * 用于剪枝，减少不必要的搜索。
     * 思路：检查相邻烙饼的顺序关系。
     * 问题讨论：为什么检查 difference != 1 而不是 p[i-1] < p[i]？
     * 答：原始 C++ 代码逻辑是检查相邻元素是否在“排序上相邻”（差值为1或-1），
     * 可能是为配合其（略有不一致的）递减目标。本 Java 实现明确了目标是递增排序，
     * 因此一个更通用的判断是检查 p[i-1] < p[i]。当前实现保留了原逻辑（检查 == 1），
     * 但这隐含了输入数据是（或趋向于）连续整数的假设。一个更普适的 lowerBound
     * 可能是：if (pCakeArray[i - 1] >= pCakeArray[i]) ret++;
     *
     * @param pCakeArray 当前烙饼状态数组
     * @param cakeCount 烙饼数量
     * @return 估算的最少翻转次数下界
     */
    public int lowerBound(int[] pCakeArray, int cakeCount) {
        int ret = 0;
        // 遍历所有相邻的元素对。
        // 为什么循环是 for (int i = 1; i < cakeCount; ++i) ?
        // 答：i 代表每对元素中的第二个元素索引。
        //     从 i=1 开始是为了安全访问 p[i-1] (即索引 0)。
        //     到 i < cakeCount 结束是为了避免访问 p[cakeCount] (越界)。
        //     这样正好遍历了 (p[0],p[1]), (p[1],p[2]), ..., (p[n-2],p[n-1]) 共 n-1 对。
        for (int i = 1; i < cakeCount; ++i) {
            int difference = pCakeArray[i] - pCakeArray[i - 1];
            // 检查相邻元素是否是连续递增的 (例如 3, 4)
            if (difference != 1) {
                // 如果不是连续递增，则认为状态不佳，估算需要更多翻转
                ret++;
            }
            // 如果 difference == 1，则状态较好，不增加估算值
        }
        return ret;
    }

    /**
     * 核心搜索函数 (使用回溯法)
     *
     * 算法流程与回溯机制：
     * 1.  估算与剪枝：计算当前状态的下界，如果 (step + 下界) >= maxSwap，则剪枝返回。
     * 2.  检查解：如果当前 reverseCakeArray 已排序，则找到一个解。如果步数更少，则更新最优解。
     * 3.  递归探索：
     * a. 遍历所有可能的翻转位置 i (从1到cakeCount-1)。
     * b. 执行翻转：修改工作区 reverseCakeArray。
     * c. 记录路径：在 reverseCakeArraySwap[step] 记录翻转位置 i。
     * d. 递归调用：调用 search(step + 1)，在修改后的状态基础上继续搜索。
     * e. 回溯恢复：
     * 当 search(step + 1) 返回后（无论是找到解还是剪枝/无解返回），
     * 当前状态是执行了翻转 i 后的状态。
     * 为了探索下一个分支 (下一个 i')，必须撤销本次翻转 i 的影响。
     * 方法是对同一段 (0, i) 再次执行一次翻转操作，利用翻转的可逆性将
     * reverseCakeArray 恢复到进入本次循环 (执行翻转 i 之前) 的状态。
     * reverseCakeArraySwap 中对应 step 的记录会被下一次循环覆盖，无需手动清除。
     *
     * 关于返回点和状态管理的讨论：
     * 问题：search 函数有两个返回点（剪枝返回和找到解返回），为什么不清理 reverseCakeArraySwap？
     * 答：因为不需要。
     * 1. reverseCakeArraySwap[step] 的值由 search(step) 自身在其 for 循环中写入。
     * 2. 当 search(step) 从递归调用返回后，无论该调用是因剪枝还是找到解而返回，
     * search(step) 都会立即执行回溯操作 reverse(0, i) 恢复工作区状态。
     * 3. 然后，search(step) 的 for 循环会进入下一次迭代，尝试下一个翻转位置 i'。
     * 在这次迭代开始时，search(step) 会立刻执行 reverseCakeArraySwap[step] = i'。
     * 这会覆盖掉上一次迭代（尝试 i）时留在该位置的任何“脏”数据。
     * 4. 因此，清理旧数据的责任在于下一次写入操作（覆盖），而不是显式的清理。
     * 这是回溯法中高效的状态管理方式：每个层级只关心自己的槽位，旧值会被新值自然覆盖。
     *
     * @param step 当前已经执行的翻转步数
     */
    public void search(int step) {
        int estimate;
        searchCount++; // 统计搜索次数

        // 估算从当前状态到目标状态所需的最少翻转次数
        estimate = lowerBound(reverseCakeArray, cakeCount);
        // 剪枝：如果 (已执行步数 + 估算剩余步数) >= 当前已知最优解步数，则此分支不可能更优，停止搜索
        if (step + estimate >= maxSwap) { // 如果用 step >= maxSwap 来代替这个条件，也可能得到一个更小的 minimum flips，但是这个问题已经被证明是一个 np
            // hard 问题 煎饼排序的相关背景内容可以参考百度百科「煎饼排序」。2011年，劳伦特·比尔托（Laurent Bulteau）、纪尧姆·佛丁（Guillaume Fertin）和伊雷娜·鲁苏（Irena
            // Rusu）证明了给定一叠煎饼的长度分布，找到最短解法是 NP 困难的，参考论文「Bulteau, Laurent; Fertin, Guillaume; Rusu, Irena. Pancake
            // Flipping Is Hard. Journal of Computer and System Sciences: 1556–1574.」。
            return; // 返回点 1: 剪枝返回
        }

        // 检查当前状态是否已经排序完成 (目标：从上到下递增)
        if (isSorted(reverseCakeArray, cakeCount)) {
            // 如果找到一个更短的翻转序列，则更新最优解
            if (step < maxSwap) {
                maxSwap = step; // 更新最少翻转次数
                // 记录当前找到的最优翻转序列
                // 将当前路径临时记录的翻转序列 (reverseCakeArraySwap) 复制到最终结果 (swapArray)
                // 存储翻转的位置索引 (即翻转顶部 k 个饼的 k 值)
                if (maxSwap >= 0) {
                    System.arraycopy(reverseCakeArraySwap, 0, swapArray, 0, maxSwap);
                }
            }
            return; // 返回点 2: 找到解并返回
        }

        // 递归尝试所有可能的翻转操作
        // i 表示翻转范围的结束索引 (翻转 [0, i] 范围内的元素，共 i+1 个)
        // i 从 1 到 cakeCount-1 (翻转1个饼无意义，翻转全部饼也无直接意义，但逻辑上允许)
        for (int i = 1; i < cakeCount; ++i) {
            // --- 关键步骤 1: 执行操作 ---
            // 执行翻转：翻转从顶部 (索引 0) 到第 i 个位置 (索引 i) 的烙饼
            // 这会修改工作区 reverseCakeArray 的状态，代表进入了新的搜索状态
            reverse(0, i);

            // --- 关键步骤 2: 记录路径 ---
            // 记录本次翻转操作的位置 (即翻转了顶部 i+1 个饼，记录位置 i)
            // 这记录了当前搜索路径上的一个决策
            // 为什么 reverseCakeArraySwap 的状态不会错乱？
            // 答：因为 step 参数精确指定了每个递归层级操作的数组索引。
            //     search(step) 只写 reverseCakeArraySwap[step]。
            //     不同层级操作不同索引，同一层级在循环中更新同一索引（这是功能性的覆盖）。
            //     回溯保证了工作区状态的恢复，使得不同分支的探索互不干扰。
            reverseCakeArraySwap[step] = i;

            // --- 关键步骤 3: 递归探索 ---
            // 递归进入下一步搜索，在执行了本次翻转后的新状态下继续探索
            search(step + 1);

            // --- 关键步骤 4: 回溯恢复 ---
            // 当 search(step + 1) 返回后，工作区 reverseCakeArray 的状态是执行翻转 i 后的状态。
            // 为了在当前循环中尝试下一个不同的翻转操作 (例如 i')，
            // 必须撤销本次翻转 i 对工作区的影响，将状态恢复到进入循环体前。
            // 方法是对同一段 (0, i) 再次执行一次翻转，利用其可逆性进行恢复。
            reverse(0, i);
            // 现在 reverseCakeArray 的状态已恢复，可以安全地进入下一次循环 (i = i' ...)。
            // reverseCakeArraySwap[step] 的值将在下一次循环中被新的 i' 覆盖。
        }
    }

    /**
     * 检查给定的烙饼数组是否已经按目标顺序（从上到下递增）排好
     *
     * @param pCakeArray 待检查的烙饼数组
     * @param cakeCount 烙饼数量
     * @return 如果已排序则返回 true，否则返回 false
     */
    public boolean isSorted(int[] pCakeArray, int cakeCount) {
        for (int i = 1; i < cakeCount; ++i) {
            // 检查是否严格递增
            if (pCakeArray[i - 1] > pCakeArray[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 执行一次翻转操作：将数组中从索引 begin 到 end 的元素顺序颠倒
     * 翻转的可逆性：对同一段连续元素连续执行两次相同的翻转操作，数组会回到原始状态。
     *
     * @param begin 翻转范围的起始索引 (包含)
     * @param end 翻转范围的结束索引 (包含)
     */
    public void reverse(int begin, int end) {
        // 使用双指针法进行原地翻转
        while (begin < end) {
            int temp = reverseCakeArray[begin];
            reverseCakeArray[begin] = reverseCakeArray[end];
            reverseCakeArray[end] = temp;
            begin++;
            end--;
        }
    }
}