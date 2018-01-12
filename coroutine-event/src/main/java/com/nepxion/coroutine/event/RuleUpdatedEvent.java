package com.nepxion.coroutine.event;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public class RuleUpdatedEvent extends RuleEvent {
    private static final long serialVersionUID = 2259834088222766682L;

    private String ruleContent;

    public RuleUpdatedEvent(String categoryName, String ruleName, String ruleContent) {
        super(categoryName, ruleName);

        this.ruleContent = ruleContent;
    }

    public String getRuleContent() {
        return ruleContent;
    }
}