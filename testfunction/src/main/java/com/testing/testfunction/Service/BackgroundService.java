package com.testing.testfunction.Service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by admin on 2016/5/27.
 */
public class BackgroundService extends IntentService {

    public BackgroundService() {
        super("background");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
