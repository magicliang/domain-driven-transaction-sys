package com.magicliang.transaction.sys.aop.factory;

import com.magicliang.transaction.sys.DomainDrivenTransactionSysApplicationIntegrationTest;
import com.magicliang.transaction.sys.aop.advice.LoggingAdvice;
import com.magicliang.transaction.sys.aop.generator.IntegerGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

import javax.annotation.Resource;

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
        factory.setInterfaces(IntegerGenerator.class);
        // 使用平凡的 api 则不用这样做，但底层的 advised 的 interfaces 成员里仍然有这个接口
//        ProxyFactory factory = new ProxyFactory(integerGenerator);

        factory.addAdvice(new LoggingAdvice());
//        factory.addAdvisor(myAdvisor);
        // 如果底层实现是 JdkDynamicAopProxy，这个 object 是个合成的 Proxy 内部类，有个 h 成员为 JdkDynamicAopProxy，factory 能够识别出几个接口，底层的 realProxy 就能做怎样的转化
        final Object realProxy = factory.getProxy();
        final IntegerGenerator proxy = (IntegerGenerator) realProxy;
        /*
         * JdkDynamicAopProxy 是个包外不可见的类，所以不能再做 casting，所以只能用调试来一探究竟：
         *
         *
         */
        log.info("proxy is: {}", proxy);
        Assertions.assertTrue(true);

        // 确认 bean parser 的加载过程
        // 区别两个 MethodInterceptor
        // 搞清楚最深层的 getProxy 的功能
    }

}