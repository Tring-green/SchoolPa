package com.testing.testfunction.Lib;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.testing.testfunction.Lib.Callback.ObjectCallback;
import com.testing.testfunction.Utils.SPHttpClass;
import com.testing.testfunction.Utils.StreamUtils;
import com.testing.testfunction.Utils.ToastUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/5/27.
 */
public class SPChatManager {
    private static SPChatManager instance;
    private Context mContext;

    public SPChatManager(Context context) {
        mContext = context;
    }

    public static SPChatManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SPChatManager.class) {
                if (instance == null) {
                    instance = new SPChatManager(context);
                }
            }
        }
        return instance;
    }

    public SPHttpClass sendRequest(String url, Map<String, String> parameters, final ObjectCallback callback) {
        final Map<String, String> params = new HashMap<>();
        if (parameters != null) {
            for (Map.Entry<String, String> me : parameters.entrySet()) {
                params.put(me.getKey(), me.getValue());
            }
        }
        SPHttpClass httpClass = new SPHttpClass(mContext);
        httpClass.startConnection(url, "POST", true, new SPHttpClass.OnVisitingListener
                () {
            @Override
            public void onSuccess(HttpURLConnection conn) {
                try {
                    String string = StreamUtils.getString(conn.getInputStream());
                    System.out.println(string);
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(string);
                    JsonObject root = element.getAsJsonObject();
                    JsonPrimitive flagJson = root.getAsJsonPrimitive("flag");
                    boolean flag = flagJson.getAsBoolean();
                    if (flag) {
                        //注册成功
                        JsonObject dataObject = root
                                .getAsJsonObject("data");
                        if (dataObject != null) {
                            if (callback != null) {
                                Object obj = new Gson().fromJson(dataObject, callback.getDataClass());
                                callback.onSuccess(obj);
                            }
                        }

                    } else {
                        //注册失败
                        JsonPrimitive errorCodeJson = root.getAsJsonPrimitive("errorCode");
                        JsonPrimitive errorMessageJson = root.getAsJsonPrimitive("errorMessage");
                        ToastUtils.showTestShort(mContext, errorCodeJson.getAsInt() + ": " +
                                errorMessageJson.getAsString());
                        if (callback != null)
                            callback.onFailure(errorCodeJson.getAsInt(), errorCodeJson.getAsString());

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSetDetails(HttpURLConnection conn) {
                try {
                    SPHttpClass.sendPostParams(params, conn);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
                if (callback != null)
                    callback.onFailure(SPError.ERROR_SERVER, "服务器连接问题！");
            }

        });
        return httpClass;
    }
}
