# domain-driven-transaction-sys

**一个领域驱动设计的交易系统的例子*

TODO：一个留给我自己的问题：我到底要不要让这个服务构造成一个 k8s 的微服务，要不要把所有最佳实践引入这个开源项目里来？

TODO：

1. 梳理 gradle task。补完在 Java 单机场景下的集成测试用例和单元测试用例
2. 实现一个 Spring WebFlux 的 reactor controller。
3. 引入 mariadb4j 做集成测试用。
4. 脚本 docker 化：准备加一个带有 MySQL 的 k8s 集群。
5. mock 支付环节。
6. 写模型设计和系统分层的输入。
7. 实现线程池开源化。
8. 实现基于db 的最大努力型事务。
9. 实现对 aggregate root 的工厂方法化
10. 实现对值对象的构建
11. 补完 JPA 的映射方法
12. 补完 MyBatis的映射方法
13. 要实现动态生成或者 immutable list 的 DRM/MCC/apollo/rainbow 实现方案

## GitHub SSH 免登录配置

如果遇到 `git push` 时提示 "Authentication failed" 或要求输入用户名密码，说明你的仓库使用的是 HTTPS URL 而非 SSH URL。

### 配置步骤

1. **检查 SSH 密钥**
   ```bash
   ls -la ~/.ssh/
   # 应该能看到 id_rsa 和 id_rsa.pub
   ```

2. **复制公钥内容**
   ```bash
   cat ~/.ssh/id_rsa.pub
   ```

3. **添加公钥到 GitHub**
   - 访问：https://github.com/settings/keys
   - 点击 "New SSH key"
   - 粘贴公钥内容并保存

4. **测试 SSH 连接**
   ```bash
   ssh -T git@github.com
   # 成功会显示：Hi username! You've successfully authenticated
   ```

5. **更新 remote URL 为 SSH 格式**
   ```bash
   # 查看当前 remote URL
   git remote -v
   
   # 如果是 HTTPS URL（https://github.com/...），需要改为 SSH URL
   git remote set-url origin git@github.com:magicliang/domain-driven-transaction-sys.git
   
   # 验证修改
   git remote -v
   ```

6. **现在可以免密推送了**
   ```bash
   git push
   ```

### URL 格式对比

- ❌ **HTTPS**：`https://github.com/magicliang/domain-driven-transaction-sys.git`（需要输入密码）
- ✅ **SSH**：`git@github.com:magicliang/domain-driven-transaction-sys.git`（免密登录）

## how to build it

```bash
sdk install java 8.0.432
./gradlew clean build -x test --stacktrace
```

## how to test it

```bash
# 测试和汇总测试报告
./gradlew testAggregateTestReport

# 单引号双引号都可要圈起来特殊的字符，google 搜索 gradle test filtering
# 可以切换到 ./gradlew clean 版本

gradle test 

gradle clean test
# 执行具体的测试需要 build 完成，如果没有妥善构建完成也可能找不到测试

# Executes all tests in SomeTestClass 
gradle test --tests com.magicliang.transaction.sys.aop.factory.ProxyFactoryTest 

# Executes a single specified test in SomeTestClass
gradle test --tests 'com.magicliang.transaction.sys.DomainDrivenTransactionSysApplicationIntegrationTest.testGetWildCardType'

gradle test --tests com.magicliang.transaction.sys.DomainDrivenTransactionSysApplicationIntegrationTest.*test*

gradle test --tests "org.gradle.SomeTestClass.some method containing spaces"


./gradlew clean  test --tests com.magicliang.transaction.sys.aop.factory.ProxyFactoryTest 

./gradlew clean test --tests 'com.magicliang.transaction.sys.DomainDrivenTransactionSysApplicationIntegrationTest.testGetWildCardType'

./gradlew clean test --tests com.magicliang.transaction.sys.DomainDrivenTransactionSysApplicationIntegrationTest.*test*

./gradlew clean test --tests "org.gradle.SomeTestClass.some method containing spaces"
```

# 架构解说

## 经典 gradle 架构

application

- src
- main
- test
- settings.gradle
- build.gradle
- gradle.properties

## gradle 多模块架构

注意，新增 module 最好只增加 gradle module。

    - module1
        -src
            - main
            -test
        - settings.gradle
        - build.gradle
    - module2
        -src
            - main
            -test
        - settings.gradle
        - build.gradle
    - settings.gradle
    - build.gradle
    - gradle.properties

# 如何修复gradle运行问题

![如何修复gradle运行问题](如何修复gradle运行问题.png)

# Sofa 分层的涵义

![sofa分层](sofa分层.png)

# 一些编程的原则

- 尽量对列表尽量使用浅拷贝后再操作的做法，尽量不影响原列表，更不要影响原列表的 subList
- rich object is important
