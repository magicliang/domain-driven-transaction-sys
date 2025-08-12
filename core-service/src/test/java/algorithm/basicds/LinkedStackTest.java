package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EmptyStackException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * LinkedStack的单元测试类
 */
class LinkedStackTest {

    private LinkedStack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new LinkedStack<>();
    }

    @Test
    @DisplayName("新创建的栈应该为空")
    void testNewStackIsEmpty() {
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("push单个元素后栈不应为空")
    void testPushSingleElement() {
        stack.push(42);
        assertFalse(stack.isEmpty());
        assertEquals(1, stack.size());
    }

    @Test
    @DisplayName("push多个元素后size应正确")
    void testPushMultipleElements() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.size());
    }

    @Test
    @DisplayName("pop应返回最后push的元素")
    void testPopReturnsLastPushedElement() {
        stack.push(1);
        stack.push(2);
        stack.push(3);

        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    @DisplayName("pop后size应减少")
    void testPopDecreasesSize() {
        stack.push(1);
        stack.push(2);

        stack.pop();
        assertEquals(1, stack.size());

        stack.pop();
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("peek应返回栈顶元素但不移除")
    void testPeekReturnsTopWithoutRemoval() {
        stack.push(1);
        stack.push(2);

        assertEquals(2, stack.peek());
        assertEquals(2, stack.size()); // size不应改变
        assertEquals(2, stack.peek()); // 再次peek应返回相同值
    }

    @Test
    @DisplayName("空栈pop应抛出EmptyStackException")
    void testPopEmptyStackThrowsException() {
        assertThrows(EmptyStackException.class, () -> stack.pop());
    }

    @Test
    @DisplayName("空栈peek应抛出EmptyStackException")
    void testPeekEmptyStackThrowsException() {
        assertThrows(EmptyStackException.class, () -> stack.peek());
    }

    @Test
    @DisplayName("push null应抛出IllegalArgumentException")
    void testPushNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> stack.push(null));
    }

    @Test
    @DisplayName("push和pop交替操作应正确")
    void testPushPopAlternating() {
        stack.push(1);
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());

        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    @DisplayName("测试泛型功能 - String类型")
    void testGenericFunctionalityWithString() {
        LinkedStack<String> stringStack = new LinkedStack<>();

        stringStack.push("hello");
        stringStack.push("world");

        assertEquals("world", stringStack.pop());
        assertEquals("hello", stringStack.pop());
        assertTrue(stringStack.isEmpty());
    }

    @Test
    @DisplayName("测试泛型功能 - 自定义对象类型")
    void testGenericFunctionalityWithCustomObject() {
        class TestObject {

            private final int value;

            TestObject(int value) {
                this.value = value;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null || getClass() != obj.getClass()) {
                    return false;
                }
                TestObject that = (TestObject) obj;
                return value == that.value;
            }
        }

        LinkedStack<TestObject> objectStack = new LinkedStack<>();
        TestObject obj1 = new TestObject(1);
        TestObject obj2 = new TestObject(2);

        objectStack.push(obj1);
        objectStack.push(obj2);

        assertEquals(obj2, objectStack.pop());
        assertEquals(obj1, objectStack.pop());
    }

    @Test
    @DisplayName("大量元素测试")
    void testLargeNumberOfElements() {
        final int count = 10000;

        for (int i = 0; i < count; i++) {
            stack.push(i);
        }

        assertEquals(count, stack.size());

        for (int i = count - 1; i >= 0; i--) {
            assertEquals(i, stack.pop());
        }

        assertTrue(stack.isEmpty());
    }

    @Test
    @DisplayName("isEmpty在push和pop后的状态变化")
    void testIsEmptyStateChanges() {
        assertTrue(stack.isEmpty());

        stack.push(1);
        assertFalse(stack.isEmpty());

        stack.pop();
        assertTrue(stack.isEmpty());
    }
}