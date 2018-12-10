package com.cy.framework.mybaties.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017\10\15 0015.
 */
public interface BaseFrameworkMapper<T,ID> {
    /**
     * 条件删除
     *
     * @param t
     * @param <T>
     * @return
     */
    <T> Integer deleteObject(T t);

    /**
     * 条件查询 不分页
     *
     * @param t
     * @return
     */
    List<T> query(@Param(value = "param") T t);

    /**
     * 条件查询 分页
     *
     * @param t
     * @return
     */
    List<T> queryListPage(@Param(value = "param") T t);

    /**
     * 查询总条数
     *
     * @param t
     * @return
     */
    Integer queryListPageCount(@Param(value = "param") T t);

    /**
     * @mbggenerated 2017-10-15
     */
    int deleteByPrimaryKey(ID id);

    /**
     * @mbggenerated 2017-10-15
     */
    int insert(T t);

    /**
     * @mbggenerated 2017-10-15
     */
    int insertSelective(T t);

    /**
     * @mbggenerated 2017-10-15
     */
    T selectByPrimaryKey(ID id);

    /**
     * @mbggenerated 2017-10-15
     */
    int updateByPrimaryKeySelective(T t);

    /**
     * @mbggenerated 2017-10-15
     */
    int updateByPrimaryKey(T t);
}
