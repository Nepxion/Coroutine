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

public class ClassEntity implements Serializable {
    private static final long serialVersionUID = -3445750912195446532L;

    private String id;
    private String clazz;
    private List<MethodEntity> methodEntityList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<MethodEntity> getMethodEntityList() {
        return methodEntityList;
    }

    public void setMethodEntityList(List<MethodEntity> methodEntityList) {
        this.methodEntityList = methodEntityList;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;

        if (id != null) {
            hashCode = 37 * hashCode + id.hashCode();
        }

        if (clazz != null) {
            hashCode = 37 * hashCode + clazz.hashCode();
        }

        if (methodEntityList != null) {
            hashCode = 37 * hashCode + methodEntityList.hashCode();
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ClassEntity)) {
            return false;
        }

        ClassEntity classEntity = (ClassEntity) object;
        if (StringUtils.equals(this.id, classEntity.id)
                && StringUtils.equals(this.clazz, classEntity.clazz)
                && CollectionUtils.isEqualCollection(this.methodEntityList, classEntity.methodEntityList)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id=");
        builder.append(id);
        builder.append(", class=");
        builder.append(clazz);
        builder.append(", methodEntityList=");
        builder.append(methodEntityList);

        return builder.toString();
    }
}