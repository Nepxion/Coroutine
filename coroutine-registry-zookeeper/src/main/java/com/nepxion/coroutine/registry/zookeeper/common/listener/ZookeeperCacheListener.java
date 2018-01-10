package com.nepxion.coroutine.registry.zookeeper.common.listener;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.PathUtils;

import com.nepxion.coroutine.registry.zookeeper.common.ZookeeperException;

public abstract class ZookeeperCacheListener {
    protected CuratorFramework client;
    protected String path;

    public ZookeeperCacheListener(CuratorFramework client, String path) {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        PathUtils.validatePath(path);

        this.client = client;
        this.path = path;
    }

    public CuratorFramework getClient() {
        return client;
    }

    public String getPath() {
        return path;
    }
}