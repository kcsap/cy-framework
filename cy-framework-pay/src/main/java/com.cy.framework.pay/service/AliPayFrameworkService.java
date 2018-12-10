package com.cy.framework.pay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;

import java.math.BigDecimal;

public interface AliPayFrameworkService {
    void init();

    AlipayClient getAlipayClient();

    String getAlipayAuthUrl();

    String getAlipayAuthUrl(String app_id, String scope, String redirect_uri, String state);

    String app(String order_code, String title, String passbackParams, BigDecimal bigDecimal) throws AlipayApiException;

    String wap(String order_code, String title, String passbackParams, BigDecimal bigDecimal) throws AlipayApiException;

    String pc(String order_code, String title, String passbackParams, BigDecimal bigDecimal) throws AlipayApiException;
}
