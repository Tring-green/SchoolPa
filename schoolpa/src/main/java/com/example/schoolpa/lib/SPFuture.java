package com.example.schoolpa.lib;

public interface SPFuture {

	boolean isCancelled();

	boolean cancel(boolean mayInterruptIfRunning);

	boolean isFinished();
}
