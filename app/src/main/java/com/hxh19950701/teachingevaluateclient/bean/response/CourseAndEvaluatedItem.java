package com.hxh19950701.teachingevaluateclient.bean.response;

import java.util.List;

public class CourseAndEvaluatedItem {

    private Course course;
    private List<StudentCourseEvaluate> item;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<StudentCourseEvaluate> getItem() {
        return item;
    }

    public void setItem(List<StudentCourseEvaluate> item) {
        this.item = item;
    }
}
