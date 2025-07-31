package algorithm.sort;


import java.util.Arrays;

/**
 * project name: domain-driven-transaction-sys
 *
 * description:
 * description: 原地归并排序实现
 *
 * @author magicliang
 *
 *         date: 2025-07-31 12:01
 */
public class Sorter {

    public static void main(String[] args) {
        System.out.println("-----------mergeSort");
        System.out.println(Arrays.toString(mergeSort(new int[]{6, 3, 8, 5, 1, 7, 9, 2})));

        System.out.println("-----------inPlaceMergeSort");
        int[] arr = {6, 3, 8, 5, 1, 7, 9, 2};
        inPlaceMergeSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    public static int[] mergeSort(int[] arr) {
        if (arr.length == 1 || arr.length == 0) {
            return arr;
        }

        int mid = arr.length / 2;
        int[] a = new int[mid];
        int[] b = new int[arr.length - mid];
        for (int i = 0; i < mid; i++) {
            a[i] = arr[i];
        }
        for (int j = 0; j < b.length; j++) {
            b[j] = arr[j + mid];
        }

//        return merge2Arrays(mergeSort(a), mergeSort(b));
        return merge2Arrays2(mergeSort(a), mergeSort(b));
    }

    // 易错的点：begin、end最好都是闭区间
    // 易错的点，没有分割两个数组来分治
    // 易错的点：两个数组到底应该怎么分界，两个数组最好都是前开后闭区间
    // 易错的点：没有处理已排好序的场景

    // 原地归并排序实现
    public static void inPlaceMergeSort(int[] arr, int left, int right) {

        // 某些情况下不该排序
        if (arr == null || arr.length == 1 || left >= right) {
            return;
        }

        // 生成一个临时数据段，最好和数组完全相等，对整个数组反复使用；如果和 right - left + 1 相等，则需要使用一些技巧
        int[] temp = new int[right - left + 1];

        // 再排右部分：因为偏左，所以加一是安全的
        inPlaceMergeSort(arr, temp, left, right);
    }

    public static void inPlaceMergeSort(int[] arr, int[] temp, int left, int right) {
        // 某些情况下不该排序
        if (arr == null || arr.length == 1 || left >= right) {
            return;
        }
        // 取中点：对奇数处于正中央，对于偶数处于偏左的位置。对于单值意味着本值本身
        int mid = left + (right - left) / 2;

        // 先排左部分
        inPlaceMergeSort(arr, temp, left, mid);

        // 再排右部分：因为偏左，所以加一是安全的
        inPlaceMergeSort(arr, temp, mid + 1, right);

        // 易错的点：分治以后应该在每一层都做好 merge
        inPlaceMerge(arr, temp, left, mid, right);
    }


    // 原地合并算法
    private static void inPlaceMerge(int[] arr, int[] temp, int left, int mid, int right) {
        // 已排序
        if (arr[mid] <= arr[mid + 1]) {
            return;
        }

        // 原地内存操作的核心思想是：准备一个和现空间一样大的空间，先把数据拷贝过去，然后再拷贝回这个共享数据段

        // 如果数组完全和原始 arr 总长相等，则可以一对一映射，否则，这里面的 temp 要从0开始，而 arr要从 left 开始
        for (int i = left; i <= right; i++) {
            temp[i - left] = arr[i];
        }

        // 设置两个游标，分别在区间的起点

        // 先对距离在 temp 里是从0开始的，映射到 arr 的 left 区间
        int i = 0;
        int j = mid - left + 1;
        int tempMid = j - 1;
        int end = right - left;
        int k = left;
        while (i <= tempMid && j <= end) {
            if (temp[i] <= temp[j]) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
            }
        }

        while (i <= tempMid) {
            arr[k++] = temp[i++];
        }

        while (j <= end) {
            arr[k++] = temp[j++];
        }
    }

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
