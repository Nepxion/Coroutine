package com.nepxion.coroutine.monitor.mail;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.delegate.CoroutineDelegateImpl;
import com.nepxion.coroutine.common.property.CoroutineProperties;
import com.nepxion.coroutine.common.util.ExceptionUtil;
import com.nepxion.coroutine.common.util.StringUtil;
import com.nepxion.coroutine.data.entity.CoroutineId;
import com.nepxion.coroutine.data.entity.DependencyEntity;
import com.nepxion.coroutine.data.entity.ExecutorType;
import com.nepxion.coroutine.data.entity.MethodEntity;
import com.nepxion.coroutine.data.entity.ReferenceEntity;
import com.nepxion.coroutine.data.entity.ReferenceType;
import com.nepxion.coroutine.monitor.MonitorEntity;
import com.nepxion.coroutine.monitor.MonitorLauncher;
import com.nepxion.coroutine.monitor.mail.smtp.SmtpExecutor;
import com.nepxion.coroutine.monitor.mail.smtp.SmtpSslExecutor;

public class MailMonitorLauncher extends CoroutineDelegateImpl implements MonitorLauncher {
    private static final Logger LOG = LoggerFactory.getLogger(MailMonitorLauncher.class);

    private SmtpExecutor smtpExecutor;

    @Override
    public void setProperties(CoroutineProperties properties) {
        super.setProperties(properties);

        boolean mailSend = properties.getBoolean(CoroutineConstants.MONITOR_MAIL_FAILURE_SEND);
        if (mailSend) {
            String host = properties.getString(CoroutineConstants.SMTP_HOST_ATTRIBUTE_NAME);
            String user = properties.getString(CoroutineConstants.SMTP_USER_ATTRIBUTE_NAME);
            String password = properties.getString(CoroutineConstants.SMTP_PASSWORD_ATTRIBUTE_NAME);
            boolean ssl = properties.getBoolean(CoroutineConstants.SMTP_SSL_ATTRIBUTE_NAME);
            if (ssl) {
                smtpExecutor = new SmtpSslExecutor(host, user, password);
            } else {
                smtpExecutor = new SmtpExecutor(host, user, password);
            }
        }
    }

    @Override
    public void startSuccess(MonitorEntity monitorEntity) {
        // 链式调用正常，不需要发送邮件
    }

    @Override
    public void startFailure(MonitorEntity monitorEntity) {
        if (smtpExecutor != null) {
            ExecutorType executorType = monitorEntity.getExecutorType();
            CoroutineId id = monitorEntity.getId();
            ReferenceEntity referenceEntity = monitorEntity.getReferenceEntity();
            String chainName = monitorEntity.getChainName();
            Exception exception = monitorEntity.getException();
            long startTime = monitorEntity.getStartTime();
            long endTime = monitorEntity.getEndTime();
            int index = referenceEntity.getIndex();
            ReferenceType referenceType = referenceEntity.getReferenceType();
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            if (referenceType == ReferenceType.COMPONENT_REFERENCE) {
                MethodEntity methodEntity = (MethodEntity) referenceEntity;
                String clazz = methodEntity.getClazz();
                String method = methodEntity.getMethod();
                String parameterTypes = methodEntity.getParameterTypes();
                map.put("id", id.getId());
                map.put("categoryName", id.getCategoryName());
                map.put("ruleName", id.getRuleName());
                map.put("chainName", chainName);
                map.put("executorType", StringUtil.firstLetterToUpper(executorType.toString()));
                map.put("referenceType", referenceType.toString());
                map.put("index", index);
                map.put("class", clazz);
                map.put("method", method);
                map.put("parameterTypes", parameterTypes);
                map.put("startTime", startTime);
                map.put("endTime", endTime);
                map.put("exception", ExceptionUtil.toExceptionString(exception));
            } else if (referenceType == ReferenceType.DEPENDENCY_REFERENCE) {
                DependencyEntity dependencyEntity = (DependencyEntity) referenceEntity;
                String categoryName = dependencyEntity.getCategoryName();
                String ruleName = dependencyEntity.getRuleName();

                map.put("id", id.getId());
                map.put("categoryName", categoryName);
                map.put("ruleName", ruleName);
                map.put("chainName", chainName);
                map.put("executorType", StringUtil.firstLetterToUpper(executorType.toString()));
                map.put("referenceType", referenceType.toString());
                map.put("index", index);
                map.put("startTime", startTime);
                map.put("endTime", endTime);
                map.put("exception", ExceptionUtil.toExceptionString(exception));
            }

            try {
                send(map);
            } catch (Exception e) {
                LOG.error("Send mail failed", e);
            }
        }
    }

    private void send(Map<String, Object> map) throws Exception {
        String from = properties.getString(CoroutineConstants.SMTP_MAIL_FROM_ATTRIBUTE_NAME);
        String to = properties.getString(CoroutineConstants.SMTP_MAIL_TO_ATTRIBUTE_NAME);
        String cc = properties.getString(CoroutineConstants.SMTP_MAIL_CC_ATTRIBUTE_NAME);
        String bcc = properties.getString(CoroutineConstants.SMTP_MAIL_BCC_ATTRIBUTE_NAME);
        String namespace = properties.getString(CoroutineConstants.NAMESPACE_ELEMENT_NAME);
        String subject = StringUtil.firstLetterToUpper(namespace) + "调用异常通知";

        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<style type=text/css>");
        builder.append("table{border-collapse:collapse;border-spacing:0;border-left:1px solid #888;border-top:1px solid #888;}");
        builder.append("td{border-right:1px solid #888;border-bottom:1px solid #888;padding:5px 15px;font-size:12px;}");
        builder.append("</style>");
        builder.append("</head>");
        // builder.append("<h3>" + subject + "</h3>");
        builder.append("<body>");
        builder.append("<table>");
        builder.append("<tr>");
        builder.append("<td bgcolor=#efefef>参数</td>");
        builder.append("<td bgcolor=#efefef>值</td>");
        builder.append("</tr>");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = null;
            if (entry.getValue() != null) {
                value = entry.getValue().toString().replace("\n", "<br>"); // 换行符
                if (StringUtils.equals(key, "exception")) {
                    value = value.replace(" ", "&nbsp;");
                    value = value.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;"); // tab符
                }
                value = value.trim();
            }

            builder.append("<tr>");
            builder.append("<td>");
            builder.append(key);
            builder.append("</td>");
            builder.append("<td>");
            builder.append(value);
            builder.append("</td>");
            builder.append("</tr>");
        }
        builder.append("</table>");
        builder.append("</body>");
        builder.append("</html>");

        smtpExecutor.sendHtml(from, to, cc, bcc, subject, builder.toString(), CoroutineConstants.ENCODING_FORMAT);
    }
}