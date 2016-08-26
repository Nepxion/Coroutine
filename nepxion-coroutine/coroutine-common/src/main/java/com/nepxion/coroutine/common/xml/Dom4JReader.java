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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import com.nepxion.coroutine.common.constant.CoroutineConstants;

public class Dom4JReader {
    public static Document getDocument(String text) throws DocumentException {
        return DocumentHelper.parseText(text);
    }

    public static Document getFormatDocument(String text) throws DocumentException, UnsupportedEncodingException {
        return getFormatDocument(text, CoroutineConstants.ENCODING_FORMAT);
    }

    public static Document getFormatDocument(String text, String charset) throws DocumentException, UnsupportedEncodingException {
        String formatText = new String(text.getBytes("ISO-8859-1"), CoroutineConstants.ENCODING_FORMAT);

        return getDocument(formatText);
    }

    public static Document getDocument(File file) throws DocumentException, IOException {
        InputStream inputStream = new FileInputStream(file);

        return getDocument(inputStream);
    }

    public static Document getFormatDocument(File file) throws DocumentException, IOException, UnsupportedEncodingException {
        return getFormatDocument(file, CoroutineConstants.ENCODING_FORMAT);
    }

    public static Document getFormatDocument(File file, String charset) throws DocumentException, IOException, UnsupportedEncodingException {
        InputStream inputStream = new FileInputStream(file);

        return getFormatDocument(inputStream, charset);
    }

    public static Document getDocument(InputSource inputSource) throws DocumentException {
        SAXReader saxReader = new SAXReader();

        return saxReader.read(inputSource);
    }

    public static Document getFormatDocument(InputSource inputSource) throws DocumentException {
        return getFormatDocument(inputSource, CoroutineConstants.ENCODING_FORMAT);
    }

    public static Document getFormatDocument(InputSource inputSource, String charset) throws DocumentException {
        inputSource.setEncoding(charset);

        return getDocument(inputSource);
    }

    public static Document getDocument(InputStream inputStream) throws DocumentException, IOException {
        SAXReader saxReader = new SAXReader();

        Document document = null;
        try {
            document = saxReader.read(inputStream);
        } catch (DocumentException e) {
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return document;
    }

    public static Document getFormatDocument(InputStream inputStream) throws DocumentException, IOException, UnsupportedEncodingException {
        return getFormatDocument(inputStream, CoroutineConstants.ENCODING_FORMAT);
    }

    public static Document getFormatDocument(InputStream inputStream, String charset) throws DocumentException, IOException, UnsupportedEncodingException {
        Reader inputStreamReader = new InputStreamReader(inputStream, charset);

        return getDocument(inputStreamReader);
    }

    public static Document getDocument(Reader reader) throws DocumentException, IOException {
        SAXReader saxReader = new SAXReader();

        Document document = null;
        try {
            document = saxReader.read(reader);
        } catch (DocumentException e) {
            throw e;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return document;
    }

    public static Document getDocument(URL url) throws DocumentException {
        SAXReader saxReader = new SAXReader();

        return saxReader.read(url);
    }
}