package com.nepxion.coroutine.testcase.framework;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Timer;
import java.util.TimerTask;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.callback.CoroutineCallback;
import com.nepxion.coroutine.data.entity.CoroutineResult;
import com.nepxion.coroutine.framework.core.CoroutineManager;

public class CoroutineDistributionTest {
    private static final Logger LOG = LoggerFactory.getLogger(CoroutineDistributionTest.class);

    @Before
    public void before() throws Exception {
        // 1. 从远程注册中心装载
        // 启动和远程注册中心连接
        // CoroutineManager.start();
        // 解析远端规则
        // CoroutineManager.parseRemote("Distribution PayRoute", "Distribution Rule");

        // 2. 从本地装载
        // 解析本地规则，不支持子规则引用
        CoroutineManager.parseLocal("Distribution PayRoute", "Distribution Rule", "rule3.xml");
    }

    @Test
    public void testAsync() throws Exception {
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

        System.in.read();
    }

    @Test
    public void testSync() throws Exception {
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

        System.in.read();
    }
}