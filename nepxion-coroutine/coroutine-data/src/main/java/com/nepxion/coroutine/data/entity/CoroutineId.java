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

import org.apache.commons.lang3.StringUtils;

public class CoroutineId extends RuleKey {
    private static final long serialVersionUID = -6897462807357937528L;

    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;

        if (id != null) {
            hashCode = 37 * hashCode + id.hashCode();
        }

        if (categoryName != null) {
            hashCode = 37 * hashCode + categoryName.hashCode();
        }

        if (ruleName != null) {
            hashCode = 37 * hashCode + ruleName.hashCode();
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CoroutineId)) {
            return false;
        }

        CoroutineId coroutineKey = (CoroutineId) object;
        if (StringUtils.equals(this.id, coroutineKey.id)
                && StringUtils.equals(this.categoryName, coroutineKey.categoryName)
                && StringUtils.equals(this.ruleName, coroutineKey.ruleName)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id=");
        builder.append(id);
        builder.append(", categoryName=");
        builder.append(categoryName);
        builder.append(", ruleName=");
        builder.append(ruleName);

        return builder.toString();
    }
}