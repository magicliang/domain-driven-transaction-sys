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
     * 就一般而言无锁 swap 太难保证原子性，太危险了，容易造成不一致
     */
    private synchronized void swap() {
        BlockingQueue<T> temp = writeBuffer;
        writeBuffer = readBuffer;
        readBuffer = temp;
    }
}
