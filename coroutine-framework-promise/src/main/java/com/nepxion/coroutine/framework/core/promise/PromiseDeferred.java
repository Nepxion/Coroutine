package com.nepxion.coroutine.framework.core.promise;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.jdeferred.Deferred;
import org.jdeferred.impl.DeferredObject;

public class PromiseDeferred<T> extends DeferredObject<T, Exception, Void> {

    @Override
    public Deferred<T, Exception, Void> reject(Exception exception) {
        // LOG.error("Promise chain is terminated", exception);

        return super.reject(exception);
    }
}