package com.hxh19950701.teachingevaluateclient.bean.service;


import com.hxh19950701.teachingevaluateclient.base.TimeMakableRecord;

public class Department extends TimeMakableRecord {
	private int id;
	private String name;
	
	public Department(){
	}
	
	public Department(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}