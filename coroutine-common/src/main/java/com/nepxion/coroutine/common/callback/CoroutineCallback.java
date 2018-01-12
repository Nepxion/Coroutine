package com.nepxion.coroutine.common.callback;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

public abstract class CoroutineCallback<T> {
    public void call(T result, Exception exception) {
        if (exception == null) {
            onResult(result);
        } else {
            onError(exception);
        }
    }

    public abstract void onResult(T result);

    public abstract void onError(Exception exception);
}