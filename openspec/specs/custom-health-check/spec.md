# custom-health-check Specification

## Purpose

TBD - 提供自定义健康检查 API，支持查询系统各组件（数据库、应用等）的健康状态，并支持按组件过滤和自动发现健康检查器。

## Requirements

### Requirement: 查询整体健康状态

系统 SHALL 提供自定义健康检查 API 端点 `/res/v1/health/custom`，返回所有组件的综合健康状态。

#### Scenario: 所有组件健康时返回 UP 状态

- **WHEN** 客户端 GET 请求 `/res/v1/health/custom` 且所有组件（数据库、应用等）正常运行
- **THEN** 系统返回 HTTP 200，响应体包含 `"status": "UP"` 和所有组件的详细信息

#### Scenario: 部分组件故障时返回 DOWN 状态

- **WHEN** 客户端 GET 请求 `/res/v1/health/custom` 且至少一个组件（如数据库）不可用
- **THEN** 系统返回 HTTP 200，响应体包含 `"status": "DOWN"` 并标识故障组件

#### Scenario: 响应包含时间戳

- **WHEN** 客户端请求健康检查 API
- **THEN** 响应体包含 `timestamp` 字段，格式为 ISO 8601（如 `2026-03-23T10:30:00Z`）

### Requirement: 按组件查询健康状态

系统 SHALL 支持通过查询参数 `component` 过滤特定组件的健康状态。

#### Scenario: 查询数据库组件健康状态

- **WHEN** 客户端 GET 请求 `/res/v1/health/custom?component=database`
- **THEN** 系统仅返回数据库组件的健康状态，不包含其他组件信息

#### Scenario: 查询不存在的组件时返回错误

- **WHEN** 客户端 GET 请求 `/res/v1/health/custom?component=unknown`
- **THEN** 系统返回 HTTP 400，响应体包含错误信息 "未知的组件名称: unknown"

#### Scenario: 不传 component 参数时返回所有组件

- **WHEN** 客户端 GET 请求 `/res/v1/health/custom`（无查询参数）
- **THEN** 系统返回所有已注册组件的健康状态

### Requirement: 数据库健康检查

系统 SHALL 检查所有配置的数据源（master、slave1）的连接可用性。

#### Scenario: 主从数据库均正常时返回 UP

- **WHEN** 执行数据库健康检查且 master 和 slave1 数据源均可响应 `SELECT 1` 查询
- **THEN** 数据库组件状态为 `UP`，details 包含 master 和 slave1 的响应时间（毫秒）

#### Scenario: 主数据库故障时返回 DOWN

- **WHEN** 执行数据库健康检查且 master 数据源连接失败（超时或异常）
- **THEN** 数据库组件状态为 `DOWN`，details 中 master 的状态为 `DOWN` 并包含错误信息

#### Scenario: 从数据库故障时返回 DOWN

- **WHEN** 执行数据库健康检查且 slave1 数据源连接失败
- **THEN** 数据库组件状态为 `DOWN`，details 中 slave1 的状态为 `DOWN` 并包含错误信息

#### Scenario: 数据库检查超时

- **WHEN** 数据库健康检查的 SQL 查询超过 3 秒未返回
- **THEN** 该数据源状态标记为 `DOWN`，错误信息包含 "检查超时"

### Requirement: 应用基础健康检查

系统 SHALL 检查应用运行的基础指标（运行时长、内存使用率）。

#### Scenario: 应用正常运行时返回 UP

- **WHEN** 执行应用健康检查且 JVM 正常运行
- **THEN** 应用组件状态为 `UP`，details 包含 uptime（秒）和 memoryUsage（0-1 之间的小数）

#### Scenario: 内存使用率过高时返回 WARN

- **WHEN** 执行应用健康检查且 JVM 堆内存使用率超过 90%
- **THEN** 应用组件状态为 `WARN`，details 包含 memoryUsage 并标注 "内存使用率过高"

### Requirement: 统一响应格式

系统 SHALL 使用 `ApiResult<HealthStatusVO>` 包装所有健康检查响应。

#### Scenario: 成功响应格式

- **WHEN** 健康检查请求成功（无论组件状态是 UP 还是 DOWN）
- **THEN** 响应体包含 `success: true`、`data` 字段，`data` 包含 `status`、`timestamp`、`components`

#### Scenario: 参数错误响应格式

- **WHEN** 健康检查请求参数错误（如无效的 component 名称）
- **THEN** 响应体包含 `success: false`、`message` 字段描述错误原因，`data` 为 null

#### Scenario: 系统异常响应格式

- **WHEN** 健康检查过程中发生未捕获异常
- **THEN** 响应体包含 `success: false`、`message: "健康检查失败"`，HTTP 状态码为 500

### Requirement: 健康状态聚合规则

系统 SHALL 根据各组件状态计算整体健康状态。

#### Scenario: 所有组件 UP 时整体状态为 UP

- **WHEN** 所有组件（database、application 等）的状态均为 `UP`
- **THEN** 整体状态 `status` 为 `UP`

#### Scenario: 任一组件 DOWN 时整体状态为 DOWN

- **WHEN** 至少一个组件的状态为 `DOWN`（无论其他组件状态如何）
- **THEN** 整体状态 `status` 为 `DOWN`

#### Scenario: 无 DOWN 但有 WARN 时整体状态为 WARN

- **WHEN** 所有组件状态为 `UP` 或 `WARN`，且至少一个组件为 `WARN`
- **THEN** 整体状态 `status` 为 `WARN`

### Requirement: 健康检查器自动发现

系统 SHALL 通过 Spring 依赖注入自动发现所有 `HealthIndicator` 实现类，无需手动注册。

#### Scenario: 新增健康检查器自动生效

- **WHEN** 开发人员添加新的 `@Component` 标注的 `HealthIndicator` 实现类（如 `PaymentChannelHealthIndicator`）
- **THEN** 该检查器自动包含在健康检查聚合结果中，无需修改 Controller 代码

#### Scenario: 健康检查器异常不影响其他检查器

- **WHEN** 某个 `HealthIndicator` 实现类抛出未捕获异常
- **THEN** 该组件状态标记为 `DOWN`，其他组件的健康检查继续执行并返回结果

### Requirement: 性能要求

系统 SHALL 确保健康检查 API 的响应时间满足监控系统的实时性要求。

#### Scenario: 整体响应时间不超过 5 秒

- **WHEN** 客户端请求 `/res/v1/health/custom` 且所有组件正常
- **THEN** API 响应时间（从请求到响应）不超过 5 秒

#### Scenario: 单个组件检查超时不影响整体响应

- **WHEN** 某个组件的健康检查超时（如数据库查询超过 3 秒）
- **THEN** 该组件标记为 `DOWN`，其他组件检查结果正常返回，整体响应时间不超过 6 秒
