package com.cy.framework.service.dao;


import com.cy.framework.util.result.ResultParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017\10\15 0015.
 */
public interface BaseFrameworkService<T, ID> {

    void init();
    /**
     * 公用的添加
     *
     * @param t 添加的对象
     * @return
     */
    ResultParam insert(T t, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据主键删除
     *
     * @param id      删除的主键
     * @param request
     * @return
     */
    ResultParam delete(ID id, HttpServletRequest request, HttpServletResponse response);

    /**
     * 条件更新
     *
     * @param t
     * @param request
     * @return
     */
    ResultParam update(T t, HttpServletRequest request, HttpServletResponse response);

    /**
     * 条件删除
     *
     * @param t       删除的条件
     * @param request
     * @return
     */
    ResultParam deleteObject(T t, HttpServletRequest request, HttpServletResponse response);

    /**
     * 查询单条记录
     *
     * @param id      需要查询的id
     * @param request
     * @return
     */
    T findById(ID id, HttpServletRequest request, HttpServletResponse response);

    /**
     * 公用的查询
     *
     * @param t 参数
     * @return
     */
    List<T> query(T t, HttpServletRequest request, HttpServletResponse response);

    /**
     * 分页查询
     *
     * @param t        查询的条件对象
     * @param request
     * @return
     */
    List<T> queryListPage(T t, HttpServletRequest request, HttpServletResponse response);

    /**
     * 查询总条数
     *
     * @param t       查询的条件
     * @param request
     * @return
     */
    Integer queryListPageCount(T t, HttpServletRequest request, HttpServletResponse response);

    /**
     * 分页查询-返回总条数
     *
     * @param t        查询的条件
     * @param request
     * @return
     */
    Map<String, Object> queryManagerListPage(T t, HttpServletRequest request, HttpServletResponse response);
}
