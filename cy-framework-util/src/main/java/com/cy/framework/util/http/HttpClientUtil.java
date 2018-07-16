package com.cy.framework.util.http;

import com.alibaba.fastjson.JSONObject;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpClientUtil {
    private final static String defualt_url = "http://47.98.133.241:1080/service";

    public static void execute(String url, String controllerName, String actionName, CallBack<?> callBack, Object... objects) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("controllerName", controllerName);
        jsonObject.put("actionName", actionName);
        jsonObject.put("data", Arrays.asList(objects));
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)//请求的url
                .post(requestBody)
                .build();
        //创建/Call
        try {
            okHttpClient.newCall(request).enqueue(callBack);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void execute(String controllerName, String actionName, CallBack<?> callBack, Object... objects) {
        execute(defualt_url, controllerName, actionName, callBack, objects);
    }

    /**
     * 表单提交形式
     * @param url
     * @param callBack
     * @param map
     */
    public static Response executeForm(String url, CallBack<?> callBack, Map<String,Object> map) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //post方式提交的数据
        FormBody.Builder formBody = new FormBody.Builder();
        for(String key:map.keySet()){
            formBody.add(key,map.get(key).toString());
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

}
