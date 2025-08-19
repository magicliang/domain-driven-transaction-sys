package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 图数据结构的边界条件和异常测试类
 * 专门测试各种边界情况和异常处理
 */
class GraphBoundaryTest {

    private GraphAdjMat adjMatGraph;
    private GraphAdjList adjListGraph;

    @BeforeEach
    void setUp() {
        adjMatGraph = new GraphAdjMat();
        adjListGraph = new GraphAdjList();
    }

    // ===== 空图边界测试 =====

    @Test
    @DisplayName("空图的所有操作边界测试")
    void testEmptyGraphBoundaries() {
        // 邻接矩阵空图
        assertEquals(0, adjMatGraph.size());

        // 所有遍历操作应该抛出异常
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.bfsTraversal(0));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.bfsTraversal(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.bfsTraversal(100));

        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.dfsTraversal(0));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.dfsTraversal(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.dfsTraversal(100));

        // 边操作应该抛出异常
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(0, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(0, 1));

        // 顶点删除应该抛出异常
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.removeVertex(0));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.removeVertex(-1));

        // 邻接表空图
        assertEquals(0, adjListGraph.size());

        GraphAdjList.Vertex dummy = new GraphAdjList.Vertex(0);
        assertThrows(IllegalArgumentException.class, () -> adjListGraph.bfsTraversal(dummy));
        assertThrows(IllegalArgumentException.class, () -> adjListGraph.dfsTraversal(dummy));
        assertThrows(IllegalArgumentException.class, () -> adjListGraph.removeVertex(dummy));
    }

    // ===== 单顶点图边界测试 =====

    @Test
    @DisplayName("单顶点图的所有边界操作")
    void testSingleVertexGraphBoundaries() {
        // 邻接矩阵
        adjMatGraph.addVertex(42);
        assertEquals(1, adjMatGraph.size());

        // 有效操作
        assertDoesNotThrow(() -> adjMatGraph.bfsTraversal(0));
        assertDoesNotThrow(() -> adjMatGraph.dfsTraversal(0));

        // 无效索引操作
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.bfsTraversal(1));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.bfsTraversal(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(0, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(1, 0));

        // 自环测试
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(0, 0));

        // 邻接表
        GraphAdjList.Vertex singleVertex = new GraphAdjList.Vertex(42);
        adjListGraph.addVertex(singleVertex);
        assertEquals(1, adjListGraph.size());

        // 有效操作
        assertDoesNotThrow(() -> adjListGraph.bfsTraversal(singleVertex));
        assertDoesNotThrow(() -> adjListGraph.dfsTraversal(singleVertex));

        // 无效顶点操作
        GraphAdjList.Vertex invalidVertex = new GraphAdjList.Vertex(999);
        assertThrows(IllegalArgumentException.class, () -> adjListGraph.addEdge(singleVertex, invalidVertex));
        assertThrows(IllegalArgumentException.class, () -> adjListGraph.removeVertex(invalidVertex));

        // 自环测试
        assertThrows(IllegalArgumentException.class, () -> adjListGraph.addEdge(singleVertex, singleVertex));
    }

    // ===== 索引边界测试 =====

    @Test
    @DisplayName("测试极端索引值")
    void testExtremeIndexValues() {
        // 邻接矩阵
        adjMatGraph.addVertex(1);
        adjMatGraph.addVertex(2);

        // 测试Integer边界值
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(Integer.MAX_VALUE, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(Integer.MIN_VALUE, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(0, Integer.MAX_VALUE));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(0, Integer.MIN_VALUE));

        // 测试负数索引
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.removeVertex(-1));

        // 测试超出范围的索引
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(0, 100));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(100, 0));
    }

    // ===== 重复操作边界测试 =====

    @Test
    @DisplayName("测试重复添加和删除操作")
    void testDuplicateOperations() {
        // 邻接矩阵
        adjMatGraph.addVertex(1);
        adjMatGraph.addVertex(2);

        // 重复添加相同的边
        assertDoesNotThrow(() -> adjMatGraph.addEdge(0, 1));
        assertDoesNotThrow(() -> adjMatGraph.addEdge(0, 1));
        assertDoesNotThrow(() -> adjMatGraph.addEdge(1, 0));

        // 重复删除相同的边
        assertDoesNotThrow(() -> adjMatGraph.removeEdge(0, 1));
        assertDoesNotThrow(() -> adjMatGraph.removeEdge(0, 1));

        // 重复删除顶点
        adjMatGraph.addVertex(3);
        assertDoesNotThrow(() -> adjMatGraph.removeVertex(2));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.removeVertex(2));

        // 邻接表
        GraphAdjList.Vertex v1 = new GraphAdjList.Vertex(1);
        GraphAdjList.Vertex v2 = new GraphAdjList.Vertex(2);
        adjListGraph.addVertex(v1);
        adjListGraph.addVertex(v2);

        // 重复添加相同的边
        assertDoesNotThrow(() -> adjListGraph.addEdge(v1, v2));
        assertDoesNotThrow(() -> adjListGraph.addEdge(v1, v2));

        // 重复删除相同的边
        assertDoesNotThrow(() -> adjListGraph.removeEdge(v1, v2));
        assertDoesNotThrow(() -> adjListGraph.removeEdge(v1, v2));

        // 重复删除顶点
        GraphAdjList.Vertex v3 = new GraphAdjList.Vertex(3);
        adjListGraph.addVertex(v3);
        assertDoesNotThrow(() -> adjListGraph.removeVertex(v3));
        assertThrows(IllegalArgumentException.class, () -> adjListGraph.removeVertex(v3));
    }

    // ===== 并发修改边界测试 =====

    @Test
    @DisplayName("测试快速添加删除序列")
    void testRapidAddRemoveSequence() {
        // 测试快速添加和删除顶点
        for (int i = 0; i < 1000; i++) {
            adjMatGraph.addVertex(i);
            if (i > 0) {
                adjMatGraph.addEdge(i - 1, i);
            }
        }

        assertEquals(1000, adjMatGraph.size());

        // 快速删除顶点
        for (int i = 999; i >= 0; i--) {
            adjMatGraph.removeVertex(i);
        }

        assertEquals(0, adjMatGraph.size());

        // 邻接表测试
        GraphAdjList.Vertex[] vertices = new GraphAdjList.Vertex[1000];
        for (int i = 0; i < 1000; i++) {
            vertices[i] = new GraphAdjList.Vertex(i);
            adjListGraph.addVertex(vertices[i]);
            if (i > 0) {
                adjListGraph.addEdge(vertices[i - 1], vertices[i]);
            }
        }

        assertEquals(1000, adjListGraph.size());

        // 快速删除顶点
        for (int i = 999; i >= 0; i--) {
            adjListGraph.removeVertex(vertices[i]);
        }

        assertEquals(0, adjListGraph.size());
    }

    // ===== 特殊值边界测试 =====

    @Test
    @DisplayName("测试特殊顶点值")
    void testSpecialVertexValues() {
        // 测试零值
        adjMatGraph.addVertex(0);
        assertEquals(1, adjMatGraph.size());

        // 测试负值
        adjMatGraph.addVertex(-1);
        adjMatGraph.addVertex(-100);
        assertEquals(3, adjMatGraph.size());

        // 测试大值
        adjMatGraph.addVertex(Integer.MAX_VALUE);
        adjMatGraph.addVertex(Integer.MIN_VALUE);
        assertEquals(5, adjMatGraph.size());

        // 邻接表
        GraphAdjList.Vertex v0 = new GraphAdjList.Vertex(0);
        GraphAdjList.Vertex vNeg = new GraphAdjList.Vertex(-1);
        GraphAdjList.Vertex vMax = new GraphAdjList.Vertex(Integer.MAX_VALUE);

        adjListGraph.addVertex(v0);
        adjListGraph.addVertex(vNeg);
        adjListGraph.addVertex(vMax);

        assertEquals(3, adjListGraph.size());

        // 测试这些特殊值之间的连接
        assertDoesNotThrow(() -> adjListGraph.addEdge(v0, vNeg));
        assertDoesNotThrow(() -> adjListGraph.addEdge(vNeg, vMax));
    }

    // ===== 内存边界测试 =====

    @Test
    @DisplayName("测试内存边界情况")
    void testMemoryBoundaries() {
        // 测试大量顶点后立即删除
        final int largeSize = 10000;

        // 邻接矩阵
        for (int i = 0; i < largeSize; i++) {
            adjMatGraph.addVertex(i);
        }
        assertEquals(largeSize, adjMatGraph.size());

        // 删除所有顶点
        for (int i = largeSize - 1; i >= 0; i--) {
            adjMatGraph.removeVertex(i);
        }
        assertEquals(0, adjMatGraph.size());

        // 邻接表
        GraphAdjList.Vertex[] vertices = new GraphAdjList.Vertex[largeSize];
        for (int i = 0; i < largeSize; i++) {
            vertices[i] = new GraphAdjList.Vertex(i);
            adjListGraph.addVertex(vertices[i]);
        }
        assertEquals(largeSize, adjListGraph.size());

        // 删除所有顶点
        for (int i = largeSize - 1; i >= 0; i--) {
            adjListGraph.removeVertex(vertices[i]);
        }
        assertEquals(0, adjListGraph.size());
    }

    // ===== 图结构完整性测试 =====

    @Test
    @DisplayName("测试图结构完整性")
    void testGraphStructureIntegrity() {
        // 构建复杂图后验证结构完整性
        int[] vertices = {1, 2, 3, 4, 5, 6};
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5},  // 主路径
                {0, 3}, {1, 4}, {2, 5}                    // 交叉边
        };

        GraphAdjMat complexGraph = new GraphAdjMat(vertices, edges);

        // 验证所有顶点可达
        Set<Integer> allVertices = new HashSet<>();
        for (int i = 0; i < vertices.length; i++) {
            allVertices.add(i);
        }

        List<Integer> reachable = complexGraph.bfsTraversal(0);
        assertEquals(vertices.length, reachable.size());
        assertTrue(new HashSet<>(reachable).containsAll(allVertices));

        // 验证删除顶点后结构完整性
        complexGraph.removeVertex(2);
        reachable = complexGraph.bfsTraversal(0);
        assertEquals(vertices.length - 1, reachable.size());
    }

    // ===== 错误恢复测试 =====

    @Test
    @DisplayName("测试错误恢复能力")
    void testErrorRecovery() {
        // 测试在异常后继续正常操作
        adjMatGraph.addVertex(1);

        // 触发异常
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(0, 1));

        // 验证图仍然可用
        assertDoesNotThrow(() -> adjMatGraph.addVertex(2));
        assertDoesNotThrow(() -> adjMatGraph.addEdge(0, 1));
        assertEquals(2, adjMatGraph.size());

        // 邻接表
        GraphAdjList.Vertex v1 = new GraphAdjList.Vertex(1);
        adjListGraph.addVertex(v1);

        // 触发异常
        GraphAdjList.Vertex invalid = new GraphAdjList.Vertex(999);
        assertThrows(IllegalArgumentException.class, () -> adjListGraph.addEdge(v1, invalid));

        // 验证图仍然可用
        GraphAdjList.Vertex v2 = new GraphAdjList.Vertex(2);
        adjListGraph.addVertex(v2);
        assertDoesNotThrow(() -> adjListGraph.addEdge(v1, v2));
        assertEquals(2, adjListGraph.size());
    }
}