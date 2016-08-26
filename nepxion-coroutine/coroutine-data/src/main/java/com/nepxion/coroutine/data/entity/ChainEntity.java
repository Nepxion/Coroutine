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

public class ChainEntity implements Serializable {
    private static final long serialVersionUID = 7084094152372962217L;

    private String name;
    private List<StepEntity> stepEntityList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public List<StepEntity> getStepEntityList() {
        return stepEntityList;
    }

    public void setStepEntityList(List<StepEntity> stepEntityList) {
        this.stepEntityList = stepEntityList;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;

        if (name != null) {
            hashCode = 37 * hashCode + name.hashCode();
        }
        
        if (stepEntityList != null) {
            hashCode = 37 * hashCode + stepEntityList.hashCode();
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ChainEntity)) {
            return false;
        }

        ChainEntity chainEntity = (ChainEntity) object;
        if (StringUtils.equals(this.name, chainEntity.name) 
                && CollectionUtils.isEqualCollection(this.stepEntityList, chainEntity.stepEntityList)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("stepEntityList=");
        builder.append(stepEntityList);

        return builder.toString();
    }
}