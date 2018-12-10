package com.cy.framework.pay.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(
        prefix = "service.pay.wx"
)
public class WxPayParam {
    /**
     * appid 公众号
     */
    private String gAppId;
    /**
     * 小程序的 appid
     */
    private String xAppid;
    /**
     * 公众号 的appSecret
     */
    private String gAppSecret;
    /**
     * 小程序的 appSecret
     */
    private String xAppSecret;
    /**
     *  app 的appid
     */
    private String appId;
    /**
     * app的 appSecret
     */
    private String appSecret;
    /**
     * 商户ID
     */
    private String machId;
    /**
     * 秘钥
     */
    private String key;
    /**
     * 证书的路径
     */
    private String certPath;

    /**
     * 异步通知的 url
     */

    private String notify_url;

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getgAppId() {
        return gAppId;
    }

    public void setgAppId(String gAppId) {
        this.gAppId = gAppId;
    }

    public String getxAppid() {
        return xAppid;
    }

    public void setxAppid(String xAppid) {
        this.xAppid = xAppid;
    }

    public String getgAppSecret() {
        return gAppSecret;
    }

    public void setgAppSecret(String gAppSecret) {
        this.gAppSecret = gAppSecret;
    }

    public String getxAppSecret() {
        return xAppSecret;
    }

    public void setxAppSecret(String xAppSecret) {
        this.xAppSecret = xAppSecret;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMachId() {
        return machId;
    }

    public void setMachId(String machId) {
        this.machId = machId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }
}
