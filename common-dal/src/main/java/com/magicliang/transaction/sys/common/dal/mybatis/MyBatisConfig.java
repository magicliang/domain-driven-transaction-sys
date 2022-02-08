package com.magicliang.transaction.sys.common.dal.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 * 参考：http://mybatis.org/spring/mappers.html#scan
 * 多数据源配置参考：https://blog.51cto.com/u_12462157/3798109
 *
 * @author magicliang
 * <p>
 * date: 2022-01-27 15:46
 */
@Configuration
// @MapperScan("com.magicliang.transaction.sys.common.dal.mybatis.mapper")
public class MyBatisConfig {

    /**
     * 主库数据源
     * 这是 org.springframework.boot.jdbc 数据源的配置方法，如果使用 druid 之类的配置，还是选用 xml配置更好
     * 这个 bean 不依赖于 dynamic-datasource-spring-boot-starter
     *
     * @return masterDataSource
     */
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    @Primary
    public DataSource masterDataSource() {
        // 打印 context 能够打印出 masterDataSource
        return DataSourceBuilder.create().build();
    }

    /**
     * 从库数据源
     *
     * @return slaveDataSource
     */
    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave1")
    public DataSource slaveDataSource() {
        // 打印 context 能够打印出 masterDataSource
        return DataSourceBuilder.create().build();
    }

    // 严格注意：如果使用了sharding中间件，需要保证 mybatis 的 SqlSessionFactory 和 DataSourceTransactionManager 使用同一个最抽象的数据源

    // 程序化地装配 SqlSessionFactory -> SqlSessionTemplate -> DataSourceTransactionManager 的做法

//    @Bean(name = "test1SqlSessionFactory")
//    @Primary
//    public SqlSessionFactory testSqlSessionFactory(@Qualifier("ds1") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        //指定xml映射文件位置
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/ds1/**/*Mapper.xml"));
//        return bean.getObject();
//    }
//
//    @Bean(name = "test1TransactionManager")
//    @Primary
//    public DataSourceTransactionManager testTransactionManager(@Qualifier("ds1") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean(name = "test1SqlSessionTemplate")
//    @Primary
//    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("test1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
}
