package com.nepxion.coroutine.event;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.Serializable;
import java.util.Map;

import com.nepxion.coroutine.event.eventbus.Event;

public class RuleReconnectedEvent extends Event implements Serializable {
    private static final long serialVersionUID = -3193207206624426501L;

    private String categoryName;
    private Map<String, String> ruleMap;

    public RuleReconnectedEvent(String categoryName, Map<String, String> ruleMap) {
        this.categoryName = categoryName;
        this.ruleMap = ruleMap;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Map<String, String> getRuleMap() {
        return ruleMap;
    }
}