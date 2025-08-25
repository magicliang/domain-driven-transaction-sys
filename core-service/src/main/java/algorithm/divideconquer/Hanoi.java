package algorithm.divideconquer;

import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 汉诺塔 - 经典递归分治算法实现
 *
 * 问题修复说明：
 * 原实现中出现的StackOverflowError主要原因：
 * 1. 当输入参数异常时（如负数或极大值）可能导致无限递归
 * 2. JVM栈深度限制（通常约1000-10000层，取决于JVM设置）
 *
 * 解决方案：
 * 1. 添加边界条件检查：i <= 0时直接返回
 * 2. 对于极大数量的盘子（如>1000），建议使用迭代实现或尾递归优化
 * 3. 在实际应用中，可以限制最大盘子数量
 *
 * @author magicliang
 *
 *         date: 2025-08-25 15:53
 */
public class Hanoi {

    /**
     * 将源柱子顶部的盘子移动到目标柱子
     *
     * @param src 源柱子，用List表示，顶部元素在列表末尾
     * @param dst 目标柱子，用List表示，顶部元素在列表末尾
     */
    void move(List<Integer> src, List<Integer> dst) {
        // 移出原塔的盘子
        Integer pan = src.remove(src.size() - 1);
        // 移动到当前的塔上
        dst.add(pan);
    }

    /**
     * 递归解决汉诺塔问题
     *
     * @param i 要移动的盘子数量
     * @param src 源柱子
     * @param buf 缓冲柱子
     * @param dst 目标柱子
     *
     *         时间复杂度：O(2^n)，移动2^n - 1次
     *         空间复杂度：O(n)，递归栈深度
     */
    void hanoi(int i, List<Integer> src, List<Integer> buf, List<Integer> dst) {
        // 关键修复：添加边界条件检查，防止无限递归
        if (i <= 0) {
            return;
        }

        // i 带有层次技术的作用，对于带层下降是有用的
        if (i == 1) {
            // 如果只移动一个盘子，直接移动
            move(src, dst);
            return;
        }

        // 如果超过1个盘子，先把顶部的盘子移到 buf 上

        // 把大部分子问题先解决，先移到 buf 上
        hanoi(i - 1, src, dst, buf);

        // 移动顶部的盘子
        move(src, dst);
        // 把子问题再移到 dst 上
        hanoi(i - 1, buf, src, dst); // 这个地方妙就妙在，src也可以当作 buf

        // 汉诺塔的启示是，附带一个随着递归使用的数，类似烧饼排序，来帮助递归下降，而且可以把子问题的解 buffer 出去，再转移回来
    }

    /**
     * 求解汉诺塔问题
     *
     * @param A 源柱子，初始包含所有盘子，盘子按从小到大顺序排列
     * @param B 缓冲柱子，初始为空
     * @param C 目标柱子，初始为空
     *
     *         使用示例：
     *         List<Integer> A = Arrays.asList(1,2,3,4,5); // 1是最小盘子在最上面
     *         List<Integer> B = new ArrayList<>();
     *         List<Integer> C = new ArrayList<>();
     *         hanoi.solveHanoi(A, B, C);
     *         // 结果：C = [1,2,3,4,5]
     */
    public void solveHanoi(List<Integer> A, List<Integer> B, List<Integer> C) {
        int n = A.size();
        if (n > 0) {
            hanoi(n, A, B, C);
        }
    }
}
