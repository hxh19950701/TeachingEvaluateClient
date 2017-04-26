package com.hxh19950701.teachingevaluateclient.bean.response;

public class StudentCourseInfo extends StringMakableRecord {

    private Student student;
    private Course course;
    private int score;
    private String comment;
    private String completeTime;
    private String reply;
    private String replyTime;

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

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

}
