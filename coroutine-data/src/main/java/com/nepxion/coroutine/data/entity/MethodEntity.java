package com.nepxion.coroutine.data.entity;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.nepxion.coroutine.common.util.ClassUtil;
import com.nepxion.coroutine.data.cache.ObjectCache;

public class MethodEntity extends ReferenceEntity {
    private static final long serialVersionUID = -7828680690422538386L;

    private String classId;
    private String clazz;
    private String method;
    private String parameterTypes;
    private Class<?>[] parameterClasses;
    private boolean cache;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(String parameterTypes) {
        if (StringUtils.isEmpty(parameterTypes)) {
            throw new IllegalArgumentException("Parameter types are empty");
        }

        this.parameterTypes = parameterTypes.replace(" ", "");

        // 转化成用于反射的类型数组
        try {
            this.parameterClasses = ClassUtil.createParameterClasses(parameterTypes);
        } catch (Exception e) {
            throw new IllegalArgumentException("Create parameter classes failed", e);
        }
    }

    public Class<?>[] getParameterClasses() {
        return parameterClasses;
    }

    public String getKeyName() {
        if (StringUtils.isEmpty(clazz) && StringUtils.isEmpty(classId)) {
            throw new IllegalArgumentException("Class name and id can't all be empty");
        }

        return StringUtils.isNotEmpty(clazz) ? "class" : "classId";
    }

    public String getKey() {
        if (StringUtils.isEmpty(clazz) && StringUtils.isEmpty(classId)) {
            throw new IllegalArgumentException("Class name and id can't all be empty");
        }

        return StringUtils.isNotEmpty(clazz) ? clazz : classId;
    }

    public Object getObject() {
        String key = getKey();

        return ObjectCache.getObject(key);
    }

    // 该方法在处理Bean方法的时候，必须最后执行
    public void setObject(Object object) {
        String key = getKey();

        ObjectCache.putObject(key, object);

        // 当外部不传入parameterTypes，说明该方法不是多态，由内部反射创建parameterClasses
        if (this.parameterClasses == null) {
            this.parameterClasses = ClassUtil.createParameterClasses(object, method);
            this.parameterTypes = ClassUtil.createParameterTypes(parameterClasses);
        }
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    @Override
    public ReferenceType getReferenceType() {
        return ReferenceType.COMPONENT_REFERENCE;
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