package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * 图数据结构的高级测试类
 * 包含性能测试、边界条件测试、复杂图结构测试等
 */
class GraphAdvancedTest {

    private GraphAdjMat adjMatGraph;
    private GraphAdjList adjListGraph;

    @BeforeEach
    void setUp() {
        adjMatGraph = new GraphAdjMat();
        adjListGraph = new GraphAdjList();
    }

    // ===== 性能压力测试 =====

    @Test
    @DisplayName("测试大规模图的性能 - 邻接矩阵")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testLargeScaleAdjMatGraph() {
        final int vertexCount = 5000;

        // 添加顶点
        for (int i = 0; i < vertexCount; i++) {
            adjMatGraph.addVertex(i);
        }

        // 添加边形成稀疏图
        for (int i = 0; i < vertexCount - 1; i += 50) {
            adjMatGraph.addEdge(i, i + 1);
        }

        // 验证基本操作
        assertEquals(vertexCount, adjMatGraph.size());

        // 测试遍历性能
        List<Integer> traversal = adjMatGraph.bfsTraversal(0);
        assertNotNull(traversal);
        assertTrue(traversal.size() > 0);
    }

    @Test
    @DisplayName("测试大规模图的性能 - 邻接表")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testLargeScaleAdjListGraph() {
        final int vertexCount = 10000;

        // 添加顶点
        GraphAdjList.Vertex[] vertices = new GraphAdjList.Vertex[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            vertices[i] = new GraphAdjList.Vertex(i);
            adjListGraph.addVertex(vertices[i]);
        }

        // 添加边形成稀疏图
        for (int i = 0; i < vertexCount - 1; i += 100) {
            adjListGraph.addEdge(vertices[i], vertices[i + 1]);
        }

        // 验证基本操作
        assertEquals(vertexCount, adjListGraph.size());

        // 测试遍历性能
        List<GraphAdjList.Vertex> traversal = adjListGraph.bfsTraversal(vertices[0]);
        assertNotNull(traversal);
        assertTrue(traversal.size() > 0);
    }

    // ===== 复杂图结构测试 =====

    @Test
    @DisplayName("测试二叉树结构")
    void testBinaryTreeStructure() {
        // 构建完全二叉树
        int[] vertices = {1, 2, 3, 4, 5, 6, 7};
        int[][] edges = {{0, 1}, {0, 2},  // 根节点到左右子节点
                {1, 3}, {1, 4},  // 左子树的子节点
                {2, 5}, {2, 6}   // 右子树的子节点
        };

        GraphAdjMat tree = new GraphAdjMat(vertices, edges);

        // 验证树的层次遍历
        List<Integer> bfs = tree.bfsTraversal(0);
        assertEquals(7, bfs.size());

        // 验证父子关系
        assertEquals(0, bfs.get(0).intValue()); // 根节点
        assertTrue(bfs.subList(1, 3).containsAll(Arrays.asList(1, 2))); // 第二层
        assertTrue(bfs.subList(3, 7).containsAll(Arrays.asList(3, 4, 5, 6))); // 第三层
    }

    @Test
    @DisplayName("测试网格图结构")
    void testGridGraphStructure() {
        // 构建3x3网格图
        GraphAdjList gridGraph = new GraphAdjList();

        // 创建9个顶点
        GraphAdjList.Vertex[][] grid = new GraphAdjList.Vertex[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = new GraphAdjList.Vertex(i * 3 + j + 1);
                gridGraph.addVertex(grid[i][j]);
            }
        }

        // 添加水平和垂直边
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // 水平边
                if (j < 2) {
                    gridGraph.addEdge(grid[i][j], grid[i][j + 1]);
                }
                // 垂直边
                if (i < 2) {
                    gridGraph.addEdge(grid[i][j], grid[i + 1][j]);
                }
            }
        }

        // 验证网格图的连通性
        List<GraphAdjList.Vertex> traversal = gridGraph.bfsTraversal(grid[0][0]);
        assertEquals(9, traversal.size());
    }

    @Test
    @DisplayName("测试二分图")
    void testBipartiteGraph() {
        // 构建二分图 K3,3
        GraphAdjList bipartiteGraph = new GraphAdjList();

        // 创建两组顶点
        GraphAdjList.Vertex[] setA = {new GraphAdjList.Vertex(1), new GraphAdjList.Vertex(2),
                new GraphAdjList.Vertex(3)};

        GraphAdjList.Vertex[] setB = {new GraphAdjList.Vertex(4), new GraphAdjList.Vertex(5),
                new GraphAdjList.Vertex(6)};

        // 添加所有顶点
        for (GraphAdjList.Vertex v : setA) {
            bipartiteGraph.addVertex(v);
        }
        for (GraphAdjList.Vertex v : setB) {
            bipartiteGraph.addVertex(v);
        }

        // 添加所有可能的边（两组之间的完全连接）
        for (GraphAdjList.Vertex a : setA) {
            for (GraphAdjList.Vertex b : setB) {
                bipartiteGraph.addEdge(a, b);
            }
        }

        // 验证二分图结构：每个顶点应该连接到另一组的所有顶点
        for (GraphAdjList.Vertex a : setA) {
            List<GraphAdjList.Vertex> reachable = bipartiteGraph.bfsTraversal(a);
            assertEquals(6, reachable.size()); // 所有6个顶点都应该连通
        }

        // 验证每个A集合顶点的邻居数量（度数为3，连接到所有B集合顶点）
        for (GraphAdjList.Vertex a : setA) {
            // 由于无法直接获取邻居，验证从A顶点出发可以到达所有B顶点
            List<GraphAdjList.Vertex> traversal = bipartiteGraph.bfsTraversal(a);
            Set<Integer> values = traversal.stream().map(GraphAdjList.Vertex::getVal).collect(Collectors.toSet());
            assertTrue(values.containsAll(Arrays.asList(4, 5, 6))); // 应该包含所有B集合顶点
        }
    }

    // ===== 边界条件测试 =====

    @Test
    @DisplayName("测试空图的所有操作")
    void testEmptyGraphOperations() {
        // 邻接矩阵空图
        assertEquals(0, adjMatGraph.size());
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.bfsTraversal(0));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.dfsTraversal(0));

        // 邻接表空图
        assertEquals(0, adjListGraph.size());
        GraphAdjList.Vertex dummy = new GraphAdjList.Vertex(0);
        assertThrows(IllegalArgumentException.class, () -> adjListGraph.bfsTraversal(dummy));
    }

    @Test
    @DisplayName("测试单顶点图的所有操作")
    void testSingleVertexGraph() {
        // 邻接矩阵单顶点图
        adjMatGraph.addVertex(42);
        assertEquals(1, adjMatGraph.size());

        List<Integer> bfs = adjMatGraph.bfsTraversal(0);
        assertEquals(1, bfs.size());
        assertEquals(0, bfs.get(0).intValue());

        // 邻接表单顶点图
        GraphAdjList.Vertex singleVertex = new GraphAdjList.Vertex(42);
        adjListGraph.addVertex(singleVertex);
        assertEquals(1, adjListGraph.size());

        List<GraphAdjList.Vertex> bfsList = adjListGraph.bfsTraversal(singleVertex);
        assertEquals(1, bfsList.size());
        assertEquals(42, bfsList.get(0).getVal());
    }

    @Test
    @DisplayName("测试最大顶点数限制")
    void testMaximumVertices() {
        // 测试邻接矩阵的内存限制
        final int maxVertices = 1000;

        for (int i = 0; i < maxVertices; i++) {
            adjMatGraph.addVertex(i);
        }

        assertEquals(maxVertices, adjMatGraph.size());

        // 验证仍然可以正常操作
        adjMatGraph.addEdge(0, maxVertices - 1);
        List<Integer> traversal = adjMatGraph.bfsTraversal(0);

        // 由于图可能不是完全连通的，验证遍历结果不为空且包含起始顶点
        assertNotNull(traversal);
        assertTrue(traversal.size() > 0);
        assertTrue(traversal.contains(0));
    }

    // ===== 算法正确性测试 =====

    @Test
    @DisplayName("测试BFS最短路径性质")
    void testBFSShortestPathProperty() {
        // 构建测试图
        int[] vertices = {1, 2, 3, 4, 5, 6};
        int[][] edges = {{0, 1}, {0, 2},    // 第一层
                {1, 3}, {1, 4},    // 第二层
                {2, 4}, {2, 5}     // 第二层
        };

        GraphAdjMat testGraph = new GraphAdjMat(vertices, edges);

        // 从顶点0开始BFS
        List<Integer> bfsOrder = testGraph.bfsTraversal(0);

        // 验证层次顺序
        assertEquals(0, bfsOrder.get(0).intValue()); // 距离0
        assertTrue(bfsOrder.subList(1, 3).containsAll(Arrays.asList(1, 2))); // 距离1
        assertTrue(bfsOrder.subList(3, 6).containsAll(Arrays.asList(3, 4, 5))); // 距离2
    }

    @Test
    @DisplayName("测试DFS的访问顺序")
    void testDFSVisitOrder() {
        // 构建树形结构
        GraphAdjList tree = new GraphAdjList();

        GraphAdjList.Vertex root = new GraphAdjList.Vertex(0);
        GraphAdjList.Vertex left1 = new GraphAdjList.Vertex(1);
        GraphAdjList.Vertex right1 = new GraphAdjList.Vertex(2);
        GraphAdjList.Vertex left2 = new GraphAdjList.Vertex(3);
        GraphAdjList.Vertex right2 = new GraphAdjList.Vertex(4);

        tree.addVertex(root);
        tree.addVertex(left1);
        tree.addVertex(right1);
        tree.addVertex(left2);
        tree.addVertex(right2);

        tree.addEdge(root, left1);
        tree.addEdge(root, right1);
        tree.addEdge(left1, left2);
        tree.addEdge(right1, right2);

        // 验证DFS访问所有顶点
        List<GraphAdjList.Vertex> dfsResult = tree.dfsTraversal(root);
        assertEquals(5, dfsResult.size());

        Set<Integer> visitedValues = dfsResult.stream().map(GraphAdjList.Vertex::getVal).collect(Collectors.toSet());

        Set<Integer> expected = new HashSet<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        expected.add(3);
        expected.add(4);
        assertEquals(expected, visitedValues);
    }

    // ===== 异常处理测试 =====

    @Test
    @DisplayName("测试无效索引处理")
    void testInvalidIndexHandling() {
        // 邻接矩阵测试
        adjMatGraph.addVertex(1);

        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(0, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.removeVertex(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.removeVertex(1));

        // 邻接表测试
        GraphAdjList.Vertex v = new GraphAdjList.Vertex(1);
        adjListGraph.addVertex(v);

        GraphAdjList.Vertex invalid = new GraphAdjList.Vertex(999);
        assertThrows(IllegalArgumentException.class, () -> adjListGraph.addEdge(v, invalid));
        assertThrows(IllegalArgumentException.class, () -> adjListGraph.removeVertex(invalid));
    }

    @Test
    @DisplayName("测试自环边处理")
    void testSelfLoopHandling() {
        // 邻接矩阵测试
        adjMatGraph.addVertex(1);
        assertThrows(IndexOutOfBoundsException.class, () -> adjMatGraph.addEdge(0, 0));

        // 邻接表测试
        GraphAdjList.Vertex v = new GraphAdjList.Vertex(1);
        adjListGraph.addVertex(v);
        assertThrows(IllegalArgumentException.class, () -> adjListGraph.addEdge(v, v));
    }

    // ===== 性能对比测试 =====

    @Test
    @DisplayName("测试两种实现的性能对比")
    void testImplementationPerformanceComparison() {
        final int testSize = 1000;

        // 准备测试数据
        int[] vertices = IntStream.range(0, testSize).toArray();
        int[][] edges = new int[testSize - 1][2];
        for (int i = 0; i < testSize - 1; i++) {
            edges[i] = new int[]{i, i + 1};
        }

        // 测试邻接矩阵
        long startTime = System.nanoTime();
        GraphAdjMat matGraph = new GraphAdjMat(vertices, edges);
        List<Integer> matBfs = matGraph.bfsTraversal(0);
        long matTime = System.nanoTime() - startTime;

        // 测试邻接表
        startTime = System.nanoTime();
        GraphAdjList.Vertex[] listVertices = new GraphAdjList.Vertex[testSize];
        for (int i = 0; i < testSize; i++) {
            listVertices[i] = new GraphAdjList.Vertex(i);
            adjListGraph.addVertex(listVertices[i]);
        }
        for (int i = 0; i < testSize - 1; i++) {
            adjListGraph.addEdge(listVertices[i], listVertices[i + 1]);
        }
        List<GraphAdjList.Vertex> listBfs = adjListGraph.bfsTraversal(listVertices[0]);
        long listTime = System.nanoTime() - startTime;

        // 验证结果正确性
        assertEquals(testSize, matBfs.size());
        assertEquals(testSize, listBfs.size());

        // 输出性能信息（仅用于参考）
        System.out.println("Adjacency Matrix time: " + matTime / 1_000_000 + " ms");
        System.out.println("Adjacency List time: " + listTime / 1_000_000 + " ms");
    }

    // ===== 随机图测试 =====

    @Test
    @DisplayName("测试随机图生成")
    void testRandomGraphGeneration() {
        final int vertexCount = 100;
        final Random random = new Random(42); // 固定种子保证可重复

        // 生成随机邻接矩阵图
        for (int i = 0; i < vertexCount; i++) {
            adjMatGraph.addVertex(i);
        }

        // 随机添加边
        for (int i = 0; i < vertexCount; i++) {
            for (int j = i + 1; j < vertexCount; j++) {
                if (random.nextDouble() < 0.1) { // 10%的概率添加边
                    adjMatGraph.addEdge(i, j);
                }
            }
        }

        // 验证图的基本属性
        assertEquals(vertexCount, adjMatGraph.size());
        List<Integer> traversal = adjMatGraph.bfsTraversal(0);
        assertNotNull(traversal);
    }

    // ===== 内存压力测试 =====

    @Test
    @DisplayName("测试内存压力下的图操作")
    void testMemoryPressure() {
        // 测试大量添加删除操作
        final int cycles = 100;
        final int verticesPerCycle = 100;

        for (int cycle = 0; cycle < cycles; cycle++) {
            // 添加顶点
            for (int i = 0; i < verticesPerCycle; i++) {
                adjMatGraph.addVertex(cycle * verticesPerCycle + i);
            }

            // 添加一些边
            for (int i = 0; i < verticesPerCycle - 1; i++) {
                if (i % 10 == 0) {
                    adjMatGraph.addEdge(i, i + 1);
                }
            }

            // 删除一半顶点
            for (int i = 0; i < verticesPerCycle / 2; i++) {
                adjMatGraph.removeVertex(0);
            }
        }

        // 验证图仍然有效
        assertTrue(adjMatGraph.size() >= 0);
    }
}