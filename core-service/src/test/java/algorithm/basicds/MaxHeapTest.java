package algorithm.basicds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * MaxHeap类的单元测试类
 * <p>
 * 测试覆盖范围：
 * - 构造函数测试
 * - 基本堆操作测试（push, pop, peek）
 * - 合并操作测试
 * - TopK算法测试
 * - 边界条件测试
 * - 异常情况测试
 */
@DisplayName("MaxHeap单元测试")
class MaxHeapTest {

    private MaxHeap emptyHeap;
    private MaxHeap singleElementHeap;
    private MaxHeap multiElementHeap;

    @BeforeEach
    void setUp() {
        emptyHeap = new MaxHeap();

        singleElementHeap = new MaxHeap();
        singleElementHeap.push(42);

        multiElementHeap = new MaxHeap();
        int[] values = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        for (int val : values) {
            multiElementHeap.push(val);
        }
    }

    // ==================== 构造函数测试 ====================

    @Test
    @DisplayName("测试默认构造函数创建空堆")
    void testDefaultConstructor() {
        MaxHeap heap = new MaxHeap();
        assertTrue(heap.toList().isEmpty());
    }

    @Test
    @DisplayName("测试列表构造函数创建堆")
    void testListConstructor() {
        List<Integer> values = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6);
        MaxHeap heap = new MaxHeap(values);

        assertEquals(8, heap.toList().size());
        assertEquals(9, heap.peek()); // 最大堆的堆顶应该是最大值
    }

    @Test
    @DisplayName("测试空列表构造函数")
    void testEmptyListConstructor() {
        List<Integer> emptyList = new ArrayList<>();
        MaxHeap heap = new MaxHeap(emptyList);

        assertTrue(heap.toList().isEmpty());
    }

    // ==================== 基本堆操作测试 ====================

    @Test
    @DisplayName("测试push操作")
    void testPush() {
        emptyHeap.push(10);
        assertEquals(1, emptyHeap.toList().size());
        assertEquals(10, emptyHeap.peek());

        emptyHeap.push(20);
        assertEquals(2, emptyHeap.toList().size());
        assertEquals(20, emptyHeap.peek());

        emptyHeap.push(5);
        assertEquals(3, emptyHeap.toList().size());
        assertEquals(20, emptyHeap.peek()); // 最大值仍然是20
    }

    @Test
    @DisplayName("测试pop操作")
    void testPop() {
        assertEquals(9, multiElementHeap.pop());
        assertEquals(6, multiElementHeap.pop());
        assertEquals(5, multiElementHeap.pop());
    }

    @Test
    @DisplayName("测试空堆pop抛出异常")
    void testPopEmptyHeap() {
        assertThrows(IllegalStateException.class, () -> emptyHeap.pop());
    }

    @Test
    @DisplayName("测试peek操作")
    void testPeek() {
        assertEquals(9, multiElementHeap.peek());

        MaxHeap heap = new MaxHeap();
        heap.push(100);
        assertEquals(100, heap.peek());
    }

    @Test
    @DisplayName("测试空堆peek抛出异常")
    void testPeekEmptyHeap() {
        assertThrows(IndexOutOfBoundsException.class, () -> emptyHeap.peek());
    }

    // ==================== 工具方法测试 ====================

    @Test
    @DisplayName("测试left方法")
    void testLeft() {
        assertEquals(1, emptyHeap.left(0));
        assertEquals(3, emptyHeap.left(1));
        assertEquals(5, emptyHeap.left(2));
    }

    @Test
    @DisplayName("测试right方法")
    void testRight() {
        assertEquals(2, emptyHeap.right(0));
        assertEquals(4, emptyHeap.right(1));
        assertEquals(6, emptyHeap.right(2));
    }

    @Test
    @DisplayName("测试parent方法")
    void testParent() {
        assertEquals(0, emptyHeap.parent(1));
        assertEquals(0, emptyHeap.parent(2));
        assertEquals(1, emptyHeap.parent(3));
        assertEquals(1, emptyHeap.parent(4));
    }

    @Test
    @DisplayName("测试toList方法")
    void testToList() {
        List<Integer> list = multiElementHeap.toList();
        assertNotNull(list);
        assertEquals(10, list.size());

        // 验证列表包含所有插入的元素
        assertTrue(list.contains(1));
        assertTrue(list.contains(9));
        assertTrue(list.contains(5));
    }

    // ==================== 合并操作测试 ====================

    @Test
    @DisplayName("测试合并两个非空堆")
    void testMergeHeap() {
        MaxHeap heap1 = new MaxHeap(Arrays.asList(1, 3, 5));
        MaxHeap heap2 = new MaxHeap(Arrays.asList(2, 4, 6));

        MaxHeap merged = heap1.mergeHeap(heap2);

        assertEquals(6, merged.toList().size());
        assertEquals(6, merged.peek());
        assertSame(heap1, merged); // 验证返回的是当前堆实例
    }

    @Test
    @DisplayName("测试合并空堆")
    void testMergeEmptyHeap() {
        MaxHeap heap1 = new MaxHeap(Arrays.asList(1, 2, 3));
        MaxHeap empty = new MaxHeap();

        MaxHeap merged = heap1.mergeHeap(empty);

        assertEquals(3, merged.toList().size());
        assertEquals(3, merged.peek());
    }

    @Test
    @DisplayName("测试合并null堆抛出异常")
    void testMergeNullHeap() {
        assertThrows(IllegalArgumentException.class, () -> multiElementHeap.mergeHeap(null));
    }

    // ==================== TopK算法测试 ====================

    @Test
    @DisplayName("测试traversalTopK正常情况")
    void testTraversalTopK() {
        List<Integer> nums = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6);
        List<Integer> result = MaxHeap.traversalTopK(nums, 3);

        assertEquals(3, result.size());
        assertEquals(9, result.get(0));
        assertEquals(6, result.get(1));
        assertEquals(5, result.get(2));
    }

    @Test
    @DisplayName("测试traversalTopK边界条件")
    void testTraversalTopKBoundary() {
        List<Integer> nums = Arrays.asList(1, 2, 3);

        assertTrue(MaxHeap.traversalTopK(nums, 0).isEmpty());
        assertEquals(3, MaxHeap.traversalTopK(nums, 5).size());
        assertTrue(MaxHeap.traversalTopK(new ArrayList<>(), 3).isEmpty());
    }

    @Test
    @DisplayName("测试sortTopK正常情况")
    void testSortTopK() {
        List<Integer> nums = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6);
        List<Integer> result = MaxHeap.sortTopK(nums, 3);

        assertEquals(3, result.size());
        assertEquals(9, result.get(2));
        assertEquals(6, result.get(1));
        assertEquals(5, result.get(0));
    }

    @Test
    @DisplayName("测试sortTopK边界条件")
    void testSortTopKBoundary() {
        List<Integer> nums = Arrays.asList(1, 2, 3);

        assertTrue(MaxHeap.sortTopK(nums, 0).isEmpty());
        assertEquals(3, MaxHeap.sortTopK(nums, 5).size());
        assertTrue(MaxHeap.sortTopK(new ArrayList<>(), 3).isEmpty());
    }

    @Test
    @DisplayName("测试heapMinK正常情况")
    void testHeapMinK() {
        List<Integer> nums = Arrays.asList(7, 10, 4, 3, 20, 15);
        List<Integer> result = MaxHeap.heapMinK(nums, 3);

        assertEquals(3, result.size());
        assertTrue(result.contains(3));
        assertTrue(result.contains(4));
        assertTrue(result.contains(7));
    }

    @Test
    @DisplayName("测试heapMinK边界条件")
    void testHeapMinKBoundary() {
        List<Integer> nums = Arrays.asList(1, 2, 3);

        assertTrue(MaxHeap.heapMinK(nums, 0).isEmpty());
        assertEquals(3, MaxHeap.heapMinK(nums, 5).size());
        assertTrue(MaxHeap.heapMinK(new ArrayList<>(), 3).isEmpty());
        assertTrue(MaxHeap.heapMinK(null, 3).isEmpty());
    }

    @Test
    @DisplayName("测试minKWithPriorityQueue正常情况")
    void testMinKWithPriorityQueue() {
        int[] nums = {7, 10, 4, 3, 20, 15};
        Queue<Integer> result = MaxHeap.minKWithPriorityQueue(nums, 3);

        assertEquals(3, result.size());
        assertTrue(result.contains(3));
        assertTrue(result.contains(4));
        assertTrue(result.contains(7));
    }

    @Test
    @DisplayName("测试minKWithPriorityQueue边界条件")
    void testMinKWithPriorityQueueBoundary() {
        int[] nums = {1, 2, 3};

        assertEquals(0, MaxHeap.minKWithPriorityQueue(nums, 0).size());
        assertEquals(3, MaxHeap.minKWithPriorityQueue(nums, 5).size());
        assertEquals(0, MaxHeap.minKWithPriorityQueue(new int[]{}, 3).size());

        assertThrows(IllegalArgumentException.class, () -> MaxHeap.minKWithPriorityQueue(null, 3));
        assertThrows(IllegalArgumentException.class, () -> MaxHeap.minKWithPriorityQueue(nums, -1));
    }

    @Test
    @DisplayName("测试topK正常情况")
    void testTopK() {
        int[] nums = {7, 10, 4, 3, 20, 15};
        Queue<Integer> result = multiElementHeap.topK(nums, 3);

        assertEquals(3, result.size());
        assertTrue(result.contains(20));
        assertTrue(result.contains(15));
        assertTrue(result.contains(10));
    }

    @Test
    @DisplayName("测试topK边界条件")
    void testTopKBoundary() {
        int[] nums = {1, 2, 3};

        assertEquals(0, multiElementHeap.topK(nums, 0).size());
        assertEquals(3, multiElementHeap.topK(nums, 5).size());
        assertEquals(0, multiElementHeap.topK(new int[]{}, 3).size());

        assertThrows(IllegalArgumentException.class, () -> multiElementHeap.topK(null, 3));
        assertThrows(IllegalArgumentException.class, () -> multiElementHeap.topK(nums, -1));
    }

    // ==================== 性能测试 ====================

    @Test
    @DisplayName("测试大数组操作")
    void testLargeArrayOperations() {
        MaxHeap heap = new MaxHeap();
        int size = 10000;

        // 测试大量插入
        for (int i = 0; i < size; i++) {
            heap.push(i);
        }
        assertEquals(size, heap.toList().size());
        assertEquals(size - 1, heap.peek());

        // 测试大量删除
        for (int i = 0; i < 1000; i++) {
            heap.pop();
        }
        assertEquals(size - 1000, heap.toList().size());
    }

    // ==================== 堆性质验证测试 ====================

    @Test
    @DisplayName("验证最大堆性质")
    void testMaxHeapProperty() {
        MaxHeap heap = new MaxHeap(Arrays.asList(1, 3, 5, 7, 9, 2, 4, 6, 8));

        List<Integer> list = heap.toList();
        for (int i = 0; i < list.size(); i++) {
            int left = heap.left(i);
            int right = heap.right(i);

            if (left < list.size()) {
                assertTrue(list.get(i) >= list.get(left));
            }
            if (right < list.size()) {
                assertTrue(list.get(i) >= list.get(right));
            }
        }
    }
}