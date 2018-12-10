package com.cy.framework.model.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(
        prefix = "service.swagger"
)
@RefreshScope
public class SwaggerParam {
    /**
     * 扫描的包
     */
    private String basePackage;
    /**
     * 页面的标题
     */
    private String title;
    /**
     * 创建人的名称
     */
    private String contactName;
    /**
     * 创建人的链接
     */
    private String contactUrl;
    /**
     * 创建人的邮箱
     */
    private String email;
    /**
     * 版本号
     */
    private String version = "1.0";
    /**
     * 描述
     */
    private String description;
    /**
     * 认证服务器
     */
    private String authServer;

    public String getAuthServer() {
        return authServer;
    }

    public void setAuthServer(String authServer) {
        this.authServer = authServer;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
