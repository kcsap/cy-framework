package com.cy.framework.service.impl;

import com.cy.framework.service.dao.RedisService;
import com.cy.framework.service.dao.UserTokenService;
import com.cy.framework.util.FinalConfigParam;
import com.cy.framework.util.SerializeUtil;
import com.cy.framework.util.StringUtil;
import com.cy.framework.util.safe.MD5Util;
import com.cy.framework.util.time.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;

@Service
public class UserTokenServiceImpl implements UserTokenService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String access_token = "token:";
    private final String user_id = "user_id:";
    @Resource
    private RedisService redisService;

    @Override
    public String generate(String key) {
        // TODO Auto-generated method stub
        return MD5Util.GetMD5Code(key + FinalConfigParam.KEY);
    }

    @Override
    public String generateUserId(String key) {
        String string = redisService.getStr(FinalConfigParam.USER_ID_KEY);
        if (StringUtil.isEmpty(string)) {
            string = "0";
        }
        string = (Integer.parseInt(string) + 1) + "";
        redisService.set(FinalConfigParam.USER_ID_KEY, string);
        return StringUtil.isEmpty(key) ? TimeUtil.formatYYYMMDD(new Date().getTime()) + string : key + string;
    }

    @Override
    public String generateUserId() {
        return generateUserId(null);
    }

    @Override
    public boolean validate(String token) {
        // TODO Auto-generated method stub
        boolean result = false;
        try {
            byte[] validate = redisService.getObjBytes(access_token + token);
            result = !(validate == null || validate.length <= 0);
        } catch (Exception e) {
            // TODO: handle exception
            logger.warn("validate token Exception", e);
        }
        return result;
    }

    /**
     * 描述： 获取用户信息
     *
     * @param token
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月15日 下午12:42:40
     */
    @Override
    public Object getUserInfo(String token) {
        Object obj = null;
        try {
            byte[] b = redisService.getObjBytes(access_token + token);
            obj = SerializeUtil.unserialize(b);
            if (obj == null) {
                throw new Exception("用户未登录");
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.warn("validate token Exception", e);
        }
        return obj;
    }

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public Object getUserInfo(HttpServletRequest request) {
        // TODO Auto-generated method stub
        if (request.getHeader("token") == null || request.getHeader("token").isEmpty()) {
            return null;
        }
        return getUserInfo(request.getHeader("token"));
    }

    @Override
    public String putTokenUserInfo(String builder, Serializable userInfoEntity) {
        String key1 = MD5Util.GetMD5Code(builder + FinalConfigParam.LOGIN_KEYYES);// 1
        String key2 = MD5Util.GetMD5Code(builder + FinalConfigParam.LOGIN_KEYNO);// 2
        String putKey = null;
        byte[] token = redisService.getObjBytes(key1);// 用户1
        if (token == null || token.length <= 0) {
            // 1用户未登录
            byte[] token2 = redisService.getObjBytes(key2);// 用户2
            if (token2 == null || token2.length <= 0) {
                // 2用户未登录
                putKey = key2;// 生成1用户的key
            } else {
                // 2用户登录状态
                putKey = key1;
            }
        } else {
            // 1用户已登录
            putKey = key2;

        }
        redisService.del((access_token + key1));// 删除用户1和用户2的token
        redisService.del((access_token + key2));
        redisService.set((access_token + putKey).getBytes(), SerializeUtil.serialize(userInfoEntity), FinalConfigParam.EXPIRE);
        redisService.set((user_id + builder).getBytes(), putKey.getBytes(), FinalConfigParam.EXPIRE);
        return putKey;
    }

    @Override
    public String putTokenUserInfo(String key, boolean one, Serializable userInfoEntity) {
        String key1 = MD5Util.GetMD5Code(key + FinalConfigParam.LOGIN_KEYYES);
        byte[] token = redisService.getObjBytes(access_token + key1);
        if (token == null || token.length <= 0) {
            redisService.set((access_token + key1).getBytes(), SerializeUtil.serialize(userInfoEntity), FinalConfigParam.EXPIRE);
            redisService.set((user_id + key).getBytes(), key1.getBytes(), FinalConfigParam.EXPIRE);
        }
        return key1;
    }
}
