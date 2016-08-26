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

import org.apache.commons.lang.ArrayUtils;

public class StepEntity implements Serializable {
    private static final long serialVersionUID = -3202934598824591147L;

    private int[] indexes;
    private StepType stepType;

    public int[] getIndexes() {
        return indexes;
    }

    public void setIndexes(int[] indexes) {
        this.indexes = indexes;
    }

    public StepType getStepType() {
        return stepType;
    }

    public void setStepType(StepType stepType) {
        this.stepType = stepType;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;

        hashCode = 37 * hashCode + indexes.hashCode();

        if (stepType != null) {
            hashCode = 37 * hashCode + stepType.hashCode();
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StepEntity)) {
            return false;
        }

        StepEntity stepEntity = (StepEntity) object;
        if (ArrayUtils.isEquals(this.indexes, stepEntity.indexes)
                && this.stepType == stepEntity.stepType) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("indexes=");
        builder.append(indexes);
        builder.append(", stepType=");
        builder.append(stepType);

        return builder.toString();
    }
}