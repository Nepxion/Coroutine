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

public class RuleRemovedEvent extends RuleEvent {
    private static final long serialVersionUID = -3351737040522879501L;

    public RuleRemovedEvent(String categoryName, String ruleName) {
        super(categoryName, ruleName);
    }
}