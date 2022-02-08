package com.magicliang.transaction.sys.common.dal.datasource;

import com.magicliang.transaction.sys.common.exception.BaseTransException;

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

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import lombok.extern.slf4j.Slf4j;

import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.INVALID_EMBEDDED_DB_PORT_ERROR;
import static com.magicliang.transaction.sys.common.enums.TransErrorEnum.UNABLE_TO_BOOTSTRAP_EMBEDDED_DB_PORT_ERROR;

/**
 * 只在 mariadb4j 这个profile下面使用的数据源
 * 参考：{@link https://github.com/vorburger/MariaDB4j/blob/master/mariaDB4j/src/test/java/ch/vorburger/mariadb4j/tests/MariaDB4jSampleTutorialTest.java}
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


    @Bean(name = "masterMariaDB4jSpringService", destroyMethod = "stop")
    @Profile("local-mariadb4j-dev")
    @Primary
    public MariaDB4jSpringService getMariaDB4jSpringService() {
        Environment environment = applicationContext.getEnvironment();
        final String port = environment.getProperty("spring.datasource.master.port");
        // 生成一个局部的、临时的 mariaDB4jSpringService，只为托管一个嵌入式 DB
        if (StringUtils.isBlank(port)) {
            throw new BaseTransException(INVALID_EMBEDDED_DB_PORT_ERROR, INVALID_EMBEDDED_DB_PORT_ERROR.getErrorMsg() + port);
        }
        MariaDB4jSpringService mariaDB4jSpringService = new MariaDB4jSpringService();
        mariaDB4jSpringService.setDefaultPort(Integer.parseInt(port));
        return mariaDB4jSpringService;
    }

//    @Bean(name = "slaveDataSource1", destroyMethod = "close")
//    public DataSource slaveDataSource1() {
//        final String databaseName = applicationContext.getEnvironment().getProperty("spring.datasource.slave1.schemaName");
//        mariaDB4j(databaseName);
//        return DataSourceBuilder.create().build();
//    }

}
