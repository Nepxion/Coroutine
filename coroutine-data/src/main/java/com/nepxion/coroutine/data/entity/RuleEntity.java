package com.nepxion.coroutine.data.entity;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RuleEntity implements Serializable {
    private static final long serialVersionUID = -966938499277118469L;

    private int version;

    private List<ComponentEntity> componentEntityList;
    private List<DependencyEntity> dependencyEntityList;
    private List<ChainEntity> chainEntityList;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<ComponentEntity> getComponentEntityList() {
        return componentEntityList;
    }

    public void setComponentEntityList(List<ComponentEntity> componentEntityList) {
        this.componentEntityList = componentEntityList;
    }

    public List<DependencyEntity> getDependencyEntityList() {
        return dependencyEntityList;
    }

    public void setDependencyEntityList(List<DependencyEntity> dependencyEntityList) {
        this.dependencyEntityList = dependencyEntityList;
    }

    public List<ChainEntity> getChainEntityList() {
        return chainEntityList;
    }

    public void setChainEntityList(List<ChainEntity> chainEntityList) {
        this.chainEntityList = chainEntityList;
    }

    public ReferenceEntity getReferenceEntity(int index) {
        for (ComponentEntity componentEntity : componentEntityList) {
            List<ClassEntity> classEntityList = componentEntity.getClassEntityList();
            for (ClassEntity classEntity : classEntityList) {
                List<MethodEntity> methodEntityList = classEntity.getMethodEntityList();
                for (MethodEntity methodEntity : methodEntityList) {
                    if (methodEntity.getIndex() == index) {
                        return methodEntity;
                    }
                }
            }
        }

        for (DependencyEntity dependencyEntity : dependencyEntityList) {
            if (dependencyEntity.getIndex() == index) {
                return dependencyEntity;
            }
        }

        return null;
    }

    public ChainEntity getChainEntity(String name) {
        for (ChainEntity chainEntity : chainEntityList) {
            String chainName = chainEntity.getName();
            if (StringUtils.equals(chainName, name)) {
                return chainEntity;
            }
        }

        return null;
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