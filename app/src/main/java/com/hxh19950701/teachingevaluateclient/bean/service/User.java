package com.hxh19950701.teachingevaluateclient.bean.service;

import com.hxh19950701.teachingevaluateclient.base.TimeMakableRecord;

public class User extends TimeMakableRecord {

	private int id;
	private String username;
	private String password;
	private int identity;
	
	public User() {

	}

	public User(String username, String password, int identity) {
		this.username = username;
		this.password = password;
		this.identity = identity;
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

	public int getIdentity() {
		return identity;
	}

	public void setIdentity(int identity) {
		this.identity = identity;
	}
}
