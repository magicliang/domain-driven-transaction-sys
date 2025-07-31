package algorithm.sort;


import java.util.Arrays;

/**
 * project name: domain-driven-transaction-sys
 *
 * description:
 *
 * @author magicliang
 *
 *         date: 2025-07-31 12:01
 */
public class Sorter {

    public static void main(String[] args) {
        System.out.println("-----------mergeSort");
        System.out.println(Arrays.toString(mergeSort(new int[]{6, 3, 8, 5, 1, 7, 9, 2})));
        System.out.println("-----------mergeSort2");
        System.out.println(Arrays.toString(mergeSort2(new int[]{6, 3, 8, 5, 1, 7, 9, 2})));
    }

    public static int[] mergeSort(int[] arr) {
        if (arr.length == 1) {
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

        return merge2Arrays(mergeSort(a), mergeSort(b));
    }

    public static int[] mergeSort2(int[] arr, int begin, int end) {
        if (arr.length == 1) {
            return arr;
        }
        int mid = arr.length / 2;

        // 易错的点，没有分割两个数组来分治
        int[] arr1 = mergeSort2(arr, 0, mid);
        int[] arr2 = mergeSort2(arr, mid + 1, end);
        return mergeArray(arr, 0, arr.length / 2, arr.length - 1);
    }

    // 易错的点：begin、end最好都是闭区间
    public static int[] mergeArray(int[] a, int begin, int mid, int end) {
        // 把一个数组里的两段合并成有序的新数组返回，新数组的长度等于原数组长度之和
        // 准备两个游标，在一个while循环里移动，注意起止
        // 要注意某类游标移动到尽头以后的操作，排序是排升序，已经排好的数组也是升序的
        // 每轮迭代只移动一张卡片
        int newLength = end - begin + 1;
        int[] temp = new int[newLength];

        int i = begin;
        int j = mid + 1;
        int k = 0;

        // 在一个大while里区分 || 的多种场景

        // 易错的点：两个数组到底应该怎么分界，两个数组最好都是前开后闭区间
        while (i <= mid || j <= end) {
            if (i <= mid && j <= end) {
                int tempA = a[i];
                int tempB = a[j];
                if (tempA <= tempB) {
                    temp[k++] = tempA;
                    i++;
                } else {
                    temp[k++] = tempB;
                    j++;
                }
            } else if (i <= mid) {
                temp[k++] = a[i++];

            } else {
                temp[k++] = a[j++];
            }
        }

        return temp;
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
            Integer curA = null;
            Integer curB = null;

            if (i < a.length) {
                curA = a[i];
            }
            if (j < b.length) {
                curB = b[j];
            }

            // 易错的点，逻辑分支如果是互斥的，则用 else-if 串起来，这样可以实现互斥

            if (curA != null && curB != null) {
                if (curA <= curB) {
                    temp[k++] = curA;
                    i++;
                } else {
                    temp[k++] = curB;
                    j++;
                }
            } else if (curA != null) {
                temp[k++] = curA;
                i++;
            } else {
                temp[k++] = curB;
                j++;
            }
        }
        return temp;
    }
}
