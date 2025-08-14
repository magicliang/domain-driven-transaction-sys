package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: BinarySearchTree的单元测试类
 *
 * @author magicliang
 *
 *         date: 2025-08-14 11:24
 */
class BinarySearchTreeTest {

    private BinarySearchTree bst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree();
    }

    @Test
    void testInsertAndSearchEmptyTree() {
        assertFalse(bst.search(5));
        assertTrue(bst.isEmpty());
    }

    @Test
    void testInsertSingleNode() {
        bst.insert(5);
        assertTrue(bst.search(5));
        assertFalse(bst.isEmpty());
        assertEquals(1, bst.size());
    }

    @Test
    void testInsertMultipleNodes() {
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.insert(2);
        bst.insert(4);
        bst.insert(6);
        bst.insert(8);

        assertTrue(bst.search(5));
        assertTrue(bst.search(3));
        assertTrue(bst.search(7));
        assertTrue(bst.search(2));
        assertTrue(bst.search(4));
        assertTrue(bst.search(6));
        assertTrue(bst.search(8));
        assertFalse(bst.search(1));
        assertFalse(bst.search(9));
    }

    @Test
    void testInsertDuplicateValues() {
        bst.insert(5);
        bst.insert(3);
        bst.insert(5); // 重复值
        bst.insert(3); // 重复值

        assertEquals(2, bst.size()); // 应该只有2个唯一值
        assertTrue(bst.search(5));
        assertTrue(bst.search(3));
    }

    @Test
    void testInOrderTraversal() {
        // 构建BST: 5,3,7,2,4,6,8
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.insert(2);
        bst.insert(4);
        bst.insert(6);
        bst.insert(8);

        List<Integer> result = bst.inOrder();
        assertEquals(Arrays.asList(2, 3, 4, 5, 6, 7, 8), result);
    }

    @Test
    void testPreOrderTraversal() {
        // 构建BST: 5,3,7,2,4,6,8
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.insert(2);
        bst.insert(4);
        bst.insert(6);
        bst.insert(8);

        List<Integer> result = bst.preOrder();
        assertEquals(Arrays.asList(5, 3, 2, 4, 7, 6, 8), result);
    }

    @Test
    void testPostOrderTraversal() {
        // 构建BST: 5,3,7,2,4,6,8
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.insert(2);
        bst.insert(4);
        bst.insert(6);
        bst.insert(8);

        List<Integer> result = bst.postOrder();
        assertEquals(Arrays.asList(2, 4, 3, 6, 8, 7, 5), result);
    }

    @Test
    void testLevelOrderTraversal() {
        // 构建BST: 5,3,7,2,4,6,8
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.insert(2);
        bst.insert(4);
        bst.insert(6);
        bst.insert(8);

        List<Integer> result = bst.levelOrder();
        assertEquals(Arrays.asList(5, 3, 7, 2, 4, 6, 8), result);
    }

    @Test
    void testFindMin() {
        assertThrows(IllegalStateException.class, () -> bst.findMin());

        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.insert(2);
        bst.insert(4);

        assertEquals(2, bst.findMin());
    }

    @Test
    void testFindMax() {
        assertThrows(IllegalStateException.class, () -> bst.findMax());

        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.insert(2);
        bst.insert(4);

        assertEquals(7, bst.findMax());
    }

    @Test
    void testDeleteLeafNode() {
        // 删除叶子节点
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);

        bst.delete(7);
        assertFalse(bst.search(7));
        assertEquals(2, bst.size());
        assertEquals(Arrays.asList(3, 5), bst.inOrder());
    }

    @Test
    void testDeleteNodeWithOneChild() {
        // 删除只有一个子节点的节点
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.insert(6);

        bst.delete(7);
        assertFalse(bst.search(7));
        assertTrue(bst.search(6));
        assertEquals(Arrays.asList(3, 5, 6), bst.inOrder());
    }

    @Test
    void testDeleteNodeWithTwoChildren() {
        // 删除有两个子节点的节点
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.insert(2);
        bst.insert(4);
        bst.insert(6);
        bst.insert(8);

        bst.delete(5); // 删除根节点
        assertFalse(bst.search(5));
        assertEquals(6, bst.size());
        assertEquals(Arrays.asList(2, 3, 4, 6, 7, 8), bst.inOrder());
    }

    @Test
    void testDeleteNonExistentValue() {
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);

        bst.delete(10); // 不存在的值
        assertEquals(3, bst.size());
        assertEquals(Arrays.asList(3, 5, 7), bst.inOrder());
    }

    @Test
    void testDeleteRootOnly() {
        bst.insert(5);
        bst.delete(5);
        assertTrue(bst.isEmpty());
        assertFalse(bst.search(5));
    }

    @Test
    void testHeight() {
        assertEquals(0, bst.height());

        bst.insert(5);
        assertEquals(1, bst.height());

        bst.insert(3);
        bst.insert(7);
        assertEquals(2, bst.height());

        bst.insert(2);
        bst.insert(4);
        bst.insert(6);
        bst.insert(8);
        assertEquals(3, bst.height());
    }

    @Test
    void testSize() {
        assertEquals(0, bst.size());

        bst.insert(5);
        assertEquals(1, bst.size());

        bst.insert(3);
        bst.insert(7);
        assertEquals(3, bst.size());

        bst.insert(3); // 重复值
        assertEquals(3, bst.size());

        bst.delete(7);
        assertEquals(2, bst.size());
    }

    @Test
    void testClear() {
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);

        bst.clear();
        assertTrue(bst.isEmpty());
        assertEquals(0, bst.size());
        assertFalse(bst.search(5));
    }

    @Test
    void testComplexOperations() {
        // 复杂操作序列测试
        int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};

        for (int val : values) {
            bst.insert(val);
        }

        assertEquals(11, bst.size());
        assertEquals(Arrays.asList(10, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80), bst.inOrder());

        // 删除多个节点
        bst.delete(20);
        bst.delete(50);
        bst.delete(80);

        assertEquals(8, bst.size());
        assertEquals(Arrays.asList(10, 25, 30, 35, 40, 45, 60, 70), bst.inOrder());

        // 验证搜索
        assertTrue(bst.search(30));
        assertTrue(bst.search(45));
        assertFalse(bst.search(20));
        assertFalse(bst.search(50));
    }

    @Test
    void testLeftSkewedBST() {
        // 测试左斜BST
        bst.insert(5);
        bst.insert(4);
        bst.insert(3);
        bst.insert(2);
        bst.insert(1);

        assertEquals(Arrays.asList(1, 2, 3, 4, 5), bst.inOrder());
        assertEquals(5, bst.height());
        assertEquals(5, bst.size());
    }

    @Test
    void testRightSkewedBST() {
        // 测试右斜BST
        bst.insert(1);
        bst.insert(2);
        bst.insert(3);
        bst.insert(4);
        bst.insert(5);

        assertEquals(Arrays.asList(1, 2, 3, 4, 5), bst.inOrder());
        assertEquals(5, bst.height());
        assertEquals(5, bst.size());
    }

    @Test
    void testBalancedBST() {
        // 测试平衡BST
        int[] values = {4, 2, 6, 1, 3, 5, 7};
        for (int val : values) {
            bst.insert(val);
        }

        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7), bst.inOrder());
        assertEquals(3, bst.height());
        assertEquals(7, bst.size());
    }

    @Test
    void testInsertNoneRecursiveEmptyTree() {
        assertFalse(bst.search(5));
        assertTrue(bst.isEmpty());

        // 使用非递归插入
        bst.insertNoneRecursive(5);
        assertTrue(bst.search(5));
        assertFalse(bst.isEmpty());
        assertEquals(1, bst.size());
    }

    @Test
    void testInsertNoneRecursiveSingleNode() {
        bst.insertNoneRecursive(10);
        assertTrue(bst.search(10));
        assertFalse(bst.isEmpty());
        assertEquals(1, bst.size());
    }

    @Test
    void testInsertNoneRecursiveMultipleNodes() {
        // 使用非递归方式构建BST: 50,30,70,20,40,60,80
        bst.insertNoneRecursive(50);
        bst.insertNoneRecursive(30);
        bst.insertNoneRecursive(70);
        bst.insertNoneRecursive(20);
        bst.insertNoneRecursive(40);
        bst.insertNoneRecursive(60);
        bst.insertNoneRecursive(80);

        // 验证所有值都存在
        assertTrue(bst.search(50));
        assertTrue(bst.search(30));
        assertTrue(bst.search(70));
        assertTrue(bst.search(20));
        assertTrue(bst.search(40));
        assertTrue(bst.search(60));
        assertTrue(bst.search(80));

        // 验证不存在的值
        assertFalse(bst.search(25));
        assertFalse(bst.search(90));

        // 验证树的大小
        assertEquals(7, bst.size());

        // 验证中序遍历结果（升序排列）
        assertEquals(Arrays.asList(20, 30, 40, 50, 60, 70, 80), bst.inOrder());
    }

    @Test
    void testInsertNoneRecursiveDuplicateValues() {
        bst.insertNoneRecursive(10);
        bst.insertNoneRecursive(5);
        bst.insertNoneRecursive(10); // 重复值
        bst.insertNoneRecursive(5);  // 重复值
        bst.insertNoneRecursive(15);

        assertEquals(3, bst.size()); // 应该只有3个唯一值
        assertTrue(bst.search(10));
        assertTrue(bst.search(5));
        assertTrue(bst.search(15));
    }

    @Test
    void testInsertNoneRecursiveVsInsertConsistency() {
        // 创建两个BST，一个用递归插入，一个用非递归插入
        BinarySearchTree bstRecursive = new BinarySearchTree();
        BinarySearchTree bstNoneRecursive = new BinarySearchTree();

        int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};

        // 分别用两种方法插入相同的值
        for (int val : values) {
            bstRecursive.insert(val);
            bstNoneRecursive.insertNoneRecursive(val);
        }

        // 验证两个树的大小相同
        assertEquals(bstRecursive.size(), bstNoneRecursive.size());

        // 验证两个树的遍历结果相同
        assertEquals(bstRecursive.inOrder(), bstNoneRecursive.inOrder());
        assertEquals(bstRecursive.preOrder(), bstNoneRecursive.preOrder());
        assertEquals(bstRecursive.postOrder(), bstNoneRecursive.postOrder());

        // 验证搜索行为相同
        for (int val : values) {
            assertEquals(bstRecursive.search(val), bstNoneRecursive.search(val));
        }
    }

    @Test
    void testInsertNoneRecursiveLeftSkewedTree() {
        // 测试左斜树：5,4,3,2,1
        bst.insertNoneRecursive(5);
        bst.insertNoneRecursive(4);
        bst.insertNoneRecursive(3);
        bst.insertNoneRecursive(2);
        bst.insertNoneRecursive(1);

        assertEquals(5, bst.size());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), bst.inOrder());
        assertEquals(5, bst.height());
    }

    @Test
    void testInsertNoneRecursiveRightSkewedTree() {
        // 测试右斜树：1,2,3,4,5
        bst.insertNoneRecursive(1);
        bst.insertNoneRecursive(2);
        bst.insertNoneRecursive(3);
        bst.insertNoneRecursive(4);
        bst.insertNoneRecursive(5);

        assertEquals(5, bst.size());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), bst.inOrder());
        assertEquals(5, bst.height());
    }

    @Test
    void testInsertNoneRecursiveComplexOperations() {
        // 复杂操作序列测试
        int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};

        for (int val : values) {
            bst.insertNoneRecursive(val);
        }

        assertEquals(11, bst.size());
        assertEquals(Arrays.asList(10, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80), bst.inOrder());

        // 删除多个节点
        bst.delete(20);
        bst.delete(50);
        bst.delete(80);

        assertEquals(8, bst.size());
        assertEquals(Arrays.asList(10, 25, 30, 35, 40, 45, 60, 70), bst.inOrder());

        // 验证搜索
        assertTrue(bst.search(30));
        assertTrue(bst.search(45));
        assertFalse(bst.search(20));
        assertFalse(bst.search(50));
    }

    @Test
    void testSearchNoneRecursiveEmptyTree() {
        assertFalse(bst.searchNoneRecursive(5));
    }

    @Test
    void testSearchNoneRecursiveSingleNodeFound() {
        bst.insertNoneRecursive(10);
        assertTrue(bst.searchNoneRecursive(10));
    }

    @Test
    void testSearchNoneRecursiveSingleNodeNotFound() {
        bst.insertNoneRecursive(10);
        assertFalse(bst.searchNoneRecursive(5));
        assertFalse(bst.searchNoneRecursive(15));
    }

    @Test
    void testSearchNoneRecursiveMultipleNodes() {
        // 构建BST: 50,30,70,20,40,60,80
        bst.insertNoneRecursive(50);
        bst.insertNoneRecursive(30);
        bst.insertNoneRecursive(70);
        bst.insertNoneRecursive(20);
        bst.insertNoneRecursive(40);
        bst.insertNoneRecursive(60);
        bst.insertNoneRecursive(80);

        // 测试存在的值
        assertTrue(bst.searchNoneRecursive(50));
        assertTrue(bst.searchNoneRecursive(30));
        assertTrue(bst.searchNoneRecursive(70));
        assertTrue(bst.searchNoneRecursive(20));
        assertTrue(bst.searchNoneRecursive(40));
        assertTrue(bst.searchNoneRecursive(60));
        assertTrue(bst.searchNoneRecursive(80));

        // 测试不存在的值
        assertFalse(bst.searchNoneRecursive(25));
        assertFalse(bst.searchNoneRecursive(90));
        assertFalse(bst.searchNoneRecursive(0));
        assertFalse(bst.searchNoneRecursive(100));
    }

    @Test
    void testSearchNoneRecursiveLeftSkewedTree() {
        // 构建左斜树：5,4,3,2,1
        bst.insertNoneRecursive(5);
        bst.insertNoneRecursive(4);
        bst.insertNoneRecursive(3);
        bst.insertNoneRecursive(2);
        bst.insertNoneRecursive(1);

        // 测试所有存在的值
        assertTrue(bst.searchNoneRecursive(5));
        assertTrue(bst.searchNoneRecursive(4));
        assertTrue(bst.searchNoneRecursive(3));
        assertTrue(bst.searchNoneRecursive(2));
        assertTrue(bst.searchNoneRecursive(1));

        // 测试不存在的值
        assertFalse(bst.searchNoneRecursive(0));
        assertFalse(bst.searchNoneRecursive(6));
    }

    @Test
    void testSearchNoneRecursiveRightSkewedTree() {
        // 构建右斜树：1,2,3,4,5
        bst.insertNoneRecursive(1);
        bst.insertNoneRecursive(2);
        bst.insertNoneRecursive(3);
        bst.insertNoneRecursive(4);
        bst.insertNoneRecursive(5);

        // 测试所有存在的值
        assertTrue(bst.searchNoneRecursive(1));
        assertTrue(bst.searchNoneRecursive(2));
        assertTrue(bst.searchNoneRecursive(3));
        assertTrue(bst.searchNoneRecursive(4));
        assertTrue(bst.searchNoneRecursive(5));

        // 测试不存在的值
        assertFalse(bst.searchNoneRecursive(0));
        assertFalse(bst.searchNoneRecursive(6));
    }

    @Test
    void testSearchNoneRecursiveVsSearchConsistency() {
        // 创建两个BST，一个用递归搜索，一个用非递归搜索
        BinarySearchTree bst1 = new BinarySearchTree();
        BinarySearchTree bst2 = new BinarySearchTree();

        int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};

        // 构建相同的树
        for (int val : values) {
            bst1.insert(val);
            bst2.insert(val);
        }

        // 测试所有存在的值
        for (int val : values) {
            assertEquals(bst1.search(val), bst2.searchNoneRecursive(val));
        }

        // 测试不存在的值
        int[] nonExistentValues = {0, 15, 55, 90, 100};
        for (int val : nonExistentValues) {
            assertEquals(bst1.search(val), bst2.searchNoneRecursive(val));
        }
    }

    @Test
    void testSearchNoneRecursiveDuplicateValues() {
        bst.insertNoneRecursive(10);
        bst.insertNoneRecursive(10); // 重复插入
        bst.insertNoneRecursive(5);
        bst.insertNoneRecursive(15);

        assertTrue(bst.searchNoneRecursive(10));
        assertTrue(bst.searchNoneRecursive(5));
        assertTrue(bst.searchNoneRecursive(15));
        assertFalse(bst.searchNoneRecursive(20));
    }

    @Test
    void testSearchNoneRecursiveLargeTree() {
        // 测试较大规模的树
        int[] values = {50, 25, 75, 12, 37, 62, 87, 6, 18, 31, 43, 56, 68, 81, 93};

        for (int val : values) {
            bst.insertNoneRecursive(val);
        }

        // 测试所有存在的值
        for (int val : values) {
            assertTrue(bst.searchNoneRecursive(val));
        }

        // 测试不存在的值
        int[] nonExistentValues = {0, 1, 100, 99, 26, 74};
        for (int val : nonExistentValues) {
            assertFalse(bst.searchNoneRecursive(val));
        }
    }
}