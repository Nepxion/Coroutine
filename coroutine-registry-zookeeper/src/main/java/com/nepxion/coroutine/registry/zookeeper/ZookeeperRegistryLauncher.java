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

import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.delegate.CoroutineDelegateImpl;
import com.nepxion.coroutine.registry.RegistryEntity;
import com.nepxion.coroutine.registry.RegistryExecutor;
import com.nepxion.coroutine.registry.RegistryInitializer;
import com.nepxion.coroutine.registry.RegistryLauncher;

public class ZookeeperRegistryLauncher extends CoroutineDelegateImpl implements RegistryLauncher {
    private RegistryEntity registryEntity = new RegistryEntity();
    private RegistryInitializer registryInitializer = new ZookeeperRegistryInitializer();
    private RegistryExecutor registryExecutor = new ZookeeperRegistryExecutor();

    @Override
    public void start() throws Exception {
        String address = properties.getString(CoroutineConstants.ZOOKEEPER_ADDRESS_ATTRIBUTE_NAME);

        start(address);
    }

    @Override
    public void start(String address) throws Exception {
        registryEntity.setAddress(address);

        registryInitializer.start(registryEntity, properties);

        registryExecutor.setRegistryInitializer(registryInitializer);
        registryExecutor.setProperties(properties);
    }

    @Override
    public void stop() throws Exception {
        registryInitializer.stop();
    }

    @Override
    public boolean enabled() {
        return registryInitializer.enabled();
    }

    @Override
    public RegistryExecutor getRegistryExecutor() {
        return registryExecutor;
    }
}