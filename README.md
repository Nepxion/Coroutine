# Nepxion Coroutine
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/Nepxion/Coroutine/blob/master/LICENSE)
[![Build Status](https://travis-ci.org/Nepxion/Coroutine.svg?branch=master)](https://travis-ci.org/Nepxion/Coroutine)

Nepxion Coroutine是一款基于Kilim + Promise JDeferred + Zookeeper + Spring Boot的协程式驱动框架，提供分布式规则存储和动态规则变更通知

## 介绍
支持如下功能

    1. 基于微服务框架理念设计
    2. 支持同步/异步调用
    3. 支持串行/并行调用
    4. 支持本地/分布式(包括Thunder，Dubbo，Motan等)/混合链式调用
    5. 支持嵌套规则/子规则调用
    6. 支持本地/分布式规则引用
    7. 支持调用链追踪
    8. 异常捕获后智能处理链式调用的终止

## 架构
协程工作场景图

![Alt text](https://github.com/Nepxion/Coroutine/blob/master/coroutine-doc/Coroutine1.jpg)

Coroutine架构图

![Alt text](https://github.com/Nepxion/Coroutine/blob/master/coroutine-doc/Coroutine2.jpg)

Coroutine链式调用图

![Alt text](https://github.com/Nepxion/Coroutine/blob/master/coroutine-doc/Coroutine3.jpg)

## 规则
```java
<?xml version="1.0" encoding="UTF-8"?>
<coroutine>
    <!-- 规则定义 -->
    <!-- 1. 规则目录即为注册中心的目录(category)节点名，规则名称即为存储当前规则内容的规则(rule)节点名；如果是本地规则，这两者可以随意定义。例如方法调用时，CoroutineManager.load().startSync("规则目录", "规则名称"...) -->
    <!-- 2. 协程(coroutine)节点下可以存在多个规则(rule)节点，以版本号(version)为区分，驱动过程采用最大版本号的规则，版本号必须全局唯一 -->
    <rule version="1">
        <!-- 规则组件定义 -->
        <!-- 规则组件支持本地引用和远程分布式(例如Dubbo接口)引用 -->

        <!-- 1. 规则组件的本地引用方式，采用类反射机制 -->
        <!--    class为类定义，class属性为类的全路径，例如class="com.nepxion.coroutine.test.service.impl.AServiceImpl" -->
        <!--    index为索引号，在当前规则下必须全局唯一 -->
        <!--    method为方法定义，method属性为对应方法名 -->
        <!--    parameterTypes为参数类型定义，如果一个接口/类下，存在多态的方法(即方法名相同，参数类型不一样)，必须以参数类型作为区分 -->
        <component>
            <class class="com.nepxion.coroutine.test.service.impl.AServiceImpl">
                <method index="1" method="doA" parameterTypes="java.lang.String,int"/>
                <method index="2" method="doA" parameterTypes="java.lang.String"/>
            </class>
            <class class="com.nepxion.coroutine.test.service.impl.BServiceImpl">
                <method index="3" method="doB"/>
            </class>
        </component>

        <!-- 2. 规则组件的远程分布式注入方式，采用接口注入机制方式 -->
        <!--    applicationContext为标准的Spring xml路径配置方式，例如applicationContext="classpath*:cApplicationContext.xml"，applicationContext.xml名称必须全局唯一 -->
        <!--    id为Spring Bean的id，id必须全局唯一 -->
        <!--    index为索引号，在当前规则下必须全局唯一 -->
        <!--    method为方法定义，method属性为对应方法名 -->
        <!--    parameterTypes为参数类型定义，如果一个接口/类下，存在多态的方法(即方法名相同，参数类型不一样)，必须以参数类型作为区分 -->
        <component applicationContext="classpath*:cApplicationContext.xml">
            <class id="cService">
                <method index="4" method="doC"/>
            </class>
        </component>
        <component applicationContext="classpath*:dApplicationContext.xml">
            <class id="dService">
                <method index="5" method="doD"/>
            </class>
        </component>

        <!-- 子规则依赖定义，可以存在多个依赖(dependency)节点 -->
        <!-- 1. 子规则不能当前父规则，否则会引起死循环。例如父规则A，引用子规则B，子规则B又引用父规则A -->
        <dependency index="5" category="A规则目录" rule="A-1规则" chain="a" timeout="5000"/>
        <dependency index="6" category="A规则目录" rule="A-2规则" chain="b" timeout="5000"/>
        <dependency index="7" category="B规则目录" rule="B-1规则" chain="c" timeout="5000"/>
        <dependency index="8" category="B规则目录" rule="B-2规则" chain="d" timeout="5000"/>

        <!-- 链式调用定义 -->
        <!-- 可定义多个chain。调用端需要把name值传入，如果配置里name不配，则传入null即可 -->
        <!-- 1. 并行(when)的索引(index)值列表，不需要区分次序 -->
        <!-- 2. 串行(then)的索引(index)值列表，需要区分次序 -->
        <chain name="x">
            <then index="1,2"/>
            <when index="3,4"/>
            <then index="5,6,7,8"/>
        </chain>

        <chain name="y">
            <then index="1,2"/>
            <when index="3,4"/>
            <then index="5,6,7,8"/>
        </chain>
    </rule>
</coroutine>
```

## 示例
异步调用
```java
CoroutineManager.load().startAsync("PayRoute", "Rule", chainName, new String[] { "入参" }, false, new CoroutineCallback<CoroutineResult<Object>>() {
    @Override
    public void onResult(CoroutineResult<Object> result) {
        LOG.info("异步回调结果: 线程序号={}, id={}, result={}", index, result.getId(), result.getResult());
    }

    @Override
    public void onError(Exception exception) {
        LOG.error("异步回调异常", exception);
    }
});
```

同步调用
```java
try {
    CoroutineResult<Object> result = CoroutineManager.load().startSync("PayRoute", "Rule", chainName, new String[] { "入参" }, 3000, false);
    LOG.info("同步调用结果: 线程序号={}, id={}, result={}", index, result.getId(), result.getResult());
} catch (Exception e) {
    LOG.error("同步调用异常", e);
}
```

### 本地调用方式

参照coroutine-test工程

定义规则1
```java
<?xml version="1.0" encoding="UTF-8"?>
<coroutine>
    <rule version="1">
        <component>
            <class class="com.nepxion.coroutine.test.service.impl.AServiceImpl">
                <method index="1" method="doThen"/>
                <method index="2" method="doWhen"/>
                <method index="3" method="doMerge"/>
            </class>
            <class class="com.nepxion.coroutine.test.service.impl.BServiceImpl">
                <method index="4" method="doThen"/>
                <method index="5" method="doWhen"/>
                <method index="6" method="doMerge"/>
            </class>
            <class class="com.nepxion.coroutine.test.service.impl.CServiceImpl">
                <method index="7" method="doThen"/>
                <method index="8" method="doWhen"/>
                <method index="9" method="doMerge"/>
            </class>
            <class class="com.nepxion.coroutine.test.service.impl.DServiceImpl">
                <method index="10" method="doThen"/>
                <method index="11" method="doWhen"/>
                <method index="12" method="doMerge"/>
            </class>
        </component>

        <dependency index="13" category="PayRoute" rule="SubRule" chain="chain2-2" file="rule2.xml" timeout="5000"/>

        <chain name="chain1-1">
            <then index="1,4"/>
            <when index="8,11"/>
            <then index="12,1,13"/>
        </chain>

        <chain name="chain1-2">
            <then index="1,4"/>
            <when index="8,11"/>
            <then index="12,1,13"/>
        </chain>
    </rule>
</coroutine>
```

定义规则2
```java
<?xml version="1.0" encoding="UTF-8"?>
<coroutine>
    <rule version="1">
        <component>
            <class class="com.nepxion.coroutine.test.service.impl.AServiceImpl">
                <method index="1" method="doThen"/>
                <method index="2" method="doWhen"/>
                <method index="3" method="doMerge"/>
            </class>
            <class class="com.nepxion.coroutine.test.service.impl.BServiceImpl">
                <method index="4" method="doThen"/>
                <method index="5" method="doWhen"/>
                <method index="6" method="doMerge"/>
            </class>
            <class class="com.nepxion.coroutine.test.service.impl.CServiceImpl">
                <method index="7" method="doThen"/>
                <method index="8" method="doWhen"/>
                <method index="9" method="doMerge"/>
            </class>
            <class class="com.nepxion.coroutine.test.service.impl.DServiceImpl">
                <method index="10" method="doThen"/>
                <method index="11" method="doWhen"/>
                <method index="12" method="doMerge"/>
            </class>
        </component>

        <chain name="chain2-1">
            <when index="2,5"/>
            <then index="9,10"/>
            <when index="8,11"/>
            <then index="3,4"/>
        </chain>

        <chain name="chain2-2">
            <then index="1,4,7,10"/>
        </chain>
    </rule>
</coroutine>
```

调用入口
```java
package com.nepxion.coroutine.test;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Timer;
import java.util.TimerTask;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.callback.CoroutineCallback;
import com.nepxion.coroutine.data.entity.CoroutineResult;
import com.nepxion.coroutine.framework.core.CoroutineManager;

public class CoroutineTest {
    private static final Logger LOG = LoggerFactory.getLogger(CoroutineTest.class);

    @Test
    public void testRemote() throws Exception {
        // 请确保Zookeeper有对应的规则（运行CoroutineRuleRegistryTest相关方法）

        // 从远程注册中心装载
        // 启动和远程注册中心连接
        CoroutineManager.start();

        // 解析远端规则（支持子规则引用）
        CoroutineManager.parseRemote("PayRoute", "Rule");

        // 链名称从xml配置中获取
        invokeAsync("chain1-1");
        invokeSync("chain1-2");

        System.in.read();
    }

    @Test
    public void testLocalRule1() throws Exception {
        // 从本地装载
        // 解析本地规则（不支持子规则引用）
        CoroutineManager.parseLocal("PayRoute", "Rule", "rule1.xml");

        // 链名称从xml配置中获取
        invokeAsync("chain1-1");
        invokeSync("chain1-2");

        System.in.read();
    }

    @Test
    public void testLocalRule2() throws Exception {
        // 从本地装载
        // 解析本地规则（不支持子规则引用）
        CoroutineManager.parseLocal("PayRoute", "Rule", "rule2.xml");

        // 链名称从xml配置中获取
        invokeAsync("chain2-1");
        invokeSync("chain2-2");

        System.in.read();
    }

    public void invokeAsync(final String chainName) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 1; i++) {
                    final int index = i % 5;
                    CoroutineManager.load().startAsync("PayRoute", "Rule", chainName, new String[] { "Start[" + index + "]" }, false, new CoroutineCallback<CoroutineResult<Object>>() {
                        @Override
                        public void onResult(CoroutineResult<Object> result) {
                            LOG.info("异步回调结果: 线程序号={}, id={}, result={}", index, result.getId(), result.getResult());
                        }

                        @Override
                        public void onError(Exception exception) {
                            LOG.error("异步回调异常", exception);
                        }
                    });
                }
                LOG.info("------------------------------------------------------------");
            }
        }, 0, 20000);
    }

    public void invokeSync(final String chainName) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 1; i++) {
                    final int index = i % 5;

                    try {
                        CoroutineResult<Object> result = CoroutineManager.load().startSync("PayRoute", "Rule", chainName, new String[] { "Start[" + index + "]" }, 3000, false);
                        LOG.info("同步调用结果: 线程序号={}, id={}, result={}", index, result.getId(), result.getResult());
                    } catch (Exception e) {
                        LOG.error("同步调用异常", e);
                    }
                }
                LOG.info("------------------------------------------------------------");
            }
        }, 0, 20000);
    }
}
```

运行结果
```java
2017-12-23 19:13:43.641 INFO [Coroutine-192.168.1.3-thread-1][com.nepxion.coroutine.data.cache.CoroutineCache:39] - Daemon thread for scanning cache starts...
2017-12-23 19:13:43.656 INFO [Coroutine-192.168.1.3-thread-1][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=1, categoryName=PayRoute, ruleName=Rule, chainName=chain1-2, class=com.nepxion.coroutine.test.service.impl.AServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=3 ms, id=6fcb1456-fc34-49a0-9ad4-832fd5f3f375
2017-12-23 19:13:43.656 INFO [Coroutine-192.168.1.3-thread-0][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=1, categoryName=PayRoute, ruleName=Rule, chainName=chain1-1, class=com.nepxion.coroutine.test.service.impl.AServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=3 ms, id=98c84565-ca73-42b8-a2e8-da4dee6df22a
2017-12-23 19:13:43.660 INFO [Coroutine-192.168.1.3-thread-1][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=4, categoryName=PayRoute, ruleName=Rule, chainName=chain1-2, class=com.nepxion.coroutine.test.service.impl.BServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=6fcb1456-fc34-49a0-9ad4-832fd5f3f375
2017-12-23 19:13:43.660 INFO [Coroutine-192.168.1.3-thread-0][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=4, categoryName=PayRoute, ruleName=Rule, chainName=chain1-1, class=com.nepxion.coroutine.test.service.impl.BServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=98c84565-ca73-42b8-a2e8-da4dee6df22a
2017-12-23 19:13:43.666 INFO [Coroutine-192.168.1.3-thread-1][com.nepxion.coroutine.common.thread.ThreadPoolFactory:38] - Thread pool executor is created, threadName=Promise-192.168.1.3-thread, corePoolSize=64, maximumPoolSize=128, keepAliveTime=900000, allowCoreThreadTimeOut=false
2017-12-23 19:13:43.669 INFO [Promise-192.168.1.3-thread-1][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Parallel completed, referenceType=componentReference, index=8, categoryName=PayRoute, ruleName=Rule, chainName=chain1-2, class=com.nepxion.coroutine.test.service.impl.CServiceImpl, method=doWhen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=6fcb1456-fc34-49a0-9ad4-832fd5f3f375
2017-12-23 19:13:43.669 INFO [Promise-192.168.1.3-thread-2][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Parallel completed, referenceType=componentReference, index=11, categoryName=PayRoute, ruleName=Rule, chainName=chain1-2, class=com.nepxion.coroutine.test.service.impl.DServiceImpl, method=doWhen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=6fcb1456-fc34-49a0-9ad4-832fd5f3f375
2017-12-23 19:13:43.669 INFO [Promise-192.168.1.3-thread-3][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Parallel completed, referenceType=componentReference, index=11, categoryName=PayRoute, ruleName=Rule, chainName=chain1-1, class=com.nepxion.coroutine.test.service.impl.DServiceImpl, method=doWhen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=98c84565-ca73-42b8-a2e8-da4dee6df22a
2017-12-23 19:13:43.669 INFO [Promise-192.168.1.3-thread-0][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Parallel completed, referenceType=componentReference, index=8, categoryName=PayRoute, ruleName=Rule, chainName=chain1-1, class=com.nepxion.coroutine.test.service.impl.CServiceImpl, method=doWhen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=98c84565-ca73-42b8-a2e8-da4dee6df22a
2017-12-23 19:13:43.675 INFO [Coroutine-192.168.1.3-thread-1][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=12, categoryName=PayRoute, ruleName=Rule, chainName=chain1-2, class=com.nepxion.coroutine.test.service.impl.DServiceImpl, method=doMerge, parameterTypes=com.nepxion.coroutine.data.entity.CoroutineList, returnType=java.lang.String, spentTime=0 ms, id=6fcb1456-fc34-49a0-9ad4-832fd5f3f375
2017-12-23 19:13:43.675 INFO [Coroutine-192.168.1.3-thread-0][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=12, categoryName=PayRoute, ruleName=Rule, chainName=chain1-1, class=com.nepxion.coroutine.test.service.impl.DServiceImpl, method=doMerge, parameterTypes=com.nepxion.coroutine.data.entity.CoroutineList, returnType=java.lang.String, spentTime=0 ms, id=98c84565-ca73-42b8-a2e8-da4dee6df22a
2017-12-23 19:13:43.675 INFO [Coroutine-192.168.1.3-thread-1][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=1, categoryName=PayRoute, ruleName=Rule, chainName=chain1-2, class=com.nepxion.coroutine.test.service.impl.AServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=6fcb1456-fc34-49a0-9ad4-832fd5f3f375
2017-12-23 19:13:43.675 INFO [Coroutine-192.168.1.3-thread-0][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=1, categoryName=PayRoute, ruleName=Rule, chainName=chain1-1, class=com.nepxion.coroutine.test.service.impl.AServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=98c84565-ca73-42b8-a2e8-da4dee6df22a
2017-12-23 19:13:43.677 INFO [Coroutine-192.168.1.3-thread-2][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=1, categoryName=PayRoute, ruleName=SubRule, chainName=chain2-2, class=com.nepxion.coroutine.test.service.impl.AServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=6fcb1456-fc34-49a0-9ad4-832fd5f3f375
2017-12-23 19:13:43.677 INFO [Coroutine-192.168.1.3-thread-3][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=1, categoryName=PayRoute, ruleName=SubRule, chainName=chain2-2, class=com.nepxion.coroutine.test.service.impl.AServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=98c84565-ca73-42b8-a2e8-da4dee6df22a
2017-12-23 19:13:43.678 INFO [Coroutine-192.168.1.3-thread-2][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=4, categoryName=PayRoute, ruleName=SubRule, chainName=chain2-2, class=com.nepxion.coroutine.test.service.impl.BServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=6fcb1456-fc34-49a0-9ad4-832fd5f3f375
2017-12-23 19:13:43.678 INFO [Coroutine-192.168.1.3-thread-3][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=4, categoryName=PayRoute, ruleName=SubRule, chainName=chain2-2, class=com.nepxion.coroutine.test.service.impl.BServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=98c84565-ca73-42b8-a2e8-da4dee6df22a
2017-12-23 19:13:43.678 INFO [Coroutine-192.168.1.3-thread-2][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=7, categoryName=PayRoute, ruleName=SubRule, chainName=chain2-2, class=com.nepxion.coroutine.test.service.impl.CServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=6fcb1456-fc34-49a0-9ad4-832fd5f3f375
2017-12-23 19:13:43.678 INFO [Coroutine-192.168.1.3-thread-3][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=7, categoryName=PayRoute, ruleName=SubRule, chainName=chain2-2, class=com.nepxion.coroutine.test.service.impl.CServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=0 ms, id=98c84565-ca73-42b8-a2e8-da4dee6df22a
2017-12-23 19:13:43.679 INFO [Coroutine-192.168.1.3-thread-3][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=10, categoryName=PayRoute, ruleName=SubRule, chainName=chain2-2, class=com.nepxion.coroutine.test.service.impl.DServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=1 ms, id=98c84565-ca73-42b8-a2e8-da4dee6df22a
2017-12-23 19:13:43.679 INFO [Coroutine-192.168.1.3-thread-2][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=10, categoryName=PayRoute, ruleName=SubRule, chainName=chain2-2, class=com.nepxion.coroutine.test.service.impl.DServiceImpl, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=1 ms, id=6fcb1456-fc34-49a0-9ad4-832fd5f3f375
2017-12-23 19:13:43.680 INFO [Coroutine-192.168.1.3-thread-1][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:61] - Serial completed, referenceType=dependencyReference, index=13, categoryName=PayRoute, ruleName=SubRule, chainName=chain1-2, returnType=com.nepxion.coroutine.data.entity.CoroutineResult, spentTime=4 ms, id=6fcb1456-fc34-49a0-9ad4-832fd5f3f375
2017-12-23 19:13:43.680 INFO [Coroutine-192.168.1.3-thread-0][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:61] - Serial completed, referenceType=dependencyReference, index=13, categoryName=PayRoute, ruleName=SubRule, chainName=chain1-1, returnType=com.nepxion.coroutine.data.entity.CoroutineResult, spentTime=4 ms, id=98c84565-ca73-42b8-a2e8-da4dee6df22a
2017-12-23 19:13:43.692 INFO [Coroutine-192.168.1.3-thread-0][com.nepxion.coroutine.test.CoroutineTest$1$1:80] - 异步回调结果: 线程序号=0, id=com.nepxion.coroutine.data.entity.CoroutineId@3ee322f7[
  id=98c84565-ca73-42b8-a2e8-da4dee6df22a
  categoryName=PayRoute
  ruleName=Rule
], result=com.nepxion.coroutine.data.entity.CoroutineResult@719c38e3[
  id=com.nepxion.coroutine.data.entity.CoroutineId@14874a4f[
  id=98c84565-ca73-42b8-a2e8-da4dee6df22a
  categoryName=PayRoute
  ruleName=SubRule
]
  timestamp=0
  result=(Start[0] -> A[0] -> B[0] -> C[0] -> D[0] , Start[0] -> A[0] -> B[0] -> D[0] -> D[0]) -> A[0]) -> A[0]) -> B[0]) -> C[0]) -> D[0])
  exception=<null>
]
2017-12-23 19:13:43.692 INFO [Timer-1][com.nepxion.coroutine.test.CoroutineTest$2:103] - 同步调用结果: 线程序号=0, id=com.nepxion.coroutine.data.entity.CoroutineId@320ca662[
  id=6fcb1456-fc34-49a0-9ad4-832fd5f3f375
  categoryName=PayRoute
  ruleName=Rule
], result=com.nepxion.coroutine.data.entity.CoroutineResult@475af95a[
  id=com.nepxion.coroutine.data.entity.CoroutineId@3ce1ece3[
  id=6fcb1456-fc34-49a0-9ad4-832fd5f3f375
  categoryName=PayRoute
  ruleName=SubRule
]
  timestamp=0
  result=(Start[0] -> A[0] -> B[0] -> C[0] -> D[0] , Start[0] -> A[0] -> B[0] -> D[0] -> D[0]) -> A[0]) -> A[0]) -> B[0]) -> C[0]) -> D[0])
  exception=<null>
]
```

### 分布式调用方式

基于Spring Boot在Dubbo和Thunder框架的协程调用，分布式API的聚合

定义规则
```java
<?xml version="1.0" encoding="UTF-8"?>
<coroutine>
    <rule version="1">
        <component applicationContext="classpath*:dubbo-client-context-coroutine.xml">
            <class id="aService">
                <method index="1" method="doThen"/>
                <method index="2" method="doWhen"/>
                <method index="3" method="doMerge"/>
            </class>
            <class id="bService">
                <method index="4" method="doThen"/>
                <method index="5" method="doWhen"/>
                <method index="6" method="doMerge"/>
            </class>
        </component>
        <component applicationContext="classpath*:thunder-client-context-coroutine.xml">
            <class id="cService">
                <method index="7" method="doThen"/>
                <method index="8" method="doWhen"/>
                <method index="9" method="doMerge"/>
            </class>
            <class id="dService">
                <method index="10" method="doThen"/>
                <method index="11" method="doWhen"/>
                <method index="12" method="doMerge"/>
            </class>
        </component>

        <chain>
            <when index="2,5"/>
            <then index="9,10"/>
            <when index="8,11"/>
            <then index="3,4"/>
        </chain>
    </rule>
</coroutine>
```

调用入口

运行coroutine-spring-boot-dubbo-server-example下的DubboServerApplication.java

运行coroutine-spring-boot-thunder-server-example下的ThunderServerApplication.java

运行coroutine-spring-boot-client-example下的CoroutineClientApplication.java
```java
package com.nepxion.coroutine;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.nepxion.coroutine.common.callback.CoroutineCallback;
import com.nepxion.coroutine.data.entity.CoroutineResult;
import com.nepxion.coroutine.framework.core.CoroutineManager;

@SpringBootApplication
@ComponentScan(basePackages = { "com.nepxion.coroutine" })
public class CoroutineClientApplication {
    private static final Logger LOG = LoggerFactory.getLogger(CoroutineClientApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CoroutineClientApplication.class, args);

        // invokeRemote();
        invokeLocal();
    }

    public static void invokeRemote() throws Exception {
        // 请确保Zookeeper有对应的规则（运行CoroutineRuleRegistry相关方法）

        // 从远程注册中心装载
        // 启动和远程注册中心连接
        CoroutineManager.start();

        // 解析远端规则（支持子规则引用）
        CoroutineManager.parseRemote("Distribution PayRoute", "Distribution Rule");

        invokeAsync();
        invokeSync();
    }

    public static void invokeLocal() throws Exception {
        // 从本地装载
        // 解析本地规则（不支持子规则引用）
        CoroutineManager.parseLocal("Distribution PayRoute", "Distribution Rule", "rule.xml");

        invokeAsync();
        invokeSync();
    }

    public static void invokeAsync() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 1; i++) {
                    final int index = i % 5;
                    CoroutineManager.load().startAsync("Distribution PayRoute", "Distribution Rule", null, new String[] { "Start[" + index + "]" }, false, new CoroutineCallback<CoroutineResult<Object>>() {
                        @Override
                        public void onResult(CoroutineResult<Object> result) {
                            LOG.info("异步回调结果: 线程序号={}, id={}, result={}", index, result.getId(), result.getResult());
                        }

                        @Override
                        public void onError(Exception exception) {
                            LOG.error("异步回调异常", exception);
                        }
                    });
                }
                LOG.info("------------------------------------------------------------");
            }
        }, 0, 20000);
    }

    public static void invokeSync() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 1; i++) {
                    final int index = i % 5;

                    try {
                        CoroutineResult<Object> result = CoroutineManager.load().startSync("Distribution PayRoute", "Distribution Rule", null, new String[] { "Start[" + index + "]" }, 3000, false);
                        LOG.info("同步调用结果: 线程序号={}, id={}, result={}", index, result.getId(), result.getResult());
                    } catch (Exception e) {
                        LOG.error("同步调用异常", e);
                    }
                }
                LOG.info("------------------------------------------------------------");
            }
        }, 0, 20000);
    }

    @Bean
    public EmbeddedServletContainerFactory createEmbeddedServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory();
        tomcatFactory.setPort(9081);

        return tomcatFactory;
    }
}
```

运行结果
```java
2017-12-23 19:20:28.905 INFO [Coroutine-192.168.1.3-thread-1][com.nepxion.coroutine.common.thread.ThreadPoolFactory:38] - Thread pool executor is created, threadName=Promise-192.168.1.3-thread, corePoolSize=64, maximumPoolSize=128, keepAliveTime=900000, allowCoreThreadTimeOut=false
2017-12-23 19:20:29.052 INFO [Promise-192.168.1.3-thread-2][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Parallel completed, referenceType=componentReference, index=5, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=bService, method=doWhen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=136 ms, id=956ab727-0e18-48aa-8334-1453c139fa23
2017-12-23 19:20:29.052 INFO [Promise-192.168.1.3-thread-3][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Parallel completed, referenceType=componentReference, index=5, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=bService, method=doWhen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=136 ms, id=b22a77b6-0776-49a1-a6bb-e15268ffd0c1
2017-12-23 19:20:29.052 INFO [Promise-192.168.1.3-thread-0][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Parallel completed, referenceType=componentReference, index=2, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=aService, method=doWhen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=136 ms, id=956ab727-0e18-48aa-8334-1453c139fa23
2017-12-23 19:20:29.052 INFO [Promise-192.168.1.3-thread-1][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Parallel completed, referenceType=componentReference, index=2, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=aService, method=doWhen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=136 ms, id=b22a77b6-0776-49a1-a6bb-e15268ffd0c1
2017-12-23 19:20:29.199 INFO [ClientAffinityThreadFactory][com.nepxion.thunder.common.thread.ThreadPoolFactory:106] - Thread pool executor is created, threadName=Thunder-reference-192.168.1.3:6010-thread, corePoolSize=32, maximumPoolSize=64, keepAliveTime=900000, allowCoreThreadTimeOut=false
2017-12-23 19:20:29.200 INFO [Coroutine-192.168.1.3-thread-0][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=9, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=cService, method=doMerge, parameterTypes=java.util.List, returnType=java.lang.String, spentTime=143 ms, id=956ab727-0e18-48aa-8334-1453c139fa23
2017-12-23 19:20:29.201 INFO [Coroutine-192.168.1.3-thread-1][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=9, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=cService, method=doMerge, parameterTypes=java.util.List, returnType=java.lang.String, spentTime=143 ms, id=b22a77b6-0776-49a1-a6bb-e15268ffd0c1
2017-12-23 19:20:29.206 INFO [Coroutine-192.168.1.3-thread-1][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=10, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=dService, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=3 ms, id=b22a77b6-0776-49a1-a6bb-e15268ffd0c1
2017-12-23 19:20:29.206 INFO [Coroutine-192.168.1.3-thread-0][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=10, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=dService, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=3 ms, id=956ab727-0e18-48aa-8334-1453c139fa23
2017-12-23 19:20:29.213 INFO [Promise-192.168.1.3-thread-4][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Parallel completed, referenceType=componentReference, index=8, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=cService, method=doWhen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=5 ms, id=b22a77b6-0776-49a1-a6bb-e15268ffd0c1
2017-12-23 19:20:29.214 INFO [Promise-192.168.1.3-thread-5][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Parallel completed, referenceType=componentReference, index=8, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=cService, method=doWhen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=6 ms, id=956ab727-0e18-48aa-8334-1453c139fa23
2017-12-23 19:20:29.214 INFO [Promise-192.168.1.3-thread-7][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Parallel completed, referenceType=componentReference, index=11, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=dService, method=doWhen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=6 ms, id=b22a77b6-0776-49a1-a6bb-e15268ffd0c1
2017-12-23 19:20:29.215 INFO [Promise-192.168.1.3-thread-6][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Parallel completed, referenceType=componentReference, index=11, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=dService, method=doWhen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=7 ms, id=956ab727-0e18-48aa-8334-1453c139fa23
2017-12-23 19:20:29.220 INFO [Coroutine-192.168.1.3-thread-1][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=3, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=aService, method=doMerge, parameterTypes=java.util.List, returnType=java.lang.String, spentTime=5 ms, id=b22a77b6-0776-49a1-a6bb-e15268ffd0c1
2017-12-23 19:20:29.220 INFO [Coroutine-192.168.1.3-thread-0][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=3, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=aService, method=doMerge, parameterTypes=java.util.List, returnType=java.lang.String, spentTime=5 ms, id=956ab727-0e18-48aa-8334-1453c139fa23
2017-12-23 19:20:29.222 INFO [Coroutine-192.168.1.3-thread-1][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=4, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=bService, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=2 ms, id=b22a77b6-0776-49a1-a6bb-e15268ffd0c1
2017-12-23 19:20:29.222 INFO [Coroutine-192.168.1.3-thread-0][com.nepxion.coroutine.monitor.log.LogMonitorLauncher:55] - Serial completed, referenceType=componentReference, index=4, categoryName=Distribution PayRoute, ruleName=Distribution Rule, chainName=null, classId=bService, method=doThen, parameterTypes=java.lang.String, returnType=java.lang.String, spentTime=2 ms, id=956ab727-0e18-48aa-8334-1453c139fa23
2017-12-23 19:20:29.223 INFO [Timer-1][com.nepxion.coroutine.CoroutineClientApplication$2:96] - 同步调用结果: 线程序号=0, id=com.nepxion.coroutine.data.entity.CoroutineId@2295f70c[
  id=956ab727-0e18-48aa-8334-1453c139fa23
  categoryName=Distribution PayRoute
  ruleName=Distribution Rule
], result=((Start[0] -> A[0] -> C[0] , Start[0] -> B[0] -> C[0]) -> D[0]) -> C[0]) -> A[0]) , (Start[0] -> A[0] -> C[0] , Start[0] -> B[0] -> C[0]) -> D[0]) -> D[0]) -> A[0])) -> B[0]))
2017-12-23 19:20:29.223 INFO [Coroutine-192.168.1.3-thread-1][com.nepxion.coroutine.CoroutineClientApplication$1$1:73] - 异步回调结果: 线程序号=0, id=com.nepxion.coroutine.data.entity.CoroutineId@51baebd3[
  id=b22a77b6-0776-49a1-a6bb-e15268ffd0c1
  categoryName=Distribution PayRoute
  ruleName=Distribution Rule
], result=((Start[0] -> A[0] -> C[0] , Start[0] -> B[0] -> C[0]) -> D[0]) -> C[0]) -> A[0]) , (Start[0] -> A[0] -> C[0] , Start[0] -> B[0] -> C[0]) -> D[0]) -> D[0]) -> A[0])) -> B[0]))
```