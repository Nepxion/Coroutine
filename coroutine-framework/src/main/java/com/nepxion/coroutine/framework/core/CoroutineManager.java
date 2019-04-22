package com.nepxion.coroutine.framework.core;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.nepxion.banner.NepxionBanner;
import com.nepxion.coroutine.common.constant.CoroutineConstant;
import com.nepxion.coroutine.common.property.CoroutineContent;
import com.nepxion.coroutine.data.entity.RuleKey;
import com.nepxion.coroutine.event.RuleEvent;
import com.nepxion.coroutine.event.RuleUpdatedEvent;
import com.nepxion.coroutine.event.eventbus.EventControllerFactory;
import com.nepxion.coroutine.framework.parser.RuleParser;
import com.nepxion.coroutine.monitor.MonitorLoader;
import com.nepxion.coroutine.registry.RegistryExecutor;
import com.nepxion.coroutine.registry.RegistryLoader;
import com.taobao.text.Color;

public class CoroutineManager {
    private static final Logger LOG = LoggerFactory.getLogger(CoroutineManager.class);
    private static final CoroutineManager COROUTINE_MANAGER = new CoroutineManager();

    static {
        /*String bannerShown = System.getProperty(BannerConstant.BANNER_SHOWN, "true");
        if (Boolean.valueOf(bannerShown)) {
            System.out.println("");
            System.out.println("╔═══╗           ╔╗");
            System.out.println("║╔═╗║          ╔╝╚╗");
            System.out.println("║║ ╚╬══╦═╦══╦╗╔╬╗╔╬╦═╗╔══╗");
            System.out.println("║║ ╔╣╔╗║╔╣╔╗║║║║║║╠╣╔╗╣║═╣");
            System.out.println("║╚═╝║╚╝║║║╚╝║╚╝║║╚╣║║║║║═╣");
            System.out.println("╚═══╩══╩╝╚══╩══╝╚═╩╩╝╚╩══╝");
            System.out.println("Nepxion Coroutine  v" + CoroutineConstant.COROUTINE_VERSION);
            System.out.println("");
        }*/

        LogoBanner logoBanner = new LogoBanner(CoroutineManager.class, "/com/nepxion/coroutine/resource/logo.txt", "Welcome to Nepxion", 9, 5, new Color[] { Color.red, Color.green, Color.cyan, Color.blue, Color.yellow, Color.magenta, Color.red, Color.green, Color.cyan }, true);

        NepxionBanner.show(logoBanner, new Description(BannerConstant.VERSION + ":", CoroutineConstant.COROUTINE_VERSION, 0, 1), new Description(BannerConstant.GITHUB + ":", BannerConstant.NEPXION_GITHUB + "/Coroutine", 0, 1));

        MonitorLoader.load();
        RegistryLoader.load();
        CoroutineLoader.load();
    }

    public static void start() throws Exception {
        RegistryLoader.load().start();
    }

    public static void start(String address) throws Exception {
        RegistryLoader.load().start(address);
    }

    public static void stop() throws Exception {
        RegistryLoader.load().stop();
    }

    public static boolean enabled() {
        return RegistryLoader.load().enabled();
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
        String ruleContent = new CoroutineContent(rulePath, CoroutineConstant.ENCODING_UTF_8).getContent();

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