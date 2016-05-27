package com.testing.testfunction.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 2016/3/16.
 */
public class ToastUtils {
    public static void showTestShort(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showTestLong(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
