package com.magicliang.transaction.sys.common.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 公共工具类
 *
 * @author magicliang
 * <p>
 * date: 2021-12-30 13:40
 */
public class CommonUtils {

    /**
     * 私有构造器
     */
    private CommonUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 在一个集合里寻找重复集合
     *
     * @param collection 候选集合
     * @param <T>        类型参数
     * @return 重复集合
     */
    public static <T> Set<T> findDuplicateByGrouping(Collection<T> collection) {
        return collection.stream()
                // create a map {1=1, 2=1, 3=2, 4=2, 5=1, 7=1, 9=2}
                .collect(Collectors.groupingBy(Function.identity()
                        , Collectors.counting()))
                // Map -> Stream
                .entrySet()
                .stream()
                // if map value > 1, duplicate element
                .filter(m -> m.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
