package com.nepxion.coroutine.common.property;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.nepxion.coroutine.common.util.IOUtil;

public class CoroutineContent {
    private String content;

    public CoroutineContent(String path, String encoding) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = IOUtil.getInputStream(path);
            this.content = IOUtils.toString(inputStream, encoding);
        } finally {
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }

    public CoroutineContent(File file, String encoding) throws IOException {
        this.content = FileUtils.readFileToString(file, encoding);
    }

    public CoroutineContent(StringBuilder stringBuilder) throws IOException {
        this.content = stringBuilder.toString();
    }

    public String getContent() {
        return content;
    }
}