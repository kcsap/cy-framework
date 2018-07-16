package com.cy.framework.service.web;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;

public class DruidDataSource {
    @Value(value = "${spring.datasource.registration.addInitParameter.allow}")
    private String allow;
    @Value(value = "${spring.datasource.registration.addInitParameter.deny}")
    private String deny;
    @Value(value = "${spring.datasource.registration.addInitParameter.loginUsername}")
    private String loginUsername;
    @Value(value = "${spring.datasource.registration.addInitParameter.loginPassword}")
    private String loginPassword;
    @Value(value = "${spring.datasource.registration.addInitParameter.resetEnable}")
    private String resetEnable;
    @Value(value = "${spring.datasource.registration.addUrlPatterns}")
    private String addUrlPatterns;
    @Value(value = "${spring.datasource.registration.url}")
    private String url;
    
    /**
     * 注册一个StatViewServlet
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean DruidStatViewServle2() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), url);
        //添加初始化参数：initParams
        /** 白名单，如果不配置或value为空，则允许所有 */
        servletRegistrationBean.addInitParameter("allow", allow);
        /** 黑名单，与白名单存在相同IP时，优先于白名单 */
        servletRegistrationBean.addInitParameter("deny", deny);
        /** 用户名 */
        servletRegistrationBean.addInitParameter("loginUsername", loginUsername);
        /** 密码 */
        servletRegistrationBean.addInitParameter("loginPassword", loginPassword);
        /** 禁用页面上的“Reset All”功能 */
        servletRegistrationBean.addInitParameter("resetEnable", resetEnable);
        return servletRegistrationBean;
    }

    /**
     * 注册一个：WebStatFilter
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean druidStatFilter2() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

        /** 过滤规则 */
        filterRegistrationBean.addUrlPatterns(addUrlPatterns);
        /** 忽略资源 */
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico," + url);
        return filterRegistrationBean;
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public com.alibaba.druid.pool.DruidDataSource dataSource() {
        com.alibaba.druid.pool.DruidDataSource druidDataSource = new com.alibaba.druid.pool.DruidDataSource();
        druidDataSource.setConnectionInitSqls(Arrays.asList(new String[]{"select 1"}));
        return druidDataSource;
    }
}
