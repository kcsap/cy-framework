package com.cy.framework.service.impl.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Configuration
@EnableAutoConfiguration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    private static Logger logger = Logger.getLogger(RedisConfig.class);
    @Resource
    private RedisProperties param;
//
//    @Bean
//    public JedisPoolConfig getRedisConfig() {
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxIdle(param.getPool().getMaxIdle());
//        config.setMinIdle(param.getPool().getMinIdle());
//        config.setMaxWaitMillis(param.getPool().getMaxWait());
//        config.setMaxTotal(param.getPool().getMaxActice());
//        return config;
//    }
//
//    /**
//     * 单机-集群模式
//     *
//     * @param jedisPoolConfig
//     * @return
//     */
//    @Bean
//    public JedisCluster getJedisCluster(JedisPoolConfig jedisPoolConfig) {
//        String[] cNodes = param.getClusters().getNodes().split(",");
//        Set<HostAndPort> nodes = new HashSet<>();
//        //分割出集群节点
//        for (String node : cNodes) {
//            String[] hp = node.split(":");
//            nodes.add(new HostAndPort(hp[0], Integer.parseInt(hp[1])));
//        }
//        //创建集群对象
//        return new JedisCluster(nodes, param.getCommandTimeout(), 1000, 1, param.getPassword(), jedisPoolConfig);
//    }
//
//    public JedisSentinelPool getJedisSentinelPool(JedisPoolConfig jedisPoolConfig) {
//
//    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory);
        setSerializer(template);//设置序列化工具
        template.afterPropertiesSet();
        return template;
    }

    private void setSerializer(StringRedisTemplate template) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
    }

    public RedisCacheConfiguration getRedisCacheConfiguration() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
        return cacheConfiguration;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory).cacheDefaults(getRedisCacheConfiguration()).build();
        return cacheManager;
    }

    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };

    }
}
