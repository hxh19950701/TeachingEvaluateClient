package com.hxh19950701.teachingevaluateclient.bean.service;

public class Subject extends TimeMakableRecord {
	private int id;
	private Department department;
	private String name;
	
	public Subject(){
	}
	
	public Subject(Department department, String name) {
		this.department = department;
		this.name = name;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
