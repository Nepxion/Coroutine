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
import org.apache.commons.lang3.StringUtils;
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
        int hashCode = 17;

        if (applicationContextPath != null) {
            hashCode = 37 * hashCode + applicationContextPath.hashCode();
        }

        if (classEntityList != null) {
            hashCode = 37 * hashCode + classEntityList.hashCode();
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ComponentEntity)) {
            return false;
        }

        ComponentEntity componentEntity = (ComponentEntity) object;
        if (StringUtils.equals(this.applicationContextPath, componentEntity.applicationContextPath)
                && CollectionUtils.isEqualCollection(this.classEntityList, componentEntity.classEntityList)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("applicationContextPath=");
        builder.append(applicationContextPath);
        builder.append(", classEntityList=");
        builder.append(classEntityList);

        return builder.toString();
    }
}