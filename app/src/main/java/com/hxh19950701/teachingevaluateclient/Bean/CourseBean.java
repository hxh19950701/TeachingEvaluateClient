package com.hxh19950701.teachingevaluateclient.Bean;

/**
 * Created by hxh19950701 on 2016/7/13.
 */
public class CourseBean {
    private boolean success;
    private Course course;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
