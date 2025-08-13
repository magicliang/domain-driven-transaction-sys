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

}
