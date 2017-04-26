package com.hxh19950701.teachingevaluateclient.bean.response;

public class LoginRecord extends IdRecord {

    private String ip; // ��¼IP
    private User user; // ��¼�û�
    private String time; // ��¼ʱ��

    public LoginRecord() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}