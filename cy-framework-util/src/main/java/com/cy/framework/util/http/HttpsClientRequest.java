package com.cy.framework.util.http;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

/**
 * 含义：http客户端请求对象
 *
 * @author zhufei 2014-8-28 下午5:51:04
 */
public class HttpsClientRequest {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(HttpsClientRequest.class);

    static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    }

    private static Logger log = Logger.getLogger(HttpsClientRequest.class);

    /**
     * @Description: post 请求
     * @author yangchengfu
     * @Date : 2017/7/28 11:26
     */
    public static String post(String url, String json, Map<String, Object> header, String[] filePaths) {
        String result = null;
        System.out.println(json);
        HttpClient client = new DefaultHttpClient();
        HttpPost request;
        try {
            MultipartEntity entityBuild = new MultipartEntity();
            request = new HttpPost(new URI(url));
            if (header != null) {
                Set<String> keys = header.keySet();
                for (String key : keys) {
                    request.setHeader(key, header.get(key).toString());
                }
            }
            if (json != null && filePaths == null) {
                StringEntity s = new StringEntity(json, "utf-8");
                s.setContentType("application/json");
                request.setEntity(s);
            } else if (filePaths != null && json == null) {
                FileBody bin = null;
                for (String path : filePaths) {
                    File file = new File(path);
                    if (file.exists()) {
                        bin = new FileBody(file);
                        entityBuild.addPart("file", bin);
                    }
                }
                request.setEntity(entityBuild);
            } else if (json != null && filePaths != null) {
                FileBody bin = null;
                for (String path : filePaths) {
                    File file = new File(path);
                    if (file.exists()) {
                        bin = new FileBody(file);
                        entityBuild.addPart("file", bin);
                    }
                }
                StringBody sbody = new StringBody(json, "application/json", Charset.forName("utf-8"));
                entityBuild.addPart("parameter", sbody);
                request.setEntity(entityBuild);
            }

            // 设置SSLContext
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{new TrustAnyTrustManager()}, null);

            // 打开连接,要发送的POST请求
            SSLSocketFactory socketFactory = new SSLSocketFactory(sslcontext);
            socketFactory.setHostnameVerifier(new AllowAllHostnameVerifier());
            client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));
            HttpResponse response = client.execute(request);
            int returncode = response.getStatusLine().getStatusCode();
            if (returncode == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, "UTF-8");
            } else {
                logger.warn("requrl=" + url + ",json=" + json + ",resultCode" + returncode);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return result;
    }

    /**
     * 方法描述：get方式请求数据
     *
     * @param url    地址
     * @param header 认证标记
     * @return
     * @author zhufei 2015-12-1 下午2:06:44
     */
    public static String httpsGet(String url, Map<String, Object> header) {
        String result = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet request;
        try {
            request = new HttpGet(new URI(url));
            if (header != null) {
                Set<String> set = header.keySet();
                for (String key : set) {
                    request.setHeader(key, header.get(key).toString());
                }
            }
            // 设置SSLContext
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{new TrustAnyTrustManager()}, null);

            // 要发送的POST请求
            SSLSocketFactory socketFactory = new SSLSocketFactory(sslcontext);
            client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));
            HttpResponse response = client.execute(request);
            int returncode = response.getStatusLine().getStatusCode();
            if (returncode == 200) {
                HttpEntity entity = response.getEntity();
                long contentLen = entity.getContentLength();
                if (contentLen == 0) {
                    return result;
                }
                String charset = EntityUtils.getContentCharSet(entity);
                InputStreamReader isr = null;
                if (charset != null) {
                    isr = new InputStreamReader(entity.getContent(), charset);
                } else {
                    isr = new InputStreamReader(entity.getContent());
                }
                StringBuffer sb = new StringBuffer();
                char[] ct = new char[64];
                int len = 0;
                while ((len = isr.read(ct)) != -1) {
                    String sst = new String(ct);
                    if (len < 64) {
                        sst = sst.substring(0, len).trim();
                    }
                    sb.append(sst);
                }
                result = sb.toString().replace("\\\\", "\\");
                result = result.replace("\\\"", "\"");
                if (result.startsWith("\"")) {
                    result = result.substring(1);
                }
                if (result.endsWith("\"")) {
                    result = result.substring(0, result.length() - 1);
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return result;
    }

    /**
     * 方法描述：http的请求，带有编码格式的数据处理
     *
     * @param httpUrl 请求URL
     * @param header  头部信息
     * @param charset 编码格式
     * @return
     * @author zhufei 2016-2-3 上午10:25:42
     */
    public static String httpGet(String httpUrl, Map<String, Object> header, String charset) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (header != null) {
                Set<String> keys = header.keySet();
                for (String key : keys) {
                    connection.setRequestProperty(key, header.get(key).toString());
                }
            }
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, charset));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String xmlPost(String url, Object xmlObj) throws Exception {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        // 连接超时时间，默认10秒
        int socketTimeout = 10000;
        // 传输超时时间，默认30秒
        int connectTimeout = 30000;
        // HTTP请求器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 根据默认超时限制初始化requestConfig
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout).build();
        // 解决XStream对出现双下划线的bug
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        // 将要提交给API的数据对象转换成XML格式数据Post给API
        String postDataXML = xStreamForRequestPostData.toXML(xmlObj);
        log.warn("API，POST过去的数据是：" + postDataXML);
        // 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(postDataXML.toString(), "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);
        // 设置请求器的配置
        httpPost.setConfig(requestConfig);
        log.warn("executing request" + httpPost.getRequestLine());
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (ConnectionPoolTimeoutException e) {
            log.error("http get throw ConnectionPoolTimeoutException(wait time out)," + e.getMessage());
        } catch (ConnectTimeoutException e) {
            log.error("http get throw ConnectTimeoutException," + e.getMessage());
        } catch (SocketTimeoutException e) {
            log.error("http get throw SocketTimeoutException," + e.getMessage());
        } catch (Exception e) {
            log.error("http get throw Exception," + e.getMessage());
        } finally {
            httpPost.abort();
        }
        return result;
    }
}
