package com.cy.framework.service.impl.redis;

import com.cy.framework.service.impl.ApplicationContextHolder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * redis+mybaties 二级缓存 集群缓存/不考虑分布式
 */
public class RedisMybatiesCache implements Cache {
    private static Logger logger = LoggerFactory.getLogger(RedisMybatiesCache.class);
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final String id; // cache instance id
    private RedisTemplate redisTemplate;
    private static final long EXPIRE_TIME_IN_MINUTES = 30; // redis过期时间/秒

    public RedisMybatiesCache(String id) {
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
    @SuppressWarnings("unchecked")
    public void putObject(Object key, Object value) {
        RedisTemplate redisTemplate = getRedisTemplate();
        ValueOperations opsForValue = redisTemplate.opsForValue();
        CacheKey cacheKey = (CacheKey) key;
        String key2 = cacheKey.hashCode() + "";
        //设置过期时间，比xml的cache设置优先级高一些
        opsForValue.set(key2, value, EXPIRE_TIME_IN_MINUTES, TimeUnit.SECONDS);
        logger.debug("Put query result to redis");
    }

    /**
     * Get cached query result from redis
     */
    @Override
    public Object getObject(Object key) {
        RedisTemplate redisTemplate = getRedisTemplate();
        CacheKey cacheKey = (CacheKey) key;
        String key2 = cacheKey.hashCode() + "";
        ValueOperations opsForValue = redisTemplate.opsForValue();
        logger.debug("Get cached query result from redis");
        return opsForValue.get(key2);
    }

    /**
     * Remove cached query result from redis
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object removeObject(Object key) {
        RedisTemplate redisTemplate = getRedisTemplate();
        CacheKey cacheKey = (CacheKey) key;
        String key2 = cacheKey.hashCode() + "";
        redisTemplate.delete(key2);
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
