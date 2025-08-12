package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ArrayQueue的单元测试类
 */
class ArrayQueueTest {

    private ArrayQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new ArrayQueue<>();
    }

    @Test
    @DisplayName("测试空队列状态")
    void testEmptyQueue() {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
        assertThrows(NoSuchElementException.class, () -> queue.dequeue());
        assertThrows(NoSuchElementException.class, () -> queue.peek());
    }

    @Test
    @DisplayName("测试基本入队和出队操作")
    void testBasicEnqueueDequeue() {
        queue.enqueue(1);
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        assertEquals(1, queue.peek());

        queue.enqueue(2);
        assertEquals(2, queue.size());
        assertEquals(1, queue.peek());

        assertEquals(1, queue.dequeue());
        assertEquals(1, queue.size());
        assertEquals(2, queue.peek());

        assertEquals(2, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("测试FIFO顺序")
    void testFIFOOrder() {
        int[] testData = {1, 2, 3, 4, 5};

        for (int value : testData) {
            queue.enqueue(value);
        }

        assertEquals(5, queue.size());

        for (int expected : testData) {
            assertEquals(expected, queue.dequeue());
        }

        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("测试null元素异常")
    void testNullElementException() {
        assertThrows(IllegalArgumentException.class, () -> queue.enqueue(null));
    }

    @Test
    @DisplayName("测试循环数组行为")
    void testCircularArrayBehavior() {
        // 填满队列
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }

        // 出队5个元素
        for (int i = 0; i < 5; i++) {
            assertEquals(i, queue.dequeue());
        }

        // 再入队5个元素，测试循环行为
        for (int i = 10; i < 15; i++) {
            queue.enqueue(i);
        }

        // 验证顺序正确
        for (int i = 5; i < 15; i++) {
            assertEquals(i, queue.dequeue());
        }

        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("测试动态扩容")
    void testDynamicResize() {
        // 测试默认容量10的扩容
        for (int i = 0; i < 15; i++) {
            queue.enqueue(i);
        }

        assertEquals(15, queue.size());
        assertTrue(queue.getCapacity() >= 15);

        // 验证顺序正确
        for (int i = 0; i < 15; i++) {
            assertEquals(i, queue.dequeue());
        }
    }

    @Test
    @DisplayName("测试自定义容量")
    void testCustomCapacity() {
        ArrayQueue<String> customQueue = new ArrayQueue<>(5);

        // 测试5个元素不扩容
        for (int i = 0; i < 5; i++) {
            customQueue.enqueue("item" + i);
        }

        assertEquals(5, customQueue.size());
        assertEquals(5, customQueue.getCapacity());

        // 测试第6个元素触发扩容
        customQueue.enqueue("item5");
        assertEquals(6, customQueue.size());
        assertTrue(customQueue.getCapacity() >= 6);
    }

    @Test
    @DisplayName("测试缩容功能")
    void testShrink() {
        // 入队大量元素触发扩容
        for (int i = 0; i < 100; i++) {
            queue.enqueue(i);
        }

        int initialCapacity = queue.getCapacity();
        assertTrue(initialCapacity >= 100);

        // 出队大部分元素，触发缩容
        for (int i = 0; i < 90; i++) {
            queue.dequeue();
        }

        // 验证缩容发生
        assertTrue(queue.getCapacity() < initialCapacity);
        assertEquals(10, queue.size());
    }

    @Test
    @DisplayName("测试泛型支持")
    void testGenericSupport() {
        ArrayQueue<String> stringQueue = new ArrayQueue<>();
        stringQueue.enqueue("hello");
        stringQueue.enqueue("world");

        assertEquals("hello", stringQueue.dequeue());
        assertEquals("world", stringQueue.dequeue());

        // 测试自定义对象
        class TestObject {

            private final int value;

            TestObject(int value) {
                this.value = value;
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof TestObject && ((TestObject) obj).value == this.value;
            }
        }

        ArrayQueue<TestObject> objectQueue = new ArrayQueue<>();
        TestObject obj1 = new TestObject(1);
        TestObject obj2 = new TestObject(2);

        objectQueue.enqueue(obj1);
        objectQueue.enqueue(obj2);

        assertEquals(obj1, objectQueue.dequeue());
        assertEquals(obj2, objectQueue.dequeue());
    }

    @Test
    @DisplayName("测试大量元素操作")
    void testLargeScaleOperations() {
        final int count = 10000;

        // 入队大量元素
        for (int i = 0; i < count; i++) {
            queue.enqueue(i);
        }

        assertEquals(count, queue.size());

        // 验证出队顺序正确
        for (int i = 0; i < count; i++) {
            assertEquals(i, queue.dequeue());
        }

        assertTrue(queue.isEmpty());
    }

    @Test
    @DisplayName("测试交替入队出队")
    void testAlternatingOperations() {
        // 测试交替操作
        for (int i = 0; i < 100; i++) {
            queue.enqueue(i);
            assertEquals(i, queue.dequeue());
        }

        assertTrue(queue.isEmpty());

        // 测试批量入队后批量出队
        for (int i = 0; i < 50; i++) {
            queue.enqueue(i);
        }

        for (int i = 0; i < 50; i++) {
            assertEquals(i, queue.dequeue());
        }

        assertTrue(queue.isEmpty());
    }
}