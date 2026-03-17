# Spec 001: 用 Testcontainers MariaDB 替代 mariadb4j

- **状态**: 已完成
- **创建日期**: 2026-03-12
- **完成日期**: 2026-03-12

## 背景

项目依赖 mariadb4j 提供嵌入式 MariaDB 用于本地开发和集成测试。mariadb4j 通过在 JVM 进程内解压并运行 MariaDB 原生二进制来工作，但该库不支持 ARM 架构芯片（如 Apple Silicon M1/M2/M3），导致在 ARM 设备上无法使用 `local-mariadb4j-dev` profile 进行本地开发。

## 目标

1. 提供一个新的数据库 profile，支持所有芯片架构（ARM64/AMD64/x86_64）和操作系统
2. 保持与现有双数据源架构（master/slave）的完全兼容
3. 不影响现有 mariadb4j profile 的可用性（保留给 x86 用户）
4. 成为默认激活的 profile

## 方案设计

### 技术选型: Testcontainers + MariaDB Docker 容器

选择 Testcontainers 而非其他方案的理由:
- Docker 镜像天然支持多架构（linux/amd64, linux/arm64）
- Testcontainers 是 Java 生态成熟的集成测试库，与 Spring Boot 深度集成
- 容器生命周期完全由 Testcontainers 管理，开发者无感知
- 支持 Docker Desktop 和 Podman 两种容器运行时

### 架构设计

```
单 MariaDB 10.11 容器
├── tc_default (Testcontainers 默认库，不使用)
├── test_master (主库 schema)
└── test_slave1 (从库 schema)
```

- 一个容器内创建两个数据库，通过不同 JDBC URL 连接
- 与 mariadb4j 的双实例方案语义等价，但资源占用更低
- DDL 通过 `EmbeddedTestcontainersDbConfig.initDatabase()` 手动执行
- Spring Boot 自动初始化通过 `spring.sql.init.mode=never` 禁用，避免冲突

### 权限管理

MariaDB 10.4+ 的 root 用户默认仅允许 localhost 连接，无法从容器外部使用 root 远程连接。解决方案:
- 通过 `docker-entrypoint-initdb.d/` 挂载初始化脚本 (`tc-init-privileges.sql`)
- 在容器启动时由 MariaDB 入口脚本以 root 身份执行
- 授予 test 用户全局 `GRANT ALL PRIVILEGES ON *.*`
- 后续所有操作（CREATE DATABASE、DDL 执行）均使用 test 用户

## 涉及文件

### 新增
- `common-dal/src/main/java/.../datasource/EmbeddedTestcontainersDbConfig.java` - Testcontainers 数据源配置
- `common-dal/src/main/resources/sql/tc-init-privileges.sql` - 容器初始化权限脚本

### 修改
- `biz-service-impl/src/main/resources/application.yml` - 新增 local-tc-dev profile，设置为默认
- `common-dal/build.gradle` - 添加 Testcontainers 依赖
- `biz-service-impl/src/test/.../DomainDrivenTransactionSysApplicationIntegrationTest.java` - 排除 DataSourceAutoConfiguration
- `README.md` - 更新 Profile 使用说明

### 删除
- `biz-service-impl/src/main/resources/sql/h2/schema.ddl` - H2 DDL（架构不兼容）
- `biz-service-impl/src/main/resources/sql/h2/data.sql` - H2 数据
- `biz-service-impl/src/test/resources/application.yml` - 遮蔽主配置的测试文件

## 解决的问题

| 问题 | 根因 | 解决方案 |
|------|------|----------|
| mariadb4j 不支持 ARM | 原生二进制无 ARM 版本 | 使用 Docker 多架构镜像 |
| H2 profile 不可用 | 单数据源 vs 双数据源架构不匹配 | 删除 H2 profile |
| DataSourceBeanCreationException | 测试 application.yml 遮蔽主配置 | 删除测试配置文件 + 排除自动配置 |
| Access denied for test user | test 用户无 CREATE DATABASE 权限 | docker-entrypoint-initdb.d 授权 |
| Access denied for root | MariaDB 10.4+ root 禁止远程连接 | 放弃 root 远程连接，授权 test 用户 |
| schema.ddl not found | Spring Boot 自动初始化与手动初始化冲突 | 设置 `mode: never` |

## 验证

- 9 个测试用例全部通过 (BUILD SUCCESSFUL)
- 容器运行时: Podman (macOS ARM64)
- MariaDB 版本: 10.11
- 双数据源正常初始化（test_master + test_slave1）
