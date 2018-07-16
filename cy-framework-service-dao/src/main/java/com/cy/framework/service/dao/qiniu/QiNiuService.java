package com.cy.framework.service.dao.qiniu;

import com.cy.framework.model.qiniu.QiNiuUploadParam;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 描述： 七牛云存储
 *
 * @author yangchengfu
 * @DataTime 2017年6月24日 上午11:51:31
 */
public interface QiNiuService {
    public Logger logger = Logger.getLogger(QiNiuService.class);

    /**
     * 描述： 七牛上传图片
     *
     * @param param
     * @param request
     * @param response
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月24日 下午2:48:07
     */
    Map<String, Object> upload(QiNiuUploadParam param, HttpServletRequest request, HttpServletResponse response);

    boolean delete(String key);
}
