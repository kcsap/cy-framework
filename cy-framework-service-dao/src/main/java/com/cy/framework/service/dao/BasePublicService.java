package com.cy.framework.service.dao;

import com.cy.framework.model.BaseFactoryParam;
import com.cy.framework.mybaties.dao.BaseFrameworkMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2017\10\15 0015.
 */
public interface BasePublicService {

    void init();

    void setMapper(BaseFrameworkMapper mapper, Class<?> cl);

    /**
     * 公用的添加
     *
     * @param object  添加的对象
     * @param request
     * @return
     */
    Map<String, Object> insert(Object object, HttpServletRequest request);

    /**
     * 根据主键删除
     *
     * @param id      删除的主键
     * @param request
     * @return
     */
    Map<String, Object> delete(Object id, HttpServletRequest request);

    /**
     * 条件更新
     *
     * @param object
     * @param request
     * @return
     */
    Map<String, Object> update(Object object, HttpServletRequest request);

    /**
     * 条件删除
     *
     * @param object  删除的条件
     * @param request
     * @return
     */
    Map<String, Object> deleteObject(Object object, HttpServletRequest request);

    /**
     * 查询单条记录
     *
     * @param id      需要查询的id
     * @param request
     * @return
     */
    Object findById(Object id, HttpServletRequest request);

    /**
     * 公用的查询
     *
     * @param t 参数
     * @return
     */
    Object query(Object t, HttpServletRequest request);

    /**
     * 分页查询
     *
     * @param t        查询的条件对象
     * @param page     当前页数
     * @param pageSize 页数的大小
     * @param request  参数
     * @return
     */
    Object queryListPage(Object t, Integer page, Integer pageSize, HttpServletRequest request);

    /**
     * 查询总条数
     *
     * @param t       查询的条件
     * @param request
     * @return
     */
    Integer queryListPageCount(Object t, HttpServletRequest request);

    /**
     * 分页查询-返回总条数
     *
     * @param t        查询的条件
     * @param page     当前页数
     * @param pageSize 需要显示的条数
     * @param request
     * @return
     */
    Map<String, Object> queryManagerListPage(Object t, Integer page, Integer pageSize, HttpServletRequest request);

    /**
     * 公用的请求队列 形势
     *
     * @param param
     * @return
     */
    Object sendQueue(BaseFactoryParam param);

    /**
     * 公用的发送广播 形势
     *
     * @param param
     * @return
     */
    Object sendTopic(BaseFactoryParam param);
}
