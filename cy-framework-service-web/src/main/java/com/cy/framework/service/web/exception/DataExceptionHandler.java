package com.cy.framework.service.web.exception;

import com.cy.framework.service.impl.DataException;
import com.cy.framework.util.result.ResultParam;
import com.cy.framework.util.result.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 公用的异常处理
 */
@ControllerAdvice
public class DataExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({DataException.class, Exception.class})
    public ResponseEntity<ResultParam> handlerSellerException(Exception e) {
        logger.error(e.getMessage(), e);
        if (e instanceof DataException) {
            DataException ee = (DataException) e;
            return getResponse(ResultUtil.error(ee.getCode(), ee.getMessage()));
        } else {
            return handlerSellerException2(e);
        }
    }

    public ResponseEntity<ResultParam> handlerSellerException2(Exception e) {
        return getResponse(ResultUtil.error(4, "系统错误"));
    }

    private ResponseEntity<ResultParam> getResponse(ResultParam resultParam) {
        return new ResponseEntity(resultParam, HttpStatus.OK);
    }
}
