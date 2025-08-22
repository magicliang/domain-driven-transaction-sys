package algorithm.sort;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 快速排序实现
 * 使用递归分治策略，通过 pivot 分割区间，逐步将每个元素归位
 *
 * @author magicliang
 *
 *         date: 2025-08-21 16:00
 */
public class QuickSort {

    /**
     * 快速排序入口方法：对整个数组进行原地快速排序
     * 使用递归分治策略，通过 pivot 分割区间，逐步将每个元素归位
     *
     * @param arr 待排序的整型数组（允许 null 或空）
     * @return 排序后的原数组（null 或已排序）
     */
    public static int[] quickSort(int[] arr) {
        // 将全局排序委托给区间版本：[0, length-1]
        return quickSort(arr, 0, arr.length - 1);
    }

    /**
     * 快速排序递归主函数：对子数组 [begin, end] 进行排序
     * 实现原地（in-place）排序，不额外分配空间
     * <p>
     * 核心流程：
     * 1. 分区（partition）：选出一个 pivot，将其放置到最终有序位置 pivotal
     * 2. 分治（divide & conquer）：
     * - 递归排序左半部分 [begin, pivotal-1]
     * - 递归排序右半部分 [pivotal+1, end]
     * <p>
     * 关键设计：
     * - 使用 begin >= end 作为递归终止条件，防止 pivotal±1 导致的无限递归
     * - 所有操作均在原数组上进行，空间复杂度 O(log n)（仅递归栈）
     *
     * @param arr 待排序数组
     * @param begin 当前排序区间的起始索引（包含）
     * @param end 当前排序区间的结束索引（包含）
     * @return 排序完成后的原数组引用
     */
    public static int[] quickSort(int[] arr, int begin, int end) {
        // ======== 递归终止条件：无需排序的情况 ========
        // 以下情况直接返回，不再递归：
        // 1. 数组为空或未初始化
        // 2. 区间无效（begin >= end）——这是 pivotal±1 不越界崩溃的关键防护！
        //    - pivotal-1 可能 < begin（如 pivotal == begin）
        //    - pivotal+1 可能 > end（如 pivotal == end）
        //    若无此判断，将导致无限递归 → StackOverflowError
        if (arr == null || begin >= end) {
            return arr;
        }

        // ======== 分治三步走 ========
        // 1. 分区：将当前区间划分为左小右大两部分，并返回 pivot 的最终位置
        int pivotal = partition(arr, begin, end);

        // 2. 递归处理左半部分：[begin, pivotal - 1]
        //    注意：pivotal 已就位，不再参与排序
        quickSort(arr, begin, pivotal - 1);

        // 3. 递归处理右半部分：[pivotal + 1, end]
        //    同样跳过 pivot，只排右边
        quickSort(arr, pivotal + 1, end);

        // 所有递归完成后，整个数组自然有序
        return arr;
    }

    /**
     * 为了防止栈帧空间的累积，我们可以在每轮哨兵排序完成后，比较两个子数组的长度，仅对较短的子数组进行递归。
     * 由于较短子数组的长度不会超过 n/2，因此这种方法能确保递归深度不超过 logn
     *
     * 尾递归优化的快速排序实现：
     * - 使用迭代+递归混合模式，将递归深度控制在O(log n)
     * - 每次只递归处理较短的子区间，避免栈溢出
     * - 较长的子区间通过迭代处理，减少栈帧创建
     *
     * 算法流程：
     * 1. 使用while循环替代纯递归，处理当前区间
     * 2. 每次分区后，比较左右子区间长度
     * 3. 递归处理较短的子区间（保证递归深度）
     * 4. 通过修改begin/end迭代处理较长的子区间
     *
     * 时间复杂度：平均O(n log n)，最坏O(n²)（但概率极低，因使用三数取中法）
     * 空间复杂度：O(log n)（栈空间，最坏情况）
     *
     * @param arr 待排序数组
     * @param begin 当前排序区间的起始索引（包含）
     * @param end 当前排序区间的结束索引（包含）
     * @return 排序完成后的原数组引用
     */
    public static int[] quickSortShortest(int[] arr, int begin, int end) {
        // ======== 递归终止条件：无需排序的情况 ========
        // 以下情况直接返回，不再递归：
        // 1. 数组为空或未初始化
        // 2. 区间无效（begin >= end）——这是 pivotal±1 不越界崩溃的关键防护！
        //    - pivotal-1 可能 < begin（如 pivotal == begin）
        //    - pivotal+1 可能 > end（如 pivotal == end）
        //    若无此判断，将导致无限递归 → StackOverflowError
        if (arr == null || begin >= end) {
            return arr;
        }

        // 递归排序要像二分一样，迭代处理一个区间
        while (begin < end) {
            // 每一轮查找开始，会重新计算 pivotal
            int pivotal = partitionMedian(arr, begin, end); // 使用 partition3
            // 只排序短区间
            if (pivotal - begin < end - pivotal) {
                quickSortShortest(arr, begin, pivotal - 1);
                // 左区间排好，收窄左区间，进入下一轮循环
                begin = pivotal + 1;
            } else {
                quickSortShortest(arr, pivotal + 1, end);
                // 右区间排好，收窄右区间，进入下一轮循环
                end = pivotal - 1;
            }
        }

        // 当二分结束以后，所有子区间都排好序了
        return arr;

    }

    /**
     * 使用partition2的快速排序入口方法
     * 基于Hoare分区方案的快速排序实现
     *
     * @param arr 待排序的整型数组（允许 null 或空）
     * @return 排序后的原数组（null 或已排序）
     */
    public static int[] quickSort2(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr;
        }
        return quickSort2(arr, 0, arr.length - 1);
    }

    /**
     * 使用partition2的快速排序递归主函数
     * 基于Hoare分区方案的快速排序实现
     *
     * @param arr 待排序数组
     * @param begin 当前排序区间的起始索引（包含）
     * @param end 当前排序区间的结束索引（包含）
     * @return 排序完成后的原数组引用
     */
    public static int[] quickSort2(int[] arr, int begin, int end) {
        if (arr == null || begin >= end) {
            return arr;
        }

        // 使用partition2进行分区
        int pivotal = partition2(arr, begin, end);

        // 递归处理左半部分：[begin, pivotal-1]
        quickSort2(arr, begin, pivotal - 1);

        // 递归处理右半部分：[pivotal+1, end]
        quickSort2(arr, pivotal + 1, end);

        return arr;
    }

    /**
     * 使用partition3的快速排序入口方法
     * 基于三数取中法和Hoare分区方案的高效快速排序实现
     *
     * 特点：
     * - 使用三数取中法选择pivot，避免最坏情况
     * - 使用Hoare分区方案，减少交换次数
     * - 对于大数据集有更好的性能表现
     *
     * @param arr 待排序的整型数组（允许 null 或空）
     * @return 排序后的原数组（null 或已排序）
     */
    public static int[] quickSort3(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr;
        }
        return quickSort3(arr, 0, arr.length - 1);
    }

    /**
     * 使用partition3的快速排序递归主函数
     * 基于三数取中法和Hoare分区方案的高效快速排序实现
     *
     * @param arr 待排序数组
     * @param begin 当前排序区间的起始索引（包含）
     * @param end 当前排序区间的结束索引（包含）
     * @return 排序完成后的原数组引用
     */
    public static int[] quickSort3(int[] arr, int begin, int end) {
        if (arr == null || begin >= end) {
            return arr;
        }

        // 使用partition3进行分区（三数取中法+Hoare分区）
        int pivotal = partitionMedian(arr, begin, end);

        // 递归处理左半部分：[begin, pivotal-1]
        quickSort3(arr, begin, pivotal - 1);

        // 递归处理右半部分：[pivotal+1, end]
        quickSort3(arr, pivotal + 1, end);

        return arr;
    }

    /**
     * 快速排序的核心分区操作：Lomuto 分区方案
     * 将子数组 [begin, end] 按 pivot 值划分为：
     * [ 小于 pivot ]  [ pivot ]  [ 大于等于 pivot ]
     * 并返回 pivot 的最终位置（即"已排好序的中间节点"）
     * <p>
     * 与归并排序不同，快排是"先定位，再分治"：
     * - 先确定一个元素的最终位置（锚点）
     * - 再递归处理其左右区间
     * <p>
     * 使用 Lomuto 方案（以末尾元素为 pivot），逻辑清晰，易于理解
     *
     * @param arr 待分区的数组
     * @param begin 当前处理区间的起始索引（包含）
     * @param end 当前处理区间的结束索引（包含）
     * @return pivot 元素排序后所在的索引位置（分割点）
     */
    static int partition(int[] arr, int begin, int end) {
        // 特殊情况：区间只有一个元素，无需分区，直接返回其位置
        if (begin == end) {
            return begin;
        }

        // 【选择基准】以最后一个元素作为 pivot（目标值），这样开闭区间的伸缩就从第一个元素开始
        int target = arr[end];

        // 【初始化双游标】
        // i：指向"小于 pivot"区域的最后一个元素的索引
        //    初始时空区间，故 i = begin - 1
        // j：遍历指针，扫描 [begin, end-1] 的每一个元素
        int i = begin - 1;
        int j = begin;

        // 【主扫描循环】j 从 begin 遍历到 end-1
        // 易错点：j 必须从 begin 开始，不能写死为 0！
        //        因为 begin 可能 > 0（递归中常见）
        while (j <= end - 1) {
            // 【发现小值】若当前元素小于 pivot
            if (arr[j] < target) {
                // 【扩张小值区】i 右移，准备接纳新成员
                i++;
                // 【交换】将 arr[j] 移入小值区末尾
                //       原地操作，不使用额外空间
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                // 此时 arr[i] 属于小值区，i 仍指向其末尾
            }
            // 【推进扫描】j 持续右移，不受交换影响
            j++;
        }

        // 【安置 pivot】
        // 扫描结束后，所有 < target 的元素位于 [begin, i]
        // 所以 pivot 的正确位置是 i + 1
        i++; // pivot 的最终落点

        // 将 pivot（原 arr[end]）与 arr[i] 交换
        // 此时 arr[i] 左边全小，右边全大，完成分区
        int temp = arr[i];
        arr[i] = target;
        arr[end] = temp;

        // 【返回中间节点】
        // 返回 pivot 的最终索引，作为递归分治的分割点
        // 这个位置的元素已经"排好序"，后续不再变动
        return i;
    }

    /**
     * 另一种 partition 方法
     *
     * @param arr
     * @param begin
     * @param end
     * @return
     */
    static int partition2(int[] arr, int begin, int end) {
        if (begin == end) {
            return begin;
        }

        // 先选左边作为基准，这个位置会影响第一个i
        int pivotal = arr[begin];

        int i = begin;
        int j = end;

        // 哨兵划分 partition() 的最后一步是交换 nums[left] 和 nums[i] 。完成交换后，基准数左边的元素都 <= 基准数，这就要求最后一步交换前 nums[left] >= nums[i]
        // 必须成立。假设我们先"从左往右查找"，那么如果找不到比基准数更大的元素，则会在 i == j 时跳出循环，此时可能 nums[j] == nums[i] >
        // nums[left]。也就是说，此时最后一步交换操作会把一个比基准数更大的元素交换至数组最左端，导致哨兵划分失败。

        // 假设我们选了左边作为基准，则先从右边开始选区间
        while (i < j) {
            // 维持本区间性质，j继续移动。=号是必须的，这样才能恰好找到必须交换的值，排除等号的值
            while (i < j && arr[j] >= pivotal) { // 易错的点：在区间 while 内继续 while 是需要把父条件拷贝进来的
                j--;
            }
            // 维持本区间性质，i继续移动
            while (i < j && arr[i] <= pivotal) { // 易错的点：在区间 while 内继续 while 是需要把父条件拷贝进来的
                i++;
            }
            swap(arr, i, j);
        }
        swap(arr, begin, i);

        return i;
    }

    /**
     * 使用三数取中法的Hoare分区方案
     *
     * 该分区方法通过以下步骤实现：
     * 1. 使用三数取中法选择pivot，避免最坏情况
     * 2. 将pivot交换到区间起始位置
     * 3. 使用Hoare双指针方案进行分区
     * 4. 最后将pivot交换到最终位置
     *
     * 分区完成后满足：
     * - pivot左边的所有元素 <= pivot
     * - pivot右边的所有元素 >= pivot
     * - pivot位于其最终排序位置
     *
     * @param arr 待分区的数组
     * @param begin 分区区间的起始索引（包含）
     * @param end 分区区间的结束索引（包含）
     * @return pivot的最终位置索引
     */
    static int partitionMedian(int[] arr, int begin, int end) {
        if (begin >= end) {
            return begin;
        }

        int mid = begin + (end - begin) / 2;
        final int median = medianThree(arr, begin, mid, end);

        // 标准的左交换方案：即使是使用随机化的 pivotal，找到以后都要和开头交换
        int target = arr[median];
        swap(arr, median, begin);
        int l = begin;
        int r = end;

        // 如果l和r相遇了，那么不要循环了
        while (l < r) {
            // 根据 hoare partition 的逻辑，pivot放在开头，就先从右边开始找起

            // 这样可以保证在最后一个循环的时候：
            // 1. 如果r走向l，导致 l == r，而 l 不会向右走。因为此时 l 仍然是上一轮结束的时候交换的结果，所以l必然小于等于target，这样最终与begin交换就可以保证begin的位置仍然小于等于
            // target 的。
            // 2. 如果l走向r，而r不走，那么r保持交换以前的性质，r 大于等于 target
            while (l < r && arr[r] >= target) {
                r--;
            }
            while (l < r && arr[l] <= target) {
                l++;
            }
            swap(arr, l, r);
        }

        // 退出以后，l==r，此时将pivot交换到最终位置，交换前 arr[l]必定小于等于 target
        swap(arr, begin, l);
        return l;
    }

    /**
     * 交换数组中两个位置的元素
     *
     * @param array 目标数组
     * @param i 第一个位置
     * @param j 第二个位置
     */
    private static void swap(int[] array, int i, int j) {
        if (i == j) {
            return;
        }
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    /**
     * 三数取中法：找到三个位置中中位数的索引
     * 用于快速排序中选择更好的pivot，避免最坏情况
     *
     * @param nums 数组
     * @param left 左索引
     * @param mid 中间索引
     * @param right 右索引
     * @return 中位数所在的索引
     */
    static int medianThree(int[] nums, int left, int mid, int right) {
        int a = nums[left];
        int b = nums[mid];
        int c = nums[right];

        // 找出中位数对应的索引，这种中位数的比对方法是唯一正确的，尝试简化if语句都会出错
        if ((a >= b && a <= c) || (a >= c && a <= b)) {
            return left;
        } else if ((b >= a && b <= c) || (b >= c && b <= a)) {
            return mid;
        } else {
            return right;
        }
    }

}