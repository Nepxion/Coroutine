package com.nepxion.coroutine.serialization.protostuff;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.concurrent.ConcurrentMap;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.google.common.collect.Maps;
import com.nepxion.coroutine.common.delegate.CoroutineDelegateImpl;
import com.nepxion.coroutine.serialization.Serializer;

public class ProtoStuffSerializer extends CoroutineDelegateImpl implements Serializer {
    private static final Objenesis OBJENESIS = new ObjenesisStd(true);
    private static final ConcurrentMap<Class<?>, Schema<?>> SCHEMA_MAP = Maps.newConcurrentMap();
    private static final ThreadLocal<LinkedBuffer> BUFFERS = new ThreadLocal<LinkedBuffer>() {
        @Override
        protected LinkedBuffer initialValue() {
            return LinkedBuffer.allocate();
        }
    };

    @SuppressWarnings("unchecked")
    public <T> byte[] serialize(T object) {
        Schema<T> schema = getSchema((Class<T>) object.getClass());

        LinkedBuffer buffer = BUFFERS.get();
        try {
            return ProtostuffIOUtil.toByteArray(object, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = getSchema(clazz);
        T object = OBJENESIS.newInstance(clazz);

        ProtostuffIOUtil.mergeFrom(bytes, object, schema);

        return object;
    }

    @SuppressWarnings("unchecked")
    private <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) SCHEMA_MAP.get(clazz);
        if (schema == null) {
            Schema<T> newSchema = RuntimeSchema.createFrom(clazz);
            schema = (Schema<T>) SCHEMA_MAP.putIfAbsent(clazz, newSchema);
            if (schema == null) {
                schema = newSchema;
            }
        }

        return schema;
    }
}