package com.nepxion.coroutine.framework.core;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.coroutine.common.delegate.CoroutineDelegateImpl;
import com.nepxion.coroutine.common.util.ClassUtil;
import com.nepxion.coroutine.data.cache.ServiceCache;
import com.nepxion.coroutine.data.entity.CoroutineId;
import com.nepxion.coroutine.data.entity.CoroutineResult;
import com.nepxion.coroutine.data.entity.DependencyEntity;
import com.nepxion.coroutine.data.entity.ExecutorType;
import com.nepxion.coroutine.data.entity.MethodEntity;
import com.nepxion.coroutine.data.entity.ReferenceEntity;
import com.nepxion.coroutine.data.entity.ReferenceType;
import com.nepxion.coroutine.data.entity.ServiceId;
import com.nepxion.coroutine.monitor.MonitorEntity;

public abstract class AbstractCoroutineExecutor extends CoroutineDelegateImpl implements CoroutineExecutor {
    @Override
    public Object invoke(ReferenceEntity referenceEntity, CoroutineResult<Object> result) throws Exception {
        String id = result.getId().getId();
        Object[] parameters = ClassUtil.createParameters(result.getResult());

        ReferenceType referenceType = referenceEntity.getReferenceType();
        if (referenceType == ReferenceType.COMPONENT_REFERENCE) {
            MethodEntity methodEntity = (MethodEntity) referenceEntity;
            Object object = methodEntity.getObject();
            String method = methodEntity.getMethod();
            Class<?>[] parameterClasses = methodEntity.getParameterClasses();
            boolean cache = methodEntity.isCache();

            Object value = ClassUtil.invoke(object, method, parameterClasses, parameters);
            if (cache) {
                String categoryName = result.getId().getCategoryName();
                String ruleName = result.getId().getRuleName();
                int index = methodEntity.getIndex();

                ServiceId serviceId = new ServiceId();
                serviceId.setId(id);
                serviceId.setCategoryName(categoryName);
                serviceId.setRuleName(ruleName);
                serviceId.setIndex(index);

                ServiceCache.put(serviceId, value);
            }

            return value;
        } else if (referenceType == ReferenceType.DEPENDENCY_REFERENCE) {
            DependencyEntity dependencyEntity = (DependencyEntity) referenceEntity;
            String categoryName = dependencyEntity.getCategoryName();
            String ruleName = dependencyEntity.getRuleName();
            String chainName = dependencyEntity.getChainName();
            long timeout = dependencyEntity.getTimeout();

            AbstractCoroutineLauncher launcher = (AbstractCoroutineLauncher) CoroutineManager.load();

            return launcher.invokeSub(id, categoryName, ruleName, chainName, parameters, timeout);
        }

        return null;
    }

    public MonitorEntity createMonitorEntity(ExecutorType executorType, CoroutineId id, ReferenceEntity referenceEntity, String chainName) {
        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setExecutorType(executorType);
        monitorEntity.setId(id);
        monitorEntity.setReferenceEntity(referenceEntity);
        monitorEntity.setChainName(chainName);

        return monitorEntity;
    }
}