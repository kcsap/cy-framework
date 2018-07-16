package com.cy.framework.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cy.framework.model.BaseFactoryParam;
import com.cy.framework.model.PageInfo;
import com.cy.framework.service.dao.lock.DistributedLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @param
 * @author yangchengfu
 * @Description: 公用的异常处理
 * @return
 * @Date : 2017/10/23 10:13
 */
public class DataException extends RuntimeException {
    public Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    public DistributedLockService lockService;
    /**
     * 锁的唯一标识
     */
    private String lockKey;
    /**
     * 锁的名称
     */
    private String lockName;

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public void setClass(Class<?> cl) {
        logger = LoggerFactory.getLogger(cl);
    }

    public DataException(String message) {
        super(message);
        logger.warn(message);
    }

    public DataException(DataException throwable, BaseFactoryParam param) {
        super(throwable);
        logger.warn(throwable.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("result", false);
        map.put("remark", throwable.getMessage());
        param.setExceptionParam(map);
    }

    public DataException(String data, BaseFactoryParam param) {
        super(data);
        logger.warn(data);
        Map<String, Object> map = new HashMap<>();
        map.put("result", false);
        map.put("remark", data);
        param.setExceptionParam(map);
    }

    public DataException(String data, Object extend, BaseFactoryParam param) {
        super(data);
        logger.warn(data);
        Map<String, Object> map = new HashMap<>();
        map.put("result", false);
        map.put("remark", data);
        map.put("extend", extend);
        param.setExceptionParam(map);
    }

    public DataException(String message, String lock_name, String lock_key) {
        super(message);
        logger.warn(message);
        setLockName(lock_name);
        setLockKey(lock_key);
    }

    public DataException(Object message) {
        super(message.toString());
        logger.warn(message.toString());
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
        logger.warn(message, cause);
    }

    public DataException(Throwable cause) {
        super(cause);
        logger.warn(cause.getMessage(), cause);
    }

    protected DataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        logger.warn(message, cause, enableSuppression, writableStackTrace);
    }

    public DataException() {
        super();
    }

    public Map<String, Object> exceptionResult(DataException e) {
        lockService.releaseLock(e.getLockName(), e.getLockKey());
        logger.warn(e.getMessage(), e);
        Map<String, Object> map = new HashMap<>();
        map.put("result", false);
        map.put("remark", e.getMessage());
        return map;
    }

    public Map<String, Object> exceptionResult(String message) {
        logger.warn(message);
        Map<String, Object> map = new HashMap<>();
        map.put("result", false);
        map.put("remark", message);
        return map;
    }
    public String getErrorJson(Exception e){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "-9999");
        jsonObject.put("message", e.getMessage());
        jsonObject.put("data", null);
        return jsonObject.toString();
    }
    public PageInfo getPageInfo(Integer page, Integer pageSize){
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize((pageSize == null || pageSize <= 0) ? 10 : pageSize);
        pageInfo.setCurrentPage((page == null || page <= 1) ? 1 : page);
        return  pageInfo;
    }
}
