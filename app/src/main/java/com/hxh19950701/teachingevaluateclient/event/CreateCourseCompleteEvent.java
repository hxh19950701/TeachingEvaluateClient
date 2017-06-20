package com.hxh19950701.teachingevaluateclient.event;

import com.hxh19950701.teachingevaluateclient.bean.response.Course;

public class CreateCourseCompleteEvent {

    private Course course;

    public CreateCourseCompleteEvent(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
