package algorithm.basicds;

import algorithm.Queue;
import java.util.NoSuchElementException;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 基于数组实现的泛型队列数据结构，采用循环数组技术
 *
 * <h3>循环数组指针说明</h3>
 * <p>
 * <b>head指针</b>：指向队列头部元素（即下一个要出队的元素）
 * <br>
 * <b>tail指针</b>：指向队列尾部元素的下一个位置（即下一个入队元素的插入位置）
 * </p>
 *
 * <h3>指针计算技巧</h3>
 * <p>
 * 1. <b>循环索引计算</b>：使用模运算实现循环
 * <pre>
 * nextIndex = (currentIndex + 1) % capacity
 * prevIndex = (currentIndex - 1 + capacity) % capacity
 * </pre>
 *
 * 2. <b>队列状态判断</b>：
 * <pre>
 * 空队列：size == 0
 * 满队列：size == capacity
 * </pre>
 *
 * 3. <b>元素位置计算</b>：
 * <pre>
 * 第i个元素位置：(head + i) % capacity
 * 最后一个元素位置：(tail - 1 + capacity) % capacity
 * </pre>
 *
 * 4. <b>指针移动规则</b>：
 * <pre>
 * 入队：elements[tail] = item; tail = (tail + 1) % capacity
 * 出队：item = elements[head]; head = (head + 1) % capacity
 * </pre>
 *
 * 5. <b>容量计算</b>：
 * <pre>
 * 当前容量：capacity
 * 已用空间：size
 * 剩余空间：capacity - size
 * </pre>
 * </p>
 *
 * <h3>示例说明</h3>
 * <p>
 * 假设capacity=5，初始状态：
 * <pre>
 * 索引:  0   1   2   3   4
 * 数组: [ ,  ,  ,  ,  ]
 * head=0, tail=0, size=0
 * </pre>
 *
 * 入队A,B,C后：
 * <pre>
 * 索引:  0   1   2   3   4
 * 数组: [A, B, C,  ,  ]
 * head=0, tail=3, size=3
 * </pre>
 *
 * 出队A,B后：
 * <pre>
 * 索引:  0   1   2   3   4
 * 数组: [ ,  , C,  ,  ]
 * head=2, tail=3, size=1
 * </pre>
 *
 * 继续入队D,E,F,G（触发循环）：
 * <pre>
 * 索引:  0   1   2   3   4
 * 数组: [G,  , C, D, E]
 * head=2, tail=1, size=4
 * </pre>
 * </p>
 *
 * @author magicliang
 *
 *         date: 2025-08-12 19:19
 */
public class ArrayQueue<T> implements Queue<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private T[] elements;
    private int head;  // 队列头部索引 - 指向第一个元素
    private int tail;  // 队列尾部索引 - 指向最后一个元素的下一个位置
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
        this.head = 0;  // 初始时head指向0
        this.tail = 0;  // 初始时tail指向0
        this.size = 0;  // 初始为空
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
        tail = (tail + 1) % capacity; // 循环数组：tail向后移动，到达末尾时回到0
        size++;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        T element = elements[head];
        elements[head] = null; // 避免内存泄漏
        head = (head + 1) % capacity; // 循环数组：head向后移动，到达末尾时回到0
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
        return elements[head]; // 直接返回head指向的元素
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