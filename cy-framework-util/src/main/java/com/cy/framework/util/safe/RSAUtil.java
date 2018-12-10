package com.cy.framework.util.safe;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA签名公共类
 *
 * @author Feli
 */
public class RSAUtil {

    private static Logger log = Logger.getLogger(RSAUtil.class);
    private static RSAUtil instance;

    private RSAUtil() {
    }

    public static RSAUtil getInstance() {
        if (null == instance)
            return new RSAUtil();
        return instance;
    }

    /**
     * 读取密钥文件内容
     *
     * @param ins :文件路径
     * @return
     */
    public static String getKeyContent(InputStream ins) {
        BufferedReader br = null;
        // InputStream ins = null;
        StringBuffer sReturnBuf = new StringBuffer();
        try {
            br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
            String readStr = null;
            readStr = br.readLine();
            while (readStr != null) {
                sReturnBuf.append(readStr);
                readStr = br.readLine();
            }
        } catch (IOException e) {
            log.warn("Fail to read private key:" + e.getMessage());
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    log.warn("Fail to read private key:" + e.getMessage());
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                    ins = null;
                } catch (IOException e) {
                    log.warn("Fail to read private key:" + e.getMessage());
                }
            }
        }
        return sReturnBuf.toString();
    }

    /**
     * 签名处理
     *
     * @param prikeyvalue ：私钥文件
     * @param sign_str    ：签名源内容
     * @return
     */
    public static String sign(String prikeyvalue, String sign_str, String signature) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getBytesBASE64(prikeyvalue));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
            // 用私钥对信息生成数字签名
            Signature signet = Signature.getInstance(signature);
            signet.initSign(myprikey);
            signet.update(sign_str.getBytes("UTF-8"));
            byte[] signed = signet.sign(); // 对信息的数字签名
            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(signed));
        } catch (Exception e) {
            log.error("Failed to sign:" + e.getMessage());
        }
        return null;
    }

    /**
     * 签名
     *
     * @param privekeyval
     * @param sign_str
     * @return
     */
    public static String sign(String privekeyval, String sign_str) {
        return sign(privekeyval, sign_str, "MD5withRSA");
    }

    /**
     * 签名验证
     *
     * @param pubkeyvalue ：公钥
     * @param oid_str     ：源串
     * @param signed_str  ：签名结果串
     * @return
     */
    public static boolean checksign(String pubkeyvalue, String oid_str, String signed_str, String signature) {
        try {
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64.getBytesBASE64(pubkeyvalue));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
            byte[] signed = Base64.getBytesBASE64(signed_str);// 这是SignatureData输出的数字签名
            Signature signetcheck = Signature.getInstance(signature);
            signetcheck.initVerify(pubKey);
            signetcheck.update(oid_str.getBytes("UTF-8"));
            return signetcheck.verify(signed);
        } catch (Exception e) {
            log.error("Fail to check sign:" + e.getMessage());
        }
        return false;
    }

    public static boolean checksign(String pubkeyvalue, String oid_str, String signed_str) {
        return checksign(pubkeyvalue, oid_str, signed_str, "MD5withRSA");
    }
}
