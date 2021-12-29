package com.magicliang.transaction.sys.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: json 工具
 * 本工具类主要基于 Jackson
 *
 * @author magicliang
 * <p>
 * date: 2021-12-29 15:02
 */
@Slf4j
public class JsonUtils {

    /**
     * 没有排除任何字段的映射器
     */
    private static final ObjectMapper OBJECT_MAPPER_INCLUDE_ALWAYS = new ObjectMapper();

    /**
     * 排除任何有 empty、blank 语义的字段的映射器
     */
    private static final ObjectMapper OBJECT_MAPPER_INCLUDE_NONEMPTY = new ObjectMapper();

    /**
     * 没有排除任何字段的映射器的禁用缓存版本
     */
    private static final ObjectMapper OBJECT_MAPPER_INCLUDE_ALWAYS_NO_CACHE = new ObjectMapper();

    /**
     * 排除任何有 empty、blank 语义的字段的映射器的禁用缓存版本
     */
    private static final ObjectMapper OBJECT_MAPPER_INCLUDE_NONEMPTY_NO_CACHE = new ObjectMapper();

    /**
     * 序列化为 a_b 小写下划线形式的映射器
     */
    private static final ObjectMapper OBJECT_MAPPER_CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES = new ObjectMapper();

    static {
        // 默认开启缓存

        // 先关闭反序列化失败，这样可以提高 util 的兼容性
        OBJECT_MAPPER_INCLUDE_ALWAYS.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 只包含非空字段。最严厉的选项，会排除空字符串、null 和 []
        OBJECT_MAPPER_INCLUDE_NONEMPTY.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        OBJECT_MAPPER_INCLUDE_NONEMPTY.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        OBJECT_MAPPER_INCLUDE_ALWAYS_NO_CACHE.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 关闭缓存的ObjectMapper。这个 disable 虽然 deprecated 了，但因为取不到原生的 factory，所以暂且继续用
        OBJECT_MAPPER_INCLUDE_ALWAYS_NO_CACHE.getFactory().disable(JsonFactory.Feature.USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING);

        // 只包含非空字段，且关闭缓存
        OBJECT_MAPPER_INCLUDE_NONEMPTY_NO_CACHE.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        OBJECT_MAPPER_INCLUDE_NONEMPTY_NO_CACHE.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 在本 mapper 上禁用线程局部缓存，这样对性能可能不好，但会对 gc 好，也防止内存泄漏：https://github.com/fasterxml/jackson-core/issues/189
        OBJECT_MAPPER_INCLUDE_NONEMPTY_NO_CACHE.getFactory().disable(JsonFactory.Feature.USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING);

        // 序列化为 a_b 小写下划线形式
        OBJECT_MAPPER_CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    }

    /**
     * 私有构造器
     */
    private JsonUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 把 json 反序列化为列表
     *
     * @param json  原始 json 字符串
     * @param clazz 类型目击者
     * @param <T>   列表元素 类型
     * @return 反序列结果
     */
    public static <T> List<T> toList(String json, Class<T>... clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        ObjectMapper objectMapper = OBJECT_MAPPER_INCLUDE_ALWAYS;
        JavaType javaType = TypeFactory.defaultInstance().constructParametricType(List.class, clazz);
        try {
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            log.error("JSON转换异常", e);
        }
        return null;
    }
}
