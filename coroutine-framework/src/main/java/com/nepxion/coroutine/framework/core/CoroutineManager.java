package com.nepxion.coroutine.framework.core;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.property.CoroutineContent;
import com.nepxion.coroutine.data.entity.RuleKey;
import com.nepxion.coroutine.event.RuleEvent;
import com.nepxion.coroutine.event.RuleUpdatedEvent;
import com.nepxion.coroutine.event.eventbus.EventControllerFactory;
import com.nepxion.coroutine.framework.parser.RuleParser;
import com.nepxion.coroutine.monitor.MonitorLoader;
import com.nepxion.coroutine.registry.RegistryExecutor;
import com.nepxion.coroutine.registry.RegistryLoader;

public class CoroutineManager {

    private static final Logger LOG = LoggerFactory.getLogger(CoroutineManager.class);
    private static final CoroutineManager COROUTINE_MANAGER = new CoroutineManager();
    private static final AtomicBoolean START = new AtomicBoolean(false);

    static {
        System.out.println("");
        System.out.println("╔═══╗           ╔╗");
        System.out.println("║╔═╗║          ╔╝╚╗");
        System.out.println("║║ ╚╬══╦═╦══╦╗╔╬╗╔╬╦═╗╔══╗");
        System.out.println("║║ ╔╣╔╗║╔╣╔╗║║║║║║╠╣╔╗╣║═╣");
        System.out.println("║╚═╝║╚╝║║║╚╝║╚╝║║╚╣║║║║║═╣");
        System.out.println("╚═══╩══╩╝╚══╩══╝╚═╩╩╝╚╩══╝");
        System.out.println("Nepxion Coroutine  v1.0.0.RELEASE");
        System.out.println("");

        MonitorLoader.load();
        RegistryLoader.load();
        CoroutineLoader.load();
    }

    public static void start() throws Exception {
        if (START.get()) {
            throw new IllegalArgumentException("Registry Center has already connected");
        }

        RegistryLoader.load().start();

        START.set(true);
    }

    public static void start(String address) throws Exception {
        if (START.get()) {
            throw new IllegalArgumentException("Registry Center has already connected");
        }

        RegistryLoader.load().start(address);

        START.set(true);
    }

    public static void stop() throws Exception {
        RegistryLoader.load().stop();

        START.set(false);
    }

    public static void parseRemote(String categoryName, String ruleName) throws Exception {
        RegistryExecutor registryExecutor = RegistryLoader.load().getRegistryExecutor();
        if (registryExecutor == null) {
            throw new IllegalArgumentException("Not support remote parser, is Registry Center connected successfully?");
        }

        String ruleContent = registryExecutor.retrieveRule(categoryName, ruleName);

        COROUTINE_MANAGER.parse(categoryName, ruleName, ruleContent);

        registryExecutor.addRuleListener(categoryName, ruleName);
    }

    public static void parseLocal(String categoryName, String ruleName, String rulePath) throws Exception {
        String ruleContent = new CoroutineContent(rulePath, CoroutineConstants.ENCODING_FORMAT).getContent();

        COROUTINE_MANAGER.parse(categoryName, ruleName, ruleContent);
    }

    public static CoroutineLauncher load() {
        return CoroutineLoader.load();
    }

    private CoroutineManager() {
        EventControllerFactory.getAsyncController(RuleEvent.getEventName()).register(this);
    }

    private void parse(String categoryName, String ruleName, String ruleContent) throws Exception {
        RuleKey ruleKey = new RuleKey();
        ruleKey.setCategoryName(categoryName);
        ruleKey.setRuleName(ruleName);

        RuleParser parser = new RuleParser(ruleKey);
        parser.parse(ruleContent);
    }

    @Subscribe
    public void listen(RuleUpdatedEvent event) {
        String categoryName = event.getCategoryName();
        String ruleName = event.getRuleName();
        String ruleContent = event.getRuleContent();

        try {
            parse(categoryName, ruleName, ruleContent);
        } catch (Exception e) {
            LOG.error("Parse rule failed, categoryName={}, ruleName={}", categoryName, ruleName, e);
        }
    }
}