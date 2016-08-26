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

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.delegate.CoroutineDelegateImpl;
import com.nepxion.coroutine.registry.RegistryExecutor;
import com.nepxion.coroutine.registry.RegistryInitializer;
import com.nepxion.coroutine.registry.zookeeper.common.ZookeeperException;
import com.nepxion.coroutine.registry.zookeeper.common.ZookeeperInvoker;

public class ZookeeperRegistryExecutor extends CoroutineDelegateImpl implements RegistryExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperRegistryExecutor.class);

    private ZookeeperInvoker invoker;
    private CuratorFramework client;

    private String namespace;

    public ZookeeperRegistryExecutor() {
        this(null);
    }

    public ZookeeperRegistryExecutor(ZookeeperRegistryInitializer initializer) {
        this(initializer, null);
    }

    public ZookeeperRegistryExecutor(ZookeeperRegistryInitializer initializer, String namespace) {
        setRegistryInitializer(initializer);
        setNamespace(namespace);
    }

    @Override
    public void setRegistryInitializer(RegistryInitializer registryInitializer) {
        if (registryInitializer == null) {
            return;
        }

        ZookeeperRegistryInitializer initializer = (ZookeeperRegistryInitializer) registryInitializer;
        this.invoker = initializer.getInvoker();
        this.client = initializer.getClient();
    }

    @Override
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public ZookeeperInvoker getInvoker() {
        return invoker;
    }

    public CuratorFramework getClient() {
        return client;
    }

    @Override
    public void registerRule(String categoryName, String ruleName) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createRulePath(categoryName, ruleName);
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            LOG.info("Register rule [{}]", path);
            invoker.createPath(client, path, CreateMode.PERSISTENT);
        }
    }

    @Override
    public void deleteRule(String categoryName, String ruleName) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createRulePath(categoryName, ruleName);
        String path = builder.toString();

        if (invoker.pathExist(client, path)) {
            LOG.info("Delete rule [{}]", path);
            invoker.deletePath(client, path);
        }
    }

    @Override
    public void persistRule(String categoryName, String ruleName, String ruleContent) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createRulePath(categoryName, ruleName);
        String path = builder.toString();

        LOG.info("Persist rule [{}]", path);
        invoker.setData(client, path, ruleContent.getBytes());
    }

    @Override
    public String retrieveRule(String categoryName, String ruleName) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createRulePath(categoryName, ruleName);
        String path = builder.toString();

        byte[] data = invoker.getData(client, path);
        if (ArrayUtils.isNotEmpty(data)) {
            LOG.info("Retrieved property [{}]", path);

            String ruleContent = new String(data, CoroutineConstants.ENCODING_FORMAT);

            return ruleContent;
        }

        return null;
    }

    @Override
    public List<String> getRuleNames(String categoryName) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }
        
        StringBuilder builder = createCategoryPath(categoryName);
        String path = builder.toString();

        return invoker.getChildNameList(client, path);
    }

    @Override
    public void addCategoryListener(String categoryName) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }
        
        StringBuilder builder = createCategoryPath(categoryName);
        String path = builder.toString();

        new ZookeeperCategoryListener(client, invoker, path);
    }
    
    @Override
    public void addRuleListener(String categoryName, String ruleName) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }
        
        StringBuilder builder = createRulePath(categoryName, ruleName);
        String path = builder.toString();
        
        new ZookeeperRuleListener(client, invoker, path);
    }

    public StringBuilder createNamespacePath() {
        StringBuilder builder = new StringBuilder();
        builder.append("/");
        builder.append(StringUtils.isEmpty(namespace) ? properties.getString(CoroutineConstants.NAMESPACE_ELEMENT_NAME) : namespace);

        return builder;
    }

    public StringBuilder createCategoryPath(String categoryName) {
        StringBuilder builder = createNamespacePath();
        builder.append("/");
        builder.append(categoryName);

        return builder;
    }

    public StringBuilder createRulePath(String categoryName, String ruleName) {
        StringBuilder builder = createCategoryPath(categoryName);
        builder.append("/");
        builder.append(ruleName);

        return builder;
    }
}