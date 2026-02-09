# domain-driven-transaction-sys

**一个领域驱动设计的交易系统的示例项目**

## 项目简介

本项目是一个基于领域驱动设计(DDD)原则构建的交易系统示例,展示了如何在 Java 8 + Spring Boot 2.7.18 环境下实现企业级的交易系统架构。项目采用 Gradle 多模块构建,遵循 SOFA 分层架构设计。

## 技术栈

- **Java**: 8 (使用 Gradle Toolchain 管理)
- **Spring Boot**: 2.7.18
- **Spring Cloud**: 2021.0.8
- **构建工具**: Gradle 8.6
- **持久层**: 
  - Spring Data JPA
  - MyBatis 2.3.2
  - H2 / MySQL / MariaDB
- **测试框架**: JUnit 5.9.3
- **日志**: Log4j2
- **可观测性**: OpenTelemetry 2.10.0

## 项目结构

```
domain-driven-transaction-sys
├── biz-service-impl/        # 业务服务实现层
├── biz-shared/              # 业务共享模块
├── common-dal/              # 数据访问层
├── common-service-facade/   # 服务门面层
├── common-service-integration/ # 服务集成层
├── common-util/             # 通用工具类
├── core-model/              # 核心领域模型
└── core-service/            # 核心服务层
```

### 模块说明

- **biz-service-impl**: 业务服务实现,包含 WebFlux 和 Web MVC 支持,是应用的可启动模块
- **biz-shared**: 业务共享模块,提供业务级别的共享组件
- **common-dal**: 数据访问层,集成 JPA 和 MyBatis,支持多数据源
- **common-service-facade**: 服务门面,定义对外暴露的服务接口
- **common-service-integration**: 服务集成层,处理第三方系统集成
- **common-util**: 通用工具类库
- **core-model**: 核心领域模型,包含领域实体、值对象、聚合根等
- **core-service**: 核心领域服务,实现业务逻辑

## 快速开始

### 环境要求

- JDK 8 (推荐使用 SDKMAN 安装: `sdk install java 8.0.432`)
- Gradle 8.6 (项目自带 Gradle Wrapper)

### 构建项目

```bash
# 清理并构建(跳过测试)
./gradlew clean build -x test --stacktrace

# 完整构建(包含测试)
./gradlew clean build
```

### 运行测试

```bash
# 运行所有测试
./gradlew test

# 运行所有测试并生成聚合报告
./gradlew testAggregateTestReport

# 运行指定测试类
./gradlew test --tests com.magicliang.transaction.sys.aop.factory.ProxyFactoryTest

# 运行指定测试方法
./gradlew test --tests 'com.magicliang.transaction.sys.DomainDrivenTransactionSysApplicationIntegrationTest.testGetWildCardType'

# 使用通配符运行测试
./gradlew test --tests com.magicliang.transaction.sys.DomainDrivenTransactionSysApplicationIntegrationTest.*test*
```

## 架构设计

### SOFA 分层架构

项目遵循 SOFA (Service Oriented Fabric Architecture) 分层原则,实现清晰的职责分离:

![SOFA分层](sofa分层.png)

**分层说明**:
- **展示层**: 处理 HTTP 请求,参数校验,响应封装
- **应用层**: 编排业务流程,事务管理
- **领域层**: 核心业务逻辑,领域模型
- **基础层**: 数据访问,第三方集成,技术支撑

### Gradle 多模块架构

采用 Gradle 多模块构建,每个模块职责清晰,依赖关系明确:

```
biz-service-impl
  ├── biz-shared
  ├── common-service-facade
  └── (Spring Boot dependencies)

core-model
  ├── common-util
  ├── common-service-integration
  └── common-dal
```

## 开发指南

### 代码规范

- 尽量对列表使用浅拷贝后再操作,避免影响原列表及其 subList
- 使用 Rich Object 设计模式,封装业务行为
- 遵循领域驱动设计原则,将业务逻辑集中在领域层

### 测试策略

项目支持单元测试和集成测试:

```groovy
sourceSets {
    test.java.srcDirs = ['src/test/integration/java', 'src/test/unit/java']
}
```

测试任务支持并行执行,提升测试效率:

```groovy
test {
    maxParallelForks = Runtime.runtime.availableProcessors()
}
```

## 待办事项

### 架构优化

- [ ] 评估是否将服务改造为 K8s 微服务架构
- [ ] 引入更多最佳实践到开源项目

### 功能实现

- [ ] 梳理 Gradle Task,完善集成测试和单元测试用例
- [ ] 实现 Spring WebFlux Reactor Controller
- [ ] 引入 mariadb4j 用于集成测试
- [ ] 脚本 Docker 化,准备 MySQL + K8s 集群
- [ ] Mock 支付环节
- [ ] 补充模型设计和系统分层文档
- [ ] 实现线程池开源化
- [ ] 实现基于 DB 的最大努力型事务
- [ ] 实现聚合根(Aggregate Root)的工厂方法化
- [ ] 实现值对象(Value Object)的构建
- [ ] 补全 JPA 映射方法
- [ ] 补全 MyBatis 映射方法
- [ ] 实现动态生成或 Immutable List 的 DRM/MCC/apollo/rainbow 方案

## GitHub SSH 免登录配置

如果遇到 `git push` 时提示 "Authentication failed" 或要求输入用户名密码,说明仓库使用的是 HTTPS URL 而非 SSH URL。

### 配置步骤

1. **检查 SSH 密钥**
   ```bash
   ls -la ~/.ssh/
   # 应该能看到 id_rsa 和 id_rsa.pub
   ```

2. **复制公钥内容**
   ```bash
   cat ~/.ssh/id_rsa.pub
   ```

3. **添加公钥到 GitHub**
   - 访问: https://github.com/settings/keys
   - 点击 "New SSH key"
   - 粘贴公钥内容并保存

4. **测试 SSH 连接**
   ```bash
   ssh -T git@github.com
   # 成功会显示: Hi username! You've successfully authenticated
   ```

5. **更新 remote URL 为 SSH 格式**
   ```bash
   # 查看当前 remote URL
   git remote -v
   
   # 如果是 HTTPS URL,改为 SSH URL
   git remote set-url origin git@github.com:magicliang/domain-driven-transaction-sys.git
   
   # 验证修改
   git remote -v
   ```

### URL 格式对比

- ❌ **HTTPS**: `https://github.com/magicliang/domain-driven-transaction-sys.git` (需要输入密码)
- ✅ **SSH**: `git@github.com:magicliang/domain-driven-transaction-sys.git` (免密登录)

## 最近更新

- `f6cce91` - docs: 添加 GitHub SSH 免登录配置说明
- `475545e` - feat: 添加 JsonDiffToolJackson 工具类及完整测试
- `62094b7` - chore: 删除 ArrayDeque 等多个数据结构类文件
- `755b709` - feat: 新增空间优化的动态规划方法及多场景测试
- `f8b3a6d` - docs: 为二维和一维DP数组添加初始条件及逻辑注释

## 常见问题

### 如何修复 Gradle 运行问题?

![如何修复gradle运行问题](如何修复gradle运行问题.png)

## 许可证

本项目仅供学习和参考使用。

## 贡献

欢迎提交 Issue 和 Pull Request!