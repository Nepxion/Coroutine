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
        int hashCode = 17;

        if (componentEntityList != null) {
            hashCode = 37 * hashCode + componentEntityList.hashCode();
        }

        if (dependencyEntityList != null) {
            hashCode = 37 * hashCode + dependencyEntityList.hashCode();
        }

        if (chainEntityList != null) {
            hashCode = 37 * hashCode + chainEntityList.hashCode();
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RuleEntity)) {
            return false;
        }

        RuleEntity ruleEntity = (RuleEntity) object;
        if (CollectionUtils.isEqualCollection(this.componentEntityList, ruleEntity.componentEntityList)
                && CollectionUtils.isEqualCollection(this.dependencyEntityList, ruleEntity.dependencyEntityList)
                && CollectionUtils.isEqualCollection(this.chainEntityList, ruleEntity.chainEntityList)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("componentList=");
        builder.append(componentEntityList);
        builder.append(", dependencyList=");
        builder.append(dependencyEntityList);
        builder.append(", chainEntityList=");
        builder.append(chainEntityList);

        return builder.toString();
    }
}