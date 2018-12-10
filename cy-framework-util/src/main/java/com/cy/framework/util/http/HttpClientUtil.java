package com.cy.framework.util.http;

import com.alibaba.fastjson.JSONObject;
import com.cy.framework.util.json.JsonUtilFastjson;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpClientUtil {
    private final static String defualt_url = "http://47.98.133.241:1080/service";
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static void execute(String url, String controllerName, String actionName, CallBack<?> callBack, Object... objects) {
        //创建/Call
        try {
            new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build().newCall(getRequest(url, controllerName, actionName, objects)).enqueue(callBack);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public static String execute(String url, String controllerName, String actionName, Object... objects) {
        //创建/Call
        try {
            return getString(new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build().newCall(getRequest(url, controllerName, actionName, objects)).execute());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    public static String execute(String url, String controllerName, String actionName, String token, Object... objects) {
        //创建/Call
        try {
            return getString(new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build().newCall(getRequest(url, controllerName, actionName, token, objects)).execute());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    public static String execute(String url, String controllerName, String actionName, Map<String, Object> headr, Object... objects) {
        //创建/Call
        try {
            return getString(new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build().newCall(getRequest(url, controllerName, actionName, headr, objects)).execute());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    private static String getString(Response response) throws IOException {
        String string = response.body().string();
        logger.debug("response:" + string);
        return string;
    }

    public static void execute(String url, String json, CallBack<?> callBack) {
        //创建/Call
        try {
            new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build().newCall(getRequest(url, json)).enqueue(callBack);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public static String execute(String url, String json) {
        //创建/Call
        try {
            return getString(new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build().newCall(getRequest(url, json)).execute());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    private static Request getRequest(String url, String string) {
        logger.debug("request body is：" + string);
        return new Request.Builder()
                .url(url)
                //请求的url
                .post(FormBody.create(MediaType.parse("application/json; charset=utf-8")
                        , string))
                .build();
    }

    private static Request getRequest(String url, String string, Map<String, Object> headr) {
        logger.debug("request body is：" + string);
        Request.Builder builder = new Request.Builder()
                .url(url);
        if (headr != null && !headr.isEmpty()) {
            for (String str : headr.keySet()) {
                builder.addHeader(str, headr.get(str).toString());
            }
        }
        return builder
                //请求的url
                .post(FormBody.create(MediaType.parse("application/json; charset=utf-8")
                        , string))
                .build();
    }

    private static Request getRequest(String url, Map<String, Object> jsonObject) {
        return getRequest(url, JsonUtilFastjson.toJSONString(jsonObject));
    }

    private static Request getRequest(String url, String controllerName, String actionName, Object... objects) {
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("controllerName", controllerName);
        jsonObject.put("actionName", actionName);
        jsonObject.put("data", Arrays.asList(objects));
        return getRequest(url, jsonObject);
    }

    private static Request getRequest(String url, String controllerName, String actionName, Map<String, Object> headar, Object... objects) {
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("controllerName", controllerName);
        jsonObject.put("actionName", actionName);
        jsonObject.put("data", Arrays.asList(objects));
        return getRequest(url, JsonUtilFastjson.toJSONString(jsonObject), headar);
    }

    private static Request getRequest(String url, String controllerName, String actionName, String token, Object... objects) {
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("controllerName", controllerName);
        jsonObject.put("actionName", actionName);
        jsonObject.put("data", Arrays.asList(objects));
        jsonObject.put("token", token);
        return getRequest(url, jsonObject);
    }

    public static void execute(String controllerName, String actionName, CallBack<?> callBack, Object... objects) {
        execute(defualt_url, controllerName, actionName, callBack, objects);
    }

    /**
     * 表单提交形式
     *
     * @param url
     * @param callBack
     * @param map
     */
    public static Response executeForm(String url, CallBack<?> callBack, Map<String, Object> map) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //post方式提交的数据
        FormBody.Builder formBody = new FormBody.Builder();
        for (String key : map.keySet()) {
            formBody.add(key, map.get(key).toString());
        }
        Request request = new Request.Builder()
                .url(url)//请求的url
                .post(formBody.build())
                .build();
        //创建/Call
        try {
            return okHttpClient.newCall(request).execute();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static String httpGet(String url) {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        String str = null;
        try {
            response = httpClient.newCall(request).execute();
            str = response.body().string();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return str; // 返回的是string 类型，json的mapper可以直接处理
    }
}
