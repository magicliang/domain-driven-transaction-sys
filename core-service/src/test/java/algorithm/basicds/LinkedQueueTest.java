package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: LinkedQueue的单元测试类
 *
 * @author magicliang
 *
 *         date: 2025-08-12 19:26
 */
class LinkedQueueTest {

    private LinkedQueue<Integer> intQueue;
    private LinkedQueue<String> stringQueue;

    @BeforeEach
    void setUp() {
        intQueue = new LinkedQueue<>();
        stringQueue = new LinkedQueue<>();
    }

    @Test
    void testNewQueueIsEmpty() {
        assertTrue(intQueue.isEmpty());
        assertEquals(0, intQueue.size());
    }

    @Test
    void testEnqueueSingleElement() {
        intQueue.enqueue(42);
        assertFalse(intQueue.isEmpty());
        assertEquals(1, intQueue.size());
        assertEquals(42, intQueue.peek());
    }

    @Test
    void testEnqueueMultipleElements() {
        intQueue.enqueue(1);
        intQueue.enqueue(2);
        intQueue.enqueue(3);

        assertEquals(3, intQueue.size());
        assertEquals(1, intQueue.peek());
    }

    @Test
    void testDequeueSingleElement() {
        intQueue.enqueue(100);
        assertEquals(100, intQueue.dequeue());
        assertTrue(intQueue.isEmpty());
        assertEquals(0, intQueue.size());
    }

    @Test
    void testDequeueMultipleElements() {
        intQueue.enqueue(10);
        intQueue.enqueue(20);
        intQueue.enqueue(30);

        assertEquals(10, intQueue.dequeue());
        assertEquals(2, intQueue.size());
        assertEquals(20, intQueue.peek());

        assertEquals(20, intQueue.dequeue());
        assertEquals(1, intQueue.size());
        assertEquals(30, intQueue.peek());

        assertEquals(30, intQueue.dequeue());
        assertTrue(intQueue.isEmpty());
    }

    @Test
    void testPeekDoesNotRemoveElement() {
        intQueue.enqueue(99);
        assertEquals(99, intQueue.peek());
        assertEquals(1, intQueue.size());
        assertEquals(99, intQueue.peek()); // 再次peek应该返回相同值
    }

    @Test
    void testEnqueueNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> intQueue.enqueue(null));
    }

    @Test
    void testDequeueEmptyQueueThrowsException() {
        assertThrows(java.util.NoSuchElementException.class, () -> intQueue.dequeue());
    }

    @Test
    void testPeekEmptyQueueThrowsException() {
        assertThrows(java.util.NoSuchElementException.class, () -> intQueue.peek());
    }

    @Test
    void testStringQueue() {
        stringQueue.enqueue("Hello");
        stringQueue.enqueue("World");
        stringQueue.enqueue("Queue");

        assertEquals("Hello", stringQueue.dequeue());
        assertEquals("World", stringQueue.dequeue());
        assertEquals("Queue", stringQueue.dequeue());
        assertTrue(stringQueue.isEmpty());
    }

    @Test
    void testLargeNumberOfElements() {
        int count = 1000;
        for (int i = 0; i < count; i++) {
            intQueue.enqueue(i);
        }

        assertEquals(count, intQueue.size());

        for (int i = 0; i < count; i++) {
            assertEquals(i, intQueue.dequeue());
        }

        assertTrue(intQueue.isEmpty());
    }

    @Test
    void testAlternatingEnqueueDequeue() {
        intQueue.enqueue(1);
        assertEquals(1, intQueue.dequeue());

        intQueue.enqueue(2);
        intQueue.enqueue(3);
        assertEquals(2, intQueue.dequeue());

        intQueue.enqueue(4);
        assertEquals(3, intQueue.dequeue());
        assertEquals(4, intQueue.dequeue());

        assertTrue(intQueue.isEmpty());
    }

    @Test
    void testToString() {
        intQueue.enqueue(1);
        intQueue.enqueue(2);
        intQueue.enqueue(3);

        String expected = "[1, 2, 3]";
        assertEquals(expected, intQueue.toString());
    }

    @Test
    void testToStringEmptyQueue() {
        assertEquals("[]", intQueue.toString());
    }

    @Test
    void testToStringSingleElement() {
        intQueue.enqueue(42);
        assertEquals("[42]", intQueue.toString());
    }

    // 测试自定义对象
    static class Person {

        private final String name;
        private final int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return name + "(" + age + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Person person = (Person) obj;
            return age == person.age && name.equals(person.name);
        }
    }

    @Test
    void testCustomObjectQueue() {
        LinkedQueue<Person> personQueue = new LinkedQueue<>();
        Person alice = new Person("Alice", 25);
        Person bob = new Person("Bob", 30);

        personQueue.enqueue(alice);
        personQueue.enqueue(bob);

        assertEquals(alice, personQueue.dequeue());
        assertEquals(bob, personQueue.dequeue());
        assertTrue(personQueue.isEmpty());
    }
}