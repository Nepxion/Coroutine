package com.nepxion.coroutine.test;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.data.entity.CoroutineResult;
import com.nepxion.coroutine.framework.core.CoroutineManager;

public class CoroutinePerformanceTest {
    private static final Logger LOG = LoggerFactory.getLogger(CoroutinePerformanceTest.class);

    static {
        try {
            CoroutineManager.parseLocal("PayRoute", "Rule", "rule.xml");
        } catch (Exception e) {
            LOG.error("解析规则文件异常", e);
        }
    }
    
    public static void main(String[] args) {
        try {
            CoroutineResult<Object> result = CoroutineManager.load().startSync("PayRoute", "Rule", null, new String[] { "Start" }, 30000, false);
            LOG.info("同步调用结果: id={}, result={}", result.getId(), result.getResult());
        } catch (Exception e) {
            LOG.error("同步调用异常", e);
        }
    }
}