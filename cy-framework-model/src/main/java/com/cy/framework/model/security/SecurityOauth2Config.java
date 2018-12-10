package com.cy.framework.model.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(
        prefix = "service.security"
)
@Component
public class SecurityOauth2Config {
    /**
     * 资源的ID
     */
    private String resourceId;
    /**
     * 是否需要授权
     */
    private Boolean oauth = false;
    /**
     * 过滤的url
     * [{
     * product:[insert,update]
     * }]
     */
    private Map<String, List<String>> filters;

    public Boolean getOauth() {
        return oauth;
    }

    public void setOauth(Boolean oauth) {
        this.oauth = oauth;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Map<String, List<String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, List<String>> filters) {
        this.filters = filters;
    }
}
