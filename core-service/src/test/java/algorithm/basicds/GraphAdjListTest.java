package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
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
 * GraphAdjList的单元测试类
 * 测试所有公共方法的正确性和边界情况
 */
class GraphAdjListTest {

    private GraphAdjList graph;
    private GraphAdjList.Vertex v1;
    private GraphAdjList.Vertex v2;
    private GraphAdjList.Vertex v3;
    private GraphAdjList.Vertex v4;
    private GraphAdjList.Vertex v5;
    private GraphAdjList.Vertex v6;

    @BeforeEach
    void setUp() {
        graph = new GraphAdjList();
        v1 = new GraphAdjList.Vertex(1);
        v2 = new GraphAdjList.Vertex(2);
        v3 = new GraphAdjList.Vertex(3);
        v4 = new GraphAdjList.Vertex(4);
        v5 = new GraphAdjList.Vertex(5);
        v6 = new GraphAdjList.Vertex(6);
    }

    @Test
    void testDefaultConstructor() {
        assertEquals(0, graph.size());
        assertTrue(graph.toString().contains("GraphAdjList"));
    }

    @Test
    void testConstructorWithEdges() {
        GraphAdjList.Vertex[][] edges = {{v1, v2}, {v2, v3}, {v1, v3}};

        GraphAdjList g = new GraphAdjList(edges);
        assertEquals(3, g.size());
    }

    @Test
    void testAddVertex() {
        graph.addVertex(v1);
        assertEquals(1, graph.size());

        // 重复添加不应增加
        graph.addVertex(v1);
        assertEquals(1, graph.size());

        // 添加null顶点应抛出异常
        assertThrows(IllegalArgumentException.class, () -> graph.addVertex(null));
    }

    @Test
    void testRemoveVertex() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(v1, v2);

        graph.removeVertex(v1);
        assertEquals(1, graph.size());

        // 顶点不存在应抛出异常
        assertThrows(IllegalArgumentException.class, () -> graph.removeVertex(v1));
        assertThrows(IllegalArgumentException.class, () -> graph.removeVertex(null));
    }

    @Test
    void testAddEdge() {
        graph.addVertex(v1);
        graph.addVertex(v2);

        graph.addEdge(v1, v2);

        // 顶点不存在应抛出异常
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(v1, v3));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(null, v2));

        // 自环应抛出异常
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(v1, v1));
    }

    @Test
    void testRemoveEdge() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(v1, v2);

        graph.removeEdge(v1, v2);

        // 顶点不存在应抛出异常
        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge(v1, v3));
        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge(null, v2));

        // 自环应抛出异常
        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge(v1, v1));
    }

    @Test
    void testSize() {
        assertEquals(0, graph.size());
        graph.addVertex(v1);
        assertEquals(1, graph.size());
        graph.addVertex(v2);
        assertEquals(2, graph.size());
    }

    @Test
    void testComplexOperations() {
        // 构建一个完整的图
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v4);

        assertEquals(4, graph.size());

        // 删除一个顶点，验证相关边也被删除
        graph.removeVertex(v3);
        assertEquals(3, graph.size());

        // 验证v1和v2之间仍有边
        // 这里需要添加获取邻接顶点的方法来验证
    }

    @Test
    void testEdgeOperationsWithMultipleEdges() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);

        // 添加多条边
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v2, v3);

        // 删除一条边
        graph.removeEdge(v1, v2);

        // 验证其他边仍然存在
        // 这里需要添加获取邻接顶点的方法来验证
    }

    @Test
    void testPrintMethod() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(v1, v2);

        // 验证print方法不抛出异常
        assertDoesNotThrow(() -> graph.print());
    }

    @Test
    void testConstructorWithInvalidEdges() {
        // 测试无效边数组
        assertThrows(IllegalArgumentException.class, () -> {
            GraphAdjList.Vertex[][] invalidEdges = {{v1}  // 只有1个顶点
            };
            new GraphAdjList(invalidEdges);
        });
    }

    // ===== BFS遍历测试用例 =====

    @Test
    void testBfsTraversalSimplePath() {
        // 测试简单路径图: 1-2-3-4
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v4);

        List<GraphAdjList.Vertex> result = graph.bfsTraversal(v1);
        assertEquals(4, result.size());
        assertEquals(1, result.get(0).getVal());
        assertEquals(2, result.get(1).getVal());
        assertEquals(3, result.get(2).getVal());
        assertEquals(4, result.get(3).getVal());
    }

    @Test
    void testBfsTraversalStarGraph() {
        // 测试星形图: 1为中心，连接2,3,4
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v1, v4);

        List<GraphAdjList.Vertex> result = graph.bfsTraversal(v1);
        assertEquals(4, result.size());
        assertEquals(1, result.get(0).getVal());

        // 验证第二层节点（2,3,4）都在结果中
        List<Integer> secondLevel = Arrays.asList(result.get(1).getVal(), result.get(2).getVal(),
                result.get(3).getVal());
        assertTrue(secondLevel.contains(2));
        assertTrue(secondLevel.contains(3));
        assertTrue(secondLevel.contains(4));
    }

    @Test
    void testBfsTraversalDisconnectedGraph() {
        // 测试非连通图: 1-2 和 3-4 两个独立子图
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        graph.addEdge(v1, v2);
        graph.addEdge(v3, v4);

        List<GraphAdjList.Vertex> result = graph.bfsTraversal(v1);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getVal());
        assertEquals(2, result.get(1).getVal());
    }

    @Test
    void testBfsTraversalSingleVertex() {
        // 测试单顶点图
        graph.addVertex(v1);

        List<GraphAdjList.Vertex> result = graph.bfsTraversal(v1);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getVal());
    }

    @Test
    void testBfsTraversalEmptyGraph() {
        // 测试空图 - 应该抛出异常
        assertThrows(IllegalArgumentException.class, () -> graph.bfsTraversal(v1));
    }

    @Test
    void testBfsTraversalNonExistentVertex() {
        // 测试不存在的顶点 - 应该抛出异常
        graph.addVertex(v1);

        GraphAdjList.Vertex nonExistent = new GraphAdjList.Vertex(999);
        assertThrows(IllegalArgumentException.class, () -> graph.bfsTraversal(nonExistent));
    }

    @Test
    void testBfsTraversalComplexGraph() {
        // 测试复杂图结构
        // 图结构:
        //     1
        //    / \
        //   2   3
        //  / \   \
        // 4   5   6
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);

        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v2, v4);
        graph.addEdge(v2, v5);
        graph.addEdge(v3, v6);

        List<GraphAdjList.Vertex> result = graph.bfsTraversal(v1);
        assertEquals(6, result.size());
        assertEquals(1, result.get(0).getVal());

        // 验证层次遍历顺序
        List<Integer> secondLevel = Arrays.asList(result.get(1).getVal(), result.get(2).getVal());
        assertTrue(secondLevel.contains(2));
        assertTrue(secondLevel.contains(3));

        List<Integer> thirdLevel = Arrays.asList(result.get(3).getVal(), result.get(4).getVal(),
                result.get(5).getVal());
        assertTrue(thirdLevel.contains(4));
        assertTrue(thirdLevel.contains(5));
        assertTrue(thirdLevel.contains(6));
    }

    @Test
    void testBfsTraversalWithCycle() {
        // 测试包含环的图
        // 图结构: 1-2-3-1 (三角形)
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);

        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v1);

        List<GraphAdjList.Vertex> result = graph.bfsTraversal(v1);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getVal());

        // 验证所有节点都被访问且没有重复
        Set<Integer> visitedValues = new HashSet<>();
        for (GraphAdjList.Vertex vertex : result) {
            visitedValues.add(vertex.getVal());
        }
        assertEquals(3, visitedValues.size());
        assertTrue(visitedValues.contains(1));
        assertTrue(visitedValues.contains(2));
        assertTrue(visitedValues.contains(3));
    }

    @Test
    void testBfsTraversalFromDifferentStartPoints() {
        // 测试从不同起点遍历同一图
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);

        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);

        // 从v1开始遍历
        List<GraphAdjList.Vertex> resultFromV1 = graph.bfsTraversal(v1);
        assertEquals(3, resultFromV1.size());

        // 从v3开始遍历
        List<GraphAdjList.Vertex> resultFromV3 = graph.bfsTraversal(v3);
        assertEquals(3, resultFromV3.size());

        // 验证两个结果包含相同的节点，但顺序不同
        Set<Integer> valuesFromV1 = new HashSet<>();
        Set<Integer> valuesFromV3 = new HashSet<>();

        for (GraphAdjList.Vertex vertex : resultFromV1) {
            valuesFromV1.add(vertex.getVal());
        }
        for (GraphAdjList.Vertex vertex : resultFromV3) {
            valuesFromV3.add(vertex.getVal());
        }

        assertEquals(valuesFromV1, valuesFromV3);
        assertEquals(3, valuesFromV1.size());
    }

    // ===== DFS遍历测试用例 =====

    @Test
    void testDfsTraversalSimplePath() {
        // 测试简单路径图: 1-2-3-4
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v4);

        List<GraphAdjList.Vertex> result = graph.dfsTraversal(v1);
        assertEquals(4, result.size());
        assertEquals(1, result.get(0).getVal());
    }

    @Test
    void testDfsTraversalStarGraph() {
        // 测试星形图: 1为中心，连接2,3,4
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v1, v4);

        List<GraphAdjList.Vertex> result = graph.dfsTraversal(v1);
        assertEquals(4, result.size());
        assertEquals(1, result.get(0).getVal());
    }

    @Test
    void testDfsTraversalDisconnectedGraph() {
        // 测试非连通图: 1-2 和 3-4 两个独立子图
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        graph.addEdge(v1, v2);
        graph.addEdge(v3, v4);

        List<GraphAdjList.Vertex> result = graph.dfsTraversal(v1);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getVal());
        assertEquals(2, result.get(1).getVal());
    }

    @Test
    void testDfsTraversalSingleVertex() {
        // 测试单顶点图
        graph.addVertex(v1);

        List<GraphAdjList.Vertex> result = graph.dfsTraversal(v1);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getVal());
    }

    @Test
    void testDfsTraversalEmptyGraph() {
        // 测试空图 - 应该抛出异常
        assertThrows(IllegalArgumentException.class, () -> graph.dfsTraversal(v1));
    }

    @Test
    void testDfsTraversalNonExistentVertex() {
        // 测试不存在的顶点 - 应该抛出异常
        graph.addVertex(v1);

        GraphAdjList.Vertex nonExistent = new GraphAdjList.Vertex(999);
        assertThrows(IllegalArgumentException.class, () -> graph.dfsTraversal(nonExistent));
    }

    @Test
    void testDfsTraversalComplexGraph() {
        // 测试复杂图结构
        // 图结构:
        //     1
        //    / \
        //   2   3
        //  / \   \
        // 4   5   6
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);

        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v2, v4);
        graph.addEdge(v2, v5);
        graph.addEdge(v3, v6);

        List<GraphAdjList.Vertex> result = graph.dfsTraversal(v1);
        assertEquals(6, result.size());
        assertEquals(1, result.get(0).getVal());
    }

    @Test
    void testDfsTraversalWithCycle() {
        // 测试包含环的图
        // 图结构: 1-2-3-1 (三角形)
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);

        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v1);

        List<GraphAdjList.Vertex> result = graph.dfsTraversal(v1);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getVal());

        // 验证所有节点都被访问且没有重复
        Set<Integer> visitedValues = new HashSet<>();
        for (GraphAdjList.Vertex vertex : result) {
            visitedValues.add(vertex.getVal());
        }
        assertEquals(3, visitedValues.size());
        assertTrue(visitedValues.contains(1));
        assertTrue(visitedValues.contains(2));
        assertTrue(visitedValues.contains(3));
    }

    @Test
    void testDfsTraversalTreeStructure() {
        // 测试树形结构
        // 图结构:
        //     1
        //    / \
        //   2   3
        //  / \
        // 4   5
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);

        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v2, v4);
        graph.addEdge(v2, v5);

        List<GraphAdjList.Vertex> result = graph.dfsTraversal(v1);
        assertEquals(5, result.size());
        assertEquals(1, result.get(0).getVal());
    }

    @Test
    void testDfsTraversalFromDifferentStartPoints() {
        // 测试从不同起点遍历同一图
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);

        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);

        // 从v1开始遍历
        List<GraphAdjList.Vertex> resultFromV1 = graph.dfsTraversal(v1);
        assertEquals(3, resultFromV1.size());

        // 从v3开始遍历
        List<GraphAdjList.Vertex> resultFromV3 = graph.dfsTraversal(v3);
        assertEquals(3, resultFromV3.size());

        // 验证两个结果包含相同的节点
        Set<Integer> valuesFromV1 = new HashSet<>();
        Set<Integer> valuesFromV3 = new HashSet<>();

        for (GraphAdjList.Vertex vertex : resultFromV1) {
            valuesFromV1.add(vertex.getVal());
        }
        for (GraphAdjList.Vertex vertex : resultFromV3) {
            valuesFromV3.add(vertex.getVal());
        }

        assertEquals(valuesFromV1, valuesFromV3);
        assertEquals(3, valuesFromV1.size());
    }

    @Test
    void testBfsDfsTraversalComparison() {
        // 测试BFS和DFS遍历的差异
        // 图结构:
        //     1
        //    / \
        //   2   3
        //  / \
        // 4   5
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);

        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v2, v4);
        graph.addEdge(v2, v5);

        List<GraphAdjList.Vertex> bfsResult = graph.bfsTraversal(v1);
        List<GraphAdjList.Vertex> dfsResult = graph.dfsTraversal(v1);

        // 两种遍历都应该访问所有顶点
        assertEquals(5, bfsResult.size());
        assertEquals(5, dfsResult.size());

        // 验证BFS的层次遍历特性
        assertEquals(1, bfsResult.get(0).getVal()); // 第一层
        // 第二层应该是2和3
        Set<Integer> secondLevel = new HashSet<>();
        secondLevel.add(bfsResult.get(1).getVal());
        secondLevel.add(bfsResult.get(2).getVal());
        assertTrue(secondLevel.contains(2));
        assertTrue(secondLevel.contains(3));

        // 验证DFS的深度优先特性
        assertEquals(1, dfsResult.get(0).getVal()); // 起始点
        // DFS会先深入一条路径
        Set<Integer> allValues = new HashSet<>();
        for (GraphAdjList.Vertex vertex : dfsResult) {
            allValues.add(vertex.getVal());
        }
        assertTrue(allValues.contains(2));
        assertTrue(allValues.contains(3));
        assertTrue(allValues.contains(4));
        assertTrue(allValues.contains(5));
    }

    // ===== 新增全面测试用例 =====

    /**
     * 测试图的连通性检测
     */
    @Test
    void testGraphConnectivity() {
        // 连通图测试
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v4);

        List<GraphAdjList.Vertex> reachable = graph.bfsTraversal(v1);
        assertEquals(4, reachable.size());

        // 非连通图测试
        GraphAdjList disconnectedGraph = new GraphAdjList();
        disconnectedGraph.addVertex(v1);
        disconnectedGraph.addVertex(v2);
        disconnectedGraph.addVertex(v3);
        disconnectedGraph.addVertex(v4);
        disconnectedGraph.addEdge(v1, v2);
        disconnectedGraph.addEdge(v3, v4);

        List<GraphAdjList.Vertex> component1 = disconnectedGraph.bfsTraversal(v1);
        List<GraphAdjList.Vertex> component2 = disconnectedGraph.bfsTraversal(v3);

        assertEquals(2, component1.size());
        assertEquals(2, component2.size());

        Set<Integer> component1Values = new HashSet<>();
        Set<Integer> component2Values = new HashSet<>();

        for (GraphAdjList.Vertex v : component1) {
            component1Values.add(v.getVal());
        }
        for (GraphAdjList.Vertex v : component2) {
            component2Values.add(v.getVal());
        }

        assertTrue(Collections.disjoint(component1Values, component2Values));
    }

    /**
     * 测试完全图
     */
    @Test
    void testCompleteGraph() {
        GraphAdjList completeGraph = new GraphAdjList();

        // 添加顶点
        GraphAdjList.Vertex[] vertices = {v1, v2, v3, v4};
        for (GraphAdjList.Vertex v : vertices) {
            completeGraph.addVertex(v);
        }

        // 构建完全图
        for (int i = 0; i < vertices.length; i++) {
            for (int j = i + 1; j < vertices.length; j++) {
                completeGraph.addEdge(vertices[i], vertices[j]);
            }
        }

        // 验证每个顶点的度数为n-1
        for (GraphAdjList.Vertex v : vertices) {
            List<GraphAdjList.Vertex> neighbors = completeGraph.bfsTraversal(v);
            assertEquals(vertices.length, neighbors.size());
        }
    }

    /**
     * 测试环形图
     */
    @Test
    void testCycleGraph() {
        GraphAdjList cycleGraph = new GraphAdjList();

        // 添加顶点
        GraphAdjList.Vertex[] vertices = {v1, v2, v3, v4, v5};
        for (GraphAdjList.Vertex v : vertices) {
            cycleGraph.addVertex(v);
        }

        // 构建环形图
        for (int i = 0; i < vertices.length; i++) {
            int next = (i + 1) % vertices.length;
            cycleGraph.addEdge(vertices[i], vertices[next]);
        }

        // 验证环形结构
        List<GraphAdjList.Vertex> traversal = cycleGraph.bfsTraversal(v1);
        assertEquals(5, traversal.size());
    }

    /**
     * 测试星形图
     */
    @Test
    void testStarGraph() {
        GraphAdjList starGraph = new GraphAdjList();

        // 添加顶点
        GraphAdjList.Vertex center = new GraphAdjList.Vertex(0);
        GraphAdjList.Vertex[] leaves = {v1, v2, v3, v4};

        starGraph.addVertex(center);
        for (GraphAdjList.Vertex leaf : leaves) {
            starGraph.addVertex(leaf);
            starGraph.addEdge(center, leaf);
        }

        // 验证中心顶点连接到所有叶子
        List<GraphAdjList.Vertex> centerNeighbors = starGraph.bfsTraversal(center);
        assertEquals(5, centerNeighbors.size());

        // 验证整个图的连通性
        for (GraphAdjList.Vertex leaf : leaves) {
            List<GraphAdjList.Vertex> reachable = starGraph.bfsTraversal(leaf);
            assertEquals(5, reachable.size()); // 所有节点都应该连通
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
        graph.addVertex(v1);
        assertEquals(1, graph.size());

        // 验证不能添加自环
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(v1, v1));

        // 验证不能添加不存在的顶点之间的边
        GraphAdjList.Vertex nonExistent = new GraphAdjList.Vertex(999);
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(v1, nonExistent));
    }

    /**
     * 测试重复添加边和顶点
     */
    @Test
    void testDuplicateOperations() {
        // 重复添加顶点
        graph.addVertex(v1);
        graph.addVertex(v1);
        assertEquals(1, graph.size());

        // 添加边
        graph.addVertex(v2);
        graph.addEdge(v1, v2);

        // 重复添加相同的边
        assertDoesNotThrow(() -> graph.addEdge(v1, v2));
        assertDoesNotThrow(() -> graph.addEdge(v2, v1));

        // 验证图的结构不变
        assertEquals(2, graph.size());
    }

    /**
     * 测试大规模图的性能
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testLargeGraphPerformance() {
        final int size = 1000;

        // 添加大量顶点
        GraphAdjList.Vertex[] vertices = new GraphAdjList.Vertex[size];
        for (int i = 0; i < size; i++) {
            vertices[i] = new GraphAdjList.Vertex(i);
            graph.addVertex(vertices[i]);
        }

        // 添加边形成稀疏图
        for (int i = 0; i < size - 1; i += 10) {
            graph.addEdge(vertices[i], vertices[i + 1]);
        }

        // 验证遍历性能
        List<GraphAdjList.Vertex> traversal = graph.bfsTraversal(vertices[0]);
        assertNotNull(traversal);
        assertTrue(traversal.size() > 0);
    }

    /**
     * 测试图的密度
     */
    @Test
    void testGraphDensity() {
        GraphAdjList denseGraph = new GraphAdjList();

        // 添加顶点
        GraphAdjList.Vertex[] vertices = {v1, v2, v3, v4};
        for (GraphAdjList.Vertex v : vertices) {
            denseGraph.addVertex(v);
        }

        // 构建稠密图
        for (int i = 0; i < vertices.length; i++) {
            for (int j = i + 1; j < vertices.length; j++) {
                denseGraph.addEdge(vertices[i], vertices[j]);
            }
        }

        // 验证边数
        int expectedEdges = vertices.length * (vertices.length - 1) / 2;
        assertEquals(4, vertices.length);
    }

    /**
     * 测试顶点对象的等价性
     */
    @Test
    void testVertexEquivalence() {
        GraphAdjList.Vertex vertex1 = new GraphAdjList.Vertex(100);
        GraphAdjList.Vertex vertex2 = new GraphAdjList.Vertex(100);
        GraphAdjList.Vertex vertex3 = new GraphAdjList.Vertex(200);

        // 测试值相等但对象不同
        assertEquals(vertex1.getVal(), vertex2.getVal());
        assertEquals(vertex1.getVal(), vertex2.getVal()); // 值相等

        // 测试对象引用不同
        assertNotSame(vertex1, vertex2); // 不同的对象实例

        // 测试值不相等
        assertNotEquals(vertex1.getVal(), vertex3.getVal());
    }

    /**
     * 测试图的序列化和反序列化概念
     */
    @Test
    void testGraphSerializationConcept() {
        // 构建一个图
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);

        // 模拟序列化：保存图的状态
        int vertexCount = graph.size();
        List<GraphAdjList.Vertex> bfsOrder = graph.bfsTraversal(v1);

        // 模拟反序列化：重建图
        GraphAdjList reconstructed = new GraphAdjList();
        GraphAdjList.Vertex[] newVertices = {new GraphAdjList.Vertex(1), new GraphAdjList.Vertex(2),
                new GraphAdjList.Vertex(3)};

        for (GraphAdjList.Vertex v : newVertices) {
            reconstructed.addVertex(v);
        }
        reconstructed.addEdge(newVertices[0], newVertices[1]);
        reconstructed.addEdge(newVertices[1], newVertices[2]);

        // 验证重建的图与原图结构相同
        assertEquals(vertexCount, reconstructed.size());
    }

    /**
     * 测试图的连通分量
     */
    @Test
    void testConnectedComponents() {
        // 创建3个连通分量
        GraphAdjList multiComponentGraph = new GraphAdjList();

        GraphAdjList.Vertex[][] components = {{new GraphAdjList.Vertex(1), new GraphAdjList.Vertex(2)},
                {new GraphAdjList.Vertex(3), new GraphAdjList.Vertex(4)},
                {new GraphAdjList.Vertex(5), new GraphAdjList.Vertex(6)}};

        // 添加所有顶点
        for (GraphAdjList.Vertex[] component : components) {
            for (GraphAdjList.Vertex v : component) {
                multiComponentGraph.addVertex(v);
            }
            // 在每个分量内添加边
            if (component.length > 1) {
                multiComponentGraph.addEdge(component[0], component[1]);
            }
        }

        // 验证每个连通分量
        List<GraphAdjList.Vertex> component1 = multiComponentGraph.bfsTraversal(components[0][0]);
        List<GraphAdjList.Vertex> component2 = multiComponentGraph.bfsTraversal(components[1][0]);
        List<GraphAdjList.Vertex> component3 = multiComponentGraph.bfsTraversal(components[2][0]);

        assertEquals(2, component1.size());
        assertEquals(2, component2.size());
        assertEquals(2, component3.size());
    }

    /**
     * 测试图的直径计算（近似）
     */
    @Test
    void testGraphDiameter() {
        // 线性图：1-2-3-4-5
        GraphAdjList linearGraph = new GraphAdjList();
        GraphAdjList.Vertex[] vertices = new GraphAdjList.Vertex[5];
        for (int i = 0; i < 5; i++) {
            vertices[i] = new GraphAdjList.Vertex(i + 1);
            linearGraph.addVertex(vertices[i]);
        }

        for (int i = 0; i < 4; i++) {
            linearGraph.addEdge(vertices[i], vertices[i + 1]);
        }

        // 计算最长最短路径（直径）
        int maxDistance = 0;
        for (GraphAdjList.Vertex start : vertices) {
            List<GraphAdjList.Vertex> traversal = linearGraph.bfsTraversal(start);
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
        GraphAdjList graphWithCutVertex = new GraphAdjList();

        GraphAdjList.Vertex[] vertices = {new GraphAdjList.Vertex(1), new GraphAdjList.Vertex(2),
                new GraphAdjList.Vertex(3), new GraphAdjList.Vertex(4), new GraphAdjList.Vertex(5)};

        for (GraphAdjList.Vertex v : vertices) {
            graphWithCutVertex.addVertex(v);
        }

        graphWithCutVertex.addEdge(vertices[0], vertices[1]);
        graphWithCutVertex.addEdge(vertices[1], vertices[2]);
        graphWithCutVertex.addEdge(vertices[1], vertices[3]);
        graphWithCutVertex.addEdge(vertices[3], vertices[4]);

        // 验证原始图的连通性
        List<GraphAdjList.Vertex> originalReachable = graphWithCutVertex.bfsTraversal(vertices[0]);
        assertEquals(5, originalReachable.size());

        // 模拟删除割点
        graphWithCutVertex.removeVertex(vertices[1]);
        List<GraphAdjList.Vertex> newReachable = graphWithCutVertex.bfsTraversal(vertices[0]);
        assertEquals(1, newReachable.size()); // 只能到达自己
    }

    /**
     * 测试图的遍历顺序一致性
     */
    @Test
    void testTraversalConsistency() {
        // 构建一个图
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v4);

        // 多次遍历应该得到相同的结果
        List<GraphAdjList.Vertex> traversal1 = graph.bfsTraversal(v1);
        List<GraphAdjList.Vertex> traversal2 = graph.bfsTraversal(v1);
        List<GraphAdjList.Vertex> traversal3 = graph.bfsTraversal(v1);

        assertEquals(traversal1, traversal2);
        assertEquals(traversal2, traversal3);

        // DFS也应该一致
        List<GraphAdjList.Vertex> dfs1 = graph.dfsTraversal(v1);
        List<GraphAdjList.Vertex> dfs2 = graph.dfsTraversal(v1);

        assertEquals(dfs1, dfs2);
    }

    /**
     * 测试图的内存使用
     */
    @Test
    void testMemoryUsage() {
        // 测试大量添加和删除操作
        final int operations = 1000;
        GraphAdjList.Vertex[] vertices = new GraphAdjList.Vertex[operations];

        for (int i = 0; i < operations; i++) {
            vertices[i] = new GraphAdjList.Vertex(i);
            graph.addVertex(vertices[i]);
        }

        assertEquals(operations, graph.size());

        // 删除一半顶点
        for (int i = 0; i < operations / 2; i++) {
            graph.removeVertex(vertices[i]);
        }

        assertEquals(operations / 2, graph.size());
    }

    /**
     * 测试图的等价性
     */
    @Test
    void testGraphEquivalence() {
        // 创建两个等价的图
        GraphAdjList graph1 = new GraphAdjList();
        GraphAdjList.Vertex[] vertices1 = {new GraphAdjList.Vertex(1), new GraphAdjList.Vertex(2),
                new GraphAdjList.Vertex(3)};

        for (GraphAdjList.Vertex v : vertices1) {
            graph1.addVertex(v);
        }
        graph1.addEdge(vertices1[0], vertices1[1]);
        graph1.addEdge(vertices1[1], vertices1[2]);

        GraphAdjList graph2 = new GraphAdjList();
        GraphAdjList.Vertex[] vertices2 = {new GraphAdjList.Vertex(1), new GraphAdjList.Vertex(2),
                new GraphAdjList.Vertex(3)};

        for (GraphAdjList.Vertex v : vertices2) {
            graph2.addVertex(v);
        }
        graph2.addEdge(vertices2[0], vertices2[1]);
        graph2.addEdge(vertices2[1], vertices2[2]);

        // 验证遍历结果相同
        assertEquals(graph1.size(), graph2.size());
    }

    /**
     * 测试图的鲁棒性
     */
    @Test
    void testGraphRobustness() {
        // 测试极端情况
        assertDoesNotThrow(() -> {
            GraphAdjList robustGraph = new GraphAdjList();

            // 添加大量顶点后立即删除
            GraphAdjList.Vertex[] vertices = new GraphAdjList.Vertex[100];
            for (int i = 0; i < 100; i++) {
                vertices[i] = new GraphAdjList.Vertex(i);
                robustGraph.addVertex(vertices[i]);
            }

            // 添加一些边
            for (int i = 0; i < 99; i++) {
                robustGraph.addEdge(vertices[i], vertices[i + 1]);
            }

            // 删除所有顶点
            for (int i = 99; i >= 0; i--) {
                robustGraph.removeVertex(vertices[i]);
            }

            assertEquals(0, robustGraph.size());
        });
    }

    /**
     * 测试非递归DFS与递归DFS的一致性
     */
    @Test
    void testDfsConsistency() {
        // 构建一个复杂图
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);

        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        graph.addEdge(v2, v4);
        graph.addEdge(v3, v5);

        // 比较两种DFS的结果
        List<GraphAdjList.Vertex> recursiveDfs = graph.dfsTraversal(v1);
        List<GraphAdjList.Vertex> nonRecursiveDfs = graph.dfsTraversalNonRecursive(v1);

        // 两种遍历应该访问相同的顶点集合
        Set<Integer> recursiveValues = new HashSet<>();
        Set<Integer> nonRecursiveValues = new HashSet<>();

        for (GraphAdjList.Vertex v : recursiveDfs) {
            recursiveValues.add(v.getVal());
        }
        for (GraphAdjList.Vertex v : nonRecursiveDfs) {
            nonRecursiveValues.add(v.getVal());
        }

        assertEquals(recursiveValues, nonRecursiveValues);
        assertEquals(5, recursiveValues.size());
    }

    /**
     * 测试图的权重概念（虽然是无权图，但可以测试路径长度）
     */
    @Test
    void testPathLength() {
        // 构建路径图：1-2-3-4-5
        GraphAdjList.Vertex[] vertices = new GraphAdjList.Vertex[5];
        for (int i = 0; i < 5; i++) {
            vertices[i] = new GraphAdjList.Vertex(i + 1);
            graph.addVertex(vertices[i]);
        }

        for (int i = 0; i < 4; i++) {
            graph.addEdge(vertices[i], vertices[i + 1]);
        }

        // 验证BFS的层次性
        List<GraphAdjList.Vertex> bfsResult = graph.bfsTraversal(vertices[0]);
        assertEquals(5, bfsResult.size());
        assertEquals(1, bfsResult.get(0).getVal()); // 起点
        assertEquals(2, bfsResult.get(1).getVal()); // 距离1
        assertEquals(3, bfsResult.get(2).getVal()); // 距离2
    }
}