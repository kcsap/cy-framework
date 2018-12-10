package com.cy.framework.service.impl;

import com.cy.framework.util.result.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @param
 * @author yangchengfu
 * @Description: 公用的异常处理
 * @return
 * @Date : 2017/10/23 10:13
 */
public class DataException extends RuntimeException {
    public Logger logger = LoggerFactory.getLogger(getClass());
    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataException(ResultEnum resultEnum) {
        super(resultEnum.getDesc());
        this.code = resultEnum.getCode();
    }

    public DataException(Integer code,String message) {
        this.code = code;
        this.message = message;
    }
}
