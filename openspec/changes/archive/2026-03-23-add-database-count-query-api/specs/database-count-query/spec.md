## ADDED Requirements

### Requirement: 按表名查询数据库总记录数

系统 SHALL 提供按表名查询数据库总记录数的能力，通过 REST API 暴露查询接口，返回指定表的总行数。

#### Scenario: 查询合法表的总记录数

- **WHEN** 客户端发送 GET 请求到 `/api/v1/database/count?tableName=tb_trans_pay_order`
- **THEN** 系统返回 HTTP 200 状态码
- **AND** 响应体包含表名和总记录数，格式为 `{"success": true, "data": {"tableName": "tb_trans_pay_order", "totalCount": 12345}}`

#### Scenario: 查询不存在的表

- **WHEN** 客户端请求不存在的表名 `tableName=non_existent_table`
- **THEN** 系统返回 HTTP 400 状态码
- **AND** 响应体包含错误信息 `{"success": false, "errorMsg": "表不存在: non_existent_table"}`

#### Scenario: 查询包含非法字符的表名

- **WHEN** 客户端请求包含特殊字符的表名 `tableName=tb_trans_pay_order; DROP TABLE`
- **THEN** 系统返回 HTTP 400 状态码
- **AND** 响应体包含错误信息 `{"success": false, "errorMsg": "表名格式不合法"}`

#### Scenario: 查询超时处理

- **WHEN** 表数据量过大导致 COUNT 查询超过 5 秒
- **THEN** 系统返回 HTTP 504 状态码
- **AND** 响应体包含错误信息 `{"success": false, "errorMsg": "查询超时，请稍后重试"}`

### Requirement: 表名安全校验

系统 SHALL 对输入的表名进行双重校验（正则表达式 + 白名单），防止 SQL 注入攻击。

#### Scenario: 正则校验拦截特殊字符

- **WHEN** 表名包含空格、分号、注释符等特殊字符
- **THEN** 系统拒绝查询并返回 400 错误
- **AND** 错误日志记录非法请求来源 IP

#### Scenario: 白名单校验拦截未授权表

- **WHEN** 表名通过正则校验但不在数据库元数据表中
- **THEN** 系统拒绝查询并返回 400 错误
- **AND** 错误信息提示"表不存在"

#### Scenario: 合法表名通过校验

- **WHEN** 表名符合命名规范且存在于数据库元数据表
- **THEN** 系统执行 COUNT 查询并返回结果

### Requirement: 表名白名单自动加载

系统 SHALL 在首次查询时自动从 `information_schema.tables` 加载所有表名到内存缓存，后续查询直接使用缓存。

#### Scenario: 首次查询触发白名单加载

- **WHEN** 系统启动后首次收到计数查询请求
- **THEN** 系统查询 `information_schema.tables` 获取所有表名
- **AND** 将表名列表缓存到内存（ConcurrentHashMap）
- **AND** 后续查询直接使用缓存，无需重复查询元数据

#### Scenario: 缓存命中

- **WHEN** 表名已在缓存中
- **THEN** 系统跳过元数据查询，直接执行 COUNT 操作
- **AND** 响应时间小于 100ms（不含数据库 COUNT 耗时）

### Requirement: 查询性能保护

系统 SHALL 对 COUNT 查询设置超时限制，防止长时间阻塞线程。

#### Scenario: 查询超时中断

- **WHEN** COUNT 查询执行时间超过 5 秒
- **THEN** 系统中断查询并抛出 TimeoutException
- **AND** 返回 HTTP 504 错误给客户端
- **AND** 记录警告日志包含表名和执行时间

#### Scenario: 正常查询完成

- **WHEN** COUNT 查询在 5 秒内完成
- **THEN** 系统返回查询结果
- **AND** 响应时间记录到监控指标
