package com.hxh19950701.teachingevaluateclient.bean.response;


public class Course extends TimeMakableRecord {

	private String name;
	private Teacher teacher;
	private int year;
	private int term;
	private String password;
	private int score;
	private int personCount;
	private int evaluatedPersonCount;
	private int totalPersonCount;

	public Course() {
	}

	public Course(String name, Teacher teacher, int year, int term, int totalPersonCount, String password) {
		super();
		this.name = name;
		this.teacher = teacher;
		this.year = year;
		this.term = term;
		this.password = password;
		this.totalPersonCount = totalPersonCount;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getPersonCount() {
		return personCount;
	}

	public void setPersonCount(int personCount) {
		this.personCount = personCount;
	}

	public int getEvaluatedPersonCount() {
		return evaluatedPersonCount;
	}

	public void setEvaluatedPersonCount(int evaluatedPersonCount) {
		this.evaluatedPersonCount = evaluatedPersonCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalPersonCount() {
		return totalPersonCount;
	}

	public void setTotalPersonCount(int totalPersonCount) {
		this.totalPersonCount = totalPersonCount;
	}

	
}
