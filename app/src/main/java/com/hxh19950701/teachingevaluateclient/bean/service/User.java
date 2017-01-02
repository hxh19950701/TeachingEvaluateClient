package com.hxh19950701.teachingevaluateclient.bean.service;

import java.sql.Timestamp;

public class User {

	private int id; // �û�id
	private String username; // �û���
	private String password; // ����MD5
	private int identity; // �û���� 0ѧ�� 1��ʦ 2����Ա
	private Timestamp registerTime; // ע��ʱ��
	private String mark;

	public User() {

	}

	public User(String username, String password, int identity) {
		this.username = username;
		this.password = password;
		this.identity = identity;
		registerTime = new Timestamp(System.currentTimeMillis());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Timestamp getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}

	public int getIdentity() {
		return identity;
	}

	public void setIdentity(int identity) {
		this.identity = identity;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

}
