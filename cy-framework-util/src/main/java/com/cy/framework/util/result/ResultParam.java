package com.cy.framework.util.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "公用参数", description = "公用参数说明")
public class ResultParam implements Serializable {
    @ApiModelProperty(value = "返回编码", example = "0")
    private Integer code;
    @ApiModelProperty(value = "返回的描述", example = "成功")
    private String message;
    @ApiModelProperty(value = "返回的数据")
    private Object data;
    @ApiModelProperty(value = "服务端实时时间", example = "1540483321460")
    private Long time;
    @ApiModelProperty(value = "返回枚举说明", example = "0")
    private ResultEnum resultEnum;

    public ResultEnum getResultEnum() {
        return resultEnum;
    }

    public void setResultEnum(ResultEnum resultEnum) {
        this.resultEnum = resultEnum;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
