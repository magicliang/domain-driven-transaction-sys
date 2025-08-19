package algorithm.basicds;

import java.util.List;

/**
 * 打印工具类
 * 提供各种打印辅助方法
 */
public class PrintUtil {

    /**
     * 打印邻接矩阵
     * 以整齐的格式打印二维矩阵，每行元素用空格分隔
     *
     * @param matrix 要打印的邻接矩阵
     */
    public static void printMatrix(List<List<Integer>> matrix) {
        if (matrix == null || matrix.isEmpty()) {
            System.out.println("[]");
            return;
        }

        int n = matrix.size();

        // 打印矩阵的每一行
        for (List<Integer> row : matrix) {
            System.out.print("[");
            for (int j = 0; j < row.size(); j++) {
                System.out.print(row.get(j));
                if (j < row.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println("]");
        }
    }

    /**
     * 打印二维数组矩阵（重载方法）
     * 用于支持原始二维数组的打印
     *
     * @param matrix 要打印的二维数组
     */
    public static void printMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            System.out.println("[]");
            return;
        }

        for (int[] row : matrix) {
            System.out.print("[");
            for (int j = 0; j < row.length; j++) {
                System.out.print(row[j]);
                if (j < row.length - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println("]");
        }
    }
}