package com.hxh19950701.teachingevaluateclient.bean.service;

public class Clazz {
	private int id;				//�༶ID
	private String name;		//�༶����
	private int year;			//�����꼶
	private Subject subject;	//�༶רҵ
	private Teacher teacher;	//������
	private String mark;
	
	public Clazz(){ 
		
	}
	
	public Clazz(String name, int year, Subject subject, Teacher teacher) {
		this.name = name;
		this.subject = subject;
		this.teacher = teacher;
		this.year = year;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

}
