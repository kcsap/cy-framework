package com.cy.framework.model;

import com.alibaba.fastjson.JSONArray;
import com.cy.framework.util.StringUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class BaseFactoryParam implements Serializable {

    private static final long serialVersionUID = 2126828134780790670L;
    private String controllerName;// 类名
    private String actionName;// 方法名
    private String token; //用户的token
    private JSONArray data;// 数据
    private String clicent_ip;
    private Object userInfo;
    private Boolean admin;
    /**
     * 是否http请求否则tpc
     */
    private boolean http;
    /**
     * 回调的方法
     */
    private String callbackMethod;
    /**
     * 登陆的经度
     */
    private BigDecimal lng;

    /**
     * 登陆的纬度
     */
    private BigDecimal lat;
    /**
     * 对列的名称
     */
    private String queue_name;
    /**
     * 过滤的参数
     */
    private String[] params;
    /**
     * 出现异常 返回的参数
     */
    private Map<String, Object> exceptionParam;

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public boolean isHttp() {
        return http;
    }

    public void setHttp(boolean http) {
        this.http = http;
    }

    public String getCallbackMethod() {
        return callbackMethod;
    }

    public void setCallbackMethod(String callbackMethod) {
        this.callbackMethod = callbackMethod;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public Map<String, Object> getExceptionParam() {
        return exceptionParam;
    }

    public void setExceptionParam(Map<String, Object> exceptionParam) {
        this.exceptionParam = exceptionParam;
    }

    public String getQueue_name() {
        return queue_name;
    }

    public void setQueue_name(String queue_name) {
        this.queue_name = queue_name;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public Object getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Object userInfo) {
        this.userInfo = userInfo;
    }

    public String getClicent_ip() {
        return clicent_ip;
    }

    public void setClicent_ip(String clicent_ip) {
        this.clicent_ip = clicent_ip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getControllerName() {
        return controllerName != null ? controllerName.trim() : null;
    }

    public void setControllerName(String controllerName) {
        if (StringUtil.isNotEmpty(controllerName)) {
            controllerName = controllerName.substring(0, 1).toLowerCase() + controllerName.substring(1, controllerName.length());
            if (!controllerName.equals("systemController") && controllerName.contains("Controller")) {
                controllerName = controllerName.substring(0, controllerName.length() - "Controller".length());
            }
        }
        this.controllerName = controllerName.trim();
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName == null ? null : actionName.trim();
    }

    public JSONArray getData() {
        if (data == null) {
            data = new JSONArray();
        }
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }

}
