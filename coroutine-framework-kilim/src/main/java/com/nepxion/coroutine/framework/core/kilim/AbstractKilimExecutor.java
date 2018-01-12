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

import com.nepxion.coroutine.framework.core.AbstractCoroutineExecutor;

public abstract class AbstractKilimExecutor extends AbstractCoroutineExecutor {

    public abstract boolean isTerminated() throws Exception;

    public abstract void start() throws Exception;
}