import com.alibaba.fastjson.JSONObject;
import com.cy.framework.util.http.CallBack;
import com.cy.framework.util.http.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Response;
import org.junit.Test;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTest {
    @Test
    public void test() throws Exception {
        String url = "http://mm.weizhangwang.com/query_all_car.php";
        String params = "province=%E6%B9%96%E5%8C%97&province_sn=%E6%B9%98&city_sn=A&c_id=169&cphm=P1C73&hpzl=02&enginenumber=679247&classnumber=005258&cityname=%E6%AD%A6%E6%B1%89&areacode=027&city_id=133&province_id=undefined&pid=20&jhcc=FB_WH&id360=169&sourceline=jh&hpzl_text=%E5%B0%8F%E5%9E%8B%E6%B1%BD%E8%BD%A6&km_jgjId=&js_carorg=wuhan&dd_carorg=%E6%AD%A6%E6%B1%89%E5%B8%82&sjb_carorg=wuhan&classlen=6&enginelen=6&refer=m_weizhangwang";
        Map<String, Object> map = new HashMap<>();
        for (String str : params.split("&")) {
            String datas[]=str.split("=");
            map.put(datas[0],datas.length==1?"":datas[1]);
        }
      Response response= HttpClientUtil.executeForm(url, new CallBack<Object>() {
            @Override
            public void success(Object o, List<Object> objects, Call call) {
                System.out.printf(o.toString());
            }

            @Override
            public void error(JSONObject jsonObject, Call call) {
                System.out.printf(jsonObject.toJSONString());
            }

            @Override
            public void failure(Call call, IOException e) {
                System.out.printf(e.getMessage());
            }
        }, map);
        String string=response.body().string();
        System.out.print(string);
    }
    @Test
    public void code() throws Exception{
        String url="http://mm.weizhangwang.com/mysource/getcarjson.php?carIntNO=4311260113070302&carCode=005258&carCodeLen=6&carEngineCode=679247&carEngineCodeLen=6&c_id=169&sourceLine=jh&refer=m_weizhangwang&city=FB_WH&km_jgjId=&js_carorg=wuhan&province=%E5%B9%BF%E4%B8%9C&dd_carorg=%E6%AD%A6%E6%B1%89%E5%B8%82&flag=1&showAll=0&code=000000";
        JSONObject map=new JSONObject();
        String[] params=url.split("\\?")[1].split("&");
        for(String string:params){
            String value[]=string.split("=");
            map.put(value[0],value.length==1? "":URLDecoder.decode(value[1]));
        }
        System.out.println(map.toString());
    }
}
