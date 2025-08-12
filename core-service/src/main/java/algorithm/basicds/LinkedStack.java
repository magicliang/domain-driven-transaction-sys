package algorithm.basicds;


import algorithm.Stack;
import java.util.EmptyStackException;
import lombok.Getter;

/**
 * project name: domain-driven-transaction-sys
 *
 * description:
 *
 * @author magicliang
 *
 *         date: 2025-08-12 17:40
 */
public class LinkedStack<T> implements Stack<T> {

    @Getter
    class Node<T> {

        private T item;

        private Node<T> next;

        /**
         * 为了方便操作，还是需要增加 prev 方便在尾部增减元素
         */
        private Node<T> prev;


        public Node(T item) {
            this.item = item;
        }
    }

    /**
     * 通常栈只需要尾指针就行
     */
    private Node<T> top;

    /**
     * 通常基于链表的数据结构不需要知道容量，也就没有 resize 函数
     */
    private int size;

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
        size++;
        if (top == null) {
            // 注意：栈不是循环列表
            top = new Node<>(item);
            return;
        }

        top.next = new Node<>(item);
        top.next.prev = top;
        top = top.next;
    }

    /**
     * 移除并返回栈顶元素
     *
     * @return 栈顶元素
     * @throws EmptyStackException 如果栈为空
     */
    @Override
    public T pop() {
        if (top == null) {
            throw new EmptyStackException();
        }
        Node<T> oldTail = top;
        top = top.prev;
        oldTail.prev = null;
        if (top != null) { // 易错的点：prev 和 next 都可能为 null，特别是只有一个元素或者跨越边界的时候
            top.next = null;
        }
        size--;
        return oldTail.getItem();
    }

    /**
     * 返回栈顶元素但不移除
     *
     * @return 栈顶元素
     * @throws EmptyStackException 如果栈为空
     */
    @Override
    public T peek() {
        if (top == null) {
            throw new EmptyStackException();
        }
        return top.getItem();
    }

    @Override
    public int size() {
        return size;
    }

}
