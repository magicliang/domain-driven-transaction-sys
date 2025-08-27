package doublebuffer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 尝试写一个读写缓冲区实现
 * 这个设计的精髓在于：
 * 1. 把取或者写封装在函数里，然后函数指向特定指针。
 * 2. 在读写遇到瓶颈以后，直接触发切换。
 *
 * 也可以设计为使用两个 size 来维护 buffer 的容量，有一个 buffer 触发阈值以后就直接切换
 *
 * @author magicliang
 *
 *         date: 2025-08-12 16:48
 */
public class CapacityDrivenDoubleBufferQueue<T> {

    private volatile BlockingQueue<T> writeBuffer;
    private volatile BlockingQueue<T> readBuffer;

    public CapacityDrivenDoubleBufferQueue(int capacaity) {
        // 使用任意的 BlockingQueue 可以实现 offer 在队列满了以后返回 false
        this.writeBuffer = new ArrayBlockingQueue<>(capacaity);
        this.readBuffer = new ArrayBlockingQueue<>(capacaity);
    }

    /**
     * 向双缓冲队列中放入元素
     *
     * 工作原理：
     * 1. 首先尝试向当前的写缓冲区(writeBuffer)放入元素
     * 2. 如果写缓冲区已满(offer返回false)，则触发swap操作交换读写缓冲区
     * 3. 交换后重试，直到成功放入元素
     *
     * 注意：由于使用了while(true)，在极端情况下可能会无限循环，
     * 特别是当两个缓冲区都满的时候。实际应用中可能需要设置最大重试次数
     *
     * @param item 要放入队列的元素
     */
    public void put(T item) {
        // 如果运气不好，则这个写入可能无限循环，阻塞在这里。有些ai建议在这里 for 3 次
        while (true) {
            if (writeBuffer.offer(item)) {
                return;
            }
            // 如果不return 则触发 swap，进入下一个重试
            swap();
        }
    }

    /**
     * 从双缓冲队列中取出元素
     *
     * 工作原理：
     * 1. 首先尝试从当前的读缓冲区(readBuffer)取出元素
     * 2. 如果读缓冲区为空(poll返回null)，则触发swap操作交换读写缓冲区
     * 3. 交换后重试，直到成功取出元素
     *
     * 注意：由于使用了while(true)，在极端情况下可能会无限循环，
     * 特别是当两个缓冲区都空的时候。实际应用中可能需要设置最大重试次数
     *
     * @return 从队列中取出的元素
     */
    public T take() {
        // 如果运气不好，则这个读取可能无限循环，阻塞在这里。有些ai建议在这里 for 3 次
        while (true) {
            final T result = readBuffer.poll();
            if (result != null) {
                return result;
            }
            // 如果不return 则触发 swap，进入下一个重试
            swap();
        }
    }

    /**
     * 交换读写缓冲区
     *
     * 这是双缓冲机制的核心操作：
     * 1. 将当前的写缓冲区变为读缓冲区
     * 2. 将当前的读缓冲区变为写缓冲区
     *
     * 使用synchronized保证线程安全，防止并发交换导致的数据不一致
     * 由于只是交换引用，操作非常快，锁竞争的影响较小
     *
     * 注意：交换后，新的写缓冲区是空的（原来的读缓冲区），
     * 新的读缓冲区可能包含之前写入但未读取的数据
     */
    private synchronized void swap() {
        BlockingQueue<T> temp = writeBuffer;
        writeBuffer = readBuffer;
        readBuffer = temp;
    }

    /**
     * 获取当前写缓冲区的元素数量
     * 主要用于测试和监控
     *
     * @return 写缓冲区中的元素数量
     */
    public int getWriteBufferSize() {
        return writeBuffer.size();
    }

    /**
     * 获取当前读缓冲区的元素数量
     * 主要用于测试和监控
     *
     * @return 读缓冲区中的元素数量
     */
    public int getReadBufferSize() {
        return readBuffer.size();
    }

    /**
     * 检查双缓冲队列是否为空
     * 当两个缓冲区都为空时返回true
     *
     * @return 如果队列为空返回true，否则返回false
     */
    public boolean isEmpty() {
        return writeBuffer.isEmpty() && readBuffer.isEmpty();
    }
}
