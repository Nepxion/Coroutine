package com.nepxion.coroutine.common.delegate;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.coroutine.common.property.CoroutineProperties;

public interface CoroutineDelegate {
    // 获取属性句柄容器
    CoroutineProperties getProperties();

    void setProperties(CoroutineProperties properties);

    // 反射创建Delegate类
    <T> T createDelegate(String delegateClassId) throws Exception;
}