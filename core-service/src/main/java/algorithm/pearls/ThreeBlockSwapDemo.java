package algorithm.pearls;

/**
 * @author liangchuan
 */
public class ThreeBlockSwapDemo {

    static void reverse(char[] a, int l, int r) {
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
     * @param k 数组长度或 γ 结束下标（不含）
     */
    public static void threeReverse(char[] a, int i, int j, int k) {
        reverse(a, i, j - 1);      // α
        reverse(a, j, k - 1);      // βγ
        reverse(a, i, k - 1);      // 整体
    }

    /**
     * 标准化旋转：将第一段移到最后
     * @param array 向量数组
     * @param segmentLengths 每一段的长度数组
     */
    public static void rotateVectorSegments(char[] array, int[] segmentLengths) {
        if (segmentLengths.length < 2) return;

        // 计算第一段长度
        int firstLen = segmentLengths[0];

        // 计算其余段总长度
        int totalLen = 0;
        for (int len : segmentLengths) {
            totalLen += len;
        }

        // 使用 threeReverse 实现第一段移到最后：[first][rest] => [rest][first]
        threeReverse(array, 0, firstLen, totalLen);
    }

    public static void main(String[] args) {
        // 测试复杂情况：多个段，总长度大
        char[] vectorData = new char[10000];
        for (int i = 0; i < 10000; i++) {
            vectorData[i] = (char) ('a' + (i % 26));
        }

        System.out.println("原始向量前100字符: " + new String(vectorData, 0, 100));

        // 分成 5 段，比如：[1000][2000][3000][2500][1500]
        int[] segmentLengths = {1000, 2000, 3000, 2500, 1500};

        rotateVectorSegments(vectorData, segmentLengths);

        System.out.println("旋转后向量前100字符: " + new String(vectorData, 0, 100));
        System.out.println("旋转后向量后100字符: " + new String(vectorData, 9900, 100));
    }
}
