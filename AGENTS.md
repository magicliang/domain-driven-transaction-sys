# PROJECT KNOWLEDGE BASE

**Generated:** 2026-03-24
**Commit:** bdddf36
**Branch:** main

## OVERVIEW

领域驱动设计(DDD)交易系统示例项目，Java 8 + Spring Boot 2.7.18 + Gradle 8.6 多模块构建，遵循 SOFA 分层架构。

## STRUCTURE

```
domain-driven-transaction-sys/
├── biz-service-impl/     # 应用入口层：Controllers、RPC Services、Facade
├── biz-shared/           # 应用服务层：Handlers、Command/Query Bus
├── core-service/         # 领域层：Activities、Strategies、Managers
├── core-model/           # 领域模型：Entities、Value Objects、Factories
├── common-dal/           # 基础设施层：MyBatis Mappers、POs、DataSource
├── common-util/          # 通用工具：DateUtils、HttpUtils、并发锁
├── common-service-integration/  # 外部集成：Alipay Delegate、Leaf ID
├── common-service-facade/       # 服务门面：API 接口定义
├── deploy/               # K8s 部署：Dockerfile、K8s manifests、脚本
└── docs/                 # 文档
```

## WHERE TO LOOK

| 任务 | 位置 | 说明 |
|------|------|------|
| 添加 API 端点 | `biz-service-impl/src/main/java/.../web/controller/` | REST Controller |
| 编排业务流程 | `biz-shared/src/main/java/.../handler/` | Handler 模式 |
| 实现领域逻辑 | `core-service/src/main/java/.../domain/activity/` | Activity 编排 |
| 扩展策略 | `core-service/src/main/java/.../domain/strategy/` | Strategy 模式 |
| 定义实体 | `core-model/src/main/java/.../model/entity/` | 聚合根、实体 |
| 数据持久化 | `common-dal/src/main/java/.../mybatis/mapper/` | MyBatis Mapper |
| 外部服务调用 | `common-service-integration/src/main/java/.../delegate/` | Delegate 模式 |
| 工具类 | `common-util/src/main/java/.../util/` | 通用工具 |

## CONVENTIONS

**命名规范：**
- 模块：kebab-case (biz-service-impl, common-dal)
- 类：PascalCase，后缀表明职责 (Entity, Activity, Strategy, Manager, Mapper)
- 包：`com.magicliang.transaction.sys.{module}.{submodule}`

**分层依赖：**
```
biz-service-impl → biz-shared → core-service → core-model
                              ↘ common-dal (通过接口)
                              ↘ common-service-integration
```

**代码规范：**
- 列表操作前先浅拷贝，避免影响原列表及 subList
- 使用 Rich Object 模式，在实体中封装业务行为
- 领域逻辑集中在 core-service，不泄露到应用层

## ANTI-PATTERNS (THIS PROJECT)

**禁止：**
- ❌ 嵌套锁（PaymentFacadeImpl 中 FIXME 已标记，可能导致死锁）
- ❌ 使用废弃类 `ComponentHealthVO`、`HealthStatusVO`（已 @deprecated）

**警告（需完善）：**
- ⚠️ 大量 TODO/FIXME 未处理（约 20 处）
- ⚠️ 外部服务委托为 stub 实现（AlipayDelegateImpl、LeafServiceDelegateImpl）
- ⚠️ 嵌套锁问题待修复

## UNIQUE STYLES

**DDD 模式：**
- 聚合根：`TransPayOrderEntity` 包含子订单、支付请求
- 值对象：实现 `ValueObject` 接口，按值比较
- 活动(Activity)：编排业务流程（AcceptanceActivity、PaymentActivity）
- 策略(Strategy)：可替换的业务逻辑单元

**Bounded Contexts：**
- Acceptance（受理）、Payment（支付）、Notification（通知）、IdGeneration（ID 生成）

**技术特色：**
- 主从数据源配置（master/slave1）
- Testcontainers 集成测试
- Log4j2 替代 Logback
- OpenTelemetry 可观测性

## COMMANDS

```bash
# 构建（跳过测试）
./gradlew clean build -x test --stacktrace

# 运行测试
./gradlew test

# 启动应用（默认 local-tc-dev profile，需 Docker）
./gradlew bootRun

# 切换 Profile
./gradlew bootRun -Dspring.profiles.active=local-mariadb4j-dev
```

## NOTES

- 默认 profile `local-tc-dev` 需要 Docker/Podman 运行 Testcontainers
- ARM 芯片不支持 `local-mariadb4j-dev`（仅 x86_64）
- 生成的大文件（PoExample、Po）由 MyBatis Generator 生成，不建议手动修改
- K8s 部署见 `deploy/` 目录，支持 dev/staging/prod 三套环境