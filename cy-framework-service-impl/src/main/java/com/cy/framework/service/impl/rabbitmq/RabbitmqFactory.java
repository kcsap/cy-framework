package com.cy.framework.service.impl.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;

import javax.annotation.Resource;

public class RabbitmqFactory {
    @Resource
    public AmqpTemplate template;
}
