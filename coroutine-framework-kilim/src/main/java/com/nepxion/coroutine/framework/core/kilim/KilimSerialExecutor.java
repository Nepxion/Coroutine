package com.nepxion.coroutine.framework.core.kilim;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;

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

/**
 * Kilim 相关方法用法：
 * get()   / set()   / put()   阻塞协程，不阻塞线程
 * getb()  / setb()  / putb()  阻塞线程
 * getnb() / setnb() / setnb() 不阻塞协程，也不阻塞线程
*/
public class KilimSerialExecutor extends AbstractKilimExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(KilimSerialExecutor.class);

    private Mailbox<ReferenceEntity> mailbox;
    private Task task;

    @SuppressWarnings("unchecked")
    public KilimSerialExecutor(final CoroutineId coroutineId, RuleEntity ruleEntity, StepEntity stepEntity, final String chainName, final CoroutineCallback<CoroutineResult<Object>> callback, final boolean lastStep) {
        final CoroutineResult<Object> result = (CoroutineResult<Object>) CoroutineCache.getResult(coroutineId);
        if (result == null || CoroutineCache.hasException(result)) {
            return;
        }

        setProperties(CoroutinePropertiesManager.getProperties());

        mailbox = new Mailbox<ReferenceEntity>();

        final int[] indexes = stepEntity.getIndexes();
        for (int i = 0; i < indexes.length; i++) {
            int index = indexes[i];
            ReferenceEntity referenceEntity = ruleEntity.getReferenceEntity(index);
            if (referenceEntity == null) {
                CoroutineException e = new CoroutineException("No method/dependency entity is found, index=" + index);
                LOG.error("Invoke failed", e);

                throw e;
            }

            // 不阻塞协程，也不阻塞线程
            mailbox.putnb(referenceEntity);
        }

        task = new Task() {
            @Override
            public void execute() throws Pausable {
                int count = 0;

                while (true) {
                    // 阻塞协程
                    ReferenceEntity referenceEntity = mailbox.get();

                    MonitorEntity monitorEntity = KilimSerialExecutor.this.createMonitorEntity(ExecutorType.SERIAL, coroutineId, referenceEntity, chainName);
                    monitorEntity.setStartTime(System.currentTimeMillis());

                    // 监测外部是否有异常赋予链式调用
                    CoroutineResult<Object> coroutineResult = (CoroutineResult<Object>) CoroutineCache.getResult(result.getId());
                    if (CoroutineCache.hasException(coroutineResult)) {
                        Exception e = coroutineResult.getException();

                        monitorEntity.setException(e);
                        monitorEntity.setEndTime(System.currentTimeMillis());
                        MonitorLoader.startFailure(monitorEntity);

                        break;
                    }

                    try {
                        // 反射调用
                        Object value = KilimSerialExecutor.this.invoke(referenceEntity, result);
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

                        break;
                    }

                    count++;
                    // 链式调用结束，退出
                    if (count >= indexes.length) {
                        if (lastStep) {
                            callback.onResult(result);
                        } else {
                            CoroutineCache.setResult(result);
                        }

                        break;
                    }
                }

                // 结束协程
                Task.exit(0);
            }
        };
    }

    @Override
    public boolean isTerminated() throws Exception {
        return task == null;
    }

    @Override
    public void start() throws Exception {
        // 启动协程
        task.start();

        // 阻塞线程
        task.joinb();
    }
}