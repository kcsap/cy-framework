package com.cy.framework.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cy.framework.model.BaseFactoryParam;
import com.cy.framework.model.PageInfo;
import com.cy.framework.mybaties.dao.BasePublicMapper;
import com.cy.framework.service.dao.ActiveMQService;
import com.cy.framework.service.dao.BasePublicService;
import com.cy.framework.util.json.JSONUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017\10\15 0015.
 */
public abstract class BasePublicServiceImpl extends DataException implements BasePublicService {
    private BasePublicMapper baseMapper;
    private Class<?> aClass;
    @Resource
    private ActiveMQService activeMQService;

    public void setMapper(BasePublicMapper mapper, Class<?> cl) {
        if (baseMapper != null) {
            return;
        }
        baseMapper = mapper;
        aClass = cl;
        setClass(aClass);
    }

    public void setMapper(BasePublicMapper mapper) {
        if (baseMapper != null) {
            return;
        }
        baseMapper = mapper;
    }

    @Override
    public Map<String, Object> insert(Object object, HttpServletRequest param) {
        init();
        return JSONUtils.generatorMap("添加", baseMapper.insertSelective(object) > 0, object);
    }

    @Override
    public Map<String, Object> delete(Object id, HttpServletRequest param) {
        init();
        return JSONUtils.generatorMap("删除", baseMapper.deleteByPrimaryKey(id) > 0, id);
    }

    @Override
    public Map<String, Object> update(Object object, HttpServletRequest param) {
        init();
        return JSONUtils.generatorMap("更新", baseMapper.updateByPrimaryKeySelective(object) > 0, object);

    }

    @Override
    public Map<String, Object> deleteObject(Object object, HttpServletRequest param) {
        init();
        return JSONUtils.generatorMap("删除", baseMapper.deleteObject(object) > 0, object);

    }

    @Override
    public Object findById(Object id, HttpServletRequest param) {
        init();
        return baseMapper.selectByPrimaryKey(id);
    }

    public Object query(Object t, HttpServletRequest param) {
        init();
        return baseMapper.query(t);
    }

    @Override
    public Object queryListPage(Object t, Integer page, Integer pageSize, HttpServletRequest param) {
        init();

        return baseMapper.queryListPage(t, getPageInfo(page,pageSize));
    }

    @Override
    public Integer queryListPageCount(Object t, HttpServletRequest param) {
        init();
        return baseMapper.queryListPageCount(t);
    }

    @Override
    public Map<String, Object> queryManagerListPage(Object t, Integer page, Integer pageSize, HttpServletRequest param) {
        Map<String, Object> map = new HashMap<>();
        map.put("rows", queryListPage(t, page, pageSize, param));
        map.put("total", queryListPageCount(t, param));
        return map;
    }

    @Override
    public Object sendQueue(BaseFactoryParam param) {
        String object = null;
        try {
            if (param.getQueue_name() == null || param.getQueue_name().isEmpty()) {
                throw new Exception("队列名称未传");
            }
            object = activeMQService.sendMessage(activeMQService.getQueue(param.getQueue_name()), param, String.class);
        } catch (Exception e) {
            object=getErrorJson(e);
        }
        return object;
    }

    @Override
    public Object sendTopic(BaseFactoryParam param) {
        Object object = null;
        try {
            if (param.getQueue_name() == null || param.getQueue_name().isEmpty()) {
                throw new Exception("队列名称未传");
            }
            object = activeMQService.sendMessage(activeMQService.getTopic(param.getQueue_name()), param, null);
        } catch (Exception e) {
            object=getErrorJson(e);
        }
        return object;
    }
}
