package com.nepxion.coroutine.common.delegate;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.coroutine.common.property.CoroutineProperties;
import com.nepxion.coroutine.common.util.ClassUtil;

public class CoroutineDelegateImpl implements CoroutineDelegate {
    protected CoroutineProperties properties;

    public CoroutineDelegateImpl() {

    }

    @Override
    public CoroutineProperties getProperties() {
        return properties;
    }

    @Override
    public void setProperties(CoroutineProperties properties) {
        this.properties = properties;
    }

    @Override
    public <T> T createDelegate(String delegateClassId) throws Exception {
        String delegateClassName = properties.getString(delegateClassId);

        T delegateInstance = ClassUtil.createInstance(delegateClassName);

        CoroutineDelegate delegate = (CoroutineDelegate) delegateInstance;
        delegate.setProperties(properties);

        return delegateInstance;
    }
}