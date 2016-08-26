package com.nepxion.coroutine.framework.core.promise;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.jdeferred.Promise;

import com.nepxion.coroutine.common.callback.CoroutineCallback;
import com.nepxion.coroutine.data.cache.CoroutineCache;
import com.nepxion.coroutine.data.entity.CoroutineId;
import com.nepxion.coroutine.data.entity.CoroutineResult;
import com.nepxion.coroutine.data.entity.RuleEntity;
import com.nepxion.coroutine.data.entity.StepEntity;
import com.nepxion.coroutine.framework.core.AbstractCoroutineExecutor;

public abstract class AbstractPromiseExecutor<T, F, P> extends AbstractCoroutineExecutor {    
    /**
     * 执行异步调用，用在某调用链中间步
     * 当when链结束后执行then链，或者then链结束后执行when链，上个链的返回的上下文对象需要缓存，以便被下个链所引用
     * 对于异步来说，如果上个链正常调用，则缓存对象，如果上个链调用出现异常，则直接通过callback回调异常，并终止下个链调用
     * @param id         全局唯一的ID，可以用UUID来实现，也可以外部传入唯一交易号/流水号
     * @param ruleEntity 规则实体
     * @param stepEntity 步骤实体
     * @param chainName  链式调用名称
     * @param callback   回调接口
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void execute(CoroutineId id, RuleEntity ruleEntity, StepEntity stepEntity, String chainName, CoroutineCallback<CoroutineResult<Object>> callback) throws Exception {
        CoroutineResult<Object> result = (CoroutineResult<Object>) CoroutineCache.getResult(id);
        if (result == null || CoroutineCache.hasException(result)) {
            return;
        }

        Promise<T, F, P> promise = chain(result, ruleEntity, stepEntity, chainName);
        promise = done(promise);
        promise = fail(promise, callback);
        // 阻塞等待链式调用最后一步结束
        promise.waitSafely();
    }

    /**
     * 执行异步回调，用在调用链最后一步
     * @param id         全局唯一的ID，可以用UUID来实现，也可以外部传入唯一交易号/流水号
     * @param ruleEntity 规则实体
     * @param stepEntity 步骤实体
     * @param chainName  链式调用名称
     * @param callback   回调接口
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void finish(CoroutineId id, RuleEntity ruleEntity, StepEntity stepEntity, String chainName, CoroutineCallback<CoroutineResult<Object>> callback) throws Exception {
        CoroutineResult<Object> result = (CoroutineResult<Object>) CoroutineCache.getResult(id);
        if (result == null || CoroutineCache.hasException(result)) {
            return;
        }

        Promise<T, F, P> promise = chain(result, ruleEntity, stepEntity, chainName);

        promise = done(promise, callback);
        fail(promise, callback);
    }

    // 执行链式调用(不包含done和fail)
    protected abstract Promise<T, F, P> chain(CoroutineResult<Object> origin, RuleEntity ruleEntity, StepEntity stepEntity, String chainName) throws Exception;

    // 链式调用结束后，如果还有下一组链式调用，需要缓存这组链式调用的结果
    protected abstract Promise<T, F, P> done(Promise<T, F, P> promise);

    // 链式调用结束后，回调处理成功的结果
    protected abstract Promise<T, F, P> done(Promise<T, F, P> promise, CoroutineCallback<CoroutineResult<Object>> callback);

    // 链式调用结束后，回调处理失败的异常
    protected abstract Promise<T, F, P> fail(Promise<T, F, P> promise, CoroutineCallback<CoroutineResult<Object>> callback);
}