package com.magicliang.transaction.sys.aop.factory;

import com.magicliang.transaction.sys.DomainDrivenTransactionSysApplicationIntegrationTest;
import com.magicliang.transaction.sys.aop.advice.LoggingAdvice;
import com.magicliang.transaction.sys.aop.generator.IntegerGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.ProxyFactory;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2022-05-28 19:20
 */
@Slf4j
class ProxyFactorTest extends DomainDrivenTransactionSysApplicationIntegrationTest {

    @Resource
    private IntegerGenerator integerGenerator;


    /**
     * 测试平凡的代理工厂
     */
    @Test
    public void testJdkDynamicProxyFactory() {
        // 使用 HotSwappableTargetSource 必须添加接口
        ProxyFactory factory = new ProxyFactory(HotSwappableTargetSourceFactory.createHotSwappableTargetSource(integerGenerator));
//        final Class<?>[] allInterfaces = ArrayUtils.add(ClassUtils.getAllInterfaces(IntegerGenerator.class), IntegerGenerator.class);
        // 每一步 setInterfaces 会 clear 掉上一步 setInterfaces 的结果，注意，直接把目标接口 setInterfaces 会比 得到的接口少，后者会产生一些反射才能得出（不能直接从代码里得出的）的 type 等接口
        factory.setInterfaces(IntegerGenerator.class);
        // 使用平凡的 api 则不用这样做，但底层的 advised 的 interfaces 成员里仍然有这个接口
//        ProxyFactory factory = new ProxyFactory(integerGenerator);

        // 底层触发 Pointcut.TRUE 让这个 advice always match 这个 proxy 的方法
        factory.addAdvice(new LoggingAdvice());
//        factory.addAdvisor(myAdvisor);
        // 如果底层实现是 JdkDynamicAopProxy，这个 object 是个合成的 Proxy 内部类，有个 h 成员为 JdkDynamicAopProxy，factory 能够识别出几个接口，底层的 realProxy 就能做怎样的转化
        final Object realProxy = factory.getProxy();
        final IntegerGenerator proxy = (IntegerGenerator) realProxy;
        /*
         * JdkDynamicAopProxy 是个包外不可见的类，所以不能再做 casting，所以只能用调试来一探究竟：

         */
        log.info("proxy is: {}", proxy);
        // A isAssignableFrom B 意思是 A 是否 B 的超类，和 B instanceof A 的语义正好反过来，但不知道为什么 AopProxy  不是 JdkDynamicProxy 的超类
        log.info("AopProxy.class.isAssignableFrom: {}", AopProxy.class.isAssignableFrom(proxy.getClass()));
        log.info("proxy isProxyClass: {}", Proxy.isProxyClass(proxy.getClass()));
        log.info("proxy.generate(): {}", proxy.generate());

        Assertions.assertTrue(true);

        // 确认 bean parser 的加载过程
        // 区别两个 MethodInterceptor
        // 搞清楚最深层的 getProxy 的功能
    }

}