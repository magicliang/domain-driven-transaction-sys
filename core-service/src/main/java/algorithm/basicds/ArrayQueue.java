package algorithm.basicds;

import algorithm.Queue;
import java.util.NoSuchElementException;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 基于数组实现的泛型队列数据结构，采用循环数组技术
 *
 * @author magicliang
 *
 *         date: 2025-08-12 19:19
 */
public class ArrayQueue<T> implements Queue<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private T[] elements;
    private int head;  // 队列头部索引
    private int tail;  // 队列尾部索引（下一个插入位置）
    private int size;  // 当前元素数量
    private int capacity; // 数组容量

    public ArrayQueue() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayQueue(int capacity) {
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
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void enqueue(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot enqueue null item");
        }

        if (size == capacity) {
            resize(capacity * 2); // 动态扩容
        }

        elements[tail] = item;
        tail = (tail + 1) % capacity; // 循环数组
        size++;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        T element = elements[head];
        elements[head] = null; // 避免内存泄漏
        head = (head + 1) % capacity; // 循环数组
        size--;

        // 缩容：当元素数量小于容量的1/4且容量大于默认容量时
        if (size > 0 && size == capacity / 4 && capacity > DEFAULT_CAPACITY) {
            resize(capacity / 2);
        }

        return element;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return elements[head];
    }

    /**
     * 动态调整数组大小
     * <p>
     * 注意：这里不能使用 {@link System#arraycopy(Object, int, Object, int, int)}，
     * 因为循环数组中的元素可能分布在数组的两端，不是连续存储的。
     * </p>
     * <p>
     * 例如，当队列使用循环数组时，可能出现以下情况：
     * <pre>
     * 原始循环数组（容量=8）:
     * [5, 6, 7, null, null, 1, 2, 3, 4]
     *          ↑tail        ↑head
     * </pre>
     * 元素1,2,3,4,5,6,7分布在数组的两端，如果使用System.arraycopy，
     * 只能复制连续的内存块，无法处理这种"断裂"的分布。
     * </p>
     * <p>
     * 因此必须使用循环计算的方式，按逻辑顺序重新排列元素到新数组中：
     * <pre>
     * 新数组（容量=16）:
     * [1, 2, 3, 4, 5, 6, 7, null, null, ...]
     * </pre>
     * </p>
     *
     * @param newCapacity 新的容量
     */
    private void resize(int newCapacity) {
        T[] newElements = (T[]) new Object[newCapacity];

        // 将元素按逻辑顺序复制到新数组
        // 使用循环计算确保正确处理循环数组的"断裂"分布
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[(head + i) % capacity];
        }

        elements = newElements;
        head = 0;  // 重置头指针到数组起始位置
        tail = size;  // 尾指针指向size位置
        capacity = newCapacity;
    }

    /**
     * 获取队列的当前容量（主要用于测试）
     *
     * @return 当前容量
     */
    public int getCapacity() {
        return capacity;
    }
}