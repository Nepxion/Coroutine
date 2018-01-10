package com.nepxion.coroutine.test.service;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.coroutine.data.entity.CoroutineList;

public interface DService {
    String doThen(String value);

    String doWhen(String value);

    String doMerge(CoroutineList<Object> value);
}