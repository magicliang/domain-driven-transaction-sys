package com.magicliang.transaction.sys.common;

import org.junit.jupiter.api.BeforeAll;

import java.net.MalformedURLException;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 标准单元测试基类
 *
 * @author magicliang
 * <p>
 * date: 2022-05-06 18:25
 */
public abstract class UnitTest {

    @BeforeAll
    public static void setLogger() throws MalformedURLException {
        System.setProperty("log4j.configurationFile", "log4j2/log4j2-offline.xml");
    }

}
