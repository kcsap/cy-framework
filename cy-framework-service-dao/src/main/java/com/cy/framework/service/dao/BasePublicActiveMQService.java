package com.cy.framework.service.dao;


import com.cy.framework.model.BaseFactoryParam;
import com.cy.framework.mybaties.dao.BasePublicMapper;
import com.cy.framework.util.SystemCode;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017\10\15 0015.
 */
public interface BasePublicActiveMQService {

    void init();

    void setMapper(BasePublicMapper mapper, Class<?> cl);

    ActiveMQService getActiveMQService();

    /**
     * 公用的添加
     *
     * @param object 添加的对象
     * @param param
     * @return
     */
    Map<String, Object> insert(Object object, BaseFactoryParam param);

    /**
     * 根据主键删除
     *
     * @param id    删除的主键
     * @param param
     * @return
     */
    Map<String, Object> delete(Object id, BaseFactoryParam param);

    /**
     * 条件更新
     *
     * @param object
     * @param param
     * @return
     */
    Map<String, Object> update(Object object, BaseFactoryParam param);

    /**
     * 条件删除
     *
     * @param object 删除的条件
     * @param param
     * @return
     */
    Map<String, Object> deleteObject(Object object, BaseFactoryParam param);

    /**
     * 查询单条记录
     *
     * @param id    需要查询的id
     * @param param
     * @return
     */
    Object findById(Object id, BaseFactoryParam param);

    /**
     * 公用的查询
     *
     * @param t 参数
     * @return
     */
    Object query(Object t, BaseFactoryParam param);

    /**
     * 分页查询
     *
     * @param t        查询的条件对象
     * @param page     当前页数
     * @param pageSize 页数的大小
     * @param param    参数
     * @return
     */
    Object queryListPage(Object t, Integer page, Integer pageSize, BaseFactoryParam param);

    /**
     * 查询总条数
     *
     * @param t     查询的条件
     * @param param
     * @return
     */
    Integer queryListPageCount(Object t, BaseFactoryParam param);

    /**
     * 分页查询-返回总条数
     *
     * @param t        查询的条件
     * @param page     当前页数
     * @param pageSize 需要显示的条数
     * @param param
     * @return
     */
    Map<String, Object> queryManagerListPage(Object t, Integer page, Integer pageSize, BaseFactoryParam param);

    /**
     * 公用的请求队列 形势
     *
     * @param param
     * @return
     */
    <T> T sendQueue(BaseFactoryParam param, Class<T> t);

    /**
     * 公用的发送广播 形势
     *
     * @param param
     * @return
     */
    Object sendTopic(BaseFactoryParam param);

    /**
     * 发送消息
     *
     * @param data
     * @param token
     * @return
     */
    void sendUserIdMessage(Map<String, Object> data, String token);

    /**
     * @param code
     * @param list
     * @param callBackMethod
     * @param token
     */
    <T> void sendUserIdMessage(String code, List<T> list, String callBackMethod, String token);

    /**
     * @param code
     * @param targetUsers
     * @param callBackMethod
     * @param param
     * @param objects
     * @param <T>
     */
    <T> void sendUserIdMessage(SystemCode code, List<String> targetUsers, String callBackMethod, BaseFactoryParam param, Object... objects);

    /**
     * @param code
     * @param targetUsers
     * @param callBackMethod
     * @param token
     * @param sendUser
     * @param objects
     * @param <T>
     */
    <T> void sendUserIdMessage(SystemCode code, List<String> targetUsers, String callBackMethod, String token, String sendUser, Object... objects);

    /**
     * @param code
     * @param callBackMethod
     * @param param
     * @param targetUsers
     * @param object
     * @param <T>
     */
    <T> void sendUserIdMessage(SystemCode code, String callBackMethod, BaseFactoryParam param, List<String> targetUsers, List<T> object);

}
