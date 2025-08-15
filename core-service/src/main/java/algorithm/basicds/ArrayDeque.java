package algorithm.basicds;

import java.util.NoSuchElementException;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 基于循环数组实现的双端队列数据结构
 * 支持在队列两端进行高效的插入和删除操作
 *
 * @author magicliang
 *
 *         date: 2025-08-12 19:35
 */
public class ArrayDeque<T> implements Deque<T> {

    private static final int DEFAULT_CAPACITY = 16;

    private T[] elements;
    private int head;
    private int tail;
    private int size;
    private int capacity;

    public ArrayDeque() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayDeque(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.elements = (T[]) new Object[capacity];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    @Override
    public void addFirst(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item");
        }
        if (size == capacity) {
            resize(capacity * 2);
        }
        head = (head - 1 + capacity) % capacity;
        elements[head] = item;
        size++;
    }

    @Override
    public void addLast(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item");
        }
        if (size == capacity) {
            resize(capacity * 2);
        }
        elements[tail] = item;
        tail = (tail + 1) % capacity;
        size++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        T item = elements[head];
        elements[head] = null; // 避免内存泄漏
        head = (head + 1) % capacity;
        size--;

        // 缩容策略：当元素数量小于容量的1/4且容量大于默认容量时
        if (size > 0 && size == capacity / 4 && capacity > DEFAULT_CAPACITY) {
            resize(capacity / 2);
        }
        return item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        tail = (tail - 1 + capacity) % capacity;
        T item = elements[tail];
        elements[tail] = null; // 避免内存泄漏
        size--;

        // 缩容策略
        if (size > 0 && size == capacity / 4 && capacity > DEFAULT_CAPACITY) {
            resize(capacity / 2);
        }
        return item;
    }

    @Override
    public T peekFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[head];
    }

    @Override
    public T peekLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        int lastIndex = (tail - 1 + capacity) % capacity;
        return elements[lastIndex];
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
     * 动态调整数组容量
     * 由于循环数组的特殊结构，需要按逻辑顺序重新排列元素
     *
     * @param newCapacity 新的容量
     */
    private void resize(int newCapacity) {
        T[] newElements = (T[]) new Object[newCapacity];

        // 将元素按逻辑顺序复制到新数组
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[(head + i) % capacity];
        }

        elements = newElements;
        head = 0;
        tail = size;
        capacity = newCapacity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[(head + i) % capacity]);
        }
        sb.append("]");
        return sb.toString();
    }
}