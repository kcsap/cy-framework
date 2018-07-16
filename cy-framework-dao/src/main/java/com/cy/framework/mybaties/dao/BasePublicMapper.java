package com.cy.framework.mybaties.dao;

import com.cy.framework.model.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017\10\15 0015.
 */
public interface BasePublicMapper {
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
     * @param <T>
     * @return
     */
    <T> List<T> query(@Param(value = "param") T t);

    /**
     * 条件查询 分页
     *
     * @param t
     * @param pageInfo
     * @param <T>
     * @return
     */
    <T> List<T> queryListPage(@Param(value = "param") T t, @Param(value = "page") PageInfo pageInfo);

    /**
     * 查询总条数
     *
     * @param t
     * @param <T>
     * @return
     */
    <T> Integer queryListPageCount(@Param(value = "param") T t);

    /**
     * @mbggenerated 2017-10-15
     */
    int deleteByPrimaryKey(Object obj);

    /**
     * @mbggenerated 2017-10-15
     */
    <T> int insert(T t);

    /**
     * @mbggenerated 2017-10-15
     */
    <T> int insertSelective(T t);

    /**
     * @mbggenerated 2017-10-15
     */
    Object selectByPrimaryKey(Object id);

    /**
     * @mbggenerated 2017-10-15
     */
    <T> int updateByPrimaryKeySelective(T t);

    /**
     * @mbggenerated 2017-10-15
     */
    <T> int updateByPrimaryKey(T t);
}
