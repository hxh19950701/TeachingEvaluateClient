package com.hxh19950701.teachingevaluateclient.utils;

import com.hxh19950701.teachingevaluateclient.common.Constant;

public class CourseUtils {

    private CourseUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static String formatCourseTime(int year, int term) {
        return year + "年 " + (term == Constant.TERM_FIRST ? "上" : "下") + "学期";
    }
}
