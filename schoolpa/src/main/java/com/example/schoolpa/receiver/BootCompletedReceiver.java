package com.example.schoolpa.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.schoolpa.service.ChatCoreService;
import com.example.schoolpa.utils.CommonUtil;

public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 开机启动服务
        if (!CommonUtil.isServiceRunning(context, ChatCoreService.class)) {
            context.startService(new Intent(context, ChatCoreService.class));
        }
    }
}
