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

public class CoroutineEntry<T> implements Serializable {
    private static final long serialVersionUID = -1012429028036247568L;

    private CoroutineId id;
    private ReferenceEntity referenceEntity;
    private T result;

    public CoroutineId getId() {
        return id;
    }

    public void setId(CoroutineId id) {
        this.id = id;
    }

    public ReferenceEntity getReferenceEntity() {
        return referenceEntity;
    }

    public void setReferenceEntity(ReferenceEntity referenceEntity) {
        this.referenceEntity = referenceEntity;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;

        if (id != null) {
            hashCode = 37 * hashCode + id.hashCode();
        }

        if (referenceEntity != null) {
            hashCode = 37 * hashCode + referenceEntity.hashCode();
        }

        if (result != null) {
            hashCode = 37 * hashCode + result.hashCode();
        }

        return hashCode;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CoroutineEntry)) {
            return false;
        }

        CoroutineEntry<T> coroutineEntry = (CoroutineEntry<T>) object;
        if (this.id.equals(coroutineEntry.id)
                && this.referenceEntity.equals(coroutineEntry.referenceEntity)
                && this.result.equals(coroutineEntry.result)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id=");
        builder.append(id);
        builder.append(", referenceEntity=");
        builder.append(referenceEntity);
        builder.append(", result=");
        builder.append(result);

        return builder.toString();
    }
}