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
    void testNumberToPhrase() {
        // 测试数字串转单词串
        String[] dict = {"FLOWERS", "FLOW", "LOWERS", "LOWER", "FLO", "WER", "ER", "S"};
        Map<String, Set<String>> index = PhoneMnemonic8Plain.buildIndex(dict);

        List<List<String>> phrases = PhoneMnemonic8Plain.numberToPhrase("3569377", index);

        // 验证结果是否包含预期的单词组合
        boolean found = phrases.stream().anyMatch(
                phrase -> phrase.equals(Arrays.asList("FLOWERS"))
        );
        assertTrue(found);

        found = phrases.stream().anyMatch(
                phrase -> phrase.equals(Arrays.asList("FLOW", "ER", "S"))
        );
        assertTrue(found);
    }
}
