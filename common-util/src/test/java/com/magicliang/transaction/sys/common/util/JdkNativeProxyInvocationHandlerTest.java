package com.magicliang.transaction.sys.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: Jdk原生代理的处理器
 *
 * @author magicliang
 * <p>
 * date: 2022-04-22 19:45
 */
public class JdkNativeProxyInvocationHandlerTest {

    @Test
    public void testHandler() {
        TestInterface testInterface = new TestInterface() {
            @Override
            public void foo() {
                try {
                    Thread.currentThread().sleep(3500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        TestInterface proxyInstance = DynamicProxyFactory.getProxyInstance(TestInterface.class, testInterface, new ProxyConfig());
        // 这个测试要跑得好，要准备好对日志的重定向，在 test 中重定向到 console
        proxyInstance.foo();
        assertTrue(true);
    }

    interface TestInterface {
        void foo();
    }
}
