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

public class ServiceId extends CoroutineId {
    private static final long serialVersionUID = -401397971609375544L;

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

        hashCode = 37 * hashCode + index;

        return hashCode;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ServiceId)) {
            return false;
        }

        ServiceId serviceId = (ServiceId) object;
        if (StringUtils.equals(this.id, serviceId.id)
                && StringUtils.equals(this.categoryName, serviceId.categoryName)
                && StringUtils.equals(this.ruleName, serviceId.ruleName)
                && this.index == serviceId.index) {
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
        builder.append(", index=");
        builder.append(index);

        return builder.toString();
    }
}