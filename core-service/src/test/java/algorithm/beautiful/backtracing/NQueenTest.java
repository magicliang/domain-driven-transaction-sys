package algorithm.beautiful.backtracing;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NQueen算法测试类
 */
class NQueenTest {

    /**
     * 测试1x1棋盘
     */
    @Test
    void test1x1Board() {
        List<List<List<String>>> solutions = NQueen.nQueen(1);
        assertEquals(1, solutions.size());
        assertValidSolution(solutions.get(0), 1);
    }

    /**
     * 测试2x2棋盘(无解情况)
     */
    @Test
    void test2x2Board() {
        List<List<List<String>>> solutions = NQueen.nQueen(2);
        assertEquals(0, solutions.size());
    }

    /**
     * 测试3x3棋盘(无解情况)
     */
    @Test
    void test3x3Board() {
        List<List<List<String>>> solutions = NQueen.nQueen(3);
        assertEquals(0, solutions.size());
    }

    /**
     * 测试4x4棋盘
     */
    @Test
    void test4x4Board() {
        List<List<List<String>>> solutions = NQueen.nQueen(4);
        assertEquals(2, solutions.size());
        for (List<List<String>> solution : solutions) {
            assertValidSolution(solution, 4);
        }
    }

    /**
     * 测试5x5棋盘
     */
    @Test
    void test5x5Board() {
        List<List<List<String>>> solutions = NQueen.nQueen(5);
        assertEquals(10, solutions.size());
        for (List<List<String>> solution : solutions) {
            assertValidSolution(solution, 5);
        }
    }

    /**
     * 测试8x8棋盘
     */
    @Test
    void test8x8Board() {
        List<List<List<String>>> solutions = NQueen.nQueen(8);
        assertEquals(92, solutions.size());
        for (List<List<String>> solution : solutions) {
            assertValidSolution(solution, 8);
        }
    }

    /**
     * 测试边界情况(0或负数输入)
     */
    @Test
    void testInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> NQueen.nQueen(0));
        assertThrows(IllegalArgumentException.class, () -> NQueen.nQueen(-1));
    }

    /**
     * 验证解决方案的正确性
     *
     * @param board 棋盘解决方案
     * @param n     棋盘大小
     */
    private void assertValidSolution(List<List<String>> board, int n) {
        // 验证棋盘大小
        assertEquals(n, board.size());
        for (List<String> row : board) {
            assertEquals(n, row.size());
        }

        // 验证皇后数量
        int queenCount = 0;
        for (List<String> row : board) {
            for (String cell : row) {
                if (cell.equals("Q")) {
                    queenCount++;
                }
            }
        }
        assertEquals(n, queenCount);

        // 验证皇后不互相攻击
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board.get(i).get(j).equals("Q")) {
                    // 检查同一列
                    for (int k = 0; k < n; k++) {
                        if (k != i && board.get(k).get(j).equals("Q")) {
                            fail("Queens attack each other vertically");
                        }
                    }
                    // 检查对角线
                    for (int k = 1; k < n; k++) {
                        if (i + k < n && j + k < n && board.get(i + k).get(j + k).equals("Q")) {
                            fail("Queens attack each other diagonally");
                        }
                        if (i - k >= 0 && j - k >= 0 && board.get(i - k).get(j - k).equals("Q")) {
                            fail("Queens attack each other diagonally");
                        }
                        if (i + k < n && j - k >= 0 && board.get(i + k).get(j - k).equals("Q")) {
                            fail("Queens attack each other diagonally");
                        }
                        if (i - k >= 0 && j + k < n && board.get(i - k).get(j + k).equals("Q")) {
                            fail("Queens attack each other diagonally");
                        }
                    }
                }
            }
        }
    }
}
