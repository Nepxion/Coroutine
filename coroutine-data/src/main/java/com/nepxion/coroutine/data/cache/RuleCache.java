package com.nepxion.coroutine.data.cache;

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
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.Maps;
import com.nepxion.coroutine.data.entity.RuleEntity;
import com.nepxion.coroutine.data.entity.RuleKey;

// 缓存规则解析结果
public class RuleCache {
    private static final ConcurrentMap<RuleKey, List<RuleEntity>> RULE_MAP = Maps.newConcurrentMap();
    private static final byte[] LOCK = new byte[0];

    public static void putRules(RuleKey ruleKey, List<RuleEntity> ruleEntityList) {
        synchronized (LOCK) {
            RULE_MAP.put(ruleKey, ruleEntityList);
        }
    }

    public static void putRule(RuleKey ruleKey, RuleEntity ruleEntity) {
        synchronized (LOCK) {
            List<RuleEntity> ruleEntityList = RULE_MAP.get(ruleKey);
            if (CollectionUtils.isEmpty(ruleEntityList)) {
                throw new IllegalArgumentException("Rule entity list doesn't exist, categoryName=" + ruleKey.getCategoryName() + ", ruleName=" + ruleKey.getRuleName());
            }

            RuleEntity latestRuleEntity = getLatestRule(ruleKey);
            if (ruleEntity.getVersion() <= latestRuleEntity.getVersion()) {
                throw new IllegalArgumentException("The version of new rule entity is <= existed rules, categoryName=" + ruleKey.getCategoryName() + ", ruleName=" + ruleKey.getRuleName());
            }

            ruleEntityList.add(ruleEntity);
        }
    }

    public static void clearExpiredRule(RuleKey ruleKey) {
        synchronized (LOCK) {
            RuleEntity latestRuleEntity = getLatestRule(ruleKey);

            List<RuleEntity> ruleEntityList = RULE_MAP.get(ruleKey);
            ruleEntityList.clear();
            ruleEntityList.add(latestRuleEntity);
        }
    }

    public static RuleEntity getLatestRule(RuleKey ruleKey) {
        List<RuleEntity> ruleEntityList = RULE_MAP.get(ruleKey);
        if (CollectionUtils.isEmpty(ruleEntityList)) {
            throw new IllegalArgumentException("Rule entity list doesn't exist, categoryName=" + ruleKey.getCategoryName() + ", ruleName=" + ruleKey.getRuleName());
        }

        RuleEntity entity = null;
        int version = -1;
        for (RuleEntity ruleEntity : ruleEntityList) {
            if (ruleEntity.getVersion() > version) {
                version = ruleEntity.getVersion();
                entity = ruleEntity;
            }
        }

        return entity;
    }

    public static boolean isRuleExisted(RuleKey ruleKey, int version) {
        List<RuleEntity> ruleEntityList = RULE_MAP.get(ruleKey);
        for (RuleEntity ruleEntity : ruleEntityList) {
            if (ruleEntity.getVersion() == version) {
                return true;
            }
        }

        return false;
    }

    public static List<RuleEntity> getRules(RuleKey ruleKey) {
        return RULE_MAP.get(ruleKey);
    }

    public static ConcurrentMap<RuleKey, List<RuleEntity>> getRuleMap() {
        return RULE_MAP;
    }
}