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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.jdeferred.Promise;
import org.jdeferred.multiple.MasterProgress;
import org.jdeferred.multiple.MultipleResults;
import org.jdeferred.multiple.OneReject;
import org.jdeferred.multiple.OneResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.callback.CoroutineCallback;
import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.property.CoroutinePropertiesManager;
import com.nepxion.coroutine.data.cache.CoroutineCache;
import com.nepxion.coroutine.data.entity.CoroutineEntry;
import com.nepxion.coroutine.data.entity.CoroutineId;
import com.nepxion.coroutine.data.entity.CoroutineList;
import com.nepxion.coroutine.data.entity.CoroutineResult;
import com.nepxion.coroutine.data.entity.DependencyEntity;
import com.nepxion.coroutine.data.entity.ExecutorType;
import com.nepxion.coroutine.data.entity.MethodEntity;
import com.nepxion.coroutine.data.entity.ReferenceEntity;
import com.nepxion.coroutine.data.entity.ReferenceType;
import com.nepxion.coroutine.data.entity.RuleEntity;
import com.nepxion.coroutine.data.entity.StepEntity;
import com.nepxion.coroutine.framework.core.CoroutineException;
import com.nepxion.coroutine.monitor.MonitorEntity;
import com.nepxion.coroutine.monitor.MonitorLoader;

public class PromiseParallelExecutor extends AbstractPromiseExecutor<MultipleResults, OneReject, MasterProgress> {
    private static final Logger LOG = LoggerFactory.getLogger(PromiseParallelExecutor.class);

    public PromiseParallelExecutor() {
        setProperties(CoroutinePropertiesManager.getProperties());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Promise<MultipleResults, OneReject, MasterProgress> chain(final CoroutineResult<Object> origin, RuleEntity ruleEntity, StepEntity stepEntity, final String chainName) throws Exception {
        int[] indexes = stepEntity.getIndexes();

        Callable<Object>[] callables = new Callable[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            int index = indexes[i];
            final ReferenceEntity referenceEntity = ruleEntity.getReferenceEntity(index);
            if (referenceEntity == null) {
                CoroutineException e = new CoroutineException("No method/dependency entity is found, index=" + index);
                LOG.error("Invoke failed", e);

                throw e;
            }

            callables[i] = new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    CoroutineId id = origin.getId();

                    MonitorEntity monitorEntity = PromiseParallelExecutor.this.createMonitorEntity(ExecutorType.PARALLEL, id, referenceEntity, chainName);
                    monitorEntity.setStartTime(System.currentTimeMillis());

                    Object value = null;
                    try {
                        // 反射调用
                        value = invoke(referenceEntity, origin);

                        monitorEntity.setReturnType(value != null ? value.getClass().getName() : null);
                        monitorEntity.setEndTime(System.currentTimeMillis());
                        MonitorLoader.startSuccess(monitorEntity);
                    } catch (Exception e) {
                        monitorEntity.setException(e);
                        monitorEntity.setEndTime(System.currentTimeMillis());
                        MonitorLoader.startFailure(monitorEntity);

                        throw e;
                    }

                    CoroutineEntry<Object> entry = new CoroutineEntry<Object>();
                    entry.setId(id);
                    entry.setReferenceEntity(referenceEntity);
                    entry.setResult(value);

                    return entry;
                }
            };
        }

        PromiseParallelPortal promiseParallelExecutor = new PromiseParallelPortal(PromiseThreadPoolFactory.getThreadPoolExecutor());
        Promise<MultipleResults, OneReject, MasterProgress> promise = promiseParallelExecutor.when(callables);

        return promise;
    }

    // 并行结果缓存
    @Override
    protected Promise<MultipleResults, OneReject, MasterProgress> done(Promise<MultipleResults, OneReject, MasterProgress> promise) {
        return promise.done(new PromiseDone<MultipleResults>() {
            @Override
            public void onDone(MultipleResults results) {
                CoroutineResult<Object> result = createResult(results);

                CoroutineCache.setResult(result);
            }
        });
    }

    // 并行结果回调
    @Override
    protected Promise<MultipleResults, OneReject, MasterProgress> done(Promise<MultipleResults, OneReject, MasterProgress> promise, final CoroutineCallback<CoroutineResult<Object>> callback) {
        return promise.done(new PromiseDone<MultipleResults>() {
            @Override
            public void onDone(MultipleResults results) {
                CoroutineResult<Object> result = createResult(results);

                callback.onResult(result);
            }
        });
    }

    // 并行异常回调
    @Override
    protected Promise<MultipleResults, OneReject, MasterProgress> fail(Promise<MultipleResults, OneReject, MasterProgress> promise, final CoroutineCallback<CoroutineResult<Object>> callback) {
        return promise.fail(new PromiseFail<OneReject>() {
            @Override
            public void onFail(OneReject reject) {
                callback.onError((Exception) reject.getReject());
            }
        });
    }

    // 解析返回对象集
    protected CoroutineResult<Object> createResult(MultipleResults results) {
        boolean flag = properties.getBoolean(CoroutineConstants.PARALLEL_AGGREGATION_COUPLING);
        if (flag) {
            return createListResult(results);
        } else {
            return createMapResult(results);
        }
    }

    // 返回CoroutineEntry结果集，这种方式简单，但会强势侵入业务端
    @SuppressWarnings("unchecked")
    protected CoroutineResult<Object> createListResult(MultipleResults results) {
        CoroutineId id = null;

        List<CoroutineEntry<Object>> value = new ArrayList<CoroutineEntry<Object>>();
        for (Iterator<OneResult> iterator = results.iterator(); iterator.hasNext();) {
            CoroutineEntry<Object> entry = (CoroutineEntry<Object>) iterator.next().getResult();
            if (id == null) {
                id = entry.getId();
            }
            value.add(entry);
        }

        CoroutineList<Object> list = new CoroutineList<Object>();
        list.setCoroutineEntryList(value);

        CoroutineResult<Object> result = new CoroutineResult<Object>();
        result.setId(id);
        result.setResult(list);

        return result;
    }

    // 返回Map结果集，这种方式不会强势侵入业务端
    @SuppressWarnings("unchecked")
    protected CoroutineResult<Object> createMapResult(MultipleResults results) {
        CoroutineId id = null;

        List<Map<String, Object>> value = new ArrayList<Map<String, Object>>();
        for (Iterator<OneResult> iterator = results.iterator(); iterator.hasNext();) {
            CoroutineEntry<Object> entry = (CoroutineEntry<Object>) iterator.next().getResult();
            if (id == null) {
                id = entry.getId();
            }
            ReferenceEntity referenceEntity = entry.getReferenceEntity();
            ReferenceType referenceType = referenceEntity.getReferenceType();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(CoroutineConstants.ID_ATTRIBUTE_NAME, entry.getId().getId());
            map.put(CoroutineConstants.RESULT_TYPES_ATTRIBUTE_NAME, entry.getResult());
            map.put(CoroutineConstants.REFERENCE_TYPE_ATTRIBUTE_NAME, referenceType.toString());
            map.put(CoroutineConstants.INDEX_ATTRIBUTE_NAME, referenceEntity.getIndex());
            if (referenceType == ReferenceType.COMPONENT_REFERENCE) {
                MethodEntity methodEntity = (MethodEntity) referenceEntity;
                map.put(CoroutineConstants.CLASS_ATTRIBUTE_NAME, methodEntity.getClazz());
                map.put(CoroutineConstants.METHOD_ATTRIBUTE_NAME, methodEntity.getMethod());
                map.put(CoroutineConstants.PARAMETER_TYPES_ATTRIBUTE_NAME, methodEntity.getParameterTypes());
            } else if (referenceType == ReferenceType.DEPENDENCY_REFERENCE) {
                DependencyEntity dependencyEntity = (DependencyEntity) referenceEntity;
                map.put(CoroutineConstants.CATEGORY_ATTRIBUTE_NAME, dependencyEntity.getCategoryName());
                map.put(CoroutineConstants.RULE_ATTRIBUTE_NAME, dependencyEntity.getRuleName());
            }

            value.add(map);
        }

        CoroutineResult<Object> result = new CoroutineResult<Object>();
        result.setId(id);
        result.setResult(value);

        return result;
    }
}