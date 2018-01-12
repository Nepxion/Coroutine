package com.nepxion.coroutine.framework.core.kilim;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kilim.Pausable;
import kilim.Task;
import kilim.TaskGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.callback.CoroutineCallback;
import com.nepxion.coroutine.common.constant.CoroutineConstant;
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

/**
 * Kilim 相关方法用法：
 * get()   / set()   / put()   阻塞协程，不阻塞线程
 * getb()  / setb()  / putb()  阻塞线程
 * getnb() / setnb() / setnb() 不阻塞协程，也不阻塞线程
*/
public class KilimParallelExecutor extends AbstractKilimExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(KilimParallelExecutor.class);

    private TaskGroup taskGroup;
    private List<Task> tasks;
    private List<CoroutineEntry<Object>> entries;
    private CoroutineCallback<CoroutineResult<Object>> callback;
    private boolean lastStep;

    @SuppressWarnings("unchecked")
    public KilimParallelExecutor(final CoroutineId coroutineId, RuleEntity ruleEntity, StepEntity stepEntity, final String chainName, final CoroutineCallback<CoroutineResult<Object>> callback, boolean lastStep) {
        final CoroutineResult<Object> result = (CoroutineResult<Object>) CoroutineCache.getResult(coroutineId);
        if (result == null || CoroutineCache.hasException(result)) {
            return;
        }

        setProperties(CoroutinePropertiesManager.getProperties());

        taskGroup = new TaskGroup();
        tasks = new ArrayList<Task>();
        entries = new ArrayList<CoroutineEntry<Object>>();
        this.callback = callback;
        this.lastStep = lastStep;

        final int[] indexes = stepEntity.getIndexes();
        for (int i = 0; i < indexes.length; i++) {
            int index = indexes[i];
            final ReferenceEntity referenceEntity = ruleEntity.getReferenceEntity(index);
            if (referenceEntity == null) {
                CoroutineException e = new CoroutineException("No method/dependency entity is found, index=" + index);
                LOG.error("Invoke failed", e);

                throw e;
            }

            Task task = new Task() {
                @Override
                public void execute() throws Pausable {
                    CoroutineId id = result.getId();

                    MonitorEntity monitorEntity = KilimParallelExecutor.this.createMonitorEntity(ExecutorType.PARALLEL, coroutineId, referenceEntity, chainName);
                    monitorEntity.setStartTime(System.currentTimeMillis());

                    Object value = null;
                    try {
                        // 反射调用
                        value = KilimParallelExecutor.this.invoke(referenceEntity, result);
                        result.setResult(value);

                        monitorEntity.setReturnType(value != null ? value.getClass().getName() : null);
                        monitorEntity.setEndTime(System.currentTimeMillis());
                        MonitorLoader.startSuccess(monitorEntity);
                    } catch (Exception e) {
                        monitorEntity.setException(e);
                        monitorEntity.setEndTime(System.currentTimeMillis());
                        MonitorLoader.startFailure(monitorEntity);

                        result.setException(e);

                        callback.onError(e);

                        // 结束协程
                        Task.exit(0);
                    }

                    CoroutineEntry<Object> entry = new CoroutineEntry<Object>();
                    entry.setId(id);
                    entry.setReferenceEntity(referenceEntity);
                    entry.setResult(value);

                    entries.add(entry);

                    // 结束协程
                    Task.exit(0);
                }
            };

            taskGroup.add(task);
            tasks.add(task);
        }
    }

    @Override
    public boolean isTerminated() throws Exception {
        return taskGroup == null;
    }

    @Override
    public void start() throws Exception {
        for (Task task : tasks) {
            task.start();
        }

        // 阻塞线程
        taskGroup.joinb();

        CoroutineResult<Object> result = createResult(entries);

        if (lastStep) {
            callback.onResult(result);
        } else {
            CoroutineCache.setResult(result);
        }
    }

    // 解析返回对象集
    protected CoroutineResult<Object> createResult(List<CoroutineEntry<Object>> value) {
        boolean flag = properties.getBoolean(CoroutineConstant.PARALLEL_AGGREGATION_COUPLING);
        if (flag) {
            return createListResult(value);
        } else {
            return createMapResult(value);
        }
    }

    // 返回CoroutineEntry结果集，这种方式简单，但会强势侵入业务端
    protected CoroutineResult<Object> createListResult(List<CoroutineEntry<Object>> value) {
        CoroutineId id = null;

        for (CoroutineEntry<Object> entry : value) {
            if (id == null) {
                id = entry.getId();
            }
        }

        CoroutineList<Object> list = new CoroutineList<Object>();
        list.setCoroutineEntryList(value);

        CoroutineResult<Object> result = new CoroutineResult<Object>();
        result.setId(id);
        result.setResult(list);

        return result;
    }

    // 返回Map结果集，这种方式不会强势侵入业务端
    protected CoroutineResult<Object> createMapResult(List<CoroutineEntry<Object>> entries) {
        CoroutineId id = null;

        List<Map<String, Object>> value = new ArrayList<Map<String, Object>>();
        for (CoroutineEntry<Object> entry : entries) {
            if (id == null) {
                id = entry.getId();
            }
            ReferenceEntity referenceEntity = entry.getReferenceEntity();
            ReferenceType referenceType = referenceEntity.getReferenceType();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(CoroutineConstant.ID_ATTRIBUTE_NAME, entry.getId().getId());
            map.put(CoroutineConstant.RESULT_TYPES_ATTRIBUTE_NAME, entry.getResult());
            map.put(CoroutineConstant.REFERENCE_TYPE_ATTRIBUTE_NAME, referenceType.toString());
            map.put(CoroutineConstant.INDEX_ATTRIBUTE_NAME, referenceEntity.getIndex());
            if (referenceType == ReferenceType.COMPONENT_REFERENCE) {
                MethodEntity methodEntity = (MethodEntity) referenceEntity;
                map.put(CoroutineConstant.CLASS_ATTRIBUTE_NAME, methodEntity.getClazz());
                map.put(CoroutineConstant.METHOD_ATTRIBUTE_NAME, methodEntity.getMethod());
                map.put(CoroutineConstant.PARAMETER_TYPES_ATTRIBUTE_NAME, methodEntity.getParameterTypes());
            } else if (referenceType == ReferenceType.DEPENDENCY_REFERENCE) {
                DependencyEntity dependencyEntity = (DependencyEntity) referenceEntity;
                map.put(CoroutineConstant.CATEGORY_ATTRIBUTE_NAME, dependencyEntity.getCategoryName());
                map.put(CoroutineConstant.RULE_ATTRIBUTE_NAME, dependencyEntity.getRuleName());
            }

            value.add(map);
        }

        CoroutineResult<Object> result = new CoroutineResult<Object>();
        result.setId(id);
        result.setResult(value);

        return result;
    }
}