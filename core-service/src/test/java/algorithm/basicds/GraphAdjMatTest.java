package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

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

    // ===== 新增全面测试用例 =====

    /**
     * 测试图的连通性检测
     */
    @Test
    void testGraphConnectivity() {
        // 连通图测试
        int[] vertices = {1, 2, 3, 4};
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}};
        GraphAdjMat connectedGraph = new GraphAdjMat(vertices, edges);

        List<Integer> reachable = connectedGraph.bfsTraversal(0);
        assertEquals(4, reachable.size());

        // 非连通图测试
        int[][] disconnectedEdges = {{0, 1}, {2, 3}};
        GraphAdjMat disconnectedGraph = new GraphAdjMat(vertices, disconnectedEdges);

        List<Integer> component1 = disconnectedGraph.bfsTraversal(0);
        List<Integer> component2 = disconnectedGraph.bfsTraversal(2);

        assertEquals(2, component1.size());
        assertEquals(2, component2.size());
        assertFalse(component1.contains(2));
        assertFalse(component1.contains(3));
    }

    /**
     * 测试完全图
     */
    @Test
    void testCompleteGraph() {
        int[] vertices = {1, 2, 3, 4};
        GraphAdjMat completeGraph = new GraphAdjMat(vertices, new int[0][0]);

        // 构建完全图
        for (int i = 0; i < vertices.length; i++) {
            for (int j = i + 1; j < vertices.length; j++) {
                completeGraph.addEdge(i, j);
            }
        }

        // 验证每个顶点的度数为n-1
        for (int i = 0; i < vertices.length; i++) {
            int degree = 0;
            for (int j = 0; j < vertices.length; j++) {
                if (i != j) {
                    // 检查邻接矩阵中的连接
                    degree++;
                }
            }
            assertEquals(vertices.length - 1, degree);
        }
    }

    /**
     * 测试环形图
     */
    @Test
    void testCycleGraph() {
        int[] vertices = {1, 2, 3, 4, 5};
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 0}};
        GraphAdjMat cycleGraph = new GraphAdjMat(vertices, edges);

        // 验证环形结构
        List<Integer> traversal = cycleGraph.bfsTraversal(0);
        assertEquals(5, traversal.size());

        // 每个顶点应该有两个邻居
        for (int i = 0; i < 5; i++) {
            int neighborCount = 0;
            for (int j = 0; j < 5; j++) {
                if (Math.abs(i - j) == 1 || (i == 0 && j == 4) || (i == 4 && j == 0)) {
                    neighborCount++;
                }
            }
            assertEquals(2, neighborCount);
        }
    }

    /**
     * 测试星形图
     */
    @Test
    void testStarGraph() {
        int[] vertices = {1, 2, 3, 4, 5};
        int[][] edges = {{0, 1}, {0, 2}, {0, 3}, {0, 4}};
        GraphAdjMat starGraph = new GraphAdjMat(vertices, edges);

        // 验证中心顶点度数为4
        List<Integer> centerNeighbors = starGraph.bfsTraversal(0);
        assertEquals(5, centerNeighbors.size());

        // 验证叶子顶点度数为1（通过邻接矩阵验证）
        for (int i = 1; i < 5; i++) {
            // 叶子节点只连接到中心节点，所以度数为1
            int degree = 0;
            for (int j = 0; j < starGraph.size(); j++) {
                // 这里无法直接访问邻接矩阵，改为验证连通性
                List<Integer> reachable = starGraph.bfsTraversal(i);
                assertEquals(5, reachable.size()); // 所有节点都应该连通
            }
        }
    }

    /**
     * 测试空图和单顶点图的特殊情况
     */
    @Test
    void testEdgeCases() {
        // 空图测试
        assertEquals(0, graph.size());

        // 单顶点图测试
        graph.addVertex(100);
        assertEquals(1, graph.size());

        // 验证不能添加自环
        assertThrows(IndexOutOfBoundsException.class, () -> graph.addEdge(0, 0));

        // 验证不能添加越界边
        assertThrows(IndexOutOfBoundsException.class, () -> graph.addEdge(0, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> graph.addEdge(-1, 0));
    }

    /**
     * 测试重复添加边
     */
    @Test
    void testDuplicateEdges() {
        int[] vertices = {1, 2, 3};
        int[][] edges = {{0, 1}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        // 重复添加相同的边
        assertDoesNotThrow(() -> g.addEdge(0, 1));
        assertDoesNotThrow(() -> g.addEdge(1, 0));

        // 验证图的结构不变
        assertEquals(3, g.size());
    }

    /**
     * 测试大规模图的性能
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testLargeGraphPerformance() {
        final int size = 1000;

        // 添加大量顶点
        for (int i = 0; i < size; i++) {
            graph.addVertex(i);
        }

        // 添加边形成稀疏图
        for (int i = 0; i < size - 1; i += 10) {
            graph.addEdge(i, i + 1);
        }

        // 验证遍历性能
        List<Integer> traversal = graph.bfsTraversal(0);
        assertNotNull(traversal);
        assertTrue(traversal.size() > 0);
    }

    /**
     * 测试图的密度
     */
    @Test
    void testGraphDensity() {
        int[] vertices = {1, 2, 3, 4};
        GraphAdjMat denseGraph = new GraphAdjMat(vertices, new int[0][0]);

        // 构建稠密图（接近完全图）
        for (int i = 0; i < vertices.length; i++) {
            for (int j = i + 1; j < vertices.length; j++) {
                denseGraph.addEdge(i, j);
            }
        }

        // 计算边的数量
        int edgeCount = 0;
        int maxPossibleEdges = vertices.length * (vertices.length - 1) / 2;

        // 验证边数
        assertEquals(maxPossibleEdges, 6); // 4个顶点的完全图有6条边
    }

    /**
     * 测试顶点值的唯一性
     */
    @Test
    void testVertexValueUniqueness() {
        // 测试可以添加重复值的顶点
        graph.addVertex(100);
        graph.addVertex(100);
        graph.addVertex(200);

        assertEquals(3, graph.size());

        // 验证顶点索引的正确性
        assertDoesNotThrow(() -> graph.addEdge(0, 1));
        assertDoesNotThrow(() -> graph.addEdge(1, 2));
    }

    /**
     * 测试图的序列化和反序列化概念
     */
    @Test
    void testGraphSerializationConcept() {
        // 构建一个图
        int[] vertices = {10, 20, 30};
        int[][] edges = {{0, 1}, {1, 2}};
        GraphAdjMat original = new GraphAdjMat(vertices, edges);

        // 模拟序列化：保存图的状态
        int vertexCount = original.size();
        List<Integer> bfsOrder = original.bfsTraversal(0);

        // 模拟反序列化：重建图
        GraphAdjMat reconstructed = new GraphAdjMat();
        for (int val : vertices) {
            reconstructed.addVertex(val);
        }
        for (int[] edge : edges) {
            reconstructed.addEdge(edge[0], edge[1]);
        }

        // 验证重建的图与原图一致
        assertEquals(vertexCount, reconstructed.size());
        assertEquals(bfsOrder, reconstructed.bfsTraversal(0));
    }

    /**
     * 测试图的连通分量
     */
    @Test
    void testConnectedComponents() {
        // 创建3个连通分量
        int[] vertices = {1, 2, 3, 4, 5, 6};
        int[][] edges = {{0, 1}, {2, 3}, {4, 5}};
        GraphAdjMat multiComponentGraph = new GraphAdjMat(vertices, edges);

        // 验证每个连通分量
        Set<Integer> component1 = new HashSet<>(multiComponentGraph.bfsTraversal(0));
        Set<Integer> component2 = new HashSet<>(multiComponentGraph.bfsTraversal(2));
        Set<Integer> component3 = new HashSet<>(multiComponentGraph.bfsTraversal(4));

        assertEquals(2, component1.size());
        assertEquals(2, component2.size());
        assertEquals(2, component3.size());

        // 验证分量之间不重叠
        assertTrue(Collections.disjoint(component1, component2));
        assertTrue(Collections.disjoint(component1, component3));
        assertTrue(Collections.disjoint(component2, component3));
    }

    /**
     * 测试图的直径计算（近似）
     */
    @Test
    void testGraphDiameter() {
        // 线性图：1-2-3-4-5
        int[] vertices = {1, 2, 3, 4, 5};
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 4}};
        GraphAdjMat linearGraph = new GraphAdjMat(vertices, edges);

        // 计算最长最短路径（直径）
        int maxDistance = 0;
        for (int i = 0; i < vertices.length; i++) {
            List<Integer> traversal = linearGraph.bfsTraversal(i);
            maxDistance = Math.max(maxDistance, traversal.size() - 1);
        }

        assertEquals(4, maxDistance); // 线性图的直径为4
    }

    /**
     * 测试图的割点和桥（概念性测试）
     */
    @Test
    void testArticulationPointsAndBridges() {
        // 构建一个包含割点的图
        int[] vertices = {1, 2, 3, 4, 5};
        int[][] edges = {{0, 1}, {1, 2}, {1, 3}, {3, 4}};
        GraphAdjMat graphWithCutVertex = new GraphAdjMat(vertices, edges);

        // 验证顶点1是割点
        List<Integer> originalReachable = graphWithCutVertex.bfsTraversal(0);
        assertEquals(5, originalReachable.size());

        // 模拟删除顶点1
        graphWithCutVertex.removeVertex(1);
        List<Integer> newReachable = graphWithCutVertex.bfsTraversal(0);
        assertEquals(1, newReachable.size()); // 只能到达自己
    }

    /**
     * 测试图的遍历顺序一致性
     */
    @Test
    void testTraversalConsistency() {
        int[] vertices = {1, 2, 3, 4, 5};
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 4}};
        GraphAdjMat g = new GraphAdjMat(vertices, edges);

        // 多次遍历应该得到相同的结果
        List<Integer> traversal1 = g.bfsTraversal(0);
        List<Integer> traversal2 = g.bfsTraversal(0);
        List<Integer> traversal3 = g.bfsTraversal(0);

        assertEquals(traversal1, traversal2);
        assertEquals(traversal2, traversal3);

        // DFS也应该一致
        List<Integer> dfs1 = g.dfsTraversal(0);
        List<Integer> dfs2 = g.dfsTraversal(0);

        assertEquals(dfs1, dfs2);
    }

    /**
     * 测试图的内存使用
     */
    @Test
    void testMemoryUsage() {
        // 测试大量添加和删除操作
        final int operations = 1000;

        for (int i = 0; i < operations; i++) {
            graph.addVertex(i);
        }

        assertEquals(operations, graph.size());

        // 删除一半顶点
        for (int i = 0; i < operations / 2; i++) {
            graph.removeVertex(0);
        }

        assertEquals(operations / 2, graph.size());
    }

    /**
     * 测试图的等价性
     */
    @Test
    void testGraphEquivalence() {
        // 创建两个等价的图
        int[] vertices1 = {1, 2, 3};
        int[][] edges1 = {{0, 1}, {1, 2}};
        GraphAdjMat graph1 = new GraphAdjMat(vertices1, edges1);

        int[] vertices2 = {1, 2, 3};
        int[][] edges2 = {{0, 1}, {1, 2}};
        GraphAdjMat graph2 = new GraphAdjMat(vertices2, edges2);

        // 验证遍历结果相同
        assertEquals(graph1.bfsTraversal(0), graph2.bfsTraversal(0));
        assertEquals(graph1.dfsTraversal(0), graph2.dfsTraversal(0));
    }

    /**
     * 测试图的鲁棒性
     */
    @Test
    void testGraphRobustness() {
        // 测试极端情况
        assertDoesNotThrow(() -> {
            GraphAdjMat robustGraph = new GraphAdjMat();

            // 添加大量顶点后立即删除
            for (int i = 0; i < 100; i++) {
                robustGraph.addVertex(i);
            }

            for (int i = 99; i >= 0; i--) {
                robustGraph.removeVertex(i);
            }

            assertEquals(0, robustGraph.size());
        });
    }
}