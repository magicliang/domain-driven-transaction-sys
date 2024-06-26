package com.magicliang.transaction.sys.common.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import lombok.extern.slf4j.Slf4j;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 动态代理工厂
 *
 * @author magicliang
 *         <p>
 *         date: 2022-04-22 19:34
 */
@Slf4j
public class CustomDynamicProxyFactory {

    /**
     * 生产代理
     * Proxy class 的特点：
     * - Proxy classes are public, final, and not abstract.
     * - The unqualified name of a proxy class is unspecified. The space of class names that begin with the string
     * "$Proxy" should be, however, reserved for proxy classes.
     * - A proxy class extends java.lang.reflect.Proxy.
     * - A proxy class implements exactly the interfaces specified at its creation, in the same order.
     * - If a proxy class implements a non-public interface, then it will be defined in the same package as that
     * interface. Otherwise, the package of a proxy class is also unspecified. Note that package sealing will not
     * prevent a proxy class from being successfully defined in a particular package at runtime, and neither will
     * classes already defined by the same class loader and the same package with particular signers.
     * - Since a proxy class implements all of the interfaces specified at its creation, invoking getInterfaces on its
     * Class object will return an array containing the same list of interfaces (in the order specified at its
     * creation), invoking getMethods on its Class object will return an array of Method objects that include all of the
     * methods in those interfaces, and invoking getMethod will find methods in the proxy interfaces as would be
     * expected.
     * - The Proxy.isProxyClass method will return true if it is passed a proxy class-- a class returned by
     * Proxy.getProxyClass or the class of an object returned by Proxy.newProxyInstance-- and false otherwise.
     * - The java.security.ProtectionDomain of a proxy class is the same as that of system classes loaded by the
     * bootstrap class loader, such as java.lang.Object, because the code for a proxy class is generated by trusted
     * system code. This protection domain will typically be granted java.security.AllPermission.
     * - Each proxy class has one public constructor that takes one argument, an implementation of the interface
     * InvocationHandler, to set the invocation handler for a proxy instance. Rather than having to use the reflection
     * API to access the public constructor, a proxy instance can be also be created by calling the
     * Proxy.newProxyInstance method, which combines the actions of calling Proxy.getProxyClass with invoking the
     * constructor with an invocation handler.
     * <p>
     * 每个 proxy 实例有以下特点：
     * - Given a proxy instance proxy and one of the interfaces implemented by its proxy class Foo, the following
     * expression will return true:
     * proxy instanceof Foo
     * <p>
     * - and the following cast operation will succeed (rather than throwing a ClassCastException):
     * (Foo) proxy
     * <p>
     * - Each proxy instance has an associated invocation handler, the one that was passed to its constructor. The
     * static Proxy.getInvocationHandler method will return the invocation handler associated with the proxy instance
     * passed as its argument.
     * - An interface method invocation on a proxy instance will be encoded and dispatched to the invocation handler's
     * invoke method as described in the documentation for that method.
     * - An invocation of the hashCode, equals, or toString methods declared in java.lang.Object on a proxy instance
     * will be encoded and dispatched to the invocation handler's invoke method in the same manner as interface method
     * invocations are encoded and dispatched, as described above. The declaring class of the Method object passed to
     * invoke will be java.lang.Object. Other public methods of a proxy instance inherited from java.lang.Object are not
     * overridden by a proxy class, so invocations of those methods behave like they do for instances of
     * java.lang.Object.
     *
     * @param interfaceClass 待代理的接口类，注意，这必须是 T 的 Class
     * @param instance 待代理的对象，注意这个对象必须也是 T 类型
     * @param proxyConfig 代理配置
     * @param <T> 目标接口实例类型
     * @return 目标接口实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProxyInstance(Class<T> interfaceClass, T instance,
            ProxyConfig proxyConfig) {
        // Proxy 是 静态工厂类大集合，也是所有 proxy 的超类，InvocationHandler 是里面扮演类似 advice 的角色的东西
        final T t = (T) Proxy.newProxyInstance(JdkNativeProxyInvocationHandler.class.getClassLoader(),
                // Proxy 的工厂方法是可以接纳多个接口的，但 JdkNativeProxyInvocationHandler 只有一个，所以对调用的拦截就要有接纳多个接口的多个方法的能力
                new Class[]{interfaceClass},
                new JdkNativeProxyInvocationHandler(interfaceClass.getSimpleName(), instance, proxyConfig));
        final InvocationHandler invocationHandler = Proxy.getInvocationHandler(t);
        log.info("getProxyInstance, invocationHandler: {}", invocationHandler);
        return t;
    }
}
