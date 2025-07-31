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

    // 易错的点：begin、end最好都是闭区间
    // 易错的点，没有分割两个数组来分治
    // 易错的点：两个数组到底应该怎么分界，两个数组最好都是前开后闭区间

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
}
