# COMMON-DAL 数据访问层

## OVERVIEW

基础设施层数据访问：MyBatis Mapper、PO 实体、主从数据源配置。大部分代码由 MyBatis Generator 生成。

## STRUCTURE

```
common-dal/src/main/java/.../dal/
├── mybatis/
│   ├── mapper/             # MyBatis Mapper 接口
│   │   ├── TransPayOrderPoMapper.java
│   │   └── *SqlProvider.java  # 动态 SQL
│   └── po/                 # PO 实体（生成）
│       ├── TransPayOrderPo.java
│       └── TransPayOrderPoExample.java  # 查询条件构建
├── datasource/             # 数据源配置
│   ├── DataSourceConfig.java          # 主从数据源
│   ├── EmbeddedTestcontainersDbConfig.java  # Testcontainers
│   └── EmbeddedMariaDbConfig.java     # 嵌入式 MariaDB
└── resources/
    └── autogen/generatorConfig.xml    # MyBatis Generator 配置
```

## WHERE TO LOOK

| 任务 | 位置 | 说明 |
|------|------|------|
| 支付订单 CRUD | `mybatis/mapper/TransPayOrderPoMapper.java` | Mapper 接口 |
| 动态 SQL | `mybatis/mapper/*SqlProvider.java` | SqlProvider 类 |
| 数据源配置 | `datasource/DataSourceConfig.java` | 主从库配置 |
| 测试数据源 | `datasource/EmbeddedTestcontainersDbConfig.java` | Testcontainers |
| 生成器配置 | `resources/autogen/generatorConfig.xml` | MyBatis Generator |

## CONVENTIONS

- **Mapper 注解**：使用 `@Select`、`@Insert` 等注解，无 XML mapper 文件
- **PO 命名**：`{Table}Po.java`，对应数据库表 `tb_{table}`
- **Example 查询**：使用 `*Example.java` 构建复杂查询条件
- **主从分离**：`master` 数据源写，`slave1` 读（但需应用层手动选择）

## ANTI-PATTERNS

- ⚠️ build.gradle TODO：需要升级依赖
- ⚠️ build.gradle TODO：暂时引入第三方依赖

## GENERATED FILES

大文件由 MyBatis Generator 生成，不建议手动修改：
- `TransPayOrderPoExample.java` (2095 行)
- `TransChannelRequestPoExample.java` (1175 行)
- `TransPayOrderPo.java` (1045 行)

## PROFILES

| Profile | 数据源 | 用途 |
|---------|--------|------|
| `local-tc-dev` | Testcontainers MariaDB | 本地开发（需 Docker） |
| `local-mariadb4j-dev` | 嵌入式 MariaDB | 本地开发（仅 x86_64） |
| `local-mysql-dev` | 本地 MySQL | 本地开发 |
| `staging`/`prod` | K8s MariaDB | 部署环境 |