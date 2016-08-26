package com.nepxion.coroutine.framework.core;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.callback.CoroutineCallback;
import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.delegate.CoroutineDelegateImpl;
import com.nepxion.coroutine.common.util.RandomUtil;
import com.nepxion.coroutine.data.cache.CoroutineCache;
import com.nepxion.coroutine.data.cache.RuleCache;
import com.nepxion.coroutine.data.cache.ServiceCache;
import com.nepxion.coroutine.data.entity.CoroutineId;
import com.nepxion.coroutine.data.entity.CoroutineResult;
import com.nepxion.coroutine.data.entity.RuleEntity;
import com.nepxion.coroutine.data.entity.RuleKey;
import com.nepxion.coroutine.framework.thread.CoroutineThreadPoolFactory;

public abstract class AbstractCoroutineLauncher extends CoroutineDelegateImpl implements CoroutineLauncher {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCoroutineLauncher.class);

    // 异步调用
    @Override
    public CoroutineId startAsync(String categoryName, String ruleName, String chainName, Object[] parameters, boolean last, CoroutineCallback<CoroutineResult<Object>> callback) {
        String id = RandomUtil.uuidRandom();

        return startAsync(id, categoryName, ruleName, chainName, parameters, last, callback);
    }

    // 异步调用
    @Override
    public CoroutineId startAsync(final String id, final String categoryName, final String ruleName, final String chainName, final Object[] parameters, final boolean last, final CoroutineCallback<CoroutineResult<Object>> callback) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("Id can't be null or empty");
        }

        if (StringUtils.isEmpty(categoryName)) {
            throw new IllegalArgumentException("Category name can't be null or empty");
        }

        if (StringUtils.isEmpty(ruleName)) {
            throw new IllegalArgumentException("Rule name can't be null or empty");
        }

        // 参数可以空数组，但不能为null
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        }

        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null");
        }

        final CoroutineId coroutineId = new CoroutineId();
        coroutineId.setId(id);
        coroutineId.setCategoryName(categoryName);
        coroutineId.setRuleName(ruleName);

        CoroutineThreadPoolFactory.getThreadPoolExecutor().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // 构造调用初始值
                CoroutineResult<Object[]> origin = new CoroutineResult<Object[]>();
                origin.setId(coroutineId);
                origin.setResult(parameters);

                CoroutineCache.setResult(origin);

                // 获取规则数据
                RuleKey ruleKey = new RuleKey();
                ruleKey.setCategoryName(categoryName);
                ruleKey.setRuleName(ruleName);

                RuleEntity ruleEntity;
                try {
                    ruleEntity = RuleCache.getLatestRule(ruleKey);
                } catch (Exception e) {
                    LOG.error("Rule is retrieved failed, are you sure to load rules correctly?", e);

                    throw e;
                }

                try {
                    invoke(coroutineId, ruleEntity, chainName, callback);
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (last) {
                        ServiceCache.clear(id);
                    }
                }

                return null;
            }
        });

        return coroutineId;
    }

    // 同步调用
    @Override
    public CoroutineResult<Object> startSync(String categoryName, String ruleName, String chainName, Object[] parameters, long timeout, boolean last) throws Exception {
        String id = RandomUtil.uuidRandom();

        return startSync(id, categoryName, ruleName, chainName, parameters, timeout, last);
    }

    // 同步调用
    @Override
    public CoroutineResult<Object> startSync(String id, String categoryName, String ruleName, String chainName, Object[] parameters, long timeout, boolean last) throws Exception {
        if (timeout <= 0) {
            LOG.warn("Sync timeout is invalid, use default value={}", properties.getLong(CoroutineConstants.SYNC_TIMEOUT_ATTRIBUTE_NAME));
        }

        return invokeSync(id, categoryName, ruleName, chainName, parameters, timeout, last);
    }

    private CoroutineResult<Object> invokeSync(String id, String categoryName, String ruleName, String chainName, Object[] parameters, long timeout, boolean last) throws Exception {
        final CyclicBarrier barrier = new CyclicBarrier(2);

        final CoroutineResult<Object> coroutineResult = new CoroutineResult<Object>();

        CoroutineCallback<CoroutineResult<Object>> callback = new CoroutineCallback<CoroutineResult<Object>>() {
            @Override
            public void onResult(CoroutineResult<Object> result) {
                coroutineResult.setId(result.getId());
                coroutineResult.setResult(result.getResult());

                try {
                    barrier.await();
                } catch (Exception e) {
                    LOG.error("Coroutine callback for onResult is timeout");
                }
            }

            @Override
            public void onError(Exception exception) {
                coroutineResult.setException(exception);

                try {
                    barrier.await();
                } catch (Exception e) {
                    LOG.error("Coroutine callback for onError is timeout");
                }
            }
        };

        startAsync(id, categoryName, ruleName, chainName, parameters, last, callback);

        try {
            barrier.await(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            // 在超时发生时，通知链式调用终止下一个环节
            coroutineResult.setException(e);

            CoroutineId coroutineId = new CoroutineId();
            coroutineId.setId(id);
            coroutineId.setCategoryName(categoryName);
            coroutineId.setRuleName(ruleName);
            coroutineResult.setId(coroutineId);

            CoroutineCache.setResult(coroutineResult);

            throw e;
        }

        if (coroutineResult.getException() != null) {
            throw coroutineResult.getException();
        }

        return coroutineResult;
    }

    // 启动链式调用
    protected abstract void invoke(CoroutineId coroutineId, RuleEntity ruleEntity, String chainName, CoroutineCallback<CoroutineResult<Object>> callback) throws Exception;
    
    // 启动子链式调用
    protected abstract CoroutineResult<Object> invokeSub(String id, String categoryName, String ruleName, String chainName, Object[] parameters, long timeout) throws Exception;
}