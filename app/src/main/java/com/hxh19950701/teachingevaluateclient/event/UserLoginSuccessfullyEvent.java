package com.hxh19950701.teachingevaluateclient.event;

public class UserLoginSuccessfullyEvent {

    private String username;
    private String password;
    private boolean isRememberPassword;
    private boolean isAutoLoginEnable;

    public UserLoginSuccessfullyEvent(String username, String password, boolean isRememberPassword, boolean isAutoLoginEnable) {
        this.username = username;
        this.password = password;
        this.isRememberPassword = isRememberPassword;
        this.isAutoLoginEnable = isAutoLoginEnable;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberPassword() {
        return isRememberPassword;
    }

    public void setRememberPassword(boolean rememberPassword) {
        isRememberPassword = rememberPassword;
    }

    public boolean isAutoLoginEnable() {
        return isAutoLoginEnable;
    }

    public void setAutoLoginEnable(boolean autoLoginEnable) {
        isAutoLoginEnable = autoLoginEnable;
    }
}
