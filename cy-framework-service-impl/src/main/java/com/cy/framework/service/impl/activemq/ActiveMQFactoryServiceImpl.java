package com.cy.framework.service.impl.activemq;

import com.cy.framework.model.ActiveMQParam;
import com.cy.framework.service.dao.ActiveMQFactoreService;
import com.cy.framework.service.impl.DataException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class ActiveMQFactoryServiceImpl extends DataException implements ActiveMQFactoreService {
    @Resource
    private ActiveMQParam activeMQParam;

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        //是否在每次尝试重新发送失败后,增长这个等待时间
        redeliveryPolicy.setUseExponentialBackOff(true);
        //重发次数,默认为6次   这里设置为10次
        redeliveryPolicy.setMaximumRedeliveries(10);
        //重发时间间隔,默认为1秒
        redeliveryPolicy.setInitialRedeliveryDelay(1);
        //第一次失败后重新发送之前等待500毫秒,第二次失败再等待500 * 2毫秒,这里的2就是value
        redeliveryPolicy.setBackOffMultiplier(3);
        //是否避免消息碰撞
        redeliveryPolicy.setUseCollisionAvoidance(false);
        //设置重发最大拖延时间-1 表示没有拖延只有UseExponentialBackOff(true)为true时生效
        redeliveryPolicy.setMaximumRedeliveryDelay(-1);
        return redeliveryPolicy;
    }

    @Bean
    @ConfigurationProperties(prefix = "service.activemq")
    public ActiveMQConnectionFactory connectionFactory(RedeliveryPolicy redeliveryPolicy) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(activeMQParam.getUser(), activeMQParam.getPassword(), "failover:(" + activeMQParam.getBrokerUrl() + ")");
        connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        connectionFactory.setTrustedPackages(new ArrayList(Arrays.asList(activeMQParam.getTrusted_Packages().split(","))));
//        connectionFactory.setTrustAllPackages(true);
        return connectionFactory;
    }

    /**
     * @param connectionFactory
     * @Description: Administrator
     * @Author:yangchengfu
     * @Date 2017\11\24 0024 15:43
     */
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }

    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate(ActiveMQConnectionFactory connectionFactory) {
        JmsMessagingTemplate template = new JmsMessagingTemplate(connectionFactory);
        JmsTemplate jmsTemplate = template.getJmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setDeliveryMode(1);//进行持久化配置 1表示非持久化，2表示持久化
        return template;
    }
}
