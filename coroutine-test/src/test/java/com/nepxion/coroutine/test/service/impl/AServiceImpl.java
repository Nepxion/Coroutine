package com.nepxion.coroutine.test.service.impl;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.coroutine.data.entity.CoroutineList;
import com.nepxion.coroutine.test.service.AService;

public class AServiceImpl implements AService {
    @Override
    public String doThen(String value) {
        return CoroutineInvoker.doThen(value, "A");
    }

    @Override
    public String doWhen(String value) {
        return CoroutineInvoker.doWhen(value, "A");
    }

    @Override
    public String doMerge(CoroutineList<Object> value) {
        return CoroutineInvoker.doMerge(value, "A");
    }
}