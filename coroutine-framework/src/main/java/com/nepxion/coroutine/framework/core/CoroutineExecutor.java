package com.nepxion.coroutine.framework.core;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.coroutine.common.delegate.CoroutineDelegate;
import com.nepxion.coroutine.data.entity.CoroutineResult;
import com.nepxion.coroutine.data.entity.ReferenceEntity;

/**
 * 调用链执行器
 */
public interface CoroutineExecutor extends CoroutineDelegate {
    /**
     * 封装本地反射调用和远程分布式(例如Dubbo接口)调用的逻辑
     * @param referenceEntity 引用实体
     * @param result          上次链式调用结果
     * @return                本次链式调用结果
     * @throws Exception
     */
    Object invoke(ReferenceEntity referenceEntity, CoroutineResult<Object> result) throws Exception;
}