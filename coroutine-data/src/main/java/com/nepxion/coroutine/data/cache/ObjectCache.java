package com.nepxion.coroutine.data.cache;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.ConcurrentMap;

import org.springframework.context.ApplicationContext;

import com.google.common.collect.Maps;

// 缓存外部的对象实例，包括反射对象(本地方式)，Spring ApplicationContext(分布式方式)
public class ObjectCache {
    private static final ConcurrentMap<String, Object> OBJECT_MAP = Maps.newConcurrentMap();
    private static final ConcurrentMap<String, ApplicationContext> APPLICATION_CONTEXT_MAP = Maps.newConcurrentMap();

    public static Object getObject(String key) {
        return OBJECT_MAP.get(key);
    }

    public static void putObject(String key, Object object) {
        if (object != null) {
            OBJECT_MAP.putIfAbsent(key, object);
        }
    }

    public static ApplicationContext getApplicationContext(String key) {
        return APPLICATION_CONTEXT_MAP.get(key);
    }

    public static void putApplicationContext(String key, ApplicationContext applicationContext) {
        if (applicationContext != null) {
            APPLICATION_CONTEXT_MAP.putIfAbsent(key, applicationContext);
        }
    }
}