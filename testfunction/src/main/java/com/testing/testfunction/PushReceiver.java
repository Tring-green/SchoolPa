package com.testing.testfunction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class PushReceiver extends BroadcastReceiver {

    public static String ACTION_TEXT = "com.example.action.text";

    public static String DATA_KEY = "data";

    public PushReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
