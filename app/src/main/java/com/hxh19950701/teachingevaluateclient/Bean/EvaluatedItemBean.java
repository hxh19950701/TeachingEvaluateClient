package com.hxh19950701.teachingevaluateclient.Bean;

import java.util.List;

/**
 * Created by hxh19950701 on 2016/7/13.
 */
public class EvaluatedItemBean {
    private boolean success;
    private List<StudentCourseEvaluate> item;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<StudentCourseEvaluate> getItem() {
        return item;
    }

    public void setItem(List<StudentCourseEvaluate> item) {
        this.item = item;
    }
}
