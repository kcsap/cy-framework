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
    @Resource
    private RedisService redisService;

    @Override
    public String generate(String key) {
        // TODO Auto-generated method stub
        return MD5Util.GetMD5Code(key + FinalConfigParam.KEY);
    }

    @Override
    public String generateUserId(String key) {
        String string=redisService.getStr(FinalConfigParam.USER_ID_KEY);
        if(StringUtil.isEmpty(string)){
            string="0";
        }
        string=(Integer.parseInt(string)+1)+"";
        redisService.set(FinalConfigParam.USER_ID_KEY,string);
        return StringUtil.isEmpty(key) ? TimeUtil.formatYYYMMDD(new Date().getTime())+string : key+string;
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
            byte[] validate = redisService.getObjBytes(token);
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
    public Object getUserInfo(String token) throws Exception {
        Object obj = null;
        try {
            byte[] b = redisService.getObjBytes(token);
            obj = SerializeUtil.unserialize(b);
            if (obj == null) {
                throw new Exception("用户未登录");
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.warn("validate token Exception", e);
            throw new Exception("用户未登录");
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
    public Object getUserInfo(HttpServletRequest request) throws Exception {
        // TODO Auto-generated method stub
        if (request.getAttribute("token") == null || request.getAttribute("token").toString().isEmpty()) {
            return null;
        }
        return getUserInfo(request.getAttribute("token").toString());
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
        redisService.del(key1);// 删除用户1和用户2的token
        redisService.del(key2);
        redisService.set(putKey.getBytes(), SerializeUtil.serialize(userInfoEntity), FinalConfigParam.EXPIRE);
        redisService.set(builder, putKey, FinalConfigParam.EXPIRE);
        return putKey;
    }
}
