package algorithm.beautiful.backtracing;

import algorithm.basicds.BTree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用的回溯查找器 - 使用标准的回溯算法框架
 *
 * 特点：
 * 1. 分离的回溯方法（isSolution, recordSolution, isValid, makeChoice, undoChoice）
 * 2. 支持自定义约束条件
 * 3. 灵活的框架设计
 *
 * @author magicliang
 */
public class GenericBacktrackFinder {

    private int targetValue;
    private int forbiddenValue;

    /**
     * 构造函数，指定目标值和禁止值
     *
     * @param targetValue 要查找的目标值
     * @param forbiddenValue 禁止选择的值（约束条件）
     */
    public GenericBacktrackFinder(int targetValue, int forbiddenValue) {
        this.targetValue = targetValue;
        this.forbiddenValue = forbiddenValue;
    }

    /**
     * 默认构造函数：查找值为7的路径，禁止选择值为3的节点
     */
    public GenericBacktrackFinder() {
        this(7, 3); // 默认查找7，禁止选择3
    }

    /**
     * 公共接口：查找满足条件的路径
     */
    public List<List<BTree.Node>> findPaths(BTree.Node root) {
        List<List<BTree.Node>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        List<BTree.Node> state = new ArrayList<>();
        backtrack(state, Arrays.asList(root), result);
        return result;
    }

    /* 判断当前状态是否为解 */
    private boolean isSolution(List<BTree.Node> state) {
        return !state.isEmpty() && state.get(state.size() - 1).val == targetValue;
    }

    /* 记录解 */
    private void recordSolution(List<BTree.Node> state, List<List<BTree.Node>> res) {
        res.add(new ArrayList<>(state));
    }

    /* 判断在当前状态下，该选择是否合法 */
    private boolean isValid(List<BTree.Node> state, BTree.Node choice) {
        return choice != null && choice.val != forbiddenValue;
    }

    /* 更新状态 */
    private void makeChoice(List<BTree.Node> state, BTree.Node choice) {
        state.add(choice);
    }

    /* 恢复状态 */
    private void undoChoice(List<BTree.Node> state, BTree.Node choice) {
        state.remove(state.size() - 1);
    }

    /* 回溯算法核心 */
    private void backtrack(List<BTree.Node> state, List<BTree.Node> choices, List<List<BTree.Node>> res) {
        // 检查是否为解
        if (isSolution(state)) {
            recordSolution(state, res);
        }

        // 这个算法的优势是把左右子树都放进 choices 里，而且在下层通过遍历的方法来解 choices，这样上层代码不需要写太复杂
        // 遍历所有选择
        for (BTree.Node choice : choices) {
            // 剪枝：检查选择是否合法
            if (isValid(state, choice)) {
                // 尝试：做出选择，更新状态。对每一个子树都做加上本路径
                makeChoice(state, choice);
                // 进行下一轮选择（左右子节点）
                backtrack(state, Arrays.asList(choice.left, choice.right), res);
                // 回退：撤销选择，恢复到之前的状态
                undoChoice(state, choice); // 去除本路径
            }
        }
    }

    /**
     * 便捷方法：查找值为7的路径（禁止选择3）
     */
    public static List<List<BTree.Node>> findSevenPaths(BTree.Node root) {
        GenericBacktrackFinder finder = new GenericBacktrackFinder();
        return finder.findPaths(root);
    }

    /**
     * 便捷方法：自定义目标值和约束值
     */
    public static List<List<BTree.Node>> findPathsWithConstraints(BTree.Node root, int target, int forbidden) {
        GenericBacktrackFinder finder = new GenericBacktrackFinder(target, forbidden);
        return finder.findPaths(root);
    }
}