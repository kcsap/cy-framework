package com.cy.framework.service.web;


import com.cy.framework.model.redis.RedisCacheParam;
import com.cy.framework.service.dao.RedisService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * redis 分布式缓存数据入口
 */
@RestController
@RequestMapping("redis")
public class RedisController {
    @Resource
    private RedisService redisService;

    @RequestMapping(value = "putData", method = RequestMethod.POST)
    public void put(@RequestBody RedisCacheParam cacheParam) {
        redisService.cachePut(cacheParam);
    }

    @RequestMapping(value = "getData", method = RequestMethod.GET)
    public String get(@RequestParam String key) {
        return redisService.getStr(key);
    }

    @RequestMapping(value = "removeData", method = RequestMethod.GET)
    public void remove(@RequestParam String key) {
        redisService.remove(key);
    }
}