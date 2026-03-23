## Why

当前系统使用 Spring Boot Actuator 的 `/actuator/health` 作为健康检查端点，该端点被 K8s 的 startupProbe、readinessProbe 和 livenessProbe 广泛使用。然而，标准的 Actuator 健康检查仅提供基础的应用状态信息，无法满足以下业务场景需求：

1. **业务级健康检查**：需要验证核心业务组件（如数据库连接、支付渠道集成、分布式锁服务）的可用性
2. **细粒度状态报告**：运维和监控系统需要更详细的各组件健康状态，而非简单的 UP/DOWN
3. **自定义诊断信息**：在故障排查时需要提供系统关键指标（如连接池状态、缓存命中率、队列积压等）

## What Changes

- 新增自定义健康检查 API 端点 `/res/v1/health/custom`
- 实现多维度健康检查器（数据库、支付渠道、分布式锁等）
- 提供结构化的健康状态响应，包含各组件的详细状态和诊断信息
- 支持按组件查询健康状态（如 `/res/v1/health/custom?component=database`）
- 保持与现有 Actuator 健康检查的兼容性，不修改 K8s 探针配置

## Capabilities

### New Capabilities
- `custom-health-check`: 自定义健康检查 API，提供业务级健康状态报告、组件级诊断信息和细粒度状态查询能力

### Modified Capabilities
<!-- 无现有能力的需求变更 -->

## Impact

**Affected Code:**
- 新增 `HealthController` 控制器（`biz-service-impl/src/main/java/.../web/controller/HealthController.java`）
- 新增 `HealthIndicator` 接口及实现类（数据库、支付渠道等健康检查器）
- 新增健康状态响应模型类（`biz-shared` 模块）

**Dependencies:**
- 复用现有的 `spring-boot-starter-actuator` 依赖
- 利用现有的多数据源配置和支付渠道集成服务

**Systems:**
- K8s 探针继续使用 `/actuator/health`，不受影响
- 监控系统和运维工具可调用新的 `/res/v1/health/custom` 获取详细健康信息
- API 文档需更新以包含新的健康检查端点
