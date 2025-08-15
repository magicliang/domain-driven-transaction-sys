package algorithm.basicds;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 泛型队列接口定义，提供了队列数据结构的基本操作规范
 *
 * @param <T> 队列中元素的类型
 * @author magicliang
 *
 *         date: 2025-08-12 19:19
 */
public interface Queue<T> {

    /**
     * 将指定元素入队到队列尾部
     *
     * @param item 要入队的元素，不能为null
     * @throws IllegalArgumentException 如果item为null
     */
    void enqueue(T item);

    /**
     * 移除并返回队列头部元素
     *
     * @return 队列头部元素
     * @throws java.util.NoSuchElementException 如果队列为空
     */
    T dequeue();

    /**
     * 返回队列头部元素但不移除
     *
     * @return 队列头部元素
     * @throws java.util.NoSuchElementException 如果队列为空
     */
    T peek();

    /**
     * 返回队列中的元素数量
     *
     * @return 队列中的元素数量
     */
    int size();

    /**
     * 检查队列是否为空
     *
     * @return 如果队列为空返回true，否则返回false
     */
    boolean isEmpty();
}