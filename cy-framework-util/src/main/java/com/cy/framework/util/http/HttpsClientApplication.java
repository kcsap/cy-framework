/**
 * copyright to Huihe Intelligence
 *
 * @author zhufei 2016-2-1  下午5:58:26
 * <p>
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\  =  /O
 * ____/`---'\____
 * .'  \\|     |//  `.
 * /  \\|||  :  |||//  \
 * /  _||||| -:- |||||-  \
 * |   | \\\  -  /// |   |
 * | \_|  ''\---/''  |   |
 * \  .-\__  `-`  ___/-. /
 * ___`. .'  /--.--\  `. . __
 * ."" '<  `.___\_<|>_/___.'  >'"".
 * | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ====`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 佛祖保佑       永无BUG
 */
/**
 *                 _ooOoo_
 *                o8888888o
 *                88" . "88
 *                (| -_- |)
 *                O\  =  /O
 *             ____/`---'\____
 *           .'  \\|     |//  `.
 *          /  \\|||  :  |||//  \
 *         /  _||||| -:- |||||-  \
 *         |   | \\\  -  /// |   |
 *         | \_|  ''\---/''  |   |
 *         \  .-\__  `-`  ___/-. /
 *       ___`. .'  /--.--\  `. . __
 *    ."" '<  `.___\_<|>_/___.'  >'"".
 *   | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *   \  \ `-.   \_ __\ /__ _/   .-` /  /
 *====`-.____`-.___\_____/___.-`____.-'======
 *                 `=---='
 *^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *             佛祖保佑       永无BUG
 */
package com.cy.framework.util.http;

import com.alibaba.fastjson.JSON;
import com.cy.framework.util.FinalConfigParam;
import com.cy.framework.util.json.JSONUtils;
import com.cy.framework.util.param.WechatTokenParam;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 含义：http请求的应用
 *
 * @author zhufei 2016-2-1 下午5:58:26
 */
public class HttpsClientApplication {

    private static Logger log = Logger.getLogger(HttpsClientApplication.class);

    /**
     *
     * void descr: 根据地址查询坐标
     *
     * @param address
     * @return String
     * @datatime 2016-11-15 下午9:50:34
     * @author yangchengfu
     */
    public static String getBaiduLocation(String address) {
        BufferedReader in = null;
        String str = null;
        try {
            address = URLEncoder.encode(address, "UTF-8");
            URL tirc = new URL(FinalConfigParam.BAIDUMAP_LOCATIONAPI + address);
            in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            str = sb.toString();
        } catch (Exception e) {
            log.warn("Error to request baidu map api using the address:" + address);
            log.warn(e.getMessage());
        }
        return str;
    }

    /**
     * 方法描述：百度地图根据坐标查询地址
     *
     * @param latitude
     * @return
     * @author zhufei 2014-12-8 下午5:14:04
     */
    public static String getBaiduAddress(double latitude, double longitude) {
        BufferedReader in = null;
        String str = null;
        try {
            URL tirc = new URL(FinalConfigParam.BAIDUMAP_DELOCATEAPI + latitude + "," + longitude);
            in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
            String res = null;
            StringBuilder sb = new StringBuilder();
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            str = sb.toString();
            log.info(str);
        } catch (Exception e) {
            log.warn("Error happened when get baidu address for location:lat=" + latitude + ",lng=" + longitude);
            log.warn(e.getMessage());
        }
        return str;
    }

    /**
     * 方法描述：查询手机号码的相关信息，包括地区与运营商
     *
     * @param mobile
     *            手机号码
     * @return 包含省市与运营商的映射数据
     * @author zhufei 2016-2-3 上午10:28:04
     */
    public static Map<String, String> getPhoneArea(String mobile) {
        Map<String, String> result = new HashMap<String, String>();
        // String url =
        // "http://virtual.paipai.com/extinfo/GetMobileProductInfo?amount=10000"
        // +
        // "&callname=getPhoneNumInfoExtCallback&mobile=";
        // if(CommonUtils.validatePhone(mobile)){
        // url += mobile;
        // }else{
        // return null;
        // }
        // String ret = HttpsClientRequest.httpGet(url, null,"GBK");
        // if(ret != null && !ret.isEmpty() &&
        // ret.contains("{")&&ret.contains("}")){
        // ret = ret.substring(ret.indexOf("{"), ret.indexOf("}")+1);
        // Map<String, Object> map = JSONUtils.json2Map(ret);
        // if(map != null){
        // result = new HashMap<String, String>();
        // if(map.containsKey("province")){
        // result.put("province", map.get("province").toString());
        // }
        // if(map.containsKey("cityname")){
        // result.put("city", map.get("cityname").toString());
        // }
        // if(map.containsKey("isp")){
        // result.put("isp", map.get("isp").toString());
        // }
        // }
        // }
        // return result;
        try {
            String url = "http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi?chgmobile=" + mobile;
            String ret = HttpsClientRequest.httpGet(url, null, "gb2312");
            Map<String, Object> content = new HashMap<>();
            if (content != null) {
                if (content.containsKey("city")) {
                    result.put("city", content.get("city").toString());
                }
                if (content.containsKey("province")) {
                    result.put("province", content.get("province").toString());
                }
            }
        } catch (Exception e) {
            log.warn("Failed to get mobile area,error:" + e.toString() + ",msg:" + e.getMessage());
            result.put("city", "330100");
            result.put("province", "330000");
        }
        return result;
    }

    /**
     *
     * void descr: 获取微信的token
     *
     * @param appid
     * @param secret
     * @return WechatTokenParam
     * @datatime 2016-11-21 上午10:44:18
     * @author yangchengfu
     */
    public static WechatTokenParam getAccessToken(String appid, String secret) {
        String url = null;
        url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret="
                + secret;
        WechatTokenParam param = null;
        try {
            String accessToken = HttpsClientRequest.httpGet(url, null, "utf8");
            if (accessToken != null && accessToken.length() > 0) {
                param = JSON.parseObject(accessToken, WechatTokenParam.class);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return param;
    }

    /**
     * 方法描述：根据 token与openid来获取用户信息
     *
     * @param token
     * @param openid
     * @return
     * @author zhufei 2016-5-30 下午4:46:20
     */
    public static Map<String, Object> getWechatUserInfo(String token, String openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid=" + openid
                + "&lang=zh_CN";
        String userInfo = HttpsClientRequest.httpGet(url, null, "utf8");
        Map<String, Object> map = null;
        if (userInfo != null) {
            map = JSONUtils.json2Map(userInfo);
        }
        return map;
    }

    /**
     * 方法描述：通过微信的授权，将长链接变成短链接
     *
     * @param token
     * @param url
     * @return
     * @author zhufei 2016-5-30 下午4:41:06
     */
    public static Map<String, Object> urlLong2short(String token, String url) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("action", "long2short");
        map.put("long_url", url);
        JSONObject json = JSONObject.fromObject(map);
        String js = json.toString();
        url = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=" + token;
        Map<String, Object> retMap = null;
        try {
            String result = HttpsClientRequest.post(url, js, null, null);
            if (result != null && !result.isEmpty()) {
                retMap = JSONUtils.json2Map(result);
            }
        } catch (Exception e) {
            log.warn("Failed to transfer long url to short url,error:" + e.toString() + ",msg:" + e.getMessage());
        }
        return retMap;
    }
}
