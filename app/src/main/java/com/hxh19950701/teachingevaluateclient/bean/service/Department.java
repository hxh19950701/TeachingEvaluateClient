package com.hxh19950701.teachingevaluateclient.bean.service;


public class Department extends TimeMakableRecord {

	private String name;
	
	public Department(){
	}
	
	public Department(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}