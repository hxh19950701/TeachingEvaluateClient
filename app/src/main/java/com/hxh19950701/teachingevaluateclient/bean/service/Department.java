package com.hxh19950701.teachingevaluateclient.bean.service;

public class Department {
	private int id;
	private String name;
	private String mark;
	
	public Department(){
		this.name = "δ����ϵ��";
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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	
}