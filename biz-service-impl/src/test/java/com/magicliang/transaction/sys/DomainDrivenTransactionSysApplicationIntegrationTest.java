package com.magicliang.transaction.sys;

import ch.vorburger.mariadb4j.springboot.autoconfigure.DataSourceAutoConfiguration;
import com.magicliang.transaction.sys.biz.shared.event.ApplicationEvents;
import com.magicliang.transaction.sys.core.model.entity.TransPayOrderEntity;
import com.magicliang.transaction.sys.core.model.event.TransPayOrderAcceptedEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 基于 Spring 的集成测试层，每次跑测试都要启动 Spring 容器，只有集成测试才需要使用这个东西
 *
 * @author magicliang
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootTest(classes = {DomainDrivenTransactionSysApplicationIntegrationTest.class})
public class DomainDrivenTransactionSysApplicationIntegrationTest {

    @Autowired
    private ApplicationEvents applicationEvents;

    @Test
    void contextLoads() {
        Assertions.assertTrue(true);
    }

    @Test
    void testEvents() {
        final TransPayOrderEntity payOrder = new TransPayOrderEntity();
        payOrder.setPayOrderNo(ThreadLocalRandom.current().nextLong());
        applicationEvents.transPayOrderAccepted(TransPayOrderAcceptedEvent.create(this, payOrder));
        Assertions.assertTrue(true);
    }

}
