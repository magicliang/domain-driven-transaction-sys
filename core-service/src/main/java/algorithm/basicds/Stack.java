package algorithm.basicds;


/**
 * project name: domain-driven-transaction-sys
 *
 * description: 泛型栈接口定义，提供了栈数据结构的基本操作规范
 *
 * @param <T> 栈中元素的类型
 * @author magicliang
 *
 *         date: 2025-08-12 17:23
 */
public interface Stack<T> {

    /**
     * 将指定元素压入栈顶
     *
     * @param item 要压入栈的元素，不能为null
     * @throws IllegalArgumentException 如果item为null
     */
    void push(T item);

    /**
     * 移除并返回栈顶元素
     *
     * @return 栈顶元素
     * @throws java.util.EmptyStackException 如果栈为空
     */
    T pop();

    /**
     * 返回栈顶元素但不移除
     *
     * @return 栈顶元素
     * @throws java.util.EmptyStackException 如果栈为空
     */
    T peek();

    int size();

    /**
     * 检查栈是否为空
     *
     * @return 如果栈为空返回true，否则返回false
     */
    boolean isEmpty();
}
