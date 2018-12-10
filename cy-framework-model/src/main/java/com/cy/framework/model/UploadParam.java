package com.cy.framework.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UploadParam {
    /**
     * 图片路径
     */
    private List<MultipartFile> files;
    /**
     * 类型 user/product/banner
     */
    private String type;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}
