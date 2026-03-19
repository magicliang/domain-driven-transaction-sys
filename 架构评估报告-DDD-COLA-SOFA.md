# domain-driven-transaction-sys 架构评估报告（DDD / COLA / SOFA 对比）

## 1. 报告目的

本报告用于评估当前系统架构成熟度，并从三条参考路径给出可执行改进建议：

- 传统 DDD 经典分层（强调领域模型纯度与依赖倒置）
- 基于 DDD 改进的 COLA 思路（强调工程落地与分层约束）
- 蚂蚁 SOFA 分层思路（强调企业级服务治理与中间件生态）

你可以基于本报告决定后续演进方向（继续强化现有 SOFA 分层、向 COLA 收敛，或保留现状做局部治理）。

---

## 2. 评估范围与证据

### 2.1 评估范围

- Gradle 多模块结构与依赖关系
- 交易主链路：受理 → 支付 → 通知
- 领域层、应用层、基础设施层边界清晰度
- 代码完整性（空实现、TODO/FIXME、示例代码污染）

### 2.2 主要证据文件

- 模块结构与依赖：`settings.gradle`、`build.gradle`、各子模块 `build.gradle`
- 领域模型：`core-model/src/main/java/.../TransPayOrderEntity.java`
- 活动编排：`core-service/src/main/java/.../PaymentActivity.java`
- 业务总线：`biz-shared/src/main/java/.../CommandQueryBus.java`
- 门面编排：`biz-service-impl/src/main/java/.../PaymentFacadeImpl.java`
- 数据访问：`core-service/src/main/java/.../PayOrderManagerImpl.java`
- 模型转换：`core-model/src/main/java/.../TransPayOrderConvertor.java`
- 启动入口：`biz-service-impl/src/main/java/.../DomainDrivenTransactionSysApplication.java`

---

## 3. 当前系统架构画像（现状）

## 3.1 模块分层（结构上）

从目录和 Gradle 依赖看，系统已经具备较完整的企业分层：

- 接入/实现层：`biz-service-impl`
- 业务共享层：`biz-shared`
- 领域模型层：`core-model`
- 领域服务层：`core-service`
- 基础设施层：`common-dal`、`common-service-integration`
- 通用能力层：`common-util`

这与 README 中宣称的 SOFA 分层整体一致。

## 3.2 交易链路（行为上）

主流程链路清晰：

1. 门面层调用 `commandQueryBus.send(...)`
2. `CommandQueryBus` 路由到对应 Handler
3. Handler 组织 Activity（受理/支付/通知）
4. Activity 根据策略枚举分发 DomainStrategy 执行
5. 通过 Manager / Delegate 触达数据库与外部通道

这条链路是当前架构最大优点：**业务主干可读、可追踪、可扩展策略点**。

## 3.3 已有优势（建议保留）

1. **业务流程显式化**：Activity + Strategy 模式把交易步骤拆分得较清楚。
2. **多模块拆分基础较好**：不是单体巨包，具备后续治理空间。
3. **上下文对象统一**：`TransTransactionContext` / `TransactionModel` 提供了流程承载。
4. **并发与幂等意识存在**：Handler 层引入分布式锁与幂等键。

---

## 4. 核心问题与风险（按优先级）

## P0（高优先）

### 4.1 领域层与基础设施强耦合，依赖倒置被破坏

现象：

- `core-model/build.gradle` 直接 `api project(":common-dal")`
- `core-model` 内 converter 直接 import `common.dal.mybatis.po.*`
- `core-service` 内 manager/service 直接依赖 mapper/po

影响：

- 领域模型难以保持“业务语义优先”，被数据库结构牵引
- 测试替身与替换基础设施成本高
- 未来拆分服务或替换存储代价上升

### 4.2 关键路径存在大量空实现与占位实现

例子：

- `TransPayOrderConvertor.toPo()/toDomainEntity()` 直接 `return null`
- `PayOrderManagerImpl` 多个查询/更新方法 `return Collections.emptyList()/true/0`
- `AlipayDelegateImpl.standardPay(...)` 主要是占位逻辑

影响：

- 代码“结构完整但行为不完整”，可运行性与正确性高度依赖外部假设
- 评估架构时容易被目录结构误导

## P1（中优先）

### 4.3 核心模块混入实验性/教学性代码

`core-service/src/main/java` 下存在 `generic/`、`doublebuffer/`、`utilization/` 等非交易域代码。

影响：

- 语义边界被稀释
- 新人理解成本上升
- 架构治理（扫描、分层规则）噪声变大

### 4.4 职责下沉不充分，应用层与领域层边界不稳定

现象：

- Facade/Handler/Activity/Manager 都承担了部分流程控制
- `@Transactional` 主要集中在 Manager，事务边界与用例边界未完全对齐

影响：

- 规则变更时改动点分散
- 难建立稳定的“用例级编排层”

## P2（建议优化）

### 4.5 总线路由方式线性扫描

`CommandQueryBus` 按 `List<BaseHandler>` 逐个匹配 `identify()`。

影响：

- 当前规模问题不大，但业务扩容后检索成本与可观测性会下降

### 4.6 启动类与基础 Bean 探测逻辑偏“演示型”

`DomainDrivenTransactionSysApplication` 中存在大量调试/实验性质逻辑（例如读取配置流演示）。

影响：

- 生产入口职责不够纯粹

---

## 5. 与传统 DDD 经典架构对比

## 5.1 你当前“像 DDD”的部分

- 有聚合根（`TransPayOrderEntity`）与状态迁移方法
- 有领域活动（Activity）和领域策略（Strategy）
- 有统一业务语言（支付订单、请求、通知、受理）

## 5.2 与经典 DDD 的主要偏差

1. **领域层纯度不足**：领域模型直接依赖 DAL PO。
2. **Repository 抽象不足**：更多是“Manager + Mapper”直连。
3. **边界上下文未显式化**：包结构按技术层多于按子域。
4. **应用服务编排不稳定**：Facade/Handler/Activity 分工有重叠。

## 5.3 结论

当前更接近“**DDD 风格 + SOFA 分层工程化骨架**”，不是严格意义上的“经典 DDD 分层落地”。

---

## 6. 与 COLA 架构对比（基于 DDD 的工程化改进）

> 说明：COLA 的核心价值不是替代 DDD，而是把 DDD 在工程层面做“可治理的分层落地”（尤其在 Java 团队协作与多人演进中）。

## 6.1 COLA 对你当前系统最有价值的点

1. **明确应用层（App）用例编排责任**：让 Handler/Facade 角色收敛，避免业务流程散落。
2. **清晰的 DTO/Command/Response 边界**：减少跨层对象污染。
3. **扩展点标准化（Extension）**：替代部分手写策略分发胶水代码。
4. **分层依赖规则可自动检查**：更容易“防回退”。

## 6.2 你当前与 COLA 的差距

- 已有“类似 App 编排”能力，但职责分散在多层。
- 已有 Command/Handler 形态，但未形成统一规范（命名、目录、依赖边界）。
- Domain 对 Infra 仍有反向依赖，违背 COLA 倡导的分层方向。

## 6.3 结论

如果你的目标是“**先把工程可维护性和团队协作稳定下来**”，COLA 思路非常适合作为下一阶段治理框架。

---

## 7. 与 SOFA 架构对比

## 7.1 你当前“已对齐 SOFA”的部分

- 分层命名与模块拆分总体对齐展示层/应用层/领域层/基础层
- 面向企业交易系统的中间件思维（锁、任务、集成、配置）已具备

## 7.2 主要偏差

1. **分层边界有“穿透”**：领域层反向耦合基础设施实现。
2. **基础能力与业务代码混杂**：演示代码混入核心域模块。
3. **部分模块“名义分层”强于“行为分层”**：结构清晰但落地未闭环。

## 7.3 结论

你当前更像“**SOFA 分层骨架已搭建，DDD 内核尚未治理完成**”。

---

## 8. 决策建议（修正版：避免“路线三选一”误区）

> 关键澄清：DDD / COLA / SOFA 不是同一层面的替代关系。
>
> - **SOFA**：更偏工程分层与企业服务体系骨架
> - **DDD**：更偏业务建模与边界治理原则
> - **COLA**：更偏 DDD 的工程落地组织方式（尤其应用层与分层规范）
>
> 因此建议不是“在三者中二选一”，而是：
> **保留 SOFA 骨架，按 DDD 原则治理边界，在需要时引入 COLA 的工程约束。**

## 路径 A：保留现有 SOFA 骨架，先做行为闭环与止血（低风险，优先）

适用：希望最小扰动、快速提升质量。

做法：

1. 先补齐主链路空实现（受理/支付/通知关键路径）
2. 建立最小回归基线（成功链路、失败链路、幂等重复调用）
3. 迁出演示/实验代码，降低核心模块噪声
4. 启动类与入口逻辑去演示化，保持生产入口职责纯粹

## 路径 B：边界治理试点（DDD 原则落地，谨慎引入 COLA 约束）

适用：团队协作规模上升，想建立统一研发范式。

做法：

1. 先按一个子域试点（建议支付链路）做 Repository + Adapter 隔离
2. converter 从 core-model 下沉到 infrastructure adapter
3. 事务边界按用例重画（避免“远程调用 + 本地事务”混边界）
4. 架构守卫先告警后收紧（先 report，再 hard fail）

## 路径 C：引入 COLA 风格规范（条件触发，不强制）

适用：当边界试点完成后，团队仍存在明显协作混乱（命名、目录、依赖频繁回退）。

做法：

1. 在试点域引入 Client/App/Domain/Infra 的包规范
2. 标准化 Command/Response/Assembler，减少跨层对象污染
3. 只在“高频扩展点”引入 Extension，不做全量替换

风险：若在行为未稳定时过早范式化，容易“换抽象不换质量”。

---

## 9. 建议路线图（分阶段，重排优先级）

## 阶段 0（1~2 周）：行为闭环止血（必须先完成）

- 补齐 P0 空实现（convertor / manager / delegate）
- 建立最小可回归用例（成功、失败、幂等）
- 将实验性代码迁出 `core-service` 主路径
- 入口去演示化（启动类保留必要初始化）

交付物：可执行主链路 + 最小回归基线。

## 阶段 1（2~4 周）：边界治理试点（单域）

- 在一个子域引入 repository 接口（Domain）+ adapter 实现（Infra）
- converter 下沉到 adapter，移除 core-model 对 DAL PO 直依赖（先试点）
- 收敛事务边界到用例层并验证远程调用失败恢复路径
- 架构规则先“告警模式”运行

交付物：试点域依赖方向正确 + 失败恢复路径可验证。

## 阶段 2（4~8 周）：扩面与规范化（按需引入 COLA 风格）

- 将试点经验复制到其他子域（按业务价值排序）
- 若协作痛点仍高，再引入 COLA 风格的目录/对象规范
- 架构守卫从告警升级为门禁（仅对新代码先收紧）

交付物：跨域可复制治理模板 + 分层规则稳定执行。

---

## 9.1 关键风险补充（交易系统必须关注）

相较于“命名/抽象风格”，本系统更应优先审查以下风险：

1. **一致性模型风险**：本地事务与远程调用（支付通道/通知）边界是否清晰。
2. **失败恢复风险**：远程成功但本地落库失败、或本地成功远程失败时的补偿策略。
3. **幂等与重试风险**：重复请求、任务重放、通知重复发送是否可证正确。
4. **可观测性风险**：关键链路是否可定位“在哪一步失败、是否已补偿”。

这些风险的优先级应高于“总线线性扫描改 Map”这类结构性优化。

---

## 10. 最终判断（供你决策）

一句话结论：

**这是一个“架构骨架不错、但领域与基础设施边界尚未收口”的交易系统。**

如果你下一步想要“稳妥且见效快”，建议优先采用：

**SOFA 分层骨架不动 + 引入 COLA/DDD 的边界治理方法（先局部试点，再全局推广）。**

这样既不会推倒重来，也能显著降低后续复杂度和维护成本。

---

## 11. 附：本次评估中的关键观察摘要

- 模块分层与主链路设计：良好（结构优势）
- 领域纯度与依赖倒置：不足（核心短板）
- 行为闭环完整性：不足（空实现较多）
- 工程规范一致性：中等（可治理）

综合评分（主观工程评估）：**6.5 / 10**

> 评分含义：具备清晰升级潜力，不建议重写，建议分阶段治理。

---

## 12. 备注（本次复核修订说明）

本报告在初稿基础上做了复核修订，重点修正了：

- 避免把 DDD/COLA/SOFA 错当作互斥路线
- 将路线图重排为“先行为闭环，再边界治理，再范式化”
- 增补交易系统一致性/失败恢复/幂等等关键风险项
