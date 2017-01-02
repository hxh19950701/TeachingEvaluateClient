package com.hxh19950701.teachingevaluateclient.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hxh19950701.teachingevaluateclient.R;
import com.hxh19950701.teachingevaluateclient.base.BaseActivity;
import com.hxh19950701.teachingevaluateclient.bean.service.Course;
import com.hxh19950701.teachingevaluateclient.constant.Constant;
import com.hxh19950701.teachingevaluateclient.internet.SimpleServiceCallback;
import com.hxh19950701.teachingevaluateclient.internet.api.CourseApi;
import com.hxh19950701.teachingevaluateclient.utils.MD5Utils;

public class TeacherNewCourseActivity extends BaseActivity {

    TextInputLayout tilCourseName;
    TextInputLayout tilCoursePassword;
    TextInputLayout tilCoursePasswordRetype;
    TextInputLayout tilYear;
    RadioButton rbTerm1;
    RadioButton rbTerm2;
    RadioGroup rgTerm;
    Button btnNewCourse;
    CoordinatorLayout clNewCourse;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_teacher_new_course);
        tilCourseName = (TextInputLayout) findViewById(R.id.tilCourseName);
        tilCoursePassword = (TextInputLayout) findViewById(R.id.tilCoursePassword);
        tilCoursePasswordRetype = (TextInputLayout) findViewById(R.id.tilCoursePasswordRetype);
        tilYear = (TextInputLayout) findViewById(R.id.tilYear);
        rbTerm1 = (RadioButton) findViewById(R.id.rbTerm1);
        rbTerm2 = (RadioButton) findViewById(R.id.rbTerm2);
        rgTerm = (RadioGroup) findViewById(R.id.rgTerm);
        btnNewCourse = (Button) findViewById(R.id.btnNewCourse);
        clNewCourse = (CoordinatorLayout) findViewById(R.id.clNewCourse);
    }

    @Override
    protected void initListener() {
        btnNewCourse.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewCourse:
                newCourse();
                break;
        }
    }

    private void newCourse() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("正在创建").content("请稍后...").cancelable(false)
                .progress(true, 0).progressIndeterminateStyle(false).show();
        String courseName = tilCourseName.getEditText().getText().toString();
        String coursePassword = MD5Utils.encipher(tilCoursePassword.getEditText().getText().toString());
        int year = Integer.parseInt(tilYear.getEditText().getText().toString());
        int term = rbTerm1.isChecked() ? Constant.TERM_FIRST : Constant.TERM_SECOND;
        CourseApi.newCourse(courseName, coursePassword, year, term, new SimpleServiceCallback<Course>(clNewCourse) {
            @Override
            public void onAfter() {
                dialog.dismiss();
            }

            @Override
            public void onGetDataSuccess(Course course) {
                showNewCourseSuccessDialog(course);
            }
        });
    }

    private void showNewCourseSuccessDialog(Course course) {
        new MaterialDialog.Builder(this).title("新建课程成功")
                .content(R.string.newCourseSuccessHint, course.getId(), course.getName(), course.getYear(), course.getTerm() == Constant.TERM_FIRST ? "上学期" : "下学期")
                .positiveText(R.string.close).cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
}
