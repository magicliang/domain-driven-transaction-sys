package com.magicliang.transaction.sys.common.util;

import java.lang.reflect.Proxy;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 动态代理工厂
 *
 * @author magicliang
 * <p>
 * date: 2022-04-22 19:34
 */
public class DynamicProxyFactory {

    /**
     * 生产代理
     *
     * @param interfaceClass 待代理的接口类，注意，这必须是 T 的 Class
     * @param instance       待代理的对象，注意这个对象必须也是 T 类型
     * @param proxyConfig    代理配置
     * @param <T>            目标接口实例类型
     * @return 目标接口实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProxyInstance(Class<T> interfaceClass, T instance,
                                         ProxyConfig proxyConfig) {
        return (T) Proxy.newProxyInstance(JdkNativeProxyInvocationHandler.class.getClassLoader(),
                new Class[]{interfaceClass},
                new JdkNativeProxyInvocationHandler(interfaceClass.getSimpleName(), instance, proxyConfig));
    }
}
