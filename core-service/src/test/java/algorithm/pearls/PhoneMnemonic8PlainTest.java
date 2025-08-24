package algorithm.pearls;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhoneMnemonic8PlainTest {

    @Test
    void testNumberToWords() {
        // 测试数字转字母组合
        List<String> result = PhoneMnemonic8Plain.numberToWords("23");
        List<String> expected = Arrays.asList("AD", "AE", "AF", "BD", "BE", "BF", "CD", "CE", "CF");
        assertEquals(expected, result);

        // 测试包含0或1的数字串
        result = PhoneMnemonic8Plain.numberToWords("201");
        assertEquals(3, result.size()); // 0和1无映射，结果长度为数字串长度
    }

    @Test
    void testBuildIndex() {
        // 测试词典索引构建
        String[] dict = {"FLOWERS", "FLOW", "LOWERS", "LOWER", "FLO", "WER", "S"};
        Map<String, Set<String>> index = PhoneMnemonic8Plain.buildIndex(dict);

        // 验证单词映射是否正确
        assertTrue(index.containsKey("3569377")); // FLOWERS
        assertTrue(index.containsKey("3569"));    // FLOW
        assertTrue(index.containsKey("356"));     // FLO

        // 验证大小写不敏感
        assertTrue(index.get("3569377").contains("FLOWERS"));
    }

    @Test
    void testFindMatchesByKey() {
        // 构建词典索引
        String[] dict = {"FLOWERS", "FLOW", "LOWERS", "LOWER", "FLO", "WER", "S"};
        Map<String, Set<String>> index = PhoneMnemonic8Plain.buildIndex(dict);

        // 测试按键编码 "3569377" 的匹配结果
        Set<String> matches = PhoneMnemonic8Plain.findMatchesByKey("3569377", index);
        assertTrue(matches.contains("FLOWERS"));
        assertFalse(matches.contains("FLOW")); // 确保不匹配其他单词

        // 测试按键编码 "3569" 的匹配结果
        matches = PhoneMnemonic8Plain.findMatchesByKey("3569", index);
        assertTrue(matches.contains("FLOW"));
        assertFalse(matches.contains("FLOWERS")); // 确保不匹配其他单词
    }

}
