## Context

项目采用 DDD 分层架构（SOFA 模式），数据访问层使用 MyBatis。现有 Mapper 接口通过 MyBatis Generator 生成，包含 `countByExample` 方法但需要 Example 对象。运维监控需要快速查询各表总记录数，当前缺少统一的查询入口。

**约束条件**：
- 遵循现有分层架构：common-dal → core-service → biz-service-impl
- MyBatis 使用注解 + XML 混合配置
- 需要防止 SQL 注入（表名参数化）
- 查询性能需优化（COUNT 操作在大数据量下较慢）

## Goals / Non-Goals

**Goals:**
- 提供按表名查询总记录数的 REST API
- 实现动态表名 COUNT 查询的 Mapper 层
- 添加表名白名单校验防止 SQL 注入
- 遵循项目现有代码规范和分层结构

**Non-Goals:**
- 不支持复杂条件计数（仅总记录数）
- 不支持多表联合计数
- 不包含分页或缓存机制（后续可扩展）

## Decisions

### 1. 动态 SQL 实现方式
**选择**：使用 MyBatis `@SelectProvider` + `ScriptRunner` 动态构建 SQL

**理由**：
- 项目已有 `*SqlProvider` 模式（如 `TransPayOrderPoSqlProvider`）
- 避免硬编码表名，支持动态扩展
- 比纯 XML 更灵活，比纯注解更易维护

**替代方案**：
- XML `<script>` 标签：需新增 XML 文件，增加维护成本
- 原生 JDBC `PreparedStatement`：绕过 MyBatis，破坏架构一致性

### 2. 表名安全校验
**选择**：白名单机制 + 正则校验

**实现**：
- 白名单：从数据库元数据表 `information_schema.tables` 查询现有表名
- 正则：`^[a-zA-Z_][a-zA-Z0-9_]*$` 防止特殊字符注入
- 双重校验：先正则后白名单

**理由**：
- MyBatis 不支持表名参数化（`${}` 语法有注入风险）
- 白名单确保只查询合法业务表
- 正则作为第一道防线拦截恶意输入

### 3. API 设计
**选择**：`GET /api/v1/database/count?tableName=xxx`

**理由**：
- RESTful 风格，符合项目现有 API 规范
- GET 请求适合幂等查询操作
- 查询参数简单明了

**响应格式**：
```json
{
  "success": true,
  "data": {
    "tableName": "tb_trans_pay_order",
    "totalCount": 12345
  },
  "errorMsg": null
}
```

### 4. 分层职责划分
- **common-dal**: `DatabaseCountMapper` 接口 + `DatabaseCountSqlProvider`
- **core-service**: `DatabaseCountService` 接口 + `DatabaseCountServiceImpl`
- **biz-service-impl**: `DatabaseCountController` + 请求/响应 DTO

## Risks / Trade-offs

| 风险 | 缓解措施 |
|------|----------|
| COUNT 查询在大数据量表上性能差 | 添加查询超时控制（5秒），后续可引入缓存或异步统计 |
| 表名白名单需维护 | 首次查询时自动从元数据加载，缓存到内存 |
| `${}` 语法存在 SQL 注入风险 | 双重校验（正则 + 白名单），拒绝非法表名 |
| 多数据源场景 | 当前仅支持主数据源，后续可扩展 `@DataSource` 注解路由 |

## Migration Plan

**部署步骤**：
1. 代码合并到主分支
2. 无需数据库变更
3. 重启服务后 API 自动生效

**回滚策略**：
- 直接回滚代码，无数据迁移风险
- API 为新增功能，不影响现有接口

## Open Questions

无
