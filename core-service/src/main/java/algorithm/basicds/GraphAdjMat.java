package algorithm.basicds;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 基于邻接矩阵的图实现
 * 该类提供了使用邻接矩阵表示无向图的基本操作，包括顶点和边的增删改查
 *
 * @author magicliang
 * @date 2025-08-19 16:44
 */
public class GraphAdjMat {

    /**
     * 存储图中所有顶点的值
     * 顶点的索引对应其在邻接矩阵中的行/列索引
     */
    private List<Integer> vertices;

    /**
     * 邻接矩阵，表示图的连接关系
     * matrix[i][j] = 1 表示顶点i和顶点j之间有边
     * matrix[i][j] = 0 表示顶点i和顶点j之间没有边
     */
    private List<List<Integer>> matrix;

    /**
     * 默认构造函数
     * 创建一个空的图，包含0个顶点和0条边
     */
    public GraphAdjMat() {
        vertices = new ArrayList<>();
        matrix = new ArrayList<>();
    }

    /**
     * 带参数的构造函数
     * 根据给定的顶点和边创建图
     *
     * @param vertices 顶点值数组，数组索引将作为顶点在图中的索引
     * @param edges 边数组，每个元素是一个长度为2的数组，表示两个顶点之间的边
     *         edges[i][0]和edges[i][1]是顶点在vertices数组中的索引
     */
    public GraphAdjMat(int[] vertices, int[][] edges) {
        this();

        // 添加顶点
        for (int val : vertices) {
            addVertex(val);
        }

        // 添加边
        // 请注意，edges 元素代表顶点索引，即对应 vertices 元素索引
        for (int[] e : edges) {
            addEdge(e[0], e[1]);
        }
    }

    /**
     * 获取图中顶点的数量
     *
     * @return 图中顶点的总数
     */
    public int size() {
        return vertices.size();
    }

    /**
     * 向图中添加一个新顶点
     * 添加顶点时会自动扩展邻接矩阵，保持矩阵的方阵特性
     *
     * @param val 要添加的顶点值
     */
    public void addVertex(int val) {
        // 增加节点
        vertices.add(val);

        // 在矩阵里面增加一列每行末尾加0和一行（全0）
        for (List<Integer> row : matrix) {
            row.add(0);
        }
        final int n = vertices.size();
        List<Integer> newRow = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            newRow.add(0);
        }
        matrix.add(newRow);
    }

    /**
     * 从图中删除指定索引的顶点
     * 删除顶点时会同时删除与该顶点相关的所有边
     * 注意：此方法使用顶点索引进行删除，而不是顶点值
     *
     * @param index 要删除的顶点索引，必须在[0, size()-1]范围内
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    public void removeVertex(int index) {
        // 这个解如果是使用 index，就比较简单，如果尝试删除值，则要求图里的值唯一才能找到index，几乎不可能-也不实用

        // 增加节点
        vertices.remove(index);

        // 我们只知道一个点，是不能 removeEdge remove 全部的边的，当然我们可以用for循环来做这件事，但是我们也可以直接删除掉这些行和列-而不是用置 0 的
        // 直接删除索引行
        matrix.remove(index);

        // 把剩下的行里特定的index也删除掉，让 list 自己执行缩容操作
        for (List<Integer> row : matrix) {
            row.remove(index);
        }
    }

    /**
     * 在图中添加一条边
     * 由于是无向图，会在两个方向上都添加边
     *
     * @param i 第一个顶点的索引
     * @param j 第二个顶点的索引
     * @throws IndexOutOfBoundsException 如果任一索引超出范围或索引相等
     */
    public void addEdge(int i, int j) {
        if (i < 0 || j < 0 || i >= size() || j >= size() || i == j) {
            // 在平凡图里面，不允许 i、j 数组超标
            throw new IndexOutOfBoundsException();
        }

        matrix.get(i).set(j, 1);
        matrix.get(j).set(i, 1);
    }

    /**
     * 从图中删除一条边
     * 由于是无向图，会在两个方向上都删除边
     *
     * @param i 第一个顶点的索引
     * @param j 第二个顶点的索引
     * @throws IndexOutOfBoundsException 如果任一索引超出范围或索引相等
     */
    public void removeEdge(int i, int j) {
        if (i < 0 || j < 0 || i >= size() || j >= size() || i == j) {
            // 在平凡图里面，不允许 i、j 数组超标
            throw new IndexOutOfBoundsException();
        }

        matrix.get(i).set(j, 0);
        matrix.get(j).set(i, 0);
    }

    /**
     * 广度优先搜索遍历图
     * 从指定起始顶点开始进行BFS遍历，返回遍历顺序的顶点索引列表
     * 使用boolean数组记录已访问顶点避免重复遍历，使用队列实现层次遍历
     *
     * 时间复杂度：O(V²)
     * 证明：对于邻接矩阵，检查每个顶点的所有邻接顶点需要O(V)时间，共V个顶点
     *
     * 空间复杂度：O(V)
     * 证明：
     * 1. 队列(queue)最坏情况下存储整层的顶点，最多存储O(V)个顶点
     * 2. 数组(visited)存储所有已访问顶点的标记，需要O(V)空间
     * 3. 结果列表(result)存储所有顶点索引，存储O(V)个顶点
     * 4. 其他变量为常数空间O(1)
     * 因此总空间复杂度为O(V)
     *
     * @param startIndex 起始顶点的索引
     * @return 按BFS顺序排列的顶点索引列表
     * @throws IndexOutOfBoundsException 如果起始顶点索引不存在
     */
    public List<Integer> bfsTraversal(int startIndex) {
        if (startIndex < 0 || startIndex >= size()) {
            throw new IndexOutOfBoundsException("起始顶点索引超出范围");
        }

        boolean[] visited = new boolean[size()];
        Deque<Integer> queue = new LinkedList<>();
        queue.offer(startIndex);

        List<Integer> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (visited[current]) {
                continue;
            }

            result.add(current);
            visited[current] = true;

            // 遍历邻接矩阵的当前行，找到所有邻接顶点
            for (int i = 0; i < size(); i++) {
                if (matrix.get(current).get(i) == 1 && !visited[i]) {
                    queue.offer(i);
                }
            }
        }

        return result;
    }

    /**
     * 深度优先搜索遍历图
     * 从指定起始顶点开始进行DFS遍历，返回遍历顺序的顶点索引列表
     * 使用boolean数组记录已访问顶点避免重复遍历，使用递归实现深度优先探索
     *
     * 时间复杂度：O(V²)
     * 证明：对于邻接矩阵，检查每个顶点的所有邻接顶点需要O(V)时间，共V个顶点
     *
     * 空间复杂度：O(V)
     * 证明：
     * 1. 递归调用栈(call stack)最坏情况下存储从起点到最远叶子的路径，最多存储O(V)个顶点
     * 2. 数组(visited)存储所有已访问顶点的标记，需要O(V)空间
     * 3. 结果列表(result)存储所有顶点索引，存储O(V)个顶点
     * 4. 其他变量为常数空间O(1)
     * 因此总空间复杂度为O(V)
     *
     * @param startIndex 起始顶点的索引
     * @return 按DFS顺序排列的顶点索引列表
     * @throws IndexOutOfBoundsException 如果起始顶点索引不存在
     */
    public List<Integer> dfsTraversal(int startIndex) {
        if (startIndex < 0 || startIndex >= size()) {
            throw new IndexOutOfBoundsException("起始顶点索引超出范围");
        }

        boolean[] visited = new boolean[size()];
        List<Integer> result = new ArrayList<>();

        dfs(startIndex, visited, result);

        return result;
    }

    /**
     * DFS递归辅助方法
     * 从当前顶点开始进行深度优先搜索
     *
     * 空间复杂度：O(V) - 由递归调用栈深度决定，最坏情况下为图的高度
     *
     * @param current 当前访问的顶点索引
     * @param visited 记录已访问顶点的数组
     * @param result 存储遍历结果的列表
     */
    private void dfs(int current, boolean[] visited, List<Integer> result) {
        if (visited[current]) {
            return;
        }

        result.add(current);
        visited[current] = true;

        // 遍历邻接矩阵的当前行，找到所有邻接顶点
        for (int i = 0; i < size(); i++) {
            if (matrix.get(current).get(i) == 1 && !visited[i]) {
                dfs(i, visited, result);
            }
        }
    }

    /**
     * 打印图的当前状态
     * 输出格式包括顶点列表和邻接矩阵的完整表示
     * 用于调试和可视化图的结构
     *
     * 输出示例：
     * 顶点列表 = [1, 2, 3, 4]
     * 邻接矩阵 =
     * [[0, 1, 1, 0],
     * [1, 0, 1, 1],
     * [1, 1, 0, 0],
     * [0, 1, 0, 0]]
     */
    public void print() {
        System.out.print("顶点列表 = ");
        System.out.println(vertices);
        System.out.println("邻接矩阵 =");
        PrintUtil.printMatrix(matrix);
    }
}
