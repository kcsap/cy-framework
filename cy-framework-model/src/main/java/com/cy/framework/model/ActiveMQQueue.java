package com.cy.framework.model;

import java.util.Date;

public class ActiveMQQueue {
    /**
     * 队列的id
     */
    private Integer queueId;

    /**
     * 队列的名称
     */
    private String name;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 创建的时间
     */
    private Date createTime;

    /**
     * 描述
     */
    private String descr;

    /**
     * 队列的id
     * @return queue_id 队列的id
     */
    public Integer getQueueId() {
        return queueId;
    }

    /**
     * 队列的id
     * @param queueId 队列的id
     */
    public void setQueueId(Integer queueId) {
        this.queueId = queueId;
    }

    /**
     * 队列的名称
     * @return name 队列的名称
     */
    public String getName() {
        return name;
    }

    /**
     * 队列的名称
     * @param name 队列的名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 方法名称
     * @return method 方法名称
     */
    public String getMethod() {
        return method;
    }

    /**
     * 方法名称
     * @param method 方法名称
     */
    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    /**
     * 创建的时间
     * @return create_time 创建的时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建的时间
     * @param createTime 创建的时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 描述
     * @return descr 描述
     */
    public String getDescr() {
        return descr;
    }

    /**
     * 描述
     * @param descr 描述
     */
    public void setDescr(String descr) {
        this.descr = descr == null ? null : descr.trim();
    }
}