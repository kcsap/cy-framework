package com.cy.framework.util.result;

public enum ResultEnum {
    SYSTEM_ERROR(9999, "系統异常,请稍后再试..."), SUCCESS(0, "成功"),
    NOT_LOGIN(9998,"用户未登录"), TOKEN_IS_NOT_AVAILABLE(9997, "用户token失效"),
    DATA_ERROR(9996, "数据异常,请核对"), NOT_ROEL(9990, "没有权限"),
    result_1(1,"添加失败"),result_2(2,"删除失败"),result_3(3,"修改失败");
    /**
     * 结果码
     */
    private Integer code;

    /**
     * 结果描述
     */
    private String desc;

    /**
     * 获取结果码
     *
     * @param code 待查询code
     * @return 对应的结果码
     */
    public static ResultEnum getByCode(String code) {
        for (ResultEnum resultCode : values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode;
            }
        }
        return null;
    }

    /**
     * @param code
     * @param desc
     */
    ResultEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

}
