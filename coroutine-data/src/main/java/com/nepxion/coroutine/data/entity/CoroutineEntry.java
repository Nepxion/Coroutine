package com.nepxion.coroutine.data.entity;

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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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