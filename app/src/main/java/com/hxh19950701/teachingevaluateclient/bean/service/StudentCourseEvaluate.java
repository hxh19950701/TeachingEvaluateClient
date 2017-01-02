package com.hxh19950701.teachingevaluateclient.bean.service;

import java.sql.Timestamp;

public class StudentCourseEvaluate {

	private int id;
	private StudentCourseInfo info;//ѧ���γ̶�Ӧ��ϵ
	private EvaluateThirdTarget item;//���۵���Ŀ
	private float score;//���÷���
	private Timestamp time;//���۴����ʱ��
	private String mark;

	public StudentCourseEvaluate() {
		// TODO Auto-generated constructor stub
	}
	
	public StudentCourseEvaluate(StudentCourseInfo info, EvaluateThirdTarget item, float score) {
		this.info = info;
		this.item = item;
		this.score = score;
		this.time = new Timestamp(System.currentTimeMillis());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public StudentCourseInfo getInfo() {
		return info;
	}

	public void setInfo(StudentCourseInfo info) {
		this.info = info;
	}

	public EvaluateThirdTarget getItem() {
		return item;
	}

	public void setItem(EvaluateThirdTarget item) {
		this.item = item;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

}
