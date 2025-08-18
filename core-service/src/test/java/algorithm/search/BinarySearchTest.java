package algorithm.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * BinarySearch类的全套测试用例
 * <p>
 * 测试覆盖范围：
 * <ul>
 *   <li>标准二分查找</li>
 *   <li>搜索插入位置（两种实现）</li>
 *   <li>查找左边界（两种实现）</li>
 *   <li>查找右边界（两种实现）</li>
 *   <li>查找floor值</li>
 * </ul>
 *
 * @author liangchuan
 */
@DisplayName("二分查找算法测试")
class BinarySearchTest {

    private BinarySearch binarySearch;
    private int[] emptyArray;
    private int[] singleElementArray;
    private int[] normalArray;
    private int[] duplicateArray;
    private int[] negativeArray;

    @BeforeEach
    void setUp() {
        binarySearch = new BinarySearch();
        emptyArray = new int[]{};
        singleElementArray = new int[]{5};
        normalArray = new int[]{1, 3, 5, 7, 9, 11, 13, 15};
        duplicateArray = new int[]{1, 2, 2, 2, 3, 4, 4, 5};
        negativeArray = new int[]{-5, -3, -1, 0, 2, 4, 6};
    }

    // ==================== 标准二分查找测试 ====================

    @Test
    @DisplayName("标准二分查找 - 空数组")
    void testBinarySearch_EmptyArray() {
        assertEquals(-1, binarySearch.binarySearch(emptyArray, 5));
    }

    @Test
    @DisplayName("标准二分查找 - 单元素数组存在")
    void testBinarySearch_SingleElementExists() {
        assertEquals(0, binarySearch.binarySearch(singleElementArray, 5));
    }

    @Test
    @DisplayName("标准二分查找 - 单元素数组不存在")
    void testBinarySearch_SingleElementNotExists() {
        assertEquals(-1, binarySearch.binarySearch(singleElementArray, 3));
    }

    @Test
    @DisplayName("标准二分查找 - 正常数组存在")
    void testBinarySearch_NormalArrayExists() {
        assertEquals(2, binarySearch.binarySearch(normalArray, 5));
        assertEquals(0, binarySearch.binarySearch(normalArray, 1));
        assertEquals(7, binarySearch.binarySearch(normalArray, 15));
    }

    @Test
    @DisplayName("标准二分查找 - 正常数组不存在")
    void testBinarySearch_NormalArrayNotExists() {
        assertEquals(-1, binarySearch.binarySearch(normalArray, 0));
        assertEquals(-1, binarySearch.binarySearch(normalArray, 16));
        assertEquals(-1, binarySearch.binarySearch(normalArray, 6));
    }

    @Test
    @DisplayName("标准二分查找 - 负数数组")
    void testBinarySearch_NegativeArray() {
        assertEquals(0, binarySearch.binarySearch(negativeArray, -5));
        assertEquals(3, binarySearch.binarySearch(negativeArray, 0));
        assertEquals(6, binarySearch.binarySearch(negativeArray, 6));
        assertEquals(-1, binarySearch.binarySearch(negativeArray, -6));
    }

    // ==================== searchInsert1 测试 ====================

    @Test
    @DisplayName("searchInsert1 - 空数组")
    void testSearchInsert1_EmptyArray() {
        assertEquals(0, binarySearch.searchInsert1(emptyArray, 5));
    }

    @Test
    @DisplayName("searchInsert1 - 单元素数组")
    void testSearchInsert1_SingleElement() {
        assertEquals(0, binarySearch.searchInsert1(singleElementArray, 3));
        assertEquals(1, binarySearch.searchInsert1(singleElementArray, 7));
        assertEquals(0, binarySearch.searchInsert1(singleElementArray, 5));
    }

    @Test
    @DisplayName("searchInsert1 - 正常数组")
    void testSearchInsert1_NormalArray() {
        assertEquals(0, binarySearch.searchInsert1(normalArray, 0));
        assertEquals(1, binarySearch.searchInsert1(normalArray, 2));
        assertEquals(2, binarySearch.searchInsert1(normalArray, 5));
        assertEquals(8, binarySearch.searchInsert1(normalArray, 20));
    }

    // ==================== searchInsert2 测试 ====================

    @Test
    @DisplayName("searchInsert2 - 空数组")
    void testSearchInsert2_EmptyArray() {
        assertEquals(0, binarySearch.searchInsert2(emptyArray, 5));
    }

    @Test
    @DisplayName("searchInsert2 - 单元素数组")
    void testSearchInsert2_SingleElement() {
        assertEquals(0, binarySearch.searchInsert2(singleElementArray, 3));
        assertEquals(1, binarySearch.searchInsert2(singleElementArray, 7));
        assertEquals(0, binarySearch.searchInsert2(singleElementArray, 5));
    }

    @Test
    @DisplayName("searchInsert2 - 正常数组")
    void testSearchInsert2_NormalArray() {
        assertEquals(0, binarySearch.searchInsert2(normalArray, 0));
        assertEquals(1, binarySearch.searchInsert2(normalArray, 2));
        assertEquals(2, binarySearch.searchInsert2(normalArray, 5));
        assertEquals(8, binarySearch.searchInsert2(normalArray, 20));
    }

    // ==================== leftBound1 测试 ====================

    @Test
    @DisplayName("leftBound1 - 空数组")
    void testLeftBound1_EmptyArray() {
        assertEquals(-1, binarySearch.leftBound1(emptyArray, 5));
    }

    @Test
    @DisplayName("leftBound1 - 单元素数组")
    void testLeftBound1_SingleElement() {
        assertEquals(0, binarySearch.leftBound1(singleElementArray, 5));
        assertEquals(-1, binarySearch.leftBound1(singleElementArray, 3));
    }

    @Test
    @DisplayName("leftBound1 - 重复元素数组")
    void testLeftBound1_DuplicateArray() {
        assertEquals(1, binarySearch.leftBound1(duplicateArray, 2));
        assertEquals(5, binarySearch.leftBound1(duplicateArray, 4));
        assertEquals(0, binarySearch.leftBound1(duplicateArray, 1));
        assertEquals(-1, binarySearch.leftBound1(duplicateArray, 0));
    }

    // ==================== leftBound2 测试 ====================

    @Test
    @DisplayName("leftBound2 - 空数组")
    void testLeftBound2_EmptyArray() {
        assertEquals(-1, binarySearch.leftBound2(emptyArray, 5));
    }

    @Test
    @DisplayName("leftBound2 - 单元素数组")
    void testLeftBound2_SingleElement() {
        assertEquals(0, binarySearch.leftBound2(singleElementArray, 5));
        assertEquals(-1, binarySearch.leftBound2(singleElementArray, 3));
    }

    @Test
    @DisplayName("leftBound2 - 重复元素数组")
    void testLeftBound2_DuplicateArray() {
        assertEquals(1, binarySearch.leftBound2(duplicateArray, 2));
        assertEquals(5, binarySearch.leftBound2(duplicateArray, 4));
        assertEquals(0, binarySearch.leftBound2(duplicateArray, 1));
        assertEquals(-1, binarySearch.leftBound2(duplicateArray, 0));
    }

    // ==================== rightBound1 测试 ====================

    @Test
    @DisplayName("rightBound1 - 空数组")
    void testRightBound1_EmptyArray() {
        assertEquals(-1, binarySearch.rightBound1(emptyArray, 5));
    }

    @Test
    @DisplayName("rightBound1 - 单元素数组")
    void testRightBound1_SingleElement() {
        assertEquals(0, binarySearch.rightBound1(singleElementArray, 5));
        assertEquals(-1, binarySearch.rightBound1(singleElementArray, 3));
    }

    @Test
    @DisplayName("rightBound1 - 重复元素数组")
    void testRightBound1_DuplicateArray() {
        assertEquals(3, binarySearch.rightBound1(duplicateArray, 2));
        assertEquals(6, binarySearch.rightBound1(duplicateArray, 4));
        assertEquals(0, binarySearch.rightBound1(duplicateArray, 1));
        assertEquals(-1, binarySearch.rightBound1(duplicateArray, 0));
    }

    // ==================== rightBound2 测试 ====================

    @Test
    @DisplayName("rightBound2 - 空数组")
    void testRightBound2_EmptyArray() {
        assertEquals(-1, binarySearch.rightBound2(emptyArray, 5));
    }

    @Test
    @DisplayName("rightBound2 - 单元素数组")
    void testRightBound2_SingleElement() {
        assertEquals(0, binarySearch.rightBound2(singleElementArray, 5));
        assertEquals(-1, binarySearch.rightBound2(singleElementArray, 3));
    }

    @Test
    @DisplayName("rightBound2 - 重复元素数组")
    void testRightBound2_DuplicateArray() {
        assertEquals(3, binarySearch.rightBound2(duplicateArray, 2));
        assertEquals(6, binarySearch.rightBound2(duplicateArray, 4));
        assertEquals(0, binarySearch.rightBound2(duplicateArray, 1));
        assertEquals(-1, binarySearch.rightBound2(duplicateArray, 0));
    }

    // ==================== floor 测试 ====================

    @Test
    @DisplayName("floor - 空数组")
    void testFloor_EmptyArray() {
        assertEquals(-1, binarySearch.floor(emptyArray, 5));
    }

    @Test
    @DisplayName("floor - 单元素数组")
    void testFloor_SingleElement() {
        assertEquals(-1, binarySearch.floor(singleElementArray, 3));
        assertEquals(-1, binarySearch.floor(singleElementArray, 5));
        assertEquals(0, binarySearch.floor(singleElementArray, 7));
    }

    @Test
    @DisplayName("floor - 正常数组")
    void testFloor_NormalArray() {
        assertEquals(-1, binarySearch.floor(normalArray, 0));
        assertEquals(0, binarySearch.floor(normalArray, 2));
        assertEquals(1, binarySearch.floor(normalArray, 4));
        assertEquals(2, binarySearch.floor(normalArray, 5));
        assertEquals(7, binarySearch.floor(normalArray, 15));
        assertEquals(7, binarySearch.floor(normalArray, 20));
    }

    @Test
    @DisplayName("floor - 负数数组")
    void testFloor_NegativeArray() {
        assertEquals(-1, binarySearch.floor(negativeArray, -6));
        assertEquals(0, binarySearch.floor(negativeArray, -4));
        assertEquals(1, binarySearch.floor(negativeArray, -2));
        assertEquals(3, binarySearch.floor(negativeArray, 1));
        assertEquals(6, binarySearch.floor(negativeArray, 7));
    }

    // ==================== 边界情况测试 ====================

    @Test
    @DisplayName("边界测试 - 极大值和极小值")
    void testBoundaryValues() {
        int[] largeArray = new int[1000];
        for (int i = 0; i < 1000; i++) {
            largeArray[i] = i * 2;
        }

        assertEquals(0, binarySearch.binarySearch(largeArray, 0));
        assertEquals(999, binarySearch.binarySearch(largeArray, 1998));
        assertEquals(-1, binarySearch.binarySearch(largeArray, 1999));
        assertEquals(500, binarySearch.binarySearch(largeArray, 1000));
    }

    @Test
    @DisplayName("边界测试 - 重复元素边界")
    void testDuplicateBoundary() {
        int[] allSame = new int[]{5, 5, 5, 5, 5};

        assertEquals(0, binarySearch.leftBound1(allSame, 5));
        assertEquals(4, binarySearch.rightBound1(allSame, 5));
        assertEquals(0, binarySearch.leftBound2(allSame, 5));
        assertEquals(4, binarySearch.rightBound2(allSame, 5));
    }

    // ==================== 一致性测试 ====================

    @Test
    @DisplayName("一致性测试 - leftBound1和leftBound2结果一致")
    void testLeftBoundConsistency() {
        int[] testArray = {1, 2, 2, 3, 3, 3, 4, 5};

        for (int target = 0; target <= 6; target++) {
            assertEquals(
                    binarySearch.leftBound1(testArray, target),
                    binarySearch.leftBound2(testArray, target)
            );
        }
    }

    @Test
    @DisplayName("一致性测试 - rightBound1和rightBound2结果一致")
    void testRightBoundConsistency() {
        int[] testArray = {1, 2, 2, 3, 3, 3, 4, 5};

        for (int target = 0; target <= 6; target++) {
            assertEquals(
                    binarySearch.rightBound1(testArray, target),
                    binarySearch.rightBound2(testArray, target)
            );
        }
    }

    @Test
    @DisplayName("一致性测试 - searchInsert1和searchInsert2结果一致")
    void testSearchInsertConsistency() {
        int[] testArray = {1, 3, 5, 7, 9};

        for (int target = 0; target <= 10; target++) {
            assertEquals(
                    binarySearch.searchInsert1(testArray, target),
                    binarySearch.searchInsert2(testArray, target)
            );
        }
    }
}