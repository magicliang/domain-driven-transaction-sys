package com.magicliang.transaction.sys.common.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.magicliang.transaction.sys.common.UnitTest;
import org.junit.jupiter.api.Test;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: Jdk原生代理的处理器
 *
 * @author magicliang
 *         <p>
 *         date: 2022-04-22 19:45
 */
public class JdkNativeProxyInvocationHandlerTest extends UnitTest {

    /**
     * 如果是遗留项目要引用 vintage 的测试注解，否则引用 jupiter 的测试注解更好
     */
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

        TestInterface proxyInstance = CustomDynamicProxyFactory.getProxyInstance(TestInterface.class, testInterface,
                new ProxyConfig());
        // 这个测试要跑得好，要准备好对日志的重定向，在 test 中重定向到 console
        proxyInstance.foo();
        assertTrue(true);
    }

    interface TestInterface {

        void foo();
    }
}
