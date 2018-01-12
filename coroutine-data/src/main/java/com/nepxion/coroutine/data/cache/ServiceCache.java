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

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.nepxion.coroutine.data.entity.ServiceId;

// 缓存业务端缓存
public class ServiceCache {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceCache.class);
    private static final Map<ServiceId, Object> RESULT_MAP = Maps.newConcurrentMap();

    @SuppressWarnings("unchecked")
    public static <T> T get(ServiceId serviceId) {
        return (T) RESULT_MAP.get(serviceId);
    }

    public static void put(ServiceId serviceId, Object result) {
        RESULT_MAP.put(serviceId, result);
    }

    public static void clear(String id) {
        if (MapUtils.isNotEmpty(RESULT_MAP)) {
            Iterator<Map.Entry<ServiceId, Object>> iterator = RESULT_MAP.entrySet().iterator();
            int count = 0;
            while (iterator.hasNext()) {
                Map.Entry<ServiceId, Object> entry = iterator.next();
                ServiceId key = entry.getKey();
                if (StringUtils.equals(key.getId(), id)) {
                    iterator.remove();
                    count++;
                }
            }

            LOG.info("Service cache for id={} is cleared, count={}", id, count);
        }
    }
}