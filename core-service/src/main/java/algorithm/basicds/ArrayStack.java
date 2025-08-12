package algorithm.basicds;


import algorithm.Stack;
import java.util.EmptyStackException;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 基于数组实现的泛型栈数据结构
 *
 * @author magicliang
 *
 *         date: 2025-08-12 17:26
 */
public class ArrayStack<T> implements Stack<T> {

    private static final int DEFAULT_SIZE = 10;

    private T[] elements;

    private int size;

    public ArrayStack() {
        elements = (T[]) new Object[DEFAULT_SIZE];
        size = 0; // 易错的点：忘记初始化
    }

    public ArrayStack(int capacity) {
        if (capacity <= 0) { // 易错的点：不正确的初始化参数
            throw new IllegalArgumentException("Capacity must be positive");
        }
        elements = (T[]) new Object[capacity];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * 检查栈是否为空
     *
     * @return 如果栈为空返回true，否则返回false
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 将指定元素压入栈顶
     *
     * @param item 要压入栈的元素，不能为null
     * @throws IllegalArgumentException 如果item为null
     */
    @Override
    public void push(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot push null item");
        }
        if (size == elements.length) { // 易错的点：忘记扩容
            resize(elements.length * 2); // 动态扩容
        }
        elements[size++] = item;
    }

    /**
     * 移除并返回栈顶元素
     *
     * @return 栈顶元素
     * @throws EmptyStackException 如果栈为空
     */
    @Override
    public T pop() {
        if (isEmpty()) { // 易错的点：忘记处理 size 在左右边界的情况
            throw new EmptyStackException();
        }
        // 在一步里面完成对 size 的修改
        T element = elements[--size];
        elements[size] = null; // 避免内存泄漏
        return element;
    }

    /**
     * 返回栈顶元素但不移除
     *
     * @return 栈顶元素
     * @throws EmptyStackException 如果栈为空
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return elements[size - 1];
    }

    /**
     * 动态扩容数组
     *
     * @param newCapacity 新的容量
     */
    private void resize(int newCapacity) {
        T[] newElements = (T[]) new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }
}
