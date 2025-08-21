package algorithm.sort;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 归并排序实现
 * 采用分治策略，将数组分成两半分别排序，然后合并两个有序数组
 *
 * @author magicliang
 *
 *         date: 2025-08-21 16:00
 */
public class MergeSort {

    /**
     * 归并排序入口方法
     * 采用分治策略，将数组分成两半分别排序，然后合并两个有序数组
     *
     * @param arr 待排序的整数数组
     * @return 排序后的新数组
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
     * 合并两个已排序的数组
     * 使用双指针技术，逐个比较两个数组的元素，按升序合并
     *
     * @param a 第一个已排序的数组
     * @param b 第二个已排序的数组
     * @return 合并后的有序数组
     */
    public static int[] merge2Arrays(int[] a, int[] b) {
        // 1. 把两个数组合并成1个数组，是需要引入一个临时数据结构的，这就要求我们寻找一个 a + b 长度的数据结构。
        // 2. 尝试使用循环-一轮循环、多轮循环来移动2个指针，一直走到两个指针走到尽头
        // 3. 升序从 0 开始
        int i = 0;
        int j = 0;

        int tempLength = a.length + b.length;
        int[] temp = new int[tempLength];
        int k = 0;

        // 易错的点：每轮迭代移动超过一张卡片导致越界

        while (i < a.length || j < b.length) {
            // 易错的点，逻辑分支如果是互斥的，则用 else-if 串起来，这样可以实现互斥
            if (i < a.length && j < b.length) {
                if (a[i] <= b[j]) {
                    temp[k++] = a[i];
                    i++;
                } else {
                    temp[k++] = b[j];
                    j++;
                }
            } else if (i < a.length) {

                // 这里如果单独拆出 i < a.length，则可能同时满足上面的 && 条件
                temp[k++] = a[i];
                i++;
            } else {
                temp[k++] = b[j];
                j++;
            }
        }
        return temp;
    }

    /**
     * 合并两个已排序的数组（优化版本）
     * 使用更清晰的逻辑结构，先处理共同部分，再处理剩余部分
     *
     * @param a 第一个已排序的数组
     * @param b 第二个已排序的数组
     * @return 合并后的有序数组
     */
    public static int[] merge2Arrays2(int[] a, int[] b) {
        // 和另一个算法的差别是：另一个算法用 || 来控制一个多个游标，用 if 来区分游标的移动场景，这种做法是危险的，要区分在每轮迭代里游标移动几次

        // 本算法的特点是，用 && 来控制游标共移动的场景，然后用各自的循环来控制游标单独移动的场景

        int i = 0;
        int j = 0;

        int k = 0;
        int[] temp = new int[a.length + b.length];
        while (i < a.length && j < b.length) {
            if (a[i] <= b[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = b[j++];
            }
        }

        while (i < a.length) {
            temp[k++] = a[i++];
        }

        while (j < b.length) {
            temp[k++] = b[j++];
        }

        return temp;
    }
}