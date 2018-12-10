/**
 * @author zhufei 2014-7-18 上午11:19:31
 */
package com.cy.framework.util;

import com.cy.framework.util.json.JSONUtils;
import com.cy.framework.util.safe.MD5Util;
import com.thoughtworks.xstream.XStream;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 含义：系统的一些公共处理方法
 *
 * @author zhufei 2014-7-18 上午11:19:31
 */
public class CommonUtils {

    private static Logger logger = Logger.getLogger(CommonUtils.class);

    public static String CreateNoncestr() {
        return CreateNoncestr(10);
    }

    public static String CreateNoncestr(int x) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < x; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    /**
     * 判断一个字符串 是否包含 字母和数字
     *
     * @param str
     * @return
     */
    public static boolean isDL(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) { //循环遍历字符串
            if (Character.isDigit(str.charAt(i))) { //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(str.charAt(i))) { //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        return (isDigit && isLetter);
    }

    /**
     * 方法描述: map转javaBean
     *
     * @param map
     * @param t
     * @return
     * @author yangchengfu
     * @datatime 2017-1-20 上午10:32:49
     */
    public static <T> T mapToBean(Map<String, Object> map, T t, List<String> filter) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
            PropertyDescriptor[] descriptor = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : descriptor) {
                String key = propertyDescriptor.getName();
                if (map.containsKey(key)) {
                    if (filter != null && filter.size() > 0) {
                        if (filter.contains(key)) {
                            continue;
                        }
                    }
                    Object object = map.get(key);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(t, object);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 获取随机的中文
     *
     * @param count
     * @return
     */
    public static StringBuilder getRandomChar(int count) {
        StringBuilder str = new StringBuilder();
        for (int x = 0; x < count; x++) {
            int hightPos; //
            int lowPos;
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39)));
            lowPos = (161 + Math.abs(random.nextInt(93)));
            byte[] b = new byte[2];
            b[0] = (Integer.valueOf(hightPos)).byteValue();
            b[1] = (Integer.valueOf(lowPos)).byteValue();
            try {
                str.append(new String(b, "GB2312"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                System.out.println("错误");
            }
        }
        return str;
    }

    public static StringBuilder getRandomChar(int count, boolean abc, int abcCount) {
        StringBuilder string = getRandomChar(count);
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        if (abc) {
            for (int x = 0; x < abcCount; x++) {
                string.append(chars.charAt((int) (Math.random() * 26)));
            }
        }
        return string;
    }

    /**
     * 方法描述: 获取客户端的ip地址
     *
     * @param request
     * @return
     * @author yangchengfu
     * @datatime 2017-2-15 上午11:30:04
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String generateId(int type) {
        StringBuilder builder = new StringBuilder();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(new Date());
        builder.append(type).append(calendar.get(Calendar.YEAR)).append(calendar.get(Calendar.MONTH)).append(calendar.get(Calendar.DATE));
        builder.append(calendar.getTime().getTime());
        return builder.toString();
    }


    /**
     * 描述： 生成订单号
     *
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月20日 上午11:06:31
     */
    public static String generateOrderCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FinalConfigParam.DATETIME_FORMAT_STYLE_SS_SSS);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return FinalConfigParam.ORDER_CODE + dateFormat.format(new Date()) + getRandNum();
    }

    /**
     * 自定义类型生成编号
     *
     * @param codeType
     * @return
     */
    public static String generaterCode(String codeType) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FinalConfigParam.DATETIME_FORMAT_STYLE_SS_SSS);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return codeType + dateFormat.format(new Date()) + getRandNum();
    }

    /**
     * 生成游戏开始的唯一编号
     *
     * @param tableCode
     * @return
     */
    public static String generateTableStartCode(String tableCode) {
        StringBuilder stringBuilder = new StringBuilder();
        String str = String.valueOf(new Date().getTime());
        stringBuilder.append(tableCode).append("-").append(str.substring(str.length() - 4, str.length())).append(getRandNum(1000, FinalConfigParam.MAX));
        return stringBuilder.toString();
    }

    /**
     * 生成房间号码
     *
     * @return
     */
    public static String generateRoomCode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(new Date().getTime()).append(getRandNum(FinalConfigParam.MIN, FinalConfigParam.MAX));
        return stringBuilder.toString();
    }

    /**
     * 生成桌位号
     *
     * @param room_code
     * @return
     */
    public static String generateTableCode(String room_code) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(room_code).append("-").append(new Date().getTime()).append(getRandNum(FinalConfigParam.MIN, FinalConfigParam.MAX));
        return stringBuilder.toString();
    }

    /**
     * 生成座位号
     *
     * @param table_code 桌位号
     * @return
     */
    public static String generateRoomSeatCode(String table_code, int count) {
        StringBuilder stringBuilder = new StringBuilder();
        String str = String.valueOf(new Date().getTime());
        stringBuilder.append(table_code).append("-").append(str.substring(str.length() - 4, str.length())).append(getRandNum(count, FinalConfigParam.MAX));
        return stringBuilder.toString();
    }

    /**
     * @param
     * @return
     * @Description: 生成流水号
     * @author yangchengfu
     * @Date : 2017/9/16 11:21
     */
    public static String generateFlowingCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FinalConfigParam.DATETIME_FORMAT_STYLE_SS_SSS);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return FinalConfigParam.FLOWING_CODE + dateFormat.format(new Date()) + CommonUtils.getRandNum(111111111, 999999999);
    }

    /**
     * @param
     * @return
     * @Description: 生成组合流水号
     * @author yangchengfu
     * @Date : 2017/9/16 11:21
     */
    public static String generateFlowingKey() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FinalConfigParam.DATETIME_FORMAT_STYLE_SS_SSS);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return FinalConfigParam.FLOWING_KEY + dateFormat.format(new Date()) + CommonUtils.getRandNum();
    }

    /**
     * 生成用户id
     *
     * @return
     */
    public static String generateUserId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FinalConfigParam.DATETIME_FORMAT_STYLE_SS_SSS);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return FinalConfigParam.USER_ID + dateFormat.format(new Date()) + CommonUtils.getRandNum();
    }

    /**
     * @param
     * @return
     * @Description: 获取支付的订单号
     * @author yangchengfu
     * @Date : 2017/9/19 11:32
     */
    public static String generatePayCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FinalConfigParam.DATETIME_FORMAT_STYLE_SS_SSS);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return FinalConfigParam.PAY_CODE + dateFormat.format(new Date()) + CommonUtils.getRandNum();
    }

    /**
     * 描述： 组合创健订单的唯一key
     *
     * @return
     * @author yangchengfu
     * @DataTime 2017年7月3日 下午5:17:59
     */
    public static String generateOrderKey() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat(FinalConfigParam.DATETIME_FORMAT_STYLE_SS_SSS);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return FinalConfigParam.ORDER_KEY + dateFormat.format(new Date()) + CommonUtils.getRandNum();
    }

    /**
     * 方法描述: javaBean 转map
     *
     * @param t
     * @return
     * @author yangchengfu
     * @datatime 2017-1-20 上午10:32:32
     */
    public static <T> HashMap<String, Object> beanToMap(Object t, String... keys) {
        HashMap<String, Object> map = null;
        List<String> filter = (keys != null && keys.length > 0) ? Arrays.asList(keys) : null;
        try {
            if (t == null) {
                throw new RuntimeException("obj is null");
            }
            map = new HashMap<>();
            BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
            PropertyDescriptor[] descriptor = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : descriptor) {
                String key = propertyDescriptor.getName();
                if (!key.equals("class")) {
                    if (filter != null && filter.size() > 0) {
                        if (filter.contains(key)) {
                            continue;
                        }
                    }
                    Method method = propertyDescriptor.getReadMethod();
                    Object object = method.invoke(t);
                    if (object == null) {
                        continue;
                    }
                    map.put(key, object);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.warn(e.getMessage(), e);
        }
        return map;
    }

    /**
     * 方法描述：date类型转换为字符串
     *
     * @param date 日期对象
     * @return 字符串型日期对象
     * @author zhufei 2014-8-26 下午2:30:13
     */
    public static String date2string(Date date) {
        if (date == null) {
            logger.warn("Can't parse date to string when the date is null.");
            return null;
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        String formatStr = null;
        if ((ca.get(Calendar.HOUR_OF_DAY) + ca.get(Calendar.MINUTE) + ca.get(Calendar.SECOND)) == 0) {
            formatStr = FinalConfigParam.DATE_FORMAT_STYLE;
        } else {
            formatStr = FinalConfigParam.DATETIME_FORMAT_STYLE;
        }
        return date2string(date, formatStr);
    }

    /**
     * 方法描述: 获取匹配对象
     *
     * @param key 需要匹配的值
     * @param val 匹配的类型 ，取值：all 完全匹配，left：左边匹配，right：右边匹配，default：左右模糊匹配
     * @return
     * @author yangchengfu
     * @datatime 2017-1-16 上午11:24:20
     */
    public static Pattern getPattern(String key, String val) {
        Pattern pattern = null;
        switch (key) {
            case "all":
                pattern = Pattern.compile("^" + val + "$", Pattern.CASE_INSENSITIVE);
                break;
            case "left":
                pattern = Pattern.compile("^" + val + ".*$", Pattern.CASE_INSENSITIVE);
                break;
            case "right":
                pattern = Pattern.compile("^.*" + val + "$", Pattern.CASE_INSENSITIVE);
                break;
            default:
                pattern = Pattern.compile("^.*" + val + ".*$", Pattern.CASE_INSENSITIVE);
                break;
        }
        return pattern;
    }

    /**
     * void descr: 解码
     *
     * @param decode
     * @return String
     * @datatime 2016-9-26 下午5:41:11
     * @author yangchengfu
     */
    public static String decode(String decode) {
        String str = decode;
        try {
            str = URLDecoder.decode(decode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            Logger.getLogger("decode Exception message is :" + e.getMessage());
            str = decode;
        }
        return str;
    }

    /**
     * void descr: 编码
     *
     * @param encoder
     * @return String
     * @datatime 2016-9-26 下午5:41:00
     * @author yangchengfu
     */
    public static String encoder(String encoder) {
        String str = encoder;
        try {
            str = URLEncoder.encode(encoder, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            Logger.getLogger("decode Exception message is :" + e.getMessage());
            str = encoder;
        }
        return str;
    }

    /**
     * 方法描述：根据格式将时间对象转换为字符串
     *
     * @param date
     * @param formatStr
     * @return
     * @author zhufei 2015-11-20 上午9:47:46
     */
    public static String date2string(Date date, String formatStr) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        // format.applyPattern(formatStr);
        format.setTimeZone(FinalConfigParam.TIME_ZONE);
        String sDate = format.format(date);
        return sDate;
    }

    /**
     * 方法描述：时间字符串转换为日期时间对象
     *
     * @param time
     * @return
     * @author zhufei 2015-4-15 上午9:07:58
     */
    public static Date getDateFromTimeStr(String time) {
        if (time == null) {
            return null;
        }
        Date date = null;
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat(FinalConfigParam.TIME_FORMAT_SYSTLE);
        try {
            format.setTimeZone(FinalConfigParam.TIME_ZONE);
            date = format.parse(time);
            Calendar ca = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            ca.setTime(today);
            now.setTime(date);
            now.set(Calendar.HOUR_OF_DAY, ca.get(Calendar.HOUR_OF_DAY));
            now.set(Calendar.MINUTE, ca.get(Calendar.MINUTE));
        } catch (Exception e) {
            date = null;
            logger.warn("Error to parse string to time,time:" + time);
            logger.warn(e.getMessage());
        }
        return date;
    }

    /**
     * 方法描述：指定时间的一个字段添加值
     *
     * @param now    指定的时间
     * @param field  指定字段
     * @param amount 增加量
     * @return
     * @author zhufei 2016-1-7 下午1:52:15
     */
    public static Date dateAdd(Date now, int field, int amount) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(now);
        ca.add(field, amount);
        return ca.getTime();
    }

    /**
     * 方法描述：指定时间设置指定字段值
     *
     * @param now   指定时间
     * @param field 指定字段
     * @param value 设定值
     * @return
     * @author zhufei 2016-1-7 下午1:53:17
     */
    public static Date setDate(Date now, int field, int value) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(now);
        ca.set(field, value);
        return ca.getTime();
    }

    /**
     * 方法描述：将日期转换为世界字符串
     *
     * @param time
     * @return
     * @author zhufei 2015-4-16 下午5:35:25
     */
    public static String getTimeFromDate(Date time) {
        if (time == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(FinalConfigParam.TIME_FORMAT_SYSTLE);
        format.setTimeZone(FinalConfigParam.TIME_ZONE);
        String timeStr = format.format(time);
        return timeStr;
    }

    /**
     * 方法描述：字符串转换成日期格式
     *
     * @param date 日期
     * @return 日期格式
     * @author xuliting2014-10-16 下午4:33:58
     */
    public static Date getDateFromString(String date) {
        if (date == null || date.isEmpty() || date.equals("null")) {
            return null;
        }
        String format = null;
        if (date.contains(":")) {
            if (date.contains("-")) {
                int length = date.split("-").length;
                if (length == 2) {
                    format = FinalConfigParam.MONTH_DAY_FORMAT_STYLE;// 月日时分秒,如04-04
                } else {
                    format = FinalConfigParam.DATETIME_FORMAT_STYLE;// 年月日时分秒,如2017-04-27
                }
            } else {
                if (date.contains("/")) {
                    date.replaceAll("/", "-");
                    format = FinalConfigParam.TIME_FORMAT_STYLE;
                } else {
                    format = FinalConfigParam.TIME_FORMAT_STYLE;
                }

            }
            // format = FinalConfigParam.DATETIME_FORMAT_STYLE;
        } else {
            if (date.contains("-")) {
                int length = date.split("-").length;
                if (length == 3) {
                    format = FinalConfigParam.DATE_FORMAT_STYLE;
                } else if (length == 2) {
                    format = "yyyy-MM";
                }
            }
        }
        return getDateFromString(date, format);
    }

    /**
     * 方法描述：根据格式转换字符串为时间对象
     *
     * @param date
     * @param format
     * @return
     * @author zhufei 2015-11-20 上午9:48:22
     */
    public static Date getDateFromString(String date, String format) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date newDate = null;
        try {
            sdf.setTimeZone(FinalConfigParam.TIME_ZONE);
            newDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.warn("Error to parase string with style(" + format + ") to date, error msg:" + e.getMessage());
        }
        return newDate;
    }

    /**
     * 方法描述：手机号码有效性验证
     *
     * @param mobile
     * @return
     * @author zhufei 2014-12-13 下午6:08:11
     */
    public static boolean validatePhone(String mobile) {
        boolean ret = false;
        if (mobile == null) {
            return ret;
        }
        Pattern pt = Pattern.compile("1[3|5|7|8|][0-9]{9}");
        Matcher mt = pt.matcher(mobile);
        ret = mt.matches();
        return ret;
    }

    /**
     * 方法描述：生成特定的字符串
     *
     * @param size          字符串大小
     * @param caseSensitive 是否大小写敏感
     * @param upcase        要求大写
     * @param lowcase       要求小写
     * @param numOnly       只要数字
     * @return
     * @author zhufei 2015-12-2 下午5:33:18
     */
    public static String generateRandom(int size, boolean caseSensitive, boolean upcase, boolean lowcase,
                                        boolean numOnly) {
        String result = null;
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        String base = null;
        if (caseSensitive) {
            base = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        } else if (numOnly) {
            base = "1234567890";
        } else {
            base = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        for (int i = 0; i < size; i++) {
            int degree = random.nextInt() % (base.length());
            sb.append(base.charAt(Math.abs(degree)));
        }
        result = sb.toString();
        if (upcase) {
            result = result.toUpperCase();
        } else if (lowcase) {
            result = result.toLowerCase();
        }
        return result;
    }

    /**
     * 方法描述：生成任意的随机串
     *
     * @param size
     * @return
     * @author zhufei 2016-6-8 下午5:56:49
     */
    public static String getRandowCode(int size) {
        if (size < 6) {
            String result = generateRandom(size, false, false, false, false);
            return result;
        }
        String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
                "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z"};
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        int idx = uuid.length() / size;
        for (int i = 0; i < size; i++) {
            String str = uuid.substring(i * idx, (i + 1) * idx);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    /**
     * 方法描述：生成签名内容
     *
     * @param map     待生成签名内容
     * @param excepts 排除字段
     * @return
     * @author zhufei 2016-1-12 下午3:55:37
     */
    public static String signContentGen(Map<String, Object> map, String[] excepts) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        if (map != null) {
            sb = new StringBuffer();
            Set<String> keys = map.keySet();
            if (keys != null) {
                List<String> keyList = new ArrayList<String>(keys);
                Collections.sort(keyList, String.CASE_INSENSITIVE_ORDER);
                List<String> exceptKey = null;
                if (excepts != null && excepts.length > 0) {
                    exceptKey = Arrays.asList(excepts);
                }
                for (String key : keyList) {
                    if (exceptKey != null && exceptKey.contains(key)) {
                        continue;
                    }
                    if (map.get(key) != null && !map.get(key).toString().isEmpty()) {
                        sb.append("&").append(key).append("=").append(map.get(key));
                    }
                }
                if (sb.length() > 0 && sb.toString().startsWith("&")) {
                    sb = sb.deleteCharAt(0);
                    result = sb.toString();
                }
            }
        }
        return result;
    }

    /**
     * 方法描述：签名生成接口
     *
     * @param obj
     * @param sign
     * @param key
     * @param excepts
     * @return
     * @author ycf 2016-5-25 上午9:14:02
     */
    @SuppressWarnings("unchecked")
    public static boolean signContentGenValidate(Object obj, String sign, String key, String[] excepts) {
        String[] exString = {"sign"};
        Map<String, Object> map = new HashMap<String, Object>();
        if (excepts == null || excepts.length == 0) {
            excepts = exString;
        }
        if (obj instanceof HashMap) {
            map.putAll((Map<String, Object>) obj);
        } else {
            JSONObject json = JSONUtils.obj2Json(obj, excepts);
            map.putAll(JSONUtils.obj2Map(json.toString(), null));
        }
        String signs = CommonUtils.signContentGen(map, excepts);
        signs += "&key=" + key;
        String signCode = MD5Util.GetMD5Code(signs);
        boolean result = false;
        if (obj != null) {
            if (signCode.equals(sign)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 方法描述：判断字符串是不是有效的数字类型
     *
     * @param data
     * @return
     * @author zhufei 2016-1-12 下午4:51:24
     */
    public static boolean isAllDataValid(List<String> data) {
        if (data == null || data.size() == 0) {
            return true;
        }
        Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
        Matcher matcher = null;
        boolean valid = false;
        for (String dt : data) {
            if (dt == null || dt.isEmpty()) {
                continue;
            }
            matcher = pattern.matcher(dt);
            valid = matcher.matches();
            if (valid) {
                continue;
            } else {
                break;
            }
        }
        return valid;
    }

    /**
     * 方法描述：解析xml成指定对象
     *
     * @param xml    待处理的xml字符串
     * @param tClass 对象类型
     * @return
     * @author zhufei 2016-2-17 上午10:58:49
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObjectFromXML(String xml, Class<T> tClass) {
        if (xml == null || xml.isEmpty()) {
            logger.warn("Xml code is not valid.");
            return null;
        }
        T result = null;
        try {
            XStream xStreamForResponseData = new XStream();
            xStreamForResponseData.alias("xml", tClass);
            xStreamForResponseData.ignoreUnknownElements();// 暂时忽略掉一些新增的字段
            result = (T) xStreamForResponseData.fromXML(xml);
        } catch (Exception e) {
            logger.warn("Failed to parse xml data.");
            logger.warn(e.getMessage());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T formatData(T dt) {
        T ret = null;
        DecimalFormat df = new DecimalFormat("#.000");
        if (dt.getClass().toString().equals("double")) {
            ret = (T) new Double(df.format(dt));
        } else if (dt.getClass().toString().equals("float")) {
            ret = (T) new Float(df.format(dt));
        }
        return ret;
    }

    /**
     * @param @param  array
     * @param @return 设定文件
     * @return String 返回类型
     * @Description: 数组转化字符串
     * @author yangjing
     * @date 2016-10-13 下午9:18:29
     */
    public static String arrayTOString(String[] array) {
        String str = "";
        if (array == null || array.length == 0) {
            return str;
        }
        StringBuffer sb = new StringBuffer();
        // sb.append("{");
        for (int i = 0; i < array.length; i++) {
            if (i == array.length - 1) {
                sb.append(array[i]);
                break;
            }
            sb.append(array[i]);
            sb.append(",");
        }
        // sb.append("}");
        str = sb.toString();
        return str;
    }

    /**
     * @param @param  array
     * @param @return 设定文件
     * @return String 返回类型
     * @Description: 字符串集合转化字符串
     * @author yangjing
     * @date 2016-10-13 下午9:18:29
     */
    public static String listToString(List<String> list) {
        String str = "";
        if (list == null || list.size() == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i == list.size() - 1) {
                break;
            } else {
                sb.append(",");
            }
        }
        str = sb.toString();
        return str;
    }

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0-3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证是否手机号码
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            logger.warn("手机号应为11位数");
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (!isMatch) {
                logger.warn("请填入正确的手机号");
            }
            return isMatch;
        }
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String replace(String value, String repVal, int start, int end) {
        try {
            value = value.replace(value.substring(start, end), repVal);
        } catch (Exception e) {
            // TODO: handle exception
            logger.warn("replace Exception", e);
        }
        return value;
    }

    public static int getRandNum(int min, int max) {
        int randNum = min + (int) (Math.random() * ((max - min) + 1));
        return randNum;
    }

    public static long getRandNum(long min, long max) {
        long randNum = min + (long) (Math.random() * ((max - min) + 1));
        return randNum;
    }

    public static BigInteger getRandNum(BigInteger min, BigInteger max) {
        BigInteger randNum = min.add(BigDecimal.valueOf((Math.random() * ((max.subtract(min)).add(BigInteger.ONE).longValue()))).toBigInteger());
        return randNum;
    }

    public static BigDecimal getRandNum(BigDecimal min, BigDecimal max) {
        BigDecimal randNum = min.add(BigDecimal.valueOf((Math.random() * ((max.subtract(min)).add(BigDecimal.ONE).longValue()))));
        return randNum;
    }

    public static int getRandNum() {
        int randNum = FinalConfigParam.MIN
                + (int) (Math.random() * ((FinalConfigParam.MAX - FinalConfigParam.MIN) + 1));
        return randNum;
    }
}
