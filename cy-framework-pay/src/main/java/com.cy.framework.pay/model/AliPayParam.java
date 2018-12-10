package com.cy.framework.pay.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(
        prefix = "service.pay.ali"
)
public class AliPayParam {
    /**
     * 请求的url
     */
    private String url;
    private String scope;
    /**
     * 支付宝授权认证的url
     */
    private String auth_url;
    /**
     * 授权回调的url
     */
    private String redirect_url;
    /**
     * 异步通知的url
     */
    private String notify_url;
    /**
     * pc端/h5 支付完成返回的url
     */
    private String return_url;
    /**
     * 商户的appid
     */
    private String app_id;
    /**
     * 商户的私钥
     */
    private String app_private_key;
    /**
     * 商户的公钥
     */
    private String app_public_key;
    /**
     * 支付宝的公钥
     */
    private String alipay_public_key;
    /**
     * 签名的类型
     */
    private String sign_type;
    /**
     * 格式化数据的类型
     */
    private String format="JSON";
    /**
     * 字符类型 默认utf
     */
    private String charset="UTF-8";

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuth_url() {
        return auth_url;
    }

    public void setAuth_url(String auth_url) {
        this.auth_url = auth_url;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_private_key() {
        return app_private_key;
    }

    public void setApp_private_key(String app_private_key) {
        this.app_private_key = app_private_key;
    }

    public String getApp_public_key() {
        return app_public_key;
    }

    public void setApp_public_key(String app_public_key) {
        this.app_public_key = app_public_key;
    }

    public String getAlipay_public_key() {
        return alipay_public_key;
    }

    public void setAlipay_public_key(String alipay_public_key) {
        this.alipay_public_key = alipay_public_key;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
