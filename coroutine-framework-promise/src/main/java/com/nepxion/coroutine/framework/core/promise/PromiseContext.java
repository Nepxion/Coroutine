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

public class PromiseContext {
    // 子线程内部独享的Promise对象，用来维护链式调用的上下文
    private static final ThreadLocal<PromiseDeferred<?>> PROMISE = new ThreadLocal<PromiseDeferred<?>>();

    public static PromiseDeferred<?> currentPromise() {
        PromiseDeferred<?> promise = PROMISE.get();
        PROMISE.remove();

        return promise;
    }

    @SuppressWarnings("unchecked")
    public static <T> PromiseDeferred<T> currentPromise(Class<T> genericType) {
        return (PromiseDeferred<T>) currentPromise();
    }

    public static void setPromise(PromiseDeferred<?> promise) {
        PROMISE.set(promise);
    }
}