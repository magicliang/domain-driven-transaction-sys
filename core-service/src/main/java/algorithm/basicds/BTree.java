package algorithm.basicds;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 基础二叉树
 *
 * @author magicliang
 *
 *         date: 2025-08-12 21:21
 */
public class BTree {

    public static class TreeNode {

        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }

        /**
         * 链式设置左子节点
         */
        public TreeNode left(int val) {
            this.left = new TreeNode(val);
            return this.left;
        }

        /**
         * 链式设置右子节点
         */
        public TreeNode right(int val) {
            this.right = new TreeNode(val);
            return this.right;
        }

        /**
         * 链式设置左子节点为已有节点
         */
        public TreeNode left(TreeNode node) {
            this.left = node;
            return this;
        }

        /**
         * 链式设置右子节点为已有节点
         */
        public TreeNode right(TreeNode node) {
            this.right = node;
            return this;
        }

        /**
         * 返回父节点，用于链式调用的回溯
         */
        public TreeNode up() {
            // 注意：这里需要外部维护父节点引用，简化实现中返回this
            return this;
        }
    }

    /**
     * 二叉树构建器 - 提供可读性强的构造方式
     */
    public static class TreeBuilder {

        private TreeNode root;

        public TreeBuilder(int rootVal) {
            this.root = new TreeNode(rootVal);
        }

        public static TreeBuilder create(int rootVal) {
            return new TreeBuilder(rootVal);
        }

        /**
         * 设置左子树
         * 示例：left(2).left(4).right(5)
         */
        public TreeBuilder left(int val) {
            root.left = new TreeNode(val);
            return this;
        }

        /**
         * 设置右子树
         * 示例：right(3).left(6).right(7)
         */
        public TreeBuilder right(int val) {
            root.right = new TreeNode(val);
            return this;
        }

        /**
         * 设置左子树为已有节点
         */
        public TreeBuilder left(TreeNode node) {
            root.left = node;
            return this;
        }

        /**
         * 设置右子树为已有节点
         */
        public TreeBuilder right(TreeNode node) {
            root.right = node;
            return this;
        }

        /**
         * 设置左子树为子构建器
         */
        public TreeBuilder left(TreeBuilder leftBuilder) {
            root.left = leftBuilder.build();
            return this;
        }

        /**
         * 设置右子树为子构建器
         */
        public TreeBuilder right(TreeBuilder rightBuilder) {
            root.right = rightBuilder.build();
            return this;
        }

        /**
         * 获取根节点
         */
        public TreeNode build() {
            return root;
        }

        /**
         * 链式设置左子节点并返回子构建器
         */
        public TreeBuilder leftChild(int val) {
            root.left = new TreeNode(val);
            return new TreeBuilder(val);
        }

        /**
         * 链式设置右子节点并返回子构建器
         */
        public TreeBuilder rightChild(int val) {
            root.right = new TreeNode(val);
            return new TreeBuilder(val);
        }
    }

    /**
     * 快速创建树的工具方法
     */
    public static TreeBuilder tree(int rootVal) {
        return TreeBuilder.create(rootVal);
    }

    public List<Integer> levelOrder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            final TreeNode current = queue.poll();
            result.add(current.val);
            if (current.left != null) {
                queue.offer(current.left);
            }

            if (current.right != null) {
                queue.offer(current.right);
            }
        }

        return result;
    }

    /**
     * 纯递归实现的层序遍历（不使用队列）
     * 思路：计算树高，然后按层递归访问
     */
    public List<Integer> levelOrderRecursive(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        int height = getHeight(root);

        // 这里还是少不了迭代，增加了复杂度，用迭代制造层数，然后让递归在展开过程里展开到特定的层次后才输出，如果不展开到特定层次，则不执行，这样性能不好
        for (int level = 1; level <= height; level++) {
            traverseLevel(root, level, result);
        }
        return result;
    }

    /**
     * 计算树的高度
     */
    private int getHeight(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // 消耗一个点，得到一个高度
        return 1 + Math.max(getHeight(root.left), getHeight(root.right));
    }

    /**
     * 通用迭代转递归模式：分层递归下探
     *
     * 模式核心思想：
     * 1. 分层：将问题分解为多个层次（如树的层数、数组的索引等）
     * 2. 递归下探：通过递归参数控制"下探"深度，每层递减参数
     * 3. 条件触发：在特定层级（如level=1）执行实际工作，否则继续下探
     *
     * 通用模板：
     * void recursiveLayerProcess(Node node, int layerParam, Result result) {
     * if (terminationCondition) return;
     *
     * if (layerParam == targetLayer) {
     * // 到达目标层，执行实际工作
     * doActualWork(node, result);
     * } else {
     * // 继续下探到下一层
     * recursiveLayerProcess(node.left, layerParam - 1, result);
     * recursiveLayerProcess(node.right, layerParam - 1, result);
     * }
     * }
     *
     * 这种模式适用于：
     * - 层序遍历（当前场景）
     * - 按层统计信息
     * - 分层处理数据结构
     * - 任何需要分层递归的场景
     */
    private void traverseLevel(TreeNode root, int level, List<Integer> result) {
        if (root == null) {
            return;
        }

        if (level == 1) {
            // 到达目标层，执行实际工作：收集节点值
            result.add(root.val);
        } else {
            // 继续下探到下一层，level参数递减控制下探深度
            traverseLevel(root.left, level - 1, result);
            traverseLevel(root.right, level - 1, result);
        }
    }
}
