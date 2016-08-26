package com.nepxion.coroutine.registry;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.coroutine.common.delegate.CoroutineDelegate;

public interface RegistryLauncher extends CoroutineDelegate {

    // 启动注册中心连接
    void start() throws Exception;

    // 启动注册中心连接
    void start(String address) throws Exception;

    // 停止注册中心连接
    void stop() throws Exception;

    // 获取注册中心执行器
    RegistryExecutor getRegistryExecutor();
}