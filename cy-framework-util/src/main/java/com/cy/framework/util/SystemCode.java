package com.cy.framework.util;

public enum SystemCode {
    SYSTEM_ERROR("9999", "系統异常,请稍后再试..."), SUCCESS("0000", "成功"), NOT_LOGIN("9998",
            "用户未登录"), TOKEN_IS_NOT_AVAILABLE("9997", "用户token失效"), DATA_ERROR("9996", "数据异常,请核对"), NOT_ROEL("9990", "没有权限");
    /**
     * 结果码
     */
    private String code;

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
    public static SystemCode getByCode(String code) {
        for (SystemCode resultCode : values()) {
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
    private SystemCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
}
