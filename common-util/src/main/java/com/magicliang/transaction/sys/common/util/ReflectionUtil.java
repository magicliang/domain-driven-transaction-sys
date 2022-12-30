package com.magicliang.transaction.sys.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2022-12-30 16:02
 */
@Slf4j
public class ReflectionUtil {

    private static final Class<?>[] EMPTY_CLASS_ARR = {};


    /**
     * 私有构造器
     */
    private ReflectionUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * 验证一个类型是不是另一个类型的参数化子类
     * <p>
     * A implements B<C>
     * 则 isGenericSubClass(A.class, B.class, C.class) 为 true
     * 注意，如果存在 D extends C，C extends F
     * D 作为第三个参数返回 true，而 F 返回 false
     *
     * @param subClass                     子类型
     * @param genericSuperClass            超类型，可以是借口
     * @param targetGenericActualParameter 类型实参
     * @return 认定结果
     */
    public static boolean isGenericSubClass(Class<?> subClass, Class<?> genericSuperClass, Class<?> targetGenericActualParameter) {

        if (!genericSuperClass.isAssignableFrom(subClass)) {
            return false;
        }
        final Type genericSuperclass = subClass.getGenericSuperclass();

        if (genericSuperclass instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            final Type rawType = parameterizedType.getRawType();
            // 这里可以确认 Collection 是不是 Collection-而不是 List
            final boolean rawTypeMatch = rawType == genericSuperClass;
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            // 这两行很重要，基本上能够获取类型实参就靠它了，但我们也只能得到 class
            for (Type type : actualTypeArguments) {
                if (type instanceof Class) {
                    if (rawTypeMatch && ((Class<?>) type).isAssignableFrom(targetGenericActualParameter)) {
                        return true;
                    }
                }
            }
        }


        final Type[] genericInterfaceTypes = subClass.getGenericInterfaces();
        for (Type genericInterfaceType : genericInterfaceTypes) {
            if (genericInterfaceType instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType) genericInterfaceType;
                final boolean rawTypeMatch = parameterizedType.getRawType() == genericSuperClass;
                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                // 这两行很重要，基本上能够获取类型实参就靠它了，但我们也只能得到 class
                for (Type type : actualTypeArguments) {
                    if (type instanceof Class) {
                        if (rawTypeMatch && ((Class<?>) type).isAssignableFrom(targetGenericActualParameter)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * isGenericSubClass 的 Spring 版本，只能验证第一个 TypeParameter，验证第二个参数必然返回 false
     *
     * @param subClass                     子类型
     * @param genericSuperClass            超类型，可以是借口
     * @param targetGenericActualParameter 类型实参
     * @return 认定结果
     */
    public static boolean isParameterized(Class<?> subClass, Class<?> genericSuperClass, Class<?> targetGenericActualParameter) {
        final ResolvableType generic = ResolvableType.forClass(subClass).as(genericSuperClass).getGeneric();
        return generic.isAssignableFrom(ResolvableType.forClass(targetGenericActualParameter));
    }

    public static Collection<?> getWildCardType(Class<?> wildCardClass) {
        // Spring 的 CGLIB 子类的 getGenericSuperclass 要取两轮
        Type genericSuperclass = wildCardClass.getGenericSuperclass();
        Type currentGenericClass = null;
        while (null != genericSuperclass && genericSuperclass != currentGenericClass) {
            if (genericSuperclass instanceof Class) {
                final TypeVariable<? extends Class<?>>[] typeParameters = ((Class<?>) genericSuperclass).getTypeParameters();
                for (TypeVariable<? extends Class<?>> typeParameter : typeParameters) {
                    final Type[] bounds = typeParameter.getBounds();
                    for (Type bound : bounds) {
                        log.info(bound.toString());
                    }
                }
            }
            if (genericSuperclass instanceof ParameterizedType) {
                final Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
                return Arrays.stream(actualTypeArguments).map((actualTypeArgument) -> {
                            if (actualTypeArgument instanceof WildcardType) {
                                final Type[] lowerBounds = ((WildcardType) actualTypeArgument).getLowerBounds();
                                final Type[] upperBounds = ((WildcardType) actualTypeArgument).getUpperBounds();
                                final List<Type> result = new ArrayList<>(Arrays.asList(lowerBounds));
                                result.addAll(Arrays.asList(upperBounds));
                                return result;
                            }
                            return Collections.emptyList();
                        }).flatMap(Collection::stream)
                        .collect(Collectors.toList());
            }
            currentGenericClass = genericSuperclass;
            if (genericSuperclass instanceof Class) {
                genericSuperclass = ((Class) genericSuperclass).getGenericSuperclass();
            } else {
                genericSuperclass = null;
            }
        }

        return Collections.emptyList();
    }

}
