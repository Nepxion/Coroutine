package com.nepxion.coroutine.registry;

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

public class RegistryEntity implements Serializable {
    private static final long serialVersionUID = -7164716570644944474L;

    private RegistryType type;
    private String address;

    public RegistryType getType() {
        return type;
    }

    public void setType(RegistryType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;

        if (type != null) {
            hashCode = 37 * hashCode + type.hashCode();
        }

        if (address != null) {
            hashCode = 37 * hashCode + address.hashCode();
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RegistryEntity)) {
            return false;
        }

        RegistryEntity registryEntity = (RegistryEntity) object;
        if (this.type == registryEntity.type
                && StringUtils.equals(this.address, registryEntity.address)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("type=");
        builder.append(type);
        builder.append(", address=");
        builder.append(address);

        return builder.toString();
    }
}