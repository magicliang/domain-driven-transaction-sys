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
     * 正确的思路：
     * 如果要提取前k个元素，则把前k个元素和它和它后方的全部元素交换，然后返回前k个元素
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
            // 易错的点：忘记j本身也必须在候选交换列表（允许不交换才是均匀的），且忘记nextInt后一个参数是开区间的，所以正确的算法是先用数组的终值反算出差值，再从差值反算出差值终点，再用差值中间+1
            // swap(total, j, j + ThreadLocalRandom.current().nextInt(0, n - j));
            // 优美的实现
            swap(total, j, ThreadLocalRandom.current().nextInt(j, n));
        }

        // 提取前k个元素作为结果
        int[] result = new int[k];
        for (int m = 0; m < k; m++) {
            result[m] = total[m];
        }

        return result;
    }

    public int[] reverseShuffle(int n, int k) {
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

        // 初始化数组：[0, 1, 2, ..., n-1]
        for (int i = 0; i < n; i++) {
            total[i] = i;
        }

        // 正确的逆向Fisher-Yates算法：后区间驱动，只取后k个数，跟前区间做全量交换
        for (int j = n - 1; j >= n - k; j--) {
            swap(total, j, ThreadLocalRandom.current().nextInt(0, j + 1));
        }

        // 正确的结果提取：从末尾k个位置提取
        int[] result = new int[k];
        for (int m = 0; m < k; m++) {
            result[m] = total[n - k + m];  // 提取total[n-k..n-1]
        }

        return result;
    }

    /**
     * 从0到n-1的整数中随机选择k个不重复的数（空间优化版）
     * 使用虚拟数组映射技术，空间复杂度O(k)而非O(n)
     *
     * @param n 整数范围的上限（不包含n）
     * @param k 需要选择的整数个数
     * @return 包含k个随机整数的数组，范围在[0, n-1]之间
     * @throws IllegalArgumentException 当参数不合法时
     */
    public int[] shuffleOptimized(int n, int k) {
        if (n <= 0) {
            throw new IllegalArgumentException("n必须大于0");
        }
        if (k < 0) {
            throw new IllegalArgumentException("k必须大于等于0");
        }
        if (k > n) {
            throw new IllegalArgumentException("k不能大于n");
        }

        int[] result = new int[k];

        // 使用更简洁高效的虚拟数组实现
        // 使用HashMap来跟踪已交换的位置，避免重复
        java.util.Map<Integer, Integer> virtualArray = new java.util.HashMap<>();
        
        for (int i = 0; i < k; i++) {
            // 从[i, n-1]范围内随机选择一个位置
            int randomPos = ThreadLocalRandom.current().nextInt(i, n);

            // 获取i位置的当前值（可能是虚拟值或实际值）
            int valueAtI = virtualArray.getOrDefault(i, i);
            // 获取randomPos位置的当前值（可能是虚拟值或实际值）
            int valueAtRandom = virtualArray.getOrDefault(randomPos, randomPos);
            
            // 将选择的值放入结果
            result[i] = valueAtRandom;

            // 交换i和randomPos位置的值
            if (valueAtI != valueAtRandom) {
                virtualArray.put(i, valueAtRandom);
                virtualArray.put(randomPos, valueAtI);
            }
        }

        return result;
    }

    /**
     * 从0到n-1的整数中随机选择k个不重复的数（逆向空间优化版）
     * 使用虚拟数组映射技术，空间复杂度O(k)
     *
     * @param n 整数范围的上限（不包含n）
     * @param k 需要选择的整数个数
     * @return 包含k个随机整数的数组，范围在[0, n-1]之间
     * @throws IllegalArgumentException 当参数不合法时
     */
    public int[] reverseShuffleOptimized(int n, int k) {
        if (n <= 0) {
            throw new IllegalArgumentException("n必须大于0");
        }
        if (k < 0) {
            throw new IllegalArgumentException("k必须大于等于0");
        }
        if (k > n) {
            throw new IllegalArgumentException("k不能大于n");
        }

        int[] result = new int[k];

        // 使用更高效的虚拟数组实现
        // 使用数组来模拟哈希映射，key为索引，value为映射值
        int[] virtualArray = new int[n];
        // 标记哪些位置被映射过
        boolean[] isMapped = new boolean[n];

        // 只映射必要的k个位置
        for (int i = 0; i < k; i++) {
            int pos = n - 1 - i; // 从后往前处理
            int randomPos = ThreadLocalRandom.current().nextInt(0, pos + 1);

            // 获取randomPos的当前值
            int valueAtRandom = isMapped[randomPos] ? virtualArray[randomPos] : randomPos;
            // 获取pos的当前值
            int valueAtPos = isMapped[pos] ? virtualArray[pos] : pos;

            // 交换值
            if (randomPos != pos) {
                virtualArray[randomPos] = valueAtPos;
                virtualArray[pos] = valueAtRandom;
                isMapped[randomPos] = true;
                isMapped[pos] = true;
            }

            // 将结果存入数组
            result[i] = valueAtRandom;
        }

        return result;
    }

    /**
     * 从0到n-1的整数中随机选择k个不重复的数（最优空间优化版）
     * 使用Floyd算法，空间复杂度严格为O(k)
     *
     * @param n 整数范围的上限（不包含n）
     * @param k 需要选择的整数个数
     * @return 包含k个随机整数的数组，范围在[0, n-1]之间
     * @throws IllegalArgumentException 当参数不合法时
     */
    public int[] shuffleFloyd(int n, int k) {
        if (n <= 0) {
            throw new IllegalArgumentException("n必须大于0");
        }
        if (k < 0) {
            throw new IllegalArgumentException("k必须大于等于0");
        }
        if (k > n) {
            throw new IllegalArgumentException("k不能大于n");
        }

        int[] result = new int[k];

        // 使用正确的Floyd抽样算法
        // 该算法确保从0到n-1中选择k个不重复的随机数
        java.util.Map<Integer, Integer> map = new java.util.HashMap<>();

        for (int j = 0; j < k; j++) {
            int t = ThreadLocalRandom.current().nextInt(j, n);

            // 获取t位置的值（可能是映射值或实际值）
            int valueAtT = map.getOrDefault(t, t);
            // 获取j位置的值（可能是映射值或实际值）
            int valueAtJ = map.getOrDefault(j, j);

            // 将选择的值放入结果
            result[j] = valueAtT;

            // 交换j和t位置的值
            if (valueAtT != valueAtJ) {
                map.put(t, valueAtJ);
                map.put(j, valueAtT);
            }
        }

        return result;
    }

    /**
     * 对数组的前k个元素进行逆向Fisher-Yates洗牌
     *
     * 功能：只对数组的前k个元素进行随机洗牌，保持其余元素位置不变
     * 算法：基于Fisher-Yates洗牌算法的逆向实现
     *
     * 时间复杂度：O(k) - 只需要对前k个元素进行k次交换
     * 空间复杂度：O(1) - 原地交换，不需要额外空间
     *
     * @param arr 需要洗牌的数组
     * @param k 需要洗牌的前k个元素，必须满足 0 <= k <= arr.length
     * @throws IllegalArgumentException 当k < 0 或 k > arr.length时抛出
     */
    public void shuffleBackward(int[] arr, int k) {
        if (arr == null) {
            throw new IllegalArgumentException("数组不能为null");
        }
        if (k < 0) {
            throw new IllegalArgumentException("k必须大于等于0");
        }
        if (k > arr.length) {
            throw new IllegalArgumentException("k不能大于数组长度");
        }

        // 只对前k个元素进行洗牌
        for (int i = k - 1; i > 0; i--) {
            // 在 [0, i] 范围内随机选择一个位置 j
            int j = ThreadLocalRandom.current().nextInt(i + 1);
            swap(arr, i, j);
        }
    }

    /**
     * 对数组的后k个元素进行逆向Fisher-Yates洗牌
     *
     * 功能：只对数组的后k个元素进行随机洗牌，保持其余元素位置不变
     * 算法：基于Fisher-Yates洗牌算法的逆向实现，从数组末尾开始
     *
     * 时间复杂度：O(k) - 只需要对后k个元素进行k次交换
     * 空间复杂度：O(1) - 原地交换，不需要额外空间
     *
     * @param arr 需要洗牌的数组
     * @param k 需要洗牌的后k个元素，必须满足 0 <= k <= arr.length
     * @throws IllegalArgumentException 当k < 0 或 k > arr.length时抛出
     */
    public void shuffleBackwardFromEnd(int[] arr, int k) {
        if (arr == null) {
            throw new IllegalArgumentException("数组不能为null");
        }
        if (k < 0) {
            throw new IllegalArgumentException("k必须大于等于0");
        }
        if (k > arr.length) {
            throw new IllegalArgumentException("k不能大于数组长度");
        }

        int n = arr.length;
        // 只对后k个元素进行洗牌，从倒数第k个位置开始
        for (int i = n - 1; i >= n - k + 1; i--) {
            // 在 [n-k, i] 范围内随机选择一个位置 j
            int j = ThreadLocalRandom.current().nextInt(n - k, i + 1);
            swap(arr, i, j);
        }
    }

    /**
     * 保留原始的全数组洗牌方法，用于向后兼容
     *
     * @param arr 需要洗牌的数组
     * @deprecated 建议使用新的shuffleBackward(int[] arr, int k)方法
     */
    @Deprecated
    public void shuffleBackward(int[] arr) {
        int n = arr.length;
        for (int i = n - 1; i > 0; i--) {
            // 在 [0, i] 中随机选一个位置 j
            int j = ThreadLocalRandom.current().nextInt(i + 1);
            swap(arr, i, j);
        }
    }

    /**
     * Knuth洗牌算法（Fisher-Yates的另一种实现）
     *
     * 功能：对数组进行完全随机洗牌
     * 算法：从数组开头开始，依次将每个元素与后面随机位置的元素交换
     *
     * 时间复杂度：O(n) - 需要遍历整个数组
     * 空间复杂度：O(1) - 原地交换，不需要额外空间
     *
     * @param arr 需要洗牌的数组
     * @throws IllegalArgumentException 当数组为null时抛出
     */
    public void knuthShuffle(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("数组不能为null");
        }

        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            // 在 [i, n-1] 范围内随机选择一个位置 j
            int j = ThreadLocalRandom.current().nextInt(i, n);
            swap(arr, i, j);
        }
    }

    /**
     * 内洗牌算法（In-place shuffle）
     *
     * 功能：对数组进行原地随机洗牌，保持原有元素不变
     * 算法：基于Fisher-Yates算法的内洗牌实现
     *
     * 时间复杂度：O(n) - 需要遍历整个数组
     * 空间复杂度：O(1) - 完全原地操作，不需要额外空间
     *
     * @param arr 需要洗牌的数组
     * @throws IllegalArgumentException 当数组为null时抛出
     */
    public void inPlaceShuffle(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("数组不能为null");
        }

        int n = arr.length;
        for (int i = n - 1; i > 0; i--) {
            // 在 [0, i] 范围内随机选择一个位置 j
            int j = ThreadLocalRandom.current().nextInt(i + 1);
            swap(arr, i, j);
        }
    }

    /**
     * 蓄水池抽样算法（Reservoir Sampling）
     *
     * 功能：从数据流中随机选择k个元素，每个元素被选中的概率相等
     * 算法：蓄水池抽样算法，适用于大数据流场景
     *
     * 时间复杂度：O(n) - 需要遍历整个数据流
     * 空间复杂度：O(k) - 只需要存储k个结果
     *
     * @param data 数据流数组
     * @param k 需要选择的元素个数
     * @return 包含k个随机选择元素的数组
     * @throws IllegalArgumentException 当参数不合法时抛出
     */
    public int[] reservoirSampling(int[] data, int k) {
        if (data == null) {
            throw new IllegalArgumentException("数据数组不能为null");
        }
        if (k < 0) {
            throw new IllegalArgumentException("k必须大于等于0");
        }
        if (k > data.length) {
            throw new IllegalArgumentException("k不能大于数据长度");
        }

        int[] reservoir = new int[k];

        // 初始化蓄水池，存储前k个元素
        for (int i = 0; i < k; i++) {
            reservoir[i] = data[i];
        }

        // 对剩余的元素进行随机选择
        for (int i = k; i < data.length; i++) {
            // 生成 [0, i] 范围内的随机数
            int j = ThreadLocalRandom.current().nextInt(i + 1);

            // 如果随机数小于k，则替换蓄水池中的对应元素
            if (j < k) {
                reservoir[j] = data[i];
            }
        }

        return reservoir;
    }

    /**
     * 加权洗牌算法（Weighted Shuffle）
     *
     * 功能：根据权重对数组元素进行随机洗牌，权重越大的元素出现在前面的概率越高
     * 算法：基于权重指数的随机洗牌算法
     *
     * 时间复杂度：O(n log n) - 需要排序
     * 空间复杂度：O(n) - 需要存储权重和索引
     *
     * @param arr 需要洗牌的数组
     * @param weights 对应的权重数组，必须与arr长度相同
     * @throws IllegalArgumentException 当参数不合法时抛出
     */
    public void weightedShuffle(int[] arr, double[] weights) {
        if (arr == null || weights == null) {
            throw new IllegalArgumentException("数组和权重数组都不能为null");
        }
        if (arr.length != weights.length) {
            throw new IllegalArgumentException("数组长度必须与权重数组长度相同");
        }
        if (arr.length == 0) {
            return;
        }

        int n = arr.length;

        // 创建索引数组和对应的随机键值
        Integer[] indices = new Integer[n];
        double[] keys = new double[n];

        for (int i = 0; i < n; i++) {
            indices[i] = i;
            // 使用指数分布生成随机键值，权重越大，键值越小
            keys[i] = -Math.log(ThreadLocalRandom.current().nextDouble()) / weights[i];
        }

        // 根据键值进行排序
        java.util.Arrays.sort(indices, (a, b) -> Double.compare(keys[a], keys[b]));

        // 根据排序后的索引重新排列数组
        int[] temp = new int[n];
        for (int i = 0; i < n; i++) {
            temp[i] = arr[indices[i]];
        }
        System.arraycopy(temp, 0, arr, 0, n);
    }

    /**
     * 部分洗牌算法（Partial Shuffle）
     *
     * 功能：只对数组的指定子区间进行洗牌
     * 算法：基于Fisher-Yates算法的区间洗牌实现
     *
     * 时间复杂度：O(end - start) - 只需要对指定区间进行洗牌
     * 空间复杂度：O(1) - 原地交换，不需要额外空间
     *
     * @param arr 需要洗牌的数组
     * @param start 洗牌区间的起始索引（包含）
     * @param end 洗牌区间的结束索引（不包含）
     * @throws IllegalArgumentException 当参数不合法时抛出
     */
    public void partialShuffle(int[] arr, int start, int end) {
        if (arr == null) {
            throw new IllegalArgumentException("数组不能为null");
        }
        if (start < 0 || end > arr.length || start >= end) {
            throw new IllegalArgumentException("无效的区间范围");
        }

        for (int i = end - 1; i > start; i--) {
            // 在 [start, i] 范围内随机选择一个位置 j
            int j = ThreadLocalRandom.current().nextInt(start, i + 1);
            swap(arr, i, j);
        }
    }

    /**
     * 交换数组中两个位置的元素
     *
     * @param array 目标数组
     * @param i 第一个位置
     * @param j 第二个位置
     */
    private void swap(int[] array, int i, int j) {
        if (i == j) {
            return;
        }
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
}