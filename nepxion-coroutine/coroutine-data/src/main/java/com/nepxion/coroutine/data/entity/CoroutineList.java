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
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

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
        int hashCode = 17;

        if (coroutineEntryList != null) {
            hashCode = 37 * hashCode + coroutineEntryList.hashCode();
        }

        return hashCode;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CoroutineList)) {
            return false;
        }

        CoroutineList<Object> coroutineList = (CoroutineList<Object>) object;
        if (CollectionUtils.isEqualCollection(this.coroutineEntryList, coroutineList.coroutineEntryList)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(", coroutineEntryList=");
        builder.append(coroutineEntryList);

        return builder.toString();
    }
}