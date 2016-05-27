package com.example.schoolpa.Lib;

import android.content.Context;

import com.example.schoolpa.Utils.StringUtils;
import com.example.schoolpa.Utils.ThreadUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by admin on 2016/5/26.
 */
public class SPHttpClass {

    private HttpURLConnection conn;
    private static SPHttpClass instance;
    private Context mContext;

    public SPHttpClass(Context context) {
        mContext = context;
    }

    public static SPHttpClass getInstance(Context context) {
        if (instance == null) {
            synchronized (SPHttpClass.class) {
                if (instance == null) {
                    instance = new SPHttpClass(context);
                }
            }
        }
        return instance;
    }


    public void setConnection(final String targetUrl, final String method, final boolean shouldPut) throws
            IOException {
        URL url = new URL(targetUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setDoOutput(shouldPut);
        conn.setDoInput(shouldPut);
        listener.onSetDetails(conn);
    }

    OnVisitingListener listener;

    public interface OnVisitingListener {
        void onSuccess(HttpURLConnection conn);

        void onSetDetails(HttpURLConnection conn);

        void onFailure(IOException e);
    }

    public SPHttpClass startConnection(final String url, final String method, final boolean DoOutput, final OnVisitingListener listener) {
        ThreadUtils.execute(new ThreadUtils.onStartThreadListener() {
            @Override
            public void onStartThread() {
                SPHttpClass.this.listener = listener;
                try {
                    setConnection(url, method.toUpperCase(), DoOutput);
                    if (conn.getResponseCode() == 200) {
                        ThreadUtils.runOnUiThread(mContext, new Runnable() {
                            @Override
                            public void run() {
                                listener.onSuccess(conn);
                            }
                        });
                    } else {
                        throw new IOException();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(mContext, new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure(e);
                        }
                    });
                }
            }
        });
        return this;
    }


    public static void sendPostParams(Map<String, String> map, HttpURLConnection conn) throws IOException {
        OutputStream out = conn.getOutputStream();
        String pair = StringUtils.handlePair(map);
        System.out.println(pair);
        out.write(pair.getBytes());
        out.write("\r\n".getBytes());
        out.flush();
    }

    public  void disconnect() {
        if (conn != null) {
            conn.disconnect();
        }
    }
}
