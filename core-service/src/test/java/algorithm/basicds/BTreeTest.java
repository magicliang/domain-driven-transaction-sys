package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: BTree的单元测试类
 *
 * @author magicliang
 *
 *         date: 2025-08-13 14:35
 */
class BTreeTest {

    @Test
    void testLevelOrderEmptyTree() {
        BTree tree = new BTree();
        List<Integer> result = tree.levelOrder(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testLevelOrderSingleNode() {
        BTree.TreeNode root = BTree.tree(1).build();
        BTree tree = new BTree();
        List<Integer> result = tree.levelOrder(root);
        assertEquals(Collections.singletonList(1), result);
    }

    @Test
    void testLevelOrderCompleteBinaryTree() {
        // 使用 BTree 的 TreeBuilder API 正确创建完全二叉树:
        //       1
        //     /   \
        //    2     3
        //   / \   / \
        //  4   5 6   7
        BTree.TreeNode root = BTree.tree(1)
                .left(BTree.tree(2)
                        .left(4)
                        .right(5))
                .right(BTree.tree(3)
                        .left(6)
                        .right(7))
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.levelOrder(root);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7), result);
    }

    @Test
    void testLevelOrderLeftSkewedTree() {
        // 使用链式调用创建左斜树:
        //   1
        //  /
        // 2
        // /
        //4
        BTree.TreeNode root = BTree.tree(1)
                .left(BTree.tree(2)
                        .left(4))
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.levelOrder(root);
        assertEquals(Arrays.asList(1, 2, 4), result);
    }

    @Test
    void testLevelOrderRightSkewedTree() {
        // 使用链式调用创建右斜树:
        // 1
        //  \
        //   2
        //    \
        //     3
        BTree.TreeNode root = BTree.tree(1)
                .right(BTree.tree(2)
                        .right(3)
                        .build())
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.levelOrder(root);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    void testLevelOrderUnbalancedTree() {
        // 使用链式调用创建不平衡树:
        //       1
        //     /   \
        //    2     3
        //   /       \
        //  4         5
        // /
        //6
        BTree.TreeNode root = BTree.tree(1)
                .left(2)
                .right(3)
                .build();

        root.left.left(4).left(6);
        root.right.right(5);

        BTree tree = new BTree();
        List<Integer> result = tree.levelOrder(root);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), result);
    }

    @Test
    void testLevelOrderOnlyLeftSubtree() {
        // 使用链式调用创建只有左子树的树:
        //   1
        //  /
        // 2
        //  \
        //   3
        BTree.TreeNode root = BTree.tree(1)
                .left(BTree.tree(2)
                        .right(3)
                        .build())
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.levelOrder(root);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    void testLevelOrderOnlyRightSubtree() {
        // 使用链式调用创建只有右子树的树:
        // 1
        //  \
        //   2
        //  /
        // 3
        BTree.TreeNode root = BTree.tree(1)
                .right(BTree.tree(2)
                        .left(3)
                        .build())
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.levelOrder(root);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    void testLevelOrderLargeTree() {
        // 使用链式调用创建较大的树
        // 正确的树结构：
        //         1
        //       /   \
        //      2     3
        //     / \   / \
        //    4   5 6   7
        //   / \   /
        //  8   9 10
        //       \
        //       11

        BTree.TreeNode root = BTree.tree(1).build();
        root.left = new BTree.TreeNode(2);
        root.right = new BTree.TreeNode(3);

        root.left.left = new BTree.TreeNode(4);
        root.left.right = new BTree.TreeNode(5);
        root.right.left = new BTree.TreeNode(6);
        root.right.right = new BTree.TreeNode(7);

        root.left.left.left = new BTree.TreeNode(8);
        root.left.left.right = new BTree.TreeNode(9);
        root.left.right.left = new BTree.TreeNode(10);
        root.left.right.left.right = new BTree.TreeNode(11);

        BTree tree = new BTree();
        List<Integer> result = tree.levelOrder(root);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), result);
    }

    @Test
    void testTreeBuilderUsage() {
        // 演示新的构建器用法
        BTree.TreeNode root = BTree.tree(10)
                .left(BTree.tree(5)
                        .left(3)
                        .right(7)
                        .build())
                .right(20)
                .build();

        // 验证构建的树结构
        assertEquals(10, root.val);
        assertEquals(5, root.left.val);
        assertEquals(20, root.right.val);
        assertEquals(3, root.left.left.val);
        assertEquals(7, root.left.right.val);
    }

    /**
     * 辅助方法：快速创建测试树（保留旧方法用于兼容性）
     */
    private BTree.TreeNode createTree(Integer... values) {
        if (values == null || values.length == 0) {
            return null;
        }

        BTree.TreeNode root = new BTree.TreeNode(values[0]);
        java.util.Queue<BTree.TreeNode> queue = new java.util.LinkedList<>();
        queue.offer(root);

        int i = 1;
        while (!queue.isEmpty() && i < values.length) {
            BTree.TreeNode current = queue.poll();

            if (i < values.length && values[i] != null) {
                current.left = new BTree.TreeNode(values[i]);
                queue.offer(current.left);
            }
            i++;

            if (i < values.length && values[i] != null) {
                current.right = new BTree.TreeNode(values[i]);
                queue.offer(current.right);
            }
            i++;
        }

        return root;
    }

    @Test
    void testLevelOrderWithNullValues() {
        BTree tree = new BTree();
        // 测试包含null值的树（null表示空节点）
        BTree.TreeNode root = createTree(1, 2, 3, null, 4, null, 5);
        List<Integer> result = tree.levelOrder(root);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }

    @Test
    void testLevelOrderRecursiveEmptyTree() {
        BTree tree = new BTree();
        List<Integer> result = tree.levelOrderRecursive(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testLevelOrderRecursiveSingleNode() {
        BTree.TreeNode root = BTree.tree(1).build();
        BTree tree = new BTree();
        List<Integer> result = tree.levelOrderRecursive(root);
        assertEquals(Collections.singletonList(1), result);
    }

    @Test
    void testLevelOrderRecursiveCompleteBinaryTree() {
        // 测试完全二叉树
        //       1
        //     /   \
        //    2     3
        //   / \   / \
        //  4   5 6   7
        BTree.TreeNode root = BTree.tree(1)
                .left(BTree.tree(2)
                        .left(4)
                        .right(5))
                .right(BTree.tree(3)
                        .left(6)
                        .right(7))
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.levelOrderRecursive(root);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7), result);
    }

    @Test
    void testLevelOrderRecursiveLeftSkewedTree() {
        // 测试左斜树
        //   1
        //  /
        // 2
        // /
        //3
        BTree.TreeNode root = BTree.tree(1)
                .left(BTree.tree(2)
                        .left(3))
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.levelOrderRecursive(root);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    void testLevelOrderRecursiveRightSkewedTree() {
        // 测试右斜树
        // 1
        //  \
        //   2
        //    \
        //     3
        BTree.TreeNode root = BTree.tree(1)
                .right(BTree.tree(2)
                        .right(3))
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.levelOrderRecursive(root);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    void testLevelOrderRecursiveUnbalancedTree() {
        // 测试不平衡树
        //       1
        //     /   \
        //    2     3
        //   /       \
        //  4         5
        // / \
        //6   7
        BTree.TreeNode root = BTree.tree(1)
                .left(BTree.tree(2)
                        .left(BTree.tree(4)
                                .left(6)
                                .right(7)))
                .right(BTree.tree(3)
                        .right(5))
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.levelOrderRecursive(root);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7), result);
    }

    @Test
    void testLevelOrderRecursiveCompareWithQueueMethod() {
        // 对比测试：确保递归方法和队列方法结果一致
        BTree.TreeNode root = BTree.tree(1)
                .left(BTree.tree(2)
                        .left(4)
                        .right(5))
                .right(BTree.tree(3)
                        .left(6)
                        .right(7))
                .build();

        BTree tree = new BTree();
        List<Integer> queueResult = tree.levelOrder(root);
        List<Integer> recursiveResult = tree.levelOrderRecursive(root);

        assertEquals(queueResult, recursiveResult);
    }

    @Test
    void testPreOrderEmptyTree() {
        BTree tree = new BTree();
        List<Integer> result = tree.preOrder(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testPreOrderSingleNode() {
        BTree.TreeNode root = BTree.tree(1).build();
        BTree tree = new BTree();
        List<Integer> result = tree.preOrder(root);
        assertEquals(Collections.singletonList(1), result);
    }

    @Test
    void testPreOrderCompleteBinaryTree() {
        // 测试完全二叉树
        //       1
        //     /   \
        //    2     3
        //   / \   / \
        //  4   5 6   7
        // 前序遍历结果：1,2,4,5,3,6,7
        BTree.TreeNode root = BTree.tree(1)
                .left(BTree.tree(2)
                        .left(4)
                        .right(5))
                .right(BTree.tree(3)
                        .left(6)
                        .right(7))
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.preOrder(root);
        assertEquals(Arrays.asList(1, 2, 4, 5, 3, 6, 7), result);
    }

    @Test
    void testPreOrderLeftSkewedTree() {
        // 测试左斜树
        //   1
        //  /
        // 2
        // /
        //3
        // 前序遍历结果：1,2,3
        BTree.TreeNode root = BTree.tree(1)
                .left(BTree.tree(2)
                        .left(3))
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.preOrder(root);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    void testPreOrderRightSkewedTree() {
        // 测试右斜树
        // 1
        //  \
        //   2
        //    \
        //     3
        // 前序遍历结果：1,2,3
        BTree.TreeNode root = BTree.tree(1)
                .right(BTree.tree(2)
                        .right(3))
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.preOrder(root);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    void testPreOrderUnbalancedTree() {
        // 测试不平衡树
        //       1
        //     /   \
        //    2     3
        //   /       \
        //  4         5
        // / \
        //6   7
        // 前序遍历结果：1,2,4,6,7,3,5
        BTree.TreeNode root = BTree.tree(1)
                .left(BTree.tree(2)
                        .left(BTree.tree(4)
                                .left(6)
                                .right(7)))
                .right(BTree.tree(3)
                        .right(5))
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.preOrder(root);
        assertEquals(Arrays.asList(1, 2, 4, 6, 7, 3, 5), result);
    }

    @Test
    void testPreOrderOnlyLeftSubtree() {
        // 测试只有左子树的树
        //   1
        //  /
        // 2
        //  \
        //   3
        // 前序遍历结果：1,2,3
        BTree.TreeNode root = BTree.tree(1)
                .left(BTree.tree(2)
                        .right(3))
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.preOrder(root);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    void testPreOrderOnlyRightSubtree() {
        // 测试只有右子树的树
        // 1
        //  \
        //   2
        //  /
        // 3
        // 前序遍历结果：1,2,3
        BTree.TreeNode root = BTree.tree(1)
                .right(BTree.tree(2)
                        .left(3))
                .build();

        BTree tree = new BTree();
        List<Integer> result = tree.preOrder(root);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    void testPreOrderLargeTree() {
        // 测试较大的树
        //         1
        //       /   \
        //      2     3
        //     / \   / \
        //    4   5 6   7
        //   / \   /
        //  8   9 10
        //       \
        //       11
        // 前序遍历结果：1,2,4,8,9,11,5,10,3,6,7
        BTree.TreeNode root = BTree.tree(1).build();
        root.left = new BTree.TreeNode(2);
        root.right = new BTree.TreeNode(3);

        root.left.left = new BTree.TreeNode(4);
        root.left.right = new BTree.TreeNode(5);
        root.right.left = new BTree.TreeNode(6);
        root.right.right = new BTree.TreeNode(7);

        root.left.left.left = new BTree.TreeNode(8);
        root.left.left.right = new BTree.TreeNode(9);
        root.left.right.left = new BTree.TreeNode(10);
        root.left.left.right.right = new BTree.TreeNode(11);

        BTree tree = new BTree();
        List<Integer> result = tree.preOrder(root);
        assertEquals(Arrays.asList(1, 2, 4, 8, 9, 11, 5, 10, 3, 6, 7), result);
    }
}