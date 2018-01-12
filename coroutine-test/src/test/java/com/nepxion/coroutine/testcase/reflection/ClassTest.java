package com.nepxion.coroutine.testcase.reflection;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.util.ClassUtil;

public class ClassTest {
    private static final Logger LOG = LoggerFactory.getLogger(ClassTest.class);

    @Test
    public void testParameterClasses() throws Exception {
        Class<?>[] parameterClasses = ClassUtil.createParameterClasses("java.lang.Void,java.lang.String,int,[Ljava.lang.String;,[B,[S,[I,[J,[F,[D,[C,[Z");
        for (Class<?> parameterClass : parameterClasses) {
            LOG.info("parameterClass : {}", parameterClass);
        }

        LOG.info("byte[] class : {}", byte[].class);
        LOG.info("short[] class : {}", short[].class);
        LOG.info("int[] class : {}", int[].class);
        LOG.info("long[] class : {}", long[].class);
        LOG.info("float[] class : {}", float[].class);
        LOG.info("double[] class : {}", double[].class);
        LOG.info("char[] class : {}", char[].class);
        LOG.info("boolean[] class : {}", boolean[].class);
    }

    @Test
    public void testJarPath() throws Exception {
        String path1 = ClassUtil.createJarPath(StringUtils.class);
        LOG.info("Path1 : {}", path1);

        String path2 = ClassUtil.createJarPath("org.apache.curator.framework.CuratorFramework");
        LOG.info("Path2 : {}", path2);

        String path3 = ClassUtil.createJarApplicationContextPath("org.apache.curator.framework.CuratorFramework", "a/b/applicationContext.xml");
        LOG.info("Path3 : {}", path3);
    }
}