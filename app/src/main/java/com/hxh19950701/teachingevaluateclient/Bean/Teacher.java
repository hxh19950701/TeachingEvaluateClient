package com.hxh19950701.teachingevaluateclient.Bean;

public class Teacher {
	
	private int id; // ��ʦ��Ӧ���û���ʶ��
	private String teacherId; // ����
	private String name; // ����
	private int sex; // 0 Ů 1 ��
	private String mark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

}
