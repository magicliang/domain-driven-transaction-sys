package algorithm.pearls;

import java.util.*;


/**
 * 纯循环实现 Mike Lesk 电话簿三大功能（Java 8 无 Stream）
 * 1) 数字串 -> 所有字母组合
 * 2) 词典 -> 单词->号码索引
 * 3) 号码 -> 词典中可拼写单词串
 *
 * @author liangchuan
 */
public class PhoneMnemonic8Plain {

    private static final String[] MAP = {
            "", "", "ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ"
    };

    /* ---------- 1. 数字 -> 字母组合 ---------- */
    public static List<String> numberToWords(String digits) {
        List<String> res = new ArrayList<>();
        char[] path = new char[digits.length()];
        dfs(digits.toCharArray(), 0, path, res);
        return res;
    }

    private static void dfs(char[] digits, int pos, char[] path, List<String> res) {
        if (pos == digits.length) {
            res.add(new String(path));
            return;
        }
        int d = digits[pos] - '0';
        if (d < 2) {                       // 0 或 1 无映射
            dfs(digits, pos + 1, path, res);
            return;
        }
        String letters = MAP[d];
        for (int i = 0, len = letters.length(); i < len; i++) {
            path[pos] = letters.charAt(i);
            dfs(digits, pos + 1, path, res);
        }
    }

    /* ---------- 2. 词典 -> 单词->号码索引 ---------- */
    public static Map<String, Set<String>> buildIndex(String[] dict) {
        Map<String, Set<String>> map = new HashMap<>();
        for (String w : dict) {
            String key = wordToNumber(w);
            System.out.println("Word: " + w + " -> Key: " + key);
            Set<String> set = map.computeIfAbsent(key, k -> new HashSet<>());
            set.add(w.toUpperCase());
        }
        return map;
    }

    private static String wordToNumber(String w) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length(); i++) {
            char c = Character.toUpperCase(w.charAt(i));
            for (int d = 2; d <= 9; d++) {
                if (MAP[d].indexOf(c) >= 0) {
                    sb.append(d);
                    break;
                }
            }
        }
        return sb.toString();
    }

    /* ---------- 3. 号码 -> 词典中可拼写单词串 ---------- */
    public static List<List<String>> numberToPhrase(String digits,
                                                    Map<String, Set<String>> index) {
        List<List<String>> res = new ArrayList<>();
        char[] num = digits.toCharArray();
        backtrack(num, 0, new ArrayList<>(), res, index);
        return res;
    }

    private static void backtrack(char[] num, int pos,
                                  List<String> path,
                                  List<List<String>> res,
                                  Map<String, Set<String>> index) {
        if (pos == num.length) {
            res.add(new ArrayList<>(path));
            return;
        }
        // 尝试匹配长度为1的单词（如"S"）
        if (pos + 1 <= num.length) {
            String key1 = new String(num, pos, 1);
            Set<String> words1 = index.get(key1);
            if (words1 != null) {
                for (String w : words1) {
                    path.add(w);
                    backtrack(num, pos + 1, path, res, index);
                    path.remove(path.size() - 1);
                }
            }
        }
        // 尝试匹配长度为2的单词（如"ER"）
        if (pos + 2 <= num.length) {
            String key2 = new String(num, pos, 2);
            Set<String> words2 = index.get(key2);
            if (words2 != null) {
                for (String w : words2) {
                    path.add(w);
                    backtrack(num, pos + 2, path, res, index);
                    path.remove(path.size() - 1);
                }
            }
        }
        // 尝试匹配长度为3到4的单词
        for (int len = 3; len <= 4 && pos + len <= num.length; len++) {
            String key = new String(num, pos, len);
            Set<String> words = index.get(key);
            if (words != null) {
                for (String w : words) {
                    path.add(w);
                    backtrack(num, pos + len, path, res, index);
                    path.remove(path.size() - 1);
                }
            }
        }
    }

    /* ---------- 演示 ---------- */
    public static void main(String[] args) {
        String number = "3569377";          // 1-800-FLOWERS
        System.out.println("字母组合（前 10 个）：");
        List<String> combos = numberToWords(number);
        for (int i = 0; i < Math.min(10, combos.size()); i++) {
            System.out.println(combos.get(i));
        }

        // 词典
        String[] dict = {"FLOWERS", "FLOW", "LOWERS", "LOWER", "FLO", "WER", "S"};
        Map<String, Set<String>> idx = buildIndex(dict);

        List<List<String>> phrases = numberToPhrase(number, idx);
        System.out.println("\n词典可拼出的单词串：");
        for (List<String> p : phrases) {
            for (int i = 0; i < p.size(); i++) {
                System.out.print(p.get(i));
                if (i < p.size() - 1) System.out.print(' ');
            }
            System.out.println();
        }
    }
}
