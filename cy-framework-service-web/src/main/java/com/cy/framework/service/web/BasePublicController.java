package com.cy.framework.service.web;

import com.cy.framework.model.BaseFactoryParam;
import com.cy.framework.model.qiniu.QiNiuUploadParam;
import com.cy.framework.service.dao.RedisService;
import com.cy.framework.service.dao.qiniu.QiNiuService;
import com.cy.framework.util.SystemCode;
import com.cy.framework.util.json.JSONUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasePublicController {
    public org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    public ApplicationContext context;
    @Resource
    public QiNiuService qiNiuService;

    /**
     * 公用的服务入口中心
     *
     * @param param
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public Object service(BaseFactoryParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object str = null;
        try {
            if (request != null) {
                request.setAttribute("ip", param.getClicent_ip());
            }
            logger.warn("request param:" + JSONUtils.obj2Json(param).toString());
            BaseFactoryController service = (BaseFactoryController) context.getBean(param.getControllerName() + "Controller");
            Object obj = service.invoke(param, request, response);
            Object objs = request.getAttribute("params");
            String[] params = {};
            if (objs != null) {
                params = (String[]) objs;
            }
            if (obj instanceof String) {
                str = objs.toString();
            } else if (obj instanceof List) {
                JsonConfig config = JSONUtils.getJsonConfig(params);
                JSONArray json = JSONArray.fromObject(obj, config);
                str = json.toString();
            } else {
                str = JSONUtils.obj2Json(obj, params);
            }
        } catch (Exception e) {
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            jsonObject.put("code", "-9999");
            jsonObject.put("message", "controllerName有误,请核对后再请求");
            jsonObject.put("data", null);
            str = jsonObject.toString();
            // TODO: handle exception
            logger.warn("invoke Exception,req param:" + JSONUtils.obj2Json(param).toString(), e);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        return str.toString();
    }

    public Object upload(QiNiuUploadParam param, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = null;
        String data = null;
        SystemCode code = SystemCode.SUCCESS;
        try {
            if (param.getType() == null || param.getType().isEmpty()) {
                data = "类型未传";
                throw new RuntimeException(data.toString());
            }
            if (param.getFiles() == null || param.getFiles().size() <= 0) {
                data = "未传入图片,请核对后再上传!";
                throw new RuntimeException("upload files is null" + data);
            }
            map = qiNiuService.upload(param, request, response);
        } catch (Exception e) {
            map = new HashMap<>();
            map.put("result", false);
            map.put("remark", e.getMessage());
        }
        return map;
    }

    /**
     * 公用的服务入口中心
     *
     * @param param
     * @return
     * @throws Exception
     */
    public String serviceActiveMQ(BaseFactoryParam param) {
        Object str = null;
        try {
            logger.warn("request param:" + JSONUtils.obj2Json(param).toString());
            BaseActiveMQFactoryController service = (BaseActiveMQFactoryController) context.getBean(param.getControllerName() + "Controller");
            service.baseFactoryParam = param;
            Object obj = service.invoke(param);
            String[] params = param.getParams();
            if (obj instanceof String) {
                str = obj.toString();
            } else if (obj instanceof List) {
                JsonConfig config = JSONUtils.getJsonConfig(params);
                JSONArray json = JSONArray.fromObject(obj, config);
                str = json.toString();
            } else {
                str = JSONUtils.obj2Json(obj, params);
            }
        } catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "-9999");
            jsonObject.put("message", "controllerName有误,请核对后再请求");
            jsonObject.put("data", null);
            str = jsonObject.toString();
            // TODO: handle exception
            logger.warn("invoke Exception,req param:" + JSONUtils.obj2Json(param).toString(), e);
        }
        return str.toString();
    }

    /**
     * 队列
     *
     * @param param
     * @param session
     * @return
     * @throws JMSException
     */
    public String queueListener(ObjectMessage param, Session session) throws JMSException {
        String object = null;
        try {
            object = serviceActiveMQ((BaseFactoryParam) param.getObject());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return object;
    }

    /**
     * 广播形势
     *
     * @param param
     * @param session
     * @return
     * @throws JMSException
     */
    public String topicLinner(ObjectMessage param, Session session) throws JMSException {
        String object = null;
        try {
            object = serviceActiveMQ((BaseFactoryParam) param.getObject());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return object;
    }

}
