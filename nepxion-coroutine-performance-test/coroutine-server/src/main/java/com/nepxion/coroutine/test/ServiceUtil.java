package com.nepxion.coroutine.test;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceUtil.class);
    private static final boolean LOG_PRINT = false;
    
    public static String doThen(String value, String service) {
        if (LOG_PRINT) {
            LOG.info("串行返回结果：{}", value);
        }
        
        return value;
    }

    public static String doWhen(String value, String service) {        
        if (LOG_PRINT) {
            LOG.info("并行返回结果：{}", value);
        }
        
        return value;
    }

    public static String doMerge(List<Map<String, Object>> value, String service) {
        if (LOG_PRINT) {
            LOG.info("汇聚返回结果：{}", value);
        }
        
        return value.toString();
    }
}