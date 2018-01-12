package com.nepxion.coroutine;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@EnableAutoConfiguration
@ImportResource({ "classpath*:dubbo-server-context-coroutine.xml" })
public class DubboServerApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(DubboServerApplication.class, args);
    }
}