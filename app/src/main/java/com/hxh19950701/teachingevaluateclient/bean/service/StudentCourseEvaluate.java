package com.hxh19950701.teachingevaluateclient.bean.service;


import com.hxh19950701.teachingevaluateclient.base.TimeMakableRecord;

public class StudentCourseEvaluate  extends TimeMakableRecord {

	private int id;
	private StudentCourseInfo info;//ѧ��γ̶�Ӧ��ϵ
	private EvaluateThirdTarget item;//���۵���Ŀ
	private float score;//��÷���

	public StudentCourseEvaluate() {
		// TODO Auto-generated constructor stub
	}
	
	public StudentCourseEvaluate(StudentCourseInfo info, EvaluateThirdTarget item, float score) {
		this.info = info;
		this.item = item;
		this.score = score;
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

}
