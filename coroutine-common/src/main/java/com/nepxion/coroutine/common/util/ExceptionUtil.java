package com.nepxion.coroutine.common.util;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionUtil {
    public static String toExceptionString(Exception exception) {
        if (exception == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exception.printStackTrace(new PrintStream(baos));

        return baos.toString().trim();
    }
}