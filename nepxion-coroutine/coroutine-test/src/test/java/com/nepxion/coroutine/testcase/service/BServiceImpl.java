package com.nepxion.coroutine.testcase.service;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.coroutine.data.entity.CoroutineList;

public class BServiceImpl implements BService {
    @Override
    public String doThen(String value) {
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        
//        Integer.parseInt("b");
        
        return ServiceUtil.doThen(value, "B");
    }

    @Override
    public String doWhen(String value) {
        return ServiceUtil.doWhen(value, "B");
    }

    @Override
    public String doMerge(CoroutineList<Object> value) {
        return ServiceUtil.doMerge(value, "B");
    }
}