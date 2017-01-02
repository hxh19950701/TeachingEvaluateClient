package com.hxh19950701.teachingevaluateclient.bean.service;

public class Course {

    private int id;
    private String name;
    private Teacher teacher;
    private int year;
    private int term;
    private String password;
    private int score;
    private int personCount;
    private int evaluatedPersonCount;
    private String mark;


    public Course() {
    }

    public Course(String name, Teacher teacher, int year, int term, String password) {
        super();
        this.name = name;
        this.teacher = teacher;
        this.year = year;
        this.term = term;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
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

}
