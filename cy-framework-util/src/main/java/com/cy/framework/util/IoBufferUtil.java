package com.cy.framework.util;

import org.apache.mina.core.buffer.IoBuffer;

public class IoBufferUtil {

    public static String byteToString(byte[] b) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            stringBuffer.append((char) b[i]);
        }
        return stringBuffer.toString();
    }

    /**
     * @param str
     * @return
     */
    public static IoBuffer stringToIoBuffer(String str) {

        byte bt[] = str.getBytes();

        IoBuffer ioBuffer = IoBuffer.allocate(bt.length);
        ioBuffer.put(bt, 0, bt.length);
        ioBuffer.flip();
        return ioBuffer;
    }

    public static IoBuffer byteToIoBuffer(byte[] bt, int length) {
        IoBuffer ioBuffer = IoBuffer.allocate(length);
        ioBuffer.put(bt, 0, length);
        ioBuffer.flip();
        return ioBuffer;
    }
    public static IoBuffer byteToIoBuffer(byte[] bt) {
        IoBuffer ioBuffer = IoBuffer.allocate(bt.length);
        ioBuffer.put(bt, 0, bt.length);
        ioBuffer.flip();
        return ioBuffer;
    }

    /**
     * 将IoBuffer转换成byte
     *
     * @param message
     */
    public static byte[] ioBufferToByte(Object message) {
        if (!(message instanceof IoBuffer)) {
            return null;
        }
        IoBuffer ioBuffer = (IoBuffer) message;
        byte[] b = new byte[ioBuffer.limit()];
        ioBuffer.get(b);
        return b;
    }

    /**
     * 将IoBuffer转换成string
     *
     * @param message
     */
    public static String ioBufferToString(Object message) {
        if (!(message instanceof IoBuffer)) {
            return "";
        }
        IoBuffer ioBuffer = (IoBuffer) message;
        byte[] b = new byte[ioBuffer.limit()];
        ioBuffer.get(b);
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            stringBuffer.append((char) b[i]);
        }
        return stringBuffer.toString();
    }
}
