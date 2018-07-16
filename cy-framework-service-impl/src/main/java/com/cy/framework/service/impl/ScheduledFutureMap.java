package com.cy.framework.service.impl;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * 倒计时处理
 */
@Component
public class ScheduledFutureMap {
    private Map<String, Map<String, ScheduledFuture>> dataMap = new HashMap<>();

    public void put(String key, String userId, ScheduledFuture future) {
        if (!dataMap.containsKey(key)) {
            dataMap.put(key, new HashMap<>());
        }
        if (dataMap.get(key).containsKey(userId)) {
            if (dataMap.get(key).get(userId) != null) {
                dataMap.get(key).get(userId).cancel(true);
                LoggerFactory.getLogger(this.getClass()).warn("存在倒计时:" + userId + ",取消倒计时");
            }
        }
        dataMap.get(key).put(userId, future);
    }

    public void remove(String code, String userId) {
        if (dataMap.containsKey(code)) {
            dataMap.get(code).remove(userId);
            if (dataMap.get(code).size() == 0) {
                dataMap.remove(code);
            }
        }
    }

    public void remove(String code) {
        dataMap.remove(code);
    }

    public void cancel(String code, String userId) {
        if (dataMap.containsKey(code)) {
            LoggerFactory.getLogger(this.getClass()).warn("直接取消存在倒计时:" + userId + ",取消倒计时");
            try {
                if (dataMap.get(code).get(userId) != null) {
                    dataMap.get(code).get(userId).cancel(true);
                    dataMap.get(code).remove(userId);
                }
            } catch (Exception e) {
                LoggerFactory.getLogger("空对象");
            }
        }
    }
}
