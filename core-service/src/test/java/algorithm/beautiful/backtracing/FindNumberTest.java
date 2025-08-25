package algorithm.beautiful.backtracing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import algorithm.basicds.BTree;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * FindNumber类的单元测试
 *
 * 测试场景覆盖：
 * 1. 空树处理
 * 2. 单节点树
 * 3. 目标值在根节点
 * 4. 目标值在叶子节点
 * 5. 目标值在内部节点
 * 6. 重复目标值
 * 7. 目标值不存在
 * 8. 复杂树结构
 */
class FindNumberTest {

    private FindNumber finder;
    private BTree.Node root;  // 改为直接使用Node类型

    @BeforeEach
    void setUp() {
        finder = new FindNumber();
    }

    @Test
    void testEmptyTree() {
        // 测试空树情况
        List<List<Integer>> result = finder.findNumberPath(null, 5);
        assertTrue(result.isEmpty(), "空树应该返回空列表");
    }

    @Test
    void testSingleNodeFound() {
        // 测试单节点树，找到目标值
        root = new BTree.Node(5);

        List<List<Integer>> result = finder.findNumberPath(root, 5);
        assertEquals(1, result.size(), "应该找到1条路径");
        assertEquals(Arrays.asList(5), result.get(0), "路径应该是[5]");
    }

    @Test
    void testSingleNodeNotFound() {
        // 测试单节点树，未找到目标值
        root = new BTree.Node(3);

        List<List<Integer>> result = finder.findNumberPath(root, 5);
        assertTrue(result.isEmpty(), "未找到目标值应该返回空列表");
    }

    @Test
    void testTargetAtRoot() {
        // 测试目标值在根节点
        root = new BTree.Node(7);
        root.left = new BTree.Node(3);
        root.right = new BTree.Node(9);

        List<List<Integer>> result = finder.findNumberPath(root, 7);
        assertEquals(1, result.size(), "应该找到1条路径");
        assertEquals(Arrays.asList(7), result.get(0), "根节点路径应该是[7]");
    }

    @Test
    void testTargetAtLeaf() {
        // 测试目标值在叶子节点
        root = new BTree.Node(5);
        root.left = new BTree.Node(3);
        root.right = new BTree.Node(7);
        root.left.left = new BTree.Node(2);
        root.left.right = new BTree.Node(4);

        List<List<Integer>> result = finder.findNumberPath(root, 2);
        assertEquals(1, result.size(), "应该找到1条路径");
        assertEquals(Arrays.asList(5, 3, 2), result.get(0), "路径应该是[5,3,2]");
    }

    @Test
    void testMultipleTargets() {
        // 测试树中有多个相同目标值
        root = new BTree.Node(5);
        root.left = new BTree.Node(7);
        root.right = new BTree.Node(7);
        root.left.left = new BTree.Node(7);

        List<List<Integer>> result = finder.findNumberPath(root, 7);
        assertEquals(3, result.size(), "应该找到3条路径");

        // 验证所有找到的路径 - 使用更精确的验证方式
        boolean hasPath1 = false, hasPath2 = false, hasPath3 = false;
        for (List<Integer> path : result) {
            if (path.equals(Arrays.asList(5, 7))) {
                if (!hasPath1) {
                    hasPath1 = true;
                } else {
                    hasPath2 = true;
                }
            } else if (path.equals(Arrays.asList(5, 7, 7))) {
                hasPath3 = true;
            }
        }
        assertTrue(hasPath1 && hasPath2 && hasPath3, "应该包含所有预期的路径");
    }

    @Test
    void testTargetNotFound() {
        // 测试目标值不存在于树中
        root = new BTree.Node(1);
        root.left = new BTree.Node(2);
        root.right = new BTree.Node(3);
        root.left.left = new BTree.Node(4);
        root.left.right = new BTree.Node(5);

        List<List<Integer>> result = finder.findNumberPath(root, 99);
        assertTrue(result.isEmpty(), "目标值不存在应该返回空列表");
    }

    @Test
    void testComplexTree() {
        // 测试复杂树结构
        root = new BTree.Node(5);
        root.left = new BTree.Node(3);
        root.right = new BTree.Node(7);
        root.left.left = new BTree.Node(7);
        root.left.right = new BTree.Node(2);
        root.right.left = new BTree.Node(6);
        root.right.right = new BTree.Node(7);

        List<List<Integer>> result = finder.findNumberPath(root, 7);
        assertEquals(3, result.size(), "应该找到3条路径");

        // 验证所有找到的路径
        assertTrue(result.stream().anyMatch(path -> path.equals(Arrays.asList(5, 3, 7))), "应该包含路径[5,3,7]");
        assertTrue(result.stream().anyMatch(path -> path.equals(Arrays.asList(5, 7))), "应该包含路径[5,7]");
        assertTrue(result.stream().anyMatch(path -> path.equals(Arrays.asList(5, 7, 7))), "应该包含路径[5,7,7]");
    }

    @Test
    void testNegativeValues() {
        // 测试包含负值的树
        root = new BTree.Node(-5);
        root.left = new BTree.Node(-3);
        root.right = new BTree.Node(-7);

        List<List<Integer>> result = finder.findNumberPath(root, -3);
        assertEquals(1, result.size(), "应该找到1条路径");
        assertEquals(Arrays.asList(-5, -3), result.get(0), "路径应该是[-5,-3]");
    }

    @Test
    void testDeepTree() {
        // 测试深度较大的树
        root = new BTree.Node(1);
        root.left = new BTree.Node(2);
        root.left.left = new BTree.Node(3);
        root.left.left.left = new BTree.Node(4);
        root.left.left.left.left = new BTree.Node(5);

        List<List<Integer>> result = finder.findNumberPath(root, 5);
        assertEquals(1, result.size(), "应该找到1条路径");
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result.get(0), "路径应该是[1,2,3,4,5]");
    }
}