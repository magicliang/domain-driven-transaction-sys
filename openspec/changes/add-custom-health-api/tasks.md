## 1. 数据模型与响应结构

- [x] 1.1 在 `common-util` 模块创建 `HealthStatusVO` 类，包含 `status`、`timestamp`、`components` 字段
- [x] 1.2 在 `common-util` 模块创建 `ComponentHealthVO` 类，包含 `status`、`details` 字段
- [x] 1.3 在 `common-util` 模块创建 `HealthStatusEnum` 枚举（UP、DOWN、WARN）
- [x] 1.4 确保所有 VO 类实现 Serializable 并添加 Lombok 注解（@Data、@NoArgsConstructor、@AllArgsConstructor）

## 2. 健康检查器接口与基础实现

- [x] 2.1 在 `core-service` 模块创建 `HealthIndicator` 接口，定义 `String getComponentName()` 和 `ComponentHealthVO check()` 方法
- [x] 2.2 在 `core-service` 模块创建 `ApplicationHealthIndicator` 实现类，检查 JVM 运行时长和内存使用率
- [x] 2.3 为 `ApplicationHealthIndicator` 添加内存使用率 WARN 阈值判断逻辑（> 90% 返回 WARN）
- [x] 2.4 编写 `ApplicationHealthIndicator` 的单元测试，覆盖 UP 和 WARN 两种场景

## 3. 数据库健康检查器实现

- [x] 3.1 在 `common-dal` 模块创建 `HealthCheckMapper` 接口，定义 `Integer executeHealthCheck()` 方法（执行 SELECT 1）
- [x] 3.2 使用 JdbcTemplate 替代 Mapper，简化实现
- [x] 3.3 在 `core-service` 模块创建 `DatabaseHealthIndicator` 实现类，注入 master 和 slave1 的 JdbcTemplate
- [x] 3.4 实现数据库健康检查逻辑：分别执行 SELECT 1，记录响应时间，捕获超时和异常
- [x] 3.5 为 `DatabaseHealthIndicator` 添加 3 秒超时控制（通过异常捕获和响应时间判断）
- [x] 3.6 编写 `DatabaseHealthIndicator` 的单元测试，使用 @Mock 模拟 JdbcTemplate 行为，覆盖 UP 和 DOWN 场景

## 4. 健康检查服务层

- [x] 4.1 在 `core-service` 模块创建 `ICustomHealthService` 接口，定义 `HealthStatusVO getHealthStatus(String component)` 方法
- [x] 4.2 在 `core-service` 模块创建 `CustomHealthServiceImpl` 实现类，注入 `List<HealthIndicator>`
- [x] 4.3 实现组件过滤逻辑：当 `component` 参数不为空时，仅返回指定组件的健康状态
- [x] 4.4 实现健康状态聚合逻辑：遍历所有 HealthIndicator，计算整体状态（DOWN > WARN > UP）
- [x] 4.5 实现异常隔离：单个 HealthIndicator 抛异常时捕获并标记为 DOWN，不影响其他检查器
- [x] 4.6 编写 `CustomHealthServiceImpl` 的单元测试，使用 @Mock 模拟多个 HealthIndicator，验证聚合逻辑

## 5. Web 控制器层

- [x] 5.1 在 `biz-service-impl` 模块创建 `HealthController` 类，映射路径 `/res/v1/health/custom`
- [x] 5.2 实现 GET 方法，接收可选的 `@RequestParam(required = false) String component` 参数
- [x] 5.3 调用 `ICustomHealthService.getHealthStatus(component)` 并封装为 `ApiResult<HealthStatusVO>` 返回
- [x] 5.4 添加参数校验：当 component 不为内置组件名称（database、application）时返回 400 错误
- [x] 5.5 添加全局异常处理：捕获未预期异常并返回 `ApiResult.fail("健康检查失败")`
- [x] 5.6 添加日志记录：请求开始时记录 component 参数，响应时记录整体状态和耗时

## 6. 集成测试

- [x] 6.1 在 `biz-service-impl/src/test/integration` 创建 `HealthControllerIntegrationTest` 类
- [x] 6.2 编写测试用例：验证 `/res/v1/health/custom` 返回 200 且包含 status、timestamp、components 字段
- [x] 6.3 编写测试用例：验证 `/res/v1/health/custom?component=database` 仅返回 database 组件
- [x] 6.4 编写测试用例：验证 `/res/v1/health/custom?component=unknown` 返回 400 错误
- [x] 6.5 配置 Testcontainers 或 H2 内存数据库，确保集成测试可独立运行

## 7. 文档与代码审查

- [x] 7.1 更新 API 文档（如 Swagger/OpenAPI 配置），添加健康检查端点的说明
- [x] 7.2 在 `HealthController` 添加 JavaDoc 注释，说明 API 用途、参数、响应格式
- [x] 7.3 执行代码格式化（运行 `./gradlew spotlessApply` 或项目配置的格式化工具）
- [x] 7.4 运行全量测试（`./gradlew test`），确保所有单元测试和集成测试通过
- [x] 7.5 自检：确认响应格式符合 design.md 中的 JSON 示例，确认所有 spec 场景已覆盖
