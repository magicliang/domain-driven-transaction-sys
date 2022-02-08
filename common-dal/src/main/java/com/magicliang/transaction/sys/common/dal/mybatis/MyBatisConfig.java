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

    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    @Primary
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

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
