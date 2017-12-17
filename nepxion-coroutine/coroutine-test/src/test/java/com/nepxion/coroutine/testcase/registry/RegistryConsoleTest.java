package com.nepxion.coroutine.testcase.registry;

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

import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.property.CoroutineContent;
import com.nepxion.coroutine.registry.RegistryLauncher;
import com.nepxion.coroutine.registry.zookeeper.ZookeeperRegistryLauncher;

public class RegistryConsoleTest {
    private static final Logger LOG = LoggerFactory.getLogger(RegistryConsoleTest.class);

    @Test
    public void test1() throws Exception {
        RegistryLauncher launcher = new ZookeeperRegistryLauncher();
        launcher.start();

        launcher.getRegistryExecutor().registerRule("PayRoute", "Rule");
        launcher.getRegistryExecutor().persistRule("PayRoute", "Rule", new CoroutineContent("rule1.xml", CoroutineConstants.ENCODING_FORMAT).getContent());
        LOG.info("规则 : {}", launcher.getRegistryExecutor().retrieveRule("PayRoute", "Rule"));

        System.in.read();
    }

    @Test
    public void test2() throws Exception {
        RegistryLauncher launcher = new ZookeeperRegistryLauncher();
        launcher.start();

        launcher.getRegistryExecutor().registerRule("PayRoute", "SubRule");
        launcher.getRegistryExecutor().persistRule("PayRoute", "SubRule", new CoroutineContent("rule2.xml", CoroutineConstants.ENCODING_FORMAT).getContent());
        LOG.info("规则 : {}", launcher.getRegistryExecutor().retrieveRule("PayRoute", "SubRule"));

        System.in.read();
    }

    @Test
    public void test3() throws Exception {
        RegistryLauncher launcher = new ZookeeperRegistryLauncher();
        launcher.start();

        launcher.getRegistryExecutor().registerRule("Distribution PayRoute", "Distribution Rule");
        launcher.getRegistryExecutor().persistRule("Distribution PayRoute", "Distribution Rule", new CoroutineContent("rule3.xml", CoroutineConstants.ENCODING_FORMAT).getContent());
        LOG.info("规则 : {}", launcher.getRegistryExecutor().retrieveRule("Distribution PayRoute", "Distribution Rule"));

        System.in.read();
    }
}