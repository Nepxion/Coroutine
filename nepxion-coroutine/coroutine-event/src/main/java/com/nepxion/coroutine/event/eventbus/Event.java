package com.nepxion.coroutine.event.eventbus;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

public class Event {
    protected Object source;

    public Event() {
        this(null);
    }

    public Event(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}