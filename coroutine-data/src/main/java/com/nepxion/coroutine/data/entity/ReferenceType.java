package com.nepxion.coroutine.data.entity;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public enum ReferenceType {
    COMPONENT_REFERENCE("componentReference"),
    DEPENDENCY_REFERENCE("dependencyReference");

    private String value;

    private ReferenceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ReferenceType fromString(String value) {
        for (ReferenceType type : ReferenceType.values()) {
            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Mismatched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}