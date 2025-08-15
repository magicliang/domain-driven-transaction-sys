package algorithm.basicds;

import java.util.NoSuchElementException;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 基于双向链表实现的双端队列数据结构
 * 支持在队列两端进行高效的插入和删除操作
 *
 * @author magicliang
 *
 *         date: 2025-08-12 19:36
 */
public class LinkedDeque<T> implements Deque<T> {

    private static class Node<T> {

        T item;
        Node<T> prev;
        Node<T> next;

        Node(T item) {
            this.item = item;
            this.prev = null;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedDeque() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item");
        }

        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item");
        }

        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }

        T item = head.item;
        head = head.next;

        if (head == null) {
            // 队列变为空
            tail = null;
        } else {
            head.prev = null;
        }

        size--;
        return item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }

        T item = tail.item;
        tail = tail.prev;

        if (tail == null) {
            // 队列变为空
            head = null;
        } else {
            tail.next = null;
        }

        size--;
        return item;
    }

    @Override
    public T peekFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return head.item;
    }

    @Override
    public T peekLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return tail.item;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

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
}