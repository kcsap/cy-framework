package com.cy.framework.util;


import com.cy.framework.util.safe.MD5Util;

public class TokenUtil {
    /**
     * 描述：
     *
     * @param key
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月15日 上午10:45:05
     */
    public static String generateToken(String key) {
        return MD5Util.GetMD5Code(key + FinalConfigParam.KEY);
    }

}
