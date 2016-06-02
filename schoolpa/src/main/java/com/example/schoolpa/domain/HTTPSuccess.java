package com.example.schoolpa.domain;

/**
 * Created by admin on 2016/5/29.
 */
public class HTTPSuccess {
    private boolean flag;

    public HTTPSuccess(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
