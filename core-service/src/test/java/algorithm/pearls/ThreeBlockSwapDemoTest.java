package algorithm.pearls;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ThreeBlockSwapDemoTest {

    @Test
    public void testReverseBasic() {
        char[] array = {'a', 'b', 'c', 'd', 'e'};
        ThreeBlockSwapDemo.reverse(array, 0, 4);
        assertArrayEquals(new char[]{'e', 'd', 'c', 'b', 'a'}, array);
    }

    @Test
    public void testReversePartial() {
        char[] array = {'a', 'b', 'c', 'd', 'e'};
        ThreeBlockSwapDemo.reverse(array, 1, 3);
        assertArrayEquals(new char[]{'a', 'd', 'c', 'b', 'e'}, array);
    }

    @Test
    public void testThreeReverseBasic() {
        char[] array = {'a', 'b', 'c', 'd', 'e'};
        ThreeBlockSwapDemo.threeReverse(array, 0, 2, 5);
        assertArrayEquals(new char[]{'c', 'd', 'e', 'a', 'b'}, array);
    }

    @Test
    public void testThreeReverseAlphaBetaGamma() {
        // α = "ab", βγ = "cdefg" → 经过 threeReverse 后应该得到 "cdefgab"
        char[] array = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        ThreeBlockSwapDemo.threeReverse(array, 0, 2, 7);
        assertArrayEquals(new char[]{'c', 'd', 'e', 'f', 'g', 'a', 'b'}, array);
    }

    @Test
    public void testRotateVectorSegmentsTwoSegments() {
        char[] array = {'a', 'b', 'c', 'd', 'e'};
        int[] segments = {2, 3};
        ThreeBlockSwapDemo.rotateVectorSegments(array, segments);
        assertArrayEquals(new char[]{'c', 'd', 'e', 'a', 'b'}, array);
    }

    @Test
    public void testRotateVectorSegmentsMultipleSegments() {
        char[] array = {'1', '2', '3', '4', '5', '6', '7', '8'};
        int[] segments = {2, 3, 3}; // [12][345][678] → [345678][12]
        ThreeBlockSwapDemo.rotateVectorSegments(array, segments);
        assertArrayEquals(new char[]{'3', '4', '5', '6', '7', '8', '1', '2'}, array);
    }

    @Test
    public void testRotateVectorSegmentsSingleSegment() {
        char[] array = {'a', 'b', 'c'};
        int[] segments = {3};
        ThreeBlockSwapDemo.rotateVectorSegments(array, segments);
        assertArrayEquals(new char[]{'a', 'b', 'c'}, array); // 应该保持不变
    }

    @Test
    public void testRotateVectorSegmentsEmptyArray() {
        char[] array = {};
        int[] segments = {};
        ThreeBlockSwapDemo.rotateVectorSegments(array, segments);
        assertArrayEquals(new char[]{}, array);
    }

    @Test
    public void testPerformanceWithLargeArray() {
        char[] largeArray = new char[10000];
        for (int i = 0; i < 10000; i++) {
            largeArray[i] = (char) ('a' + (i % 26));
        }

        char[] expected = largeArray.clone();
        // 手动旋转：将前1000个字符移到末尾
        System.arraycopy(largeArray, 1000, expected, 0, 9000);
        System.arraycopy(largeArray, 0, expected, 9000, 1000);

        int[] segments = {1000, 2000, 3000, 2500, 1500};
        ThreeBlockSwapDemo.rotateVectorSegments(largeArray, segments);

        assertArrayEquals(expected, largeArray);
    }
}
