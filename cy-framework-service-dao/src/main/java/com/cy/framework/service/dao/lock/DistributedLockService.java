package com.cy.framework.service.dao.lock;

/**
 * 分布式锁的实现
 */
public interface DistributedLockService {
    /**
     * 获取锁-加锁
     *
     * @param lockName       锁的名称
     * @param acquireTimeout 请求锁的超时时间
     * @param timeout        锁的过期时间
     * @return
     */
    String withLock(String lockName, long acquireTimeout, long timeout);

    /**
     * 释放锁
     *
     * @param lockName 锁的名称
     * @param key      锁的唯一标识
     * @return
     */
    boolean releaseLock(String lockName, String key);
}

