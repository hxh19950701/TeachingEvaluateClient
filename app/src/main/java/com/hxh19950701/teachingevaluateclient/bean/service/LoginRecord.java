package com.hxh19950701.teachingevaluateclient.bean.service;

import java.sql.Timestamp;

public class LoginRecord extends IdRecord {

    private String ip; // ��¼IP
    private User user; // ��¼�û�
    private Timestamp time; // ��¼ʱ��

    public LoginRecord() {
    }

    public LoginRecord(String ip, User user) {
        this.ip = ip;
        this.user = user;
        this.time = new Timestamp(System.currentTimeMillis());
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
