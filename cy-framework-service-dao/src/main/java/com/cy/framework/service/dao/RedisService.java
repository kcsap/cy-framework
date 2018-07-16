package com.cy.framework.service.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService<K, V> {
    Logger logger = LoggerFactory.getLogger(RedisService.class);

    /**
     * 描述： 获取Map
     *
     * @param key
     * @param keys
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月19日 下午5:45:06
     */
    List<byte[]> getMap(final byte[] key, byte[]... keys);

    /**
     * 描述： 获取map
     *
     * @param key
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月19日 下午5:47:09
     */
    Map<byte[], byte[]> getMap(final byte[] key);

    /**
     * 通过key删除
     *
     * @param keys
     */
    long del(String... keys);

    /**
     * 添加key value 并且设置存活时间(byte)
     *
     * @param key
     * @param value
     * @param liveTime 单位 秒
     */
    void set(byte[] key, byte[] value, long liveTime);

    /**
     * 分布式锁 使用
     *
     * @param key      名称
     * @param value    值
     * @param liveTime 过期时间
     */
    Boolean setNx(byte[] key, byte[] value, long liveTime);

    /**
     * 描述： 存取 map对象
     *
     * @param key
     * @param val
     * @param liveTime
     * @author yangchengfu
     * @DataTime 2017年6月19日 下午5:38:29
     */
    void setMap(final byte[] key, Map<byte[], byte[]> val, final long liveTime);

    /**
     * 添加key value 并且设置存活时间
     *
     * @param key
     * @param value
     * @param liveTime 单位秒
     */
    void set(String key, String value, long liveTime);

    /**
     * 添加key value
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * 添加key value (字节)(序列化)
     *
     * @param key
     * @param value
     */
    void set(byte[] key, byte[] value);

    /**
     * 获取redis value (String)
     *
     * @param key
     * @return
     */
    String getStr(String key);

    /**
     * 通过正则匹配keys
     *
     * @param pattern
     * @return
     */
    Set<K> Setkeys(K pattern);

    /**
     * 检查key是否已经存在
     *
     * @param key
     * @return
     */
    boolean exists(String key);

    /**
     * 获得key的过期时间
     *
     * @param key
     * @return
     */
    long getExpire(final String key);

    /**
     * 清空redis 所有数据
     *
     * @return
     */
    String flushDB();

    /**
     * 查看redis里有多少数据
     */
    long dbSize();

    /**
     * 检查是否连接成功
     *
     * @return
     */
    String ping();

    /**
     * 根据key获取值
     *
     * @param key
     * @return
     * @Title: getObjBytes
     * @Description: 转换成对象 样例 UserDO u1 = (UserDO)
     * SerializeUtil.unserialize(value);
     * @return: byte[]
     */
    byte[] getObjBytes(final String key);

    /**
     * 描述： 不要事物的添加
     *
     * @param key
     * @param value
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月21日 上午11:51:33
     */
    boolean put(String key, String value);

    Object getObj(String key);

    long delete(byte[]... keys);

    String getString(String key);
}