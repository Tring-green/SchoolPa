package com.example.schoolpa.Lib;

import android.content.Context;

import com.example.schoolpa.Bean.SPError;
import com.example.schoolpa.Lib.Callback.ObjectCallback;
import com.example.schoolpa.Utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
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

    public SPHttpClient sendRequest(String url, Map<String, String> header, Map<String, String> body, final
    ObjectCallback callback) {
        SPHttpClient httpClass = new SPHttpClient(mContext);
        SPHttpParams httpParams = new SPHttpParams(5000, 5000, true);
        httpClass.startConnection(url, "POST", httpParams, header, body, new SPHttpClient.OnVisitingListener() {
            @Override
            public void onSuccess(String result) {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result);
                JsonObject root = element.getAsJsonObject();
                JsonPrimitive flagJson = root.getAsJsonPrimitive("flag");
                boolean flag = flagJson.getAsBoolean();
                if (flag) {
                    //注册成功
                    JsonObject dataObject = root.getAsJsonObject("data");
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

    public SPHttpClient sendRequestNoThread(String url, Map<String, String> header, Map<String, String> body, final
    ObjectCallback callback) {
        SPHttpClient httpClass = new SPHttpClient(mContext);
        SPHttpParams httpParams = new SPHttpParams(5000, 5000, true);
        httpClass.startConnectionNOThread(url, "POST", httpParams, header, body, new SPHttpClient.OnVisitingListener() {
            @Override
            public void onSuccess(String result) {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result);
                JsonObject root = element.getAsJsonObject();
                JsonPrimitive flagJson = root.getAsJsonPrimitive("flag");
                boolean flag = flagJson.getAsBoolean();
                if (flag) {
                    //注册成功
                    JsonObject dataObject = root.getAsJsonObject("data");
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
