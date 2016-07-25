package com.hxh19950701.teachingevaluateclient.Bean;

import java.util.List;

/**
 * Created by hxh19950701 on 2016/7/2.
 */
public class CourseListBean {

    private boolean success;

    private List<StudentCourseInfo> courseList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<StudentCourseInfo> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<StudentCourseInfo> courseList) {
        this.courseList = courseList;
    }

}
