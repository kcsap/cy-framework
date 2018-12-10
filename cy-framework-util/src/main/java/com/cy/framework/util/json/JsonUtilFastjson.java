package com.cy.framework.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cy.framework.util.FinalConfigParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JsonUtilFastjson {
    private static Logger logger = LoggerFactory.getLogger(JsonUtilFastjson.class);

    /**
     * 对象转json 字符串
     *
     * @param object 需要转换的对象
     * @param keys   需要过滤的key
     * @return
     */
    public static String toJSONString(Object object, String... keys) {
        return toJSONString(object, false, keys);
    }

    public static String toJSONString(Object object, boolean is, String... keys) {
        String str = null;
        try {
            if (object instanceof String) {
                return object.toString();
            }
            SerializeConfig serializeConfig = new SerializeConfig();
            serializeConfig.put(Date.class, new DataFormat());
            str = JSONObject.toJSONString(object, serializeConfig, filter(keys, is), SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return str;
    }

    /**
     * 公用处理
     *
     * @param keys
     * @param is   过滤的字段=false? 需要的字段=true
     * @return
     */
    private static SerializeFilter filter(String[] keys, boolean is) {
        List<String> arr = (keys != null && keys.length > 0) ? Arrays.asList(keys) : null;
        PropertyFilter propertyFilter = new PropertyFilter() {
            @Override
            public boolean apply(Object o, String s, Object o1) {
                if (o1 == null || o1.toString().isEmpty() || o1.toString().trim().isEmpty()) {
                    return false;
                }
                if (arr != null && arr.contains(s)) {
                    return is;
                }
                return !is;
            }
        };
        return propertyFilter;
    }

    private static class DataFormat implements ObjectSerializer {

        private final String pattern;

        public DataFormat() {
            pattern = FinalConfigParam.DATETIME_FORMAT_STYLE;
        }

        public DataFormat(String pattern) {
            this.pattern = pattern;
        }

        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            if (object == null) {
                serializer.out.writeNull();
            } else {
                Date date = (Date) object;
                SimpleDateFormat format = new SimpleDateFormat(this.pattern);
                format.setTimeZone(FinalConfigParam.TIME_ZONE);
                String text = format.format(date);
                serializer.write(text);
            }
        }
    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "张三");
        jsonObject.put("age", 19);
        System.out.println(toJSONString(jsonObject, false, "name"));
    }
}
