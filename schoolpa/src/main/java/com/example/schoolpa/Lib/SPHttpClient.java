package com.example.schoolpa.Lib;

import android.app.Activity;
import android.content.Context;

import com.example.schoolpa.Utils.StreamUtils;
import com.example.schoolpa.Utils.ThreadUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by admin on 2016/5/26.
 */
public class SPHttpClient {

    private static final String ERROR = "error";
    private HttpURLConnection conn;
    private static SPHttpClient instance;
    private Context mContext;
    //private SPHttpParams mHttpParams;

    public SPHttpClient(Context context) {
        mContext = context;
    }

    public static SPHttpClient getInstance(Context context) {
        if (instance == null) {
            synchronized (SPHttpClient.class) {
                if (instance == null) {
                    instance = new SPHttpClient(context);
                }
            }
        }
        return instance;
    }

    private HttpURLConnection initClient(String targetUrl, String method, SPHttpParams httpParams, Map<String,
            String> header) throws
            IOException {
        URL url = new URL(targetUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(httpParams.getConnectTimeout());
        conn.setReadTimeout(httpParams.getReadTimeout());
        conn.setDoOutput(httpParams.isDoOutput());
        conn.setDoInput(httpParams.isDoInput());
        if (header != null) {
            sendHeader(conn, header);
        }
        return conn;
    }


    private void doPost(String targetUrl, SPHttpParams httpParams, Map<String, String> headers,
                        Map<String, String> body, final OnVisitingListener listener) {
        try {
            initClient(targetUrl, "POST", httpParams, headers);
                    /* 发送请求并等待响应 */
            if (httpParams.isDoOutput()) {
                sendPostBody(conn, body);
            }
            conn.connect();
                    /* 若状态码为200 ok */
            if (conn.getResponseCode() == 200) {
                if (mContext instanceof Activity) {
                    ThreadUtils.runOnUiThread(mContext, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listener.onSuccess(StreamUtils.getString(conn.getInputStream()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    listener.onSuccess(StreamUtils.getString(conn.getInputStream()));
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
            if (mContext instanceof Activity) {
                ThreadUtils.runOnUiThread(mContext, new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure(e);
                    }
                });
            } else {
                listener.onFailure(e);
            }
        }

    }


    public interface OnVisitingListener {
        void onSuccess(String result);

        void onFailure(IOException e);
    }

    public void startConnection(final String url, final String method, final SPHttpParams httpParams, final
    Map<String, String> header, final Map<String, String> body, final OnVisitingListener listener) {
        ThreadUtils.execute(new ThreadUtils.onStartThreadListener() {
            @Override
            public void onStartThread() {
                switch (method.toUpperCase()) {
                    case "POST":
                        doPost(url, httpParams, header, body, listener);
                        break;
                    case "GET":
                        break;
                    case "PUT":
                        break;
                    case "DELETE":
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void startConnectionNOThread(final String url, final String method, final SPHttpParams httpParams, final
    Map<String, String> header, final Map<String, String> body, final OnVisitingListener listener) {
        switch (method.toUpperCase()) {
            case "POST":
                doPost(url, httpParams, header, body, listener);
                break;
            case "GET":
                break;
            case "PUT":
                break;
            case "DELETE":
                break;
            default:
                break;
        }
    }

    private void sendHeader(HttpURLConnection conn, Map<String, String> header) {
        for (Map.Entry<String, String> me : header.entrySet()) {
            String key = me.getKey();
            String value = me.getValue();
            conn.addRequestProperty(key, value);
        }
    }

    private void sendPostBody(HttpURLConnection conn, Map<String, String> body) throws IOException {
        OutputStream out = conn.getOutputStream();
        String pair = handlePair(body);
        out.write(pair.getBytes());
        out.write("\r\n".getBytes());
        out.flush();
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

    public void disconnect() {
        if (conn != null) {
            conn.disconnect();
            conn = null;

        }
    }

}
