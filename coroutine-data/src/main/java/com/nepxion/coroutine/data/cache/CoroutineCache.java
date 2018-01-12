package com.nepxion.coroutine.data.cache;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.nepxion.coroutine.common.constant.CoroutineConstant;
import com.nepxion.coroutine.common.property.CoroutineProperties;
import com.nepxion.coroutine.common.property.CoroutinePropertiesManager;
import com.nepxion.coroutine.data.entity.CoroutineId;
import com.nepxion.coroutine.data.entity.CoroutineResult;
import com.nepxion.coroutine.data.scan.CoroutineScan;

// 缓存串行/并行，或者并行/串行切换时候的边界执行结果
public class CoroutineCache {
    private static final Logger LOG = LoggerFactory.getLogger(CoroutineCache.class);
    private static final Map<CoroutineId, CoroutineResult<?>> RESULT_MAP = Maps.newConcurrentMap();

    static {
        CoroutineProperties properties = CoroutinePropertiesManager.getProperties();
        long scan = properties.getLong(CoroutineConstant.ASYNC_SCAN_ATTRIBUTE_NAME);

        Thread scanThread = new Thread(new CoroutineScan(RESULT_MAP, scan), "Scan Cache Thread");
        scanThread.setDaemon(true);
        scanThread.start();

        LOG.info("Daemon thread for scanning cache starts...");
    }

    public static CoroutineResult<?> getResult(CoroutineId id) {
        return RESULT_MAP.remove(id);
    }

    public static void setResult(CoroutineResult<?> result) {
        if (hasException(result.getId())) {
            return;
        }

        result.setTimestamp(System.currentTimeMillis());

        RESULT_MAP.put(result.getId(), result);
    }

    public static boolean hasException(CoroutineId id) {
        CoroutineResult<?> result = RESULT_MAP.get(id);

        return hasException(result);
    }

    public static boolean hasException(CoroutineResult<?> result) {
        return result != null && result.getException() != null;
    }
}