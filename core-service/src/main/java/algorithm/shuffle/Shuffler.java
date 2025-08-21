package algorithm.shuffle;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 洗牌算法实现类
 * 
 * 功能：从0到n-1的整数中随机选择k个不重复的数
 * 算法：基于Fisher-Yates洗牌算法的部分洗牌实现
 * 
 * 时间复杂度：O(n) - 需要初始化数组和k次交换
 * 空间复杂度：O(n) - 需要存储整个数组
 * 
 * @author liangchuan
 */
public class Shuffler {

    /**
     * 从0到n-1的整数中随机选择k个不重复的数
     * 
     * @param n 整数范围的上限（不包含n）
     * @param k 需要选择的整数个数
     * @return 包含k个随机整数的数组，范围在[0, n-1]之间
     * @throws IllegalArgumentException 当参数不合法时
     */
    public int[] shuffle(int n, int k) {
        if (n <= 0) {
            throw new IllegalArgumentException("n必须大于0");
        }
        if (k < 0) {
            throw new IllegalArgumentException("k必须大于等于0");
        }
        if (k > n) {
            throw new IllegalArgumentException("k不能大于n");
        }

        int[] total = new int[n];

        // 初始化待排序数组：[0, 1, 2, ..., n-1]
        for (int i = 0; i < n; i++) {
            total[i] = i;
        }

        // 执行k次交换，每次将当前位置j与[j, n-1]范围内的随机位置交换
        // 这样前k个位置就是随机选择的k个数
        for (int j = 0; j < k; j++) {
            // 在[j, n-1]范围内随机选择一个位置
            // ThreadLocalRandom.current().nextInt(0, n-j) 生成[0, n-j-1]的随机数
            // 加上j后得到[j, n-1]范围内的随机位置
            swap(total, j, j + ThreadLocalRandom.current().nextInt(0, n - j));
        }

        // 提取前k个元素作为结果
        int[] result = new int[k];
        for (int m = 0; m < k; m++) {
            result[m] = total[m];
        }

        return result;
    }

    /**
     * 交换数组中两个位置的元素
     * 
     * @param array 目标数组
     * @param i 第一个位置
     * @param j 第二个位置
     */
    private void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
}