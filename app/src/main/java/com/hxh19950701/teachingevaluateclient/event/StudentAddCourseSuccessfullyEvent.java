package com.hxh19950701.teachingevaluateclient.event;

import com.hxh19950701.teachingevaluateclient.bean.response.StudentCourseInfo;

public class StudentAddCourseSuccessfullyEvent {

    private StudentCourseInfo studentCourseInfo;

    public StudentAddCourseSuccessfullyEvent(StudentCourseInfo studentCourseInfo) {
        this.studentCourseInfo = studentCourseInfo;
    }

    public StudentCourseInfo getStudentCourseInfo() {
        return studentCourseInfo;
    }

    public void setStudentCourseInfo(StudentCourseInfo studentCourseInfo) {
        this.studentCourseInfo = studentCourseInfo;
    }
}
