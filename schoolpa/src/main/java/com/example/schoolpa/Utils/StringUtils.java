package com.example.schoolpa.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by admin on 2016/3/17.
 */
public class StringUtils {
    public static String transWeek(String week) {
        if (week.equals("一"))
            week = "1";
        if (week.equals("二"))
            week = "2";
        if (week.equals("三"))
            week = "3";
        if (week.equals("四"))
            week = "4";
        if (week.equals("五"))
            week = "5";
        if (week.equals("六"))
            week = "6";
        if (week.equals("日"))
            week = "7";
        return week;
    }

    public static boolean isEffective(String str) {
        if (str.equals("") || str.equals("Nothing!") || str == null)
            return false;
        return true;
    }

    public static String handlePair(Map<String, String> map) throws UnsupportedEncodingException {
        String result = "";
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            result += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "utf-8");
            result += "&";
        }
        String substring = result.substring(0, result.length() - 1);
        return substring;
    }

}
