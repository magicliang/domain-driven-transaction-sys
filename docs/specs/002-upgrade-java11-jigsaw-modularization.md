# Spec 002: Java 8 -> Java 11 升级 + JPMS (Jigsaw) 模块化教程

- **状态**: 待实施
- **创建日期**: 2026-03-16

## 背景

项目当前使用 Java 8 (Gradle Toolchain `JavaLanguageVersion.of(8)`)。Java 8 的公开更新已于 2022 年 12 月终止。Java 11 是 Java 8 之后的第一个 LTS 版本（2018 年 9 月发布，扩展支持至 2026 年 9 月），引入了 JPMS (Java Platform Module System, 即 Project Jigsaw) 的完整生产级支持、局部变量类型推断 (`var`)、HTTP Client API 等重要特性。

本文档是一份**分步教程**，指导如何将项目从 Java 8 升级到 Java 11，并基于 Jigsaw 项目为每个 Gradle 子模块编写 `module-info.java`，实现完整的 JPMS 模块化。

## 目标

1. 将编译和运行时 Java 版本从 8 升级到 11
2. 为 8 个 Gradle 子模块各编写 `module-info.java`，实现完整 JPMS 模块化
3. 保持 Spring Boot 2.7.18 / Spring Cloud 2021.0.8 不变（二者均官方支持 Java 11）
4. 确保 IntelliJ IDEA 和 VSCode 均能正确识别项目的 JDK 11 和 JPMS 配置

## 兼容性矩阵

在开始之前，需要确认当前技术栈与 Java 11 的兼容性：

| 组件 | 当前版本 | Java 11 兼容性 | 备注 |
|------|---------|---------------|------|
| Spring Boot | 2.7.18 | 官方支持 (Java 8~21) | 无需升级 |
| Spring Cloud | 2021.0.8 | 官方支持 | 无需升级 |
| Gradle | 8.6 | 完全支持 Java 11 Toolchain | 无需升级 |
| Jackson | 2.18.2 | 完全兼容 | - |
| Guava | 31.0.1-jre | 完全兼容 | - |
| MyBatis Spring Boot Starter | 2.3.2 | 完全兼容 | - |
| Testcontainers | 1.19.8 | 完全兼容 | - |
| mariaDB4j | 2.5.3 | 兼容（需验证 ARM） | 可选 profile |
| Lombok | 1.18.24 (test) / BOM managed | 需 1.18.20+ | 建议统一到 1.18.30+ |
| mysql-connector-java | 5.1.47 | 兼容 | 后续建议升级到 8.x |
| java-jwt (auth0) | 4.5.0 | 完全兼容 | - |

### JPMS 支持现状

**重要说明**：Spring Boot 2.7.x (Spring Framework 5.x) 生态对 JPMS 的支持是**有限的**：

| 库 | JPMS 支持级别 | 说明 |
|----|-------------|------|
| Spring Framework 5.x | Automatic-Module-Name | 无真正 module-info.java，作为自动模块运行 |
| MyBatis 3.5+ | Automatic-Module-Name | 自动模块 |
| Jackson 2.x | 部分 module-info | 在 META-INF/versions/11 中有多版本支持 |
| Guava 31.x | Automatic-Module-Name | 33.4+ 才有真正 module-info |
| Log4j2 | 部分支持 | 服务发现机制在 JPMS 下有已知问题 |
| commons-lang3 | Automatic-Module-Name | 自动模块 |
| HttpComponents | Automatic-Module-Name | 自动模块 |

这意味着我们的项目模块将是**显式模块**（有 module-info.java），而大部分三方依赖将作为**自动模块**运行在模块路径上。这是当前 Spring Boot 2.x 生态下的最佳实践——既能获得编译期的强封装和依赖可见性检查，又能兼容尚未完全模块化的三方库。

---

## 第 1 章：前置准备与环境检查

### 1.1 安装 JDK 11

#### macOS (推荐 SDKMAN)

```bash
# 安装 SDKMAN（如未安装）
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# 安装 Eclipse Temurin JDK 11（与 Docker 镜像一致）
sdk install java 11.0.24-tem

# 验证
java -version
# openjdk version "11.0.24" ...
```

#### macOS (Homebrew)

```bash
brew install openjdk@11
sudo ln -sfn $(brew --prefix openjdk@11)/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-11.jdk
java -version
```

#### 手动安装

从 [Adoptium](https://adoptium.net/temurin/releases/?version=11) 下载对应平台的安装包。

### 1.2 确认 Gradle 兼容性

Gradle 8.6 原生支持 Java 11 Toolchain，无需升级 Gradle。验证：

```bash
./gradlew --version
# Gradle 8.6
```

**注意**：Gradle Toolchain 机制会自动下载和管理 JDK。如果你不想依赖自动下载，需要确保本地已安装 JDK 11 并在 `JAVA_HOME` 或 Toolchain 检测路径中。

### 1.3 Git 分支策略

建议在独立分支上进行所有升级操作：

```bash
git checkout -b feat/java11-jpms
```

**推荐的提交策略**（3 次独立提交，便于回滚）：
1. **第一次提交**：纯 Java 11 升级（Toolchain/gradle.properties/Dockerfile），不涉及 JPMS
2. **第二次提交**：JPMS 前置清理（非标准包迁移、包名整理）
3. **第三次提交**：添加所有 module-info.java + Gradle 构建适配

---

## 第 2 章：Java 8 -> 11 基础升级

本章只做 Java 版本升级，不涉及 JPMS 模块化。完成本章后项目应能在 Java 11 下正常编译和运行。

### 2.1 修改 build.gradle: Toolchain 8 -> 11

**文件**: `build.gradle` (根项目，第 79 行)

```diff
 java {
     toolchain {
-        languageVersion = JavaLanguageVersion.of(8)
+        languageVersion = JavaLanguageVersion.of(11)
     }
     withJavadocJar()
     withSourcesJar()
 }
```

### 2.2 修改 script-plugin.gradle: sourceCompatibility

**文件**: `script-plugin.gradle` (第 7~8 行)

```diff
-sourceCompatibility = 1.8
-targetCompatibility = 1.8
+sourceCompatibility = 11
+targetCompatibility = 11
```

**说明**：虽然 Toolchain 已经指定了 Java 11，但 `script-plugin.gradle` 中的显式设置可能覆盖 Toolchain 配置。保持二者一致以避免混淆。

### 2.3 修改 gradle.properties: 移除废弃 JVM 参数

**文件**: `gradle.properties` (第 11 行)

```diff
-org.gradle.jvmargs=-Xmx4g -XX:MaxPermSize=4g -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8
+org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8
```

**原因**：`-XX:MaxPermSize` 是 Java 7 时代的永久代 (PermGen) 参数。Java 8 已将 PermGen 替换为 Metaspace，该参数在 Java 8 中已弃用并产生警告，在 Java 11 中仍然产生警告。应替换为 `-XX:MaxMetaspaceSize`。

### 2.4 补充 javax.annotation 依赖

Java 11 移除了 JSR 250 中的 `javax.annotation` 包（包括 `@PreDestroy`、`@PostConstruct`、`@Nonnull`、`@Nullable` 等）。

项目中使用了这些注解的位置：
- `DomainDrivenTransactionSysApplication.java` 使用了 `@PreDestroy`
- `TestController.java` 使用了 `@PreDestroy`
- `ProfilingInterceptor.java` 使用了 `@Nonnull` / `@Nullable`

**方案**：在 `build.gradle` 根项目的 `subprojects.dependencies` 块中添加：

```groovy
// build.gradle, subprojects { dependencies { ... } } 块内
implementation 'javax.annotation:javax.annotation-api:1.3.2'
```

**说明**：Spring Boot 2.7.18 的依赖管理 BOM 通常已经包含了 `javax.annotation-api` 的版本管理。如果通过 `spring-boot-starter` 传递引入了此依赖，则可能无需显式添加。建议先不添加，编译后根据报错决定。

### 2.5 Dockerfile 基础镜像升级

**文件**: `deploy/docker/Dockerfile`

```diff
 # Stage 1: Build
-FROM eclipse-temurin:8-jdk AS builder
+FROM eclipse-temurin:11-jdk AS builder

 # ... 构建步骤不变 ...

 # Stage 2: Runtime
-FROM eclipse-temurin:8-jre
+FROM eclipse-temurin:11-jre
```

同时检查 K8s 部署配置中是否有硬编码的 Java 版本引用（当前未发现）。

### 2.6 验证检查点

```bash
# 清理并编译
./gradlew clean compileJava

# 运行所有测试
./gradlew test

# 如果测试全部通过，说明 Java 11 基础升级成功
# 此时可以做第一次提交
git add -A && git commit -m "chore: upgrade Java 8 -> 11 (toolchain, gradle properties, Dockerfile)"
```

**可能遇到的问题**：

| 症状 | 原因 | 解决 |
|------|------|------|
| `Ignoring option MaxPermSize` 警告 | 未修改 gradle.properties | 参见 2.3 |
| `package javax.annotation does not exist` | Java 11 移除了 JSR 250 | 参见 2.4 |
| Lombok 编译错误 | Lombok 版本过低 | 升级到 1.18.30+，参见第 5 章 |
| `InaccessibleObjectException` | Spring/反射访问被限制 | Java 11 默认 `--illegal-access=permit`，仅警告不阻断 |

---

## 第 3 章：JPMS 模块化前置清理

在编写 `module-info.java` 之前，需要先清理一些不符合 JPMS 要求的代码结构。

### 3.1 core-service 非标准包迁移

**问题**：`core-service/src/main/java/` 下存在 5 个不在 `com.magicliang.transaction.sys` 命名空间下的顶级包：

```
doublebuffer/          ← CapacityDrivenDoubleBufferQueue, DoubleBufferDemo
generic/               ← WildcardCapture, CurryHowardExample, GenericReflectionTest, Pair 等
generic/aireport/      ← 航班状态系统泛型示例
security/jwt/          ← JwtUtil
utilization/           ← SimpleCPULoader
```

**原因**：JPMS 要求一个模块的所有包在逻辑上属于该模块。虽然 JPMS 不强制包名前缀，但这些顶级包名（`doublebuffer`, `generic` 等）在 `module-info.java` 中需要被显式 `exports`/`opens`，且与模块名的命名约定不一致。更重要的是，如果未来其他模块或第三方库也有同名顶级包，会导致 split package 冲突。

**方案 A（推荐）：迁移到标准命名空间**

将这些包移动到 `com.magicliang.transaction.sys.core.experimental` 下：

```
com.magicliang.transaction.sys.core.experimental.doublebuffer
com.magicliang.transaction.sys.core.experimental.generic
com.magicliang.transaction.sys.core.experimental.generic.aireport
com.magicliang.transaction.sys.core.experimental.security.jwt
com.magicliang.transaction.sys.core.experimental.utilization
```

**方案 B：移到独立的 playground 模块**

在 `settings.gradle` 中添加 `include 'playground'`，将实验代码移入独立模块。该模块可以不参与 JPMS 模块化。

**方案 C：直接删除**

如果这些代码确认是纯演示/学习用途且不会在生产中使用，可以直接删除。

### 3.2 主类包名位置讨论

**现状**：`DomainDrivenTransactionSysApplication.java` 位于 `com.magicliang.transaction.sys` 包中（biz-service-impl 模块）。

**问题**：`com.magicliang.transaction.sys` 是所有模块的共同前缀。虽然其他模块没有在这个精确的包中放置 `.java` 文件（因此不构成 split package），但从 JPMS 模块边界清晰度来看，将主类放在模块特有的包中更合适。

**建议**：将主类移到 `com.magicliang.transaction.sys.biz.service.impl` 包下。

```diff
-package com.magicliang.transaction.sys;
+package com.magicliang.transaction.sys.biz.service.impl;
```

**注意**：移动主类后，`@SpringBootApplication` 的默认组件扫描范围会从 `com.magicliang.transaction.sys` 缩小到 `com.magicliang.transaction.sys.biz.service.impl`，需要显式配置 `scanBasePackages`：

```java
@SpringBootApplication(scanBasePackages = "com.magicliang.transaction.sys")
public class DomainDrivenTransactionSysApplication {
    // ...
}
```

**或者选择不移动**：如果不想改动主类位置，也可以不移动。只要确认 `com.magicliang.transaction.sys` 这个包只在 biz-service-impl 模块中有文件，就不会产生 split package 问题。

### 3.3 资源文件访问审计

JPMS 下，模块内的资源文件默认对其他模块不可见（除非通过 `opens` 指令开放）。以下是需要关注的资源文件：

| 资源 | 所在模块 | 访问方式 | JPMS 影响 |
|------|---------|---------|----------|
| `applicationContext.xml` | biz-service-impl | `classpath*:spring/*.xml` | 需确保资源包被 opens |
| `mybatis/mybatis.xml` | biz-service-impl | `classpath:mybatis/mybatis.xml` | Spring Boot 自动配置处理 |
| `sql/mysql/schema.ddl` | biz-service-impl | `ClassPathResource` | 需确保资源可达 |
| `sql/tc-init-privileges.sql` | common-dal | `ClassPathResource` | 需确保资源可达 |
| `log4j2/*.xml` | biz-service-impl | 系统属性指定 | 不受 JPMS 影响 |

**好消息**：当应用以 classpath 模式启动（Spring Boot 的默认行为，即使有 module-info.java），资源文件访问不受 JPMS 限制。只有在严格的 module-path 模式下才需要额外的 `opens` 配置。

**建议**：保持 Spring Boot 的默认 classpath 启动模式。`module-info.java` 的主要价值在于**编译期**的模块边界检查，运行时仍然使用 classpath。

### 3.4 验证检查点

完成前置清理后：

```bash
./gradlew clean test
# 确保所有测试通过

git add -A && git commit -m "refactor: JPMS 前置清理 - 迁移非标准包到标准命名空间"
```

---

## 第 4 章：逐模块编写 module-info.java

### 4.1 策略说明

#### 执行顺序：自底向上

按照模块依赖图，从叶节点开始，逐模块添加 `module-info.java`：

```
依赖图（箭头表示 "依赖"）：

biz-service-impl → biz-shared → core-service → core-model → common-util (无依赖)
                 → common-service-facade (无依赖)          → common-service-integration → common-util
                                                           → common-dal → common-util

执行顺序：
1. common-util          (无项目依赖)
2. common-service-facade (无项目依赖)
3. common-dal           (依赖 common-util)
4. common-service-integration (依赖 common-util)
5. core-model           (依赖 common-util, common-service-integration, common-dal)
6. core-service          (依赖 core-model)
7. biz-shared           (依赖 core-service)
8. biz-service-impl     (依赖 biz-shared, common-service-facade)
```

#### 三条核心原则

1. **`exports`：精确控制** -- 只导出其他模块**编译时**真正需要的包。内部实现包（如 `*.impl`）不导出。
2. **`opens`：对框架宽松** -- Spring DI、Jackson、MyBatis 等框架需要反射访问的包，使用 `opens` 开放。可以用非限定 `opens`（对所有模块开放反射）或 `opens ... to` 指定目标模块。
3. **`requires transitive` 对齐 Gradle `api`** -- Gradle 中用 `api` 声明的依赖，在 module-info 中用 `requires transitive`；用 `implementation` 声明的依赖，在 module-info 中用 `requires`。

#### 查找三方库的 Automatic-Module-Name

对于不确定模块名的三方 JAR，可以用以下命令查询：

```bash
# 查看 JAR 的模块描述符
jar --describe-module --file=path/to/xxx.jar

# 或者在 Gradle 缓存中查找
find ~/.gradle/caches -name "guava-31.0.1-jre.jar" -exec jar --describe-module --file={} \;
```

### 4.2 common-util

**文件**: `common-util/src/main/java/module-info.java`

这是依赖图的底层模块，无项目内部依赖。所有包通过 `api` 暴露给上层。

```java
module com.magicliang.transaction.sys.common.util {

    // === 项目依赖 ===
    // （无项目内部依赖）

    // === 三方依赖（通过 api 传递，使用 requires transitive）===
    requires transitive com.google.common;                    // guava
    requires transitive org.apache.commons.lang3;             // commons-lang3
    requires transitive commons.collections;                  // commons-collections
    requires transitive com.fasterxml.jackson.core;           // jackson-core
    requires transitive com.fasterxml.jackson.databind;       // jackson-databind
    requires transitive com.fasterxml.jackson.annotation;     // jackson-annotations
    requires transitive org.apache.httpcomponents.httpcore;    // httpcore
    requires transitive org.apache.httpcomponents.httpclient;  // httpclient
    requires transitive commons.io;                           // commons-io

    // === 三方依赖（内部使用，不传递）===
    requires org.apache.httpcomponents.httpmime;              // httpmime
    requires fluent.hc;                                       // fluent-hc

    // === Spring 框架（通过 subprojects 的 spring-boot-starter 引入）===
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires org.apache.logging.log4j;                        // log4j2

    // === 导出所有包（这是基础工具层，被所有上层引用）===
    exports com.magicliang.transaction.sys.common;
    exports com.magicliang.transaction.sys.common.aop;
    exports com.magicliang.transaction.sys.common.concurrent;
    exports com.magicliang.transaction.sys.common.concurrent.lock;
    exports com.magicliang.transaction.sys.common.constant;
    exports com.magicliang.transaction.sys.common.enums;
    exports com.magicliang.transaction.sys.common.exception;
    exports com.magicliang.transaction.sys.common.type;
    exports com.magicliang.transaction.sys.common.util;
    exports com.magicliang.transaction.sys.common.util.apm;
    exports com.magicliang.transaction.sys.common.util.apm.internal;

    // === 开放反射访问（Spring DI、Jackson 序列化等）===
    opens com.magicliang.transaction.sys.common;
    opens com.magicliang.transaction.sys.common.enums;
    opens com.magicliang.transaction.sys.common.exception;
    opens com.magicliang.transaction.sys.common.util;
    opens com.magicliang.transaction.sys.common.concurrent;
    opens com.magicliang.transaction.sys.common.concurrent.lock;
}
```

**关键说明**：
- Lombok 是编译期注解处理器，不需要在 `module-info.java` 中声明 `requires`
- `requires transitive` 与 `build.gradle` 中的 `api` 配置一一对应
- 三方库的 automatic module name 需要通过 `jar --describe-module` 验证，上面列出的名称可能需要根据实际 JAR 内容调整

### 4.3 common-service-facade

**文件**: `common-service-facade/src/main/java/module-info.java`

最简单的模块，仅包含两个服务接口。

```java
module com.magicliang.transaction.sys.common.service.facade {

    // === 三方依赖 ===
    requires spring.context;

    // === 导出 ===
    exports com.magicliang.transaction.sys.common.service.facade;
}
```

### 4.4 common-dal

**文件**: `common-dal/src/main/java/module-info.java`

这是**最复杂的模块**，涉及 MyBatis Mapper 反射、JPA、DataSource 配置、Testcontainers。

```java
module com.magicliang.transaction.sys.common.dal {

    // === 项目依赖 ===
    requires transitive com.magicliang.transaction.sys.common.util;  // api project(":common-util")

    // === Spring 框架 ===
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.jdbc;
    requires spring.tx;
    requires spring.orm;
    requires spring.data.jpa;

    // === 数据库相关 ===
    requires java.sql;
    requires java.persistence;                        // JPA API (javax.persistence)
    requires mybatis;                                  // MyBatis core
    requires mybatis.spring;                           // mybatis-spring
    requires mybatis.spring.boot.autoconfigure;        // mybatis-spring-boot-starter
    requires com.zaxxer.hikari;                        // HikariCP
    requires pagehelper;                               // pagehelper

    // === Testcontainers（运行时嵌入式数据库）===
    requires org.testcontainers;                       // testcontainers-core
    requires org.testcontainers.mariadb;               // testcontainers-mariadb

    // === mariaDB4j（可选，x86 嵌入式数据库）===
    requires mariaDB4j;                                // mariaDB4j
    requires mariaDB4j.springboot;                     // mariaDB4j-springboot

    // === 导出 ===
    exports com.magicliang.transaction.sys.common.dal;
    exports com.magicliang.transaction.sys.common.dal.datasource;
    exports com.magicliang.transaction.sys.common.dal.mybatis.mapper;
    exports com.magicliang.transaction.sys.common.dal.mybatis.po;

    // === 开放反射访问 ===
    // MyBatis 需要反射访问 Mapper 接口（动态代理）和 PO 类（结果映射）
    opens com.magicliang.transaction.sys.common.dal.mybatis.mapper;
    opens com.magicliang.transaction.sys.common.dal.mybatis.po;
    // Spring @Configuration 需要反射
    opens com.magicliang.transaction.sys.common.dal.datasource;
    opens com.magicliang.transaction.sys.common.dal;
}
```

**注意事项**：
- MyBatis Mapper 是通过 JDK 动态代理创建的，必须 `opens` mapper 包
- PO (Persistent Object) 类被 MyBatis 通过反射实例化和赋值，必须 `opens`
- `mariaDB4j` 和 `org.testcontainers` 的 automatic module name 需要验证
- 某些 JAR 可能没有 Automatic-Module-Name，此时 JPMS 会根据 JAR 文件名自动推导（这可能不稳定），可能需要在 Gradle 中添加 `--add-modules` 参数

### 4.5 common-service-integration

**文件**: `common-service-integration/src/main/java/module-info.java`

```java
module com.magicliang.transaction.sys.common.service.integration {

    // === 项目依赖 ===
    requires transitive com.magicliang.transaction.sys.common.util;

    // === Spring 框架 ===
    requires spring.context;
    requires spring.beans;

    // === 导出 ===
    exports com.magicliang.transaction.sys.common.service.integration;
    exports com.magicliang.transaction.sys.common.service.integration.constant;
    exports com.magicliang.transaction.sys.common.service.integration.delegate;
    exports com.magicliang.transaction.sys.common.service.integration.delegate.alipay;
    exports com.magicliang.transaction.sys.common.service.integration.delegate.sequence;
    exports com.magicliang.transaction.sys.common.service.integration.param;
    // 注意：impl 包不导出（内部实现细节）

    // === 开放反射访问 ===
    // Spring DI 需要反射访问 @Component 实现类
    opens com.magicliang.transaction.sys.common.service.integration.delegate.alipay.impl;
    opens com.magicliang.transaction.sys.common.service.integration.delegate.sequence.impl;
    // Jackson/Lombok @Data 序列化
    opens com.magicliang.transaction.sys.common.service.integration.param;
}
```

### 4.6 core-model

**文件**: `core-model/src/main/java/module-info.java`

```java
module com.magicliang.transaction.sys.core.model {

    // === 项目依赖（全部通过 api 传递）===
    requires transitive com.magicliang.transaction.sys.common.util;
    requires transitive com.magicliang.transaction.sys.common.service.integration;
    requires transitive com.magicliang.transaction.sys.common.dal;

    // === Spring 框架 ===
    requires spring.context;
    requires spring.beans;

    // === 导出 ===
    exports com.magicliang.transaction.sys.core.factory;
    exports com.magicliang.transaction.sys.core.model.context;
    exports com.magicliang.transaction.sys.core.model.entity;
    exports com.magicliang.transaction.sys.core.model.entity.convertor;
    exports com.magicliang.transaction.sys.core.model.entity.helper;
    exports com.magicliang.transaction.sys.core.model.entity.validator;
    exports com.magicliang.transaction.sys.core.model.event;
    exports com.magicliang.transaction.sys.core.model.request;
    exports com.magicliang.transaction.sys.core.model.request.acceptance;
    exports com.magicliang.transaction.sys.core.model.request.idgeneration;
    exports com.magicliang.transaction.sys.core.model.request.notification;
    exports com.magicliang.transaction.sys.core.model.request.payment;
    exports com.magicliang.transaction.sys.core.model.response;
    exports com.magicliang.transaction.sys.core.model.response.acceptance;
    exports com.magicliang.transaction.sys.core.model.response.idgeneration;
    exports com.magicliang.transaction.sys.core.model.response.notification;
    exports com.magicliang.transaction.sys.core.model.response.payment;
    exports com.magicliang.transaction.sys.core.shared;
    exports com.magicliang.transaction.sys.core.shared.experimental;

    // === 开放反射访问 ===
    // 领域实体类（Lombok @Data / Jackson 序列化 / Spring DI）
    opens com.magicliang.transaction.sys.core.model.entity;
    opens com.magicliang.transaction.sys.core.model.context;
    opens com.magicliang.transaction.sys.core.model.event;
    opens com.magicliang.transaction.sys.core.model.request;
    opens com.magicliang.transaction.sys.core.model.request.acceptance;
    opens com.magicliang.transaction.sys.core.model.request.idgeneration;
    opens com.magicliang.transaction.sys.core.model.request.notification;
    opens com.magicliang.transaction.sys.core.model.request.payment;
    opens com.magicliang.transaction.sys.core.model.response;
    opens com.magicliang.transaction.sys.core.model.response.acceptance;
    opens com.magicliang.transaction.sys.core.model.response.idgeneration;
    opens com.magicliang.transaction.sys.core.model.response.notification;
    opens com.magicliang.transaction.sys.core.model.response.payment;
    opens com.magicliang.transaction.sys.core.shared;
    opens com.magicliang.transaction.sys.core.factory;
}
```

### 4.7 core-service

**文件**: `core-service/src/main/java/module-info.java`

**前提**：已完成第 3 章的非标准包迁移。如果选择了方案 A（迁移到 `core.experimental.*`），则需要在此 module-info 中包含这些包。

```java
module com.magicliang.transaction.sys.core.service {

    // === 项目依赖 ===
    requires transitive com.magicliang.transaction.sys.core.model;

    // === 三方依赖 ===
    requires com.auth0.jwt;                           // java-jwt (implementation, 不传递)

    // === Spring 框架 ===
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.aop;
    requires spring.tx;                               // @Transactional

    // === 导出 ===
    exports com.magicliang.transaction.sys.aop;
    exports com.magicliang.transaction.sys.aop.advice;
    exports com.magicliang.transaction.sys.aop.aspect;
    exports com.magicliang.transaction.sys.aop.factory;
    exports com.magicliang.transaction.sys.aop.generator;
    exports com.magicliang.transaction.sys.core.config;
    exports com.magicliang.transaction.sys.core.domain.activity;
    exports com.magicliang.transaction.sys.core.domain.activity.acceptance;
    exports com.magicliang.transaction.sys.core.domain.activity.idgeneration;
    exports com.magicliang.transaction.sys.core.domain.activity.notification;
    exports com.magicliang.transaction.sys.core.domain.activity.payment;
    exports com.magicliang.transaction.sys.core.domain.enums;
    exports com.magicliang.transaction.sys.core.domain.strategy;
    exports com.magicliang.transaction.sys.core.domain.strategy.acceptance;
    exports com.magicliang.transaction.sys.core.domain.strategy.idgeneration;
    exports com.magicliang.transaction.sys.core.domain.strategy.notification;
    exports com.magicliang.transaction.sys.core.domain.strategy.payment;
    exports com.magicliang.transaction.sys.core.event;
    exports com.magicliang.transaction.sys.core.manager;
    exports com.magicliang.transaction.sys.core.service;
    // 注意：impl 包不导出

    // === 开放反射访问 ===
    // Spring DI (@Component, @Service, @Configuration, @Value)
    opens com.magicliang.transaction.sys.core.config;
    opens com.magicliang.transaction.sys.core.domain.activity.acceptance;
    opens com.magicliang.transaction.sys.core.domain.activity.idgeneration;
    opens com.magicliang.transaction.sys.core.domain.activity.notification;
    opens com.magicliang.transaction.sys.core.domain.activity.payment;
    opens com.magicliang.transaction.sys.core.domain.strategy.acceptance;
    opens com.magicliang.transaction.sys.core.domain.strategy.idgeneration;
    opens com.magicliang.transaction.sys.core.domain.strategy.notification;
    opens com.magicliang.transaction.sys.core.domain.strategy.payment;
    opens com.magicliang.transaction.sys.core.manager.impl;
    opens com.magicliang.transaction.sys.core.service.impl;
    opens com.magicliang.transaction.sys.core.event;
    // AOP 切面需要反射
    opens com.magicliang.transaction.sys.aop.advice;
    opens com.magicliang.transaction.sys.aop.aspect;
    opens com.magicliang.transaction.sys.aop.generator.impl;

    // === 实验性代码（迁移后的新位置）===
    // 如果选择方案 A，取消以下注释：
    // exports com.magicliang.transaction.sys.core.experimental.doublebuffer;
    // exports com.magicliang.transaction.sys.core.experimental.generic;
    // exports com.magicliang.transaction.sys.core.experimental.security.jwt;
    // opens com.magicliang.transaction.sys.core.experimental.doublebuffer;
    // opens com.magicliang.transaction.sys.core.experimental.generic;
}
```

### 4.8 biz-shared

**文件**: `biz-shared/src/main/java/module-info.java`

```java
module com.magicliang.transaction.sys.biz.shared {

    // === 项目依赖 ===
    requires transitive com.magicliang.transaction.sys.core.service;

    // === Spring 框架 ===
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.tx;

    // === 导出 ===
    exports com.magicliang.transaction.sys.biz.shared;
    exports com.magicliang.transaction.sys.biz.shared.enums;
    exports com.magicliang.transaction.sys.biz.shared.event;
    exports com.magicliang.transaction.sys.biz.shared.handler;
    exports com.magicliang.transaction.sys.biz.shared.locator;
    exports com.magicliang.transaction.sys.biz.shared.request;
    exports com.magicliang.transaction.sys.biz.shared.request.acceptance;
    exports com.magicliang.transaction.sys.biz.shared.request.acceptance.convertor;
    exports com.magicliang.transaction.sys.biz.shared.request.callback;
    exports com.magicliang.transaction.sys.biz.shared.request.notification;
    exports com.magicliang.transaction.sys.biz.shared.request.notification.convertor;
    exports com.magicliang.transaction.sys.biz.shared.request.payment;
    exports com.magicliang.transaction.sys.biz.shared.request.payment.convertor;

    // === 开放反射访问 ===
    opens com.magicliang.transaction.sys.biz.shared;
    opens com.magicliang.transaction.sys.biz.shared.handler;
    opens com.magicliang.transaction.sys.biz.shared.event;
    opens com.magicliang.transaction.sys.biz.shared.locator;
    opens com.magicliang.transaction.sys.biz.shared.request.acceptance;
    opens com.magicliang.transaction.sys.biz.shared.request.acceptance.convertor;
    opens com.magicliang.transaction.sys.biz.shared.request.callback;
    opens com.magicliang.transaction.sys.biz.shared.request.notification;
    opens com.magicliang.transaction.sys.biz.shared.request.notification.convertor;
    opens com.magicliang.transaction.sys.biz.shared.request.payment;
    opens com.magicliang.transaction.sys.biz.shared.request.payment.convertor;
}
```

### 4.9 biz-service-impl (Spring Boot 主应用)

**文件**: `biz-service-impl/src/main/java/module-info.java`

这是最顶层的模块，也是 Spring Boot 的启动入口。

```java
module com.magicliang.transaction.sys.biz.service.impl {

    // === 项目依赖 ===
    requires com.magicliang.transaction.sys.biz.shared;
    requires com.magicliang.transaction.sys.common.service.facade;

    // === Spring Boot 启动所需 ===
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.web;
    requires spring.webmvc;

    // === 其他三方依赖 ===
    requires spring.boot.actuator;
    requires spring.boot.actuator.autoconfigure;
    requires spring.boot.devtools;
    requires java.servlet;                             // javax.servlet (Tomcat)

    // === 开放所有应用包给 Spring（Spring Boot 需要全面扫描）===
    // 主类所在包（如果未移动主类）
    opens com.magicliang.transaction.sys;
    // 门面层
    opens com.magicliang.transaction.sys.biz.service.impl.facade;
    opens com.magicliang.transaction.sys.biz.service.impl.facade.impl;
    // 异步任务
    opens com.magicliang.transaction.sys.biz.service.impl.job;
    // RPC
    opens com.magicliang.transaction.sys.biz.service.impl.rpc;
    // Web 层
    opens com.magicliang.transaction.sys.biz.service.impl.web;
    opens com.magicliang.transaction.sys.biz.service.impl.web.advice;
    opens com.magicliang.transaction.sys.biz.service.impl.web.config;
    opens com.magicliang.transaction.sys.biz.service.impl.web.controller;
    opens com.magicliang.transaction.sys.biz.service.impl.web.filter;
    opens com.magicliang.transaction.sys.biz.service.impl.web.http;
    opens com.magicliang.transaction.sys.biz.service.impl.web.interceptor;
    opens com.magicliang.transaction.sys.biz.service.impl.web.model.vo;
    opens com.magicliang.transaction.sys.biz.service.impl.web.util;
}
```

**说明**：作为主应用模块，biz-service-impl 通常不需要 `exports`（没有其他模块依赖它）。所有包都需要通过 `opens` 开放给 Spring 做反射访问。

### 4.10 验证检查点

```bash
# 编译所有模块（Gradle 在检测到 module-info.java 时自动切换到模块路径编译）
./gradlew clean compileJava

# 如果编译失败，最常见的错误是：
# 1. "module not found: xxx" → 三方库的 automatic module name 不正确，需要用 jar --describe-module 确认
# 2. "package xxx is not visible" → 需要在依赖模块中 exports 对应的包
# 3. "requires transitive xxx" 循环 → 检查依赖图是否有循环

# 运行测试
./gradlew test

# 提交
git add -A && git commit -m "feat: 添加 JPMS module-info.java 到所有 8 个子模块"
```

---

## 第 5 章：Gradle 构建配置适配

### 5.1 Gradle 8.6 的 JPMS 自动检测

**好消息**：Gradle 8.6 的 `java-library` 插件在检测到 `src/main/java/module-info.java` 时，会自动将 `compileJava` 任务从 classpath 模式切换到 module-path 模式。无需手动配置 `--module-path`。

但仍有一些需要手动配置的场景。

### 5.2 Lombok 在 JPMS 下的 --add-opens 处理

Lombok 是编译期注解处理器，需要深度访问 JDK 编译器内部 API。在 Java 11 模块系统下，需要显式开放这些内部模块。

在 `build.gradle` 根项目的 `allprojects` 或 `subprojects` 块中添加：

```groovy
// build.gradle, 在 allprojects { } 或 subprojects { } 块内
tasks.withType(JavaCompile).configureEach {
    options.fork = true
    options.forkOptions.jvmArgs += [
        '--add-opens', 'jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED',
        '--add-opens', 'jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED',
        '--add-opens', 'jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED',
        '--add-opens', 'jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED',
        '--add-opens', 'jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED',
        '--add-opens', 'jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED',
        '--add-opens', 'jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED',
        '--add-opens', 'jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED',
        '--add-opens', 'jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED',
    ]
}
```

**注意**：Lombok 1.18.20+ 已内置自动处理部分 `--add-opens`。如果使用 1.18.30+ 版本，可能不需要手动添加上述所有参数。建议先尝试不加参数编译，如果遇到 `IllegalAccessError: class lombok.javac.*` 再按需添加。

### 5.3 测试代码的 module patching 和 --add-opens

Gradle 的 JPMS 支持在编译测试代码时使用 **module patching** 机制：测试代码被 "打补丁" 进入主模块，共享同一个模块命名空间。但测试框架（JUnit、Mockito、Spring Test）需要反射访问被测模块的内部类。

在 `subprojects` 块的 `test` 任务中添加：

```groovy
// build.gradle, subprojects { } 块内
test {
    // ... 现有配置 ...

    // JPMS: 为测试框架开放反射访问
    jvmArgs += [
        '--add-opens', 'java.base/java.lang=ALL-UNNAMED',
        '--add-opens', 'java.base/java.lang.reflect=ALL-UNNAMED',
        '--add-opens', 'java.base/java.util=ALL-UNNAMED',
    ]
}
```

#### biz-service-impl 测试目录的特殊处理

`biz-service-impl/build.gradle` 有自定义的 test source sets：

```groovy
sourceSets {
    test.java.srcDirs = ['src/test/integration/java', 'src/test/unit/java']
}
```

确认这两个目录下的测试代码在 JPMS module patching 下能正确运行。如果遇到模块访问问题，可在 `biz-service-impl/build.gradle` 的 `test` 任务中添加额外的 `--add-opens` 和 `--add-reads`。

### 5.4 验证检查点

```bash
# 完整的构建 + 测试
./gradlew clean build

# 验证 bootJar 能正常生成
./gradlew :biz-service-impl:bootJar

# 验证应用能启动（需要 Docker/Podman 运行 Testcontainers）
./gradlew :biz-service-impl:bootRun
```

---

## 第 6 章：IDE 配置

### 6.1 IntelliJ IDEA

#### 方法 1：通过 Gradle 同步（推荐）

IntelliJ 会自动从 Gradle 配置中读取 Java 版本和模块信息：

1. 打开 `View → Tool Windows → Gradle`
2. 点击刷新按钮 (Reload All Gradle Projects)
3. IntelliJ 会自动检测 Toolchain 的 `JavaLanguageVersion.of(11)` 并下载/配置 JDK 11

#### 方法 2：手动配置

1. **Project SDK**:
   - `File → Project Structure → Project`
   - SDK: 选择或添加 JDK 11
   - Language Level: `11 - Local variable syntax for lambda parameters`

2. **Module Language Level**:
   - `File → Project Structure → Modules`
   - 确认每个模块的 Language Level 为 `11`

3. **JPMS 自动识别**:
   - IntelliJ 在检测到 `module-info.java` 文件时会自动启用 JPMS 模式
   - Module Dependencies 面板会显示 `requires` 声明的依赖关系

#### .idea/misc.xml 更新

如果手动配置后发现 `.idea/misc.xml` 未更新，需要确认内容变更为：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="ExternalStorageConfigurationManager" enabled="true" />
  <component name="ProjectRootManager" version="2" languageLevel="JDK_11"
             default="true" project-jdk-name="11" project-jdk-type="JavaSDK" />
</project>
```

**注意**：通过 Gradle 同步通常会自动更新此文件。`.idea/` 目录建议加入 `.gitignore`（除非团队需要共享 IDE 配置）。

#### 已知 IntelliJ 问题

- **Automatic Module 红色报错**：IntelliJ 可能对 `requires spring.context;` 等 automatic module 声明显示红色错误下划线，但实际编译能通过。这是 IntelliJ 的已知 JPMS 支持局限。解决方法：`File → Invalidate Caches → Restart`。
- **Gradle 委托编译 vs IntelliJ 内部编译**：建议在 `Settings → Build → Build Tools → Gradle` 中选择 `Build and run using: Gradle`，让 Gradle 而非 IntelliJ 负责编译，避免 IDE 编译器与 Gradle 配置不一致。

### 6.2 VSCode (Extension Pack for Java)

#### 前置：安装必要扩展

确保安装了以下 VSCode 扩展：
- `vscjava.vscode-java-pack` (Extension Pack for Java) -- 包含 Language Support、Debugger、Test Runner
- `vscjava.vscode-gradle` (Gradle for Java) -- Gradle 支持
- `GabrielBB.vscode-lombok` (Lombok Annotations Support) -- Lombok 支持

#### 配置 JDK 11

在项目根目录创建或编辑 `.vscode/settings.json`：

```json
{
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-11",
            "path": "/path/to/jdk-11",
            "default": true
        }
    ],
    "java.jdt.ls.java.home": "/path/to/jdk-11",
    "java.import.gradle.java.home": "/path/to/jdk-11",
    "java.compile.nullAnalysis.mode": "automatic"
}
```

**路径示例**：
- SDKMAN: `"~/.sdkman/candidates/java/11.0.24-tem"`
- Homebrew: `"/opt/homebrew/opt/openjdk@11"`
- 手动安装: `"/Library/Java/JavaVirtualMachines/temurin-11.jdk/Contents/Home"`

#### JPMS 识别

VSCode 的 Java 扩展（底层使用 Eclipse JDT Language Server）支持 JPMS：
- 会自动识别 `module-info.java`
- 在代码补全和错误检查中遵循模块边界
- `requires` 中的模块名会提供自动补全

#### 常见问题

- **清理 workspace 缓存**：如果 JPMS 配置未被识别，执行 `Ctrl+Shift+P` → `Java: Clean Java Language Server Workspace`
- **Gradle 导入问题**：执行 `Ctrl+Shift+P` → `Java: Clean Workspace` 然后重新打开项目
- **Automatic Module 识别**：VSCode 对 automatic module 的支持不如 IntelliJ 完善，可能有部分误报

---

## 第 7 章：已知陷阱与排错手册

| # | 症状 | 根因 | 解决方案 |
|---|------|------|---------|
| 1 | `java.lang.IllegalAccessError: class lombok.javac.*` | Lombok 需要访问 JDK 内部 compiler API | 在 `compileJava.options.forkOptions.jvmArgs` 中添加 `--add-opens`（参见第 5.2 节） |
| 2 | `java.lang.reflect.InaccessibleObjectException` | Spring DI 反射访问被 JPMS 阻止 | 在对应模块的 module-info 中为该包添加 `opens` 指令 |
| 3 | `Invalid bound statement (not found)` | MyBatis MapperScannerConfigurer 无法反射访问 Mapper 接口 | `opens com.magicliang.transaction.sys.common.dal.mybatis.mapper;` |
| 4 | `Cannot access field "xxx" via reflection` | Jackson 需要反射访问 POJO 字段 | `opens` 包含 `@Data` 注解的 POJO 所在包 |
| 5 | `module not found: xxx` | 三方 JAR 的 automatic module name 与 `requires` 中的名称不匹配 | 使用 `jar --describe-module --file=xxx.jar` 查看实际名称 |
| 6 | `Package xxx in both module A and B` (Split Package) | 两个模块包含相同的 Java 包 | 迁移冲突的包。本项目已确认无 split package |
| 7 | `Ignoring option MaxPermSize` JVM 警告 | `gradle.properties` 中 `-XX:MaxPermSize` 在 Java 11 已废弃 | 替换为 `-XX:MaxMetaspaceSize`（参见第 2.3 节） |
| 8 | `package javax.annotation does not exist` | Java 11 移除了 JSR 250 | 添加 `javax.annotation:javax.annotation-api:1.3.2` 依赖 |
| 9 | JUnit/Mockito 测试中 `InaccessibleObjectException` | 测试框架 module patching 不完整 | 在 test 任务 JVM 参数中添加 `--add-opens`（参见第 5.3 节） |
| 10 | `classpath*:` Spring XML 配置加载失败 | 模块路径下 classpath* 的行为变化 | 确保 Spring Boot 以 classpath 模式启动（默认行为），或将 XML 配置改为 Java Config |
| 11 | 日期/数字格式化行为与 Java 8 不一致 | Java 11 默认使用 Unicode CLDR locale 数据（JEP 252） | 添加 JVM 参数 `-Djava.locale.providers=COMPAT,SPI` |
| 12 | GC 停顿特征变化 | Java 11 默认 GC 从 Parallel GC 变为 G1 GC | 如需保持一致可添加 `-XX:+UseParallelGC`，或直接拥抱 G1 |
| 13 | IntelliJ 对 `requires` automatic module 显示红色错误 | IntelliJ JPMS 支持局限 | `File → Invalidate Caches → Restart`，或设置 `Build and run using: Gradle` |

---

## 第 8 章：回滚方案

### 方案 A：完全回滚到 Java 8（无 JPMS）

如果升级过程中遇到不可解决的阻塞问题：

```bash
# 回到升级前的分支
git checkout main
# 或者回退特定提交
git revert HEAD~3..HEAD
```

### 方案 B：保留 Java 11，仅回滚 JPMS

如果 Java 11 基础升级成功但 JPMS 模块化遇到问题：

1. 删除所有 `module-info.java` 文件：
```bash
find . -name "module-info.java" -path "*/src/main/java/*" -delete
```
2. 撤销 Gradle 中添加的 JPMS 相关 `--add-opens` 配置
3. 项目将以 "Java 11 + classpath 模式" 运行，完全等价于 Java 8 的运行模式

**JPMS 回滚的安全性**：删除 `module-info.java` 后，Gradle 会自动从模块路径切换回 classpath 模式。不需要任何其他配置变更。这是 JPMS 设计的向后兼容保证。

### 方案 C：分阶段部分保留

可以只为部分模块保留 `module-info.java`（比如只保留 common-util 和 common-service-facade 这两个简单模块），其他模块删除 `module-info.java`。但这种混合模式需要注意：有 `module-info.java` 的模块在模块路径上，没有的在 classpath 上，二者的交互需要额外的 `--add-reads` 配置。**不推荐**这种方式，建议要么全部启用要么全部回滚。

---

## 附录 A：完整依赖 JPMS 兼容性矩阵

| 依赖 | Maven 坐标 | Automatic-Module-Name | 真正 module-info | 备注 |
|------|-----------|----------------------|-----------------|------|
| Guava | com.google.guava:guava:31.0.1-jre | `com.google.common` | 无 (33.4+ 才有) | |
| Commons Lang3 | org.apache.commons:commons-lang3:3.12.0 | `org.apache.commons.lang3` | 无 | |
| Commons Collections | commons-collections:commons-collections:3.2.2 | `commons.collections` | 无 | 需验证 |
| Jackson Core | com.fasterxml.jackson.core:jackson-core | `com.fasterxml.jackson.core` | 有 (MR-JAR) | |
| Jackson Databind | com.fasterxml.jackson.core:jackson-databind | `com.fasterxml.jackson.databind` | 有 (MR-JAR) | |
| Jackson Annotations | com.fasterxml.jackson.core:jackson-annotations | `com.fasterxml.jackson.annotation` | 有 (MR-JAR) | |
| HttpCore | org.apache.httpcomponents:httpcore:4.4.15 | `org.apache.httpcomponents.httpcore` | 无 | 需验证 |
| HttpClient | org.apache.httpcomponents:httpclient:4.5.13 | `org.apache.httpcomponents.httpclient` | 无 | 需验证 |
| Commons IO | commons-io:commons-io:2.10.0 | `commons.io` | 无 | 需验证 |
| Spring Boot | org.springframework.boot:spring-boot | `spring.boot` | 无 | Automatic |
| Spring Context | org.springframework:spring-context | `spring.context` | 无 | Automatic |
| Spring Beans | org.springframework:spring-beans | `spring.beans` | 无 | Automatic |
| Spring Core | org.springframework:spring-core | `spring.core` | 无 | Automatic |
| Spring Web | org.springframework:spring-web | `spring.web` | 无 | Automatic |
| Spring WebMVC | org.springframework:spring-webmvc | `spring.webmvc` | 无 | Automatic |
| Spring AOP | org.springframework:spring-aop | `spring.aop` | 无 | Automatic |
| Spring TX | org.springframework:spring-tx | `spring.tx` | 无 | Automatic |
| Spring Data JPA | org.springframework.data:spring-data-jpa | `spring.data.jpa` | 无 | Automatic |
| MyBatis | org.mybatis:mybatis | `mybatis` | 无 | 需验证 |
| MyBatis Spring | org.mybatis:mybatis-spring | `mybatis.spring` | 无 | 需验证 |
| java-jwt (Auth0) | com.auth0:java-jwt:4.5.0 | `com.auth0.jwt` | 无 | 需验证 |
| Log4j2 API | org.apache.logging.log4j:log4j-api | `org.apache.logging.log4j` | 有 | |
| Log4j2 Core | org.apache.logging.log4j:log4j-core | `org.apache.logging.log4j.core` | 有 | 服务发现有 JPMS 问题 |
| Testcontainers | org.testcontainers:testcontainers | `org.testcontainers` | 无 | 需验证 |
| HikariCP | com.zaxxer:HikariCP | `com.zaxxer.hikari` | 无 | 需验证 |
| PageHelper | com.github.pagehelper:pagehelper | `pagehelper` | 无 | 需验证 |

**"需验证" 的含义**：上表中标注 "需验证" 的 Automatic-Module-Name 是根据常见约定推测的，实际名称需要通过以下命令确认：

```bash
jar --describe-module --file=path/to/xxx.jar
```

如果 JAR 没有 `Automatic-Module-Name`，JPMS 会根据文件名自动推导模块名（去掉版本号、将 `-` 替换为 `.`），这种推导可能不稳定。

---

## 附录 B：module-info.java 文件位置汇总

| 模块 | 文件路径 |
|------|---------|
| common-util | `common-util/src/main/java/module-info.java` |
| common-service-facade | `common-service-facade/src/main/java/module-info.java` |
| common-dal | `common-dal/src/main/java/module-info.java` |
| common-service-integration | `common-service-integration/src/main/java/module-info.java` |
| core-model | `core-model/src/main/java/module-info.java` |
| core-service | `core-service/src/main/java/module-info.java` |
| biz-shared | `biz-shared/src/main/java/module-info.java` |
| biz-service-impl | `biz-service-impl/src/main/java/module-info.java` |

---

## 附录 C：涉及修改的文件清单

### 必须修改

| 文件 | 修改内容 |
|------|---------|
| `build.gradle` (根项目) | Toolchain 8->11, allprojects 增加 JPMS 编译参数 |
| `script-plugin.gradle` | sourceCompatibility/targetCompatibility 1.8->11 |
| `gradle.properties` | 移除 -XX:MaxPermSize, 改为 -XX:MaxMetaspaceSize |
| `deploy/docker/Dockerfile` | 基础镜像 temurin:8 -> temurin:11 |

### 必须新增

| 文件 | 说明 |
|------|------|
| 8 个 `module-info.java` | 参见附录 B |

### 建议修改（可选）

| 文件 | 修改内容 |
|------|---------|
| `.idea/misc.xml` | JDK_1_8 -> JDK_11 (通常由 Gradle 同步自动处理) |
| `.vscode/settings.json` | 添加 JDK 11 运行时配置 |
| core-service 中 `doublebuffer/`、`generic/`、`security/`、`utilization/` | 迁移到标准包结构 |
| `DomainDrivenTransactionSysApplication.java` | 可选：迁移到 `biz.service.impl` 包 |
