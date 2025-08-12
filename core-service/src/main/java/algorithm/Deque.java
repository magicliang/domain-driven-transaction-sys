package algorithm;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 泛型双端队列接口定义，支持在队列两端进行插入和删除操作
 * 双端队列(Deque)结合了队列和栈的特性，可以作为队列、栈或双端队列使用
 *
 * @param <T> 队列中元素的类型
 * @author magicliang
 *
 *         date: 2025-08-12 19:27
 */
public interface Deque<T> extends Queue<T> {

    /**
     * 在队列头部插入元素
     * 时间复杂度：O(1)
     *
     * @param item 要插入的元素，不能为null
     * @throws IllegalArgumentException 如果item为null
     */
    void addFirst(T item);

    /**
     * 在队列尾部插入元素
     * 等同于Queue.enqueue()
     * 时间复杂度：O(1)
     *
     * @param item 要插入的元素，不能为null
     * @throws IllegalArgumentException 如果item为null
     */
    void addLast(T item);

    /**
     * 移除并返回队列头部元素
     * 等同于Queue.dequeue()
     * 时间复杂度：O(1)
     *
     * @return 队列头部的元素
     * @throws java.util.NoSuchElementException 如果队列为空
     */
    T removeFirst();

    /**
     * 移除并返回队列尾部元素
     * 时间复杂度：O(1)
     *
     * @return 队列尾部的元素
     * @throws java.util.NoSuchElementException 如果队列为空
     */
    T removeLast();

    /**
     * 查看队列头部元素但不移除
     * 等同于Queue.peek()
     * 时间复杂度：O(1)
     *
     * @return 队列头部的元素
     * @throws java.util.NoSuchElementException 如果队列为空
     */
    T peekFirst();

    /**
     * 查看队列尾部元素但不移除
     * 时间复杂度：O(1)
     *
     * @return 队列尾部的元素
     * @throws java.util.NoSuchElementException 如果队列为空
     */
    T peekLast();

    /**
     * 栈操作：将元素压入栈顶（等同于addFirst）
     * 时间复杂度：O(1)
     *
     * @param item 要压入栈的元素，不能为null
     * @throws IllegalArgumentException 如果item为null
     */
    default void push(T item) {
        addFirst(item);
    }

    /**
     * 栈操作：移除并返回栈顶元素（等同于removeFirst）
     * 时间复杂度：O(1)
     *
     * @return 栈顶元素
     * @throws java.util.NoSuchElementException 如果栈为空
     */
    default T pop() {
        return removeFirst();
    }

    /**
     * 栈操作：查看栈顶元素但不移除（等同于peekFirst）
     * 时间复杂度：O(1)
     *
     * @return 栈顶元素
     * @throws java.util.NoSuchElementException 如果栈为空
     */
    default T peek() {
        return peekFirst();
    }

    /**
     * 队列操作：入队（等同于addLast）
     * 时间复杂度：O(1)
     *
     * @param item 要入队的元素，不能为null
     * @throws IllegalArgumentException 如果item为null
     */
    @Override
    default void enqueue(T item) {
        addLast(item);
    }

    /**
     * 队列操作：出队（等同于removeFirst）
     * 时间复杂度：O(1)
     *
     * @return 队列头部的元素
     * @throws java.util.NoSuchElementException 如果队列为空
     */
    @Override
    default T dequeue() {
        return removeFirst();
    }

}