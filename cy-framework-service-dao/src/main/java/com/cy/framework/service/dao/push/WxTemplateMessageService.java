package com.cy.framework.service.dao.push;

import com.cy.framework.model.BaseFactoryParam;
import com.cy.framework.model.push.WxSrMessageParam;

import java.util.Map;

public interface WxTemplateMessageService {
    /**
     * 发送消息
     *
     * @param param
     * @param factoryParam
     * @return
     */
    Map<String, Object> send(WxSrMessageParam param, BaseFactoryParam factoryParam);

    /**
     * @param param
     * @param access_token
     * @param factoryParam
     * @return
     */
    Map<String, Object> send(WxSrMessageParam param, String access_token, BaseFactoryParam factoryParam);
}
