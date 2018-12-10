package com.cy.framework.util.result;

import java.util.Date;

public class ResultUtil {

    public static ResultParam success(Object object) {
        ResultParam result = new ResultParam();
        result.setCode(0);
        result.setMessage("成功");
        result.setData(object);
        result.setTime(new Date().getTime());
        return result;
    }

    public static ResultParam success() {
        return success(null);
    }

    public static ResultParam error(Integer code, String msg) {
        ResultParam result = new ResultParam();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }
}
