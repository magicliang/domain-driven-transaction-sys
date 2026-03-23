---
name: OPSX: Archive
description: 在实验性工作流中归档已完成的变更
category: Workflow
tags: [workflow, archive, experimental]
---

在实验性工作流中归档已完成的变更。

**输入**: 可选在 `/opsx:archive` 后指定变更名称（例如 `/opsx:archive add-auth`）。如果省略，检查是否可以从对话上下文中推断。如果模糊或歧义，你必须提示用户选择可用的变更。

**步骤**

1. **如果未提供变更名称，提示选择**

   运行 `openspec list --json` 获取可用变更。使用 **AskUserQuestion 工具**让用户选择。

   仅显示活跃变更（非已归档）。
   如果可用，包括每个变更使用的模式。

   **重要**: 不要猜测或自动选择变更。始终让用户选择。

2. **检查工件完成状态**

   运行 `openspec status --change "<name>" --json` 检查工件完成情况。

   解析 JSON 以理解：
   - `schemaName`: 正在使用的工作流
   - `artifacts`: 工件列表及其状态（`done` 或其他）

   **如果有任何工件不是 `done`:**
   - 显示警告，列出未完成的工件
   - 提示用户确认是否继续
   - 如果用户确认则继续

3. **检查任务完成状态**

   读取任务文件（通常是 `tasks.md`）检查未完成的任务。

   统计标记为 `- [ ]`（未完成）和 `- [x]`（已完成）的任务。

   **如果发现未完成任务:**
   - 显示警告，显示未完成任务数量
   - 提示用户确认是否继续
   - 如果用户确认则继续

   **如果不存在任务文件:** 无任务相关警告继续执行。

4. **评估增量规格同步状态**

   检查 `openspec/changes/<name>/specs/` 处的增量规格。如果不存在，无需同步提示直接继续。

   **如果存在增量规格:**
   - 将每个增量规格与 `openspec/specs/<capability>/spec.md` 处对应的主规格比较
   - 确定将应用哪些更改（添加、修改、删除、重命名）
   - 在提示前显示合并摘要

   **提示选项:**
   - 如果需要更改："立即同步（推荐）"、"不同步直接归档"
   - 如果已同步："立即归档"、"仍然同步"、"取消"

   如果用户选择同步，使用 Task 工具（subagent_type: "general-purpose", prompt: "使用 Skill 工具调用 openspec-sync-specs 处理变更 '<name>'。增量规格分析: <包含分析的增量规格摘要>"）。无论选择如何，继续归档。

5. **执行归档**

   如果归档目录不存在则创建：
   ```bash
   mkdir -p openspec/changes/archive
   ```

   使用当前日期生成目标名称：`YYYY-MM-DD-<change-name>`

   **检查目标是否已存在:**
   - 如果是：失败并报错，建议重命名现有归档或使用不同日期
   - 如果否：将变更目录移动到归档

   ```bash
   mv openspec/changes/<name> openspec/changes/archive/YYYY-MM-DD-<name>
   ```

6. **显示摘要**

   显示归档完成摘要，包括：
   - 变更名称
   - 使用的模式
   - 归档位置
   - 规格同步状态（已同步 / 跳过同步 / 无增量规格）
   - 任何警告的说明（未完成工件/任务）

**成功时的输出**

```
## 归档完成

**变更:** <change-name>
**模式:** <schema-name>
**归档至:** openspec/changes/archive/YYYY-MM-DD-<name>/
**规格:** ✓ 已同步到主规格

所有工件已完成。所有任务已完成。
```

**成功时的输出（无增量规格）**

```
## 归档完成

**变更:** <change-name>
**模式:** <schema-name>
**归档至:** openspec/changes/archive/YYYY-MM-DD-<name>/
**规格:** 无增量规格

所有工件已完成。所有任务已完成。
```

**成功但带警告的输出**

```
## 归档完成（带警告）

**变更:** <change-name>
**模式:** <schema-name>
**归档至:** openspec/changes/archive/YYYY-MM-DD-<name>/
**规格:** 跳过同步（用户选择跳过）

**警告:**
- 归档时有 2 个未完成工件
- 归档时有 3 个未完成任务
- 增量规格同步已跳过（用户选择跳过）

如果这不是有意的，请检查归档内容。
```

**失败时的输出（归档已存在）**

```
## 归档失败

**变更:** <change-name>
**目标:** openspec/changes/archive/YYYY-MM-DD-<name>/

目标归档目录已存在。

**选项:**
1. 重命名现有归档
2. 如果现有归档是重复的，删除它
3. 等到不同日期再归档
```

**约束**
- 如果未提供，始终提示选择变更
- 使用工件图（openspec status --json）检查完成情况
- 不要因警告阻塞归档 - 只需通知并确认
- 移动到归档时保留 .openspec.yaml（它随目录一起移动）
- 清晰显示发生了什么
- 如果请求同步，使用 Skill 工具调用 `openspec-sync-specs`（代理驱动）
- 如果存在增量规格，始终运行同步评估并在提示前显示合并摘要
