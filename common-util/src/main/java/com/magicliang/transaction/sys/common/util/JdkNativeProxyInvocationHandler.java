package com.magicliang.transaction.sys.common.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 一个编程式的，纯享版本 Jdk 调用拦截器。非 Spring 版本
 *
 * @author magicliang
 *         <p>
 *         date: 2022-04-22 19:20
 */
@Slf4j
public class JdkNativeProxyInvocationHandler implements InvocationHandler {

    /**
     * 代理类名
     */
    private String proxyClassName;

    /**
     * 被代理的目标
     */
    private Object target;

    /**
     * 代理配置
     */
    private ProxyConfig proxyConfig;

    /**
     * 私有构造器
     *
     * @param proxyClassName
     * @param target
     * @param proxyConfig
     */
    public JdkNativeProxyInvocationHandler(String proxyClassName, Object target, ProxyConfig proxyConfig) {
        this.proxyClassName = proxyClassName;
        this.target = target;
        this.proxyConfig = proxyConfig;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();

        long startTime = System.currentTimeMillis();
        Object result = method.invoke(target, args);
        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;
        if (elapsed >= proxyConfig.getTimeoutThreshold()) {
            log.warn("JdkNativeProxyInvocationHandler timeout, {}.{} cost {}ms, args: {}", proxyClassName, methodName,
                    elapsed, Arrays.toString(args));
        }
        log.info("JdkNativeProxyInvocationHandler, elapsed: {}", elapsed);

        return result;
    }
}
