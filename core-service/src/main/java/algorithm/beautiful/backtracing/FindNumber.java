package algorithm.beautiful.backtracing;

import algorithm.basicds.BTree;
import algorithm.basicds.BTree.Node;
import java.util.ArrayList;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 使用回溯法找到特定节点的路径
 *
 * @author magicliang
 *
 *         date: 2025-08-25 16:50
 */
public class FindNumber {

    /**
     * 主入口方法：查找树中所有值为target的节点路径
     *
     * 算法思路：
     * 1. 使用深度优先搜索(DFS)遍历整棵树
     * 2. 维护一个路径列表记录从根到当前节点的路径
     * 3. 当遇到目标值时，复制当前路径到结果集
     * 4. 使用回溯机制确保路径状态正确
     *
     * @param root 二叉树的根节点
     * @param target 要查找的目标值
     * @return 包含所有匹配路径的列表，每个路径是从根到目标节点的节点值序列
     */
    public List<List<Integer>> findNumberPath(BTree.Node root, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        // 这里不需要像排列组合一样引入一个 used 数组，因为递归每个路径做出的选择都是由父-子的选择决定的，没有在数组里做选择的难度
        List<Integer> path = new ArrayList<>();
        findNumberPath(root, target, result, path);

        return result;
    }

    /**
     * 递归辅助方法：执行实际的回溯搜索
     *
     * 回溯过程说明：
     * 1. 选择：将当前节点值加入路径
     * 2. 约束：无（遍历所有节点）
     * 3. 目标：节点值等于target
     * 4. 撤销：移除路径末尾的节点值（回溯）
     *
     * 注意：此方法会找到树中所有值为target的节点，包括重复值
     *
     * @param root 当前子树的根节点
     * @param target 要查找的目标值
     * @param result 存储所有匹配路径的结果列表
     * @param path 当前从根节点到当前节点的路径
     */
    private void findNumberPath(Node root, int target, List<List<Integer>> result, List<Integer> path) {
        if (root == null) {
            // 直接返回，不增加也不撤销
            return;
        }

        // 记录当前节点
        path.add(root.val);
        if (root.val == target) {
            // 全量复制，因为path会被逐层撤销掉
            result.add(new ArrayList<>(path));
            // 这里也没有一个 found 的 flag。如果不停止搜索，这里不做撤回
//            path.remove(path.size() - 1);

//            return; // 如果找到一个解以后就停止搜索，此处返回，否则此处不返回，找到所有的7路径
        }

        // 当前节点已记录，调用
        findNumberPath(root.left, target, result, path);
        findNumberPath(root.right, target, result, path);

        // 不管是不是在本层的子路径里面找到 path，删除本层
        path.remove(path.size() - 1);
    }
}
