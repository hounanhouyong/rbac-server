package com.hn.rbac.server.common.context;

import java.util.HashMap;
import java.util.Map;

public class RequestContext {

    private static ThreadLocal<Map<String, Object>> context = new ThreadLocal<>();

    private static Map<String, Object> getContextMap() {
        Map<String, Object> map = context.get();
        if (map == null) {
            map = new HashMap<>();
            context.set(map);
        }
        return map;
    }

    public static Object get(String key) {
        return getContextMap().get(key);
    }

    public static void put(String key, Object value) {
        getContextMap().put(key, value);
    }

    public static void clear() {
        Map<String, Object> ctx = context.get();
        if (ctx != null) {
            ctx.clear();
        }
    }

}
