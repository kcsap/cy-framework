package com.cy.framework.util.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStorage {
    private static DataStorage dataStorage = null;
    private static Map<String, Object> data = null;

    private DataStorage() {
        data = new ConcurrentHashMap<>();
    }

    public synchronized static DataStorage getInstance() {
        if (dataStorage == null) {
            dataStorage = new DataStorage();
        }
        return dataStorage;
    }

    /**
     * 描述： 添加
     *
     * @param key
     * @param val
     * @return
     * @author yangchengfu
     * @DataTime 2017年6月12日 下午1:52:57
     */
    public Object put(String key, Object val) {
        return data.put(key, val);
    }

    public void putAll(Map<? extends String, ? extends Object> map) {
        data.putAll(map);
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public boolean containsVal(Object val) {
        return data.containsValue(val);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public Object remove(String key) {
        return data.remove(key);
    }

    public Object remove(String key, Object val) {
        return data.remove(key, val);
    }
}
