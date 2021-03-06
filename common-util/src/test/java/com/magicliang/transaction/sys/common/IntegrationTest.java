package com.magicliang.transaction.sys.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 基于 Spring 的集成测试层，每次跑测试都要启动 Spring 容器，只有集成测试才需要使用这个东西
 * 这个类路径里面没有 ch/vorburger/mariadb4j/springboot/autoconfigure/DataSourceAutoConfiguration.class 这个类，所以不用排除数据源自动配置
 *
 * @author magicliang
 */
@SpringBootApplication
@SpringBootTest(classes = {IntegrationTest.class})
public class IntegrationTest extends ConfigurableTest {

    @Test
    void contextLoads() {
        Assertions.assertTrue(true);
    }

}
