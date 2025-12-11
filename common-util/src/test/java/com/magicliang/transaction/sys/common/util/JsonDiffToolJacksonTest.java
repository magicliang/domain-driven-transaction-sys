package com.magicliang.transaction.sys.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.magicliang.transaction.sys.common.util.JsonDiffToolJackson.DiffType;
import com.magicliang.transaction.sys.common.util.JsonDiffToolJackson.Difference;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: JsonDiffToolJackson 测试类
 *
 * @author magicliang
 *         <p>
 *         date: 2025-12-11 14:12
 */
class JsonDiffToolJacksonTest {

    @Test
    void testCompareIdenticalJson() throws IOException {
        String json1 = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        String json2 = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertTrue(differences.isEmpty(), "相同的 JSON 应该没有差异");
    }

    @Test
    void testCompareIdenticalJsonWithDifferentOrder() throws IOException {
        String json1 = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        String json2 = "{\"city\":\"New York\",\"name\":\"John\",\"age\":30}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertTrue(differences.isEmpty(), "键顺序不同的相同 JSON 应该没有差异");
    }

    @Test
    void testCompareValueDifference() throws IOException {
        String json1 = "{\"name\":\"John\",\"age\":30}";
        String json2 = "{\"name\":\"John\",\"age\":31}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertEquals(1, differences.size(), "应该有 1 处差异");
        assertEquals(DiffType.VALUE_DIFF, differences.get(0).getType());
        assertTrue(differences.get(0).toString().contains("root.age"));
    }

    @Test
    void testCompareTypeDifference() throws IOException {
        String json1 = "{\"value\":\"123\"}";
        String json2 = "{\"value\":123}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertEquals(1, differences.size(), "应该有 1 处差异");
        assertEquals(DiffType.TYPE_DIFF, differences.get(0).getType());
    }

    @Test
    void testCompareKeyOnlyInLeft() throws IOException {
        String json1 = "{\"name\":\"John\",\"age\":30,\"email\":\"john@example.com\"}";
        String json2 = "{\"name\":\"John\",\"age\":30}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertEquals(1, differences.size(), "应该有 1 处差异");
        assertEquals(DiffType.KEY_ONLY_IN_LEFT, differences.get(0).getType());
        assertTrue(differences.get(0).toString().contains("email"));
    }

    @Test
    void testCompareKeyOnlyInRight() throws IOException {
        String json1 = "{\"name\":\"John\",\"age\":30}";
        String json2 = "{\"name\":\"John\",\"age\":30,\"email\":\"john@example.com\"}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertEquals(1, differences.size(), "应该有 1 处差异");
        assertEquals(DiffType.KEY_ONLY_IN_RIGHT, differences.get(0).getType());
        assertTrue(differences.get(0).toString().contains("email"));
    }

    @Test
    void testCompareArrays() throws IOException {
        String json1 = "{\"numbers\":[1,2,3]}";
        String json2 = "{\"numbers\":[1,2,4]}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertEquals(1, differences.size(), "应该有 1 处差异");
        assertEquals(DiffType.VALUE_DIFF, differences.get(0).getType());
        assertTrue(differences.get(0).toString().contains("numbers[2]"));
    }

    @Test
    void testCompareArrayLengthDifference() throws IOException {
        String json1 = "{\"numbers\":[1,2,3]}";
        String json2 = "{\"numbers\":[1,2,3,4]}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertEquals(2, differences.size(), "应该有 2 处差异（长度不同 + 多余元素）");
        
        boolean hasLengthDiff = differences.stream()
                .anyMatch(d -> d.getType() == DiffType.LENGTH_DIFF);
        boolean hasExtraInRight = differences.stream()
                .anyMatch(d -> d.getType() == DiffType.EXTRA_IN_RIGHT);
        
        assertTrue(hasLengthDiff, "应该包含长度差异");
        assertTrue(hasExtraInRight, "应该包含右侧多余元素");
    }

    @Test
    void testCompareNestedObjects() throws IOException {
        String json1 = "{\"person\":{\"name\":\"John\",\"address\":{\"city\":\"New York\"}}}";
        String json2 = "{\"person\":{\"name\":\"John\",\"address\":{\"city\":\"Los Angeles\"}}}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertEquals(1, differences.size(), "应该有 1 处差异");
        assertEquals(DiffType.VALUE_DIFF, differences.get(0).getType());
        assertTrue(differences.get(0).toString().contains("person.address.city"));
    }

    @Test
    void testCompareNullValues() throws IOException {
        String json1 = "{\"value\":null}";
        String json2 = "{\"value\":null}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertTrue(differences.isEmpty(), "相同的 null 值应该没有差异");
    }

    @Test
    void testCompareNullVsValue() throws IOException {
        String json1 = "{\"value\":null}";
        String json2 = "{\"value\":\"something\"}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertEquals(1, differences.size(), "null 和值应该有差异");
        assertEquals(DiffType.VALUE_DIFF, differences.get(0).getType());
    }

    @Test
    void testCompareBooleanValues() throws IOException {
        String json1 = "{\"active\":true}";
        String json2 = "{\"active\":false}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertEquals(1, differences.size(), "布尔值不同应该有差异");
        assertEquals(DiffType.VALUE_DIFF, differences.get(0).getType());
    }

    @Test
    void testCompareComplexJson() throws IOException {
        String json1 = "{\"users\":[{\"id\":1,\"name\":\"Alice\"},{\"id\":2,\"name\":\"Bob\"}],\"count\":2}";
        String json2 = "{\"users\":[{\"id\":1,\"name\":\"Alice\"},{\"id\":2,\"name\":\"Charlie\"}],\"count\":2}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertEquals(1, differences.size(), "应该有 1 处差异");
        assertEquals(DiffType.VALUE_DIFF, differences.get(0).getType());
        assertTrue(differences.get(0).toString().contains("users[1].name"));
    }

    @Test
    void testCompareEmptyObjects() throws IOException {
        String json1 = "{}";
        String json2 = "{}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertTrue(differences.isEmpty(), "空对象应该没有差异");
    }

    @Test
    void testCompareEmptyArrays() throws IOException {
        String json1 = "{\"items\":[]}";
        String json2 = "{\"items\":[]}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        assertTrue(differences.isEmpty(), "空数组应该没有差异");
    }

    @Test
    void testDifferenceToString() throws IOException {
        String json1 = "{\"name\":\"John\"}";
        String json2 = "{\"name\":\"Jane\"}";

        JsonNode node1 = JsonDiffToolJackson.loadJsonFromString(json1);
        JsonNode node2 = JsonDiffToolJackson.loadJsonFromString(json2);

        JsonDiffToolJackson tool = new JsonDiffToolJackson();
        List<Difference> differences = tool.compare(node1, node2);

        String diffString = differences.get(0).toString();
        assertFalse(diffString.isEmpty(), "差异描述不应为空");
        assertTrue(diffString.contains("VALUE_DIFF"), "应该包含差异类型");
        assertTrue(diffString.contains("root.name"), "应该包含路径");
    }
}
