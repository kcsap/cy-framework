package com.cy.framework.model.qiniu;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public class QiNiuUploadParam implements Serializable {
    /**
     * 描述：
     *
     * @author yangchengfu
     * @DataTime 2017年6月24日 下午1:18:34
     */
    private static final long serialVersionUID = -7357898706921750672L;
    /**
     * 图片路径
     */
    private List<MultipartFile> files;
    private String base64;
    private int h = 200;
    private int w = 200;
    /**
     * 文件名称
     */
    private String key;
    /**
     * 类型
     */
    private String type;
    /**
     * 属于的id
     */
    private int parent_id;
    private String token;
    private String userId;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
