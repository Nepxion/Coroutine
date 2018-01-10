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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.context.ApplicationContext;

import com.nepxion.coroutine.data.cache.ObjectCache;

// 并行结果的单条返回值
public class ComponentEntity implements Serializable {
    private static final long serialVersionUID = 1051640196843009890L;

    private String applicationContextPath;

    private List<ClassEntity> classEntityList;

    public String getApplicationContextPath() {
        return applicationContextPath;
    }

    public void setApplicationContextPath(String applicationContextPath) {
        this.applicationContextPath = applicationContextPath;
    }

    public ApplicationContext getApplicationContext() {
        if (StringUtils.isEmpty(applicationContextPath)) {
            return null;
        }

        return ObjectCache.getApplicationContext(applicationContextPath);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        if (StringUtils.isEmpty(applicationContextPath)) {
            return;
        }

        ObjectCache.putApplicationContext(applicationContextPath, applicationContext);
    }

    public List<ClassEntity> getClassEntityList() {
        return classEntityList;
    }

    public void setClassEntityList(List<ClassEntity> classEntityList) {
        this.classEntityList = classEntityList;
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