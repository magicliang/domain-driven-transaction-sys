package com.magicliang.transaction.sys.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于 Jackson 的严格 JSON 内容比较工具
 * 支持比较乱序 JSON 的键值对差异
 *
 * @author Aone Copilot
 */
public class JsonDiffToolJackson {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 差异类型枚举
     */
    public enum DiffType {
        TYPE_DIFF("类型不同"),
        VALUE_DIFF("值不同"),
        KEY_ONLY_IN_LEFT("键只存在于左侧"),
        KEY_ONLY_IN_RIGHT("键只存在于右侧"),
        LENGTH_DIFF("长度不同"),
        EXTRA_IN_LEFT("左侧多余元素"),
        EXTRA_IN_RIGHT("右侧多余元素");

        private final String description;

        DiffType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 差异记录
     */
    public static class Difference {
        private final DiffType type;
        private final String path;
        private final JsonNode leftValue;
        private final JsonNode rightValue;

        public Difference(DiffType type, String path, JsonNode leftValue, JsonNode rightValue) {
            this.type = type;
            this.path = path;
            this.leftValue = leftValue;
            this.rightValue = rightValue;
        }

        @Override
        public String toString() {
            switch (type) {
                case TYPE_DIFF:
                    return String.format("[%s] %s: type(%s) != type(%s)",
                            type.name(), path,
                            getTypeName(leftValue), getTypeName(rightValue));
                case VALUE_DIFF:
                    return String.format("[%s] %s: %s != %s",
                            type.name(), path,
                            formatValue(leftValue), formatValue(rightValue));
                case KEY_ONLY_IN_LEFT:
                    return String.format("[%s] %s: %s",
                            type.name(), path, formatValue(leftValue));
                case KEY_ONLY_IN_RIGHT:
                    return String.format("[%s] %s: %s",
                            type.name(), path, formatValue(rightValue));
                case LENGTH_DIFF:
                    return String.format("[%s] %s: len(%s) != len(%s)",
                            type.name(), path,
                            leftValue != null ? leftValue.size() : 0,
                            rightValue != null ? rightValue.size() : 0);
                case EXTRA_IN_LEFT:
                    return String.format("[%s] %s: %s",
                            type.name(), path, formatValue(leftValue));
                case EXTRA_IN_RIGHT:
                    return String.format("[%s] %s: %s",
                            type.name(), path, formatValue(rightValue));
                default:
                    return String.format("[%s] %s", type.name(), path);
            }
        }

        private String getTypeName(JsonNode node) {
            if (node == null || node.isNull()) return "null";
            if (node.isObject()) return "Object";
            if (node.isArray()) return "Array";
            if (node.isTextual()) return "String";
            if (node.isNumber()) return "Number";
            if (node.isBoolean()) return "Boolean";
            return "Unknown";
        }

        private String formatValue(JsonNode value) {
            if (value == null || value.isNull()) return "null";
            if (value.isTextual()) return "\"" + value.asText() + "\"";
            return value.toString();
        }

        public DiffType getType() {
            return type;
        }
    }

    private final List<Difference> differences = new ArrayList<>();

    /**
     * 比较两个 JSON 对象
     *
     * @param json1 第一个 JSON 对象
     * @param json2 第二个 JSON 对象
     * @return 差异列表
     */
    public List<Difference> compare(JsonNode json1, JsonNode json2) {
        return compare(json1, json2, "root");
    }

    /**
     * 比较两个 JSON 对象（带路径）
     *
     * @param json1 第一个 JSON 对象
     * @param json2 第二个 JSON 对象
     * @param path  当前路径
     * @return 差异列表
     */
    public List<Difference> compare(JsonNode json1, JsonNode json2, String path) {
        differences.clear();
        compareRecursive(json1, json2, path);
        return new ArrayList<>(differences);
    }

    /**
     * 递归比较两个节点
     */
    private void compareRecursive(JsonNode node1, JsonNode node2, String path) {
        // 处理 null 值
        if ((node1 == null || node1.isNull()) && (node2 == null || node2.isNull())) {
            return;
        }
        if (node1 == null || node1.isNull() || node2 == null || node2.isNull()) {
            differences.add(new Difference(DiffType.VALUE_DIFF, path, node1, node2));
            return;
        }

        // 类型不同
        if (!isSameType(node1, node2)) {
            differences.add(new Difference(DiffType.TYPE_DIFF, path, node1, node2));
            return;
        }

        // Object 比较
        if (node1.isObject()) {
            compareObject((ObjectNode) node1, (ObjectNode) node2, path);
        }
        // Array 比较
        else if (node1.isArray()) {
            compareArray((ArrayNode) node1, (ArrayNode) node2, path);
        }
        // 基本类型比较
        else {
            if (!node1.equals(node2)) {
                differences.add(new Difference(DiffType.VALUE_DIFF, path, node1, node2));
            }
        }
    }

    /**
     * 比较两个 Object 节点
     */
    private void compareObject(ObjectNode obj1, ObjectNode obj2, String path) {
        Set<String> keys1 = new TreeSet<>();
        obj1.fieldNames().forEachRemaining(keys1::add);

        Set<String> keys2 = new TreeSet<>();
        obj2.fieldNames().forEachRemaining(keys2::add);

        // 只在 obj1 中存在的键
        Set<String> onlyInLeft = new TreeSet<>(keys1);
        onlyInLeft.removeAll(keys2);
        for (String key : onlyInLeft) {
            String newPath = path + "." + key;
            differences.add(new Difference(DiffType.KEY_ONLY_IN_LEFT, newPath, obj1.get(key), null));
        }

        // 只在 obj2 中存在的键
        Set<String> onlyInRight = new TreeSet<>(keys2);
        onlyInRight.removeAll(keys1);
        for (String key : onlyInRight) {
            String newPath = path + "." + key;
            differences.add(new Difference(DiffType.KEY_ONLY_IN_RIGHT, newPath, null, obj2.get(key)));
        }

        // 两者都存在的键，递归比较值
        Set<String> commonKeys = new TreeSet<>(keys1);
        commonKeys.retainAll(keys2);
        for (String key : commonKeys) {
            String newPath = path + "." + key;
            compareRecursive(obj1.get(key), obj2.get(key), newPath);
        }
    }

    /**
     * 比较两个 Array 节点
     */
    private void compareArray(ArrayNode arr1, ArrayNode arr2, String path) {
        int len1 = arr1.size();
        int len2 = arr2.size();

        // 长度不同
        if (len1 != len2) {
            differences.add(new Difference(DiffType.LENGTH_DIFF, path, arr1, arr2));
        }

        // 逐个元素比较
        int minLen = Math.min(len1, len2);
        for (int i = 0; i < minLen; i++) {
            String newPath = path + "[" + i + "]";
            compareRecursive(arr1.get(i), arr2.get(i), newPath);
        }

        // 如果长度不同，列出多余的元素
        if (len1 > len2) {
            for (int i = len2; i < len1; i++) {
                String newPath = path + "[" + i + "]";
                differences.add(new Difference(DiffType.EXTRA_IN_LEFT, newPath, arr1.get(i), null));
            }
        } else if (len2 > len1) {
            for (int i = len1; i < len2; i++) {
                String newPath = path + "[" + i + "]";
                differences.add(new Difference(DiffType.EXTRA_IN_RIGHT, newPath, null, arr2.get(i)));
            }
        }
    }

    /**
     * 判断两个节点是否为相同类型
     */
    private boolean isSameType(JsonNode node1, JsonNode node2) {
        if (node1 == null || node2 == null) {
            return node1 == node2;
        }
        if (node1.isNull() && node2.isNull()) return true;
        if (node1.isObject() && node2.isObject()) return true;
        if (node1.isArray() && node2.isArray()) return true;
        if (node1.isTextual() && node2.isTextual()) return true;
        if (node1.isNumber() && node2.isNumber()) return true;
        if (node1.isBoolean() && node2.isBoolean()) return true;
        return false;
    }

    /**
     * 重复字符串（Java 8 兼容）
     */
    private static String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 打印差异摘要
     */
    public static void printDiffSummary(List<Difference> differences) {
        if (differences.isEmpty()) {
            System.out.println("\n✅ 两个 JSON 完全相同");
            return;
        }

        System.out.println("\n❌ 发现 " + differences.size() + " 处差异:\n");
        System.out.println(repeat("=", 80));

        // 按类型分组统计
        Map<DiffType, Long> typeCounts = differences.stream()
                .collect(Collectors.groupingBy(Difference::getType, Collectors.counting()));

        System.out.println("\n差异类型统计:");
        typeCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println("  [" + entry.getKey() + "]: " + entry.getValue() + " 处"));

        System.out.println("\n" + repeat("=", 80));
        System.out.println("\n详细差异列表:\n");

        for (int i = 0; i < differences.size(); i++) {
            System.out.println((i + 1) + ". " + differences.get(i));
        }

        System.out.println("\n" + repeat("=", 80));
    }

    /**
     * 从文件加载 JSON
     */
    public static JsonNode loadJsonFromFile(String filePath) throws IOException {
        return MAPPER.readTree(new File(filePath));
    }

    /**
     * 从字符串加载 JSON
     */
    public static JsonNode loadJsonFromString(String jsonStr) throws IOException {
        return MAPPER.readTree(jsonStr);
    }

    /**
     * 主函数 - 演示用法
     */
    public static void main(String[] args) {
        System.out.println(repeat("=", 80));
        System.out.println("基于 Jackson 的严格 JSON 内容比较工具");
        System.out.println(repeat("=", 80));

        try {
            JsonNode json1, json2;

            if (args.length == 2) {
                // 从文件读取
                System.out.println("\n从文件读取 JSON:");
                System.out.println("  文件1: " + args[0]);
                System.out.println("  文件2: " + args[1]);

                json1 = loadJsonFromFile(args[0]);
                json2 = loadJsonFromFile(args[1]);
            } else {
                // 使用内置示例
                System.out.println("\n使用内置示例进行演示\n");
                System.out.println("提示: 可以通过命令行参数指定两个 JSON 文件:");
                System.out.println("  java JsonDiffToolJackson file1.json file2.json\n");

                // 示例 JSON（乱序）
                String json1Str = "";
                String json2Str = "";

                json1 = loadJsonFromString(json1Str);
                json2 = loadJsonFromString(json2Str);

                System.out.println("JSON 1:");
                System.out.println(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(json1));
                System.out.println("\nJSON 2:");
                System.out.println(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(json2));
            }

            // 执行比较
            JsonDiffToolJackson tool = new JsonDiffToolJackson();
            List<Difference> differences = tool.compare(json1, json2);

            // 打印结果
            printDiffSummary(differences);

        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
