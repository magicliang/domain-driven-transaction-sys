package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @BeforeEach
    void setUp() {
        graph = new GraphAdjList();
        v1 = new GraphAdjList.Vertex(1);
        v2 = new GraphAdjList.Vertex(2);
        v3 = new GraphAdjList.Vertex(3);
        v4 = new GraphAdjList.Vertex(4);
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
}