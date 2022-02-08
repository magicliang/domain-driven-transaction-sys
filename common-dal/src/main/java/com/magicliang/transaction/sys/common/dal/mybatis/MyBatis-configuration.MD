# Approach 1

1. init hikariCP

```yaml
datasource:
  # 默认就是hikari，可缺省
  # type: com.zaxxer.hikari.HikariDataSource
  #    driver-class-name: com.mysql.cj.jdbc.Driver
  hikari:
    pool-name: DomainDrivenTransacionSysHikariCP
    minimum-idle: 5
    maximum-pool-size: 30
    max-lifetime: 1800000
    connection-timeout: 1000
```

2. init master/slave database.

```sql
create schema test_master;
create schema test_slave;
```

3. init datasource bean，见 application.yml 里的配置，这些配置必须配合 Java Config 使用（基于 Spring xml
   的配置稍微麻烦，本次先不使用了）。
4. 需要在xml里配置齐这三样东西`SqlSessionFactory -> SqlSessionTemplate -> DataSourceTransactionManager`。其中
   DataSourceTransactionManager 是主管事务环绕拦截器的管理器，如果它使用的数据源和 mybatis的数据源不一致，则可能出现无事务的问题。

reference：

1. [SpringBoot整合MyBatis，HiKari、Druid连接池的使用，多数据源配置][1]

[1]: https://blog.51cto.com/u_12462157/3798109
