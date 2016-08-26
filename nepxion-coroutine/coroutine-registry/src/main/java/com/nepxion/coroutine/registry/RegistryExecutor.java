package com.nepxion.coroutine.registry;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;

import com.nepxion.coroutine.common.delegate.CoroutineDelegate;

public interface RegistryExecutor extends CoroutineDelegate {

    // 设置注册中心初始化器
    void setRegistryInitializer(RegistryInitializer registryInitializer);

    // 设置名称空间
    void setNamespace(String namespace);

    // 注册规则节点
    void registerRule(String categoryName, String ruleName) throws Exception;

    // 删除规则节点
    void deleteRule(String categoryName, String ruleName) throws Exception;

    // 持久化规则节点配置信息
    void persistRule(String categoryName, String ruleName, String ruleContent) throws Exception;

    // 获取规则配置信息
    String retrieveRule(String categoryName, String ruleName) throws Exception;

    // 获取规则名称列表
    List<String> getRuleNames(String categoryName) throws Exception;

    // 对规则目录进行监听
    void addCategoryListener(String categoryName) throws Exception;
    
    // 对规则进行监听
    void addRuleListener(String categoryName, String ruleName) throws Exception;
}