package com.cy.framework.service.dao;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public interface UserTokenService {
    Logger loggers = Logger.getLogger(UserTokenService.class);

    /**
     * 描述： 生成token
     *
     * @param key
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月15日 上午10:50:58
     */
    String generate(String key);

    /**
     * 生成用户的Id
     * @param key
     * @return
     */
    String generateUserId(String key);
    String generateUserId();

    /**
     * 描述： 验证token
     *
     * @param token
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月15日 上午10:51:37
     */
    boolean validate(String token);

    /**
     * 描述： 根据用户的验证token存取用户信息
     *
     * @param userInfoEntity
     * @author yangchengfu
     * @DataTime 2017年6月27日 下午5:12:39
     */
    String putTokenUserInfo(String key, Serializable userInfoEntity);

    /**
     * 描述： 获取对象
     *
     * @param token
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月15日 下午12:44:45
     */
    Object getUserInfo(String token) throws Exception;

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    Object getUserInfo(HttpServletRequest request) throws Exception;
}
