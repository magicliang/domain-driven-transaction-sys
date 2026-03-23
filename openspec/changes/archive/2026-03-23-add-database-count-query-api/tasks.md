## 1. DAL 层实现

- [x] 1.1 创建 `DatabaseCountMapper` 接口，定义 `countByTableName` 方法
- [x] 1.2 创建 `DatabaseCountSqlProvider` 类，实现动态 SQL 构建逻辑
- [x] 1.3 创建 `TableMetaMapper` 接口，用于查询 `information_schema.tables` 获取表名白名单
- [x] 1.4 在 `common-dal/src/main/resources/mapper/` 创建 `TableMetaMapper.xml` 映射文件

## 2. Core Service 层实现

- [x] 2.1 创建 `DatabaseCountService` 接口，定义 `getTotalCount(String tableName)` 方法
- [x] 2.2 创建 `DatabaseCountServiceImpl` 实现类
- [x] 2.3 实现表名正则校验逻辑（`^[a-zA-Z_][a-zA-Z0-9_]*$`）
- [x] 2.4 实现表名白名单缓存机制（`ConcurrentHashMap` + 懒加载）
- [x] 2.5 实现查询超时控制（5秒限制）
- [x] 2.6 添加异常处理和日志记录

## 3. Biz Service 层实现

- [x] 3.1 创建 `DatabaseCountRequest` DTO 类（包含 `tableName` 字段）
- [x] 3.2 创建 `DatabaseCountResponse` DTO 类（包含 `tableName` 和 `totalCount` 字段）
- [x] 3.3 创建 `DatabaseCountController` 控制器类
- [x] 3.4 实现 `GET /api/v1/database/count` 端点
- [x] 3.5 添加请求参数校验（`@Valid` + `@NotBlank`）
- [x] 3.6 实现统一异常处理和错误响应格式

## 4. 单元测试

- [x] 4.1 编写 `DatabaseCountSqlProvider` 单元测试（验证动态 SQL 生成）
- [x] 4.2 编写 `DatabaseCountServiceImpl` 单元测试（覆盖白名单校验、超时处理）
- [x] 4.3 编写 `DatabaseCountController` 单元测试（验证请求/响应格式）
- [x] 4.4 编写 SQL 注入防护测试用例（验证非法表名拦截）

## 5. 集成测试

- [x] 5.1 创建集成测试类 `DatabaseCountIntegrationTest`
- [x] 5.2 测试合法表名查询场景
- [x] 5.3 测试不存在表名场景
- [x] 5.4 测试非法字符表名场景
- [x] 5.5 测试查询超时场景（使用 Mock 或大表）

## 6. 文档和验证

- [x] 6.1 更新 API 文档（添加新接口说明）
- [x] 6.2 运行完整测试套件（`./gradlew test`）
- [x] 6.3 本地启动服务验证 API 可用性
- [x] 6.4 代码审查和格式检查（`./gradlew check`）
