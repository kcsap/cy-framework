package com.cy.framework.pay.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cy.framework.pay.model.WxPayParam;
import com.cy.framework.pay.service.WxPayFrameworkService;
import com.cy.framework.util.StringUtil;
import com.cy.framework.util.http.HttpsClientRequest;
import com.github.wxpay.sdk.WXPayConfig;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class WxPayFrameworkServiceImpl implements WxPayFrameworkService {
    @Resource
    public WxPayParam wxPayParam;
    public String url = "https://api.weixin.qq.com/sns/jscode2session";
    private List<Integer> list = Arrays.asList(1, 2, 3);
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Map<String, Object> getWxData() {
        return null;
    }

    /**
     * 获取微信的 opendId和 sessionKey
     *
     * @param code
     * @return
     */
    public JSONObject getSessionKeyOropenid(String code) {
        //微信端登录code值
        String urls = url + "?appid=" + wxPayParam.getxAppid() + "&secret=" + wxPayParam.getxAppSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        logger.warn("wx jscode2session req url is :" + urls);
        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        String string = HttpsClientRequest.httpGet(urls, null, "UTF-8");
        logger.warn("wx jscode2session result is " + string);
        return (StringUtil.isNotEmpty(string)) ? JSONObject.parseObject(string) : null;
    }


    /**
     * 解密用户敏感数据获取用户信息
     *
     * @param sessionKey    数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     * @return
     */
    public JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSON.parseObject(result);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidParameterSpecException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            logger.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            logger.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String returnXml(String code, String message) {
        return "<xml> <return_code><![CDATA[" + code + "]]></return_code><return_msg><![CDATA[" + message + "]]></return_msg></xml>";
    }

    /**
     * 获取微信的支付配置
     *
     * @param type 1=公众号 h5 pc 网页支付 2=小程序 3 app
     * @return
     */
    @Override
    public WXPayConfig getWxPayConfig(final Integer type) {
        if (!list.contains(type)) {
            throw new RuntimeException("类型不可用");
        }
        final StringBuilder appId = new StringBuilder();
        StringBuilder appSecret = new StringBuilder();
        if (type == 1) {
            appId.append(wxPayParam.getgAppId());
            appSecret.append(wxPayParam.getgAppSecret());
        } else if (type == 2) {
            appId.append(wxPayParam.getxAppid());
            appSecret.append(wxPayParam.getxAppSecret());
        } else if (type == 3) {
            appId.append(wxPayParam.getAppId());
            appSecret.append(wxPayParam.getAppSecret());
        }

        return new WXPayConfig() {
            @Override
            public String getAppID() {
                return appId.toString();
            }

            @Override
            public String getMchID() {
                return wxPayParam.getMachId();
            }

            @Override
            public String getKey() {
                return wxPayParam.getKey();
            }

            @Override
            public InputStream getCertStream() {
                try {
                    if (StringUtil.isNotEmpty(wxPayParam.getCertPath())) {
                        File file = new File(wxPayParam.getCertPath());
                        InputStream certStream = new FileInputStream(file);
                        byte[] b = new byte[(int) file.length()];
                        certStream.read(b);
                        certStream.close();
                        return new ByteArrayInputStream(b);
                    }
                } catch (Exception e) {

                }
                return null;
            }

            @Override
            public int getHttpConnectTimeoutMs() {
                return 0;
            }

            @Override
            public int getHttpReadTimeoutMs() {
                return 0;
            }
        };
    }
}
