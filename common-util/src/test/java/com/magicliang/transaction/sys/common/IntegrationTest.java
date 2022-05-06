package com.magicliang.transaction.sys.common;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 基于 Spring 的集成测试层，每次跑测试都要启动 Spring 容器，只有集成测试才需要使用这个东西
 *
 * @author magicliang
 */
@SpringBootApplication
@SpringBootTest
public class IntegrationTest {

    @Test
    void contextLoads() {
    }

}
