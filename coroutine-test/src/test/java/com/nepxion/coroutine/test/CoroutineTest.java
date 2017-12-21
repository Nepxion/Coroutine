package com.nepxion.coroutine.test;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
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