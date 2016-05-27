package com.testing.testfunction.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.testing.testfunction.Utils.CommonUtil;

/**
 * Created by admin on 2016/5/27.
 */
public class ConnectedReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (CommonUtil.isNetConnected(context)) {

            }
        }
    }
}
