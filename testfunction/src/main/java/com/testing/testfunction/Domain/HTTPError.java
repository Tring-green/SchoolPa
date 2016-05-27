package com.testing.testfunction.Domain;

/**
 * Created by admin on 2016/5/26.
 */
public class HTTPError {
    boolean flag = false;
    final String errorCode;
    final String errorMessage;

    public HTTPError(int errorCode, String errorMessage) {
        this.errorCode = errorCode + "";
        this.errorMessage = errorMessage;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
