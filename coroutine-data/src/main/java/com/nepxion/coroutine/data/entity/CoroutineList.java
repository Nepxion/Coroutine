package com.nepxion.coroutine.data.entity;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CoroutineList<T> implements Serializable {
    private static final long serialVersionUID = 1859680578996231396L;

    private List<CoroutineEntry<T>> coroutineEntryList;

    public List<CoroutineEntry<T>> getCoroutineEntryList() {
        return coroutineEntryList;
    }

    public void setCoroutineEntryList(List<CoroutineEntry<T>> coroutineEntryList) {
        this.coroutineEntryList = coroutineEntryList;
    }

    public CoroutineEntry<T> get(int index) {
        for (CoroutineEntry<T> coroutineEntry : coroutineEntryList) {
            if (coroutineEntry.getReferenceEntity().getIndex() == index) {
                return coroutineEntry;
            }
        }

        return null;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(this, object);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}