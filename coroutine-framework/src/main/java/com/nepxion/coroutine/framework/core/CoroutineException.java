package com.nepxion.coroutine.framework.core;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

public class CoroutineException extends RuntimeException {
    private static final long serialVersionUID = -6478265600135929954L;

    public CoroutineException() {
        super();
    }

    public CoroutineException(String message) {
        super(message);
    }

    public CoroutineException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoroutineException(Throwable cause) {
        super(cause);
    }
}