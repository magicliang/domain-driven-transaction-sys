package com.magicliang.transaction.sys.common.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2022-12-30 16:02
 */
public class ReflectionUtil {

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
            final boolean rawTypeMatch = parameterizedType.getRawType() == genericSuperClass;
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
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
}
