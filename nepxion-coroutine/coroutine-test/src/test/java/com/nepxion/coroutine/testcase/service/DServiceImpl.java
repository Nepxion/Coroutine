package com.nepxion.coroutine.testcase.service;

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

public class DServiceImpl implements DService {
    @Override
    public String doThen(String value) {
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        Integer.parseInt("c");
        
        return ServiceUtil.doThen(value, "D");
    }

    @Override
    public String doWhen(String value) {
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        Integer.parseInt("c");

        return ServiceUtil.doWhen(value, "D");
    }

    @Override
    public String doMerge(CoroutineList<Object> value) {
        return ServiceUtil.doMerge(value, "D");
    }
}