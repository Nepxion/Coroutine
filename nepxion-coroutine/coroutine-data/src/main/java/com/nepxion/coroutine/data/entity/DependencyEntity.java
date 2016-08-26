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

// 依赖外部的子规则
public class DependencyEntity extends ReferenceEntity {
    private static final long serialVersionUID = 1294359201200458912L;

    private String categoryName;
    private String ruleName;
    private String chainName;
    private long timeout;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    
    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public ReferenceType getReferenceType() {
        return ReferenceType.DEPENDENCY_REFERENCE;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;

        hashCode = 37 * hashCode + index;

        if (categoryName != null) {
            hashCode = 37 * hashCode + categoryName.hashCode();
        }

        if (ruleName != null) {
            hashCode = 37 * hashCode + ruleName.hashCode();
        }
        
        if (chainName != null) {
            hashCode = 37 * hashCode + chainName.hashCode();
        }

        hashCode = 37 * hashCode + String.valueOf(timeout).hashCode();

        return hashCode;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DependencyEntity)) {
            return false;
        }

        DependencyEntity dependencyEntity = (DependencyEntity) object;
        if (this.index == dependencyEntity.index
                && StringUtils.equals(this.categoryName, dependencyEntity.categoryName)
                && StringUtils.equals(this.ruleName, dependencyEntity.ruleName)
                && StringUtils.equals(this.chainName, dependencyEntity.chainName)
                && this.timeout == dependencyEntity.timeout) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("index=");
        builder.append(index);
        builder.append(", categoryName=");
        builder.append(categoryName);
        builder.append(", ruleName=");
        builder.append(ruleName);
        builder.append(", chainName=");
        builder.append(chainName);
        builder.append(", timeout=");
        builder.append(timeout);

        return builder.toString();
    }
}