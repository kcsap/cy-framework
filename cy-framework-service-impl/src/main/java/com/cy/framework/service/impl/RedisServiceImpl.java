package com.cy.framework.service.impl;

import com.cy.framework.service.dao.RedisService;
import com.cy.framework.util.SerializeUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 封装redis 缓存服务器服务接口
 */
@Service
public class RedisServiceImpl<K, V> implements RedisService<K, V> {

    @Resource
    private RedisTemplate<K, V> redisTemplate;

    /**
     * @param keys
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public long del(final String... keys) {
        return (long) redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long result = 0;
                for (int i = 0; i < keys.length; i++) {
                    result = connection.del(keys[i].getBytes());
                }
                return result;
            }
        });
    }

    @Override
    public Boolean setNx(byte[] key, byte[] value, long liveTime) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                Boolean result = connection.setNX(key, value);
                if (result) {
                    connection.expire(key, liveTime);
                }
                logger.debug("set redis reuslt is:" + result + ",key is:" + new String(key));
                return result;
            }
        });
    }

    /**
     * @param key
     * @param value
     * @param liveTime
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void set(final byte[] key, final byte[] value, final long liveTime) {
        redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key, value);
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                logger.debug("set redis reuslt is true,key is:" + new String(key));
                return 1L;
            }
        });
    }

    /**
     * 描述： redis 存入 map类型的数据
     *
     * @param key
     * @param val
     * @param liveTime
     * @author yangchengfu
     * @DataTime 2017年6月15日 上午10:03:06
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setMap(final byte[] key, Map<byte[], byte[]> val, final long liveTime) {
        redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.hMSet(key, val);
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                return 1L;
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<byte[]> getMap(final byte[] key, byte[]... keys) {
        return (List<byte[]>) redisTemplate.execute(new RedisCallback() {
            public List<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hMGet(key, keys);
            }
        });
    }

    /**
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(String key, String value, long liveTime) {
        set(key.getBytes(), value.getBytes(), liveTime);
    }

    /**
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        this.set(key, value, 0L);
    }

    /**
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        this.set(key, value, 0L);
    }

    /**
     * @param key
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getStr(final String key) {

        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] value = connection.get(key.getBytes());
                String obj = (String) redisTemplate.getStringSerializer().deserialize(value);
                return obj;
            }
        });
    }

    /**
     * 通过正则匹配keys
     *
     * @param pattern
     * @return
     */
    @SuppressWarnings("unchecked")
    public Set<K> Setkeys(K pattern) {
        return redisTemplate.keys(pattern);

    }

    /**
     * @param key
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean exists(final String key) {

        return (boolean) redisTemplate.execute(new RedisCallback() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(key.getBytes());
            }
        });
    }

    /**
     * @param key
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public long getExpire(final String key) {

        return (long) redisTemplate.execute(new RedisCallback<Long>() {

            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    return TimeUnit.SECONDS.convert(connection.pTtl(key.getBytes()), TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    // Driver may not support pTtl or we may be running on Redis
                    // 2.4
                    return TimeUnit.SECONDS.convert(connection.ttl(key.getBytes()), TimeUnit.SECONDS);
                }
            }
        }, true);
    }

    /**
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String flushDB() {

        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    /**
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public long dbSize() {

        return (long) redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    /**
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String ping() {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.ping();
            }
        });
    }

    /**
     * @param key
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public byte[] getObjBytes(final String key) {

        return (byte[]) redisTemplate.execute(new RedisCallback() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] value = connection.get(key.getBytes());
                return value;
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map<byte[], byte[]> getMap(byte[] key) {
        // TODO Auto-generated method stub
        return (Map<byte[], byte[]>) redisTemplate.execute(new RedisCallback() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                // TODO Auto-generated method stub
                return connection.hGetAll(key);
            }
        });
    }

    @Override
    public boolean put(String key, String value) {
        // TODO Auto-generated method stub
        RedisConnection connection = null;
        try {
            connection = redisTemplate.getConnectionFactory().getConnection();
            connection.set(key.getBytes(), value.getBytes());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return true;
    }

    public Object getObj(String key) {
        return (byte[]) redisTemplate.execute(new RedisCallback() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] value = connection.get(key.getBytes());
                Object object = null;
                if (value != null) {
                    object = SerializeUtil.unserialize(value);
                } else {
                    object = value;
                }
                return object;
            }
        });
    }

    public String getString(String key) {
        byte[] b = null;
        RedisConnection connection = null;
        try {
            connection = redisTemplate.getConnectionFactory().getConnection();
            b = connection.get(key.getBytes());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return b == null ? null : new String(b);
    }

    public long delete(byte[]... keys) {
        RedisConnection connection = null;
        long l = 0L;
        try {
            connection = redisTemplate.getConnectionFactory().getConnection();
            l = connection.del(keys);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return l;
    }

}