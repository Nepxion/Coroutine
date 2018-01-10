package com.nepxion.coroutine.common.property;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.constant.CoroutineConstant;

public class CoroutinePropertiesManager {
    private static final Logger LOG = LoggerFactory.getLogger(CoroutinePropertiesManager.class);
    private static final String DEFAULT_PATH = "coroutine.properties";
    private static final String EXT_PATH = "coroutine-ext.properties";

    private static CoroutineProperties properties;
    private static CoroutineProperties extProperties;

    static {
        initializeDefaultProperties();
        initializeExtProperties();
    }

    private static void initializeDefaultProperties() {
        try {
            LOG.info("Parse default property config file [{}]", DEFAULT_PATH);

            properties = new CoroutineProperties(DEFAULT_PATH, CoroutineConstant.ENCODING_GBK, CoroutineConstant.ENCODING_UTF_8);
        } catch (Exception e) {
            LOG.error("Parse default property config file failed for [{}]", DEFAULT_PATH, e);
        }
    }

    private static void initializeExtProperties() {
        try {
            LOG.info("Parse ext property config file [{}]", EXT_PATH);

            extProperties = new CoroutineProperties(EXT_PATH, CoroutineConstant.ENCODING_GBK, CoroutineConstant.ENCODING_UTF_8);
        } catch (Exception e) {
            LOG.warn("Parse ext property config file failed for [{}], maybe file doesn't exist, ignore", EXT_PATH);
        }

        if (properties != null && extProperties != null) {
            LOG.info("Merge ext property configs of [{}] to default property configs", EXT_PATH);

            try {
                properties.mergeProperties(extProperties);
            } catch (Exception e) {
                LOG.warn("Merge ext property configs failed", e);
            }
        }
    }

    public static CoroutineProperties getProperties() {
        return properties;
    }

    public static CoroutineProperties getExtProperties() {
        return extProperties;
    }
}