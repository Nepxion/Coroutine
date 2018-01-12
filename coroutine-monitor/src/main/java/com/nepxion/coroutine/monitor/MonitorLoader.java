package com.nepxion.coroutine.monitor;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;

import com.nepxion.coroutine.common.spi.SpiLoader;

public class MonitorLoader {
    private static final List<MonitorLauncher> MONITOR_LAUNCHERS = SpiLoader.loadAllDelegates(MonitorLauncher.class);

    public static List<MonitorLauncher> load() {
        return MONITOR_LAUNCHERS;
    }

    public static void startSuccess(MonitorEntity monitorEntity) {
        for (MonitorLauncher launcher : MONITOR_LAUNCHERS) {
            launcher.startSuccess(monitorEntity);
        }
    }

    public static void startFailure(MonitorEntity monitorEntity) {
        for (MonitorLauncher launcher : MONITOR_LAUNCHERS) {
            launcher.startFailure(monitorEntity);
        }
    }
}