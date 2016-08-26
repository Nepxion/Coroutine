package com.nepxion.coroutine.common.thread;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.property.CoroutineProperties;
import com.nepxion.coroutine.common.property.CoroutinePropertiesManager;

public class ThreadPoolFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ThreadPoolFactory.class);
    
    public static ThreadPoolExecutor createThreadPoolExecutor(final String threadName, final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final boolean allowCoreThreadTimeOut) {
        LOG.info("Thread pool executor is created, threadName={}, corePoolSize={}, maximumPoolSize={}, keepAliveTime={}, allowCoreThreadTimeOut={}", threadName, corePoolSize, maximumPoolSize, keepAliveTime, allowCoreThreadTimeOut);
        
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                createBlockingQueue(),
                new ThreadFactory() {
                    private AtomicInteger number = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable runnable) {
                        return new Thread(runnable, threadName + "-" + number.getAndIncrement());
                    }
                },
                createRejectedPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(allowCoreThreadTimeOut);

        return threadPoolExecutor;
    }

    public static ThreadPoolExecutor createThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, boolean allowCoreThreadTimeOut) {
        LOG.info("Thread pool executor is created, corePoolSize={}, maximumPoolSize={}, keepAliveTime={}, allowCoreThreadTimeOut={}", corePoolSize, maximumPoolSize, keepAliveTime, allowCoreThreadTimeOut);
        
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                createBlockingQueue(),
                createRejectedPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(allowCoreThreadTimeOut);

        return threadPoolExecutor;
    }

    private static BlockingQueue<Runnable> createBlockingQueue() {
        CoroutineProperties properties = CoroutinePropertiesManager.getProperties();
        String queue = properties.getString(CoroutineConstants.THREAD_POOL_QUEUE_ATTRIBUTE_NAME);
        ThreadQueueType queueType = ThreadQueueType.fromString(queue);

        int queueCapacity = CoroutineConstants.CPUS * properties.getInteger(CoroutineConstants.THREAD_POOL_QUEUE_CAPACITY_ATTRIBUTE_NAME);

        switch (queueType) {
            case LINKED_BLOCKING_QUEUE:
                return new LinkedBlockingQueue<Runnable>(queueCapacity);
            case ARRAY_BLOCKING_QUEUE:
                return new ArrayBlockingQueue<Runnable>(queueCapacity);
            case SYNCHRONOUS_QUEUE:
                return new SynchronousQueue<Runnable>();
        }

        return null;
    }

    private static RejectedExecutionHandler createRejectedPolicy() {
        CoroutineProperties properties = CoroutinePropertiesManager.getProperties();
        String rejectedPolicy = properties.getString(CoroutineConstants.THREAD_POOL_REJECTED_POLICY_ATTRIBUTE_NAME);
        ThreadRejectedPolicyType rejectedPolicyType = ThreadRejectedPolicyType.fromString(rejectedPolicy);

        switch (rejectedPolicyType) {
            case BLOCKING_POLICY_WITH_REPORT:
                return new BlockingPolicyWithReport();
            case CALLER_RUNS_POLICY_WITH_REPORT:
                return new CallerRunsPolicyWithReport();
            case ABORT_POLICY_WITH_REPORT:
                return new AbortPolicyWithReport();
            case REJECTED_POLICY_WITH_REPORT:
                return new RejectedPolicyWithReport();
            case DISCARDED_POLICY_WITH_REPORT:
                return new DiscardedPolicyWithReport();
        }

        return null;
    }
}