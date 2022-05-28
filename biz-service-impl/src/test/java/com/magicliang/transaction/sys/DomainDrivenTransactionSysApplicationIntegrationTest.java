package com.magicliang.transaction.sys;

import ch.vorburger.mariadb4j.springboot.autoconfigure.DataSourceAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 基于 Spring 的集成测试层，每次跑测试都要启动 Spring 容器，只有集成测试才需要使用这个东西
 *
 * @author magicliang
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootTest(classes = {DomainDrivenTransactionSysApplicationIntegrationTest.class})
public class DomainDrivenTransactionSysApplicationIntegrationTest {

    @Test
    void contextLoads() {
        Assertions.assertTrue(true);
    }

}
