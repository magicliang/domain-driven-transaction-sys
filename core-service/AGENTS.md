# CORE-SERVICE 领域服务层

## OVERVIEW

领域层核心，实现 DDD 业务逻辑：Activities 编排流程、Strategies 实现可替换策略、Managers 管理领域对象。

## STRUCTURE

```
core-service/src/main/java/.../core/
├── domain/
│   ├── activity/           # 领域活动：编排业务流程
│   │   ├── acceptance/     # 受理流程
│   │   ├── payment/        # 支付流程
│   │   └── notification/   # 通知流程
│   └── strategy/           # 领域策略：可替换的业务逻辑
│       └── acceptance/     # 受理策略
├── manager/                # 领域管理器：协调实体操作
├── service/                # 领域服务接口及实现
├── config/                 # 领域配置
└── event/                  # 领域事件
```

## WHERE TO LOOK

| 任务 | 位置 | 关键类 |
|------|------|--------|
| 编排受理流程 | `domain/activity/acceptance/` | `AcceptanceActivity` |
| 编排支付流程 | `domain/activity/payment/` | `PaymentActivity` |
| 编排通知流程 | `domain/activity/notification/` | `NotificationActivity` |
| 实现同步受理 | `domain/strategy/acceptance/` | `SyncAcceptanceStrategy` |
| 管理支付订单 | `manager/` | `PayOrderManagerImpl` |
| 领域服务 | `service/` | `PayOrderServiceImpl` |

## CONVENTIONS

- Activity 负责**编排**业务流程，不直接操作数据
- Strategy 实现**可替换**的业务逻辑，通过接口隔离
- Manager 协调**实体操作**，处理持久化细节
- Service 暴露**领域能力**给应用层调用

## ANTI-PATTERNS

- ⚠️ `PayOrderManagerImpl` 有 FIXME 注释，需要补完
- ⚠️ `PaymentActivity` TODO：以后用中间件表达扩展点

## KEY PATTERNS

```
Activity (流程编排)
    ↓ 调用
Strategy (业务策略) + Manager (实体管理)
    ↓ 调用
core-model (实体/值对象) + common-dal (持久化)
```