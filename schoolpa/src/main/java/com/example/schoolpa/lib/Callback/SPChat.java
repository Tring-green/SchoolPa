package com.example.schoolpa.lib.Callback;

import android.content.Context;
import android.util.Log;

public class SPChat {
	private static SPChat instance;
	private static Context context;

	public static SPChat getInstance() {
		if (instance == null) {
			synchronized (SPChat.class) {
				instance = new SPChat();
			}
		}
		return instance;
	}

	public static Context getContext() {
		if (SPChat.context == null) {
			throw new RuntimeException(
					"请在Application的onCreate方法中调用SPChat.getInstance().init(context)初始化聊天引擎.");
		}
		return SPChat.context;
	}

	public void init(Context context) {
		SPChat.context = context;
		Log.d("SPChat", "init");
	}

}