package com.hxh19950701.teachingevaluateclient.bean.service;

import com.hxh19950701.teachingevaluateclient.base.TimeMakableRecord;

public class TeacherCourseEvaluate  extends TimeMakableRecord {
	
	private int id;
	private Course course;	//��̵Ŀ�
	private EvaluateThirdTarget item;//���۵���Ŀ
	private float score;//����Ŀ��õ�ƽ���

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
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
