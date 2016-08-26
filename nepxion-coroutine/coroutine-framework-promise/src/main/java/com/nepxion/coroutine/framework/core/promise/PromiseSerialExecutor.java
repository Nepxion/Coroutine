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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.callback.CoroutineCallback;
import com.nepxion.coroutine.common.property.CoroutinePropertiesManager;
import com.nepxion.coroutine.data.cache.CoroutineCache;
import com.nepxion.coroutine.data.entity.CoroutineId;
import com.nepxion.coroutine.data.entity.CoroutineResult;
import com.nepxion.coroutine.data.entity.ExecutorType;
import com.nepxion.coroutine.data.entity.ReferenceEntity;
import com.nepxion.coroutine.data.entity.RuleEntity;
import com.nepxion.coroutine.data.entity.StepEntity;
import com.nepxion.coroutine.framework.core.CoroutineException;
import com.nepxion.coroutine.monitor.MonitorEntity;
import com.nepxion.coroutine.monitor.MonitorLoader;

public class PromiseSerialExecutor extends AbstractPromiseExecutor<CoroutineResult<Object>, Exception, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(PromiseSerialExecutor.class);
    
    public PromiseSerialExecutor() {
        setProperties(CoroutinePropertiesManager.getProperties());
    }

    @Override
    protected Promise<CoroutineResult<Object>, Exception, Void> chain(CoroutineResult<Object> origin, RuleEntity ruleEntity, StepEntity stepEntity, String chainName) throws Exception {
        PromiseSerialPortal<CoroutineResult<Object>> romiseSerialExecutor = new PromiseSerialPortal<CoroutineResult<Object>>();
        romiseSerialExecutor.start(origin);

        Promise<CoroutineResult<Object>, Exception, Void> promise = null;
        int[] indexes = stepEntity.getIndexes();
        for (int i = 0; i < indexes.length; i++) {
            int index = indexes[i];
            ReferenceEntity referenceEntity = ruleEntity.getReferenceEntity(index);
            if (referenceEntity == null) {
                CoroutineException e = new CoroutineException("No method/dependency entity is found, index=" + index);
                LOG.error("Invoke failed", e);

                throw e;
            }

            if (i == 0) {
                promise = then(romiseSerialExecutor, referenceEntity, chainName);
            } else {
                promise = then(promise, referenceEntity, chainName);
            }
        }

        return promise;
    }

    // 串行结果缓存
    @Override
    protected Promise<CoroutineResult<Object>, Exception, Void> done(Promise<CoroutineResult<Object>, Exception, Void> promise) {
        return promise.done(new PromiseDone<CoroutineResult<Object>>() {
            @Override
            public void onDone(CoroutineResult<Object> result) {
                CoroutineCache.setResult(result);
            }
        });
    }

    // 串行结果回调
    @Override
    protected Promise<CoroutineResult<Object>, Exception, Void> done(Promise<CoroutineResult<Object>, Exception, Void> promise, final CoroutineCallback<CoroutineResult<Object>> callback) {
        return promise.done(new PromiseDone<CoroutineResult<Object>>() {
            @Override
            public void onDone(CoroutineResult<Object> result) {
                callback.onResult(result);
            }
        });
    }

    // 串行异常回调
    @Override
    protected Promise<CoroutineResult<Object>, Exception, Void> fail(Promise<CoroutineResult<Object>, Exception, Void> promise, final CoroutineCallback<CoroutineResult<Object>> callback) {
        return promise.fail(new PromiseFail<Exception>() {
            @Override
            public void onFail(Exception exception) {
                callback.onError(exception);
            }
        });
    }

    // 串行调用单个步骤
    protected Promise<CoroutineResult<Object>, Exception, Void> then(Promise<CoroutineResult<Object>, Exception, Void> promise, final ReferenceEntity referenceEntity, final String chainName) {
        return promise.then(new PromisePipe<CoroutineResult<Object>, CoroutineResult<Object>>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResult(CoroutineResult<Object> result) {
                CoroutineId id = result.getId();

                // 线程上下文设置Promise
                PromiseDeferred<CoroutineResult<Object>> promise = new PromiseDeferred<CoroutineResult<Object>>();
                PromiseContext.setPromise(promise);

                MonitorEntity monitorEntity = PromiseSerialExecutor.this.createMonitorEntity(ExecutorType.SERIAL, id, referenceEntity, chainName);
                monitorEntity.setStartTime(System.currentTimeMillis());
                
                // 监测外部是否有异常赋予链式调用
                CoroutineResult<Object> coroutineResult = (CoroutineResult<Object>) CoroutineCache.getResult(result.getId());
                if (CoroutineCache.hasException(coroutineResult)) {
                    Exception e = coroutineResult.getException();
                    
                    monitorEntity.setException(e);
                    monitorEntity.setEndTime(System.currentTimeMillis());
                    MonitorLoader.startFailure(monitorEntity);

                    // 执行FaileCallback
                    promise.reject(e);

                    return;
                }

                try {
                    // 反射调用
                    Object value = invoke(referenceEntity, result);
                    result.setResult(value);

                    // 执行下个链式调用
                    promise.resolve(result);

                    monitorEntity.setReturnType(value != null ? value.getClass().getName() : null);
                    monitorEntity.setEndTime(System.currentTimeMillis());
                    MonitorLoader.startSuccess(monitorEntity);
                } catch (Exception e) {
                    monitorEntity.setException(e);
                    monitorEntity.setEndTime(System.currentTimeMillis());
                    MonitorLoader.startFailure(monitorEntity);

                    result.setException(e);

                    // 执行FaileCallback
                    promise.reject(e);
                }
            }
        });
    }
}