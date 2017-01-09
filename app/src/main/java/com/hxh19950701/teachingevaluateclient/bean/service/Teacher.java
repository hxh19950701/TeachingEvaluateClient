package com.hxh19950701.teachingevaluateclient.bean.service;

public class Teacher  extends TimeMakableRecord {

	private String teacherId; 
	private String name;
	private int sex; 

	public Teacher() {
		
	}

	public Teacher(int id, String teacherId, String name, int sex) {
		super();
		this.id = id;
		this.teacherId = teacherId;
		this.name = name;
		this.sex = sex;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

}