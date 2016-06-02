package com.example.schoolpa.lib.Future;

import com.example.schoolpa.lib.SPFuture;
import com.loopj.android.http.RequestHandle;

public class HttpFuture implements SPFuture {

	private RequestHandle handle;

	public HttpFuture(RequestHandle handle) {
		this.handle = handle;
	}

	public void test() {
		handle.isFinished();
	}

	@Override
	public boolean isCancelled() {
		return handle == null || handle.isCancelled();
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return handle == null || handle.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isFinished() {
		return handle == null || handle.isFinished();
	}
}
