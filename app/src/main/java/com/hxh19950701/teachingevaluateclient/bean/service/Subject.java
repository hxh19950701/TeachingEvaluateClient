package com.hxh19950701.teachingevaluateclient.bean.service;

public class Subject {
	private int id;
	private Department department;
	private String name;
	private String mark;
	
	public Subject(){
		this.department = null;
		this.name = "δ����רҵ";
	}
	
	public Subject(Department department, String name) {
		this.department = department;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
}
