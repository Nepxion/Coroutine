package com.nepxion.coroutine.test;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.nepxion.coroutine.framework.core.CoroutineManager;

public class CoroutineJMeterTest extends AbstractJavaSamplerClient {
    static {
        try {
            CoroutineManager.parseLocal("PayRoute", "Rule", "rule.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Local rule parsed................");
    }
    
    @Override
    public void setupTest(JavaSamplerContext context) {

    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();
        result.sampleStart();

        try {
            CoroutineManager.load().startSync("PayRoute", "Rule", null, new String[] { "Start" }, 30000, false);
            result.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccessful(false);
        }
        result.sampleEnd();

        return result;
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {

    }
}