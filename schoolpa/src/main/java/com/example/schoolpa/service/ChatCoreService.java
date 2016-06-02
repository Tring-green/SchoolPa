package com.example.schoolpa.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import com.example.schoolpa.action.Action;
import com.example.schoolpa.domain.Account;
import com.example.schoolpa.lib.SPChatManager;
import com.example.schoolpa.R;
import com.example.schoolpa.utils.CommonUtil;
import com.example.schoolpa.db.AccountDao;

import java.util.HashMap;
import java.util.Map;

/**
 * 核心服务：与服务器进行连接，并监听Action
 */
public class ChatCoreService extends Service implements SPChatManager.SPConnectListener, SPChatManager.OnPushListener {

    private SPChatManager chatManager;
    private AccountDao accountDao;
    private int reconnectCount = 0;// 重连次数
    private Map<String, Action> actionMaps = new HashMap<>();

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (CommonUtil.isNetConnected(ChatCoreService.this)) {
                    // 网络已经连接
                    connectServer();
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Core", "onCreate");
        accountDao = new AccountDao(this);
        chatManager = SPChatManager.getInstance(this);
        chatManager.addConnectionListener(this);
        chatManager.setPushListener(this);

        // 注册网络监听
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);

        scanClass();
    }

    private void scanClass() {
        String[] array = getResources().getStringArray(R.array.actions);

        if (array == null) {
            return;
        }

        String packageName = getPackageName();
        ClassLoader classLoader = getClassLoader();

        for (int i = 0; i < array.length; i++) {
            try {

                Class<?> clazz = classLoader.loadClass(packageName + "."
                        + array[i]);

                Class<?> superclass = clazz.getSuperclass();

                if (superclass != null
                        && Action.class.getName().equals(superclass.getName())) {

                    Action action = (Action) clazz.newInstance();
                    actionMaps.put(action.getAction(), action);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void connectServer() {
        Account account = accountDao.getCurrentAccount();
        if (account != null) {
            chatManager.auth(account.getUserId(), account.getToken());

            // 后台服务开启
            startService(new Intent(this, BackgroundService.class));
        }
    }

    public ChatCoreService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnecting() {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {
        Log.d("Core", "onDisconnected");

        if (CommonUtil.isNetConnected(ChatCoreService.this)) {
            // 有网络的
            Log.d("Core", "网络断开重连");
            reconnectCount++;

            if (reconnectCount < 10) {
                connectServer();
            }
        }
    }

    @Override
    public void onReconnecting() {

    }

    @Override
    public void onAuthFailed() {

    }

    @Override
    public boolean onPush(String action, Map<String, Object> data) {
        Log.d("Core", "action : " + action + " data : " + data);
        Action actioner = actionMaps.get(action);
        if (actioner != null) {
            actioner.doAction(this, data);
        }

        return true;
    }
}
