package com.cy.framework.util.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class CallBack<T> implements Callback {
    public abstract void success(T t, List<T> ts, Call call);

    public abstract void error(JSONObject jsonObject, Call call);

    public abstract void failure(Call call, IOException e);

    @Override
    public void onFailure(Call call, IOException e) {
        System.out.println(e);
        failure(call, e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        JSONObject jsonObject = JSON.parseObject(response.body().string());
        if (!"0000".equals(jsonObject.getString("code"))) {
            error(jsonObject, call);
            return;
        }
        System.out.println(jsonObject.toJSONString());
        Object object = jsonObject.get("data");
        Class<T> tClass = null;
        try {
            tClass = (Class<T>) Class.forName(((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName());
        } catch (Exception e) {
            System.out.println(e);
        }
        if (object instanceof List) {
            success(null, JSONArray.parseArray(object.toString(), tClass), call);
        } else {
            success(jsonObject.getObject("data", tClass), null, call);
        }
    }
}
