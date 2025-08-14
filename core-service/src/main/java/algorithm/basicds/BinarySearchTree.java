package algorithm.basicds;

import algorithm.basicds.BTree.Node;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 二叉搜索树实现
 * 支持插入、搜索、删除等基本操作
 * 复用BTree.Node作为节点结构
 *
 * @author magicliang
 *
 *         date: 2025-08-14 11:24
 */
public class BinarySearchTree {

    /**
     * 复用BTree的Node结构
     */
    private BTree.Node root;

    /**
     * 插入新值到BST中
     * 如果值已存在，则不进行任何操作
     * 普通二叉树没有办法通过插入实现自动建树的功能，但是搜索二叉树可以
     *
     * @param val 要插入的值
     */
    public void insert(int val) {
        root = insertRecursive(root, val);
    }

    /**
     * 非递归方式插入新值到BST中
     * 如果值已存在，则不进行任何操作
     * 使用迭代方式替代递归，避免栈溢出风险
     *
     * @param val 要插入的值
     */
    public void insertNoneRecursive(int val) {
        root = insertNoneRecursive(root, val);
    }

    /**
     * 递归插入的辅助方法
     *
     * @param node 当前子树的根节点
     * @param val 要插入的值
     * @return 插入后的子树根节点
     */
    private BTree.Node insertRecursive(BTree.Node node, int val) {
        if (node == null) {
            return new Node(val);
        }

        if (val < node.val) {
            node.left = insertRecursive(node.left, val);
        } else if (val > node.val) {
            node.right = insertRecursive(node.right, val);
        }

        // 如果val == node.val，不插入重复值
        return node;
    }

    /**
     * 非递归插入的辅助方法
     * 使用迭代方式遍历树结构，找到合适的插入位置
     * 时间复杂度：O(h)，其中h是树的高度
     * 空间复杂度：O(1)，只使用了常数个额外变量
     *
     * @param node 当前子树的根节点
     * @param val 要插入的值
     * @return 插入后的子树根节点
     */
    private BTree.Node insertNoneRecursive(BTree.Node node, int val) {
        if (node == null) {
            return new Node(val);
        }

        BTree.Node current = node;
        BTree.Node prev = null;

        // 遍历树结构，找到合适的插入位置
        while (current != null) {
            prev = current;
            if (val < current.val) {
                current = current.left;
            } else if (val > current.val) {
                current = current.right;
            } else {
                // 如果相等不用插入了直接返回
                return node;
            }
        }

        // 当current为null时，prev是其父节点
        BTree.Node newNode = new Node(val);
        if (val < prev.val) {
            prev.left = newNode;
        } else {
            // 这里不可能再有相等的节点了，因为等于的情况在上面已经得到返回值了
            prev.right = newNode;
        }
        return node;
    }

    /**
     * 搜索指定值是否存在
     *
     * @param val 要搜索的值
     * @return 如果存在返回true，否则返回false
     */
    public boolean search(int val) {
        return searchRecursive(root, val);
    }

    public boolean searchNoneRecursive(int val) {
        return searchNoneRecursive(root, val);
    }

    /**
     * 递归搜索的辅助方法
     *
     * 这里返回是否存在而不是返回具体的节点，是因为像 contains 之类的操作就是这样实现的
     *
     * @param node 当前子树的根节点
     * @param val 要搜索的值
     * @return 如果找到返回true，否则返回false
     */
    private boolean searchRecursive(BTree.Node node, int val) {
        if (node == null) {
            return false;
        }
        if (node.val == val) {
            return true;
        } else if (val < node.val) {
            return searchRecursive(node.left, val);
        } else {
            return searchRecursive(node.right, val);
        }
    }

    /**
     * 非递归搜索的辅助方法
     * 使用迭代方式遍历树结构，避免递归栈溢出风险
     * 时间复杂度：O(h)，其中h是树的高度
     * 空间复杂度：O(1)，只使用了常数个额外变量
     *
     * @param node 当前子树的根节点
     * @param val 要搜索的值
     * @return 如果找到返回true，否则返回false
     */
    private boolean searchNoneRecursive(BTree.Node node, int val) {
        BTree.Node current = node;
        while (current != null) {
            if (current.val == val) {
                return true;
            } else if (val < current.val) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    /**
     * 删除指定值
     * 如果值不存在，则不进行任何操作
     *
     * @param val 要删除的值
     */
    public void delete(int val) {
        root = deleteRecursive(root, val);
    }

    /**
     * 递归删除的辅助方法
     * 处理三种情况：
     * 1. 叶子节点：直接删除
     * 2. 只有一个子节点：用子节点替换
     * 3. 有两个子节点：用右子树的最小值替换
     *
     * @param node 当前子树的根节点
     * @param val 要删除的值
     * @return 删除后的子树根节点
     */
    private BTree.Node deleteRecursive(BTree.Node node, int val) {
        if (node == null) {
            return null;
        }

        if (val < node.val) {
            node.left = deleteRecursive(node.left, val);
        } else if (val > node.val) {
            node.right = deleteRecursive(node.right, val);
        } else {
            // 找到要删除的节点
            // 情况1：叶子节点或只有一个子节点
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            // 情况2：有两个子节点
            // 找到右子树的最小值
            int minVal = findMin(node.right);
            node.val = minVal;
            // 删除右子树中的最小值
            node.right = deleteRecursive(node.right, minVal);
        }

        return node;
    }

    /**
     * 查找最小值
     *
     * @return 最小值
     * @throws IllegalStateException 如果树为空
     */
    public int findMin() {
        if (root == null) {
            throw new IllegalStateException("BST is empty");
        }
        return findMin(root);
    }

    /**
     * 递归查找最小值的辅助方法
     *
     * @param node 当前子树的根节点
     * @return 最小值
     */
    private int findMin(BTree.Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.val;
    }

    /**
     * 查找最大值
     *
     * @return 最大值
     * @throws IllegalStateException 如果树为空
     */
    public int findMax() {
        if (root == null) {
            throw new IllegalStateException("BST is empty");
        }
        return findMax(root);
    }

    /**
     * 递归查找最大值的辅助方法
     *
     * @param node 当前子树的根节点
     * @return 最大值
     */
    private int findMax(BTree.Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node.val;
    }

    /**
     * 获取中序遍历结果（升序排列）
     * 复用BTree的中序遍历方法
     *
     * @return 升序排列的值列表
     */
    public List<Integer> inOrder() {
        BTree bTree = new BTree();
        return bTree.midOrder(root);
    }

    /**
     * 获取前序遍历结果
     * 复用BTree的前序遍历方法
     *
     * @return 前序遍历结果列表
     */
    public List<Integer> preOrder() {
        BTree bTree = new BTree();
        return bTree.preOrder(root);
    }

    /**
     * 获取后序遍历结果
     * 复用BTree的后序遍历方法
     *
     * @return 后序遍历结果列表
     */
    public List<Integer> postOrder() {
        BTree bTree = new BTree();
        return bTree.postOrder(root);
    }

    /**
     * 获取层序遍历结果
     * 复用BTree的层序遍历方法
     *
     * @return 层序遍历结果列表
     */
    public List<Integer> levelOrder() {
        BTree bTree = new BTree();
        return bTree.levelOrder(root);
    }

    /**
     * 检查树是否为空
     *
     * @return 如果为空返回true，否则返回false
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * 获取根节点（用于测试）
     *
     * @return 根节点
     */
    public BTree.Node getRoot() {
        return root;
    }

    /**
     * 清空整棵树
     */
    public void clear() {
        root = null;
    }

    /**
     * 获取树的高度
     *
     * @return 树的高度（节点数）
     */
    public int height() {
        return height(root);
    }

    /**
     * 递归计算树高的辅助方法
     *
     * @param node 当前子树的根节点
     * @return 子树的高度
     */
    private int height(BTree.Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * 获取树的节点总数
     *
     * @return 节点总数
     */
    public int size() {
        return size(root);
    }

    /**
     * 递归计算节点数的辅助方法
     *
     * @param node 当前子树的根节点
     * @return 子树的节点数
     */
    private int size(BTree.Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }
}