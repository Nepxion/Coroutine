package com.nepxion.coroutine.monitor;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.coroutine.common.delegate.CoroutineDelegate;

public interface MonitorLauncher extends CoroutineDelegate {

    // 执行调用成功的监控
    void startSuccess(MonitorEntity monitorEntity);

    // 执行调用失败的监控
    void startFailure(MonitorEntity monitorEntity);
}