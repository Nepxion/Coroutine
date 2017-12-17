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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.event.RuleEvent;
import com.nepxion.coroutine.event.RuleUpdatedEvent;
import com.nepxion.coroutine.event.eventbus.EventControllerFactory;
import com.nepxion.coroutine.registry.zookeeper.common.ZookeeperException;
import com.nepxion.coroutine.registry.zookeeper.common.ZookeeperInvoker;
import com.nepxion.coroutine.registry.zookeeper.common.listener.ZookeeperNodeCacheListener;

public class ZookeeperRuleListener extends ZookeeperNodeCacheListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperRuleListener.class);

    private ZookeeperInvoker invoker;

    public ZookeeperRuleListener(CuratorFramework client, ZookeeperInvoker invoker, String path) throws Exception {
        super(client, path);

        this.invoker = invoker;
    }

    @Override
    public void nodeChanged() throws Exception {
        String categoryName = getCategoryName();
        String ruleName = getRuleName();
        String ruleContent = getRuleContent();

        RuleUpdatedEvent ruleEvent = new RuleUpdatedEvent(categoryName, ruleName, ruleContent);

        EventControllerFactory.getAsyncController(RuleEvent.getEventName()).post(ruleEvent);

        LOG.info("Rule updated : category={}, rule={}", categoryName, ruleName);
    }

    private String getCategoryName() {
        String categoryPath = path.substring(0, path.lastIndexOf("/"));

        return categoryPath.substring(categoryPath.lastIndexOf("/") + 1);
    }

    private String getRuleName() {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private String getRuleContent() throws Exception {
        byte[] data = invoker.getData(client, path);
        if (ArrayUtils.isNotEmpty(data)) {
            return new String(data, CoroutineConstants.ENCODING_FORMAT);
        } else {
            throw new ZookeeperException("Rule content is empty");
        }
    }
}