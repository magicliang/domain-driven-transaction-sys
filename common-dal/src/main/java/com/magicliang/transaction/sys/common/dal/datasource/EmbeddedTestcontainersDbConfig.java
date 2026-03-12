package com.magicliang.transaction.sys.common.dal.datasource;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.FileCopyUtils;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.MountableFile;

/**
 * 使用 Testcontainers 运行真实 MariaDB Docker 容器的数据源配置。
 * 适配所有芯片架构（ARM64/AMD64/x86_64）和操作系统，前置条件：需要安装并运行 Docker。
 * <p>
 * 单容器双数据库：一个 MariaDB 容器内创建 test_master 和 test_slave1 两个 schema，
 * 通过不同 JDBC URL 连接，与 mariadb4j 方案语义完全等价。
 *
 * @author magicliang
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@Profile("local-tc-dev")
public class EmbeddedTestcontainersDbConfig {

    /**
     * 单例容器，整个 JVM 生命周期内只启动一次。
     * Testcontainers Ryuk 会在 JVM 退出时自动清理容器。
     */
    private static volatile MariaDBContainer<?> container;

    @Autowired
    private ApplicationContext applicationContext;

    private static synchronized MariaDBContainer<?> getContainer() {
        if (container == null) {
            container = new MariaDBContainer<>("mariadb:10.11")
                    .withDatabaseName("tc_default")
                    .withUsername("test")
                    .withPassword("test")
                    .withCopyFileToContainer(
                            MountableFile.forClasspathResource("sql/tc-init-privileges.sql"),
                            "/docker-entrypoint-initdb.d/zzz-grant-privileges.sql");
            container.start();
            log.info("Testcontainers MariaDB started - host: {}, port: {}",
                    container.getHost(), container.getMappedPort(3306));
        }
        return container;
    }

    @Bean(name = "dataSource", destroyMethod = "close")
    @Primary
    public DataSource datasource() {
        Environment environment = applicationContext.getEnvironment();
        final String databaseName = environment.getProperty("spring.datasource.master.schemaName");
        final String driverClassName = environment.getProperty("spring.datasource.master.driver-class-name");

        initDatabase(databaseName, environment);

        String url = buildJdbcUrl(databaseName);
        log.info("Master datasource URL: {}", url);
        return DataSourceBuilder
                .create()
                .username(getContainer().getUsername())
                .password(getContainer().getPassword())
                .url(url)
                .driverClassName(driverClassName)
                .build();
    }

    @Bean(name = "slaveDataSource1", destroyMethod = "close")
    public DataSource slaveDataSource1() {
        Environment environment = applicationContext.getEnvironment();
        final String databaseName = environment.getProperty("spring.datasource.slave1.schemaName");
        final String driverClassName = environment.getProperty("spring.datasource.slave1.driver-class-name");

        initDatabase(databaseName, environment);

        String url = buildJdbcUrl(databaseName);
        log.info("Slave datasource URL: {}", url);
        return DataSourceBuilder
                .create()
                .username(getContainer().getUsername())
                .password(getContainer().getPassword())
                .url(url)
                .driverClassName(driverClassName)
                .build();
    }

    /**
     * 在容器内创建数据库并执行 DDL 初始化脚本。
     * test 用户已通过 tc-init-privileges.sql 获得全局权限。
     */
    private void initDatabase(String databaseName, Environment environment) {
        MariaDBContainer<?> mc = getContainer();
        String rootUrl = "jdbc:mariadb://" + mc.getHost() + ":" + mc.getMappedPort(3306) + "/";

        // 创建数据库（test 用户已有全局权限）
        try (Connection conn = DriverManager.getConnection(rootUrl, mc.getUsername(), mc.getPassword());
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE DATABASE IF NOT EXISTS `" + databaseName + "`");
        } catch (Exception e) {
            throw new RuntimeException("Failed to create database: " + databaseName, e);
        }

        // 连接到目标数据库，执行 schema DDL 和 data SQL
        String dbUrl = rootUrl + databaseName;
        try (Connection conn = DriverManager.getConnection(dbUrl, mc.getUsername(), mc.getPassword())) {
            final String schemaLocations = environment.getProperty("spring.sql.init.schema-locations");
            if (schemaLocations != null) {
                ScriptUtils.executeSqlScript(conn, new ClassPathResource(schemaLocations));
            }
            final String dataLocations = environment.getProperty("spring.sql.init.data-locations");
            if (dataLocations != null) {
                ClassPathResource dataResource = new ClassPathResource(dataLocations);
                if (dataResource.exists() && hasContent(dataResource)) {
                    ScriptUtils.executeSqlScript(conn, dataResource);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database schema: " + databaseName, e);
        }
    }

    private boolean hasContent(ClassPathResource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            String content = FileCopyUtils.copyToString(reader);
            // 去除注释和空白后判断是否有实际 SQL 内容
            String stripped = content.replaceAll("--[^\\n]*", "").replaceAll("/\\*.*?\\*/", "").trim();
            return !stripped.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private String buildJdbcUrl(String databaseName) {
        MariaDBContainer<?> mc = getContainer();
        return "jdbc:mariadb://" + mc.getHost() + ":" + mc.getMappedPort(3306) + "/" + databaseName;
    }
}
