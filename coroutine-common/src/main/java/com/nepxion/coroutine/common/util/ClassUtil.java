package com.nepxion.coroutine.common.util;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.coroutine.common.invocation.MethodInvocation;

public class ClassUtil {
    public static Object[] createParameters(Object parameter) {
        if (parameter instanceof Object[]) {
            return (Object[]) parameter;
        } else {
            Object[] parameters = new Object[] { parameter };

            return parameters;
        }
    }

    public static Class<?>[] createParameterClasses(Object object, String methodName) {
        if (object == null) {
            throw new IllegalArgumentException("No bean found, are your sure to set application context file correctly?");
        }

        Class<?> clazz = object.getClass();

        return createParameterClasses(clazz, methodName);
    }

    public static Class<?>[] createParameterClasses(Class<?> clazz, String methodName) {
        int count = 0;

        Class<?>[] parameterClasses = null;
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (StringUtils.equals(method.getName(), methodName)) {
                parameterClasses = method.getParameterTypes();
                count++;
            }
        }

        if (count > 1) {
            throw new IllegalArgumentException("Method [" + methodName + "] in class [" + clazz + "] is polymorphic, parameter classes can't be determined to choose");
        }

        return parameterClasses;
    }

    public static Class<?>[] createParameterClasses(String parameterTypes) throws Exception {
        String[] parameterTypesArray = StringUtils.split(parameterTypes, ",");
        Class<?>[] parameterClasses = new Class[parameterTypesArray.length];
        for (int i = 0; i < parameterTypesArray.length; i++) {
            Class<?> parameterClass = null;
            String parameterType = parameterTypesArray[i].trim();
            if (StringUtils.equals(parameterType, "byte")) {
                parameterClass = byte.class;
            } else if (StringUtils.equals(parameterType, "short")) {
                parameterClass = short.class;
            } else if (StringUtils.equals(parameterType, "int")) {
                parameterClass = int.class;
            } else if (StringUtils.equals(parameterType, "long")) {
                parameterClass = long.class;
            } else if (StringUtils.equals(parameterType, "float")) {
                parameterClass = float.class;
            } else if (StringUtils.equals(parameterType, "double")) {
                parameterClass = double.class;
            } else if (StringUtils.equals(parameterType, "char")) {
                parameterClass = char.class;
            } else if (StringUtils.equals(parameterType, "boolean")) {
                parameterClass = boolean.class;
            } else {
                parameterClass = Class.forName(parameterType);
            }
            parameterClasses[i] = parameterClass;
        }

        return parameterClasses;
    }

    public static String createParameterTypes(Class<?>[] parameterClasses) {
        if (ArrayUtils.isEmpty(parameterClasses)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Class<?> clazz : parameterClasses) {
            builder.append("," + clazz.getName());
        }

        String parameter = builder.toString().trim();
        if (parameter.length() > 0) {
            return parameter.substring(1);
        }

        return "";
    }

    @SuppressWarnings("unchecked")
    public static <T> T createInstance(String className) throws Exception {
        return (T) Class.forName(className).newInstance();
    }

    @SuppressWarnings("unchecked")
    public static <T> T createInstance(String className, Object... args) throws Exception {
        Class<?> clazz = Class.forName(className);

        Class<?>[] argsClasses = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argsClasses[i] = args[i].getClass();
        }

        Constructor<?> constructor = clazz.getConstructor(argsClasses);

        return (T) constructor.newInstance(args);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createConstructor(String className, Class<?>... parameterTypes) throws Exception {
        return (T) Class.forName(className).getConstructor(parameterTypes);
    }

    public static String createJarPath(String className) throws Exception {
        Class<?> clazz = Class.forName(className);

        return createJarPath(clazz);
    }

    public static String createJarPath(Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
    }

    public static String createJarApplicationContextPath(String className, String applicationContextPath) throws Exception {
        String jarPath = ClassUtil.createJarPath(className);
        if (!jarPath.startsWith("/")) {
            jarPath = "/" + jarPath;
        }

        if (!applicationContextPath.startsWith("/")) {
            applicationContextPath = "/" + applicationContextPath;
        }

        return "jar:file:" + jarPath + "!" + applicationContextPath;
    }

    public static String createApplicationContextPath(String applicationContextPath) throws Exception {
        return "classpath*:" + applicationContextPath;
    }

    public static Object invoke(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Exception {
        MethodInvocation invocation = new MethodInvocation();
        invocation.setMethodName(methodName);
        invocation.setParameterTypes(parameterTypes);
        invocation.setParameters(parameters);

        return invocation.invoke(object);
    }
}