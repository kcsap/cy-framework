package com.cy.framework.model.push;

import java.util.List;
import java.util.Map;

/**
 * 微信小程序 推送消息
 */
public class WxSrMessageParam {
    /**
     * 接受者的 opendId
     */
    private String touser;
    /**
     * 模板ID
     */
    private String template_id;
    /**
     * 小程序的页面路径
     */
    private String page;
    /**
     * 表单提交 的  form id
     * 支付场景下，为本次支付的 prepay_id
     */
    private String form_id;
    /**
     * 推送的数据
     */
    private List<KeyWord> keyWords;
    /**
     * 模板数据
     */
    private Map<String, Object> data;
    /**
     * 模板内容的颜色 默认为黑色 red
     */
    private String color;
    /**
     * keyword1.DATA" 模板需要放大的关键词，不填则默认无放大
     */
    private String emphasis_keyword;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public List<KeyWord> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<KeyWord> keyWords) {
        this.keyWords = keyWords;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEmphasis_keyword() {
        return emphasis_keyword;
    }

    public void setEmphasis_keyword(String emphasis_keyword) {
        this.emphasis_keyword = emphasis_keyword;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public static class KeyWord {
        /**
         * 内容
         */
        private String value;
        /**
         * 颜色
         */
        private String color;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
