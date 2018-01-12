package com.nepxion.coroutine.service;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;
import java.util.Map;

public interface DService {
    String doThen(String value);

    String doWhen(String value);

    String doMerge(List<Map<String, Object>> value);
}