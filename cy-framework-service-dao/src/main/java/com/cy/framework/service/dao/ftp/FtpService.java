package com.cy.framework.service.dao.ftp;

import com.cy.framework.model.UploadParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface FtpService {

    Map<String, Object> upload(UploadParam param, HttpServletRequest request, HttpServletResponse response);
}
