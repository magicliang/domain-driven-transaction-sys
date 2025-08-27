package algorithm.sort;

/**
 * 归并排序算法实现类
 * <p>
 * 归并排序是建立在归并操作上的一种有效的排序算法，由约翰·冯·诺伊曼在1945年发明。
 * 该算法是采用分治法（Divide and Conquer）的一个非常典型的应用，且各种语言或环境下都有相应的实现。
 * </p>
 *
 * <p>
 * <strong>算法特点：</strong>
 * <ul>
 *   <li>稳定排序：相等元素的相对位置不会改变</li>
 *   <li>时间复杂度稳定：在最好、最坏、平均情况下都是O(n log n)</li>
 *   <li>分治策略：将大问题分解为小问题递归解决</li>
 *   <li>外部排序友好：适合处理大数据量的排序</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>算法思想：</strong><br>
 * "分而治之"的经典体现：
 * <ol>
 *   <li><strong>分解(Divide)：</strong>将n个元素分成各含n/2个元素的子序列</li>
 *   <li><strong>解决(Conquer)：</strong>用归并排序法对两个子序列递归地排序</li>
 *   <li><strong>合并(Combine)：</strong>合并两个已排序的子序列以得到排序结果</li>
 * </ol>
 * </p>
 *
 * @author magicliang
 * @version 1.0
 * @since 2025-08-21
 * @see <a href="https://en.wikipedia.org/wiki/Merge_sort">Merge sort - Wikipedia</a>
 */
public class MergeSort {

    /**
     * 归并排序入口方法
     * <p>
     * 采用分治策略，将数组递归分成两半分别排序，然后合并两个有序数组。
     * 这是一个稳定的排序算法，适用于各种数据分布情况。
     * </p>
     *
     * <p>
     * <strong>算法复杂度分析：</strong>
     * </p>
     * <ul>
     *   <li><strong>时间复杂度：</strong>O(n log n) - 所有情况下都保持稳定
     *     <ul>
     *       <li>分治过程：将数组递归分解为单个元素，分解层数为 log n</li>
     *       <li>合并过程：每层需要 O(n) 时间合并所有子数组</li>
     *       <li>总时间复杂度：T(n) = 2T(n/2) + O(n) = O(n log n)</li>
     *     </ul>
     *   </li>
     *   <li><strong>空间复杂度：</strong>O(n)
     *     <ul>
     *       <li>需要额外的临时数组存储合并结果，空间大小为 O(n)</li>
     *       <li>递归调用栈深度为 O(log n)</li>
     *       <li>总空间复杂度为 O(n)（临时数组占主导）</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <p>
     * <strong>复杂度证明：</strong><br>
     * 设T(n)为排序n个元素的时间复杂度，则有递推关系：
     * <ul>
     *   <li>T(n) = 2T(n/2) + O(n)，其中O(n)是合并两个子数组的时间</li>
     *   <li>根据主定理，a=2, b=2, f(n)=O(n)，满足情况2</li>
     *   <li>因此T(n) = Θ(n log n)</li>
     * </ul>
     * </p>
     *
     * <p>
     * <strong>算法优势：</strong>
     * <ul>
     *   <li>时间复杂度稳定，不受输入数据分布影响</li>
     *   <li>稳定排序，保持相等元素的相对顺序</li>
     *   <li>适合链表排序，可以达到O(1)的空间复杂度</li>
     *   <li>适合外部排序，处理超大数据集</li>
     * </ul>
     * </p>
     *
     * @param arr 待排序的整数数组（不能为null）
     * @return 排序后的新数组（与原数组内容相同但已排序）
     * @throws NullPointerException 如果输入数组为null
     */
    public static int[] mergeSort(int[] arr) {
        if (arr.length == 1 || arr.length == 0) {
            return arr;
        }

        int mid = arr.length / 2;
        int[] a = new int[mid];
        int[] b = new int[arr.length - mid];
        System.arraycopy(arr, 0, a, 0, mid);
        System.arraycopy(arr, 0 + mid, b, 0, b.length);

        return merge2Arrays(mergeSort(a), mergeSort(b));
    }

    /**
     * 合并两个已排序的数组（基础版本）
     * <p>
     * 使用双指针技术，逐个比较两个数组的元素，按升序合并成一个新的有序数组。
     * 这是归并排序算法的核心操作，体现了"合并"的思想。
     * </p>
     *
     * <p>
     * <strong>算法流程：</strong>
     * <ol>
     *   <li>创建长度为a.length + b.length的临时数组</li>
     *   <li>使用三个指针：i指向数组a，j指向数组b，k指向结果数组</li>
     *   <li>在单个while循环中处理所有情况：
     *     <ul>
     *       <li>两个数组都有剩余元素：比较并选择较小者</li>
     *       <li>只有数组a有剩余：直接复制a的剩余元素</li>
     *       <li>只有数组b有剩余：直接复制b的剩余元素</li>
     *     </ul>
     *   </li>
     * </ol>
     * </p>
     *
     * <p>
     * <strong>关键设计要点：</strong>
     * <ul>
     *   <li>使用OR条件(||)控制主循环，确保处理完所有元素</li>
     *   <li>使用else-if确保分支互斥，避免重复处理</li>
     *   <li>每轮迭代只移动一个指针，防止越界</li>
     *   <li>使用<=保证稳定性（相等元素保持原有顺序）</li>
     * </ul>
     * </p>
     *
     * <p>
     * <strong>时间复杂度：</strong> O(m + n)，其中m和n分别是两个数组的长度<br>
     * <strong>空间复杂度：</strong> O(m + n)，需要创建新的结果数组
     * </p>
     *
     * @param a 第一个已排序的数组（升序）
     * @param b 第二个已排序的数组（升序）
     * @return 合并后的有序数组，长度为a.length + b.length
     */
    public static int[] merge2Arrays(int[] a, int[] b) {
        // 1. 创建临时数组：合并两个数组需要额外的存储空间
        // 临时数组长度 = a.length + b.length
        int tempLength = a.length + b.length;
        int[] temp = new int[tempLength];

        // 2. 初始化三个指针：
        // i: 遍历数组a的指针，从0开始
        // j: 遍历数组b的指针，从0开始  
        // k: 填充结果数组的指针，从0开始
        int i = 0;
        int j = 0;
        int k = 0;

        // 3. 主合并循环：使用OR条件确保处理完所有元素
        // 循环继续条件：至少有一个数组还有未处理的元素
        while (i < a.length || j < b.length) {
            // 情况1：两个数组都有剩余元素，需要比较选择
            if (i < a.length && j < b.length) {
                // 比较当前元素，选择较小者（使用<=保证稳定性）
                if (a[i] <= b[j]) {
                    temp[k++] = a[i];   // 选择a[i]，i指针前进
                    i++;
                } else {
                    temp[k++] = b[j];   // 选择b[j]，j指针前进
                    j++;
                }
            }
            // 情况2：只有数组a有剩余元素，直接复制
            else if (i < a.length) {
                temp[k++] = a[i];
                i++;
            }
            // 情况3：只有数组b有剩余元素，直接复制
            else {
                temp[k++] = b[j];
                j++;
            }
        }
        return temp;
    }

    /**
     * 合并两个已排序的数组（优化版本）
     * <p>
     * 使用更清晰的逻辑结构，将合并过程分为三个阶段：
     * 1. 同时处理两个数组的公共部分
     * 2. 处理数组a的剩余部分
     * 3. 处理数组b的剩余部分
     * </p>
     *
     * <p>
     * <strong>算法优势：</strong>
     * <ul>
     *   <li>逻辑更清晰：三个独立的while循环，职责分明</li>
     *   <li>性能更优：避免了每次循环中的复杂条件判断</li>
     *   <li>更易理解：符合人类思维的顺序处理模式</li>
     *   <li>更安全：每个循环只处理一种情况，降低出错概率</li>
     * </ul>
     * </p>
     *
     * <p>
     * <strong>与merge2Arrays的区别：</strong>
     * <ul>
     *   <li>merge2Arrays：使用OR(||)条件在单个循环中处理所有情况</li>
     *   <li>merge2Arrays2：使用AND(&&)条件分阶段处理，逻辑更清晰</li>
     *   <li>本方法避免了复杂的分支判断，提高了代码可读性和执行效率</li>
     * </ul>
     * </p>
     *
     * <p>
     * <strong>算法流程：</strong>
     * <ol>
     *   <li><strong>阶段1：</strong>两个数组都有元素时，比较并选择较小者</li>
     *   <li><strong>阶段2：</strong>数组a有剩余时，直接复制所有剩余元素</li>
     *   <li><strong>阶段3：</strong>数组b有剩余时，直接复制所有剩余元素</li>
     * </ol>
     * </p>
     *
     * <p>
     * <strong>时间复杂度：</strong> O(m + n)，其中m和n分别是两个数组的长度<br>
     * <strong>空间复杂度：</strong> O(m + n)，需要创建新的结果数组
     * </p>
     *
     * @param a 第一个已排序的数组（升序，不能为null）
     * @param b 第二个已排序的数组（升序，不能为null）
     * @return 合并后的有序数组，长度为a.length + b.length
     */
    public static int[] merge2Arrays2(int[] a, int[] b) {
        // 初始化指针和结果数组
        int i = 0;  // 数组a的遍历指针
        int j = 0;  // 数组b的遍历指针
        int k = 0;  // 结果数组的填充指针
        int[] temp = new int[a.length + b.length];

        // 阶段1：同时处理两个数组的公共部分
        // 使用AND(&&)条件确保两个数组都有剩余元素
        // 这样避免了在循环内部进行复杂的边界检查
        while (i < a.length && j < b.length) {
            // 比较当前元素，选择较小者
            // 使用<=保证算法的稳定性（相等元素保持原有顺序）
            if (a[i] <= b[j]) {
                temp[k++] = a[i++];  // 选择a[i]，同时推进i和k指针
            } else {
                temp[k++] = b[j++];  // 选择b[j]，同时推进j和k指针
            }
        }

        // 阶段2：处理数组a的剩余元素
        // 当数组b已经处理完毕，但数组a还有剩余元素时执行
        while (i < a.length) {
            temp[k++] = a[i++];
        }

        // 阶段3：处理数组b的剩余元素  
        // 当数组a已经处理完毕，但数组b还有剩余元素时执行
        while (j < b.length) {
            temp[k++] = b[j++];
        }

        return temp;
    }
}