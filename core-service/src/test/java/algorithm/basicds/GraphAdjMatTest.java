package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * GraphAdjMat的测试类
 * 测试邻接矩阵图实现的所有功能，包括遍历算法
 */
class GraphAdjMatTest {

    private GraphAdjMat graph;

    @BeforeEach
    void setUp() {
        graph = new GraphAdjMat();
    }

    /**
     * 测试默认构造函数
     */
    @Test
    void testDefaultConstructor() {
        assertEquals(0, graph.size());
    }

    /**
     * 测试带参数的构造函数
     */
    @Test
    void testParameterizedConstructor() {
        int[] vertices = {1, 2, 3, 4};
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}};

        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        assertEquals(4, g.size());
    }

    /**
     * 测试添加顶点
     */
    @Test
    void testAddVertex() {
        assertEquals(0, graph.size());

        graph.addVertex(10);
        assertEquals(1, graph.size());

        graph.addVertex(20);
        assertEquals(2, graph.size());
    }

    /**
     * 测试删除顶点
     */
    @Test
    void testRemoveVertex() {
        // 创建测试图
        int[] vertices = {1, 2, 3};
        int[][] edges = {{0, 1}, {1, 2}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        assertEquals(3, g.size());

        // 删除中间顶点
        g.removeVertex(1);
        assertEquals(2, g.size());

        // 删除第一个顶点
        g.removeVertex(0);
        assertEquals(1, g.size());

        // 删除最后一个顶点
        g.removeVertex(0);
        assertEquals(0, g.size());
    }

    /**
     * 测试删除顶点时的边界情况
     */
    @Test
    void testRemoveVertexBoundary() {
        graph.addVertex(1);

        // 测试删除不存在的顶点
        assertThrows(IndexOutOfBoundsException.class, () -> graph.removeVertex(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> graph.removeVertex(1));
    }

    /**
     * 测试添加边
     */
    @Test
    void testAddEdge() {
        // 创建3个顶点的图
        for (int i = 0; i < 3; i++) {
            graph.addVertex(i);
        }

        // 添加有效边
        assertDoesNotThrow(() -> graph.addEdge(0, 1));
        assertDoesNotThrow(() -> graph.addEdge(1, 2));

        // 测试添加自环边
        assertThrows(IndexOutOfBoundsException.class, () -> graph.addEdge(0, 0));

        // 测试添加越界边
        assertThrows(IndexOutOfBoundsException.class, () -> graph.addEdge(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> graph.addEdge(0, 3));
    }

    /**
     * 测试删除边
     */
    @Test
    void testRemoveEdge() {
        // 创建测试图
        int[] vertices = {1, 2, 3};
        int[][] edges = {{0, 1}, {1, 2}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        // 删除存在的边
        assertDoesNotThrow(() -> g.removeEdge(0, 1));

        // 测试删除不存在的边（不会抛出异常）
        assertDoesNotThrow(() -> g.removeEdge(0, 2));

        // 测试删除自环边
        assertThrows(IndexOutOfBoundsException.class, () -> g.removeEdge(0, 0));

        // 测试删除越界边
        assertThrows(IndexOutOfBoundsException.class, () -> g.removeEdge(-1, 0));
    }

    /**
     * 测试空图操作
     */
    @Test
    void testEmptyGraphOperations() {
        assertEquals(0, graph.size());

        // 空图添加顶点
        graph.addVertex(1);
        assertEquals(1, graph.size());
    }

    /**
     * 测试复杂场景：添加和删除顶点后的图结构
     */
    @Test
    void testComplexScenario() {
        // 创建初始图
        int[] vertices = {10, 20, 30, 40, 50};
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {0, 4}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        assertEquals(5, g.size());

        // 删除中间顶点
        g.removeVertex(2); // 删除顶点30

        assertEquals(4, g.size());

        // 添加新顶点
        g.addVertex(60);
        assertEquals(5, g.size());

        // 添加新边
        assertDoesNotThrow(() -> g.addEdge(2, 4)); // 新顶点60和顶点50之间的边

        // 删除所有顶点
        for (int i = 4; i >= 0; i--) {
            g.removeVertex(i);
        }
        assertEquals(0, g.size());
    }

    /**
     * 测试单顶点图
     */
    @Test
    void testSingleVertexGraph() {
        graph.addVertex(100);
        assertEquals(1, graph.size());

        // 单顶点图不能添加边
        assertThrows(IndexOutOfBoundsException.class, () -> graph.addEdge(0, 1));

        // 可以删除单顶点
        graph.removeVertex(0);
        assertEquals(0, graph.size());
    }

    /**
     * 测试双顶点图
     */
    @Test
    void testTwoVertexGraph() {
        graph.addVertex(1);
        graph.addVertex(2);
        assertEquals(2, graph.size());

        // 添加边
        assertDoesNotThrow(() -> graph.addEdge(0, 1));

        // 删除边
        assertDoesNotThrow(() -> graph.removeEdge(0, 1));

        // 删除一个顶点
        graph.removeVertex(0);
        assertEquals(1, graph.size());
    }

    /**
     * 测试大量顶点和边的性能
     */
    @Test
    void testPerformanceWithLargeGraph() {
        final int vertexCount = 1000;

        // 添加大量顶点
        for (int i = 0; i < vertexCount; i++) {
            graph.addVertex(i);
        }
        assertEquals(vertexCount, graph.size());

        // 添加一些边
        for (int i = 0; i < vertexCount - 1; i++) {
            graph.addEdge(i, i + 1);
        }

        // 删除一些顶点
        for (int i = 0; i < 100; i++) {
            graph.removeVertex(0);
        }
        assertEquals(vertexCount - 100, graph.size());
    }

    // ===== BFS遍历测试用例 =====

    @Test
    void testBfsTraversalSimplePath() {
        // 测试简单路径图: 0-1-2-3
        int[] vertices = {10, 20, 30, 40};
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        List<Integer> result = g.bfsTraversal(0);
        assertEquals(4, result.size());
        assertEquals(0, result.get(0).intValue());
        assertEquals(1, result.get(1).intValue());
        assertEquals(2, result.get(2).intValue());
        assertEquals(3, result.get(3).intValue());
    }

    @Test
    void testBfsTraversalStarGraph() {
        // 测试星形图: 0为中心，连接1,2,3
        int[] vertices = {10, 20, 30, 40};
        int[][] edges = {{0, 1}, {0, 2}, {0, 3}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        List<Integer> result = g.bfsTraversal(0);
        assertEquals(4, result.size());
        assertEquals(0, result.get(0).intValue());

        // 验证第二层节点（1,2,3）都在结果中
        List<Integer> secondLevel = Arrays.asList(
                result.get(1), result.get(2), result.get(3)
        );
        assertTrue(secondLevel.contains(1));
        assertTrue(secondLevel.contains(2));
        assertTrue(secondLevel.contains(3));
    }

    @Test
    void testBfsTraversalDisconnectedGraph() {
        // 测试非连通图: 0-1 和 2-3 两个独立子图
        int[] vertices = {10, 20, 30, 40};
        int[][] edges = {{0, 1}, {2, 3}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        List<Integer> result = g.bfsTraversal(0);
        assertEquals(2, result.size());
        assertEquals(0, result.get(0).intValue());
        assertEquals(1, result.get(1).intValue());
    }

    @Test
    void testBfsTraversalSingleVertex() {
        // 测试单顶点图
        graph.addVertex(100);

        List<Integer> result = graph.bfsTraversal(0);
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).intValue());
    }

    @Test
    void testBfsTraversalEmptyGraph() {
        // 测试空图 - 应该抛出异常
        assertThrows(IndexOutOfBoundsException.class, () -> graph.bfsTraversal(0));
    }

    @Test
    void testBfsTraversalNonExistentVertex() {
        // 测试不存在的顶点 - 应该抛出异常
        graph.addVertex(1);
        graph.addVertex(2);

        assertThrows(IndexOutOfBoundsException.class, () -> graph.bfsTraversal(2));
        assertThrows(IndexOutOfBoundsException.class, () -> graph.bfsTraversal(-1));
    }

    @Test
    void testBfsTraversalComplexGraph() {
        // 测试复杂图结构
        // 图结构:
        //     0
        //    / \
        //   1   2
        //  / \   \
        // 3   4   5
        int[] vertices = {10, 20, 30, 40, 50, 60};
        int[][] edges = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        List<Integer> result = g.bfsTraversal(0);
        assertEquals(6, result.size());
        assertEquals(0, result.get(0).intValue());

        // 验证层次遍历顺序
        List<Integer> secondLevel = Arrays.asList(result.get(1), result.get(2));
        assertTrue(secondLevel.contains(1));
        assertTrue(secondLevel.contains(2));

        List<Integer> thirdLevel = Arrays.asList(result.get(3), result.get(4), result.get(5));
        assertTrue(thirdLevel.contains(3));
        assertTrue(thirdLevel.contains(4));
        assertTrue(thirdLevel.contains(5));
    }

    @Test
    void testBfsTraversalWithCycle() {
        // 测试包含环的图
        // 图结构: 0-1-2-0 (三角形)
        int[] vertices = {10, 20, 30};
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        List<Integer> result = g.bfsTraversal(0);
        assertEquals(3, result.size());
        assertEquals(0, result.get(0).intValue());

        // 验证所有节点都被访问且没有重复
        assertTrue(result.contains(0));
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
    }

    // ===== DFS遍历测试用例 =====

    @Test
    void testDfsTraversalSimplePath() {
        // 测试简单路径图: 0-1-2-3
        int[] vertices = {10, 20, 30, 40};
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        List<Integer> result = g.dfsTraversal(0);
        assertEquals(4, result.size());
        assertEquals(0, result.get(0).intValue());
    }

    @Test
    void testDfsTraversalStarGraph() {
        // 测试星形图: 0为中心，连接1,2,3
        int[] vertices = {10, 20, 30, 40};
        int[][] edges = {{0, 1}, {0, 2}, {0, 3}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        List<Integer> result = g.dfsTraversal(0);
        assertEquals(4, result.size());
        assertEquals(0, result.get(0).intValue());
    }

    @Test
    void testDfsTraversalDisconnectedGraph() {
        // 测试非连通图: 0-1 和 2-3 两个独立子图
        int[] vertices = {10, 20, 30, 40};
        int[][] edges = {{0, 1}, {2, 3}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        List<Integer> result = g.dfsTraversal(0);
        assertEquals(2, result.size());
        assertEquals(0, result.get(0).intValue());
        assertEquals(1, result.get(1).intValue());
    }

    @Test
    void testDfsTraversalSingleVertex() {
        // 测试单顶点图
        graph.addVertex(100);

        List<Integer> result = graph.dfsTraversal(0);
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).intValue());
    }

    @Test
    void testDfsTraversalEmptyGraph() {
        // 测试空图 - 应该抛出异常
        assertThrows(IndexOutOfBoundsException.class, () -> graph.dfsTraversal(0));
    }

    @Test
    void testDfsTraversalNonExistentVertex() {
        // 测试不存在的顶点 - 应该抛出异常
        graph.addVertex(1);
        graph.addVertex(2);

        assertThrows(IndexOutOfBoundsException.class, () -> graph.dfsTraversal(2));
        assertThrows(IndexOutOfBoundsException.class, () -> graph.dfsTraversal(-1));
    }

    @Test
    void testDfsTraversalComplexGraph() {
        // 测试复杂图结构
        int[] vertices = {10, 20, 30, 40, 50, 60};
        int[][] edges = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        List<Integer> result = g.dfsTraversal(0);
        assertEquals(6, result.size());
        assertEquals(0, result.get(0).intValue());
    }

    @Test
    void testDfsTraversalWithCycle() {
        // 测试包含环的图
        int[] vertices = {10, 20, 30};
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        List<Integer> result = g.dfsTraversal(0);
        assertEquals(3, result.size());
        assertEquals(0, result.get(0).intValue());
    }

    @Test
    void testDfsTraversalTreeStructure() {
        // 测试树形结构
        // 图结构:
        //     0
        //    / \
        //   1   2
        //  / \
        // 3   4
        int[] vertices = {10, 20, 30, 40, 50};
        int[][] edges = {{0, 1}, {0, 2}, {1, 3}, {1, 4}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        List<Integer> result = g.dfsTraversal(0);
        assertEquals(5, result.size());
        assertEquals(0, result.get(0).intValue());
    }

    @Test
    void testTraversalMethodsConsistency() {
        // 测试遍历方法的一致性
        int[] vertices = {1, 2, 3, 4, 5};
        int[][] edges = {{0, 1}, {0, 2}, {1, 3}, {2, 4}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        List<Integer> bfsResult = g.bfsTraversal(0);
        List<Integer> dfsResult = g.dfsTraversal(0);

        // 两种遍历都应该访问所有顶点
        assertEquals(5, bfsResult.size());
        assertEquals(5, dfsResult.size());

        // 结果应该包含所有顶点索引
        for (int i = 0; i < 5; i++) {
            assertTrue(bfsResult.contains(i));
            assertTrue(dfsResult.contains(i));
        }
    }
}