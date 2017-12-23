package com.nepxion.coroutine.common.constant;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public class CoroutineConstant {
    public static final String NAMESPACE_ELEMENT_NAME = "namespace";

    public static final String COROUTINE_LAUNCHER_ELEMENT_NAME = "coroutineLauncher";

    public static final String COROUTINE_ELEMENT_NAME = "coroutine";
    public static final String PROMISE_ELEMENT_NAME = "promise";
    public static final String KILIM_ELEMENT_NAME = "kilim";

    public static final String CATEGORY_ATTRIBUTE_NAME = "category";
    public static final String RULE_ATTRIBUTE_NAME = "rule";
    public static final String FILE_ATTRIBUTE_NAME = "file";
    public static final String NAME_ATTRIBUTE_NAME = "name";
    public static final String VERSION_ATTRIBUTE_NAME = "version";
    public static final String COMPONENT_ATTRIBUTE_NAME = "component";
    public static final String APPLICATION_CONTEXT_ATTRIBUTE_NAME = "applicationContext";
    public static final String CLASS_PATH_ATTRIBUTE_NAME = "classpath";
    public static final String DEPENDENCY_ATTRIBUTE_NAME = "dependency";
    public static final String CHAIN_ATTRIBUTE_NAME = "chain";
    public static final String CLASS_ATTRIBUTE_NAME = "class";
    public static final String METHOD_ATTRIBUTE_NAME = "method";
    public static final String INDEX_ATTRIBUTE_NAME = "index";
    public static final String TIMEOUT_ATTRIBUTE_NAME = "timeout";
    public static final String CACHE_ATTRIBUTE_NAME = "cache";
    public static final String PARAMETER_TYPES_ATTRIBUTE_NAME = "parameterTypes";
    public static final String REFERENCE_TYPE_ATTRIBUTE_NAME = "referenceType";
    public static final String ID_ATTRIBUTE_NAME = "id";
    public static final String RESULT_TYPES_ATTRIBUTE_NAME = "result";

    public static final String SYNC_TIMEOUT_ATTRIBUTE_NAME = "syncTimeout";
    public static final String ASYNC_SCAN_ATTRIBUTE_NAME = "asyncScan";

    public static final String PARALLEL_AGGREGATION_COUPLING = "parallelAggregationCoupling";

    public static final String COROUTINE_THREAD_POOL_CORE_POOL_SIZE_ATTRIBUTE_NAME = "coroutineThreadPoolCorePoolSize";
    public static final String COROUTINE_THREAD_POOL_MAXIMUM_POOL_SIZE_ATTRIBUTE_NAME = "coroutineThreadPoolMaximumPoolSize";
    public static final String COROUTINE_THREAD_POOL_KEEP_ALIVE_TIME_ATTRIBUTE_NAME = "coroutineThreadPoolKeepAliveTime";
    public static final String COROUTINE_THREAD_POOL_ALLOW_CORE_THREAD_TIMEOUT_ATTRIBUTE_NAME = "coroutineThreadPoolAllowCoreThreadTimeout";

    public static final String PROMISE_THREAD_POOL_CORE_POOL_SIZE_ATTRIBUTE_NAME = "promiseThreadPoolCorePoolSize";
    public static final String PROMISE_THREAD_POOL_MAXIMUM_POOL_SIZE_ATTRIBUTE_NAME = "promiseThreadPoolMaximumPoolSize";
    public static final String PROMISE_THREAD_POOL_KEEP_ALIVE_TIME_ATTRIBUTE_NAME = "promiseThreadPoolKeepAliveTime";
    public static final String PROMISE_THREAD_POOL_ALLOW_CORE_THREAD_TIMEOUT_ATTRIBUTE_NAME = "promiseThreadPoolAllowCoreThreadTimeout";

    public static final String THREAD_POOL_REJECTED_POLICY_ATTRIBUTE_NAME = "threadPoolRejectedPolicy";
    public static final String THREAD_POOL_QUEUE_ATTRIBUTE_NAME = "threadPoolQueue";
    public static final String THREAD_POOL_QUEUE_CAPACITY_ATTRIBUTE_NAME = "threadPoolQueueCapacity";

    public static final String ZOOKEEPER_ADDRESS_ATTRIBUTE_NAME = "zookeeperAddress";
    public static final String ZOOKEEPER_SESSION_TIMOUT_ATTRIBUTE_NAME = "zookeeperSessionTimeout";
    public static final String ZOOKEEPER_CONNECT_TIMEOUT_ATTRIBUTE_NAME = "zookeeperConnectTimeout";
    public static final String ZOOKEEPER_CONNECT_WAIT_TIME_ATTRIBUTE_NAME = "zookeeperConnectWaitTime";

    public static final String EVENT_BUS = "eventBus";

    public static final String MONITOR_LOG_SUCCESS_PRINT = "monitorLogSuccessPrint";
    public static final String MONITOR_LOG_FAILURE_PRINT = "monitorLogFailurePrint";

    public static final String MONITOR_MAIL_FAILURE_SEND = "monitorMailFailureSend";

    public static final String SMTP_SSL_ATTRIBUTE_NAME = "smtpSsl";
    public static final String SMTP_HOST_ATTRIBUTE_NAME = "smtpHost";
    public static final String SMTP_USER_ATTRIBUTE_NAME = "smtpUser";
    public static final String SMTP_PASSWORD_ATTRIBUTE_NAME = "smtpPassword";
    public static final String SMTP_MAIL_FROM_ATTRIBUTE_NAME = "smtpMailFrom";
    public static final String SMTP_MAIL_TO_ATTRIBUTE_NAME = "smtpMailTo";
    public static final String SMTP_MAIL_CC_ATTRIBUTE_NAME = "smtpMailCC";
    public static final String SMTP_MAIL_BCC_ATTRIBUTE_NAME = "smtpMailBCC";

    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String ENCODING_GBK = "GBK";
    public static final String ENCODING_ISO_8859_1 = "ISO-8859-1";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final int CPUS = Math.max(2, Runtime.getRuntime().availableProcessors());
}