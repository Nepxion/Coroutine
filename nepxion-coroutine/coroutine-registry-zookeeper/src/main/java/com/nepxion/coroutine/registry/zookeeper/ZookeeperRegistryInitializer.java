package com.nepxion.coroutine.registry.zookeeper;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.curator.framework.CuratorFramework;

import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.delegate.CoroutineDelegateImpl;
import com.nepxion.coroutine.common.property.CoroutineProperties;
import com.nepxion.coroutine.registry.RegistryEntity;
import com.nepxion.coroutine.registry.RegistryInitializer;
import com.nepxion.coroutine.registry.zookeeper.common.ZookeeperException;
import com.nepxion.coroutine.registry.zookeeper.common.ZookeeperInvoker;

public class ZookeeperRegistryInitializer extends CoroutineDelegateImpl implements RegistryInitializer {
    private ZookeeperInvoker invoker = new ZookeeperInvoker();
    private CuratorFramework client;

    @Override
    public void start(RegistryEntity registryEntity) throws Exception {
        if (client != null) {
            throw new ZookeeperException("Zookeeper has started");
        }

        if (properties == null) {
            throw new ZookeeperException("properties is null");
        }

        String address = registryEntity.getAddress();
        int sessionTimeout = properties.getInteger(CoroutineConstants.ZOOKEEPER_SESSION_TIMOUT_ATTRIBUTE_NAME);
        int connectTimeout = properties.getInteger(CoroutineConstants.ZOOKEEPER_CONNECT_TIMEOUT_ATTRIBUTE_NAME);
        int connectWaitTime = properties.getInteger(CoroutineConstants.ZOOKEEPER_CONNECT_WAIT_TIME_ATTRIBUTE_NAME);
        client = invoker.create(address, sessionTimeout, connectTimeout, connectWaitTime);
        invoker.startAndBlock(client);
    }

    @Override
    public void start(RegistryEntity registryEntity, CoroutineProperties properties) throws Exception {
        if (client != null) {
            throw new ZookeeperException("Zookeeper has started");
        }

        setProperties(properties);

        start(registryEntity);
    }

    @Override
    public void stop() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        invoker.close(client);
    }

    public ZookeeperInvoker getInvoker() {
        return invoker;
    }

    public CuratorFramework getClient() {
        return client;
    }
}