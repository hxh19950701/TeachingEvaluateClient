package com.hxh19950701.teachingevaluateclient.event;

import com.hxh19950701.teachingevaluateclient.bean.response.Course;

public class StudentCommentCourseCompleteEvent {

    private Course course;
    private String comment;

    public StudentCommentCourseCompleteEvent(Course course, String comment) {
        this.course = course;
        this.comment = comment;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
