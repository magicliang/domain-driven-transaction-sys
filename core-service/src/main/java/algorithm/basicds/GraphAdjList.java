package algorithm.basicds;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 基于邻接表实现的无向图类
 * 使用HashMap存储邻接表结构，支持动态添加/删除顶点和边
 * 时间复杂度：添加/删除顶点O(1)，添加/删除边O(1)，删除顶点O(V+E)
 *
 * @author magicliang
 * @date 2025-08-19 18:43
 */
public class GraphAdjList {

    /**
     * 邻接表数据结构
     * key：顶点对象
     * value：与该顶点相邻的所有顶点列表
     * 使用HashMap保证O(1)的顶点查找效率
     */
    private final Map<Vertex, List<Vertex>> adjList;

    /**
     * 默认构造方法
     * 创建一个空的图
     */
    public GraphAdjList() {
        this.adjList = new HashMap<>();
    }

    /**
     * 根据边数组构造图
     *
     * @param edges 边数组，每个元素是长度为2的顶点数组，表示一条无向边
     * @throws IllegalArgumentException 如果边数组格式不正确
     */
    public GraphAdjList(Vertex[][] edges) {
        this();
        // 添加所有顶点和边
        for (Vertex[] edge : edges) {
            if (edge == null || edge.length != 2) {
                throw new IllegalArgumentException("每条边必须包含两个顶点");
            }
            addVertex(edge[0]);
            addVertex(edge[1]);
            addEdge(edge[0], edge[1]);
        }
    }

    /**
     * 获取图中顶点数量
     *
     * @return 当前图中的顶点总数
     */
    public int size() {
        return adjList.size();
    }

    /**
     * 添加顶点
     *
     * @param vet 要添加的顶点
     * @throws IllegalArgumentException 如果顶点为null
     */
    public void addVertex(Vertex vet) {
        if (vet == null) {
            throw new IllegalArgumentException("顶点不能为null");
        }
        if (adjList.containsKey(vet)) {
            return;
        }
        adjList.put(vet, new ArrayList<>());
    }

    /**
     * 删除顶点及其所有关联边
     *
     * @param vet 要删除的顶点
     * @throws IllegalArgumentException 如果顶点不存在或为null
     */
    public void removeVertex(Vertex vet) {
        if (vet == null) {
            throw new IllegalArgumentException("顶点不能为null");
        }
        if (!adjList.containsKey(vet)) {
            throw new IllegalArgumentException("顶点不存在: " + vet.getVal());
        }
        // 移除顶点及其邻接列表
        adjList.remove(vet);
        // 从其他顶点的邻接列表中移除该顶点
        for (List<Vertex> edges : adjList.values()) {
            edges.remove(vet);
        }
    }

    /**
     * 添加无向边
     *
     * @param vet1 边的第一个顶点
     * @param vet2 边的第二个顶点
     * @throws IllegalArgumentException 如果任一顶点不存在或为null，或两个顶点相同
     */
    public void addEdge(Vertex vet1, Vertex vet2) {
        if (vet1 == null || vet2 == null) {
            throw new IllegalArgumentException("顶点不能为null");
        }
        if (!adjList.containsKey(vet1) || !adjList.containsKey(vet2)) {
            throw new IllegalArgumentException("顶点不存在，请先添加顶点");
        }
        if (vet1.equals(vet2)) {
            throw new IllegalArgumentException("不能添加自环边");
        }
        // 添加双向边（无向图特性）
        adjList.get(vet1).add(vet2);
        adjList.get(vet2).add(vet1);
    }

    /**
     * 删除无向边
     *
     * @param vet1 边的第一个顶点
     * @param vet2 边的第二个顶点
     * @throws IllegalArgumentException 如果任一顶点不存在或为null，或两个顶点相同
     */
    public void removeEdge(Vertex vet1, Vertex vet2) {
        if (vet1 == null || vet2 == null) {
            throw new IllegalArgumentException("顶点不能为null");
        }
        if (!adjList.containsKey(vet1) || !adjList.containsKey(vet2)) {
            throw new IllegalArgumentException("顶点不存在");
        }
        if (vet1.equals(vet2)) {
            throw new IllegalArgumentException("不能删除自环边");
        }
        // 移除双向边
        adjList.get(vet1).remove(vet2);
        adjList.get(vet2).remove(vet1);
    }

    /**
     * 广度优先搜索遍历图
     * 从指定起始顶点开始进行BFS遍历，返回遍历顺序的顶点列表
     * 因为邻接表是有重复节点的，所以我们为了遍历去重，必须借助数据结构
     * 使用HashSet记录已访问顶点避免重复遍历，使用队列实现层次遍历
     *
     * 时间复杂度：O(V + E)
     * 证明：每个顶点入队出队各一次(O(V))，每条边最多被检查两次(O(E))
     *
     * 空间复杂度：O(V)
     * 证明：
     * 1. 队列(queue)最坏情况下存储整层的顶点，最多存储O(V)个顶点
     * 2. 集合(set)存储所有已访问顶点，最多存储O(V)个顶点
     * 3. 结果列表(result)存储所有顶点，存储O(V)个顶点
     * 4. 其他变量为常数空间O(1)
     * 因此总空间复杂度为O(V)
     *
     * 关于已访问顶点的处理：
     * BFS保证了一旦顶点被访问，就已经找到了从起点到它的最短路径。
     * 再次访问该顶点不会产生新的有用信息，因此可以安全跳过已访问顶点。
     * 这与回溯算法不同，回溯算法可能需要在不同路径中多次访问同一节点。
     *
     * @param startVet 起始顶点
     * @return 按BFS顺序排列的顶点列表
     * @throws IllegalArgumentException 如果起始顶点不存在
     */
    public List<Vertex> bfsTraversal(Vertex startVet) {
        if (!adjList.containsKey(startVet)) {
            throw new IllegalArgumentException("不存在的顶点");
        }

        Set<Vertex> set = new HashSet<>();
        Deque<Vertex> queue = new LinkedList<>();
        queue.offer(startVet);

        List<Vertex> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            // 如果是用唯一路径访问过，可以认为只要访问过 current，它的剩余路径也被访问过了，可以直接在这里退出
            // 1. BFS保证了一旦顶点被访问，就已经找到了从起点到它的最短路径
            // 2. 再次访问该顶点不会产生新的有用信息
            // 3. 这是为了避免重复处理和潜在的无限循环
            if (set.contains(current)) {
                continue;
            }
            result.add(current);
            set.add(current);
            final List<Vertex> adjacentVertices = adjList.get(current);
            for (Vertex vertex : adjacentVertices) {
                if (set.contains(vertex)) {
                    continue;
                }
                queue.offer(vertex);
            }
        }

        return result;
    }

    /**
     * 深度优先搜索遍历图
     * 从指定起始顶点开始进行DFS遍历，返回遍历顺序的顶点列表
     * 使用HashSet记录已访问顶点避免重复遍历，使用递归实现深度优先探索
     *
     * 时间复杂度：O(V + E)
     * 证明：每个顶点被访问一次(O(V))，每条边最多被检查两次(O(E))
     *
     * 空间复杂度：O(V)
     * 证明：
     * 1. 递归调用栈(call stack)最坏情况下存储从起点到最远叶子的路径，最多存储O(V)个顶点
     * 2. 集合(visited)存储所有已访问顶点，最多存储O(V)个顶点
     * 3. 结果列表(result)存储所有顶点，存储O(V)个顶点
     * 4. 其他变量为常数空间O(1)
     * 因此总空间复杂度为O(V)
     *
     * 关于已访问顶点的处理：
     * DFS保证了一旦访问过某个顶点，其所有可达路径都已被探索。
     * 再次访问该顶点不会产生新的有用信息，因此可以安全跳过已访问顶点。
     * 这与回溯算法不同：回溯算法需要在不同路径中多次访问同一节点，
     * 而DFS图遍历中每个顶点只需访问一次。
     *
     * @param startVet 起始顶点
     * @return 按DFS顺序排列的顶点列表
     * @throws IllegalArgumentException 如果起始顶点不存在
     */
    public List<Vertex> dfsTraversal(Vertex startVet) {
        // 只访问图中的邻接节点，还是要搞递归下降
        if (!adjList.containsKey(startVet)) {
            throw new IllegalArgumentException("不存在的顶点");
        }

        Set<Vertex> visited = new HashSet<>();
        List<Vertex> result = new ArrayList<>();

        dfs(startVet, result, visited);

        return result;
    }

    /**
     * DFS递归辅助方法
     * 从当前顶点开始进行深度优先搜索
     *
     * 空间复杂度：O(V) - 由递归调用栈深度决定，最坏情况下为图的高度
     * 证明：递归调用栈深度等于从当前顶点到最远叶子的路径长度，最多为O(V)
     *
     * 关于已访问顶点的处理：
     * DFS图遍历中，一旦顶点被访问，其所有可达路径都已被探索。
     * 这与回溯算法形成对比：回溯算法需要在不同路径中多次访问同一节点，
     * 而DFS遍历中每个顶点只需访问一次即可保证遍历完整性。
     *
     * @param current 当前访问的顶点
     * @param result 存储遍历结果的列表
     * @param visited 记录已访问顶点的集合
     */
    private void dfs(Vertex current, List<Vertex> result, Set<Vertex> visited) {
        // 如果当前顶点已经访问过，跳过以避免重复处理；
        // DFS保证了一旦访问过该顶点，其所有可达路径都已被探索
        if (visited.contains(current)) {
            return;
        }
        result.add(current);
        visited.add(current);
        final List<Vertex> vertices = adjList.get(current);
        if (null == vertices) {
            return;
        }
        for (Vertex vertex : vertices) {
            dfs(vertex, result, visited);
        }
    }

    /**
     * 非递归深度优先搜索遍历图
     * 使用栈实现DFS遍历，避免递归调用栈溢出问题
     * 从指定起始顶点开始进行DFS遍历，返回遍历顺序的顶点列表
     *
     * 时间复杂度：O(V + E)
     * 证明：每个顶点被访问一次(O(V))，每条边最多被检查两次(O(E))
     *
     * 空间复杂度：O(V)
     * 证明：
     * 1. 栈(stack)最坏情况下存储从起点到最远叶子的路径，最多存储O(V)个顶点
     * 2. 集合(visited)存储所有已访问顶点，最多存储O(V)个顶点
     * 3. 结果列表(result)存储所有顶点，存储O(V)个顶点
     * 4. 其他变量为常数空间O(1)
     * 因此总空间复杂度为O(V)
     *
     * 实现说明：
     * 使用栈的后进先出(LIFO)特性模拟递归调用栈
     * 为保证遍历顺序与递归DFS一致，邻接顶点按逆序压栈
     *
     * @param startVet 起始顶点
     * @return 按DFS顺序排列的顶点列表
     * @throws IllegalArgumentException 如果起始顶点不存在
     */
    public List<Vertex> dfsTraversalNonRecursive(Vertex startVet) {
        // 只访问图中的邻接节点，还是要搞递归下降
        if (!adjList.containsKey(startVet)) {
            throw new IllegalArgumentException("不存在的顶点");
        }

        Set<Vertex> visited = new HashSet<>();
        List<Vertex> result = new ArrayList<>();
        Stack<Vertex> stack = new Stack<>();
        Vertex current = startVet;
        stack.push(current);
        // 用栈来实现深度优先遍历的方法，每次处理栈顶节点，逆序入栈保证遍历顺序
        while (!stack.isEmpty()) {
            current = stack.pop();
            if (current == null || visited.contains(current)) {
                continue;
            }
            result.add(current);
            visited.add(current);
            final List<Vertex> vertices = adjList.get(current);
            if (vertices == null) {
                continue;
            }
            int n = vertices.size();
            for (int i = n - 1; i >= 0; i--) {
                stack.push(vertices.get(i));
            }
        }
        return result;
    }

    /**
     * 打印图的邻接表表示
     * 输出格式：顶点值: [邻接顶点值列表]
     */
    public void print() {
        System.out.println("邻接表 =");
        for (Map.Entry<Vertex, List<Vertex>> pair : adjList.entrySet()) {
            List<Integer> tmp = new ArrayList<>();
            for (Vertex vertex : pair.getValue()) {
                tmp.add(vertex.val);
            }
            System.out.println(pair.getKey().val + ": " + tmp + ",");
        }
    }

    /**
     * 顶点定义
     * 使用Lombok自动生成getter/setter和全参构造方法
     */
    @Data
    @AllArgsConstructor
    public static class Vertex {

        /**
         * 顶点值
         * 用于标识顶点，允许重复值存在
         */
        private int val;
    }
}
