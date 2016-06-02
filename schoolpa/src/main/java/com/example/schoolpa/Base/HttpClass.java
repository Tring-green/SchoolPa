package com.example.schoolpa.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 2016/3/2.
 */
public class HttpClass {
    private HttpURLConnection conn;
    public boolean isPost = false;

    public void setConnection(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        listener.onSetDetails(conn);
        conn.connect();
    }

    public void setConnection(String fileUrl, String method) throws IOException {
        URL url = new URL(fileUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        listener.onSetDetails(conn);
        conn.connect();
    }

    public void setConnection(String fileUrl, String method, boolean DoOutPut) throws IOException {
        URL url = new URL(fileUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setDoOutput(DoOutPut);
        conn.setDoInput(DoOutPut);
        listener.onSetDetails(conn);
        //conn.connect();
    }

    OnVisitingListener listener;

    public interface OnVisitingListener {
        void onSuccess(HttpURLConnection conn);

        void onSetDetails(HttpURLConnection conn);

        void onFail(IOException e);
    }

    public HttpClass setOnVisitingListener(OnVisitingListener listener) {
        this.listener = listener;
        return this;
    }

    public void startConnenction(String url) {
        try {
            if (isPost) {
            } else
                setConnection(url);
            if (conn.getResponseCode() == 200) {
                listener.onSuccess(conn);
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            listener.onFail(e);
        }
    }

    public void beginConnection(String url, String method, boolean DoOutput) {
        try {
            setConnection(url, method.toUpperCase(), DoOutput);

            if (conn.getResponseCode() == 200) {
                listener.onSuccess(conn);
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            listener.onFail(e);
        }
    }

    public void setRequestProperties(Map<String, String> map) {
        String key;
        String value;
        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            key = it.next();
            value = map.get(key);
            conn.setRequestProperty(key, value);
        }
    }

    public void setRequestBody(String body) {
        try {
            PrintWriter writer = new PrintWriter(conn.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRequestProperty(String type, String value) {
        conn.setRequestProperty(type, value);
    }

    public static HttpClass newInstance() {
        return new HttpClass();
    }



}
