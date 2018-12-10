package com.cy.framework.pay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.cy.framework.pay.model.AliPayParam;
import com.cy.framework.pay.service.AliPayFrameworkService;
import com.cy.framework.util.CommonUtils;
import com.cy.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.UUID;

@Service
public class AliPayFrameworkServiceImpl implements AliPayFrameworkService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private AliPayParam aliPayParam;
    private AlipayClient alipayClient;

    @Override
    public void init() {
        if (alipayClient != null) {
            return;
        }
        alipayClient = new DefaultAlipayClient(aliPayParam.getUrl(), aliPayParam.getApp_id(), aliPayParam.getApp_private_key(), aliPayParam.getFormat(), aliPayParam.getCharset(), aliPayParam.getAlipay_public_key(), aliPayParam.getSign_type());
    }

    @Override
    public AlipayClient getAlipayClient() {
        init();
        return alipayClient;
    }

    @Override
    public String getAlipayAuthUrl(String app_id, String scope, String redirect_uri, String state) {
        init();
        StringBuilder stringBuilder = new StringBuilder(aliPayParam.getAuth_url());
        stringBuilder.append("?app_id=").append(app_id).append("&scope=").append(scope).append("&redirect_uri=").append(URLEncoder.encode(redirect_uri)).append("&state=").append(state);
        logger.warn("url=" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    @Override
    public String getAlipayAuthUrl() {
        return getAlipayAuthUrl(aliPayParam.getApp_id(), aliPayParam.getScope(), aliPayParam.getRedirect_url(), UUID.randomUUID().toString());
    }

    @Override
    public String app(String order_code, String title, String passbackParams, BigDecimal bigDecimal) throws AlipayApiException {
        init();
        AlipayTradeAppPayRequest alipayTradeAppPayRequest = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        if (StringUtil.isNotEmpty(passbackParams)) {
            model.setPassbackParams(passbackParams);
        }
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        // 描述信息
        if (StringUtil.isEmpty(order_code)) {
            throw new RuntimeException("订单号不能为空");
        }
        if (StringUtil.isEmpty(aliPayParam.getNotify_url())) {
            throw new RuntimeException("异步通知url不能为空");
        }
        model.setSubject(StringUtil.isEmpty(title) ? "订单-" + CommonUtils.generateOrderCode() : title); // 商品标题
        model.setOutTradeNo(order_code); // 商家订单编号
        model.setTimeoutExpress("30m"); // 超时关闭该订单时间
        model.setTotalAmount(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString()); // 订单总金额
        model.setProductCode("QUICK_MSECURITY_PAY"); // 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        alipayTradeAppPayRequest.setBizModel(model);
        alipayTradeAppPayRequest.setNotifyUrl(aliPayParam.getNotify_url());
        return alipayClient.sdkExecute(alipayTradeAppPayRequest).getBody();
    }

    @Override
    public String wap(String order_code, String title, String passbackParams, BigDecimal bigDecimal) throws AlipayApiException {
        init();
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        if (StringUtil.isNotEmpty(passbackParams)) {
            model.setPassbackParams(passbackParams);
        }
        if (StringUtil.isEmpty(order_code)) {
            throw new RuntimeException("订单号不能为空");
        }
        if (StringUtil.isEmpty(aliPayParam.getNotify_url())) {
            throw new RuntimeException("异步通知url不能为空");
        }
        if (StringUtil.isEmpty(aliPayParam.getReturn_url())) {
            throw new RuntimeException("返回的url不能为空");
        }
        model.setSubject(StringUtil.isEmpty(title) ? "订单-" + CommonUtils.generateOrderCode() : title); // 商品标题
        model.setOutTradeNo(order_code); // 商家订单编号
        model.setTimeoutExpress("30m"); // 超时关闭该订单时间
        model.setTotalAmount(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString()); // 订单总金额
        model.setProductCode("QUICK_WAP_PAY"); // 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        request.setBizModel(model);
        request.setNotifyUrl(aliPayParam.getNotify_url());
        request.setReturnUrl(aliPayParam.getReturn_url());
        return alipayClient.pageExecute(request).getBody();
    }

    public String pc(String order_code, String title, String passbackParams, BigDecimal bigDecimal) throws AlipayApiException {
        init();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        if (StringUtil.isNotEmpty(passbackParams)) {
            model.setPassbackParams(passbackParams);
        }
        if (StringUtil.isEmpty(order_code)) {
            throw new RuntimeException("订单号不能为空");
        }
        if (StringUtil.isEmpty(aliPayParam.getNotify_url())) {
            throw new RuntimeException("异步通知url不能为空");
        }
        if (StringUtil.isEmpty(aliPayParam.getReturn_url())) {
            throw new RuntimeException("返回的url不能为空");
        }
        model.setSubject(StringUtil.isEmpty(title) ? "订单-" + CommonUtils.generateOrderCode() : title); // 商品标题
        model.setOutTradeNo(order_code); // 商家订单编号
        model.setTimeoutExpress("30m"); // 超时关闭该订单时间
        model.setTotalAmount(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString()); // 订单总金额
        model.setProductCode("FAST_INSTANT_TRADE_PAY"); // 销售产品码，商家和支付宝签约的产品码，为固定值FAST_INSTANT_TRADE_PAY
        request.setBizModel(model);
        request.setNotifyUrl(aliPayParam.getNotify_url());
        request.setReturnUrl(aliPayParam.getReturn_url());
        return alipayClient.pageExecute(request).getBody();
    }
}
