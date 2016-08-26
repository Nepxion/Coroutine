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

import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.delegate.CoroutineDelegateImpl;
import com.nepxion.coroutine.registry.RegistryEntity;
import com.nepxion.coroutine.registry.RegistryExecutor;
import com.nepxion.coroutine.registry.RegistryInitializer;
import com.nepxion.coroutine.registry.RegistryLauncher;

public class ZookeeperRegistryLauncher extends CoroutineDelegateImpl implements RegistryLauncher {
    private RegistryInitializer registryInitializer;
    private RegistryExecutor registryExecutor;

    @Override
    public void start() throws Exception {
        String address = properties.get(CoroutineConstants.ZOOKEEPER_ADDRESS_ATTRIBUTE_NAME);

        start(address);
    }

    @Override
    public void start(String address) throws Exception {
        RegistryEntity registryEntity = new RegistryEntity();
        registryEntity.setAddress(address);

        // 启动Zookeeper连接
        registryInitializer = new ZookeeperRegistryInitializer();
        registryInitializer.start(registryEntity, properties);

        registryExecutor = new ZookeeperRegistryExecutor();
        registryExecutor.setRegistryInitializer(registryInitializer);
        registryExecutor.setProperties(properties);
    }

    @Override
    public void stop() throws Exception {
        // 停止Zookeeper连接
        registryInitializer.stop();
    }

    @Override
    public RegistryExecutor getRegistryExecutor() {
        return registryExecutor;
    }
}