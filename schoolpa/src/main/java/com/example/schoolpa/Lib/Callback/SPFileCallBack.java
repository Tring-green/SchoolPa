package com.example.schoolpa.lib.Callback;

import java.io.File;

public abstract class SPFileCallBack {

	public abstract void onSuccess(File file);

	public abstract void onProgress(long writen, long total);

	public abstract void onError(int error, String msg);

}
