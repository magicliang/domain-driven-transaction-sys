package algorithm.pearls;

import java.util.*;


/**
 * 纯循环实现 Mike Lesk 电话簿三大功能（Java 8 无 Stream）
 * 1) 数字串 -> 所有字母组合
 * 2) 词典 -> 单词->号码索引
 * 3) 号码 -> 词典中可拼写单词串
 *
 * 方法使用流程：
 * 1. numberToWords(String digits):
 *    - 输入数字串（如 "23"），返回所有可能的字母组合（如 ["AD", "AE", "AF", "BD", "BE", "BF", "CD", "CE", "CF"]）。
 *    - 适用于生成电话号码对应的字母组合。
 *
 * 2. buildIndex(String[] dict):
 *    - 输入词典数组（如 ["FLOWERS", "FLOW"]），返回数字键到单词集合的映射。
 *    - 示例输出：{"3569377": ["FLOWERS"], "3569": ["FLOW"]}。
 *    - 适用于构建词典索引，便于后续查询。
 *
 * 3. numberToPhrase(String digits, Map<String, Set<String>> index):
 *    - 输入数字串（如 "3569377"）和词典索引，返回所有可能的单词串组合。
 *    - 示例输出：[["FLOWERS"], ["FLOW", "ER", "S"]]。
 *    - 适用于将数字串转换为词典中可拼写的单词串。
 *
 * @author liangchuan
 */
public class PhoneMnemonic8Plain {

    private static final String[] MAP = {
            "", "", "ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ"
    };

    /* ---------- 1. 数字 -> 字母组合 ---------- */

    /**
     * 将数字串转换为所有可能的字母组合
     * 1. 初始化结果列表和路径数组
     * 2. 调用深度优先搜索（DFS）方法生成所有组合
     * 3. DFS 逻辑：
     * - 终止条件：当处理完所有数字时，将当前路径加入结果
     * - 跳过数字 0 和 1（无字母映射）
     * - 遍历当前数字对应的字母，递归生成所有可能的组合
     *
     * @param digits 输入的数字串
     * @return 所有可能的字母组合列表
     */
    public static List<String> numberToWords(String digits) {
        List<String> res = new ArrayList<>();
        char[] path = new char[digits.length()];
        dfs(digits.toCharArray(), 0, path, res);
        return res;
    }

    /**
     * 深度优先搜索（DFS）生成所有可能的字母组合
     * 示例：输入数字 "23"，输出 ["AD", "AE", "AF", "BD", "BE", "BF", "CD", "CE", "CF"]
     * 执行步骤：
     * 1. 初始调用：dfs(['2','3'], 0, [' ',' '], [])
     *    - pos=0, d=2, letters="ABC"
     *    - 遍历 "ABC"：
     *      a. path[0]='A' → 递归 dfs(['2','3'], 1, ['A',' '], [])
     *         - pos=1, d=3, letters="DEF"
     *         - 遍历 "DEF"：
     *           i. path[1]='D' → 递归 dfs(['2','3'], 2, ['A','D'], [])
     *              - pos=2 → 终止，res=["AD"]
     *           ii. path[1]='E' → 递归 dfs(['2','3'], 2, ['A','E'], [])
     *              - pos=2 → 终止，res=["AD", "AE"]
     *           iii. path[1]='F' → 递归 dfs(['2','3'], 2, ['A','F'], [])
     *              - pos=2 → 终止，res=["AD", "AE", "AF"]
     *      b. path[0]='B' → 递归 dfs(['2','3'], 1, ['B',' '], [])
     *         - pos=1, d=3, letters="DEF"
     *         - 遍历 "DEF"：
     *           i. path[1]='D' → res=["AD", "AE", "AF", "BD"]
     *           ii. path[1]='E' → res=[..., "BE"]
     *           iii. path[1]='F' → res=[..., "BF"]
     *      c. path[0]='C' → 递归 dfs(['2','3'], 1, ['C',' '], [])
     *         - pos=1, d=3, letters="DEF"
     *         - 遍历 "DEF"：
     *           i. path[1]='D' → res=[..., "CD"]
     *           ii. path[1]='E' → res=[..., "CE"]
     *           iii. path[1]='F' → res=[..., "CF"]
     *
     * @param digits 输入的数字字符数组
     * @param pos 当前处理的数字位置
     * @param path 当前路径（字母组合的中间结果）
     * @param res 存储所有可能的字母组合的结果列表
     */
    private static void dfs(char[] digits, int pos, char[] path, List<String> res) {
        // 终止条件：处理完所有数字，将当前路径加入结果
        if (pos == digits.length) {
            res.add(new String(path));
            return;
        }
        // 获取当前数字 d，并检查是否为 0 或 1（无字母映射）
        int d = digits[pos] - '0';
        if (d < 2) {
            // 跳过当前数字，递归处理下一个数字
            dfs(digits, pos + 1, path, res);
            return;
        }
        // 获取当前数字对应的字母集合
        String letters = MAP[d];
        // 遍历字母集合中的每个字母
        for (int i = 0, len = letters.length(); i < len; i++) {
            // 将字母填入 path 的当前位置
            path[pos] = letters.charAt(i);
            // 递归处理下一个数字
            dfs(digits, pos + 1, path, res);
        }
    }

    /**
     * 构建词典索引：将单词转换为对应的数字键，并建立映射关系
     * 示例：输入词典 ["FLOWERS", "FLOW"]
     * 执行步骤：
     * 1. 遍历词典中的每个单词 w
     *    - 调用 wordToNumber(w) 将单词转换为数字键（如 "FLOWERS" → "3569377"）
     *    - 将单词添加到对应数字键的集合中（map.computeIfAbsent）
     * 2. 返回数字键到单词集合的映射
     *
     * 输出示例：
     * Word: FLOWERS -> Key: 3569377
     * Word: FLOW -> Key: 3569
     * 最终返回的映射：
     * {
     *   "3569377": ["FLOWERS"],
     *   "3569": ["FLOW"]
     * }
     *
     * @param dict 输入的词典数组
     * @return 数字键到单词集合的映射
     */
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


    /**
     * 将单词转换为对应的数字键
     * 示例：输入 "FLOWERS"，输出 "3569377"
     * 执行步骤：
     * 1. 遍历单词的每个字符 c
     *    - 将字符转换为大写
     *    - 遍历数字 2-9，检查字符是否在 MAP[d] 中
     *      - 如果是，将数字 d 添加到结果中
     * 2. 返回生成的数字键
     *
     * @param w 输入的单词
     * @return 对应的数字键
     */
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

    /**
     * 将数字串转换为词典中可拼写的单词串组合
     * 示例：输入 "3569377" 和索引 {"3569377": ["FLOWERS"]}
     * 执行步骤：
     * 1. 初始化结果列表 res
     * 2. 调用 backtrack 方法生成所有可能的单词串组合
     * 3. 返回结果
     *
     * @param digits 输入的数字串
     * @param index 词典索引（数字键到单词集合的映射）
     * @return 所有可能的单词串组合
     */
    public static List<List<String>> numberToPhrase(String digits,
                                                    Map<String, Set<String>> index) {
        List<List<String>> res = new ArrayList<>();
        char[] num = digits.toCharArray();
        backtrack(num, 0, new ArrayList<>(), res, index);
        return res;
    }

    /**
     * 回溯生成所有可能的单词串组合
     * 示例：输入数字 "3569377"，尝试匹配词典中的单词
     * 执行步骤：
     * 1. 终止条件：处理完所有数字时，将当前路径加入结果
     * 2. 尝试匹配长度为1的单词（如 "S"）：
     *    - 提取当前数字 key1 = num[pos]
     *    - 如果索引中存在 key1，遍历所有匹配的单词，递归处理剩余数字
     * 3. 尝试匹配长度为2的单词（如 "ER"）：
     *    - 提取当前数字 key2 = num[pos..pos+1]
     *    - 如果索引中存在 key2，遍历所有匹配的单词，递归处理剩余数字
     * 4. 尝试匹配长度为3到任意长度的单词（如 "FLOW"）：
     *    - 提取当前数字 key = num[pos..pos+len-1]
     *    - 如果索引中存在 key，遍历所有匹配的单词，递归处理剩余数字
     *
     * @param num 输入的数字字符数组
     * @param pos 当前处理的数字位置
     * @param path 当前路径（单词串的中间结果）
     * @param res 存储所有可能的单词串组合的结果列表
     * @param index 词典索引
     */
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
        // 尝试匹配长度为3到任意长度的单词（支持完整单词匹配）
        for (int len = 3; len <= num.length - pos; len++) {
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

    /**
     * 根据按键编码返回所有可能的匹配名字
     * 示例：输入 "3569377"，返回 ["FLOWERS"]
     *
     * @param key   输入的按键编码
     * @param index 词典索引
     * @return 所有可能的匹配名字集合
     */
    public static Set<String> findMatchesByKey(String key, Map<String, Set<String>> index) {
        return index.getOrDefault(key, Collections.emptySet());
    }

    /* ---------- 演示 ---------- */
    public static void main(String[] args) {
        // 1. 生成数字串对应的所有字母组合
        // 示例：输入 "3569377"（对应 "FLOWERS"），生成所有可能的字母组合
        String number = "3569377";          // 1-800-FLOWERS
        System.out.println("字母组合（前 10 个）：");
        List<String> combos = numberToWords(number);
        for (int i = 0; i < Math.min(10, combos.size()); i++) {
            System.out.println(combos.get(i));
        }

        // 2. 构建词典索引：将词典中的单词转换为数字键并建立映射
        // 示例：输入词典 ["FLOWERS", "FLOW"]，输出 {"3569377": ["FLOWERS"], "3569": ["FLOW"]}
        String[] dict = {"FLOWERS", "FLOW", "LOWERS", "LOWER", "FLO", "WER", "S"};
        Map<String, Set<String>> idx = buildIndex(dict);

        // 3. 将数字串转换为词典中可拼写的单词串组合
        // 示例：输入 "3569377" 和索引，输出 [["FLOWERS"], ["FLOW", "ER", "S"]]
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
