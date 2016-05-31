package com.example.schoolpa.Lib.Callback;

import java.io.File;

public abstract class SPFileCallBack {

	public abstract void onSuccess(File file);

	public abstract void onProgress(int writen, int total);

	public abstract void onError(int error, String msg);

}
