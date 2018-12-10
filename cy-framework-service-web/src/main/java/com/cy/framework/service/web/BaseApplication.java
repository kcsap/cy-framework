package com.cy.framework.service.web;

import com.cy.framework.util.StringUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
public class BaseApplication extends com.cy.framework.service.web.DruidDataSource {
    @Value("${service.mapper.location}")
    private String locationPattern;

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // 提供SqlSeesion
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(com.alibaba.druid.pool.DruidDataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        if (StringUtil.isEmpty(locationPattern)) {
            throw new NullPointerException("locationPattern is null");
        }
        String[] strings = locationPattern.split(",");
        List<Resource> list = new ArrayList<>();
        for (String path : strings) {
            list.addAll(Arrays.asList(resolver.getResources(path)));
        }
        sqlSessionFactoryBean.setMapperLocations(list.toArray(new Resource[]{}));
//        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new MybatiesInterceptor()});
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public ConcurrentTaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(1000);
        threadPoolTaskExecutor.setQueueCapacity(35);
        threadPoolTaskExecutor.setKeepAliveSeconds(3000);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }

    @Bean
    public PlatformTransactionManager transactionManager(com.alibaba.druid.pool.DruidDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
