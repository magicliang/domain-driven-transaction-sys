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
            // 这里也没有一个 found 的 flag
            path.remove(path.size() - 1);
//            return; // 如果找到一个解以后就停止搜索，此处返回，否则此处不返回，找到所有的7路径
        }

        // 当前节点已记录，调用
        findNumberPath(root.left, target, result, path);
        findNumberPath(root.right, target, result, path);

        // 不管是不是在本层的子路径里面找到 path，删除本层
        path.remove(path.size() - 1);
    }
}
