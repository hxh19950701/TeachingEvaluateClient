package com.hxh19950701.teachingevaluateclient.bean.service;

import java.sql.Timestamp;

public class StudentCourseInfo {
	private int id;
	private Student student;// ѧ��
	private Course course;// �γ�
	private int score;// ѧ���Ըÿγ��������ܷ�
	private String comment;// ѧ���Ըÿε����ۼ�����
	private Timestamp completeTime;//�������ʱ��
	private String reply;// ��ʦ�Ļظ�
	private Timestamp replyTime;//�ظ�ʱ��
	private String mark;

	public StudentCourseInfo() {

	}

	public StudentCourseInfo(Student student, Course course) {
		super();
		this.student = student;
		this.course = course;
		this.score = -1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Timestamp getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Timestamp completeTime) {
		this.completeTime = completeTime;
	}

	public Timestamp getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Timestamp replyTime) {
		this.replyTime = replyTime;
	}

}
