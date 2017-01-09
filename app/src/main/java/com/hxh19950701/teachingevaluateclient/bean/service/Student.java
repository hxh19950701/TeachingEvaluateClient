package com.hxh19950701.teachingevaluateclient.bean.service;


public class Student extends TimeMakableRecord {

	private String studentId; // ѧ��
	private String name; // ����
	private int sex; // 0 Ů 1 ��
	private Clazz clazz; // ���ڰ༶

	public Student() {

	}

	public Student(int id, String studentId, String name, int sex, Clazz clazz) {
		this.id = id;
		this.studentId = studentId;
		this.name = name;
		this.sex = sex;
		this.clazz = clazz;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
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

	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}
}
