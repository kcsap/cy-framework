package com.cy.framework.util.safe;

import org.apache.log4j.Logger;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 含义：md5算法类
 * @author xuliting2014年7月14日 下午8:31:33
 */
public class MD5Util { 
	 // 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    private static Logger logger = Logger.getLogger("com.huihe.paycore.safe.MD5Util");

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * 方法描述：对字符串进行md5加密
     * @param strObj 待加密内容
     * @return 加密后密文
     * @author zhufei 2015-11-21  下午1:52:15
     */
    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteToString(md.digest(strObj.getBytes(Charset.forName("utf-8"))));
        } catch (NoSuchAlgorithmException ex) {
        	logger.warn("Fail to encode the content with MD5.");
            logger.warn(ex.getMessage());
        }
        return resultString;
    }   
}
