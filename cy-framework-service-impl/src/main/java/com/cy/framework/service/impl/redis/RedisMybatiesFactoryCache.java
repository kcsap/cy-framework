package com.cy.framework.service.impl.redis;

import com.cy.framework.model.redis.RedisCacheParam;
import com.cy.framework.service.impl.ApplicationContextHolder;
import com.cy.framework.service.impl.redis.cache.CacheData;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Base64Utils;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * redis+mybaties 二级缓存 分布式缓存
 */
public abstract class RedisMybatiesFactoryCache implements Cache, CacheData {
    private static Logger logger = LoggerFactory.getLogger(RedisMybatiesFactoryCache.class);
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final String id; // cache instance id
    private RedisTemplate redisTemplate;

    public RedisMybatiesFactoryCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Put query result to redis
     */
    @Override
    public void putObject(Object key, Object value) {
        RedisTemplate redisTemplate = getRedisTemplate();
        CacheKey cacheKey = (CacheKey) key;
        String key2 = cacheKey.hashCode() + "";
        put(new RedisCacheParam(key2, redisTemplate.getValueSerializer().serialize(value)));
        logger.debug("Put query result to redis");
    }

    /**
     * Get cached query result from redis
     */
    @Override
    public Object getObject(Object key) {
        CacheKey cacheKey = (CacheKey) key;
        String key2 = cacheKey.hashCode() + "";
        Object ret = get(key2);
        if (ret != null && ret instanceof String) {
            ret = ret.toString().replaceAll("\"", "");
            ret = redisTemplate.getValueSerializer().deserialize(Base64Utils.decodeFromString(ret.toString()));
        }
        logger.debug("Get cached query result from redis");
        return ret;
    }

    /**
     * Remove cached query result from redis
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object removeObject(Object key) {
        CacheKey cacheKey = (CacheKey) key;
        String key2 = cacheKey.hashCode() + "";
        remove(key2);
        logger.debug("Remove cached query result from redis");
        return null;
    }

    /**
     * Clears this cache instance
     */
    @Override
    public void clear() {
        RedisTemplate redisTemplate = getRedisTemplate();
        redisTemplate.execute((RedisCallback) connection -> {
            connection.flushDb();
            return null;
        });
        logger.debug("Clear all the cached query result from redis");
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    @Bean
    private RedisTemplate getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = ApplicationContextHolder.getBean("redisTemplate");
        }
        return redisTemplate;
    }
}
