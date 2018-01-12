package com.nepxion.coroutine.test.service.impl;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.coroutine.data.entity.CoroutineList;
import com.nepxion.coroutine.test.service.BService;

public class BServiceImpl implements BService {
    @Override
    public String doThen(String value) {
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        Integer.parseInt("b");
        
        return CoroutineInvoker.doThen(value, "B");
    }

    @Override
    public String doWhen(String value) {
        return CoroutineInvoker.doWhen(value, "B");
    }

    @Override
    public String doMerge(CoroutineList<Object> value) {
        return CoroutineInvoker.doMerge(value, "B");
    }
}