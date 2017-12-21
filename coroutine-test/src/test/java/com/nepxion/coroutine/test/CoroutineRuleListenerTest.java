package com.nepxion.coroutine.test;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.nepxion.coroutine.common.property.CoroutinePropertiesManager;
import com.nepxion.coroutine.event.RuleAddedEvent;
import com.nepxion.coroutine.event.RuleEvent;
import com.nepxion.coroutine.event.RuleReconnectedEvent;
import com.nepxion.coroutine.event.RuleRemovedEvent;
import com.nepxion.coroutine.event.RuleUpdatedEvent;
import com.nepxion.coroutine.event.eventbus.EventControllerFactory;
import com.nepxion.coroutine.registry.RegistryLauncher;
import com.nepxion.coroutine.registry.zookeeper.ZookeeperRegistryLauncher;

public class CoroutineRuleListenerTest {
    private static final Logger LOG = LoggerFactory.getLogger(CoroutineRuleListenerTest.class);

    @Test
    public void test() throws Exception {
        EventControllerFactory.getAsyncController(RuleEvent.getEventName()).register(this);

        RegistryLauncher launcher = new ZookeeperRegistryLauncher();
        launcher.setProperties(CoroutinePropertiesManager.getProperties());
        launcher.start();

        // launcher.getRegistryExecutor().addCategoryListener("PayRoute");
        launcher.getRegistryExecutor().addRuleListener("PayRoute", "Rule1");
        launcher.getRegistryExecutor().addRuleListener("PayRoute", "Rule2");

        System.in.read();
    }

    @Subscribe
    public void listen(RuleAddedEvent event) {
        LOG.info("RuleAddedEvent : category={}, ruleName={}, ruleContent={}", event.getCategoryName(), event.getRuleName(), event.getRuleContent());
    }

    @Subscribe
    public void listen(RuleUpdatedEvent event) {
        LOG.info("RuleUpdatedEvent : category={}, ruleName={}, ruleContent={}", event.getCategoryName(), event.getRuleName(), event.getRuleContent());
    }

    @Subscribe
    public void listen(RuleRemovedEvent event) {
        LOG.info("RuleRemovedEvent : category={}, ruleName={}", event.getCategoryName(), event.getRuleName());
    }

    @Subscribe
    public void listen(RuleReconnectedEvent event) {
        LOG.info("RuleReconnectedEvent : category={}, ruleMap={}", event.getCategoryName(), event.getRuleMap());
    }
}