package com.nepxion.coroutine.event;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

public class RuleAddedEvent extends RuleEvent {
    private static final long serialVersionUID = 3105014385246553706L;

    private String ruleContent;

    public RuleAddedEvent(String categoryName, String ruleName, String ruleContent) {
        super(categoryName, ruleName);

        this.ruleContent = ruleContent;
    }

    public String getRuleContent() {
        return ruleContent;
    }
}