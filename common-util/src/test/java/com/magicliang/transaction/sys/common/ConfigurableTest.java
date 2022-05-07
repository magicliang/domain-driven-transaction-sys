package com.magicliang.transaction.sys.common;

import org.junit.jupiter.api.BeforeAll;

import java.net.MalformedURLException;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 可配置的测试积累
 *
 * @author magicliang
 * <p>
 * date: 2022-05-07 16:48
 */
public abstract class ConfigurableTest {

    @BeforeAll
    public static void setLogger() throws MalformedURLException {
        System.setProperty("log4j.configurationFile", "log4j2/log4j2-offline.xml");
    }
}
