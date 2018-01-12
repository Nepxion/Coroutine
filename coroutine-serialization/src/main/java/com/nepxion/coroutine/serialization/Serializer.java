package com.nepxion.coroutine.serialization;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.coroutine.common.delegate.CoroutineDelegate;

public interface Serializer extends CoroutineDelegate {
    <T> byte[] serialize(T object);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}