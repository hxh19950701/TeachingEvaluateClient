package com.hxh19950701.teachingevaluateclient.bean.service;

import java.sql.Timestamp;

public class StudentCourseInfo  extends StringMakableRecord {

	private Student student;
	private Course course;
	private int score;
	private String comment;
	private Timestamp completeTime;
	private String reply;
	private Timestamp replyTime;

	public StudentCourseInfo() {

	}

	public StudentCourseInfo(Student student, Course course) {
		super();
		this.student = student;
		this.course = course;
		this.score = -1;
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
