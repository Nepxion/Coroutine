package com.nepxion.coroutine.registry;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.coroutine.common.spi.SpiLoader;

public class RegistryLoader {
    private static final RegistryLauncher REGISTRY_LAUNCHER = SpiLoader.loadDelegate(RegistryLauncher.class);

    public static RegistryLauncher load() {
        return REGISTRY_LAUNCHER;
    }
}