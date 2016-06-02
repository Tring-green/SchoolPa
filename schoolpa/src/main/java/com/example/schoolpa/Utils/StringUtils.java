package com.example.schoolpa.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    public static String handlePair(Map<String, String> body) throws UnsupportedEncodingException {
        String result = "";
        for (Map.Entry<String, String> me : body.entrySet()) {
            String key = me.getKey();
            String value = me.getValue();
            result += key + "=" + URLEncoder.encode(value, "utf-8") + "&";
        }
        return result.substring(0, result.length() - 1);
    }

}
