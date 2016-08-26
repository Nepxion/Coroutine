package com.nepxion.coroutine.common.spi;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.delegate.CoroutineDelegate;
import com.nepxion.coroutine.common.property.CoroutineProperties;
import com.nepxion.coroutine.common.property.CoroutinePropertiesManager;

public final class SpiLoader {
    private static final Logger LOG = LoggerFactory.getLogger(SpiLoader.class);
    
    public static <S> List<S> loadAll(Class<S> serviceClass) {
        List<S> services = new ArrayList<S>();
        
        Iterator<S> iterator = ServiceLoader.load(serviceClass).iterator();
        while (iterator.hasNext()) {
            S service = iterator.next();
            
            LOG.info("SPI loaded - interface={}, implementation={}", serviceClass.getName(), service.getClass().getName());
            
            services.add(service);
        }
        
        if (CollectionUtils.isEmpty(services)) {
            String error = "It can't be retrieved any SPI implementations for " + serviceClass.getName();
            LOG.error(error);
            throw new IllegalArgumentException(error);
        }
        
        return services;
    }
    
    public static <S> S load(Class<S> serviceClass) {
        Iterator<S> iterator = ServiceLoader.load(serviceClass).iterator();
        if (iterator.hasNext()) {
            S service = iterator.next();
            
            LOG.info("SPI loaded - interface={}, implementation={}", serviceClass.getName(), service.getClass().getName());
            
            return service;
        }
        
        String error = "It can't be retrieved SPI implementation for " + serviceClass.getName();
        LOG.error(error);
        throw new IllegalArgumentException(error);
    }
    
    public static <S> S load(Class<S> serviceClass, String serviceImplClassName) {
        Iterator<S> iterator = ServiceLoader.load(serviceClass).iterator();
        while (iterator.hasNext()) {
            S service = iterator.next();
            if (service.getClass().getName().endsWith(serviceImplClassName.trim())) {
                LOG.info("SPI loaded - interface={}, implementation={}", serviceClass.getName(), service.getClass().getName());
                
                return service;
            }
        }
        
        String error = "It can't be retrieved SPI implementation for " + serviceClass.getName();
        LOG.error(error);
        throw new IllegalArgumentException(error);
    }
    
    public static <S> S loadFromProperties(Class<S> serviceClass, String serviceKey) {
        CoroutineProperties properties = CoroutinePropertiesManager.getProperties();
        String serviceImplClassName = properties.getString(serviceKey);
        
        return load(serviceClass, serviceImplClassName);
    }
    
    
    public static <S> List<S> loadAllDelegates(Class<S> serviceClass) {
        List<S> services = loadAll(serviceClass);
        for (S service : services) {
            if (service != null && service instanceof CoroutineDelegate) {
                CoroutineDelegate delegate = (CoroutineDelegate) service;
                delegate.setProperties(CoroutinePropertiesManager.getProperties());
            } 
        }
        
        return services;
    }
    
    public static <S> S loadDelegate(Class<S> serviceClass) {
        S service = load(serviceClass);
        if (service != null && service instanceof CoroutineDelegate) {
            CoroutineDelegate delegate = (CoroutineDelegate) service;
            delegate.setProperties(CoroutinePropertiesManager.getProperties());
        }
        
        return service;
    }
    
    public static <S> S loadDelegate(Class<S> serviceClass, String serviceImplClassName) {
        S service = load(serviceClass, serviceImplClassName);
        if (service != null && service instanceof CoroutineDelegate) {
            CoroutineDelegate delegate = (CoroutineDelegate) service;
            delegate.setProperties(CoroutinePropertiesManager.getProperties());
        }
        
        return service;
    }
    
    public static <S> S loadDelegateFromProperties(Class<S> serviceClass, String serviceKey) {
        CoroutineProperties properties = CoroutinePropertiesManager.getProperties();
        String serviceImplClassName = properties.getString(serviceKey);
        
        return loadDelegate(serviceClass, serviceImplClassName);
    }
}