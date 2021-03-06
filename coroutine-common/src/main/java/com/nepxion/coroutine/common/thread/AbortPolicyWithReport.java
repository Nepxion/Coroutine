package com.nepxion.coroutine.common.thread;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 任务饱和时, 抛弃任务，抛出异常
public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {
    private static final Logger LOG = LoggerFactory.getLogger(AbortPolicyWithReport.class);

    private String threadName;

    public AbortPolicyWithReport() {
        this(null);
    }

    public AbortPolicyWithReport(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        if (threadName != null) {
            LOG.error("Thread pool [{}] is exhausted, executor={}", threadName, executor.toString());
        }

        super.rejectedExecution(runnable, executor);
    }
}