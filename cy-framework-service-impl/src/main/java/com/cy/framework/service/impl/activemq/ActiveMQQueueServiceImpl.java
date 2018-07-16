package com.cy.framework.service.impl.activemq;

import com.cy.framework.mybaties.dao.ActiveMQQueueMapper;
import com.cy.framework.service.dao.ActiveMQQueueService;
import com.cy.framework.service.impl.BasePublicActiveMQServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ActiveMQQueueServiceImpl extends BasePublicActiveMQServiceImpl implements ActiveMQQueueService {
    @Resource
    private ActiveMQQueueMapper mapper;

    @Override
    public void init() {
        setMapper(mapper, this.getClass());
    }

}
