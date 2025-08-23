package algorithm.pearls;

/**
 * @author liangchuan
 */
public class ThreeBlockSwapDemo {

    private static void reverse(char[] a, int l, int r) {
        while (l < r) {
            char tmp = a[l];
            a[l] = a[r];
            a[r] = tmp;
            l++;
            r--;
        }
    }

    /**
     * 把 αβγ 变成 γβα，β 保持原序。
     *
     * @param a 字符数组
     * @param i α 结束下标（不含）
     * @param j γ 起始下标
     */
    public static void threeReverse(char[] a, int i, int j, int k) {
        int n = a.length;
        // α 与 βγ 交换
        reverse(a, i, j - 1);      // α
        reverse(a, j, k - 1);      // βγ
        reverse(a, i, k - 1);      // 整体
    }


    public static void main(String[] args) {
        char[] data = {'a', 'b', 'c', 'X', 'Y', 'Z', 'd', 'e', 'f'};
        System.out.println("原数组: " + new String(data));
        // abc -> bac
        threeReverse(data, 0, 3, 6);
        // bac -> bca
        threeReverse(data, 3, 6, 9);
        // bca -> cba
        threeReverse(data, 0, 3, 6);

        System.out.println("交换后: " + new String(data));
    }
}
