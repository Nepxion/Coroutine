package com.nepxion.coroutine.testcase.thread;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.framework.core.CoroutineManager;

public class ThreadTest {
    private static final Logger LOG = LoggerFactory.getLogger(ThreadTest.class);

    @Test
    public void test() throws Exception {
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CoroutineManager.start();
                    } catch (Exception e) {
                        LOG.error("", e);
                    }
                }
            }).start();
        }
        
        System.in.read();
    }
}