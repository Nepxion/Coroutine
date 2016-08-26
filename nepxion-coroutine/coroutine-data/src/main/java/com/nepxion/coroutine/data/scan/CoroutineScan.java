package com.nepxion.coroutine.data.scan;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.data.entity.CoroutineId;
import com.nepxion.coroutine.data.entity.CoroutineResult;

// 过期缓存的监控
public class CoroutineScan implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(CoroutineScan.class);

    private Map<CoroutineId, CoroutineResult<?>> resultMap;
    private long scan;

    public CoroutineScan(Map<CoroutineId, CoroutineResult<?>> resultMap, long scan) {
        this.resultMap = resultMap;
        this.scan = scan;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // LOG.info("Daemon thread starts to clean expired cache...");
                if (MapUtils.isNotEmpty(resultMap)) {
                    Iterator<Map.Entry<CoroutineId, CoroutineResult<?>>> iterator = resultMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<CoroutineId, CoroutineResult<?>> entry = iterator.next();
                        CoroutineResult<?> result = entry.getValue();
                        if (System.currentTimeMillis() - result.getTimestamp() > scan) {
                            iterator.remove();
                        }
                    }
                }

                TimeUnit.MILLISECONDS.sleep(scan);
            } catch (Exception e) {
                LOG.error("Scan cache failed", e);
            }
        }
    }
}