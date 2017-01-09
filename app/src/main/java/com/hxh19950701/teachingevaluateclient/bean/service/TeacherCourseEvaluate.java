package com.hxh19950701.teachingevaluateclient.bean.service;

public class TeacherCourseEvaluate extends TimeMakableRecord {

    private Course course;
    private EvaluateThirdTarget item;
    private float score;

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
