package algorithm.basicds;


import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 基于数组的完全二叉树（complete binary tree）的堆实现
 * <p>
 * 这个类型的成员方法的 i 都是指的元素的索引
 *
 * @author magicliang
 *
 *         date: 2025-08-18 19:12
 */
public class MaxHeap {

    /**
     * 堆的内部存储，为了不解决扩容的问题，这里偷懒使用 list
     */
    private List<Integer> heap;

    public int left(int i) {
        return 2 * i + 1;
    }

    public int right(int i) {
        return 2 * i + 2;
    }

    public int parent(int i) {
        // 堆的性质告诉我们怎样让2个数变成1个数
        return (i - 1) / 2;
    }

    public int peek() {
        return heap.get(0);
    }

    public void push(int val) {
        heap.add(val);
        siftUp(heap.size() - 1);
    }

    public int pop() {
        if (heap.isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }

        swap(0, heap.size() - 1);
        int result = heap.remove(heap.size() - 1);
        siftDown(0);

        return result;
    }

    void siftUp(int i) {
        // 要考虑 i 已经是堆顶的情况
        if (i < 0 || i >= heap.size()) {
            return;
        }

        // 如果违反最大堆性质，则交换两个节点，堆操作满足性质以后会自动退出循环
        while (i != 0 && heap.get(i) > heap.get(parent(i))) {
            // i != 0 说明i还有取 parent 的余地
            int p = parent(i);
            swap(i, p);
            i = p; // 最后一步 swap 完会导致 i 变成 0
        }
    }

    void siftDown(int i) {
        if (i < 0 || i >= heap.size()) {
            return;
        }

        int size = heap.size();
        // i 还在合法的范围里，i往下走走到头就退出循环，或者堆的性质得到满足则退出循环
        while (i < size) {
            int l = left(i);
            int r = right(i);

            // 注意，不是先跟l比较，l比较大就交换l，最大堆的意思是，i节点的左右子节点中，最大的那个节点跟i交换

            int largest = l;

            if (l < size && heap.get(l) > heap.get(largest)) {
                largest = l;
            }
            if (r < size && heap.get(r) > heap.get(largest)) {
                largest = r;
            }

            // 不可交换意味着 l r可能都超标，或者都满足性质
            if (l == r) {
                // 性质已满足，不再下移
                break;
            }

            swap(i, largest);
            i = largest;
        }
    }

    private void swap(int a, int b) {
        Integer tmp = heap.get(a);
        heap.set(a, heap.get(b));
        heap.set(b, tmp);
    }
}

