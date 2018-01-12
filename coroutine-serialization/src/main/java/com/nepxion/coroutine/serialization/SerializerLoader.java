package com.nepxion.coroutine.serialization;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.coroutine.common.spi.SpiLoader;

public class SerializerLoader {
    private static final Serializer SERIALIZER = SpiLoader.loadDelegate(Serializer.class);

    public static Serializer load() {
        return SERIALIZER;
    }
}