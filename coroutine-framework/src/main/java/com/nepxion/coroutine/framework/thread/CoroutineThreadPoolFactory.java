package com.nepxion.coroutine.framework.thread;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.ThreadPoolExecutor;

import com.nepxion.coroutine.common.constant.CoroutineConstant;
import com.nepxion.coroutine.common.property.CoroutineProperties;
import com.nepxion.coroutine.common.property.CoroutinePropertiesManager;
import com.nepxion.coroutine.common.thread.ThreadPoolFactory;

public class CoroutineThreadPoolFactory {
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = createThreadPoolExecutor();

    private static ThreadPoolExecutor createThreadPoolExecutor() {
        CoroutineProperties properties = CoroutinePropertiesManager.getProperties();
        try {
            return ThreadPoolFactory.createThreadPoolExecutor(CoroutineConstant.COROUTINE_ELEMENT_NAME,
                    CoroutineConstant.CPUS * properties.getInteger(CoroutineConstant.COROUTINE_THREAD_POOL_CORE_POOL_SIZE_ATTRIBUTE_NAME),
                    CoroutineConstant.CPUS * properties.getInteger(CoroutineConstant.COROUTINE_THREAD_POOL_MAXIMUM_POOL_SIZE_ATTRIBUTE_NAME),
                    properties.getLong(CoroutineConstant.COROUTINE_THREAD_POOL_KEEP_ALIVE_TIME_ATTRIBUTE_NAME),
                    properties.getBoolean(CoroutineConstant.COROUTINE_THREAD_POOL_ALLOW_CORE_THREAD_TIMEOUT_ATTRIBUTE_NAME));
        } catch (Exception e) {
            throw new IllegalArgumentException("Properties maybe isn't initialized", e);
        }
    }

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return THREAD_POOL_EXECUTOR;
    }
}