package algorithm.sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;

public class BitmapSort {

    /**
     * 位图排序核心实现
     * 位图排序只是我们"把数据插入一个自排序"数据结构的常规实践而已，我们计算插入位置的时候，就已经让他们排序了，当我们能够思考到桶的时候，位图就呼之欲出了。
     * 字符串到整数再到位，是我们优化内存效率的好方案。
     *
     * 在内存不够的时候，我们就要考虑多趟写入、写出的归并排序了
     *
     * 位图排序(Bitmap Sort)与计数排序(Counting Sort)的关系：二者都是桶排序思想的变体，但各有特点。
     * 计数排序使用计数数组记录每个元素出现的次数，适用于重复元素场景，空间复杂度O(k)。
     * 位图排序使用位图标记元素是否存在，适用于无重复元素或需要去重的场景，空间复杂度O(k/8)，内存效率更高。
     * 当前实现是标准的位图排序，通过牺牲频次信息来换取更高的内存效率，特别适合大数据去重排序场景。
     *
     * 内存权衡认知：当内存不足以一次性加载所有数据时，这两种非比较排序都提供了"用时间换空间"的权衡方案。
     * 通过分块处理（segmented approach），我们可以将大数据集分割成适合内存大小的块，进行多趟排序。
     * 这种权衡在以下场景特别有价值：
     * 1. 数据范围已知且有限（如电话号码、ID等）
     * 2. 内存资源紧张但磁盘空间充足
     * 3. 可以接受多趟I/O操作的时间开销
     * 4. 需要稳定的线性时间复杂度排序
     *
     * @param inputFile 输入文件路径（每行一个整数）
     * @param outputFile 输出文件路径（排序结果）
     * @param maxNumber 数据最大值（如电话号码范围 0-10_000_000）
     */
    public static void bitmapSort(String inputFile, String outputFile, int maxNumber) throws IOException {
        // 1. 初始化位图：用 BitSet 存储数据存在性（每个 bit 代表一个数字）
        BitSet bitmap = new BitSet(maxNumber); // JDK 内置位图类，底层使用 long 数组优化

        // 2. 读取文件并设置位图
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int num = Integer.parseInt(line.trim());
                if (num < 0 || num >= maxNumber) {
                    throw new IllegalArgumentException("数字 " + num + " 超出范围 [0, " + maxNumber + ")");
                }
                bitmap.set(num); // 标记数字存在（自动去重）
            }
        }

        // 3. 遍历位图并输出有序结果
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (int i = bitmap.nextSetBit(0); i >= 0; i = bitmap.nextSetBit(i + 1)) {
                writer.write(Integer.toString(i));
                writer.newLine();
            }
        }
    }

    /**
     * 处理超大范围数据（内存不足时使用分区块处理）
     *
     * @param inputFile 输入文件
     * @param outputFile 输出文件
     * @param maxNumber 数据最大值
     * @param blockSize 分块大小（例如 5_000_000）
     */
    public static void segmentedBitmapSort(String inputFile, String outputFile, int maxNumber, int blockSize)
            throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // 分多趟处理数据块
            for (int start = 0; start < maxNumber; start += blockSize) {
                int end = Math.min(start + blockSize, maxNumber);
                BitSet blockBitmap = new BitSet(blockSize);

                // 读取文件并处理当前块范围内的数据
                try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        int num = Integer.parseInt(line.trim());
                        if (num >= start && num < end) {
                            blockBitmap.set(num - start); // 偏移量存储
                        }
                    }
                }

                // 输出当前块的有序结果
                for (int i = blockBitmap.nextSetBit(0); i >= 0; i = blockBitmap.nextSetBit(i + 1)) {
                    writer.write(Integer.toString(i + start)); // 还原原始数值
                    writer.newLine();
                }
            }
        }
    }

    // 示例用法
    public static void main(String[] args) {
        try {
            // 场景1：标准位图排序（适用于 maxNumber 在内存允许范围内）
            bitmapSort("numbers.txt", "sorted.txt", 10_000_000);

            // 场景2：超大数据范围（例如 1亿）使用分块处理
            segmentedBitmapSort("large_numbers.txt", "sorted_large.txt", 100_000_000, 5_000_000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}