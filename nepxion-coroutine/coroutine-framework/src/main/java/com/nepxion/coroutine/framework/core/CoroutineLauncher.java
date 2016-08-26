package com.nepxion.coroutine.framework.core;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

/**
 *　　　　　　　　┏┓　　　┏┓+ +
 *　　　　　　　┏┛┻━━━┛┻┓ + +
 *　　　　　　　┃　　　　　　　┃ 　
 *　　　　　　　┃　　　━　　　┃ ++ + + +
 *　　　　　　 ████━████ ┃+
 *　　　　　　　┃　　　　　　　┃ +
 *　　　　　　　┃　　　┻　　　┃
 *　　　　　　　┃　　　　　　　┃ + +
 *　　　　　　　┗━┓　　　┏━┛
 *　　　　　　　　　┃　　　┃　　　　　　　　　　　
 *　　　　　　　　　┃　　　┃ + + + +
 *　　　　　　　　　┃　　　┃　　　　　　　　　　　
 *　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug　　
 *　　　　　　　　　┃　　　┃
 *　　　　　　　　　┃　　　┃　　+　　　　　　　　　
 *　　　　　　　　　┃　 　　┗━━━┓ + +
 *　　　　　　　　　┃ 　　　　　　　┣┓
 *　　　　　　　　　┃ 　　　　　　　┏┛
 *　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 *　　　　　　　　　　┃┫┫　┃┫┫
 *　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 */

import com.nepxion.coroutine.common.callback.CoroutineCallback;
import com.nepxion.coroutine.common.delegate.CoroutineDelegate;
import com.nepxion.coroutine.data.entity.CoroutineId;
import com.nepxion.coroutine.data.entity.CoroutineResult;

/**
 * 调用链外部入口
 */
public interface CoroutineLauncher extends CoroutineDelegate {
    /**
     * 异步方法调用，id由系统自动生成(UUID)
     * @param categoryName 规则的目录名，跟注册中心相对应
     * @param ruleName     规则名，跟注册中心相对应
     * @param chainName    链式调用名
     * @param parameters   初始传入的参数数组
     * @param last         最后一个链式调用
     * @param callback     异步回调方法
     * @return             异步返回CoroutineId
     */
    CoroutineId startAsync(String categoryName, String ruleName, String chainName, Object[] parameters, boolean last, CoroutineCallback<CoroutineResult<Object>> callback);

    /**
     * 异步方法调用
     * @param id           全局唯一的ID，可以用UUID来实现，也可以外部传入唯一交易号/流水号
     * @param categoryName 规则的目录名，跟注册中心相对应
     * @param ruleName     规则名，跟注册中心相对应
     * @param chainName    链式调用名
     * @param parameters   初始传入的参数数组
     * @param last         最后一个链式调用
     * @param callback     异步回调方法
     * @return             异步返回CoroutineId
     */
    CoroutineId startAsync(String id, String categoryName, String ruleName, String chainName, Object[] parameters, boolean last, CoroutineCallback<CoroutineResult<Object>> callback);

    /**
     * 同步方法调用，id由系统自动生成(UUID)
     * @param categoryName 规则的目录名，跟注册中心相对应
     * @param ruleName     规则名，跟注册中心相对应
     * @param chainName    链式调用名
     * @param parameters   初始传入的参数数组
     * @param timeout      同步超时时间
     * @param last         最后一个链式调用
     * @return             同步返回CoroutineResult
     * @throws Exception
     */
    CoroutineResult<Object> startSync(String categoryName, String ruleName, String chainName, Object[] parameters, long timeout, boolean last) throws Exception;

    /**
     * 同步方法调用
     * @param id           全局唯一的ID，可以用UUID来实现，也可以外部传入唯一交易号/流水号
     * @param categoryName 规则的目录名，跟注册中心相对应
     * @param ruleName     规则名，跟注册中心相对应
     * @param chainName    链式调用名
     * @param parameters   初始传入的参数数组
     * @param timeout      同步超时时间
     * @param last         最后一个链式调用
     * @return             同步返回CoroutineResult
     * @throws Exception
     */
    CoroutineResult<Object> startSync(String id, String categoryName, String ruleName, String chainName, Object[] parameters, long timeout, boolean last) throws Exception;
}