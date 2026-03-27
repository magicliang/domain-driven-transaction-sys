# CORE-MODEL 领域模型层

## OVERVIEW

DDD 领域模型定义：聚合根、实体、值对象、领域工厂。不依赖任何基础设施层。

## STRUCTURE

```
core-model/src/main/java/.../core/
├── model/
│   └── entity/             # 领域实体：聚合根、子实体
│       ├── TransPayOrderEntity.java      # 聚合根
│       ├── TransSubOrderEntity.java      # 子订单实体
│       └── TransRequestEntity.java       # 请求实体
├── factory/                # 领域工厂：创建复杂对象
└── shared/                 # 共享概念：值对象接口、工具
    ├── ValueObject.java    # 值对象接口
    └── DomainObjectUtils.java
```

## WHERE TO LOOK

| 任务 | 位置 | 说明 |
|------|------|------|
| 定义聚合根 | `model/entity/TransPayOrderEntity.java` | 包含子订单、支付请求 |
| 定义子实体 | `model/entity/TransSubOrderEntity.java` | 聚合内的子订单 |
| 定义值对象 | `shared/ValueObject.java` | 按值比较的接口 |
| 创建复杂对象 | `factory/` | 领域工厂 |

## CONVENTIONS

- **聚合根**：`TransPayOrderEntity` 是唯一入口，外部只能通过聚合根访问子实体
- **值对象**：实现 `ValueObject` 接口，重写 `equals`/`hashCode` 按值比较
- **无依赖**：core-model 不依赖 common-dal、common-util 等基础设施层
- **Rich Object**：在实体中封装业务行为，而非贫血模型

## ANTI-PATTERNS

- ⚠️ `DomainObjectUtils` TODO：需要实现 commons-lang 代码的包装器

## AGGREGATE DESIGN

```
TransPayOrderEntity (聚合根)
├── List<TransSubOrderEntity> (子订单)
├── TransRequestEntity (支付请求)
└── 业务方法：创建子订单、状态流转等
```