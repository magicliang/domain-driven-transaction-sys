package doublebuffer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CapacityDrivenDoubleBufferQueue的演示类
 * 展示如何在实际场景中使用双缓冲队列
 */
public class DoubleBufferDemo {

    public static void main(String[] args) throws InterruptedException {
        // 创建一个容量为5的双缓冲队列
        CapacityDrivenDoubleBufferQueue<String> messageQueue =
                new CapacityDrivenDoubleBufferQueue<>(5);

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 启动生产者线程
        executor.submit(() -> {
            String[] messages = {
                    "Hello", "World", "Double", "Buffer", "Queue",
                    "Is", "Working", "Perfectly", "In", "Java"
            };

            for (String message : messages) {
                messageQueue.put(message);
                System.out.println("Produced: " + message);

                // 模拟生产延迟
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // 启动消费者线程
        executor.submit(() -> {
            int receivedCount = 0;
            while (receivedCount < 10) {
                String message = messageQueue.take();
                System.out.println("Consumed: " + message);
                receivedCount++;

                // 模拟处理延迟
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // 启动监控线程
        executor.submit(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                    System.out.printf("Queue status - WriteBuffer: %d, ReadBuffer: %d, Empty: %b%n",
                            messageQueue.getWriteBufferSize(),
                            messageQueue.getReadBufferSize(),
                            messageQueue.isEmpty());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // 等待所有任务完成
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Demo completed!");
    }
}