package com.nepxion.coroutine.framework.core;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CoroutineDestroy {
    private static final Logger LOG = LoggerFactory.getLogger(CoroutineDestroy.class);

    @PreDestroy
    public void destroy() {
        if (CoroutineManager.enabled()) {
            LOG.info("Start to close connection to registry center...");

            try {
                CoroutineManager.stop();
            } catch (Exception e) {
                LOG.error("Close connection to registry center failed", e);
            }
        }
    }
}