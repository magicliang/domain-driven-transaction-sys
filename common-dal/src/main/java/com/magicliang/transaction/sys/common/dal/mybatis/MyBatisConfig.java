package com.magicliang.transaction.sys.common.dal.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

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
@MapperScan("com.magicliang.transaction.sys.common.dal.mybatis.mapper")
public class MyBatisConfig {

//    @Bean
//    @ConfigurationProperties(prefix="datasource.primary")
//    public DataSource personDataSource() {
//        return DataSourceBuilder.create().build();
//    }

}
