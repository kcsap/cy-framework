package com.cy.framework.mybaties.dao;


import org.apache.activemq.command.ActiveMQQueue;

public interface ActiveMQQueueMapper extends BasePublicMapper {
    /**
     * @mbggenerated 2017-11-24
     */
    int deleteByPrimaryKey(Integer queueId);

    /**
     * @mbggenerated 2017-11-24
     */
    int insert(ActiveMQQueue record);

    /**
     * @mbggenerated 2017-11-24
     */
    int insertSelective(ActiveMQQueue record);

    /**
     * @mbggenerated 2017-11-24
     */
    ActiveMQQueue selectByPrimaryKey(Integer queueId);

    /**
     * @mbggenerated 2017-11-24
     */
    int updateByPrimaryKeySelective(ActiveMQQueue record);

    /**
     * @mbggenerated 2017-11-24
     */
    int updateByPrimaryKey(ActiveMQQueue record);
}