package com.nepxion.coroutine.framework.core.promise;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.jdeferred.Deferred;

public class PromiseSerialPortal<T> extends PromiseDeferred<T> {

    public Deferred<T, Exception, Void> start(T result) {
        return resolve(result);
    }
}