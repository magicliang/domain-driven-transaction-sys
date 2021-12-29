# domain-driven-transaction-sys

一个领域驱动设计的交易系统的例子

## how to build it

`./gradlew clean build -x test --stacktrace`

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
