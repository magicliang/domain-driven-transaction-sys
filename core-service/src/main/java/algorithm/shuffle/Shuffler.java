package algorithm.shuffle;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author liangchuan
 */
public class Shuffler {

    public int[] shuffle(int n, int k) {
        int[] total = new int[n];

        // 初始化待排序数组
        for (int i = 0; i < n; i++) {
            total[i] = i;
        }

        for (int j = 0; j < k; j++) {
            // 易错的点：在剩余区间内（与k无关）找到一个数，且必须包含0（这样才保持均匀性），而最高节点是 n-1，距离j有 n-1-j 个数，所以[1,n-j)。
            swap(total, j, j + ThreadLocalRandom.current().nextInt(0, n - j));
        }
        int[] result = new int[k];
        for (int m = 0; m < k; m++) {
            result[m] = total[m];
        }

        return result;
    }

    private void swap(int[] total, int i, int j) {
        int tmp = total[i];
        total[i] = total[j];
        total[j] = tmp;
    }
}
