package com.nepxion.coroutine.framework.core.kilim;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.callback.CoroutineCallback;
import com.nepxion.coroutine.data.entity.ChainEntity;
import com.nepxion.coroutine.data.entity.CoroutineId;
import com.nepxion.coroutine.data.entity.CoroutineResult;
import com.nepxion.coroutine.data.entity.RuleEntity;
import com.nepxion.coroutine.data.entity.StepEntity;
import com.nepxion.coroutine.data.entity.StepType;
import com.nepxion.coroutine.framework.core.AbstractCoroutineLauncher;
import com.nepxion.coroutine.framework.core.CoroutineException;

public class KilimLauncher extends AbstractCoroutineLauncher {
    private static final Logger LOG = LoggerFactory.getLogger(KilimLauncher.class);

    @Override
    protected void invoke(CoroutineId coroutineId, RuleEntity ruleEntity, String chainName, CoroutineCallback<CoroutineResult<Object>> callback) throws Exception {
        ChainEntity chainEntity = ruleEntity.getChainEntity(chainName);
        if (chainEntity == null) {
            CoroutineException e = new CoroutineException("No chain entity is found, chainName=" + chainName);
            LOG.error("Invoke failed", e);

            throw e;
        }

        List<StepEntity> stepEntityList = chainEntity.getStepEntityList();

        try {
            for (int i = 0; i < stepEntityList.size(); i++) {
                StepEntity stepEntity = stepEntityList.get(i);

                StepType stepType = stepEntity.getStepType();
                boolean lastStep = (i == stepEntityList.size() - 1);
                switch (stepType) {
                    case THEN:
                        KilimSerialExecutor serialExecutor = new KilimSerialExecutor(coroutineId, ruleEntity, stepEntity, chainName, callback, lastStep);
                        if (serialExecutor.isTerminated()) {
                            return;
                        }
                        serialExecutor.start();
                        break;
                    case WHEN:
                        KilimParallelExecutor parallelExecutor = new KilimParallelExecutor(coroutineId, ruleEntity, stepEntity, chainName, callback, lastStep);
                        if (parallelExecutor.isTerminated()) {
                            return;
                        }
                        parallelExecutor.start();
                        break;
                }
            }
        } catch (Exception e) {
            LOG.error("Execute chain failed", e);

            throw e;
        }
    }

    @Override
    protected CoroutineResult<Object> invokeSub(String id, String categoryName, String ruleName, String chainName, Object[] parameters, long timeout) throws Exception {
        throw new IllegalArgumentException("Dependency invoking in kilim framework isn't supported");
    }
}