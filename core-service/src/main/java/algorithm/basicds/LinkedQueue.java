package algorithm.basicds;

import java.util.NoSuchElementException;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 基于单向链表实现的泛型队列数据结构
 * 特点：
 * - 使用头尾指针实现O(1)时间的入队和出队操作
 * - 不需要考虑容量限制，支持动态增长
 * - 内存使用更灵活，但每个元素有额外指针开销
 *
 * @author magicliang
 *
 *         date: 2025-08-12 19:25
 */
public class LinkedQueue<T> implements Queue<T> {

    private Node<T> head;  // 队列头部（出队端）
    private Node<T> tail;  // 队列尾部（入队端）
    private int size;      // 当前元素数量
    public LinkedQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 将元素添加到队列尾部
     * 时间复杂度：O(1)
     *
     * @param item 要入队的元素，不能为null
     * @throws IllegalArgumentException 如果item为null
     */
    @Override
    public void enqueue(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot enqueue null item");
        }

        Node<T> newNode = new Node<>(item);

        if (isEmpty()) {
            // 空队列时，头尾指针都指向新节点
            head = tail = newNode;
        } else {
            // 非空队列，在尾部添加新节点
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    /**
     * 从队列头部移除并返回元素
     * 时间复杂度：O(1)
     *
     * @return 队列头部的元素
     * @throws NoSuchElementException 如果队列为空
     */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        T element = head.item;
        head = head.next;
        size--;

        // 如果队列变为空，重置tail指针
        if (isEmpty()) {
            tail = null;
        }

        return element;
    }

    /**
     * 查看队列头部元素但不移除
     * 时间复杂度：O(1)
     *
     * @return 队列头部的元素
     * @throws NoSuchElementException 如果队列为空
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return head.item;
    }

    /**
     * 获取队列的字符串表示，用于调试
     * 格式：[元素1, 元素2, 元素3]
     *
     * @return 队列的字符串表示
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        Node<T> current = head;
        while (current != null) {
            sb.append(current.item);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * 链表节点内部类
     */
    private static class Node<T> {

        T item;
        Node<T> next;

        Node(T item) {
            this.item = item;
            this.next = null;
        }
    }
}