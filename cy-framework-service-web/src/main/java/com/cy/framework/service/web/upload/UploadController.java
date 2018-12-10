package com.cy.framework.service.web.upload;

import com.cy.framework.model.UploadParam;
import com.cy.framework.model.qiniu.QiNiuUploadParam;
import com.cy.framework.service.dao.ftp.FtpService;
import com.cy.framework.service.dao.qiniu.QiNiuService;
import com.cy.framework.service.impl.DataException;
import com.cy.framework.util.result.ResultParam;
import com.cy.framework.util.result.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 上传图片管理
 */
@Api(value = "图片上传", tags = {"图片上传"})
@RequestMapping("upload")
@RestController
public class UploadController {
    @Resource
    private QiNiuService qiNiuService;
    @Resource
    private FtpService ftpService;

    @PostMapping("qiniu")
    public ResultParam upload(QiNiuUploadParam param, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = null;
        String data = null;
        if (param.getType() == null || param.getType().isEmpty()) {
            data = "类型未传";
            throw new DataException(1, "类型未传");
        }
        if (param.getFiles() == null || param.getFiles().size() <= 0) {
            throw new DataException(1, "请选择图片");
        }
        return ResultUtil.success(qiNiuService.upload(param, request, response));
    }

    @PostMapping("ftp")
    public ResultParam uploadFtp(UploadParam param, HttpServletRequest request, HttpServletResponse response) {
        return ResultUtil.success(ftpService.upload(param, request, response));
    }
}
