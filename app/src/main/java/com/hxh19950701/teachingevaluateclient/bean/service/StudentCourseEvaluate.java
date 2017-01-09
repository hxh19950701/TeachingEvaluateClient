package com.hxh19950701.teachingevaluateclient.bean.service;

public class StudentCourseEvaluate  extends TimeMakableRecord {

	private StudentCourseInfo info;
	private EvaluateThirdTarget item;
	private float score;

	public StudentCourseEvaluate() {
	}
	
	public StudentCourseEvaluate(StudentCourseInfo info, EvaluateThirdTarget item, float score) {
		this.info = info;
		this.item = item;
		this.score = score;
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
