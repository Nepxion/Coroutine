package com.nepxion.coroutine;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.nepxion.coroutine.common.callback.CoroutineCallback;
import com.nepxion.coroutine.data.entity.CoroutineResult;
import com.nepxion.coroutine.framework.core.CoroutineManager;

@SpringBootApplication
@ComponentScan(basePackages = { "com.nepxion.coroutine.service" })
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