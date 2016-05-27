package com.example.schoolpa.Utils;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2016/4/21.
 */
public class ThreadUtils {

    private static ExecutorService sExecutorService;

    public interface onStartThreadListener {
        void onStartThread();
    }

    private onStartThreadListener mListener;

    public static void init(int ThreadCount) {
        sExecutorService = Executors.newFixedThreadPool(ThreadCount);
    }

    public static void execute(final onStartThreadListener listener) {
        sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                listener.onStartThread();
            }
        });
    }

    public static void runOnUiThread(Context context, Runnable runnable) {
        if (context instanceof Activity)
            ((Activity) context).runOnUiThread(runnable);
        else
            try {
                throw new IOException();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
