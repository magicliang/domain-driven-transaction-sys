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
    public void testNormalProxyFactory() {
        ProxyFactory factory = new ProxyFactory(integerGenerator);
        factory.addAdvice(new LoggingAdvice());
//        factory.addAdvisor(myAdvisor);
        final IntegerGenerator proxy = (IntegerGenerator) factory.getProxy();
        log.info("proxy is: {}", proxy);
        (JdkDynamicAopProxy) proxy;
        Assertions.assertTrue(true);
    }

}