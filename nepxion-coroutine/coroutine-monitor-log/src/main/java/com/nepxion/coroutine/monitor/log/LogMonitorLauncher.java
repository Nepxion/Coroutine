package com.nepxion.coroutine.monitor.log;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.delegate.CoroutineDelegateImpl;
import com.nepxion.coroutine.common.util.StringUtil;
import com.nepxion.coroutine.data.entity.CoroutineId;
import com.nepxion.coroutine.data.entity.DependencyEntity;
import com.nepxion.coroutine.data.entity.ExecutorType;
import com.nepxion.coroutine.data.entity.MethodEntity;
import com.nepxion.coroutine.data.entity.ReferenceEntity;
import com.nepxion.coroutine.data.entity.ReferenceType;
import com.nepxion.coroutine.monitor.MonitorEntity;
import com.nepxion.coroutine.monitor.MonitorLauncher;

public class LogMonitorLauncher extends CoroutineDelegateImpl implements MonitorLauncher {
    private static final Logger LOG = LoggerFactory.getLogger(LogMonitorLauncher.class);

    @Override
    public void startSuccess(MonitorEntity monitorEntity) {
        boolean logPrint = properties.getBoolean(CoroutineConstants.MONITOR_LOG_SUCCESS_PRINT);
        if (logPrint) {
            ExecutorType executorType = monitorEntity.getExecutorType();
            CoroutineId id = monitorEntity.getId();
            ReferenceEntity referenceEntity = monitorEntity.getReferenceEntity();
            String chainName = monitorEntity.getChainName();
            String returnType = monitorEntity.getReturnType();
            if (StringUtils.isEmpty(returnType)) {
                returnType = "Unknown";
            }
            long startTime = monitorEntity.getStartTime();
            long endTime = monitorEntity.getEndTime();
            int index = referenceEntity.getIndex();
            ReferenceType referenceType = referenceEntity.getReferenceType();
            if (referenceType == ReferenceType.COMPONENT_REFERENCE) {
                MethodEntity methodEntity = (MethodEntity) referenceEntity;
                String clazz = methodEntity.getClazz();
                String method = methodEntity.getMethod();
                String parameterTypes = methodEntity.getParameterTypes();

                LOG.info("{} completed, referenceType={}, index={}, categoryName={}, ruleName={}, chainName={}, class={}, method={}, parameterTypes={}, returnType={}, spentTime={} ms, id={}", StringUtil.firstLetterToUpper(executorType.toString()), referenceType.toString(), index, id.getCategoryName(), id.getRuleName(), chainName, clazz, method, parameterTypes, returnType, endTime - startTime, id.getId());
            } else if (referenceType == ReferenceType.DEPENDENCY_REFERENCE) {
                DependencyEntity dependencyEntity = (DependencyEntity) referenceEntity;
                String categoryName = dependencyEntity.getCategoryName();
                String ruleName = dependencyEntity.getRuleName();

                LOG.info("{} completed, referenceType={}, index={}, categoryName={}, ruleName={}, chainName={}, returnType={}, spentTime={} ms, id={}", StringUtil.firstLetterToUpper(executorType.toString()), referenceType.toString(), index, categoryName, ruleName, chainName, returnType, endTime - startTime, id.getId());
            }
        }
    }

    @Override
    public void startFailure(MonitorEntity monitorEntity) {
        boolean logPrint = properties.getBoolean(CoroutineConstants.MONITOR_LOG_FAILURE_PRINT);
        if (logPrint) {
            ExecutorType executorType = monitorEntity.getExecutorType();
            CoroutineId id = monitorEntity.getId();
            ReferenceEntity referenceEntity = monitorEntity.getReferenceEntity();
            String chainName = monitorEntity.getChainName();
            Exception exception = monitorEntity.getException();
            long startTime = monitorEntity.getStartTime();
            long endTime = monitorEntity.getEndTime();
            int index = referenceEntity.getIndex();
            ReferenceType referenceType = referenceEntity.getReferenceType();
            if (referenceType == ReferenceType.COMPONENT_REFERENCE) {
                MethodEntity methodEntity = (MethodEntity) referenceEntity;
                String clazz = methodEntity.getClazz();
                String method = methodEntity.getMethod();
                String parameterTypes = methodEntity.getParameterTypes();

                LOG.error("{} failed, referenceType={}, index={}, chainName={}, class={}, method={}, parameterTypes={}, spentTime={} ms, id={}", StringUtil.firstLetterToUpper(executorType.toString()), referenceType.toString(), index, chainName, clazz, method, parameterTypes, endTime - startTime, id.getId(), exception);

            } else if (referenceType == ReferenceType.DEPENDENCY_REFERENCE) {
                DependencyEntity dependencyEntity = (DependencyEntity) referenceEntity;
                String categoryName = dependencyEntity.getCategoryName();
                String ruleName = dependencyEntity.getRuleName();

                LOG.error("{} failed, referenceType={}, index={}, categoryName={}, ruleName={}, chainName={}, spentTime={} ms, id={}", StringUtil.firstLetterToUpper(executorType.toString()), referenceType.toString(), index, categoryName, ruleName, chainName, endTime - startTime, id.getId(), exception);
            }
        }
    }
}