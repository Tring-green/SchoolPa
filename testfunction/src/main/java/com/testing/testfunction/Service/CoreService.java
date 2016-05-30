package com.testing.testfunction.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.testing.testfunction.Connector;
import com.testing.testfunction.ConnectorManager;
import com.testing.testfunction.PushReceiver;
import com.testing.testfunction.request.AuthRequest;

/**
 * Created by admin on 2016/5/29.
 */
public class CoreService extends Service implements ConnectorManager.ConnectorListener {

    private Connector mConnector;

    private ConnectorManager mConnectorManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mConnectorManager = ConnectorManager.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mConnectorManager.setConnectorListener(CoreService.this);
                //mConnectorManager.connect("#A");
                AuthRequest request;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    request = new AuthRequest("B", "B");
                } else {
                    request = new AuthRequest("A", "A");
                }
                mConnectorManager.connect(request);
            }
        }).start();
    }

    @Override
    public void pushData(String data) {
        Intent intent = new Intent();
        intent.setAction(PushReceiver.ACTION_TEXT);
        intent.putExtra(PushReceiver.DATA_KEY, data);
        sendBroadcast(intent);
    }
}
