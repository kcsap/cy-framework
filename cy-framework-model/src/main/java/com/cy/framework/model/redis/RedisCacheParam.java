package com.cy.framework.model.redis;

public class RedisCacheParam {
    /**
     * 键
     */
    private String key;
    /**
     * 值
     */
    private Object value;
    /**
     * redis key的有效期 30秒
     */
    private int minutes=30;

    public RedisCacheParam() {
    }

    public RedisCacheParam(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
