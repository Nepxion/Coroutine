package com.nepxion.coroutine.common.util;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class IOUtil {
    public static String read(String path, String encoding) throws Exception {
        String content = null;

        InputStream inputStream = null;
        try {
            // 从Resource路径获取
            inputStream = IOUtil.class.getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                // 从文件路径获取
                inputStream = new FileInputStream(path);
            }
            content = IOUtils.toString(inputStream, encoding);
        } finally {
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
        }

        return content;
    }
}