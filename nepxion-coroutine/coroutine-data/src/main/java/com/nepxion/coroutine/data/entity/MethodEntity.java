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

import com.nepxion.coroutine.common.util.ClassUtil;
import com.nepxion.coroutine.data.cache.ObjectCache;

public class MethodEntity extends ReferenceEntity {
    private static final long serialVersionUID = -7828680690422538386L;

    private String clazz;
    private String method;
    private String parameterTypes;
    private Class<?>[] parameterClasses;
    private boolean cache;

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

    public Object getObject() {
        return ObjectCache.getObject(clazz);
    }

    // 该方法在处理Bean方法的时候，必须最后执行
    public void setObject(Object object) {
        ObjectCache.putObject(clazz, object);

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
        int hashCode = 17;

        hashCode = 37 * hashCode + index;

        if (clazz != null) {
            hashCode = 37 * hashCode + clazz.hashCode();
        }

        if (method != null) {
            hashCode = 37 * hashCode + method.hashCode();
        }

        if (parameterTypes != null) {
            hashCode = 37 * hashCode + parameterTypes.hashCode();
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MethodEntity)) {
            return false;
        }

        MethodEntity methodEntity = (MethodEntity) object;
        if (this.index == methodEntity.index
                && StringUtils.equals(this.clazz, methodEntity.clazz)
                && StringUtils.equals(this.method, methodEntity.method)
                && StringUtils.equals(this.parameterTypes, methodEntity.parameterTypes)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("index=");
        builder.append(index);
        builder.append(", class=");
        builder.append(clazz);
        builder.append(", method=");
        builder.append(method);
        builder.append(", parameterTypes=");
        builder.append(parameterTypes);
        builder.append(", cache=");
        builder.append(cache);

        return builder.toString();
    }
}