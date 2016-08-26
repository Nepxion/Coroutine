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

// 每个链式调用的返回结果
public class CoroutineResult<T> implements Serializable {
    private static final long serialVersionUID = 5882891394862237693L;

    private CoroutineId id;
    private long timestamp;
    private T result;
    private Exception exception;

    public CoroutineId getId() {
        return id;
    }

    public void setId(CoroutineId id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;

        if (id != null) {
            hashCode = 37 * hashCode + id.hashCode();
        }

        hashCode = 37 * hashCode + (int) timestamp;

        if (result != null) {
            hashCode = 37 * hashCode + result.hashCode();
        }

        if (exception != null) {
            hashCode = 37 * hashCode + exception.hashCode();
        }

        return hashCode;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CoroutineResult)) {
            return false;
        }

        CoroutineResult<T> coroutineResult = (CoroutineResult<T>) object;
        if (this.id.equals(coroutineResult.id)
                && this.timestamp == coroutineResult.timestamp
                && this.result.equals(coroutineResult.result)
                && this.exception.equals(coroutineResult.exception)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id=");
        builder.append(id);
        builder.append(", timestamp=");
        builder.append(timestamp);
        builder.append(", result=");
        builder.append(result);
        builder.append(", exception=");
        builder.append(exception);

        return builder.toString();
    }
}