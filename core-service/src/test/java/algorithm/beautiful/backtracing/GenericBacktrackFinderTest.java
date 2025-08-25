package algorithm.beautiful.backtracing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import algorithm.basicds.BTree;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * GenericBacktrackFinder类的单元测试
 *
 * 测试场景覆盖：
 * 1. 空树处理
 * 2. 默认配置（查找7，禁止3）
 * 3. 自定义目标值和约束值
 * 4. 约束条件生效
 * 5. 复杂树结构
 */
class GenericBacktrackFinderTest {

    private GenericBacktrackFinder finder;
    private BTree.Node root;

    @BeforeEach
    void setUp() {
        finder = new GenericBacktrackFinder(); // 默认查找7，禁止3
    }

    @Test
    void testEmptyTree() {
        // 测试空树情况
        List<List<BTree.Node>> result = finder.findPaths(null);
        assertTrue(result.isEmpty(), "空树应该返回空列表");
    }

    @Test
    void testDefaultConfigFindSeven() {
        // 测试默认配置：查找7，禁止3
        root = new BTree.Node(5);
        root.left = new BTree.Node(7);
        root.right = new BTree.Node(3); // 这个节点应该被禁止
        root.left.left = new BTree.Node(7);

        List<List<BTree.Node>> result = finder.findPaths(root);
        assertEquals(2, result.size(), "应该找到2条路径（避开值为3的节点）");

        // 验证路径内容
        boolean hasPath1 = false, hasPath2 = false;
        for (List<BTree.Node> path : result) {
            List<Integer> values = path.stream().map(node -> node.val).collect(Collectors.toList());
            if (values.equals(Arrays.asList(5, 7))) {
                hasPath1 = true;
            } else if (values.equals(Arrays.asList(5, 7, 7))) {
                hasPath2 = true;
            }
        }
        assertTrue(hasPath1 && hasPath2, "应该包含所有预期的路径");
    }

    @Test
    void testConstraintEffective() {
        // 测试约束条件生效：禁止选择值为3的节点
        root = new BTree.Node(1);
        root.left = new BTree.Node(3); // 禁止节点
        root.right = new BTree.Node(2);
        root.left.left = new BTree.Node(7); // 这个路径应该被禁止

        List<List<BTree.Node>> result = finder.findPaths(root);
        assertTrue(result.isEmpty(), "所有包含禁止值的路径都应该被过滤掉");
    }

    @Test
    void testCustomTargetAndConstraint() {
        // 测试自定义目标值和约束值：查找5，禁止2
        GenericBacktrackFinder customFinder = new GenericBacktrackFinder(5, 2);

        root = new BTree.Node(1);
        root.left = new BTree.Node(2); // 禁止节点
        root.right = new BTree.Node(3);
        root.right.left = new BTree.Node(5); // 目标节点

        List<List<BTree.Node>> result = customFinder.findPaths(root);
        assertEquals(1, result.size(), "应该找到1条路径（避开值为2的节点）");

        List<Integer> pathValues = result.get(0).stream().map(node -> node.val).collect(Collectors.toList());
        assertEquals(Arrays.asList(1, 3, 5), pathValues, "路径应该是[1,3,5]");
    }

    @Test
    void testStaticConvenienceMethods() {
        // 测试静态便捷方法
        root = new BTree.Node(5);
        root.left = new BTree.Node(7);
        root.right = new BTree.Node(3);

        // 测试默认的findSevenPaths方法
        List<List<BTree.Node>> result1 = GenericBacktrackFinder.findSevenPaths(root);
        assertEquals(1, result1.size(), "应该找到1条路径到7");

        // 测试自定义的findPathsWithConstraints方法
        List<List<BTree.Node>> result2 = GenericBacktrackFinder.findPathsWithConstraints(root, 5, 3);
        assertEquals(1, result2.size(), "应该找到1条路径到5（避开3）");
    }

    @Test
    void testComplexTreeWithConstraints() {
        // 测试复杂树结构下的约束条件
        root = new BTree.Node(1);
        root.left = new BTree.Node(2);
        root.right = new BTree.Node(3); // 禁止节点
        root.left.left = new BTree.Node(7);
        root.left.right = new BTree.Node(4);
        root.right.left = new BTree.Node(7); // 这个路径应该被禁止（经过3）
        root.right.right = new BTree.Node(5);

        List<List<BTree.Node>> result = finder.findPaths(root);
        assertEquals(1, result.size(), "应该只找到1条路径（避开经过3的路径）");

        List<Integer> pathValues = result.get(0).stream().map(node -> node.val).collect(Collectors.toList());
        assertEquals(Arrays.asList(1, 2, 7), pathValues, "唯一有效的路径应该是[1,2,7]");
    }

    @Test
    void testNoValidPaths() {
        // 测试没有有效路径的情况
        root = new BTree.Node(3); // 根节点就是禁止值
        root.left = new BTree.Node(7); // 无法到达，因为根节点被禁止

        List<List<BTree.Node>> result = finder.findPaths(root);
        assertTrue(result.isEmpty(), "根节点被禁止时应该返回空列表");
    }
}