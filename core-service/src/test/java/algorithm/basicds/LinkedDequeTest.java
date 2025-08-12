package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: LinkedDeque的单元测试类
 *
 * @author magicliang
 *
 *         date: 2025-08-12 19:38
 */
class LinkedDequeTest {

    private LinkedDeque<Integer> deque;

    @BeforeEach
    void setUp() {
        deque = new LinkedDeque<>();
    }

    @Test
    void testAddFirstAndRemoveFirst() {
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);

        assertEquals(3, deque.removeFirst());
        assertEquals(2, deque.removeFirst());
        assertEquals(1, deque.removeFirst());
        assertTrue(deque.isEmpty());
    }

    @Test
    void testAddLastAndRemoveLast() {
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);

        assertEquals(3, deque.removeLast());
        assertEquals(2, deque.removeLast());
        assertEquals(1, deque.removeLast());
        assertTrue(deque.isEmpty());
    }

    @Test
    void testMixedOperations() {
        deque.addFirst(1);
        deque.addLast(2);
        deque.addFirst(0);
        deque.addLast(3);

        assertEquals(4, deque.size());
        assertEquals(0, deque.removeFirst());
        assertEquals(3, deque.removeLast());
        assertEquals(1, deque.removeFirst());
        assertEquals(2, deque.removeFirst());
    }

    @Test
    void testPeekOperations() {
        deque.addFirst(1);
        deque.addLast(2);

        assertEquals(1, deque.peekFirst());
        assertEquals(2, deque.peekLast());
        assertEquals(2, deque.size()); // 元素未被移除
    }

    @Test
    void testEmptyDequeOperations() {
        assertTrue(deque.isEmpty());
        assertEquals(0, deque.size());

        assertThrows(NoSuchElementException.class, () -> deque.removeFirst());
        assertThrows(NoSuchElementException.class, () -> deque.removeLast());
        assertThrows(NoSuchElementException.class, () -> deque.peekFirst());
        assertThrows(NoSuchElementException.class, () -> deque.peekLast());
    }

    @Test
    void testNullItemHandling() {
        assertThrows(IllegalArgumentException.class, () -> deque.addFirst(null));
        assertThrows(IllegalArgumentException.class, () -> deque.addLast(null));
    }

    @Test
    void testQueueOperations() {
        deque.enqueue(1);
        deque.enqueue(2);
        deque.enqueue(3);

        assertEquals(1, deque.dequeue());
        assertEquals(2, deque.dequeue());
        assertEquals(3, deque.peek());
        assertEquals(3, deque.dequeue());
    }

    @Test
    void testStackOperations() {
        deque.push(1);
        deque.push(2);
        deque.push(3);

        assertEquals(3, deque.pop());
        assertEquals(2, deque.pop());
        assertEquals(1, deque.peek());
        assertEquals(1, deque.pop());
    }

    @Test
    void testToString() {
        deque.addLast(1);
        deque.addLast(2);
        deque.addFirst(0);

        assertEquals("[0, 1, 2]", deque.toString());
    }

    @Test
    void testLargeScaleOperations() {
        for (int i = 0; i < 1000; i++) {
            deque.addLast(i);
        }

        assertEquals(1000, deque.size());

        for (int i = 0; i < 500; i++) {
            assertEquals(i, deque.removeFirst());
        }

        assertEquals(500, deque.size());

        for (int i = 999; i >= 500; i--) {
            assertEquals(i, deque.removeLast());
        }

        assertTrue(deque.isEmpty());
    }

    @Test
    void testAlternatingOperations() {
        // 测试交替操作
        deque.addFirst(1);
        deque.addLast(2);
        deque.addFirst(0);
        deque.addLast(3);

        assertEquals(0, deque.removeFirst());
        assertEquals(3, deque.removeLast());
        assertEquals(1, deque.removeFirst());
        assertEquals(2, deque.removeLast());

        assertTrue(deque.isEmpty());
    }

    @Test
    void testGenericSupport() {
        LinkedDeque<String> stringDeque = new LinkedDeque<>();
        stringDeque.addLast("hello");
        stringDeque.addLast("world");

        assertEquals("hello", stringDeque.removeFirst());
        assertEquals("world", stringDeque.removeLast());

        LinkedDeque<Double> doubleDeque = new LinkedDeque<>();
        doubleDeque.addFirst(3.14);
        doubleDeque.addLast(2.71);

        assertEquals(3.14, doubleDeque.removeFirst());
        assertEquals(2.71, doubleDeque.removeLast());
    }
}