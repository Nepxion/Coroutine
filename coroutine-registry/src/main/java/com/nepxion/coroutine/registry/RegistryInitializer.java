package com.nepxion.coroutine.registry;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.coroutine.common.delegate.CoroutineDelegate;
import com.nepxion.coroutine.common.property.CoroutineProperties;

public interface RegistryInitializer extends CoroutineDelegate {

    // 启动和注册中心的连接
    void start(RegistryEntity registryEntity) throws Exception;

    // 启动和注册中心的连接
    void start(RegistryEntity registryEntity, CoroutineProperties properties) throws Exception;

    // 停止注册中心的连接
    void stop() throws Exception;

    // 注册中心模式激活模式
    boolean enabled();
}