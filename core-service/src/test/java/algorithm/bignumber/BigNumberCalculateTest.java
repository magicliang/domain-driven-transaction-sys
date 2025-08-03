package algorithm.bignumber;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: 大数加法测试类
 *
 * @author magicliang
 *
 *         date: 2025-07-31 10:46
 */
public class BigNumberCalculateTest {

    @Test
    public void testBasicAddition() {
        assertEquals("5", BigNumberCalculate.add("2", "3"));
        assertEquals("10", BigNumberCalculate.add("5", "5"));
        assertEquals("100", BigNumberCalculate.add("50", "50"));
    }

    @Test
    public void testCarryHandling() {
        assertEquals("10", BigNumberCalculate.add("9", "1"));
        assertEquals("100", BigNumberCalculate.add("99", "1"));
        assertEquals("1000", BigNumberCalculate.add("999", "1"));
        assertEquals("10000", BigNumberCalculate.add("9999", "1"));
    }

    @Test
    public void testLargeNumbers() {
        assertEquals("1111111110", BigNumberCalculate.add("123456789", "987654321"));
        assertEquals("10000000000000000000", BigNumberCalculate.add("9999999999999999999", "1"));
    }

    @Test
    public void testDifferentLengths() {
        assertEquals("12412", BigNumberCalculate.add("12345", "67"));
        assertEquals("1000001", BigNumberCalculate.add("1", "1000000"));
        assertEquals("1000000001", BigNumberCalculate.add("1", "1000000000"));
    }

    @Test
    public void testZeroHandling() {
        assertEquals("0", BigNumberCalculate.add("0", "0"));
        assertEquals("123", BigNumberCalculate.add("0", "123"));
        assertEquals("123", BigNumberCalculate.add("123", "0"));
        assertEquals("1000", BigNumberCalculate.add("000", "1000"));
    }

    @Test
    public void testLeadingZeros() {
        assertEquals("123", BigNumberCalculate.add("0123", "000"));
        assertEquals("1000", BigNumberCalculate.add("0999", "0001"));
        assertEquals("1235", BigNumberCalculate.add("01234", "00001"));
    }

    @Test
    public void testSameLengthNumbers() {
        assertEquals("2468", BigNumberCalculate.add("1234", "1234"));
        assertEquals("11110", BigNumberCalculate.add("5555", "5555"));
    }

    @Test
    public void testMaxSingleDigit() {
        assertEquals("18", BigNumberCalculate.add("9", "9"));
        assertEquals("19", BigNumberCalculate.add("9", "10"));
    }

    @Test
    public void testVeryLargeNumbers() {
        String num1 = "123456789012345678901234567890";
        String num2 = "987654321098765432109876543210";
        String expected = "1111111110111111111011111111100";
        assertEquals(expected, BigNumberCalculate.add(num1, num2));
    }

    @Test
    public void testAllNines() {
        assertEquals("100", BigNumberCalculate.add("99", "1"));
        assertEquals("1000", BigNumberCalculate.add("999", "1"));
        assertEquals("10000", BigNumberCalculate.add("9999", "1"));
        assertEquals("100000", BigNumberCalculate.add("99999", "1"));
    }

    @Test
    public void testEmptyStringA() {
        assertThrows(IllegalArgumentException.class, () -> {
            BigNumberCalculate.add("", "123");
        });
    }

    @Test
    public void testEmptyStringB() {
        assertThrows(IllegalArgumentException.class, () -> {
            BigNumberCalculate.add("123", "");
        });
    }

    @Test
    public void testNullA() {
        assertThrows(NullPointerException.class, () -> {
            BigNumberCalculate.add(null, "123");
        });
    }

    @Test
    public void testNullB() {
        assertThrows(NullPointerException.class, () -> {
            BigNumberCalculate.add("123", null);
        });
    }

    @Test
    public void testInvalidCharacterA() {
        assertThrows(IllegalArgumentException.class, () -> {
            BigNumberCalculate.add("12a3", "456");
        });
    }

    @Test
    public void testInvalidCharacterB() {
        assertThrows(IllegalArgumentException.class, () -> {
            BigNumberCalculate.add("123", "45a6");
        });
    }

    @Test
    public void testSingleDigitNumbers() {
        assertEquals("2", BigNumberCalculate.add("1", "1"));
        assertEquals("9", BigNumberCalculate.add("4", "5"));
        assertEquals("10", BigNumberCalculate.add("9", "1"));
    }

    @Test
    public void testEdgeCaseWithMultipleCarries() {
        assertEquals("1000000000", BigNumberCalculate.add("999999999", "1"));
        assertEquals("2000000000", BigNumberCalculate.add("999999999", "1000000001"));
    }

    @Test
    public void testPalindromeNumbers() {
        assertEquals("2222", BigNumberCalculate.add("1111", "1111"));
        assertEquals("12321", BigNumberCalculate.add("12321", "0"));
    }

    @Test
    public void testSequentialNumbers() {
        assertEquals("123456789", BigNumberCalculate.add("123456788", "1"));
        assertEquals("1000000000", BigNumberCalculate.add("999999999", "1"));
    }

    // 以下是add2方法的测试用例
    @Test
    public void testAdd2BasicAddition() {
        assertEquals("5", BigNumberCalculate.add2("2", "3"));
        assertEquals("10", BigNumberCalculate.add2("5", "5"));
        assertEquals("100", BigNumberCalculate.add2("50", "50"));
    }

    @Test
    public void testAdd2CarryHandling() {
        assertEquals("10", BigNumberCalculate.add2("9", "1"));
        assertEquals("100", BigNumberCalculate.add2("99", "1"));
        assertEquals("1000", BigNumberCalculate.add2("999", "1"));
        assertEquals("10000", BigNumberCalculate.add2("9999", "1"));
    }

    @Test
    public void testAdd2LargeNumbers() {
        assertEquals("1111111110", BigNumberCalculate.add2("123456789", "987654321"));
        assertEquals("10000000000000000000", BigNumberCalculate.add2("9999999999999999999", "1"));
    }

    @Test
    public void testAdd2DifferentLengths() {
        assertEquals("12412", BigNumberCalculate.add2("12345", "67"));
        assertEquals("1000001", BigNumberCalculate.add2("1", "1000000"));
        assertEquals("1000000001", BigNumberCalculate.add2("1", "1000000000"));
    }

    @Test
    public void testAdd2ZeroHandling() {
        assertEquals("0", BigNumberCalculate.add2("0", "0"));
        assertEquals("123", BigNumberCalculate.add2("0", "123"));
        assertEquals("123", BigNumberCalculate.add2("123", "0"));
        assertEquals("1000", BigNumberCalculate.add2("000", "1000"));
    }

    @Test
    public void testAdd2LeadingZeros() {
        assertEquals("123", BigNumberCalculate.add2("0123", "000"));
        assertEquals("1000", BigNumberCalculate.add2("0999", "0001"));
        assertEquals("1235", BigNumberCalculate.add2("01234", "00001"));
    }

    @Test
    public void testAdd2SameLengthNumbers() {
        assertEquals("2468", BigNumberCalculate.add2("1234", "1234"));
        assertEquals("11110", BigNumberCalculate.add2("5555", "5555"));
    }

    @Test
    public void testAdd2MaxSingleDigit() {
        assertEquals("18", BigNumberCalculate.add2("9", "9"));
        assertEquals("19", BigNumberCalculate.add2("9", "10"));
    }

    @Test
    public void testAdd2VeryLargeNumbers() {
        String num1 = "123456789012345678901234567890";
        String num2 = "987654321098765432109876543210";
        String expected = "1111111110111111111011111111100";
        assertEquals(expected, BigNumberCalculate.add2(num1, num2));
    }

    @Test
    public void testAdd2AllNines() {
        assertEquals("100", BigNumberCalculate.add2("99", "1"));
        assertEquals("1000", BigNumberCalculate.add2("999", "1"));
        assertEquals("10000", BigNumberCalculate.add2("9999", "1"));
        assertEquals("100000", BigNumberCalculate.add2("99999", "1"));
    }

    @Test
    public void testAdd2EmptyStringA() {
        assertThrows(IllegalArgumentException.class, () -> {
            BigNumberCalculate.add2("", "123");
        });
    }

    @Test
    public void testAdd2EmptyStringB() {
        assertThrows(IllegalArgumentException.class, () -> {
            BigNumberCalculate.add2("123", "");
        });
    }

    @Test
    public void testAdd2NullA() {
        assertThrows(NullPointerException.class, () -> {
            BigNumberCalculate.add2(null, "123");
        });
    }

    @Test
    public void testAdd2NullB() {
        assertThrows(NullPointerException.class, () -> {
            BigNumberCalculate.add2("123", null);
        });
    }

//    @Test
//    public void testAdd2InvalidCharacterA() {
//        assertThrows(IllegalArgumentException.class, () -> {
//            BigNumberCalculate.add2("12a3", "456");
//        });
//    }
//
//    @Test
//    public void testAdd2InvalidCharacterB() {
//        assertThrows(IllegalArgumentException.class, () -> {
//            BigNumberCalculate.add2("123", "45a6");
//        });
//    }

    @Test
    public void testAdd2SingleDigitNumbers() {
        assertEquals("2", BigNumberCalculate.add2("1", "1"));
        assertEquals("9", BigNumberCalculate.add2("4", "5"));
        assertEquals("10", BigNumberCalculate.add2("9", "1"));
    }

    @Test
    public void testAdd2EdgeCaseWithMultipleCarries() {
        assertEquals("1000000000", BigNumberCalculate.add2("999999999", "1"));
        assertEquals("2000000000", BigNumberCalculate.add2("999999999", "1000000001"));
    }

    @Test
    public void testAdd2PalindromeNumbers() {
        assertEquals("2222", BigNumberCalculate.add2("1111", "1111"));
        assertEquals("12321", BigNumberCalculate.add2("12321", "0"));
    }

    @Test
    public void testAdd2SequentialNumbers() {
        assertEquals("123456789", BigNumberCalculate.add2("123456788", "1"));
        assertEquals("1000000000", BigNumberCalculate.add2("999999999", "1"));
    }

    /* ------------------------- minus 专用测试用例 ------------------------- */

    @Test
    public void testMinusBasic() {
        assertEquals("7", BigNumberCalculate.minus("10", "3"));
        assertEquals("0", BigNumberCalculate.minus("5", "5"));
        assertEquals("123", BigNumberCalculate.minus("200", "77"));
    }

    @Test
    public void testMinusBorrow() {
        assertEquals("9", BigNumberCalculate.minus("10", "1"));
        assertEquals("99", BigNumberCalculate.minus("100", "1"));
        assertEquals("999", BigNumberCalculate.minus("1000", "1"));
    }

    @Test
    public void testMinusLargeNumbers() {
        assertEquals("864197532", BigNumberCalculate.minus("987654321", "123456789"));
        assertEquals("9999999999999999999", BigNumberCalculate.minus("10000000000000000000", "1"));
    }

    @Test
    public void testMinusDifferentLengths() {
        assertEquals("12333", BigNumberCalculate.minus("12345", "12"));
        assertEquals("999999", BigNumberCalculate.minus("1000000", "1"));
    }

    @Test
    public void testMinusZeroHandling() {
        assertEquals("0", BigNumberCalculate.minus("0", "0"));
        assertEquals("123", BigNumberCalculate.minus("123", "0"));
        assertEquals("0", BigNumberCalculate.minus("123", "123"));
    }

    @Test
    public void testMinusLeadingZeros() {
        assertEquals("123", BigNumberCalculate.minus("0123", "000"));
        assertEquals("999", BigNumberCalculate.minus("1000", "0001"));
    }

    @Test
    public void testMinusNegativeResult() {
        assertEquals("-1", BigNumberCalculate.minus("1", "2"));
        assertEquals("-333", BigNumberCalculate.minus("123", "456"));
        assertEquals("-999", BigNumberCalculate.minus("1", "1000"));
    }

    @Test
    public void testMinusSameLengthNumbers() {
        assertEquals("0", BigNumberCalculate.minus("5555", "5555"));
        assertEquals("1357", BigNumberCalculate.minus("2468", "1111"));
    }

    @Test
    public void testMinusEdgeWithMultipleBorrows() {
        assertEquals("999999999", BigNumberCalculate.minus("1000000000", "1"));
    }

    @Test
    public void testMinusEmptyStringA() {
        assertThrows(IllegalArgumentException.class, () -> BigNumberCalculate.minus("", "123"));
    }

    @Test
    public void testMinusEmptyStringB() {
        assertThrows(IllegalArgumentException.class, () -> BigNumberCalculate.minus("123", ""));
    }

    @Test
    public void testMinusNullA() {
        assertThrows(IllegalArgumentException.class, () -> BigNumberCalculate.minus(null, "123"));
    }

    @Test
    public void testMinusNullB() {
        assertThrows(IllegalArgumentException.class, () -> BigNumberCalculate.minus("123", null));
    }
}
