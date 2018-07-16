package com.cy.framework.util;

import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {

    /**
     * 描述： 序列化
     *
     * @param object
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月15日 上午10:11:06
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            LoggerFactory.getLogger("test").warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 描述： 反序列化
     *
     * @param bytes
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月15日 上午10:11:17
     */
    public static Object unserialize(byte[] bytes) {
        try {
            // 反序列化
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            if (bais == null) {
                LoggerFactory.getLogger(SerializeUtil.class).warn("byte is null");
                return null;
            }
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
        }
        return null;
    }
}
