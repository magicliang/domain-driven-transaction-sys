package algorithm.bignumber;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

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
}