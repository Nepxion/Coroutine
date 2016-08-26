package com.nepxion.coroutine.data.entity;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.Serializable;

public abstract class ReferenceEntity implements Serializable {
    private static final long serialVersionUID = 911701778667270004L;

    protected int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public abstract ReferenceType getReferenceType();
}