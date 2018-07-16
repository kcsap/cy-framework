package com.cy.framework.model;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@ConfigurationProperties(
        prefix = "service.activemq"
)
public class ActiveMQParam extends ActiveMQConnectionFactory implements Serializable {
    private static final long serialVersionUID = -6864989712499121605L;
    /**
     * 集群的ip 多个使用逗号隔开
     */
    private String brokerUrl;
    private boolean inMemory = true;
    /**
     * 用户名
     */
    private String user;
    /**
     * 密码
     */
    private String password;
    /**
     * 可访问的包名
     */
    private String trusted_Packages="java.lang,java.util,com.alibaba.fastjson,net.sf.json,java.math";
    /**
     * 连接池
     */
    private ActiveMQParam.Pool pool = new ActiveMQParam.Pool();
    private RedeliveryPolicy policy=new RedeliveryPolicy();
    public class Pool {
        private boolean enabled;
        private int maxConnections = 5;
        private int idleTimeout = 30000;
        private long expiryTimeout = 0L;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getMaxConnections() {
            return maxConnections;
        }

        public void setMaxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
        }

        public int getIdleTimeout() {
            return idleTimeout;
        }

        public void setIdleTimeout(int idleTimeout) {
            this.idleTimeout = idleTimeout;
        }

        public long getExpiryTimeout() {
            return expiryTimeout;
        }

        public void setExpiryTimeout(long expiryTimeout) {
            this.expiryTimeout = expiryTimeout;
        }
    }

    public class RedeliveryPolicy extends org.apache.activemq.RedeliveryPolicy {

    }

    public RedeliveryPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(RedeliveryPolicy policy) {
        this.policy = policy;
    }

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public boolean isInMemory() {
        return inMemory;
    }

    public void setInMemory(boolean inMemory) {
        this.inMemory = inMemory;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public String getTrusted_Packages() {
        return trusted_Packages;
    }

    public void setTrusted_Packages(String trusted_Packages) {
        this.trusted_Packages = trusted_Packages;
    }
}

