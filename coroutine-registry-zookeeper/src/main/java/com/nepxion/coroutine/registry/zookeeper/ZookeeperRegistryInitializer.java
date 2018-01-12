package com.nepxion.coroutine.registry.zookeeper;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.coroutine.common.constant.CoroutineConstant;
import com.nepxion.coroutine.common.delegate.CoroutineDelegateImpl;
import com.nepxion.coroutine.common.property.CoroutineProperties;
import com.nepxion.coroutine.registry.RegistryEntity;
import com.nepxion.coroutine.registry.RegistryInitializer;
import com.nepxion.coroutine.registry.zookeeper.common.ZookeeperException;
import com.nepxion.coroutine.registry.zookeeper.common.ZookeeperInvoker;

public class ZookeeperRegistryInitializer extends CoroutineDelegateImpl implements RegistryInitializer {
    private ZookeeperInvoker invoker = new ZookeeperInvoker();

    @Override
    public void start(RegistryEntity registryEntity) throws Exception {
        if (properties == null) {
            throw new ZookeeperException("Properties is null");
        }

        String address = registryEntity.getAddress();
        int sessionTimeout = properties.getInteger(CoroutineConstant.ZOOKEEPER_SESSION_TIMOUT_ATTRIBUTE_NAME);
        int connectTimeout = properties.getInteger(CoroutineConstant.ZOOKEEPER_CONNECT_TIMEOUT_ATTRIBUTE_NAME);
        int connectWaitTime = properties.getInteger(CoroutineConstant.ZOOKEEPER_CONNECT_WAIT_TIME_ATTRIBUTE_NAME);

        invoker.create(address, sessionTimeout, connectTimeout, connectWaitTime);
        invoker.startAndBlock();
    }

    @Override
    public void start(RegistryEntity registryEntity, CoroutineProperties properties) throws Exception {
        setProperties(properties);

        start(registryEntity);
    }

    @Override
    public void stop() throws Exception {
        invoker.close();
    }

    @Override
    public boolean enabled() {
        return invoker.isInitialized();
    }

    public ZookeeperInvoker getInvoker() {
        return invoker;
    }
}