package com.cy.framework.service.impl.redis.cache;

import com.cy.framework.model.redis.RedisCacheParam;

public interface CacheData {

    void put(RedisCacheParam param);

    Object get(String key);

    void remove(String key);
}
