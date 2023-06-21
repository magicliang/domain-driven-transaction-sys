package com.magicliang.transaction.sys.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public static final String JSON_EXCEPTION_MSG = "反序列化失败";
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
    private static Map<String, ObjectMapper> MAPPER_CACHE = new ConcurrentHashMap<>();

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
        OBJECT_MAPPER_CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 私有构造器
     */
    private JsonUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 将Java对象转成Json，默认约束规则JsonInclude.Include.NON_EMPTY
     * 注意，原始的字符串 123 序列化后会变成 "123"
     *
     * @param object     原始对象
     * @param include    序列化的包含参数
     * @param jsonCached 是否激活 cache
     * @return 序列化后字符串
     */
    private static String toJson(Object object, JsonInclude.Include include, boolean jsonCached) {
        if (object == null) {
            return "";
        }
        ObjectMapper objectMapper;
        if (include == null || include == JsonInclude.Include.NON_EMPTY) {
            if (jsonCached) {
                objectMapper = OBJECT_MAPPER_INCLUDE_NONEMPTY;
            } else {
                objectMapper = OBJECT_MAPPER_INCLUDE_NONEMPTY_NO_CACHE;
            }
        } else if (include == JsonInclude.Include.ALWAYS) {
            if (jsonCached) {
                objectMapper = OBJECT_MAPPER_INCLUDE_ALWAYS;
            } else {
                objectMapper = OBJECT_MAPPER_INCLUDE_ALWAYS_NO_CACHE;
            }
        } else {
            final String cacheKey = include + "-" + jsonCached;
            objectMapper = MAPPER_CACHE.get(cacheKey);
            if (null == objectMapper) {
                objectMapper = new ObjectMapper();
                objectMapper.setSerializationInclusion(include);
                if (!jsonCached) {
                    objectMapper.getFactory().disable(JsonFactory.Feature.USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING);
                }
                MAPPER_CACHE.put(cacheKey, objectMapper);
            }
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            log.error(JSON_EXCEPTION_MSG, e);
        }
        return "";
    }

    /**
     * 将Java对象转成Json，约束规则JsonInclude.Include.NON_EMPTY
     * 默认的序列化行为就是会把空串去掉的
     *
     * @param object 原始对象
     * @return 序列化后字符串
     */
    public static String toJson(Object object) {
        return toJson(object, JsonInclude.Include.NON_EMPTY);
    }

    /**
     * 将 Java 对象转成 Json，约束规则 JsonInclude.Include.NON_NULL
     *
     * @param object 原始对象
     * @return 序列化后字符串
     */
    public static String toJsonWithNonNull(Object object) {
        return toJson(object, JsonInclude.Include.NON_NULL);
    }

    /**
     * 不带缓存地将Java对象转成Json，默认约束规则JsonInclude.Include.NON_EMPTY
     *
     * @param object 原始对象
     * @return 序列化后字符串
     */
    public static String toJsonNoCache(Object object) {
        return toJson(object, JsonInclude.Include.NON_EMPTY, false);
    }

    /**
     * 将Java对象转成Json，默认约束规则JsonInclude.Include.NON_EMPTY
     *
     * @param object  原始对象
     * @param include 包含策略
     * @return 序列化后字符串
     */
    public static String toJson(Object object, JsonInclude.Include include) {
        // 缺省时是激活缓存的
        return toJson(object, include, true);
    }

    /**
     * 转换成小写下划线类型
     *
     * @param json  原始 json 字符串
     * @param clazz 类对象
     * @param <T>   列表元素类型
     * @return 反序列结果
     */
    public static <T> T toObjectCamelIgnore(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER_CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES.readValue(json, clazz);
        } catch (IOException e) {
            log.error(JSON_EXCEPTION_MSG, e);
        }
        return null;
    }

    /**
     * 将 json 转成 Java 对象
     *
     * @param json  原始 json 字符串
     * @param clazz 类对象
     * @param <T>   列表元素类型
     * @return 反序列结果
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER_INCLUDE_ALWAYS.readValue(json, clazz);
        } catch (IOException e) {
            log.error(JSON_EXCEPTION_MSG, e);
        }
        return null;
    }

    /**
     * 将 json 转成 map
     *
     * @param json           原始 json 字符串
     * @param elementClasses 类对象
     * @param <T>            列表元素类型
     * @return 反序列结果
     */
    public static <T> Map<String, T> toMapObject(String json, Class<T> elementClasses) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        JavaType javaType = getCollectionType(HashMap.class, String.class, elementClasses);
        try {
            return OBJECT_MAPPER_INCLUDE_ALWAYS.readValue(json, javaType);
        } catch (IOException e) {
            log.error(JSON_EXCEPTION_MSG, e);
        }
        return null;
    }

    /**
     * 把 json 反序列化为列表
     * 反序列化的时候使用 INCLUDE_ALWAYS 的策略也没关系，因为这个策略主要是用在 serialize 这个流程上的
     *
     * @param json  原始 json 字符串
     * @param clazz 类对象
     * @param <T>   列表元素类型
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
            log.error(JSON_EXCEPTION_MSG, e);
        }
        return null;
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  类对象
     * @return JavaType Java类型
     */
    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OBJECT_MAPPER_INCLUDE_ALWAYS.getTypeFactory()
                .constructParametricType(collectionClass, elementClasses);
    }


    /**
     * 转化成 json node
     *
     * @param json 原始 json 字符串
     * @return 构建成功的 json node
     */
    public static JsonNode toJsonNode(String json) {
        try {
            return OBJECT_MAPPER_INCLUDE_ALWAYS.readTree(json);
        } catch (Exception e) {
            log.error("Convert {} to JsonNode error.", json, e);
        }

        return null;
    }
}
