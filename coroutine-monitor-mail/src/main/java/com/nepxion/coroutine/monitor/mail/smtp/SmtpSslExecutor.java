package com.nepxion.coroutine.monitor.mail.smtp;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Properties;

import com.sun.mail.util.MailSSLSocketFactory;

public class SmtpSslExecutor extends SmtpExecutor {

    public SmtpSslExecutor(String host, String user, String password) {
        super(host, user, password);
    }

    @Override
    protected Properties createProperties() {
        Properties properties = super.createProperties();
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        MailSSLSocketFactory sslSocketFactory = null;
        try {
            sslSocketFactory = new MailSSLSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sslSocketFactory.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.socketFactory", sslSocketFactory);

        return properties;
    }
}