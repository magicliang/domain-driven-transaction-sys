package com.magicliang.transaction.sys.common.dal.datasource;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import com.magicliang.transaction.sys.common.exception.BaseTransException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_EMBEDDED_DB_PORT_ERROR;
import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.UNABLE_TO_BOOTSTRAP_EMBEDDED_DB_PORT_ERROR;

/**
 * 只在 mariadb4j 这个profile下面使用的数据源
 * 参考：
 * 1. {@link https://github.com/vorburger/MariaDB4j/blob/master/mariaDB4j/src/test/java/ch/vorburger/mariadb4j/tests/MariaDB4jSampleTutorialTest.java}
 * 2. {@link https://objectpartners.com/2017/06/19/using-mariadb4j-for-a-spring-boot-embedded-database/}
 *
 * @author liangchuan
 */
@Slf4j
@Configuration
public class EmbeddedMariaDbConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(name = "dataSource", destroyMethod = "close")
    @Profile("local-mariadb4j-dev")
    @Primary
    public DataSource datasource() {
        Environment environment = applicationContext.getEnvironment();

        final String databaseName = environment.getProperty("spring.datasource.master.schemaName");
        final String userName = environment.getProperty("spring.datasource.master.userName");
        final String password = environment.getProperty("spring.datasource.master.password");
        final String driverClassName = environment.getProperty("spring.datasource.master.driver-class-name");
        MariaDB4jSpringService mariaDB4jSpringService = applicationContext.getBean("masterMariaDB4jSpringService", MariaDB4jSpringService.class);
        try {
            // Create our database with default root user and no password
            DB db = mariaDB4jSpringService.getDB();
            db.createDB(databaseName, userName, password);
            final String schemaLocations = environment.getProperty("spring.sql.init.schema-locations");
            db.source(schemaLocations, userName, password, databaseName);
        } catch (ManagedProcessException e) {
            log.error("unable to init test database");
            throw new BaseTransException(e, UNABLE_TO_BOOTSTRAP_EMBEDDED_DB_PORT_ERROR);
        }
        DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();
        String url = config.getURL(databaseName);
        return DataSourceBuilder
                .create()
                .username(userName)
                .password(password)
                .url(url)
                .driverClassName(driverClassName)
                .build();

//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setPassword(userName);
//        hikariConfig.setPassword(password);
//        hikariConfig.setDriverClassName(driverClassName);
//        hikariConfig.setJdbcUrl(url);
//        // HikariDataSource 继承自 HikariConfig，这样构建数据源也没问题
//        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

//        return hikariDataSource;
    }

    @Bean(name = "slaveDataSource1", destroyMethod = "close")
    @Profile("local-mariadb4j-dev")
    @Primary
    public DataSource slaveDataSource1() {
        Environment environment = applicationContext.getEnvironment();

        final String databaseName = environment.getProperty("spring.datasource.slave1.schemaName");
        final String userName = environment.getProperty("spring.datasource.slave1.userName");
        final String password = environment.getProperty("spring.datasource.slave1.password");
        final String driverClassName = environment.getProperty("spring.datasource.slave1.driver-class-name");
        MariaDB4jSpringService mariaDB4jSpringService = applicationContext.getBean("getSlaveMariaDB4jSpringService1", MariaDB4jSpringService.class);
        try {
            // Create our database with default root user and no password
            DB db = mariaDB4jSpringService.getDB();
            db.createDB(databaseName, userName, password);
            final String schemaLocations = environment.getProperty("spring.sql.init.schema-locations");
            /*
             * datasource:
             *   hikari:
             *     # 如果引入了 mariadb4j，就要在这里指定 schema 初始化策略，schema 不再自动生成
             *     initialization-mode: always
             */
            db.source(schemaLocations, userName, password, databaseName);
        } catch (ManagedProcessException e) {
            log.error("unable to init test database");
            throw new BaseTransException(e, UNABLE_TO_BOOTSTRAP_EMBEDDED_DB_PORT_ERROR);
        }
        DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();
        String url = config.getURL(databaseName);
        return DataSourceBuilder
                .create()
                .username(userName)
                .password(password)
                .url(url)
                .driverClassName(driverClassName)
                .build();
    }

    /**
     * MariaDB4jSpringService 实现了 LifeCycle 钩子，不用配 destroyMethod 也可以让 db 正常退出
     *
     * @return masterMariaDB4jSpringService
     */
    @Bean(name = "masterMariaDB4jSpringService")
    @Profile("local-mariadb4j-dev")
    @Primary
    public MariaDB4jSpringService getMasterMariaDB4jSpringService() {
        Environment environment = applicationContext.getEnvironment();
        final String port = environment.getProperty("spring.datasource.master.port");
        // 生成一个局部的、临时的 mariaDB4jSpringService，只为托管一个嵌入式 DB
        if (StringUtils.isBlank(port)) {
            throw new BaseTransException(INVALID_EMBEDDED_DB_PORT_ERROR, INVALID_EMBEDDED_DB_PORT_ERROR.getErrorMsg() + port);
        }
        MariaDB4jSpringService mariaDB4jSpringService = new MariaDB4jSpringService();
        mariaDB4jSpringService.setDefaultPort(Integer.parseInt(port));
        // 这个bean start 以后会启动安装和调用 mariadb4j 的功能，有时候需要启动 openssl：brew install rbenv/tap/openssl@1.0 && ln -sfn /usr/local/Cellar/openssl@1.0/1.0.2t /usr/local/opt/openssl
        return mariaDB4jSpringService;
    }

    /**
     * slave1MariaDB4jSpringService
     *
     * @return slave1MariaDB4jSpringService
     */
    @Bean(name = "getSlaveMariaDB4jSpringService1")
    @Profile("local-mariadb4j-dev")
    @Primary
    public MariaDB4jSpringService getSlaveMariaDB4jSpringService1() {
        Environment environment = applicationContext.getEnvironment();
        final String port = environment.getProperty("spring.datasource.slave1.port");
        // 生成一个局部的、临时的 mariaDB4jSpringService，只为托管一个嵌入式 DB
        if (StringUtils.isBlank(port)) {
            throw new BaseTransException(INVALID_EMBEDDED_DB_PORT_ERROR, INVALID_EMBEDDED_DB_PORT_ERROR.getErrorMsg() + port);
        }
        MariaDB4jSpringService mariaDB4jSpringService = new MariaDB4jSpringService();
        mariaDB4jSpringService.setDefaultPort(Integer.parseInt(port));
        // 这个bean start 以后会启动安装和调用 mariadb4j 的功能，有时候需要启动 openssl：brew install rbenv/tap/openssl@1.0 && ln -sfn /usr/local/Cellar/openssl@1.0/1.0.2t /usr/local/opt/openssl
        return mariaDB4jSpringService;
    }

}
