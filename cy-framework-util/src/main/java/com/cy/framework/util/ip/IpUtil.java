package com.cy.framework.util.ip;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class IpUtil {
    public static final String IP_HEADERS[] = {"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP", // 优先取其他代理设置的真实用户ip
            "NS-Client-IP", // 淘宝NAT方式设置的IP
            "X-Real-IP",// Tengine设置的remoteIP，此时如果没有取到NS-Client-Ip，那么这就是真实IP
    };

    public static String getClientIp(HttpServletRequest request) {
        for (String ipName : IP_HEADERS) {
            String ip = request.getHeader(ipName);
            if (StringUtils.isNotBlank(ip)) {
                // 对于通过代理的情况，会有多个IP，第一个IP为客户端真实IP,多个IP按照','分割
                String[] ipArr = StringUtils.split(ip, ",");
                return ipArr[0];
            }
        }
        return "";
    }

    /**
     * 获取访问者IP
     * <p>
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * <p>
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("X-Real-IP");
        if ((ip != null && ip.isEmpty()) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if ((ip != null && ip.isEmpty()) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    public static String getIp(HttpServletRequest request) {
        String ip = IpUtil.getClientIp(request);
        try {
            if (ip == null || ip.isEmpty()) {
                ip = getIpAddr(request);
            }
        } catch (Exception e) {
            // TODO: handle exception
            Logger.getLogger(IpUtil.class).warn("getIp Exception", e);
        }
        return ip;
    }

    public static String getServerIp() {
        String SERVER_IP = null;
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                ip = (InetAddress) ni.getInetAddresses().nextElement();
                SERVER_IP = ip.getHostAddress();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {
                    SERVER_IP = ip.getHostAddress();
                    break;
                } else {
                    ip = null;
                }
            }
        } catch (Exception e) {

        }
        LoggerFactory.getLogger(IpUtil.class).warn("this get ip is:" + SERVER_IP);
        return SERVER_IP;
    }

    public static String getIp2(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    public static void main(String[] args) {
        System.out.println(getServerIp());
    }
}
