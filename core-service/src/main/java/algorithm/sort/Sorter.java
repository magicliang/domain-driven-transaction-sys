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
        int[] arr = new int[]{6, 3, 8, 5, 1, 7, 9, 2};
        System.out.println(Arrays.toString(mergeSort(arr)));
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

        return merge(mergeSort(a), mergeSort(b));
    }

    public static int[] merge(int[] a, int[] b) {
        // 1. 把两个数组合并成1个数组，是需要引入一个临时数据结构的，这就要求我们寻找一个 a + b 长度的数据结构。
        // 2. 尝试使用循环-一轮循环、多轮循环来移动2个指针，一直走到两个指针走到尽头
        // 3. 升序从 0 开始
        int i = 0;
        int j = 0;

        int tempLength = a.length + b.length;
        int[] temp = new int[tempLength];
        int k = 0;
        while (i < a.length || j < b.length) {
            Integer curA = null;
            Integer curB = null;

            if (i < a.length) {
                curA = a[i];
            }
            if (j < b.length) {
                curB = b[j];
            }
            if (curA != null && curB != null) {
                if (curA < curB) {
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
