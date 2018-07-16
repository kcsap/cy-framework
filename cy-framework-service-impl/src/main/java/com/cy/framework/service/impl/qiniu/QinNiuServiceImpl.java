package com.cy.framework.service.impl.qiniu;

import com.alibaba.fastjson.JSON;
import com.cy.framework.model.qiniu.QiNiuUploadParam;
import com.cy.framework.model.qiniu.QinNiuParam;
import com.cy.framework.service.dao.RedisService;
import com.cy.framework.service.dao.qiniu.QiNiuService;
import com.cy.framework.util.ImageSizeUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class QinNiuServiceImpl implements QiNiuService {
    @Resource
    private QinNiuParam qinNiuParam;
    @Resource
    private RedisService redisService;

    @Override
    public Map<String, Object> upload(QiNiuUploadParam param, HttpServletRequest request,
                                      HttpServletResponse response) {
        // TODO Auto-generated method stub
        List<String> list = new LinkedList<>();
        Map<String, Object> map = new HashMap<>();
        ImageSizeUtil imageSizeUtil = new ImageSizeUtil();
        if (param.getFiles() != null && param.getFiles().size() > 0) {
            // 构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Zone.zone0());
            // ...其他参数参考类注释
            UploadManager uploadManager = new UploadManager(cfg);
            // ...生成上传凭证，然后准备上传
            // 默认不指定key的情况下，以文件内容的hash值作为文件名
            String upToken = getToken(param);
            for (MultipartFile bb : param.getFiles()) {
                try {
                    Response responses = uploadManager.put(bb.getBytes(), param.getKey(), upToken);
                    // 解析上传成功的结果
                    DefaultPutRet putRet = JSON.parseObject(responses.bodyString(), DefaultPutRet.class);
                    System.out.println(putRet.key);
                    System.out.println(putRet.hash);
                    list.add(qinNiuParam.getUrl() + imageSizeUtil.getImageSize(putRet.key, param.getH(), param.getW()));
                } catch (QiniuException ex) {
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                        // ignore
                    }
                } catch (Exception e) {
                    // TODO: handle exception

                }
            }
        }
        map.put("result", list.size() > 0 ? true : false);
        map.put("remark", list.size() > 0 ? "上传成功" : "上传失败");
        map.put("path", list);
        return map;
    }

    private String getToken(QiNiuUploadParam param) {
        String upToken = redisService.getStr("qiniuToken" + param.getType());
        if (upToken == null || upToken.isEmpty()) {
            Auth auth = Auth.create(qinNiuParam.getAccessKey(), qinNiuParam.getSecretKey());
            upToken = auth.uploadToken(qinNiuParam.getBucket(), param.getKey(), qinNiuParam.getExpires(), null);
            redisService.set("qiniuToken" + param.getType(), upToken, qinNiuParam.getExpires());
        }
        return upToken;
    }

    @Override
    public boolean delete(String key) {
        // TODO Auto-generated method stub
        Configuration cfg = new Configuration(Zone.zone0());
        Auth auth = Auth.create(qinNiuParam.getAccessKey(), qinNiuParam.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            Response response = bucketManager.delete(qinNiuParam.getBucket(), key);
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
