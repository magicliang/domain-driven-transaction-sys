package algorithm.sort;

import java.io.*;
import java.util.BitSet;

public class BitmapSort {

    /**
     * 位图排序核心实现
     * 位图排序只是我们“把数据插入一个自排序”数据结构的常规实践而已，我们计算插入位置的时候，就已经让他们排序了，当我们能够思考到桶的时候，位图就呼之欲出了。
     * 字符串到整数再到位，是我们优化内存效率的好方案。
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
