package com.nepxion.coroutine.service.impl;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;
import java.util.Map;

import com.nepxion.coroutine.service.DService;

public class DServiceImpl implements DService {
    @Override
    public String doThen(String value) {
        return CoroutineInvoker.doThen(value, "D");
    }

    @Override
    public String doWhen(String value) {
        return CoroutineInvoker.doWhen(value, "D");
    }

    @Override
    public String doMerge(List<Map<String, Object>> value) {
        return CoroutineInvoker.doMerge(value, "D");
    }
}