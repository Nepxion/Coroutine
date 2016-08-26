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

public class PromiseLauncher extends AbstractCoroutineLauncher {
    private static final Logger LOG = LoggerFactory.getLogger(PromiseLauncher.class);

    private PromiseSerialExecutor serialExecutor = new PromiseSerialExecutor();
    private PromiseParallelExecutor parallelExecutor = new PromiseParallelExecutor();

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
                        if (lastStep) {
                            // 最后一个链式环节，需要异步回调
                            serialExecutor.finish(coroutineId, ruleEntity, stepEntity, chainName, callback);
                        } else {
                            // 中间环节调用出现异常，直接回调回去
                            serialExecutor.execute(coroutineId, ruleEntity, stepEntity, chainName, callback);
                        }
                        break;
                    case WHEN:
                        if (lastStep) {
                            // 最后一个链式环节，需要异步回调
                            parallelExecutor.finish(coroutineId, ruleEntity, stepEntity, chainName, callback);
                        } else {
                            // 中间环节调用出现异常，直接回调回去
                            parallelExecutor.execute(coroutineId, ruleEntity, stepEntity, chainName, callback);
                        }
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
        return startSync(id, categoryName, ruleName, chainName, parameters, timeout, false);
    }
}