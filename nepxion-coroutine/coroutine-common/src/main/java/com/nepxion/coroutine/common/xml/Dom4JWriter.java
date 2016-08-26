package com.nepxion.coroutine.common.xml;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.nepxion.coroutine.common.constant.CoroutineConstants;

public class Dom4JWriter {
    public static Document createDocument() {
        return DocumentHelper.createDocument();
    }

    public static String getText(Document document) throws IOException, UnsupportedEncodingException {
        return getText(document, CoroutineConstants.ENCODING_FORMAT);
    }

    public static String getText(Document document, String charset) throws IOException, UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputFormat outputFormat = new OutputFormat("  ", true, charset);

        try {
            XMLWriter writer = new XMLWriter(baos, outputFormat);
            writer.write(document);

            baos.flush();
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (baos != null) {
                baos.close();
            }
        }

        return baos.toString(charset);
    }
}