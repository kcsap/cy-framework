package com.cy.framework.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayConfig;

import java.util.Map;

public interface WxPayFrameworkService{
    Map<String, Object> getWxData();

    JSONObject getSessionKeyOropenid(String code);

    JSONObject getUserInfo(String encryptedData, String sessionKey, String iv);

    String returnXml(String code, String message);

    WXPayConfig getWxPayConfig(Integer type);
}
