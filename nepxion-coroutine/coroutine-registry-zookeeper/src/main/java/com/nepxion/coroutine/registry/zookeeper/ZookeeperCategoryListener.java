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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.event.RuleAddedEvent;
import com.nepxion.coroutine.event.RuleEvent;
import com.nepxion.coroutine.event.RuleReconnectedEvent;
import com.nepxion.coroutine.event.RuleRemovedEvent;
import com.nepxion.coroutine.event.RuleUpdatedEvent;
import com.nepxion.coroutine.event.eventbus.EventControllerFactory;
import com.nepxion.coroutine.event.eventbus.EventControllerType;
import com.nepxion.coroutine.registry.zookeeper.common.ZookeeperException;
import com.nepxion.coroutine.registry.zookeeper.common.ZookeeperInvoker;
import com.nepxion.coroutine.registry.zookeeper.common.listener.ZookeeperPathChildrenCacheListener;

public class ZookeeperCategoryListener extends ZookeeperPathChildrenCacheListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperCategoryListener.class);

    private ZookeeperInvoker invoker;

    public ZookeeperCategoryListener(CuratorFramework client, ZookeeperInvoker invoker, String path) throws Exception {
        super(client, path);

        this.invoker = invoker;
    }

    @Override
    public void initialized(PathChildrenCacheEvent event) throws Exception {

    }

    @Override
    public void childAdded(PathChildrenCacheEvent event) throws Exception {
        String childPath = event.getData().getPath();

        String categoryName = getCategoryName();
        String ruleName = getRuleName(childPath);
        String ruleContent = getRuleContent(childPath);

        RuleAddedEvent ruleEvent = new RuleAddedEvent(categoryName, ruleName, ruleContent);

        EventControllerFactory.getController(RuleEvent.getEventName(), EventControllerType.ASYNC).post(ruleEvent);

        LOG.info("Rule added : category={}, rule={}", categoryName, ruleName);
    }

    @Override
    public void childUpdated(PathChildrenCacheEvent event) throws Exception {
        String childPath = event.getData().getPath();

        String categoryName = getCategoryName();
        String ruleName = getRuleName(childPath);
        String ruleContent = getRuleContent(childPath);

        RuleUpdatedEvent ruleEvent = new RuleUpdatedEvent(categoryName, ruleName, ruleContent);

        EventControllerFactory.getController(RuleEvent.getEventName(), EventControllerType.ASYNC).post(ruleEvent);

        LOG.info("Rule updated : category={}, rule={}", categoryName, ruleName);
    }

    @Override
    public void childRemoved(PathChildrenCacheEvent event) throws Exception {
        String childPath = event.getData().getPath();

        String categoryName = getCategoryName();
        String ruleName = getRuleName(childPath);

        RuleRemovedEvent ruleEvent = new RuleRemovedEvent(categoryName, ruleName);

        EventControllerFactory.getController(RuleEvent.getEventName(), EventControllerType.ASYNC).post(ruleEvent);

        LOG.info("Rule removed : category={}, rule={}", categoryName, ruleName);
    }

    @Override
    public void connectionSuspended(PathChildrenCacheEvent event) throws Exception {

    }

    @Override
    public void connectionReconnected(PathChildrenCacheEvent event) throws Exception {
        String categoryName = getCategoryName();
        Map<String, String> ruleMap = getRuleMap();

        RuleReconnectedEvent ruleEvent = new RuleReconnectedEvent(categoryName, ruleMap);

        EventControllerFactory.getController(RuleEvent.getEventName(), EventControllerType.ASYNC).post(ruleEvent);

        LOG.info("Rule reconnected : category={}", categoryName);
    }

    @Override
    public void connectionLost(PathChildrenCacheEvent event) throws Exception {

    }

    private String getCategoryName() {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private String getRuleName(String childPath) {
        return childPath.substring(childPath.lastIndexOf("/") + 1);
    }

    private String getRuleContent(String childPath) throws Exception {
        byte[] data = invoker.getData(client, childPath);
        if (ArrayUtils.isNotEmpty(data)) {
            return new String(data, CoroutineConstants.ENCODING_FORMAT);
        } else {
            throw new ZookeeperException("Rule content is empty");
        }
    }

    private Map<String, String> getRuleMap() throws Exception {
        Map<String, String> ruleMap = new HashMap<String, String>();
        List<String> childPathList = invoker.getChildPathList(client, path);
        for (String childPath : childPathList) {
            String ruleName = getRuleName(childPath);
            String ruleContent = getRuleContent(childPath);

            ruleMap.put(ruleName, ruleContent);
        }

        return ruleMap;
    }
}