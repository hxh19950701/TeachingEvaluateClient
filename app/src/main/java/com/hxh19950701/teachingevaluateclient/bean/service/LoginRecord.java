package com.hxh19950701.teachingevaluateclient.bean.service;

import java.sql.Timestamp;

public class LoginRecord{
	private int id; // ��¼ID
	private String ip; // ��¼IP
	private User user; // ��¼�û�
	private Timestamp time; // ��¼ʱ��

	public LoginRecord() {
		// TODO Auto-generated constructor stub
	}

	public LoginRecord(String ip, User user) {
		this.id = 0;
		this.ip = ip;
		this.user = user;
		this.time = new Timestamp(System.currentTimeMillis());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
