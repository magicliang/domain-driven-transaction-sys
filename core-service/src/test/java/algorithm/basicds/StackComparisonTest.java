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
 * ArrayStack和LinkedStack的对比测试类
 */
class StackComparisonTest {

    private ArrayStack<Integer> arrayStack;
    private LinkedStack<Integer> linkedStack;

    @BeforeEach
    void setUp() {
        arrayStack = new ArrayStack<>();
        linkedStack = new LinkedStack<>();
    }

    @Test
    @DisplayName("两个栈的基本功能应该一致")
    void testBasicFunctionalityConsistency() {
        // 测试空栈状态
        assertTrue(arrayStack.isEmpty());
        assertTrue(linkedStack.isEmpty());
        assertEquals(0, arrayStack.size());
        assertEquals(0, linkedStack.size());

        // 测试push操作
        arrayStack.push(1);
        linkedStack.push(1);
        assertFalse(arrayStack.isEmpty());
        assertFalse(linkedStack.isEmpty());
        assertEquals(1, arrayStack.size());
        assertEquals(1, linkedStack.size());

        // 测试peek操作
        assertEquals(arrayStack.peek(), linkedStack.peek());

        // 测试pop操作
        assertEquals(arrayStack.pop(), linkedStack.pop());
        assertTrue(arrayStack.isEmpty());
        assertTrue(linkedStack.isEmpty());
    }

    @Test
    @DisplayName("两个栈的异常行为应该一致")
    void testExceptionBehaviorConsistency() {
        // 测试空栈异常
        assertThrows(EmptyStackException.class, () -> arrayStack.pop());
        assertThrows(EmptyStackException.class, () -> linkedStack.pop());
        assertThrows(EmptyStackException.class, () -> arrayStack.peek());
        assertThrows(EmptyStackException.class, () -> linkedStack.peek());

        // 测试null元素异常
        assertThrows(IllegalArgumentException.class, () -> arrayStack.push(null));
        assertThrows(IllegalArgumentException.class, () -> linkedStack.push(null));
    }

    @Test
    @DisplayName("两个栈的LIFO行为应该一致")
    void testLIFOBehaviorConsistency() {
        int[] testData = {1, 2, 3, 4, 5};

        // 同时push相同数据
        for (int value : testData) {
            arrayStack.push(value);
            linkedStack.push(value);
        }

        // 验证pop顺序一致
        for (int i = testData.length - 1; i >= 0; i--) {
            assertEquals(testData[i], arrayStack.pop());
            assertEquals(testData[i], linkedStack.pop());
        }
    }

    @Test
    @DisplayName("测试ArrayStack的扩容功能")
    void testArrayStackResize() {
        // 测试默认容量10的扩容
        for (int i = 0; i < 15; i++) {
            arrayStack.push(i);
        }
        assertEquals(15, arrayStack.size());

        // 验证pop顺序正确
        for (int i = 14; i >= 0; i--) {
            assertEquals(i, arrayStack.pop());
        }
    }

    @Test
    @DisplayName("测试ArrayStack的自定义容量")
    void testArrayStackCustomCapacity() {
        ArrayStack<String> customStack = new ArrayStack<>(5);

        // 测试5个元素不扩容
        for (int i = 0; i < 5; i++) {
            customStack.push("item" + i);
        }
        assertEquals(5, customStack.size());

        // 测试第6个元素触发扩容
        customStack.push("item5");
        assertEquals(6, customStack.size());
    }

    @Test
    @DisplayName("两个栈的泛型功能应该一致")
    void testGenericFunctionality() {
        // 测试String类型
        ArrayStack<String> arrayStringStack = new ArrayStack<>();
        LinkedStack<String> linkedStringStack = new LinkedStack<>();

        arrayStringStack.push("hello");
        linkedStringStack.push("hello");

        assertEquals(arrayStringStack.pop(), linkedStringStack.pop());

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

        ArrayStack<TestObject> arrayObjStack = new ArrayStack<>();
        LinkedStack<TestObject> linkedObjStack = new LinkedStack<>();

        TestObject obj = new TestObject(42);
        arrayObjStack.push(obj);
        linkedObjStack.push(obj);

        assertEquals(arrayObjStack.pop(), linkedObjStack.pop());
    }

    @Test
    @DisplayName("测试大量元素操作的一致性")
    void testLargeScaleOperations() {
        final int count = 1000;

        // 同时push大量元素
        for (int i = 0; i < count; i++) {
            arrayStack.push(i);
            linkedStack.push(i);
        }

        assertEquals(count, arrayStack.size());
        assertEquals(count, linkedStack.size());

        // 验证pop顺序一致
        for (int i = count - 1; i >= 0; i--) {
            assertEquals(i, arrayStack.pop());
            assertEquals(i, linkedStack.pop());
        }

        assertTrue(arrayStack.isEmpty());
        assertTrue(linkedStack.isEmpty());
    }

    @Test
    @DisplayName("测试边界条件的一致性")
    void testEdgeCases() {
        // 测试单个元素
        arrayStack.push(42);
        linkedStack.push(42);

        assertEquals(42, arrayStack.peek());
        assertEquals(42, linkedStack.peek());
        assertEquals(42, arrayStack.pop());
        assertEquals(42, linkedStack.pop());

        // 测试push后立即pop
        for (int i = 0; i < 10; i++) {
            arrayStack.push(i);
            linkedStack.push(i);

            assertEquals(i, arrayStack.pop());
            assertEquals(i, linkedStack.pop());
        }

        assertTrue(arrayStack.isEmpty());
        assertTrue(linkedStack.isEmpty());
    }
}