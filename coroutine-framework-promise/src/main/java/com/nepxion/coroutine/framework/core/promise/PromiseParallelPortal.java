package com.nepxion.coroutine.framework.core.promise;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.jdeferred.impl.DefaultDeferredManager;

public class PromiseParallelPortal extends DefaultDeferredManager {
    public PromiseParallelPortal() {
        super();
    }

    public PromiseParallelPortal(ExecutorService executorService) {
        super(executorService);
    }

    public void waitForCompletion() {
        shutdown();
        while (!isTerminated()) {
            try {
                awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {

            }
        }
    }
}