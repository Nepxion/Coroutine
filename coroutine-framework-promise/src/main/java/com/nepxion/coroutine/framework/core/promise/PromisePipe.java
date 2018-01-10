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

import org.jdeferred.DonePipe;
import org.jdeferred.Promise;

// T为入参类型，即onResult方法的入参，可以来自两种方式：
//   1. 初始传入：由PromiseSerialExecutor.execute(...)方式传入
//   2. 链式传入：上一个链式调用方法的回调值方式从传入
// R为出参类型，即onResult方法的实现体里的异步方法的所带来的回调值，它将是下个链式调用onResult方法的入参
public abstract class PromisePipe<T, R> implements DonePipe<T, R, Exception, Void> {

    @SuppressWarnings("unchecked")
    @Override
    public Promise<R, Exception, Void> pipeDone(T result) {
        try {
            onResult(result);

            Promise<R, Exception, Void> promise = (Promise<R, Exception, Void>) PromiseContext.currentPromise();
            if (promise == null) {
                throw new IllegalArgumentException("Promise hasn't been injected");
            }

            return promise;
        } catch (Exception e) {
            return reject(e);
        }
    }

    public Promise<R, Exception, Void> resolve(R result) {
        // 无异常，继续执行下一个调用，通过resolve方法唤醒
        PromiseDeferred<R> promise = new PromiseDeferred<R>();

        return promise.resolve(result);
    }

    public Promise<R, Exception, Void> reject(Exception exception) {
        // 有异常，终止下一个调用，通过reject方法终止
        PromiseDeferred<R> promise = new PromiseDeferred<R>();

        return promise.reject(exception);
    }

    public abstract void onResult(T result);
}