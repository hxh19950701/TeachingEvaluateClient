package com.hxh19950701.teachingevaluateclient.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.constant.Constant;

public class StudentCommentActivity extends BaseActivity {

    public static Intent newIntent(Context context, int courseId) {
        if (courseId < 0) {
            throw new IllegalArgumentException("Course id can not be negative : " + courseId);
        }
        Intent intent = new Intent(context, EvaluateActivity.class);
        intent.putExtra(Constant.KEY_COURSE_ID, courseId);
        return intent;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_student_comment);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {

    }
}
