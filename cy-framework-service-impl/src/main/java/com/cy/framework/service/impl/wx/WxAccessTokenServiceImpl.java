package com.cy.framework.service.impl.wx;

import com.cy.framework.service.dao.RedisService;
import com.cy.framework.service.dao.wx.WxAccessTokenService;
import com.cy.framework.service.impl.DataException;
import com.cy.framework.util.SerializeUtil;
import com.cy.framework.util.StringUtil;
import com.cy.framework.util.http.HttpsClientApplication;
import com.cy.framework.util.param.WechatTokenParam;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class WxAccessTokenServiceImpl implements WxAccessTokenService {
    @Resource
    private RedisService redisService;

    @Override
    public WechatTokenParam getAccessToken(String appid, String secret) {
        if (StringUtil.isEmpty(appid)) {
            throw new DataException(1,"appid 未传");
        }
        if (StringUtil.isEmpty(secret)) {
            throw new DataException(1,"secret 未传");
        }
        WechatTokenParam token = null;
        if ((token = (WechatTokenParam) SerializeUtil.unserialize(redisService.getObjBytes("getAccessToken" + appid))) == null) {
            token = HttpsClientApplication.getAccessToken(appid, secret);
            if (token == null) {
                throw new DataException(1,"获取access Token 错误");
            }
            redisService.set(("getAccessToken" + appid).getBytes(), SerializeUtil.serialize(token), token.getExpires_in());
        }
        return token;
    }

    @Override
    public WechatTokenParam getAccessToken() {
        return null;
    }
}
