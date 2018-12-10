package com.cy.framework.service.impl;

import com.cy.framework.model.PageInfo;
import com.cy.framework.mybaties.dao.BaseFrameworkMapper;
import com.cy.framework.service.dao.BaseFrameworkService;
import com.cy.framework.util.json.JSONUtils;
import com.cy.framework.util.result.ResultEnum;
import com.cy.framework.util.result.ResultParam;
import com.cy.framework.util.result.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017\10\15 0015.
 */
public abstract class BaseFrameworkServiceImpl<T, ID> implements BaseFrameworkService<T, ID> {
    private BaseFrameworkMapper baseMapper;
    public Logger logger = LoggerFactory.getLogger(this.getClass());

    public final void setMapper(BaseFrameworkMapper mapper) {
        if (baseMapper != null) {
            return;
        }
        baseMapper = mapper;
    }

    public final void setMapper(BaseFrameworkMapper mapper, Class<?> tClass) {
        if (baseMapper != null) {
            return;
        }
        baseMapper = mapper;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultParam insert(T t, HttpServletRequest request, HttpServletResponse response) {
        init();
        return JSONUtils.generatorMap("添加", baseMapper.insertSelective(t) > 0).containsValue(true)? ResultUtil.success():ResultUtil.error(ResultEnum.result_1.getCode(),ResultEnum.result_1.getDesc());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultParam delete(ID id, HttpServletRequest request, HttpServletResponse response) {
        init();
        return JSONUtils.generatorMap("删除", baseMapper.deleteByPrimaryKey(id) > 0, id).containsValue(true)? ResultUtil.success():ResultUtil.error(ResultEnum.result_2.getCode(),ResultEnum.result_2.getDesc());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultParam update(T t, HttpServletRequest request, HttpServletResponse response) {
        init();
        return JSONUtils.generatorMap("更新", baseMapper.updateByPrimaryKeySelective(t) > 0).containsValue(true)? ResultUtil.success():ResultUtil.error(ResultEnum.result_2.getCode(),ResultEnum.result_2.getDesc());

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultParam deleteObject(T object, HttpServletRequest request, HttpServletResponse response) {
        init();
        return JSONUtils.generatorMap("删除", baseMapper.deleteObject(object) > 0).containsValue(true)? ResultUtil.success():ResultUtil.error(ResultEnum.result_2.getCode(),ResultEnum.result_2.getDesc());

    }

    @Override
    public T findById(ID id, HttpServletRequest request, HttpServletResponse response) {
        init();
        return (T) baseMapper.selectByPrimaryKey(id);
    }

    public List<T> query(T t, HttpServletRequest request, HttpServletResponse response) {
        init();
        return baseMapper.query(t);
    }

    @Override
    public List<T> queryListPage(T t, HttpServletRequest request, HttpServletResponse response) {
        init();
        return baseMapper.queryListPage(t);
    }

    @Override
    public Integer queryListPageCount(T t, HttpServletRequest request, HttpServletResponse response) {
        init();
        return baseMapper.queryListPageCount(t);
    }

    @Override
    public Map<String, Object> queryManagerListPage(T t, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        map.put("rows", this.queryListPage(t, request, response));
        map.put("total", this.queryListPageCount(t, request, response));
        return map;
    }

    public final PageInfo getPageInfo(Integer page, Integer pageSize) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize((pageSize == null || pageSize <= 0) ? 10 : pageSize);
        pageInfo.setCurrentPage((page == null || page <= 1) ? 1 : page);
        return pageInfo;
    }
}
