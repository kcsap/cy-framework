package com.cy.framework.util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 判断字符串是空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if ("".equals(str) || str == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串不是空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        if (!"".equals(str) && str != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断某一个字符串数组中是否含有某一字符串
     *
     * @param str
     * @param strArr
     * @return
     */
    public static boolean existStrArr(String str, String[] strArr) {
        return Arrays.asList(strArr).contains(str);
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     *
     * @param str
     * @param strArr
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
