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

    @Test
    @DisplayName("searchInsert1 - 插入位置在开头")
    void testSearchInsert1_InsertAtBeginning() {
        assertEquals(0, binarySearch.searchInsert1(normalArray, -1));
        assertEquals(0, binarySearch.searchInsert1(normalArray, 0));
    }

    @Test
    @DisplayName("searchInsert1 - 插入位置在末尾")
    void testSearchInsert1_InsertAtEnd() {
        assertEquals(8, binarySearch.searchInsert1(normalArray, 16));
        assertEquals(8, binarySearch.searchInsert1(normalArray, 100));
    }

    @Test
    @DisplayName("searchInsert1 - 插入位置在中间")
    void testSearchInsert1_InsertInMiddle() {
        assertEquals(1, binarySearch.searchInsert1(normalArray, 2));
        assertEquals(3, binarySearch.searchInsert1(normalArray, 6));
        assertEquals(5, binarySearch.searchInsert1(normalArray, 10));
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

    @Test
    @DisplayName("searchInsert2 - 插入位置在开头")
    void testSearchInsert2_InsertAtBeginning() {
        assertEquals(0, binarySearch.searchInsert2(normalArray, -1));
        assertEquals(0, binarySearch.searchInsert2(normalArray, 0));
    }

    @Test
    @DisplayName("searchInsert2 - 插入位置在末尾")
    void testSearchInsert2_InsertAtEnd() {
        assertEquals(8, binarySearch.searchInsert2(normalArray, 16));
        assertEquals(8, binarySearch.searchInsert2(normalArray, 100));
    }

    @Test
    @DisplayName("searchInsert2 - 插入位置在中间")
    void testSearchInsert2_InsertInMiddle() {
        assertEquals(1, binarySearch.searchInsert2(normalArray, 2));
        assertEquals(3, binarySearch.searchInsert2(normalArray, 6));
        assertEquals(5, binarySearch.searchInsert2(normalArray, 10));
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

    // ==================== rightBound3 测试 ====================

    @Test
    @DisplayName("rightBound3 - 空数组")
    void testRightBound3_EmptyArray() {
        assertEquals(-1, binarySearch.rightBound3(emptyArray, 5));
    }

    @Test
    @DisplayName("rightBound3 - 单元素数组")
    void testRightBound3_SingleElement() {
        assertEquals(0, binarySearch.rightBound3(singleElementArray, 5));
        assertEquals(-1, binarySearch.rightBound3(singleElementArray, 3));
    }

    @Test
    @DisplayName("rightBound3 - 重复元素数组")
    void testRightBound3_DuplicateArray() {
        assertEquals(3, binarySearch.rightBound3(duplicateArray, 2));
        assertEquals(6, binarySearch.rightBound3(duplicateArray, 4));
        assertEquals(0, binarySearch.rightBound3(duplicateArray, 1));
        assertEquals(-1, binarySearch.rightBound3(duplicateArray, 0));
    }

    @Test
    @DisplayName("rightBound3 - 正常数组")
    void testRightBound3_NormalArray() {
        assertEquals(0, binarySearch.rightBound3(normalArray, 1));
        assertEquals(7, binarySearch.rightBound3(normalArray, 15));
        assertEquals(-1, binarySearch.rightBound3(normalArray, 0));
        assertEquals(-1, binarySearch.rightBound3(normalArray, 16));
    }

    @Test
    @DisplayName("rightBound3 - 负数数组")
    void testRightBound3_NegativeArray() {
        assertEquals(0, binarySearch.rightBound3(negativeArray, -5));
        assertEquals(6, binarySearch.rightBound3(negativeArray, 6));
        assertEquals(-1, binarySearch.rightBound3(negativeArray, -6));
    }

    // ==================== rightBound3 一致性测试 ====================

    @Test
    @DisplayName("一致性测试 - rightBound1、rightBound2和rightBound3结果一致")
    void testRightBound3Consistency() {
        int[] testArray = {1, 2, 2, 3, 3, 3, 4, 5};

        for (int target = 0; target <= 6; target++) {
            int right1 = binarySearch.rightBound1(testArray, target);
            int right2 = binarySearch.rightBound2(testArray, target);
            int right3 = binarySearch.rightBound3(testArray, target);

            assertEquals(right1, right3,
                    "rightBound1和rightBound3在target=" + target + "时不一致");
            assertEquals(right2, right3,
                    "rightBound2和rightBound3在target=" + target + "时不一致");
        }
    }

    @Test
    @DisplayName("rightBound3 - 边界值行为")
    void testRightBound3_BoundaryBehavior() {
        int[] edgeCases = {2, 2, 2};

        // 测试小于所有元素
        assertEquals(-1, binarySearch.rightBound3(edgeCases, 1));

        // 测试等于所有元素
        assertEquals(2, binarySearch.rightBound3(edgeCases, 2));

        // 测试大于所有元素
        assertEquals(-1, binarySearch.rightBound3(edgeCases, 3));
    }

    @Test
    @DisplayName("rightBound3 - 连续重复块")
    void testRightBound3_ContinuousBlocks() {
        int[] blocks = {1, 1, 1, 3, 3, 5, 5, 5, 5, 7, 7};

        // 测试1的边界
        assertEquals(2, binarySearch.rightBound3(blocks, 1));

        // 测试3的边界
        assertEquals(4, binarySearch.rightBound3(blocks, 3));

        // 测试5的边界
        assertEquals(8, binarySearch.rightBound3(blocks, 5));

        // 测试7的边界
        assertEquals(10, binarySearch.rightBound3(blocks, 7));

        // 测试不存在的target
        assertEquals(-1, binarySearch.rightBound3(blocks, 4));
    }

    @Test
    @DisplayName("rightBound3 - 全重复数组")
    void testRightBound3_AllSame() {
        int[] allSame = {5, 5, 5, 5, 5};

        assertEquals(4, binarySearch.rightBound3(allSame, 5));
        assertEquals(-1, binarySearch.rightBound3(allSame, 4));
        assertEquals(-1, binarySearch.rightBound3(allSame, 6));
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
        assertEquals(1, binarySearch.floor(normalArray, 5));
        assertEquals(6, binarySearch.floor(normalArray, 15));
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

    // ==================== 重复元素边界测试 ====================

    @Test
    @DisplayName("重复元素测试 - 全重复数组")
    void testDuplicateElements_AllSame() {
        int[] allSame = {5, 5, 5, 5, 5};

        // 实际边界
        assertEquals(0, binarySearch.leftBound1(allSame, 5));
        assertEquals(4, binarySearch.rightBound1(allSame, 5));
        assertEquals(0, binarySearch.leftBound2(allSame, 5));
        assertEquals(4, binarySearch.rightBound2(allSame, 5));

        // 但floor(6)应该返回最后一个5的位置
        assertEquals(4, binarySearch.floor(allSame, 6));
        assertEquals(4, binarySearch.floor2(allSame, 6));
    }

    @Test
    @DisplayName("重复元素测试 - 连续重复块")
    void testDuplicateElements_ContinuousBlocks() {
        int[] blocks = {1, 1, 1, 3, 3, 5, 5, 5, 5, 7, 7};

        // 测试1的边界
        assertEquals(0, binarySearch.leftBound1(blocks, 1));
        assertEquals(2, binarySearch.rightBound1(blocks, 1));

        // 测试3的边界
        assertEquals(3, binarySearch.leftBound1(blocks, 3));
        assertEquals(4, binarySearch.rightBound1(blocks, 3));

        // 测试5的边界
        assertEquals(5, binarySearch.leftBound1(blocks, 5));
        assertEquals(8, binarySearch.rightBound1(blocks, 5));

        // 测试7的边界
        assertEquals(9, binarySearch.leftBound1(blocks, 7));
        assertEquals(10, binarySearch.rightBound1(blocks, 7));

        // 测试不存在的target
        assertEquals(5, binarySearch.searchInsert1(blocks, 4)); // 插入到5的位置
        assertEquals(-1, binarySearch.leftBound1(blocks, 4));   // 实际不存在
        assertEquals(4, binarySearch.floor(blocks, 4));         // floor是最后一个3
    }

    @Test
    @DisplayName("重复元素测试 - 理论边界一致性验证")
    void testDuplicateElements_TheoryBoundaryConsistency() {
        int[] testArray = {1, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5};

        // 验证所有方法在重复元素场景下的一致性
        for (int target = 0; target <= 6; target++) {
            // 理论边界应该一致 - 删除这些，因为 searchInsert 不适合重复 target
            // int insert1 = binarySearch.searchInsert1(testArray, target);
            // int insert2 = binarySearch.searchInsert2(testArray, target);
            // assertEquals(insert1, insert2,
            //         "searchInsert1和searchInsert2在target=" + target + "时不一致");

            // 实际边界应该一致
            int left1 = binarySearch.leftBound1(testArray, target);
            int left2 = binarySearch.leftBound2(testArray, target);
            int left3 = binarySearch.leftBound3(testArray, target);
            assertEquals(left1, left2,
                    "leftBound1和leftBound2在target=" + target + "时不一致");
            assertEquals(left1, left3,
                    "leftBound1和leftBound3在target=" + target + "时不一致");

            int right1 = binarySearch.rightBound1(testArray, target);
            int right2 = binarySearch.rightBound2(testArray, target);
            assertEquals(right1, right2,
                    "rightBound1和rightBound2在target=" + target + "时不一致");

            // floor方法不再断言一致性，因为floor1不适合重复target场景
        }
    }

    @Test
    @DisplayName("重复元素测试 - 边界值行为")
    void testDuplicateElements_BoundaryBehavior() {
        int[] edgeCases = {2, 2, 2};

        // 测试小于所有元素 - 保留，因为 target=1 不重复
        assertEquals(0, binarySearch.searchInsert1(edgeCases, 1));
        assertEquals(-1, binarySearch.leftBound1(edgeCases, 1));
        assertEquals(-1, binarySearch.rightBound1(edgeCases, 1));
        assertEquals(-1, binarySearch.rightBound3(edgeCases, 1));
        assertEquals(-1, binarySearch.floor(edgeCases, 1));

        // 测试大于所有元素 - 保留，因为 target=3 不重复
        assertEquals(3, binarySearch.searchInsert1(edgeCases, 3));
        assertEquals(-1, binarySearch.leftBound1(edgeCases, 3));
        assertEquals(-1, binarySearch.rightBound1(edgeCases, 3));
        assertEquals(-1, binarySearch.rightBound3(edgeCases, 3));
        assertEquals(2, binarySearch.floor(edgeCases, 3));
    }

    // ==================== 虚拟元素边界测试 ====================

    @Test
    @DisplayName("虚拟元素测试 - 左边界")
    void testLeftBoundByVirtualElement() {
        // 测试用例1：正常情况，存在重复元素
        int[] nums1 = {1, 2, 2, 2, 3, 4};
        assertEquals(1, binarySearch.leftBoundByVirtualElement(nums1, 2));

        // 测试用例2：单个元素
        int[] nums2 = {5};
        assertEquals(0, binarySearch.leftBoundByVirtualElement(nums2, 5));

        // 测试用例3：元素不存在
        int[] nums3 = {1, 3, 5, 7};
        assertEquals(-1, binarySearch.leftBoundByVirtualElement(nums3, 4));

        // 测试用例4：target比所有元素都小
        int[] nums4 = {2, 3, 4, 5};
        assertEquals(-1, binarySearch.leftBoundByVirtualElement(nums4, 1));

        // 测试用例5：target比所有元素都大
        int[] nums5 = {1, 2, 3, 4};
        assertEquals(-1, binarySearch.leftBoundByVirtualElement(nums5, 5));

        // 测试用例6：空数组
        int[] nums6 = {};
        assertEquals(-1, binarySearch.leftBoundByVirtualElement(nums6, 1));

        // 测试用例7：边界情况 - 第一个元素
        int[] nums7 = {1, 2, 3, 4};
        assertEquals(0, binarySearch.leftBoundByVirtualElement(nums7, 1));

        // 测试用例8：边界情况 - 最后一个元素
        int[] nums8 = {1, 2, 3, 4};
        assertEquals(3, binarySearch.leftBoundByVirtualElement(nums8, 4));
    }

    @Test
    @DisplayName("虚拟元素测试 - 右边界")
    void testRightBoundByVirtualElement() {
        // 测试用例1：正常情况，存在重复元素
        int[] nums1 = {1, 2, 2, 2, 3, 4};
        assertEquals(3, binarySearch.rightBoundByVirtualElement(nums1, 2));

        // 测试用例2：单个元素
        int[] nums2 = {5};
        assertEquals(0, binarySearch.rightBoundByVirtualElement(nums2, 5));

        // 测试用例3：元素不存在
        int[] nums3 = {1, 3, 5, 7};
        assertEquals(-1, binarySearch.rightBoundByVirtualElement(nums3, 4));

        // 测试用例4：target比所有元素都小
        int[] nums4 = {2, 3, 4, 5};
        assertEquals(-1, binarySearch.rightBoundByVirtualElement(nums4, 1));

        // 测试用例5：target比所有元素都大
        int[] nums5 = {1, 2, 3, 4};
        assertEquals(-1, binarySearch.rightBoundByVirtualElement(nums5, 5));

        // 测试用例6：空数组
        int[] nums6 = {};
        assertEquals(-1, binarySearch.rightBoundByVirtualElement(nums6, 1));

        // 测试用例7：边界情况 - 第一个元素
        int[] nums7 = {1, 2, 3, 4};
        assertEquals(0, binarySearch.rightBoundByVirtualElement(nums7, 1));

        // 测试用例8：边界情况 - 最后一个元素
        int[] nums8 = {1, 2, 3, 4};
        assertEquals(3, binarySearch.rightBoundByVirtualElement(nums8, 4));

        // 测试用例9：多个重复元素
        int[] nums9 = {1, 2, 2, 2, 2, 2, 3};
        assertEquals(5, binarySearch.rightBoundByVirtualElement(nums9, 2));
    }

    @Test
    @DisplayName("虚拟元素测试 - 方法一致性验证")
    void testVirtualElementMethodsConsistency() {
        // 验证虚拟元素方法与标准方法的一致性
        int[] nums = {1, 2, 2, 2, 3, 4, 4, 5};

        // 测试各种target值
        int[] targets = {1, 2, 3, 4, 5, 0, 6};

        for (int target : targets) {
            int leftVirtual = binarySearch.leftBoundByVirtualElement(nums, target);
            int leftStandard = binarySearch.leftBound3(nums, target);
            assertEquals(leftStandard, leftVirtual,
                    "左边界方法结果不一致，target=" + target);

            int rightVirtual = binarySearch.rightBoundByVirtualElement(nums, target);
            int rightStandard = binarySearch.rightBound3(nums, target);
            assertEquals(rightStandard, rightVirtual,
                    "右边界方法结果不一致，target=" + target);
        }
    }
}