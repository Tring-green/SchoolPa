package com.example.schoolpa.Bean;

/**
 * Created by admin on 2016/5/26.
 */
public class HttpRegister {

    private String userId;
    private String passwd;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public HttpRegister(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "HttpRegister{" +
                "userId='" + userId + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }
}
