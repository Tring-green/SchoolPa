package com.testing.testfunction;

import android.app.Application;
import android.content.Context;

/**
 * Created by admin on 2016/5/27.
 */
public class BaseApplication extends Application {
    private static BaseApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static Context getApplication() {
        return sApplication;
    }
}
