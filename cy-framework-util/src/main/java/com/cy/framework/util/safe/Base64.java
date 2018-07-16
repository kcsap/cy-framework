package com.cy.framework.util.safe;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;

public class Base64 {
	private static Logger log = Logger.getLogger(Base64.class);

	public static String getBASE64(String s) {
		if (s == null) {
			log.warn("Content is null.");
			return null;
		}
		try {
			return getBASE64(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.warn("Fail to get content's base64 code:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	public static String getBASE64(byte[] b) {
		byte[] rb = org.apache.commons.codec.binary.Base64.encodeBase64(b);
		if (rb == null) {
			return null;
		}
		try {
			return new String(rb, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warn("Fail to get content's base64 code:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String getFromBASE64(String s) {
		if (s == null) {
			return null;
		}
		try {
			byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(s.getBytes("UTF-8"));
			if (b == null) {
				return null;
			}
			return new String(b, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warn("Fail to get content from base64 code:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] getBytesBASE64(String s) {
		if (s == null) {
			return null;
		}
		try {
			byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(s.getBytes("UTF-8"));
			return b;
		} catch (Exception e) {
			log.warn("Fail to get content from base64 code:" + e.getMessage());
			return null;
		}
	}
}
