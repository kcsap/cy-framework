package com.cy.framework.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
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
        String str = null;
        try {
            str = JSONObject.toJSONString(object, filter(keys), SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return str;
    }

    /**
     * 过滤
     *
     * @param keys 需要过滤的属性
     * @return
     */
    private static SerializeFilter filter(String[] keys) {
        List<String> arr = (keys != null && keys.length > 0) ? Arrays.asList(keys) : null;
        PropertyFilter propertyFilter = new PropertyFilter() {
            @Override
            public boolean apply(Object o, String s, Object o1) {
                if (o1 == null || o1.toString().isEmpty() || o1.toString().trim().isEmpty()) {
                    return false;
                }
                if (arr != null && arr.contains(s)) {
                    return false;
                }
                return true;
            }
        };
        return propertyFilter;
    }

}
