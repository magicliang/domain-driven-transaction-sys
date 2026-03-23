## Context

当前系统基于 SOFA 分层架构和 DDD 设计原则，已具备 Spring Boot Actuator 的基础健康检查能力（`/actuator/health`），被 K8s 探针广泛使用。然而，标准 Actuator 仅提供应用级 UP/DOWN 状态，缺少业务维度的细粒度健康诊断能力。

**现状约束：**
- K8s 探针（startupProbe、readinessProbe、livenessProbe）依赖 `/actuator/health`，不可变更
- 系统采用多数据源架构（master/slave1），需分别检查各数据源健康状态
- 已集成支付渠道服务、分布式锁等核心业务组件
- 遵循 SOFA 分层：Controller → Facade → Service → Manager/Repository

**技术栈：**
- Spring Boot 2.x + Spring Boot Actuator
- MyBatis 多数据源配置
- HikariCP 连接池

## Goals / Non-Goals

**Goals:**
1. 提供业务级健康检查 API `/res/v1/health/custom`，返回结构化健康状态
2. 支持多维度健康检查：数据库（master/slave）、支付渠道、分布式锁、应用基础指标
3. 支持按组件查询：`/res/v1/health/custom?component=database`
4. 提供诊断信息：连接池状态、响应时间、错误详情
5. 遵循 SOFA 分层和现有代码规范（Controller → Service → Indicator）

**Non-Goals:**
1. 不替换或修改现有 `/actuator/health` 端点（K8s 探针依赖）
2. 不实现健康状态的持久化或历史趋势分析
3. 不实现自动故障恢复或自愈机制
4. 不集成外部监控系统（Prometheus/Grafana），仅提供 API 接口

## Decisions

### Decision 1: 健康检查架构模式

**选择：自定义 Controller + HealthIndicator 接口 + 多实现类**

**理由：**
- 复用 Spring Boot Actuator 的 `HealthIndicator` 设计模式，保持技术一致性
- 通过接口抽象实现可扩展性，新增组件健康检查只需添加新实现类
- Controller 负责 HTTP 层编排，Service 负责业务逻辑，Indicator 负责具体检查，职责清晰

**备选方案：**
- 方案 A：直接使用 Spring Boot Actuator 的 `CompositeHealthIndicator` 扩展
  - 缺点：与现有 `/actuator/health` 耦合，难以自定义响应格式和查询参数
- 方案 B：完全手写检查逻辑，不使用 HealthIndicator 模式
  - 缺点：代码重复，不符合 Spring 生态最佳实践

### Decision 2: 响应格式设计

**选择：统一使用 `ApiResult<HealthStatusVO>` 包装响应**

```json
{
  "success": true,
  "data": {
    "status": "UP",
    "timestamp": "2026-03-23T10:30:00Z",
    "components": {
      "database": {
        "status": "UP",
        "details": {
          "master": {"status": "UP", "responseTime": 5},
          "slave1": {"status": "UP", "responseTime": 8}
        }
      },
      "application": {
        "status": "UP",
        "details": {"uptime": 3600, "memoryUsage": 0.65}
      }
    }
  }
}
```

**理由：**
- 与现有 `DatabaseCountController` 的 `ApiResult` 模式保持一致
- 结构化响应便于监控系统解析和告警规则配置
- 包含 timestamp 便于诊断时序问题

**备选方案：**
- 直接返回 Actuator 原生格式（`{"status": "UP", "components": {...}}`）
  - 缺点：不符合项目现有的 `ApiResult` 封装规范

### Decision 3: 数据库健康检查实现

**选择：执行轻量级 SQL 查询（`SELECT 1`）验证连接可用性**

**理由：**
- 与现有 HikariCP 配置的 `connectionTestQuery: SELECT 1` 保持一致
- 性能开销极小（< 1ms），适合频繁健康检查
- 可捕获连接池耗尽、网络断开、数据库宕机等常见故障

**实现细节：**
- 通过 MyBatis Mapper 执行 `SELECT 1`，复用现有数据源配置
- 分别检查 master 和 slave1 数据源
- 记录响应时间用于诊断性能退化

### Decision 4: 组件健康检查的容错策略

**选择：单个组件故障不影响其他组件检查，整体状态取最严重级别**

**理由：**
- 避免级联故障：数据库故障不应阻止应用指标返回
- 便于定位问题：明确标识哪个组件异常
- 整体状态计算规则：任一组件 DOWN → 整体 DOWN；任一组件 WARN → 整体 WARN；否则 UP

**实现细节：**
- 每个 HealthIndicator 捕获自身异常，返回 `HealthStatus.DOWN` 而非抛出异常
- Controller 聚合所有组件状态，按优先级计算整体状态

## Risks / Trade-offs

### Risk 1: 健康检查接口被滥用导致性能问题

**风险：** 高频调用健康检查 API 可能增加数据库负载

**缓解措施：**
- 在文档中明确建议调用频率（如监控系统的 scrape interval ≥ 10s）
- 未来可考虑添加限流机制（如基于 Redis 的令牌桶）
- 数据库检查使用轻量级 SQL，单次调用开销 < 5ms

### Risk 2: 新增组件健康检查需要修改 Controller

**风险：** 每新增一个健康检查维度需修改 Controller 聚合逻辑，违反开闭原则

**缓解措施：**
- 使用 Spring 的 `@Autowired List<HealthIndicator>` 自动发现所有实现类
- Controller 通过遍历列表自动聚合，无需硬编码组件名称
- 新增组件只需添加 `@Component` 标注的 HealthIndicator 实现类

### Risk 3: 健康检查与业务逻辑的边界模糊

**风险：** 健康检查可能误用业务 Service，导致循环依赖或性能问题

**缓解措施：**
- HealthIndicator 仅依赖基础设施层（Mapper、连接池），不调用业务 Service
- 明确健康检查职责：验证组件可用性，而非验证业务逻辑正确性

## Migration Plan

**部署步骤：**
1. 代码合并后，新 API 自动生效（无需配置变更）
2. 更新 API 文档，通知监控团队接入 `/res/v1/health/custom`
3. 监控系统逐步从 `/actuator/health` 迁移到自定义 API（获取更详细信息）
4. K8s 探针保持使用 `/actuator/health`，不受影响

**回滚策略：**
- 新 API 为增量功能，不影响现有系统
- 如需回滚，直接 revert 代码即可，无数据迁移或配置变更

## Open Questions

1. **是否需要支持健康检查的认证/授权？**
   - 当前设计为公开接口，若需限制访问可添加 Spring Security 配置
   - 建议：初期保持公开，后续根据安全审计需求添加

2. **是否需要支持健康检查的缓存？**
   - 当前设计为实时检查，若高频调用可考虑短时缓存（如 5 秒）
   - 建议：初期不缓存，观察监控调用频率后再优化
