package com.example.schoolpa.lib.Callback;

public interface SPChatCallBack {

	void onSuccess();

	void onProgress(int progress);

	void onError(int error, String msg);

}
