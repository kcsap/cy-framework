package com.cy.framework.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONUtils {

    private static Logger logger = Logger.getLogger(JSONUtils.class);

    /**
     * 方法描述：将数据转换为json对象
     *
     * @param obj 待转换的数据对象
     * @return json对象
     */
    public static JSONObject obj2Json(Object obj) {
        JSONObject json = obj2Json(obj, null);
        return json;
    }

    /**
     * 描述： 生成Map
     *
     * @param remark
     * @param result
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月14日 上午10:04:47
     */
    public static Map<String, Object> generatorMap(String remark, boolean result) {
        Map<String, Object> map = new HashMap<>();
        map.put("result", result);
        map.put("remark", result == true ? remark + "成功" : remark + "失败");
        return map;
    }

    /**
     * 描述： 生成 Map 有返回对象
     *
     * @param remark
     * @param result
     * @param data
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月14日 上午10:05:13
     */
    public static Map<String, Object> generatorMap(String remark, boolean result, Object data) {
        Map<String, Object> map = generatorMap(remark, result);
//        map.put("data", data);
        return map;
    }

    public static Map<String, Object> getBasicJson(String remark,boolean label) {
        Map<String, Object> map = new HashMap<>();
        map.put("result", label);
        map.put("remark", remark);
        return map;
    }
    public static Map<String, Object> getBasicJson(String remark,boolean label,Object data) {
        Map<String, Object> map = getBasicJson(remark,label);
        map.put("data", data);
        return map;
    }
    public static void responseWriteText(Object obj, HttpServletRequest request, HttpServletResponse response) {
        String json = null;
        if (obj instanceof String) {
            json = obj.toString();
        } else {
            JSONObject jsonObject = JSONUtils.obj2Json(obj);
            json = jsonObject.toString();
        }
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            response.getWriter().write(json);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            logger.warn("response is error Exception:" + e.getMessage());
        }
    }

    /**
     * 方法描述：json对象解析
     *
     * @param str   json内容
     * @param clazz 接收的类
     * @return
     */
    public static <T> T json2Obj(String str, Class<T> clazz) {
        T obj = null;
        if (str != null && !str.isEmpty()) {
            try {
                obj = JSON.parseObject(str, clazz);
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        return obj;
    }

    /**
     * 方法描述：将对象转换为json对象
     *
     * @param obj    待转换的对象
     * @param params 需要排除的参数对象
     * @return 转换的json对象
     */
    public static JSONObject obj2Json(Object obj, String[] params) {
        JSONObject json = null;
        try {
            JsonConfig config = JSONUtils.getJsonConfig(params);
            json = JSONObject.fromObject(obj, config);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return json;
    }

    /**
     * 方法描述：json列表内容解析
     *
     * @param str   json的列表内容
     * @param clazz 接收的类名
     * @return
     */
    public static <T> List<T> json2List(String str, Class<T> clazz) {
        List<T> list = null;
        if (str != null && !str.isEmpty()) {
            try {
                list = com.alibaba.fastjson.JSONObject.parseArray(str, clazz);
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        return list;
    }

    /**
     * 方法描述：将列表转换成json对象
     *
     * @param obj 待转换的list列表对象
     * @return 转换后的json对象
     */
    public static String list2Json(List<?> obj) {
        return JSONUtils.list2JsonStr(obj, null);
    }

    /**
     * 方法描述：将对象中的一些对象排除后，再生产json数组
     *
     * @param obj    待处理的json数组
     * @param params 需要排除的对象参数
     * @return json数组
     */
    public static String list2JsonStr(List<?> obj, String[] params) {
        String result = null;
        try {

            JsonConfig config = JSONUtils.getJsonConfig(params);
            JSONArray json = JSONArray.fromObject(obj, config);
            result = json.toString();
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return result;
    }

    public static JSONArray list2Json(List<?> obj, String[] params) {
        JSONArray json = null;
        try {
            JsonConfig config = JSONUtils.getJsonConfig(params);
            json = JSONArray.fromObject(obj, config);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return json;
    }

    /**
     * 将json转为map映射
     *
     * @param json
     * @return
     */
    public static Map<String, Object> json2Map(String json) {
        Map<String, Object> mapStr = new HashMap<String, Object>();
        if (null != json) {
            try {
                mapStr = JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
                });
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        return mapStr;
    }

    /**
     * 将json转为映射数组
     *
     * @param json
     * @return
     */
    public static List<Map<String, Object>> json2MapList(String json) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (null != json) {
            try {
                list = JSON.parseObject(json, new TypeReference<List<Map<String, Object>>>() {
                });
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        return list;
    }

    /**
     * 方法描述：将对象转换为map对象
     *
     * @param obj 待转换的对象
     * @return
     */
    public static Map<String, Object> obj2Map(Object obj, String[] except) {
        Map<String, Object> objMap = null;
        JSONObject jsonObj = obj2Json(obj, except);
        if (jsonObj != null) {
            objMap = json2Map(jsonObj.toString());
        }
        return objMap;
    }

    public static Map<String, Object> requestMapToMap(Object obj, String[] except) {
        JSONObject jsonObj = obj2Json(obj, except);
        String str = jsonObj.toString().replaceAll("\\[", "").replaceAll("\\]", "");
        return json2Map(str);
    }

    /**
     * 方法描述：获取jsonconfig的对象，排除一些对象
     *
     * @param params 需要排除的参数对象
     * @return jsonconfig对象
     */
    public static JsonConfig getJsonConfig(String params[]) {
        JsonConfig config = new JsonConfig();
        if (params != null) {
            config.setExcludes(params);
        }
        config.registerJsonValueProcessor(java.util.Date.class, new JSONDateProcess());
        config.registerJsonValueProcessor(Float.class, new JSONDateProcess());
        config.registerJsonValueProcessor(float.class, new JSONDateProcess());
        config.registerJsonValueProcessor(Double.class, new JSONDateProcess());
        config.registerJsonValueProcessor(double.class, new JSONDateProcess());
        return config;
    }
}
