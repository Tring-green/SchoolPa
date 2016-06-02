package com.example.schoolpa.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author keane
 */
public class JsonUtils {
    private String string;

    public JsonUtils(String s) {
        string = s;
    }

    public String getObj(String key) {
        String find = RegexUtils.RegexGroup(string, key + "\":\"(.+?)\"", 1);
        if (find.equals("Nothing!"))
            return null;
        return find;
    }

    public static String str = "";

    public static String key(String key) {
        return null;
    }

    public static String surround(String key, Object value) {
        if (value instanceof String)
            return "\"" + key + "\":" + "\"" + value + "\"";
        else if (value instanceof List<?>) {
            String str = "\"" + key + "\":" + jsonbegin(1);
            List<?> list = (List<?>) value;
            str += list.get(0);
            for (int i = 1; i < list.size(); i++)
                str += comma() + list.get(i);
            str += jsonbegin(3);
            return str;
        } else
            return null;
    }

    public static String surround(Object obj) {
        if (obj instanceof String)
            return "{\"" + obj + "\"}";
        return null;
    }

    public static String comma() {
        return ",";
    }

    public static String jsonbegin(int type) {
        switch (type) {
            case 0:
                return "{";
            case 1:
                return "[";
            case 2:
                return "}";
            case 3:
                return "]";
            default:
                return null;
        }
    }

    public static String jsonend(int type) {
        switch (type) {
            case 0:
                return "}";
            case 1:
                return "]";
            default:
                return null;
        }
    }

    public static String toJson(Map<String, ?> map, int type) {
        String json = jsonbegin(0);
        String key;
        Object value;
        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        if (it.hasNext()) {
            key = it.next();

            if (type == 0) {
                value = ((String) map.get(key)).replaceAll("<small>", "").replaceAll("</small>",
                        "").replaceAll(" ",
                        "");

            } else
                value = (List<?>) map.get(key);
            json += surround(key, value);
        }
        while (it.hasNext()) {
            key = it.next();
            json += comma();
            if (type == 0) {

                value = ((String) map.get(key)).replaceAll("<small>", "").replaceAll("</small>",
                        "").replaceAll(" ",
                        "");
            } else
                value = (List<?>) map.get(key);
            json += surround(key, value);
        }
        json += jsonbegin(2);
        return json;
    }

    public static String toJson(Object obj) {
        if (obj instanceof String)
            return surround(obj);
        if (obj instanceof List<?>) {
            String json = jsonbegin(0);
            Object value;
            Iterator<?> it = ((List<?>) obj).iterator();
            if (it.hasNext()) {
                value = it.next();
                if (value instanceof String) {
                    json += value;
                }
            }
            while (it.hasNext()) {
                value = it.next();
                if (value instanceof String) {
                    json += "," + value;
                }
            }
            json += jsonend(0);
            return json;
        }
        return null;
    }
}
