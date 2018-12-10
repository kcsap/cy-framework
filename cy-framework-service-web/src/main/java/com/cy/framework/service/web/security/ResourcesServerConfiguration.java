package com.cy.framework.service.web.security;

import com.cy.framework.model.security.SecurityOauth2Config;
import com.cy.framework.util.StringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API 权限控制
 */
@EnableResourceServer
@Configuration
public class ResourcesServerConfiguration extends ResourceServerConfigurerAdapter {
    @Resource
    private RedisConnectionFactory redisConnectionFactory;
    @Resource
    private SecurityOauth2Config securityOauth2Config;
    @Resource
    private TokenStore tokenStore;

    @Bean
    RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(securityOauth2Config.getResourceId()).tokenStore(tokenStore);
    }

    @Override

    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();
        if (!securityOauth2Config.getOauth()) {
            registry.antMatchers("/**").permitAll();
            return;
        }
        if (securityOauth2Config.getFilters() != null && securityOauth2Config.getFilters().size() > 0) {
            StringBuilder builder = new StringBuilder("/");
            for (String key : securityOauth2Config.getFilters().keySet()) {
                builder.setLength(0);
                builder.append("/" + key);
                List<String> list = securityOauth2Config.getFilters().get(key);
                if (list == null || list.size() <= 0) {
                    registry.antMatchers(builder.toString()).permitAll();
                    continue;
                }
                for (String value : list) {
                    if (StringUtil.isNotEmpty(value)) {
                        builder.append("/").append(value);
                    }
                    registry.antMatchers(builder.toString()).permitAll();
                }
            }
        }
        registry.antMatchers(
                HttpMethod.GET,
                "/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui.html**",
                "/webjars/**",
                "/favicon.ico",
                "/hystrix.stream**",
                "/actuator/**",
                "/info**",
                "/*.js",
                "/*.css",
                "/*.png",
                "/*.jpg",
                "/*.jpeg",
                "/hystrix",
                "/hystrix/images/*.png",
                "/webjars/jquery/2.1.1/jquery.min.js**"
        ).permitAll();
        registry.antMatchers("/auth/**").permitAll();
        registry.antMatchers(HttpMethod.GET, "/**").

                access("#oauth2.hasScope('read')")
                .

                        antMatchers(HttpMethod.POST, "/**").

                access("#oauth2.hasScope('write')")
                .

                        antMatchers(HttpMethod.PATCH, "/**").

                access("#oauth2.hasScope('write')")
                .

                        antMatchers(HttpMethod.PUT, "/**").

                access("#oauth2.hasScope('write')")
                .

                        antMatchers(HttpMethod.DELETE, "/**").

                access("#oauth2.hasScope('write')")
                .

                        and().

                headers().

                addHeaderWriter((request, response) ->

                {
                    response.addHeader("Access-Control-Allow-Origin", "*");
                    if (request.getMethod().equals("OPTIONS")) {
                        response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
                        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
                    }
                });
    }

    public static void main(String[] args) {
        SecurityOauth2Config securityOauth2Config = new SecurityOauth2Config();
        Map<String, List<String>> map2 = new HashMap<>();
        map2.put("product", Arrays.asList("insert", "update"));
        securityOauth2Config.setFilters(map2);
        if (securityOauth2Config.getFilters() != null && securityOauth2Config.getFilters().size() > 0) {
            StringBuilder builder = new StringBuilder("/");
            for (String key : securityOauth2Config.getFilters().keySet()) {
                for (String value : securityOauth2Config.getFilters().get(key)) {
                    builder.setLength(0);
                    builder.append("/" + key);
                    builder.append("/").append(value);
                    System.out.println(builder.toString());
                }
            }
        }
    }
}
