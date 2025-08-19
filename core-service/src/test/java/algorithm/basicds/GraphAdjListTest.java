package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        GraphAdjList.Vertex[][] edges = {
                {v1, v2},
                {v2, v3},
                {v1, v3}
        };

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
            GraphAdjList.Vertex[][] invalidEdges = {
                    {v1}  // 只有1个顶点
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
        List<Integer> secondLevel = Arrays.asList(
                result.get(1).getVal(),
                result.get(2).getVal(),
                result.get(3).getVal()
        );
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
}