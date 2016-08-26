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

import org.apache.commons.lang3.StringUtils;

public class RuleKey implements Serializable {
    private static final long serialVersionUID = 6378628183086911005L;

    protected String categoryName;
    protected String ruleName;

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

    @Override
    public int hashCode() {
        int hashCode = 17;

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
        if (!(object instanceof RuleKey)) {
            return false;
        }

        RuleKey ruleKey = (RuleKey) object;
        if (StringUtils.equals(this.categoryName, ruleKey.categoryName)
                && StringUtils.equals(this.ruleName, ruleKey.ruleName)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("categoryName=");
        builder.append(categoryName);
        builder.append(", ruleName=");
        builder.append(ruleName);

        return builder.toString();
    }
}