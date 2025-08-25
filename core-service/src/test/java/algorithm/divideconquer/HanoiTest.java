package algorithm.divideconquer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 汉诺塔算法测试类
 * 验证汉诺塔算法的正确性
 */
class HanoiTest {

    private Hanoi hanoi;

    @BeforeEach
    void setUp() {
        hanoi = new Hanoi();
    }

    @Test
    void testSingleDisk() {
        // 测试单个盘子的情况
        List<Integer> A = new ArrayList<>(Arrays.asList(1));
        List<Integer> B = new ArrayList<>();
        List<Integer> C = new ArrayList<>();

        hanoi.solveHanoi(A, B, C);

        assertTrue(A.isEmpty());
        assertTrue(B.isEmpty());
        assertEquals(Arrays.asList(1), C);
    }

    @Test
    void testTwoDisks() {
        // 测试两个盘子的情况 - 注意盘子顺序，1是最小的在上，2是最大的在下
        List<Integer> A = new ArrayList<>(Arrays.asList(1, 2));
        List<Integer> B = new ArrayList<>();
        List<Integer> C = new ArrayList<>();

        hanoi.solveHanoi(A, B, C);

        assertTrue(A.isEmpty());
        assertTrue(B.isEmpty());
        assertEquals(Arrays.asList(1, 2), C);
    }

    @Test
    void testThreeDisks() {
        // 测试三个盘子的情况
        List<Integer> A = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> B = new ArrayList<>();
        List<Integer> C = new ArrayList<>();

        hanoi.solveHanoi(A, B, C);

        assertTrue(A.isEmpty());
        assertTrue(B.isEmpty());
        assertEquals(Arrays.asList(1, 2, 3), C);
    }

    @Test
    void testFiveDisks() {
        // 测试五个盘子的情况
        List<Integer> A = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> B = new ArrayList<>();
        List<Integer> C = new ArrayList<>();

        hanoi.solveHanoi(A, B, C);

        assertTrue(A.isEmpty());
        assertTrue(B.isEmpty());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), C);
    }

    @Test
    void testEmptyTower() {
        // 测试空塔的情况
        List<Integer> A = new ArrayList<>();
        List<Integer> B = new ArrayList<>();
        List<Integer> C = new ArrayList<>();

        hanoi.solveHanoi(A, B, C);

        assertTrue(A.isEmpty());
        assertTrue(B.isEmpty());
        assertTrue(C.isEmpty());
    }

    @Test
    void testDiskOrderPreserved() {
        // 测试盘子顺序是否保持正确（从小到大）
        List<Integer> A = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> B = new ArrayList<>();
        List<Integer> C = new ArrayList<>();

        hanoi.solveHanoi(A, B, C);

        // 验证盘子顺序保持不变
        assertEquals(Arrays.asList(1, 2, 3, 4), C);

        // 验证所有盘子都移动到了目标柱子
        assertEquals(4, C.size());
        assertEquals(0, A.size());
        assertEquals(0, B.size());
    }

    @Test
    void testLargeNumberOfDisks() {
        // 测试较大数量的盘子，验证不会栈溢出
        List<Integer> A = new ArrayList<>();
        List<Integer> B = new ArrayList<>();
        List<Integer> C = new ArrayList<>();

        // 测试10个盘子
        for (int i = 1; i <= 10; i++) {
            A.add(i);
        }

        hanoi.solveHanoi(A, B, C);

        assertTrue(A.isEmpty());
        assertTrue(B.isEmpty());
        assertEquals(10, C.size());

        // 验证顺序
        for (int i = 0; i < 10; i++) {
            assertEquals(i + 1, C.get(i).intValue());
        }
    }
}