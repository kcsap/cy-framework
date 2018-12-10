package com.cy.framework.service.impl.push;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cy.framework.model.BaseFactoryParam;
import com.cy.framework.model.push.WxSrMessageParam;
import com.cy.framework.service.dao.push.WxTemplateMessageService;
import com.cy.framework.service.dao.wx.WxAccessTokenService;
import com.cy.framework.service.impl.DataException;
import com.cy.framework.util.StringUtil;
import com.cy.framework.util.http.HttpClientUtil;
import com.cy.framework.util.json.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信模板消息
 */
@Service
public class WxTemplateMessageServiceImpl implements WxTemplateMessageService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private WxAccessTokenService accessTokenService;

    @Override
    public Map<String, Object> send(WxSrMessageParam param, BaseFactoryParam factoryParam) {
        return null;
    }

    private void validate(WxSrMessageParam param) {
        if (StringUtil.isEmpty(param.getTouser())) {
            throw new DataException(1,"推送的用户open_id 不能为空");
        }
        if (param.getKeyWords() == null || param.getKeyWords().size() <= 0) {
            throw new DataException(1,"推送的关键字不能为空");
        }
        if (StringUtil.isEmpty(param.getTemplate_id())) {
            throw new DataException(1,"推送的模板ID不能为空");
        }
        if (StringUtil.isEmpty(param.getPage())) {
            throw new DataException(1,"小程序页面未传");
        }
        Map<String, Object> data = new HashMap<>();
        int x = 1;
        for (WxSrMessageParam.KeyWord keyWord : param.getKeyWords()) {
            if (StringUtil.isEmpty(keyWord.getValue())) {
                throw new DataException(1,"推送的数据不能为空");
            }
            data.put("keyword" + x, keyWord);
            x++;
        }
        param.setData(data);
    }

    @Override
    public Map<String, Object> send(WxSrMessageParam param, String access_token, BaseFactoryParam factoryParam) {
        validate(param);
        if (StringUtil.isEmpty(access_token)) {
            throw new DataException(1,"access token 未传");
        }
        return send(param, access_token);
    }

    private Map<String, Object> send(WxSrMessageParam param, String access_token) {
        String s = HttpClientUtil.execute("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token, JSON.toJSONString(param));
        Map<String, Object> map = new HashMap<>();
        boolean ret = true;
        String remark = "成功";
        if (!s.contains("ok")) {
            logger.warn("send message result is not ok:" + s);
            JSONObject jsonObject = JSON.parseObject(s);
            ret = false;
            remark = jsonObject.getString("errmsg");
        }
        return JSONUtils.getBasicJson(remark, ret);
    }

}
