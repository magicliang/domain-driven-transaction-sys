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
    private static int partition(int[] arr, int begin, int end) {
        // 特殊情况：区间只有一个元素，无需分区，直接返回其位置
        if (begin == end) {
            return begin;
        }

        // 【选择基准】以最后一个元素作为 pivot（目标值）
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
    private static int partition2(int[] arr, int begin, int end) {
        if (begin == end) {
            return begin;
        }

        // 先选左边作为基准，这个位置会影响第一个i
        int pivotal = arr[begin];

        int i = begin;
        int j = end;

        // 假设我们选了左边作为基准，则先从右边开始选区间
        while (i < j) {
            // 维持本区间性质，j继续移动。=号是必须的，这样才能恰好找到必须交换的值
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

}