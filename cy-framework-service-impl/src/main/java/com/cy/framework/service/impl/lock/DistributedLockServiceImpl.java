package com.cy.framework.service.impl.lock;

import com.cy.framework.service.dao.RedisService;
import com.cy.framework.service.dao.lock.DistributedLockService;
import com.cy.framework.service.impl.DataException;
import com.cy.framework.util.CommonUtils;
import com.cy.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class DistributedLockServiceImpl extends RuntimeException implements DistributedLockService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private RedisService redisService;

    /**
     * 获取锁
     *
     * @param lockName       锁的名称
     * @param acquireTimeout 请求锁的超时时间 单位/毫秒
     * @param expire         锁的过期时间 单位/毫秒
     * @return
     */
    @Override
    public String withLock(String lockName, long acquireTimeout, long expire) throws DataException {
        long end = System.currentTimeMillis() + acquireTimeout;
        String keyValue = UUID.nameUUIDFromBytes(lockName.getBytes()).toString() + CommonUtils.getRandNum();
        while (System.currentTimeMillis() < end) {
            try {
                if (redisService.setNx(lockName.getBytes(), keyValue.getBytes(), expire)) {
                    logger.warn("获取锁......:" + lockName + ",成功");
                    //获取锁成功
                    return keyValue;
                }
                logger.warn("获取锁......:" + lockName);
                Thread.sleep(28);
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                //让当前线程中断
                Thread.currentThread().interrupt();
            }
        }
        releaseLock(lockName, keyValue);
        throw new DataException(3, "请求超时,请稍候再试...");
    }

    /**
     * 释放锁
     *
     * @param lockName 锁的名称
     * @param key      锁的唯一标识
     * @return
     */
    @Override
    public boolean releaseLock(String lockName, String key) {
        try {
            if (StringUtil.isEmpty(lockName) || StringUtil.isEmpty(key)) {
                return false;
            }
            if (key.equals(redisService.getStr(lockName))) {
                return redisService.del(lockName) > 0;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return false;
    }
}
