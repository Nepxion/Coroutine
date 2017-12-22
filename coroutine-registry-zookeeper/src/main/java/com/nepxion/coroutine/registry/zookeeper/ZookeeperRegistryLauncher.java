package com.nepxion.coroutine.registry.zookeeper;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.delegate.CoroutineDelegateImpl;
import com.nepxion.coroutine.registry.RegistryEntity;
import com.nepxion.coroutine.registry.RegistryExecutor;
import com.nepxion.coroutine.registry.RegistryInitializer;
import com.nepxion.coroutine.registry.RegistryLauncher;
import com.nepxion.coroutine.registry.zookeeper.common.ZookeeperException;

public class ZookeeperRegistryLauncher extends CoroutineDelegateImpl implements RegistryLauncher {
    private RegistryInitializer registryInitializer;
    private RegistryExecutor registryExecutor;

    private static final Lock lock = new ReentrantLock();

    @Override
    public void start() throws Exception {
        String address = properties.getString(CoroutineConstants.ZOOKEEPER_ADDRESS_ATTRIBUTE_NAME);

        start(address);
    }

    @Override
    public void start(String address) throws Exception {
        try {
            lock.lock();

            RegistryEntity registryEntity = new RegistryEntity();
            registryEntity.setAddress(address);

            if (registryInitializer != null) {
                throw new ZookeeperException("Registry initializer isn't null, it has been initialized already");
            }

            // 启动Zookeeper连接
            registryInitializer = new ZookeeperRegistryInitializer();
            registryInitializer.start(registryEntity, properties);

            registryExecutor = new ZookeeperRegistryExecutor();
            registryExecutor.setRegistryInitializer(registryInitializer);
            registryExecutor.setProperties(properties);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void stop() throws Exception {
        try {
            lock.lock();

            if (registryInitializer == null) {
                throw new ZookeeperException("Registry initializer is null");
            }

            // 停止Zookeeper连接
            registryInitializer.stop();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public RegistryExecutor getRegistryExecutor() {
        return registryExecutor;
    }
}