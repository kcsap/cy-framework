package com.cy.framework.service.dao.wx;

import com.cy.framework.util.param.WechatTokenParam;

public interface WxAccessTokenService {
    WechatTokenParam getAccessToken(String appid, String secret);
    WechatTokenParam getAccessToken();
}
