package com.nepxion.coroutine.testcase.parser;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.coroutine.common.constant.CoroutineConstants;
import com.nepxion.coroutine.common.property.CoroutineContent;
import com.nepxion.coroutine.data.cache.RuleCache;
import com.nepxion.coroutine.data.entity.ChainEntity;
import com.nepxion.coroutine.data.entity.ClassEntity;
import com.nepxion.coroutine.data.entity.ComponentEntity;
import com.nepxion.coroutine.data.entity.DependencyEntity;
import com.nepxion.coroutine.data.entity.MethodEntity;
import com.nepxion.coroutine.data.entity.RuleEntity;
import com.nepxion.coroutine.data.entity.RuleKey;
import com.nepxion.coroutine.data.entity.StepEntity;
import com.nepxion.coroutine.framework.parser.RuleParser;

public class ParserTest {
    private static final Logger LOG = LoggerFactory.getLogger(ParserTest.class);

    @Test
    public void test() throws Exception {
        RuleKey ruleKey = new RuleKey();
        ruleKey.setCategoryName("PayRoute");
        ruleKey.setRuleName("Rule");

        RuleParser parser = new RuleParser(ruleKey);
        parser.parse(new CoroutineContent("rule1.xml", CoroutineConstants.ENCODING_FORMAT).getContent());

        LOG.info("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        LOG.info("<coroutine>");
        List<RuleEntity> ruleEntityList = RuleCache.getRules(ruleKey);
        for (RuleEntity ruleEntity : ruleEntityList) {
            LOG.info("    <rule version=\"{}\">", ruleEntity.getVersion());
            List<ComponentEntity> componentEntityList = ruleEntity.getComponentEntityList();
            for (ComponentEntity componentEntity : componentEntityList) {
                LOG.info("        <component applicationContext=\"{}\">", componentEntity.getApplicationContextPath());
                List<ClassEntity> classEntityList = componentEntity.getClassEntityList();
                for (ClassEntity classEntity : classEntityList) {
                    LOG.info("            <class id=\"{}\" class=\"{}\">", classEntity.getId(), classEntity.getClazz());
                    List<MethodEntity> methodEntityList = classEntity.getMethodEntityList();
                    for (MethodEntity methodEntity : methodEntityList) {
                        LOG.info("                <method index=\"{}\" method=\"{}\" parameterTypes=\"{}\" class=\"{}\"/>", methodEntity.getIndex(), methodEntity.getMethod(), methodEntity.getParameterTypes(), methodEntity.getClazz());
                    }
                    LOG.info("            </class>");
                }
                LOG.info("        </component>");
            }

            List<DependencyEntity> dependencyEntityList = ruleEntity.getDependencyEntityList();
            for (DependencyEntity dependencyEntity : dependencyEntityList) {
                LOG.info("        <dependency index=\"{}\" category=\"{}\" rule=\"{}\" timeout=\"{}\"/>", dependencyEntity.getIndex(), dependencyEntity.getCategoryName(), dependencyEntity.getRuleName(), dependencyEntity.getTimeout());
            }

            List<ChainEntity> chainEntityList = ruleEntity.getChainEntityList();
            for (ChainEntity chainEntity : chainEntityList) {
                LOG.info("        <chain name=\"{}\">", chainEntity.getName());
                for (StepEntity stepEntity : chainEntity.getStepEntityList()) {
                    LOG.info("            <{} index=\"{}\"/>", stepEntity.getStepType(), stepEntity.getIndexes());
                }
                LOG.info("        </chain>");
            }
        }
        LOG.info("</coroutine>");
    }
}