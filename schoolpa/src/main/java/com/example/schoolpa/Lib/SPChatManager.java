package com.example.schoolpa.lib;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.schoolpa.domain.SPError;
import com.example.schoolpa.lib.Callback.SPChat;
import com.example.schoolpa.lib.Callback.SPChatCallBack;
import com.example.schoolpa.lib.Callback.SPFileCallBack;
import com.example.schoolpa.lib.Callback.SPObjectCallBack;
import com.example.schoolpa.lib.Core.AuthRequest;
import com.example.schoolpa.lib.Core.ChatRequest;
import com.example.schoolpa.lib.Core.PacketConnector;
import com.example.schoolpa.lib.Future.HttpFuture;
import com.example.schoolpa.lib.Message.ChatMessage;
import com.example.schoolpa.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.mina.core.session.IoSession;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by admin on 2016/5/27.
 */
public class SPChatManager {
    private static SPChatManager instance;
    private Context mContext;
    private Map<String, String> headers = new HashMap<>();
    private PacketConnector connector;
    private OnPushListener pushListener;
    private List<SPConnectListener> connectListeners = new LinkedList<>();
    private Map<String, ChatRequest> requests = new LinkedHashMap();
    private Thread mainThread;
    private Handler handler = new Handler();
    private String authSequence;

    public SPChatManager(Context context) {
        mContext = context;
        mainThread = Thread.currentThread();
    }

    private SPChatManager() {
        mContext = SPChat.getContext();
        mainThread = Thread.currentThread();
    }

    public static SPChatManager getInstance() {
        if (instance == null) {
            synchronized (SPChatManager.class) {
                if (instance == null) {
                    instance = new SPChatManager();
                }
            }
        }
        return instance;
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
    public HttpFuture register(String account, String password,
                               final SPObjectCallBack callBack) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30 * 1000);
        client.setMaxRetriesAndTimeout(5, 30 * 1000);
        client.setResponseTimeout(30 * 1000);
        String url = SPURL.URL_HTTP_REGISTER;
        RequestParams params = new RequestParams();
        params.put("userId", account);
        params.put("passwd", password);
        Log.d("SPChatManager", "Params have put.");
        return new HttpFuture(client.post(mContext, url, params,
                newObjectResponseHandler(callBack)));
    }
    private TextHttpResponseHandler newObjectResponseHandler(
            final SPObjectCallBack callBack) {
        return new TextHttpResponseHandler("utf-8") {

            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String responseString) {
                Log.d("###", "" + responseString);

                if (statusCode == 200) {
                    JsonParser parser = new JsonParser();
                    JsonObject root = parser.parse(responseString)
                            .getAsJsonObject();
                    if (root == null) {
                        if (callBack != null) {
                            callBack.onError(SPError.ERROR_SERVER, "服务器异常");
                        }
                    } else {
                        if (callBack != null) {
                            JsonPrimitive flagObj = root
                                    .getAsJsonPrimitive("flag");
                            boolean flag = flagObj.getAsBoolean();
                            if (flag) {
                                JsonObject dataObj = root
                                        .getAsJsonObject("data");

                                if (dataObj == null) {
                                    callBack.onSuccess(null);
                                } else {
                                    Object data = new Gson().fromJson(dataObj,
                                            callBack.getClazz());
                                    callBack.onSuccess(data);
                                }

                            } else {
                                // 如果返回错误
                                // 获得错误code
                                JsonPrimitive errorCodeObj = root
                                        .getAsJsonPrimitive("errorCode");
                                // 获得错误string
                                JsonPrimitive errorStringObj = root
                                        .getAsJsonPrimitive("errorString");

                                int errorCode = errorCodeObj.getAsInt();
                                String errorString = errorStringObj
                                        .getAsString();

                                callBack.onError(errorCode, errorString);
                            }
                        }
                    }
                } else {
                    if (callBack != null) {
                        callBack.onError(SPError.ERROR_SERVER, "服务器异常");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                if (callBack != null) {
                    callBack.onError(SPError.ERROR_SERVER, "服务器异常 : "
                            + throwable.getMessage());
                }
            }
        };
    }
    /**
     * 初始化连接用户的安全信息
     *
     * @param userId
     * @param token
     */
    public void initAccount(String userId, String token) {
        headers.put("userId", userId);
        headers.put("token", token);
    }

    public SPHttpClient sendRequest(String url, Map<String, String> header, Map<String, String> body, boolean
            useThread, final
                                    SPObjectCallBack callback) {
        SPHttpClient httpClass = new SPHttpClient(mContext);
        SPHttpParams httpParams = new SPHttpParams(5000, 5000, true);
        httpClass.startConnection(url, "POST", httpParams, header, body, useThread, new SPHttpClient
                .OnVisitingListener() {
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
                            Object obj = new Gson().fromJson(dataObject, callback.getClazz());
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
                        callback.onError(errorCodeJson.getAsInt(), errorCodeJson.getAsString());

                }

            }


            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
                if (callback != null)
                    callback.onError(SPError.ERROR_SERVER, "服务器连接问题！");
            }

        });
        return httpClass;
    }


    /**
     * 添加连接监听
     *
     * @param listener
     */
    public void addConnectionListener(SPConnectListener listener) {
        if (!connectListeners.contains(listener)) {
            connectListeners.add(listener);
        }
    }
    public HttpFuture downloadFile(String path, File file,
                                   final SPFileCallBack callBack) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30 * 1000);
        client.setMaxRetriesAndTimeout(5, 30 * 1000);
        client.setResponseTimeout(30 * 1000);
        String url = SPURL.BASE_HTTP + path;

        for (Map.Entry<String, String> me : headers.entrySet()) {
            client.addHeader(me.getKey(), me.getValue());
        }

        return new HttpFuture(client.get(url, new FileAsyncHttpResponseHandler(
                file) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                callBack.onSuccess(file);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);

                callBack.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, File file) {
                callBack.onError(SPError.ERROR_SERVER,
                        "服务器异常 : " + throwable.getMessage());
            }
        }));
    }

    /**
     * 移除连接监听
     *
     * @param listener
     */
    public void removeConnectionListener(SPConnectListener listener) {
        if (connectListeners.contains(listener)) {
            connectListeners.remove(listener);
        }
    }

    public void setPushListener(OnPushListener listener) {
        this.pushListener = listener;
    }


    private void addRequest(final ChatMessage message,
                            final SPChatCallBack callBack) {
        // 加入到请求map中 为以后的response做处理
        ChatRequest request = new ChatRequest(callBack, message);
        requests.put(request.getSequence(), request);

        connector.addRequest(request);
    }

    public void sendMessage(final ChatMessage message,
                            final SPChatCallBack callBack) {
        new Thread() {
            public void run() {
                if (connector == null) {
                    connector = new PacketConnector(SPURL.BASE_SP_HOST,
                            SPURL.BASE_SP_PORT, 3);
                }

                connectChatServer();

                addRequest(message, callBack);
            }
        }.start();
    }


    private PacketConnector.IOListener ioListener = new PacketConnector.IOListener() {

        @Override
        public void onOutputFailed(ChatRequest request, Exception e) {
            // 消息发送失败，通知回调，让显示层做处理
            SPChatCallBack callBack = request.getCallBack();
            if (callBack != null) {
                callBack.onError(SPError.ERROR_CLIENT_NET, "客户端网络未连接");
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onInputComed(IoSession session, Object message) {
            if (message instanceof String) {
                String json = ((String) message).trim();

                JsonParser parser = new JsonParser();
                JsonObject root = parser.parse(json).getAsJsonObject();

                // 获得方向:是请求还是response
                JsonPrimitive typeJson = root.getAsJsonPrimitive("type");
                String type = typeJson.getAsString();

                // 获得序列号
                JsonPrimitive sequenceJson = root
                        .getAsJsonPrimitive("sequence");
                String sequence = sequenceJson.getAsString();

                if ("request".equalsIgnoreCase(type)) {
                    // 服务器推送消息
                    JsonPrimitive actionJson = root
                            .getAsJsonPrimitive("action");
                    String action = actionJson.getAsString();
                    if (pushListener != null) {
                        boolean pushed = pushListener.onPush(action,
                                (Map<String, Object>) new Gson().fromJson(root,
                                        new TypeToken<Map<String, Object>>() {
                                        }.getType()));
                        if (pushed) {
                            session.write("{type:'response',sequence:'"
                                    + sequence + "',flag:" + true + "}");
                        } else {
                            session.write("{type:'response',sequence:'"
                                    + sequence + "',flag:" + false
                                    + ",errorCode:1,errorString:'客户端未处理成功!'}");
                        }
                    }
                } else if ("response".equalsIgnoreCase(type)) {
                    // 请求返回response
                    JsonPrimitive flagJson = root.getAsJsonPrimitive("flag");
                    boolean flag = flagJson.getAsBoolean();
                    // 消息发送结果只有 成功或者 失败,不需要返回对象
                    if (flag) {
                        if (sequence.equals(authSequence)) {
                            Log.d("#####", "认证成功");
                            return;
                        }

                        // 消息成功发送
                        ChatRequest request = requests.remove(sequence);
                        if (request != null) {
                            final SPChatCallBack callBack = request
                                    .getCallBack();
                            if (callBack != null) {
                                // 在主线程中调用发送消息时的接口onSuccess()方法
                                if (Thread.currentThread() != mainThread) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callBack.onSuccess();
                                        }
                                    });
                                } else {
                                    callBack.onSuccess();
                                }
                            }
                        }
                    } else {
                        if (sequence.equals(authSequence)) {
                            Log.d("#####", "认证失败");
                            return;
                        }

                        // 认证失败
                        ListIterator<SPConnectListener> iterator = connectListeners
                                .listIterator();
                        while (iterator.hasNext()) {
                            final SPConnectListener listener = iterator.next();
                            // 在主线程中调用
                            if (Thread.currentThread() != mainThread) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onAuthFailed();
                                    }
                                });
                            } else {
                                listener.onAuthFailed();
                            }
                        }
                    }
                }
            }
        }
    };
    private PacketConnector.ConnectListener connectListener = new PacketConnector.ConnectListener() {

        @Override
        public void onReConnecting() {
            ListIterator<SPConnectListener> iterator = connectListeners
                    .listIterator();
            while (iterator.hasNext()) {
                SPConnectListener listener = iterator.next();
                listener.onReconnecting();
            }
        }

        @Override
        public void onDisconnected() {
            Log.d("SP", "onDisconnected");

            ListIterator<SPConnectListener> iterator = connectListeners
                    .listIterator();
            while (iterator.hasNext()) {
                SPConnectListener listener = iterator.next();
                listener.onDisconnected();
            }

            authSequence = null;
            connector = null;
        }

        @Override
        public void onConnecting() {
            ListIterator<SPConnectListener> iterator = connectListeners
                    .listIterator();
            while (iterator.hasNext()) {
                SPConnectListener listener = iterator.next();
                listener.onReconnecting();
            }
        }

        @Override
        public void onConnected() {
            ListIterator<SPConnectListener> iterator = connectListeners
                    .listIterator();
            while (iterator.hasNext()) {
                SPConnectListener listener = iterator.next();
                listener.onConnected();
            }
        }
    };


    public interface SPConnectListener {
        /*    *
             * 正在连接
             */
        void onConnecting();

        /*   *
            * 已经连接
            */
        void onConnected();

        /*  *
           * 已经断开连接
           */
        void onDisconnected();

        /* *
          * 正在重试连接
          */
        void onReconnecting();

        /* *
          * 用户认证失败
          */
        void onAuthFailed();
    }


    public interface OnPushListener {
        boolean onPush(String type, Map<String, Object> data);
    }

    /**
     * 认证
     * @param account
     * @param token
     */
    public void auth(final String account, final String token) {
        headers.put("account", account);
        headers.put("token", token);

        new Thread() {
            public void run() {
                AuthRequest request = new AuthRequest(account, token);

                if (connector == null) {
                    //创建连接器
                    connector = new PacketConnector(SPURL.BASE_SP_HOST,
                            SPURL.BASE_SP_PORT, 3);
                }

                connectChatServer();

                authSequence = request.getSequence();

                connector.addRequest(request);

            }

            ;
        }.start();
    }

    private void connectChatServer() {
        if (connector != null) {
            connector.connect();
            // 设置输入输出监听
            connector.setIOListener(ioListener);
            // 设置连接监听
            connector.setConnectListener(connectListener);
        }
    }


}
