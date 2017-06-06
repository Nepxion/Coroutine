# Coroutine
[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

## 介绍

    Coroutine是基于Kilim/Promise JDeferred的协程式驱动框架，基于Apache Zookeeper的分布式规则存储和动态规则变更通知
    1. 基于微服务框架理念设计
    2. 支持同步/异步调用
    3. 支持串行/并行调用
    4. 支持本地/分布式(包括Thunder，Dubbo，Motan等)/混合链式调用
    5. 支持嵌套规则/子规则调用
    6. 支持本地/分布式规则引用
    7. 支持调用链追踪
    8. 异常捕获后智能处理链式调用的终止