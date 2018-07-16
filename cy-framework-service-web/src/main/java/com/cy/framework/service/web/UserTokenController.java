package com.cy.framework.service.web;

import com.cy.framework.model.BaseFactoryParam;
import com.cy.framework.service.dao.UserTokenService;
import com.cy.framework.service.impl.DataException;
import com.cy.framework.util.StringUtil;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserTokenController extends BaseActiveMQFactoryController {
    @Resource
    private UserTokenService userTokenService;

    @Override
    public void init() {

    }

    public Map<String, Object> validate(String token, BaseFactoryParam request) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (StringUtil.isEmpty(token)) {
                throw new DataException("请传入token");
            }
            boolean vali = userTokenService.validate(token);
            map.put("result", true);
            map.put("validate", vali);
        } catch (DataException e) {
            map.put("result", false);
            map.put("validate", false);
            map.put("remark", e.getMessage());
        }
        return map;
    }
}
