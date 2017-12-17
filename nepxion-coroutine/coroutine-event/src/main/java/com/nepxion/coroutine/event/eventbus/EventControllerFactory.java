package com.nepxion.coroutine.event.eventbus;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Map;
import java.util.concurrent.Executor;

import com.google.common.collect.Maps;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.thread.ThreadPoolFactory;

public final class EventControllerFactory {
    private static final String SINGLETON = "Singleton";
    private static final Map<Object, EventController> SYNC_CONTROLLER_MAP = Maps.newConcurrentMap();
    private static final Map<Object, EventController> ASYNC_CONTROLLER_MAP = Maps.newConcurrentMap();

    private EventControllerFactory() {

    }

    public static EventController getSingletonController(EventControllerType type) {
        return getController(SINGLETON, type);
    }

    public static EventController getController(Object id, EventControllerType type) {
        switch (type) {
            case SYNC:
                EventController syncEventController = SYNC_CONTROLLER_MAP.get(id);
                if (syncEventController == null) {
                    syncEventController = createSyncController();
                    SYNC_CONTROLLER_MAP.put(id, syncEventController);
                }

                return syncEventController;
            case ASYNC:
                EventController asyncEventController = ASYNC_CONTROLLER_MAP.get(id);
                if (asyncEventController == null) {
                    asyncEventController = createAsyncController(ThreadPoolFactory.createThreadPoolExecutor(CoroutineConstants.EVENT_BUS, 2, 4, 15 * 60 * 1000, false));
                    ASYNC_CONTROLLER_MAP.put(id, asyncEventController);
                }

                return asyncEventController;
        }

        return null;
    }

    public static EventController createSyncController() {
        return new EventControllerImpl(new EventBus());
    }

    public static EventController createSyncController(String identifier) {
        return new EventControllerImpl(new EventBus(identifier));
    }

    public static EventController createSyncController(SubscriberExceptionHandler subscriberExceptionHandler) {
        return new EventControllerImpl(new EventBus(subscriberExceptionHandler));
    }

    public static EventController createAsyncController(String identifier, Executor executor) {
        return new EventControllerImpl(new AsyncEventBus(identifier, executor));
    }

    public static EventController createAsyncController(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler) {
        return new EventControllerImpl(new AsyncEventBus(executor, subscriberExceptionHandler));
    }

    public static EventController createAsyncController(Executor executor) {
        return new EventControllerImpl(new AsyncEventBus(executor));
    }
}