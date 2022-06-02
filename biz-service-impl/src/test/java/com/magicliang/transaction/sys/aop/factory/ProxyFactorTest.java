package com.magicliang.transaction.sys.aop.factory;

import com.magicliang.transaction.sys.DomainDrivenTransactionSysApplicationIntegrationTest;
import com.magicliang.transaction.sys.aop.advice.LoggingAdvice;
import com.magicliang.transaction.sys.aop.advice.ProfilingInterceptor;
import com.magicliang.transaction.sys.aop.generator.IntegerGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.ProxyCreatorSupport;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 这种测试用内部类可以封闭可见性，让所有的测试都在同一个包里
 *
 * @author magicliang
 * <p>
 * date: 2022-05-28 19:20
 */
@Slf4j
class ProxyFactorTest extends DomainDrivenTransactionSysApplicationIntegrationTest {

    @Resource
    private IntegerGenerator integerGenerator;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 测试平凡的代理工厂
     */
    @Test
    public void testJdkDynamicAopProxyFactory() {
        ProxyFactory factory = new ProxyFactory(integerGenerator);
//        final Class<?>[] allInterfaces = ArrayUtils.add(ClassUtils.getAllInterfaces(IntegerGenerator.class), IntegerGenerator.class);
        // 每一步 setInterfaces 会 clear 掉上一步 setInterfaces 的结果，注意，直接把目标接口 setInterfaces 会比 得到的接口少，后者会产生一些反射才能得出（不能直接从代码里得出的）的 type 等接口
//        factory.setInterfaces(IntegerGenerator.class);

        // 底层触发 Pointcut.TRUE 让这个 advice always match 这个 proxy 的方法
        factory.addAdvice(applicationContext.getBean(LoggingAdvice.class));
        factory.addAdvisor(new DefaultPointcutAdvisor(Pointcut.TRUE, new ProfilingInterceptor()));

        log.info("ProxyCreatorSupport.class.isInstance: {}", ProxyCreatorSupport.class.isInstance(factory));


//        factory.addAdvisor(myAdvisor);
        // 如果底层实现是 JdkDynamicAopProxy，这个 object 是个合成的（Synthesized） Proxy 内部类，有个 h 成员为 JdkDynamicAopProxy，factory 能够识别出几个接口，底层的 realProxy 就能做怎样的转化
        final Object realProxy = factory.getProxy();
        final IntegerGenerator proxy = (IntegerGenerator) realProxy;
        /*
         * JdkDynamicAopProxy 是个包外不可见的类，所以不能再做 casting，所以只能用调试来一探究竟。
         */
        log.info("proxy is: {}", proxy);
        log.info("proxy hashCode: {}", proxy.hashCode());
        log.info("proxy equals proxy: {}", proxy.equals(realProxy));
        // A isAssignableFrom B 意思是 A 是否 B 的超类，和 B instanceof A 的语义正好反过来，AopProxy 是 JdkDynamicProxy 的超类，不是 JdkDynamicProxy.getProxy() 的超类，
        log.info("AopProxy.class.isAssignableFrom: {}", AopProxy.class.isAssignableFrom(proxy.getClass()));
        // JdkDynamicAopProxy.getProxy() 的结果
        final Class<? extends IntegerGenerator> aClass = proxy.getClass();
        // class com.sun.proxy.$Proxy160，不是 java.lang.reflect.Proxy
        log.info("aClass.toString()：{}", aClass);
        // true
        log.info("proxy isProxyClass: {}", Proxy.isProxyClass(aClass));
        // true
        log.info("proxy instanceof Proxy: {}", proxy instanceof Proxy);
        log.info("proxy.generate(): {}", proxy.generate());

        Assertions.assertTrue(true);

        // 确认 bean parser 的加载过程
        // 区别两个 MethodInterceptor
        // 搞清楚最深层的 getProxy 的功能
    }

    /**
     * 测试平凡的代理工厂
     */
    @Test
    public void testCglibAopProxyFactory() {
        // 使用 HotSwappableTargetSource 必须添加接口
        ProxyFactory factory = new ProxyFactory();

        // 不能直接 set target，需要 setTargetSource，才能不出现：org.springframework.aop.AopInvocationException: AOP configuration seems to be invalid object is not an instance of declaring class 类似的错误
        factory.setTargetSource(HotSwappableTargetSourceFactory.createHotSwappableTargetSource(integerGenerator));
        // 如果设置了 HotSwappableTargetSource，则无法自动探测出接口，会回退到 cglib 代理，设置了 setInterfaces 又会激活 JdkDynamicAopProxy
//        factory.setInterfaces(IntegerGenerator.class);
        factory.setProxyTargetClass(true);

        // 底层触发 Pointcut.TRUE 让这个 advice always match 这个 proxy 的方法
        factory.addAdvice(new LoggingAdvice());
        final Object realProxy = factory.getProxy();
        // true
        log.info("ProxyCreatorSupport.class.isInstance: {}", ProxyCreatorSupport.class.isInstance(factory));

        // 不管有没有 factory.setInterfaces，这里转型成 IntegerGenerator 都会成功，因为 cglib 的子类天然是 integerGenerator 的实现类
        final IntegerGenerator proxy = (IntegerGenerator) realProxy;
        /*
         * JdkDynamicAopProxy 是个包外不可见的类，所以不能再做 casting，所以只能用调试来一探究竟：

         */
        log.info("proxy is: {}", proxy);
        final Class<? extends IntegerGenerator> aClass = proxy.getClass();
        // aClass.toString()：class com.magicliang.transaction.sys.aop.generator.impl.IntegerGeneratorImpl$$EnhancerBySpringCGLIB$$edeee08d
        log.info("aClass.toString()：{}", aClass);
        // AopProxy 是 CglibAopProxy 的超类，不是 CglibAopProxy.getProxy() 的超类
        // false
        log.info("AopProxy.class.isAssignableFrom: {}", AopProxy.class.isAssignableFrom(proxy.getClass()));
        // Proxy 不是 CglibAopProxy.getProxy() 的超类
        // false
        log.info("proxy isProxyClass: {}", Proxy.isProxyClass(proxy.getClass()));
        // false
        log.info("proxy instanceof Proxy: {}", proxy instanceof Proxy);

        log.info("proxy.generate(): {}", proxy.generate());

        Assertions.assertTrue(true);

        // 确认 bean parser 的加载过程
        // 区别两个 MethodInterceptor
        // 搞清楚最深层的 getProxy 的功能
    }

}