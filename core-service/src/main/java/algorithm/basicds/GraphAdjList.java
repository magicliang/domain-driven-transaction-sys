package algorithm.basicds;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
}
