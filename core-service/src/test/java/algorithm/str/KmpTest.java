package algorithm.str;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 *
 * description: KMP算法全面测试用例
 *
 * @author magicliang
 *
 *         date: 2025-08-15 21:35
 */
public class KmpTest {

    private final Kmp kmp = new Kmp();

    @Test
    public void testBuildNextArray_NormalCase() {
        // 测试正常情况下的next数组构建
        String pattern = "ababaca";
        int[] expected = {0, 0, 1, 2, 3, 0, 1};
        int[] actual = kmp.buildNextArray(pattern);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBuildNextArray_RepeatedChars() {
        // 测试重复字符的情况
        String pattern = "aaaa";
        int[] expected = {0, 1, 2, 3};
        int[] actual = kmp.buildNextArray(pattern);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBuildNextArray_NoRepeats() {
        // 测试无重复字符的情况
        String pattern = "abcd";
        int[] expected = {0, 0, 0, 0};
        int[] actual = kmp.buildNextArray(pattern);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBuildNextArray_SingleChar() {
        // 测试单字符情况
        String pattern = "a";
        int[] expected = {0};
        int[] actual = kmp.buildNextArray(pattern);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBuildNextArray_TwoChars() {
        // 测试两个字符的情况
        String pattern = "ab";
        int[] expected = {0, 0};
        int[] actual = kmp.buildNextArray(pattern);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearch_NormalMatch() {
        // 测试正常匹配情况
        String text = "hello world";
        String pattern = "world";
        int result = kmp.search(text, pattern);
        assertEquals(6, result);
    }

    @Test
    public void testSearch_MatchAtStart() {
        // 测试在开头匹配的情况
        String text = "abcdefg";
        String pattern = "abc";
        int result = kmp.search(text, pattern);
        assertEquals(0, result);
    }

    @Test
    public void testSearch_MatchAtEnd() {
        // 测试在末尾匹配的情况
        String text = "abcdefg";
        String pattern = "efg";
        int result = kmp.search(text, pattern);
        assertEquals(4, result);
    }

    @Test
    public void testSearch_NoMatch() {
        // 测试无匹配的情况
        String text = "abcdefg";
        String pattern = "xyz";
        int result = kmp.search(text, pattern);
        assertEquals(-1, result);
    }

    @Test
    public void testSearch_EmptyPattern() {
        // 测试空模式串的情况
        String text = "abcdefg";
        String pattern = "";
        int result = kmp.search(text, pattern);
        assertEquals(0, result); // 空字符串在任何位置都匹配，返回0
    }

    @Test
    public void testSearch_EmptyText() {
        // 测试空文本的情况
        String text = "";
        String pattern = "abc";
        int result = kmp.search(text, pattern);
        assertEquals(-1, result);
    }

    @Test
    public void testSearch_PatternLongerThanText() {
        // 测试模式串比文本长的情况
        String text = "abc";
        String pattern = "abcdef";
        int result = kmp.search(text, pattern);
        assertEquals(-1, result);
    }

    @Test
    public void testSearch_MultipleMatches() {
        // 测试多个匹配的情况（返回第一个匹配位置）
        String text = "abababab";
        String pattern = "aba";
        int result = kmp.search(text, pattern);
        assertEquals(0, result); // 应该返回第一个匹配的位置
    }

    @Test
    public void testSearch_OverlappingMatches() {
        // 测试重叠匹配的情况
        String text = "aaaa";
        String pattern = "aa";
        int result = kmp.search(text, pattern);
        assertEquals(0, result); // 应该返回第一个匹配的位置
    }

    @Test
    public void testSearch_CaseSensitive() {
        // 测试大小写敏感
        String text = "Hello World";
        String pattern = "world";
        int result = kmp.search(text, pattern);
        assertEquals(-1, result);
    }

    @Test
    public void testSearch_SpecialCharacters() {
        // 测试特殊字符
        String text = "a@b#c$d%e";
        String pattern = "#c$";
        int result = kmp.search(text, pattern);
        assertEquals(3, result);
    }

    @Test
    public void testSearch_Numbers() {
        // 测试数字字符串
        String text = "1234567890";
        String pattern = "456";
        int result = kmp.search(text, pattern);
        assertEquals(3, result);
    }

    @Test
    public void testSearch_UnicodeCharacters() {
        // 测试Unicode字符
        String text = "你好世界";
        String pattern = "世界";
        int result = kmp.search(text, pattern);
        assertEquals(2, result);
    }

    @Test
    public void testSearch_ComplexPattern() {
        // 测试复杂模式串
        String text = "abcabcabcdabcabc";
        String pattern = "abcabcd";
        int result = kmp.search(text, pattern);
        assertEquals(3, result);
    }

    @Test
    public void testSearch_SingleCharMatch() {
        // 测试单字符匹配
        String text = "abcdefg";
        String pattern = "d";
        int result = kmp.search(text, pattern);
        assertEquals(3, result);
    }

    @Test
    public void testSearch_SingleCharNoMatch() {
        // 测试单字符不匹配
        String text = "abcdefg";
        String pattern = "z";
        int result = kmp.search(text, pattern);
        assertEquals(-1, result);
    }

    @Test
    public void testSearch_LargeText() {
        // 测试大文本情况
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            text.append("a");
        }
        text.append("pattern");

        String pattern = "pattern";
        int result = kmp.search(text.toString(), pattern);
        assertEquals(1000, result);
    }

    @Test
    public void testSearch_PeriodicPattern() {
        // 测试周期性模式
        String text = "abcabcabcabc";
        String pattern = "abcabc";
        int result = kmp.search(text, pattern);
        assertEquals(0, result);
    }

    @Test
    public void testSearch_AllSameCharacters() {
        // 测试所有字符相同的情况
        String text = "aaaaaaaaaa";
        String pattern = "aaaa";
        int result = kmp.search(text, pattern);
        assertEquals(0, result);
    }

    @Test
    public void testSearch_PatternEqualsText() {
        // 测试模式串等于文本的情况
        String text = "abcdef";
        String pattern = "abcdef";
        int result = kmp.search(text, pattern);
        assertEquals(0, result);
    }

    @Test
    public void testSearch_WhitespaceHandling() {
        // 测试空白字符处理
        String text = "hello   world";
        String pattern = "   ";
        int result = kmp.search(text, pattern);
        assertEquals(5, result);
    }

    @Test
    public void testSearch_NewlineCharacters() {
        // 测试换行符处理
        String text = "hello\nworld\ntest";
        String pattern = "world";
        int result = kmp.search(text, pattern);
        assertEquals(6, result);
    }
}