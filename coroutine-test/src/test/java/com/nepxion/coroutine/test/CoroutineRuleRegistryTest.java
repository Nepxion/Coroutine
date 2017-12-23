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

import com.nepxion.coroutine.common.constant.CoroutineConstant;
import com.nepxion.coroutine.common.property.CoroutineContent;
import com.nepxion.coroutine.common.property.CoroutinePropertiesManager;
import com.nepxion.coroutine.registry.RegistryLauncher;
import com.nepxion.coroutine.registry.zookeeper.ZookeeperRegistryLauncher;

public class CoroutineRuleRegistryTest {
    private static final Logger LOG = LoggerFactory.getLogger(CoroutineRuleRegistryTest.class);

    @Test
    public void testAddRule1() throws Exception {
        RegistryLauncher launcher = new ZookeeperRegistryLauncher();
        launcher.setProperties(CoroutinePropertiesManager.getProperties());
        launcher.start();

        launcher.getRegistryExecutor().registerRule("PayRoute", "Rule");
        launcher.getRegistryExecutor().persistRule("PayRoute", "Rule", new CoroutineContent("rule1.xml", CoroutineConstant.ENCODING_FORMAT).getContent());

        LOG.info("规则 : \n{}", launcher.getRegistryExecutor().retrieveRule("PayRoute", "Rule"));

        System.in.read();
    }

    @Test
    public void testAddRule2() throws Exception {
        RegistryLauncher launcher = new ZookeeperRegistryLauncher();
        launcher.setProperties(CoroutinePropertiesManager.getProperties());
        launcher.start();

        launcher.getRegistryExecutor().registerRule("PayRoute", "SubRule");
        launcher.getRegistryExecutor().persistRule("PayRoute", "SubRule", new CoroutineContent("rule2.xml", CoroutineConstant.ENCODING_FORMAT).getContent());

        LOG.info("规则 : \n{}", launcher.getRegistryExecutor().retrieveRule("PayRoute", "SubRule"));

        System.in.read();
    }
}