package com.hxh19950701.teachingevaluateclient.Bean;

/**
 * Created by hxh19950701 on 2016/6/1.
 */
public class LoginBean {

    private boolean success;

    private int identity;

    private int errorCode;

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
