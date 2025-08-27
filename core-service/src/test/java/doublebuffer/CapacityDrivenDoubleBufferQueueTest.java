package doublebuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * CapacityDrivenDoubleBufferQueue的测试类
 * 测试各种场景下的功能正确性
 */
class CapacityDrivenDoubleBufferQueueTest {

    private CapacityDrivenDoubleBufferQueue<String> queue;
    private static final int CAPACITY = 3;

    @BeforeEach
    void setUp() {
        queue = new CapacityDrivenDoubleBufferQueue<>(CAPACITY);
    }

    /**
     * 测试基本的放入和取出操作
     */
    @Test
    void testBasicPutAndTake() {
        queue.put("item1");
        queue.put("item2");

        assertEquals("item1", queue.take());
        assertEquals("item2", queue.take());
    }

    /**
     * 测试缓冲区切换机制
     * 当写缓冲区满时，应该触发切换
     */
    @Test
    void testBufferSwitchOnWriteFull() {
        // 填满写缓冲区
        queue.put("item1");
        queue.put("item2");
        queue.put("item3");

        // 此时写缓冲区应该已满
        assertEquals(3, queue.getWriteBufferSize());
        assertEquals(0, queue.getReadBufferSize());

        // 再放入一个元素，应该触发切换
        queue.put("item4");

        // 切换后，原来的写缓冲区变为读缓冲区
        assertTrue(queue.getReadBufferSize() > 0 || queue.getWriteBufferSize() < 3);
    }

    /**
     * 测试缓冲区切换机制
     * 当读缓冲区空时，应该触发切换
     */
    @Test
    void testBufferSwitchOnReadEmpty() {
        // 先放入一些元素
        queue.put("item1");
        queue.put("item2");

        // 取出所有元素
        assertEquals("item1", queue.take());
        assertEquals("item2", queue.take());

        // 此时读缓冲区应该为空
        assertEquals(0, queue.getReadBufferSize());

        // 再次取出，应该触发切换
        // 由于没有更多元素，这里会阻塞，我们验证队列状态
        assertTrue(queue.isEmpty());
    }

    /**
     * 测试并发环境下的正确性
     * 多个生产者和消费者同时操作
     */
    @Test
    void testConcurrentAccess() throws InterruptedException {
        final int producerCount = 3;
        final int consumerCount = 3;
        final int itemsPerProducer = 100;

        ExecutorService executor = Executors.newFixedThreadPool(producerCount + consumerCount);
        CountDownLatch latch = new CountDownLatch(producerCount + consumerCount);
        AtomicInteger produced = new AtomicInteger(0);
        AtomicInteger consumed = new AtomicInteger(0);

        // 启动生产者
        for (int i = 0; i < producerCount; i++) {
            final int producerId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < itemsPerProducer; j++) {
                        queue.put("Producer-" + producerId + "-Item-" + j);
                        produced.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // 启动消费者
        for (int i = 0; i < consumerCount; i++) {
            executor.submit(() -> {
                try {
                    int consumedCount = 0;
                    while (consumedCount < itemsPerProducer) {
                        String item = queue.take();
                        if (item != null) {
                            consumed.incrementAndGet();
                            consumedCount++;
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // 等待所有任务完成
        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        // 验证所有生产的项目都被消费
        assertEquals(produced.get(), consumed.get());
    }

    /**
     * 测试队列容量限制
     */
    @Test
    void testCapacityLimit() {
        // 测试容量限制
        for (int i = 0; i < CAPACITY * 2; i++) {
            queue.put("item-" + i);
        }

        // 应该能够取出所有元素
        for (int i = 0; i < CAPACITY * 2; i++) {
            String item = queue.take();
            assertNotNull(item);
        }

        assertTrue(queue.isEmpty());
    }

    /**
     * 测试交替读写场景
     */
    @Test
    void testAlternatingReadWrite() {
        queue.put("A");
        assertEquals("A", queue.take());

        queue.put("B");
        queue.put("C");
        assertEquals("B", queue.take());
        assertEquals("C", queue.take());

        queue.put("D");
        queue.put("E");
        queue.put("F");
        queue.put("G");

        assertEquals("D", queue.take());
        assertEquals("E", queue.take());
        assertEquals("F", queue.take());
        assertEquals("G", queue.take());
    }

    /**
     * 测试性能：大量数据的处理
     */
    @Test
    void testPerformance() {
        final int testSize = 10000;
        long startTime = System.currentTimeMillis();

        // 生产数据
        for (int i = 0; i < testSize; i++) {
            queue.put("data-" + i);
        }

        // 消费数据
        for (int i = 0; i < testSize; i++) {
            String data = queue.take();
            assertNotNull(data);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Processed " + testSize + " items in " + duration + "ms");
        assertTrue(duration < 5000, "Performance test should complete within 5 seconds");
    }
}