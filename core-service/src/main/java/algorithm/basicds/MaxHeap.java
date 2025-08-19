package algorithm.basicds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

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
    private final List<Integer> heap;

    /**
     * 默认构造函数，创建一个空的最大堆
     */
    public MaxHeap() {
        heap = new ArrayList<>();
    }

//    public MaxHeap(List<Integer> queue) {
//       this();
//       // 这个算法时间复杂度是 O(nlgn)
//       for (Integer i : queue) {
//           push(i);
//       }
//    }

    /**
     * Floyd建堆算法
     * <p>
     * 使用给定的列表构建最大堆，时间复杂度为O(n)
     *
     * @param queue 用于构建堆的整数列表
     */
    public MaxHeap(List<Integer> queue) {
        this();
        this.heap.addAll(queue);
        // 这个算法时间复杂度是 O(n)。小技巧，最后一个叶子结点的父是第一个父亲，所有父亲之后的节点已经是一个最大堆的根节点了，我们要确定父节点和它们的孩子是不是最大堆
        for (int i = parent(heap.size() - 1); i >= 0; i--) {
            siftDown(i);
        }
    }

    /**
     * 方法一：遍历选择（直接实现）
     * <p>
     * 通过遍历查找最大的k个元素，时间复杂度为O(n*k)
     *
     * @param nums 输入的整数列表
     * @param k 需要返回的最大元素个数
     * @return 包含最大的k个元素的列表
     */
    public static List<Integer> traversalTopK(List<Integer> nums, int k) {
        List<Integer> result = new ArrayList<>();
        if (k <= 0 || nums.isEmpty()) {
            return result;
        }

        List<Integer> copy = new ArrayList<>(nums); // 复制原始数据
        for (int i = 0; i < k; i++) {
            if (copy.isEmpty()) {
                break;
            }

            // 遍历寻找最大值
            int maxIndex = 0;
            for (int j = 1; j < copy.size(); j++) {
                if (copy.get(j) > copy.get(maxIndex)) {
                    maxIndex = j;
                }
            }
            // 将最大值移到结果集并从副本中移除
            result.add(copy.get(maxIndex));
            copy.remove(maxIndex);
        }
        return result;
    }

    /**
     * 方法二：排序选择（直接实现）
     * <p>
     * 通过排序后选择最大的k个元素，时间复杂度为O(n*log(n))
     *
     * @param nums 输入的整数列表
     * @param k 需要返回的最大元素个数
     * @return 包含最大的k个元素的列表
     */
    public static List<Integer> sortTopK(List<Integer> nums, int k) {
        if (k <= 0) {
            return new ArrayList<>();
        }
        if (k >= nums.size()) {
            return new ArrayList<>(nums);
        }

        // 创建副本并排序（升序）
        List<Integer> copy = new ArrayList<>(nums);
        Collections.sort(copy);

        // 取最大的k个元素（从末尾开始）
        return copy.subList(copy.size() - k, copy.size());
    }

    /**
     * 使用最大堆查找最小的k个元素
     * <p>
     * 维护一个大小为k的最大堆，堆顶元素为当前k个最小元素中的最大值
     * 遍历数组，当遇到比堆顶小的元素时替换堆顶并调整堆
     * 时间复杂度为O(n*log(k))
     *
     * @param list 输入的整数列表
     * @param k 需要返回的最小元素个数
     * @return 包含最小的k个元素的列表
     */
    public static List<Integer> heapMinK(List<Integer> list, int k) {
        List<Integer> result = new ArrayList<>();
        if (list == null || list.isEmpty() || k <= 0) {
            return result;
        }

        // 如果k大于等于列表大小，返回所有元素
        if (k >= list.size()) {
            return new ArrayList<>(list);
        }

        // 先把前k个数放进最大堆里，这k个数就是当前队列最小的k个数，其中堆顶是这k个数的边界
        MaxHeap maxHeap = new MaxHeap();
        for (int i = 0; i < k; i++) {
            maxHeap.push(list.get(i));
        }

        for (int j = k; j < list.size(); j++) {
            int current = list.get(j);

            // 确保堆不为空再进行比较
            if (!maxHeap.toList().isEmpty() && current < maxHeap.peek()) {
                maxHeap.pop();
                maxHeap.push(current);
            }
        }

        return maxHeap.toList();
    }

    /**
     * 使用Java原生PriorityQueue实现用最大堆查找最小的k个元素
     * <p>
     * 通过传入自定义Comparator将PriorityQueue配置为最大堆
     * 维护一个大小为k的最大堆，堆顶元素为当前k个最小元素中的最大值
     * 遍历数组，当遇到比堆顶小的元素时替换堆顶并调整堆
     * 时间复杂度为O(n*log(k))
     * <p>
     * 注意：此方法使用Java内置的PriorityQueue（通过Comparator实现最大堆）实现
     *
     * @param nums 输入的整数数组
     * @param k 需要返回的最小元素个数
     * @return 包含最小的k个元素的优先队列（最大堆）
     * @throws IllegalArgumentException 如果nums为null或k为负数
     */
    public static Queue<Integer> minKWithPriorityQueue(int[] nums, int k) {
        if (nums == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }
        if (k < 0) {
            throw new IllegalArgumentException("k must be non-negative");
        }
        if (k == 0) {
            return new PriorityQueue<>(Collections.reverseOrder());
        }

        // 使用Collections.reverseOrder()创建最大堆
        Queue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        // 先把前k个元素放进最大堆里
        int limit = Math.min(k, nums.length);
        for (int i = 0; i < limit; i++) {
            maxHeap.offer(nums[i]);
        }

        // 如果k大于数组长度，直接返回包含所有元素的堆
        if (k >= nums.length) {
            return maxHeap;
        }

        // 处理剩余的元素
        for (int i = k; i < nums.length; i++) {
            if (nums[i] < maxHeap.peek()) {
                maxHeap.poll();
                maxHeap.offer(nums[i]);
            }
        }

        return maxHeap;
    }

    /**
     * 合并两个最大堆
     * <p>
     * 将另一个最大堆的所有元素合并到当前堆中，使用Floyd建堆算法（从最后一个非叶子节点开始下沉）重新构建堆结构
     * 时间复杂度为O(n)，其中n是两个堆的总元素数
     * <p>
     * 注意：此方法会修改当前堆，不会创建新的堆实例
     *
     * @param maxHeap 要合并的另一个最大堆
     * @return 返回合并后的当前堆实例
     * @throws IllegalArgumentException 如果maxHeap为null
     */
    public MaxHeap mergeHeap(MaxHeap maxHeap) {
        if (maxHeap == null) {
            throw new IllegalArgumentException("Cannot merge with null heap");
        }

        if (maxHeap.heap.isEmpty()) {
            return this; // 如果另一个堆为空，直接返回当前堆
        }

        this.heap.addAll(maxHeap.toList());
        int size = this.heap.size();
        for (int i = parent(size - 1); i >= 0; i--) {
            siftDown(i);
        }
        return this;
    }

    /**
     * 使用最小堆查找最大的k个元素
     * <p>
     * 维护一个大小为k的最小堆，堆顶元素为当前k个最大元素中的最小值
     * 遍历数组，当遇到比堆顶大的元素时替换堆顶并调整堆
     * 时间复杂度为O(n*log(k))
     * <p>
     * 注意：此方法使用Java内置的PriorityQueue（最小堆）实现
     *
     * @param nums 输入的整数数组
     * @param k 需要返回的最大元素个数
     * @return 包含最大的k个元素的优先队列（最小堆）
     * @throws IllegalArgumentException 如果nums为null或k为负数
     */
    public Queue<Integer> topK(int[] nums, int k) {
        if (nums == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }
        if (k < 0) {
            throw new IllegalArgumentException("k must be non-negative");
        }
        if (k == 0) {
            return new PriorityQueue<>();
        }

        // java 默认使用最小堆
        Queue<Integer> heap = new PriorityQueue<>();

        // 先把前k个元素放进最小堆里，当作当前最大的k个元素，其中堆顶是k个元素中的最小值
        int limit = Math.min(k, nums.length);
        for (int i = 0; i < limit; i++) {
            heap.offer(nums[i]);
        }

        // 如果k大于数组长度，直接返回包含所有元素的堆
        if (k >= nums.length) {
            return heap;
        }

        // 处理剩余的元素
        for (int i = k; i < nums.length; i++) {
            if (nums[i] > heap.peek()) {
                heap.poll();
                heap.offer(nums[i]);
            }
        }

        return heap;
    }

    /**
     * 将堆转换为列表形式返回
     *
     * @return 包含堆中所有元素的列表
     */
    public List<Integer> toList() {
        return heap;
    }

    /**
     * 获取左子节点的索引
     *
     * @param i 父节点的索引
     * @return 左子节点的索引
     */
    public int left(int i) {
        return 2 * i + 1;
    }

    /**
     * 获取右子节点的索引
     *
     * @param i 父节点的索引
     * @return 右子节点的索引
     */
    public int right(int i) {
        return 2 * i + 2;
    }

    /**
     * 获取父节点的索引
     *
     * @param i 子节点的索引
     * @return 父节点的索引
     */
    public int parent(int i) {
        // 堆的性质告诉我们怎样让2个数变成1个数
        return (i - 1) / 2;
    }

    /**
     * 查看堆顶元素（最大值）
     *
     * @return 堆顶的最大元素
     * @throws IndexOutOfBoundsException 如果堆为空
     */
    public int peek() {
        if (heap.isEmpty()) {
            throw new IndexOutOfBoundsException("Heap is empty");
        }
        return heap.get(0);
    }

    /**
     * 向堆中插入一个新元素
     * <p>
     * 将元素添加到堆的末尾，然后通过上浮操作恢复堆的性质
     *
     * @param val 要插入的整数值
     */
    public void push(int val) {
        heap.add(val);
        siftUp(heap.size() - 1);
    }

    /**
     * 移除并返回堆顶元素（最大值）
     * <p>
     * 将堆顶元素与最后一个元素交换，移除堆顶，然后通过下沉操作恢复堆的性质
     *
     * @return 堆顶的最大元素
     * @throws IllegalStateException 如果堆为空
     */
    public int pop() {
        if (heap.isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }

        swap(0, heap.size() - 1);
        int result = heap.remove(heap.size() - 1);
        siftDown(0);

        return result;
    }

    /**
     * 上浮操作，用于恢复堆的性质
     * <p>
     * 从给定的索引开始，向上比较并交换元素，直到满足最大堆性质
     *
     * @param i 开始上浮的索引
     */
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

    /**
     * 下沉操作，用于恢复堆的性质
     * <p>
     * 从给定的索引开始，向下比较并交换元素，直到满足最大堆性质
     *
     * @param i 开始下沉的索引
     */
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

            int largest = i;

            if (l < size && heap.get(l) > heap.get(largest)) {
                largest = l;
            }
            if (r < size && heap.get(r) > heap.get(largest)) {
                largest = r;
            }

            // 不可交换意味着 l r可能都超标，或者都满足性质
            if (largest == i) {
                // 性质已满足，不再下移
                break;
            }

            swap(i, largest);
            i = largest;
        }
    }

    /**
     * 交换堆中两个位置的元素
     *
     * @param a 第一个元素的索引
     * @param b 第二个元素的索引
     */
    private void swap(int a, int b) {
        Integer tmp = heap.get(a);
        heap.set(a, heap.get(b));
        heap.set(b, tmp);
    }
}

