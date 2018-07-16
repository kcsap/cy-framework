package com.cy.framework.service.web;
import com.cy.framework.model.BaseFactoryParam;
import com.cy.framework.mybaties.dao.Token;
import com.cy.framework.service.dao.BasePublicService;
import com.cy.framework.service.dao.UserTokenService;
import com.cy.framework.util.SystemCode;
import net.sf.json.JSONObject;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseFactoryController {
    public org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    private BasePublicService baseService;
    @Resource
    private UserTokenService userTokenService;

    public abstract void init();

    public void setBaseService(BasePublicService service) {
        baseService = service;
    }

    /**
     * @Description: 添加数据
     * @author yangchengfu
     * @Date : 2017/7/28 9:42
     */
    public Map<String, Object> insert(Map<String, Object> map, HttpServletRequest request) {
        // TODO Auto-generated method stub
        init();
        return baseService.insert(map, request);
    }

    /**
     * @Description: 删除单条数据
     * @author yangchengfu
     * @Date : 2017/7/28 9:43
     */
    public Map<String, Object> delete(Object id, HttpServletRequest request) {
        // TODO Auto-generated method stub
        init();
        return baseService.delete(id, request);
    }

    public Map<String, Object> update(Map<String, Object> map, HttpServletRequest request) {
        // TODO Auto-generated method stub
        init();
        return baseService.update(map, request);
    }

    public Object findById(Object id, HttpServletRequest request) {
        // TODO Auto-generated method stub
        init();
        return baseService.findById(id, request);
    }

    public Object query(Map<String, Object> map, HttpServletRequest request) {
        // TODO Auto-generated method stub
        init();
        return baseService.query(map, request);
    }

    public Object queryListPage(Map<String, Object> map, Integer page, Integer pageSize, HttpServletRequest request) {
        // TODO Auto-generated method stub
        init();
        return baseService.queryListPage(map, page, pageSize, request);
    }

    public Integer queryListPageCount(Map<String, Object> map, HttpServletRequest request) {
        init();
        // TODO Auto-generated method stub
        return baseService.queryListPageCount(map, request);
    }

    /**
     * @Description:
     * @author yangchengfu
     * @Date : 2017/7/28 9:50
     */
    public Object queryManagerListPage(Map<String, Object> map, Integer page, Integer pageSize,
                                       HttpServletRequest request) {
        // TODO Auto-generated method stub
        init();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("rows", queryListPage(map, page, pageSize, request));
        map2.put("total", queryListPageCount(map, request));
        return map2;
    }

    public Object invoke(BaseFactoryParam param, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        Object result = null;
        SystemCode remark = SystemCode.SYSTEM_ERROR;
        try {
            Method[] methods = this.getClass().getMethods();
            if (methods == null || methods.length <= 0) {
                throw new RuntimeException("methods is null");
            }
            Method invokeMethod = null;
            List<String> listMethod = new ArrayList<>();
            for (Method method2 : methods) {
                listMethod.add(method2.getName());
                if (!method2.getName().equals(param.getActionName())) {
                    continue;
                }
                invokeMethod = method2;
                break;
            }
            logger.debug("className is:" + this.getClass().getName() + ",methods is:" + listMethod.toString());
            if (invokeMethod == null) {
                result = "actionName有误,未找到。请核对！";
                throw new RuntimeException("method is:" + param.getActionName() + " but methods is not contains");
            }
            String token = request.getHeader("token");
            if (token == null || token.isEmpty()) {
                token = request.getParameter("token");
                if (token == null || token.isEmpty()) {
                    token = param.getToken();
                }
            }
            if (token != null && !token.isEmpty()) {
                request.setAttribute("token", token);
            }
            Token token2 = invokeMethod.getAnnotation(Token.class);
            if (token2 != null && token2.login()) {// 需要登录
                if (token == null || token.isEmpty()) {
                    remark = SystemCode.NOT_LOGIN;
                    throw new RuntimeException("method" + invokeMethod.getName() + ",validate login is true,");
                } else {
                    try {
                        Object userInfo1 = userTokenService.getUserInfo(token);
                        if (userInfo1 == null) {
                            remark = SystemCode.NOT_LOGIN;
                            throw new RuntimeException("user is not login,token is not contains");
                        }
                        param.setUserInfo(userInfo1);
                    } catch (Exception e) {
                        logger.warn("connection redis get userInfo Exception", e);
                        remark = SystemCode.NOT_LOGIN;
                        throw new RuntimeException("user is not login,token is not contains");

                    }
                }

            }
            List<Object> params = params(invokeMethod, param.getData(), request, response);
            if (params == null || params.size() <= 0) {
                result = invokeMethod.invoke(this);
            } else {
                result = invokeMethod.invoke(this, params.toArray());
            }
            remark = SystemCode.SUCCESS;
        } catch (Exception e) {
            // TODO: handle exception
            logger.warn("inokve Exception", e);
            result = e.getMessage();
        }
        map.put("code", remark.getCode());
        map.put("message", remark.getDesc());
        map.put("data", result);
        return map;
    }

    private List<Object> params(Method method, com.alibaba.fastjson.JSONArray ja, HttpServletRequest request, HttpServletResponse response) {
        Class<?>[] params = method.getParameterTypes();
        if (params == null || params.length <= 0) {
            return null;
        }
        int x = 0;
        Object obj = null;
        List<Object> list = new ArrayList<>();
        for (Class<?> cl : params) {
            try {
                String clsName = cl.getName();
                if (clsName.equals(Float.class.getName()) || clsName.equals(float.class.getName())) {
                    String funParam = ja.getString(x);
                    obj = new Float(funParam);
                } else if (clsName.equals(int.class.getName()) || clsName.equals(Integer.class.getName())) {
                    obj = ja.getInteger(x);
                } else if (clsName.equals(double.class.getName()) || clsName.equals(Double.class.getName())) {
                    obj = ja.getDouble(x);
                } else if (clsName.equals(boolean.class.getName()) || clsName.equals(Boolean.class.getName())) {
                    obj = ja.getBoolean(x);
                } else if (clsName.equals(long.class.getName()) || clsName.equals(Long.class.getName())) {
                    obj = ja.getLong(x);
                } else if (clsName.equals(String.class.getName())) {
                    obj = ja.getString(x);
                } else if (clsName.equals(HttpServletRequest.class.getName())) {
                    obj = request;
                } else if (clsName.equals(HttpServletResponse.class.getName())) {
                    obj = response;
                } else if (clsName.equals(JSONObject.class.getName())) {
                    obj = ja.getJSONObject(x);
                } else if (clsName.equals(com.alibaba.fastjson.JSONArray.class.getName())) {
                    obj = ja.getJSONArray(x);
                } else if (clsName.equals(Object.class.getName())) {
                    obj = ja.get(x);
                } else {
                    obj = ja.getObject(x, cl);
                }
            } catch (Exception e) {
                // TODO: handle exception
                obj = null;
            }
            list.add(obj);
            x++;
        }
        return list;
    }
}
